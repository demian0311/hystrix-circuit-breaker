package com.neidetcher.hcbp.promise

import grails.async.Promise
import grails.test.mixin.*

import java.util.concurrent.TimeUnit

import spock.lang.Specification

import com.neidetcher.hcbp.util.HystrixConfigurationUtility
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import com.netflix.hystrix.HystrixCommand.Setter
import com.netflix.hystrix.exception.HystrixBadRequestException

class HystrixCommandPromiseAdapterSpec extends Specification {
	static class HystrixCommandMock extends HystrixCommand<String> {
		String value
		long delayInMillis
		HystrixCommandMock(String value) {
			super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey('TestGroup'))
					.andCommandKey(HystrixCommandKey.Factory.asKey("TestCommand"))
					.andCommandPropertiesDefaults(
					HystrixConfigurationUtility.createHystrixCommandPropertiesSetter().withCircuitBreakerEnabled(true)
							.withExecutionIsolationThreadTimeoutInMilliseconds(30000)
							.withCircuitBreakerSleepWindowInMilliseconds(10000)))
			this.value = value
		}
		HystrixCommandMock(String value, long delayInMillis) {
			this(value)
			this.delayInMillis = delayInMillis
		}
		String run() {
			if (delayInMillis > 0) {
				Thread.sleep(delayInMillis)
			}
			if (!value) {
				throw new HystrixBadRequestException("value is null")
			} 
			value 
		}
	}
	
	void "get value"() {
		given:'a promise'
		def promise = new HystrixCommandPromiseAdapter(new HystrixCommandMock('ABC'))
		
		when:'promise.get() is called'
		def value = promise.get()
		
		then:'value is returned'
		value == 'ABC'
	}
	
	void "get() with timeout"() {
		given:'promise with delayed response'
		def promise = new HystrixCommandPromiseAdapter(new HystrixCommandMock('ABC', 1000))
		
		when:'promise.get() is called'
		def value = promise.get()
		
		then:'value is returned'
		value == 'ABC'
	}
	
	void "get(timeout,units) beyond timeout"() {
		given:'promise with delayed response'
		def promise = new HystrixCommandPromiseAdapter(new HystrixCommandMock('ABC', 1000))
		
		when:'promise.get() is called'
		def value = promise.get(1200, TimeUnit.MILLISECONDS)
		
		then:'value is returned'
		value == 'ABC'
	}
	
	@spock.lang.Ignore // the Future.get(timeout, units) call isn't throwing TimeoutException
	void "get(timeout,units) within timeout"() {
		given:'promise with delayed response'
		def promise = new HystrixCommandPromiseAdapter(new HystrixCommandMock('ABC', 1000))
		
		when:'promise.get() is called'
		def value = promise.get(100, TimeUnit.MILLISECONDS)
		
		then:'value is returned'
		thrown(Throwable)
	}
	
	void "onComplete and then"() {
		given:'a promise and list to hold the result'
		def promise = new HystrixCommandPromiseAdapter(new HystrixCommandMock('ABC'))
		def result = []
		
		when:'promise has an onComplete handler'
		assert promise
			.onComplete { value -> result << value }
			.then { value -> result << value } == promise
		
		then:'result is produced'
		result == ['ABC','ABC']
	}
	
	void "onError"() {
		given:'promise with an exception thrown'
		def result = []
		def promise = new HystrixCommandPromiseAdapter(new HystrixCommandMock(null))

		when:'promise has an onError handler'
		assert promise
			.onError { Throwable e -> result.add(e) } == promise
			
		then:'exception is produced'
		result
	}
}
