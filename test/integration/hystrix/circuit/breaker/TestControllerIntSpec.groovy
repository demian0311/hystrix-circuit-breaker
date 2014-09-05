package hystrix.circuit.breaker

import grails.test.spock.IntegrationSpec;
import groovy.util.GroovyTestCase

class TestControllerIntSpec extends IntegrationSpec {

	void "hystrixAsPromise() produces command result"() {
		when:'Controller hystrixAsPromise() is called'
		def controller = new TestController()
		controller.hystrixAsPromise()
		
		then:'Hystrix produced string is available'
		controller.response.contentAsString == '{"result": "OOF" }\n'
	}

	void "hystrixClosure() produces command result"() {
		when:'Controller hystrixAsPromise() is called'
		def controller = new TestController()
		controller.hystrixClosure()
		
		then:'Hystrix produced string is available'
		controller.response.contentAsString == '{"result": "OOF" }\n'
	}

	void "hystrixClosureWithParams() produces command result"() {
		when:'Controller hystrixAsPromise() is called'
		def controller = new TestController()
		controller.hystrixClosureWithParams()
		
		then:'Hystrix produced string is available'
		controller.response.contentAsString == '{"result": "oof" }\n'
	}
}
