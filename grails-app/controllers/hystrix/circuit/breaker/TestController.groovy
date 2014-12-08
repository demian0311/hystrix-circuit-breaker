package hystrix.circuit.breaker

import com.neidetcher.hcbp.util.HystrixConfigurationUtility
import com.netflix.hystrix.*

class TestController {

    def index() {
        String result = (new DodgyStringReverser("FOO")).execute()
        log.info("result: " + result)

        render("""{"result": "${result}" }\n""")
    }

	def hystrixAsPromise() {
		def promise = hystrix(new DodgyStringReverser("FOO"))
		String result = promise.get()
		log.info("result: " + result)
		
		render("""{"result": "${result}" }\n""")
	}
	
	def hystrixClosure() {
		def promise = hystrix { "FOO".reverse() }
		String result = promise.get()
		log.info("result: " + result)
		
		render("""{"result": "${result}" }\n""")
	}
	
	def hystrixClosureWithParams() {
		def promise = hystrix(command: 'reverse', group: 'strings', fallback: 'oof') { throw new IOException() }
		String result = promise.get()
		log.info("result: " + result)
		
		render("""{"result": "${result}" }\n""")
	}

    def hystrixAsPromiseAndBadThings() {
        def promise = hystrix(new DodgyStringReverser("FOO", true))
        String result = promise.get()
        log.info("result: " + result)

        render("""{"result": "${result}" }\n""")
    }
}

class DodgyStringReverser extends HystrixCommand {
    String someState
    Boolean makeBadThingsHappen = false

    static HystrixCommand.Setter createHystrixCommandSetter(){
        HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(this.class.name))
                .andCommandPropertiesDefaults(
                HystrixConfigurationUtility.createHystrixCommandPropertiesSetter().withCircuitBreakerEnabled(true)
                        .withCircuitBreakerSleepWindowInMilliseconds(1000))
    }

    /**
     * A dodgy string reverser.  Always reverse strings this way to optimize patience with customers.
     * @param stringIn Input string to reverse
     * @param makeBadThingsHappen Set this to true to force exception thrown for testing
     */
    DodgyStringReverser(String stringIn, Boolean makeBadThingsHappen = false){
        super(createHystrixCommandSetter())
        println("command created")
        someState = stringIn
        this.makeBadThingsHappen = makeBadThingsHappen
    }

    @Override
    String run(){
        println("command called")
        int multiplier = Math.random() * 100
        println("multiplier: " + multiplier)

        // indeterminate behavior
        if(this.makeBadThingsHappen){
            throw new RuntimeException("something bad happened")
        }

        if(multiplier % 21 == 0){
            Thread.sleep(10 * 1000)
        } else {
            Thread.sleep(multiplier * 10)
        }

        someState.reverse()
    }
}
