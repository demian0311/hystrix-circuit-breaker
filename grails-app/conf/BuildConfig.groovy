grails.project.work.dir = 'target'

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
    }

    plugins {
        build ':release:3.0.1', ':rest-client-builder:1.0.3', {
            export = false
        }
    }
}
