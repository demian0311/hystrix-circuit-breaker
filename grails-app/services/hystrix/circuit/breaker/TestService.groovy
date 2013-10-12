package hystrix.circuit.breaker

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import grails.transaction.Transactional

@Transactional
class TestService {

    def String serviceMethod(String stringIn) {
        val result = stringIn.reverse()
        //(new CommandServiceMethod(stringIn)).execute()

        return result
    }
}

class CommandServiceMethod extends HystrixCommand{
    String someState

    public CommandServiceMethod(String stringIn){
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        println("command created")
        someState = stringIn
    }

    @Override def String run(){
        println("command called")
        def multiplier = (Math.random() * 100).toInteger()
        println("multiplier: " + multiplier)

        if(multiplier % 9 == 0){
            throw new RuntimeException("something bad happened")
        } else if(multiplier % 7 == 0){
            Thread.sleep(10 * 1000)
        } else {
            Thread.sleep(multiplier * 10)
        }

        someState.reverse()
    }
}
