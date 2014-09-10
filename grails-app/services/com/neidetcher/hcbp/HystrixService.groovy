package com.neidetcher.hcbp

import grails.async.Promise

import com.neidetcher.hcbp.promise.DynamicHystrixCommand
import com.neidetcher.hcbp.promise.HystrixCommandPromiseAdapter
import com.netflix.hystrix.HystrixCommand

/**
 * A service to integrate the Hystrix circuit breaker library into Grails APIs. 
 */
class HystrixService {
	static transactional = false
	
	/**
	 * Wrap a HystrixCommand in a promise.
	 */
	public <T> Promise<T> hystrix(HystrixCommand<T> command) {
		new HystrixCommandPromiseAdapter(command)
	}
	
	/**
	 * Wrap a closure in a promise that is implemented by a HystrixCommand.
	 */
	Promise<?> hystrix(Closure cl) {
		new HystrixCommandPromiseAdapter(DynamicHystrixCommand.create(cl))
	}
	
	/**
	 * Wrap a closure in a promise that is implemented by a HystrixCommand and
	 * provide parameters. See {@link DynamicHystrixCommand}.
	 */
	Promise<?> hystrix(Map<String, Object> params, Closure cl) {
		new HystrixCommandPromiseAdapter(DynamicHystrixCommand.create(params, cl))
	}
}
