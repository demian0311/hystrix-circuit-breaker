package hystrix.circuit.breaker

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import grails.transaction.Transactional

@Transactional
class TestService {

    def serviceMethod(String stringIn) {
        val result = stringIn.reverse()
        //(new CommandServiceMethod(stringIn)).execute()

        return result
    }
}
