package hystrix.circuit.breaker

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandProperties

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

        //super(new HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(50))

        // HystrixCommandProperties.Setter()
        //.withExecutionIsolationThreadTimeoutInMilliseconds(int value)


        //super(HystrixCommandProperties.Setter()
        //        .withCircuitBreakerEnabled(true)
        //        .withCircuitBreakerErrorThresholdPercentage(50)
        //        .withCircuitBreakerSleepWindowInMilliseconds(1000))

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