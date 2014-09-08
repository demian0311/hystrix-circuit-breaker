modules = {
	'hystrix-monitor' {
		dependsOn 'jquery'
		
		resource url: [dir: 'css', file: 'global.css', plugin: 'hystrix-circuit-breaker']
		resource url: [dir: 'css', file: 'monitor.css', plugin: 'hystrix-circuit-breaker']
		resource url: [dir: 'components/hystrixCommand', file: 'hystrixCommand.css', plugin: 'hystrix-circuit-breaker']
		resource url: [dir: 'components/hystrixThreadPool', file: 'hystrixThreadPool.css', plugin: 'hystrix-circuit-breaker']
			
		resource url: [dir: 'js', file: 'd3.v2.min.js', plugin: 'hystrix-circuit-breaker']
		resource url: [dir: 'js', file: 'jquery.tinysort.min.js', plugin: 'hystrix-circuit-breaker']
		resource url: [dir: 'js', file: 'tmpl.js', plugin: 'hystrix-circuit-breaker']
		resource url: [dir: 'components/hystrixCommand', file: 'hystrixCommand.js', plugin: 'hystrix-circuit-breaker']
		resource url: [dir: 'components/hystrixThreadPool', file: 'hystrixThreadPool.js', plugin: 'hystrix-circuit-breaker']
	}
}