package com.neidetcher.hcbp.util

import com.netflix.hystrix.HystrixCommandProperties
import hystrix.circuit.breaker.HystrixMonitorController
import org.apache.commons.configuration.Configuration

class HystrixConfigurationUtility {
    static HystrixCommandProperties.Setter createHystrixCommandPropertiesSetter() {
        HystrixCommandProperties.invokeMethod("Setter", null)
    }

    private static void applyConfigToHystrixProperties(Properties props, Configuration config) {
        props?.each { key, value ->
            config.setProperty(key, value)
        }
    }

    /**
     * Configure Hystrix and (optionally) Turbine from the application config.
     */
    static void configureHystrix(def application, Configuration configuration) {
        applyConfigToHystrixProperties(application.config.hystrix?.toProperties('hystrix'), configuration)

        if (application.config.turbine) {
            def turbineProps = application.config.turbine?.toProperties('turbine')

            // InstanceDiscovery isn't prefixed by 'turbine' and we want to provide a richer config
            def discovery = application.config.turbine.discovery
            if (discovery) {
                if (discovery.config) {
                    turbineProps['InstanceDiscovery.impl'] = 'com.netflix.turbine.discovery.ConfigPropertyBasedDiscovery'
                    discovery.config.flatten().each { key, value ->
                        if (key == 'default_') {
                            key = 'default'
                        }
                        turbineProps['turbine.ConfigPropertyBasedDiscovery.' + key + '.instances'] = value.join(',')
                        HystrixMonitorController.clusters << key
                    }

                } else if (discovery.file) {
                    turbineProps['InstanceDiscovery.impl'] = 'com.netflix.turbine.discovery.FileBasedInstanceDiscovery'
                    def path = discovery.file as String
                    turbineProps['turbine.FileBasedInstanceDiscovery.filePath'] = path
                    def clusters = []
                    new File(path).eachLine { line ->
                        def split = line.split(',')
                        if (split.length >= 3) {
                            clusters << split[1]
                        }
                    }
                    HystrixMonitorController.clusters.addAll(clusters.unique())

                } else if (discovery.eureka) {
                    turbineProps['InstanceDiscovery.impl'] = 'com.netflix.turbine.discovery.EurekaInstanceDiscovery'
                    if (discovery.eureka instanceof List) {
                        HystrixMonitorController.clusters.addAll(discovery.eureka)
                    } else {
                        HystrixMonitorController.clusters << discovery.eureka as String
                    }

                } else if (discovery.other) {
                    turbineProps['InstanceDiscovery.impl'] = discovery.other.impl
                    def simpleName = discovery.other.impl.split('\\.').last()
                    discovery.other.each { cluster, props ->
                        if (cluster == 'default_') {
                            cluster = 'default'
                        }
                        if (cluster != 'impl') {
                            props.flatten().each { key, value ->
                                turbineProps['turbine.' + simpleName + '.' + cluster + '.' + key] = value
                            }
                            HystrixMonitorController.clusters << cluster
                        }
                    }

                }
            }
            turbineProps['turbine.aggregator.clusterConfig'] = HystrixMonitorController.clusters.join(',') ?: 'default'

            applyConfigToHystrixProperties(turbineProps, configuration)
        }
    }
}
