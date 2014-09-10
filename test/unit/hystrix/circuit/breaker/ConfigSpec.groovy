package hystrix.circuit.breaker

import org.apache.commons.configuration.MapConfiguration;

import spock.lang.Specification

import com.neidetcher.hcbp.util.HystrixConfigurationUtility
import com.netflix.config.ConfigurationManager

class ConfigSpec extends Specification {

	private org.apache.commons.configuration.Configuration processConfig(String configStr) {
		MapConfiguration mapConfig = new MapConfiguration(new Properties())
		ConfigSlurper slurper = new ConfigSlurper()
		def config = slurper.parse(configStr)
		def application = ['config':config]
		HystrixConfigurationUtility.configureHystrix(application, mapConfig)
		mapConfig
	} 
	
	def cleanup() {
		HystrixMonitorController.clusters.clear()
	}
	
	void 'hystrix config'() {
		when:'thread pool config is specified'
		def config = processConfig(
"""
hystrix {
    threadpool {
        'default' {
            coreSize = 100
        }
    }
}
""")
		
		then:'configuration is set properly'
		config.getString("hystrix.threadpool.default.coreSize") == '100'
	}
	
	void 'turbine not configured'() {
		when:'thread pool config is specified'
		def config = processConfig(
"""
hystrix {
    threadpool {
        'default' {
            coreSize = 100
        }
    }
}
""")
		
		then:'configuration is set properly'
		config.getString("turbine.aggregator.clusterConfig") == null
	}
	
	void 'turbine config discovery'() {
		when:'turbine config discovery is specified'
		def config = processConfig(
"""
turbine {
    instanceUrlSuffix = ':8080/MyApp/hystrix.stream'
    
    discovery {
        config {
            default_ = ['server1','server2']
            cluster2 = ['server3','server4']
        }
	}
}
""")
		
		then:'properties are set properly'
		config.getString("InstanceDiscovery.impl") == 'com.netflix.turbine.discovery.ConfigPropertyBasedDiscovery'
		config.getString("turbine.instanceUrlSuffix") == ':8080/MyApp/hystrix.stream'
		config.getList("turbine.aggregator.clusterConfig") == ['default','cluster2']
		config.getList("turbine.ConfigPropertyBasedDiscovery.default.instances") == ['server1','server2']
		config.getList("turbine.ConfigPropertyBasedDiscovery.cluster2.instances") == ['server3','server4']
	}
	
	void 'turbine file discovery'() {
		when:'turbine file discovery is specified'
		def config = processConfig(
"""
turbine {
    instanceUrlSuffix = ':8080/MyApp/hystrix.stream'
    
    discovery {
		file = 'test/unit/hystrix/circuit/breaker/filediscovery.txt'
	}
}
""")
		
		then:'properties are set properly'
		config.getString("InstanceDiscovery.impl") == 'com.netflix.turbine.discovery.FileBasedInstanceDiscovery'
		config.getString("turbine.instanceUrlSuffix") == ':8080/MyApp/hystrix.stream'
		config.getString("turbine.FileBasedInstanceDiscovery.filePath") == 'test/unit/hystrix/circuit/breaker/filediscovery.txt'
		config.getList("turbine.aggregator.clusterConfig") == ['weather-svc-prod','weather-svc-test','weather-svc-canary']
	}
	
	void 'turbine eureka discovery'() {
		when:'turbine eureka discovery is specified'
		def config = processConfig(
"""
turbine {
    instanceUrlSuffix = ':8080/MyApp/hystrix.stream'
    
    discovery {
		eureka = ['asgname1','asgname2']
	}
}
""")
		
		then:'properties are set properly'
		config.getString("InstanceDiscovery.impl") == 'com.netflix.turbine.discovery.EurekaInstanceDiscovery'
		config.getString("turbine.instanceUrlSuffix") == ':8080/MyApp/hystrix.stream'
		config.getList("turbine.aggregator.clusterConfig") == ['asgname1','asgname2']
	}
	
	void 'turbine other discovery'() {
		when:'turbine custom discovery is specified'
		def config = processConfig(
"""
turbine {
    instanceUrlSuffix = ':8080/MyApp/hystrix.stream'
    
	discovery {
	    other {
	        impl = 'com.netflix.turbine.discovery.OtherInstanceDiscovery'
	        default_ {
	            prop1 = 'value1'
	            prop2 = 'value2'
	        }
	        cluster2 {
	            prop1 = 'value3'
	            prop2 = 'value4'
	        }
	    }
	}
}
""")
		
		then:'properties are set properly'
		config.getString("InstanceDiscovery.impl") == 'com.netflix.turbine.discovery.OtherInstanceDiscovery'
		config.getString("turbine.instanceUrlSuffix") == ':8080/MyApp/hystrix.stream'
		config.getList("turbine.aggregator.clusterConfig") == ['default','cluster2']
		config.getString("turbine.OtherInstanceDiscovery.default.prop1") == 'value1'
		config.getString("turbine.OtherInstanceDiscovery.default.prop2") == 'value2'
		config.getString("turbine.OtherInstanceDiscovery.cluster2.prop1") == 'value3'
		config.getString("turbine.OtherInstanceDiscovery.cluster2.prop2") == 'value4'
	}
}
