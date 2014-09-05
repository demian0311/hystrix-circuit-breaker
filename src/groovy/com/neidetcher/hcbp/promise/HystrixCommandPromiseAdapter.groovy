package com.neidetcher.hcbp.promise

import grails.async.Promise

import java.util.concurrent.TimeUnit

import rx.Observable
import rx.Observer

import com.netflix.hystrix.HystrixCommand

class HystrixCommandPromiseAdapter<T> implements Promise<T> {
	final HystrixCommand<T> command
	private Observable<T> observable
	
	HystrixCommandPromiseAdapter(HystrixCommand command) {
		this.command = command
	}
	
	@Override
	public T get() throws Throwable {
		command.execute()
	}

	@Override
	public T get(long timeout, TimeUnit units) throws Throwable {
		command.queue().get(timeout, units)
	}

	private Observable<T> observe() {
		if (observable == null) {
			observable = command.observe()
		}
		observable
	}
	
	@Override
	public Promise<T> onComplete(Closure callable) {
		observe().subscribe([ onCompleted: {},
			onError: {},
			onNext: { value -> callable.call(value) }
			] as Observer<T>)
		this
	}

	@Override
	public Promise<T> onError(Closure callable) {
		observe().subscribe([ onCompleted: {},
			onError: { Throwable e -> callable.call(e) },
			onNext: {}
			] as Observer<T>)
		this
	}

	@Override
	public Promise<T> then(Closure callable) {
		return onComplete(callable)
	}
}
