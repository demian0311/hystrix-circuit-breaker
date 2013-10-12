package hystrix.circuit.breaker

class TestController {
    def testService

    def index() {
        log.info("*****************")
        log.info("*****************")

        log.info("testService: " + testService)
        log.info("testService: " + testService)

        String lastResult = testService.serviceMethod("FOO")
        log.info("lastResult: " + lastResult)

        render(""" {
            "result": "${lastResult}"
            }
        """)
    }
}
