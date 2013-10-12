package hystrix.circuit.breaker

import grails.test.mixin.TestFor

@TestFor(TestService)
class TestServiceTests {
    def testServiceMethod(){
        println("service: " + service)
        assertEquals("oof", service.serviceMethod("foo"))
    }
}
