package hystrix.circuit.breaker

import com.netflix.config.ConfigurationManager
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandProperties
import static com.netflix.hystrix.HystrixCommand.Setter
import static com.netflix.hystrix.HystrixCommandGroupKey.Factory

class TestController {


    def index() {
        //ConfigurationManager.getConfigInstance().setProperty("hystrix.threadpool.default.coreSize", 8);
        //ConfigurationManager.configInstance.addProperty(
        //        "hystrix.command.DodgyStringReverser.execution.isolation.thread.timeoutInMilliseconds", 8)

        String result = (new DodgyStringReverser("FOO")).execute()
        log.info("result: " + result)

        render("""{"result": "${result}" }\n""")
    }
}

import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class DodgyStringReverser extends HystrixCommand {
    String someState

    static com.netflix.hystrix.HystrixCommandProperties.Setter createHystrixCommandPropertiesSetter(){
        HystrixCommandProperties.invokeMethod("Setter", null)
    }

    static com.netflix.hystrix.HystrixCommand.Setter createHystrixCommandSetter(){
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(this.class.name))
                .andCommandPropertiesDefaults(createHystrixCommandPropertiesSetter().withCircuitBreakerEnabled(true).withCircuitBreakerSleepWindowInMilliseconds(1000))
    }


    def DodgyStringReverser(String stringIn){
        super(createHystrixCommandSetter())
        //super(HystrixCommandGroupKey.Factory.asKey(this.getClass().name))

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