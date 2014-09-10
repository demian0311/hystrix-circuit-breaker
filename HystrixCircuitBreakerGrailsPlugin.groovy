import com.neidetcher.hcbp.util.HystrixConfigurationUtility
import com.netflix.config.ConfigurationManager
import com.netflix.turbine.init.TurbineInit

class HystrixCircuitBreakerGrailsPlugin {
    def version = "0.3"
    def grailsVersion = "2.3 > *"
    def pluginExcludes = [
        "grails-app/controllers/hystrix/circuit/breaker/TestController.groovy"
    ]
    def title = "Hystrix Circuit Breaker Plugin"
    def description = 'Hystrix is awesome, this is an attempt to make it easy to use for a Grails application'
    def documentation = "http://grails.org/plugin/hystrix-circuit-breaker"

    def license = "APACHE"
    def developers = [
        [name: 'Demian Neidetcher', email: 'demian0311@gmail.com']
    ]
    def issueManagement = [system: 'GITHUB', url: 'https://github.com/demian0311/hystrix-circuit-breaker/issues']
    def scm = [url: 'https://github.com/demian0311/hystrix-circuit-breaker']

    def doWithWebDescriptor = { xml ->
        def mappingElement = xml.'servlet-mapping'

        def lastMapping = mappingElement[mappingElement.size() - 1]
        lastMapping + {
            'servlet' {
                'servlet-name'("HystrixMetricsStreamServlet")
                'display-name'("HystrixMetricsStreamServlet")
                'servlet-class'("com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet")
                'description'("")
            }
            'servlet-mapping' {
                'servlet-name'("HystrixMetricsStreamServlet")
                'url-pattern'("/hystrix.stream")
            }
        }
		
		if (application.config.turbine) {
			lastMapping + {
				'servlet' {
					'servlet-name'("TurbineStreamServlet")
					'display-name'("TurbineStreamServlet")
					'servlet-class'("com.netflix.turbine.streaming.servlet.TurbineStreamServlet")
					'description'("")
				}
				'servlet-mapping' {
					'servlet-name'("TurbineStreamServlet")
					'url-pattern'("/turbine.stream")
				}
			}
			
			def listeners = xml.'listener'
			def lastListener = listeners[listeners.size() - 1]
			lastListener + {
				'listener' {
					'listener-class'('hystrix.circuit.breaker.TurbineContextListener')
				}
			}
		}
    }

    def doWithApplicationContext = { applicationContext -> 
		HystrixConfigurationUtility.configureHystrix(application, ConfigurationManager.getConfigInstance()) 
	}
}
