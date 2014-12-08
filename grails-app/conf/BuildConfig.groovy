grails.project.work.dir = 'target'

grails.project.fork = [
        test: false,
        run: false
]

grails.project.dependency.resolver = 'maven' // or ivy
grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        runtime 'com.netflix.hystrix:hystrix-core:1.3.7',
                'com.netflix.hystrix:hystrix-metrics-event-stream:1.3.7'

        runtime("com.netflix.turbine:turbine-core:0.4") {
            excludes("servlet-api", "junit", "mockito-all")
        }
    }

    plugins {
        compile(":asset-pipeline:2.0.15"){
            export = false
        }
        build ":tomcat:7.0.52.1"
        build (':release:3.0.1', ':rest-client-builder:1.0.3') {
            export = false
        }
        runtime ":jquery:1.11.1"
    }
}
