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
        runtime 'com.netflix.hystrix:hystrix-core:1.4.5',
                'com.netflix.hystrix:hystrix-metrics-event-stream:1.4.5'

        runtime("com.netflix.turbine:turbine-core:1.0.0") {
            excludes("servlet-api", "junit", "mockito-all")
        }
    }

    plugins {
        compile(":asset-pipeline:2.1.5"){
            export = false
        }
        build ":tomcat:7.0.54"
        build (':release:3.0.1', ':rest-client-builder:1.0.3') {
            export = false
        }
        runtime ":jquery:1.11.1"
    }
}
