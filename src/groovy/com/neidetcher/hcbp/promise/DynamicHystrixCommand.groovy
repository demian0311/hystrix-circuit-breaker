package com.neidetcher.hcbp.promise

import com.neidetcher.hcbp.util.HystrixConfigurationUtility;
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommand.Setter
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Hystrix command created from a closure.
 * The parameters in the 'params' map are:
 * <dl>
 * <dt>group</dt><dd>(String) group key</dd>
 * <dt>command</dt><dd>(String) name of the command</dd>
 * <dt>threadpool</dt><dd>(String) thread pool key</dd>
 * <dt>cacheKey</dt><dd>(String) cache key</dd>
 * <dt>fallback</dt><dd>(Closure or Object) fallback value or closure</dd>
 * <dt>timeout</dt><dd>(long in millis) execution isolation timeout</dd>
 * </dl>
 */
class DynamicHystrixCommand<T> extends HystrixCommand<T> {
	private Closure cl
	private Object fallback
	private String cacheKey
	
	private static Setter mapToSetter(Map<String, Object> params, Closure cl) {
		def group = params.group ?: 'grails'
		def setter = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group))
		setter.andCommandKey(HystrixCommandKey.Factory.asKey(params.command ?: cl.getOwner().toString()))
		if (params.threadpool) {
			setter.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(params.threadpool))
		}
		if (params.timeout) {
			setter.andCommandPropertiesDefaults(HystrixConfigurationUtility.createHystrixCommandPropertiesSetter().
				withExecutionIsolationThreadTimeoutInMilliseconds(params.timeout as int))
		}
		setter
	}
	
	static DynamicHystrixCommand create(Closure cl) {
		create(Collections.emptyMap(), cl)
	}
	
	static DynamicHystrixCommand create(Map<String, Object> params, Closure cl) {
		new DynamicHystrixCommand(params, cl)
	}
	
	DynamicHystrixCommand(Map<String, Object> params, Closure cl) {
		super(mapToSetter(params, cl))
		assert cl
		this.cl = cl
		this.fallback = params.get('fallback')
		this.cacheKey = params.get('cacheKey')
	}
	
	@Override
	protected T run() throws Exception {
		return cl.call();
	}
	
	@Override
	protected T getFallback() {
		if (fallback instanceof Closure) {
			Closure c = fallback
			return c.call()
		} else if (fallback != null) {
			return fallback
		}
		return super.getFallback()
	}
	
	@Override
	protected String getCacheKey() {
		cacheKey ? cacheKey : super.getCacheKey()
	}
}
