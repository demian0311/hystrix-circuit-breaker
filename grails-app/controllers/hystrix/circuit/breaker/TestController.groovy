package hystrix.circuit.breaker

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey

class TestController {

    def index() {
        String result = (new DodgyStringReverser("FOO")).execute()
        log.info("result: " + result)

        render("""{"result": "${result}" }\n""")
    }
}

class DodgyStringReverser extends HystrixCommand{
    String someState

    def DodgyStringReverser(String stringIn){
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        println("command created")
        someState = stringIn
    }

    @Override
    def String run(){
        println("command called")
        def multiplier = (Math.random() * 100).toInteger()
        println("multiplier: " + multiplier)

        if(multiplier % 9 == 0){
            throw new RuntimeException("something bad happened")
        } else if(multiplier % 21 == 0){
            Thread.sleep(10 * 1000)
        } else {
            Thread.sleep(multiplier * 10)
        }

        someState.reverse()
    }
}