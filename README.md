
## Summary
This plugin gets your application set up to use the Hystrix Circuit Breaker from 
Netflix. 

## Installation
In your `BuildConfig.groovy` file add the following line in the `plugins` section.

```compile ":hystrix-circuit-breaker:0.1"```

## Description

### Overview
This is a very brief introduction to how circuit breakers work with Hystrix.  You 
should totally read up on all that Hystrix has to offer. 

To use Hystrix circuit breakers you need to wrap your code in a HystrixCommand.

Here's an example of a simple controller that uses a HystrixCommand.

      import com.neidetcher.hcbp.util.HystrixConfigurationUtility
      import com.netflix.hystrix.*

      class TestController {

      def index() {
         String result = (new DodgyStringReverser("FOO")).execute()
         log.info("result: " + result)

         render("""{"result": "${result}" }\n""")
      }
      }

      class DodgyStringReverser extends HystrixCommand {
      String someState

      static HystrixCommand.Setter createHystrixCommandSetter(){
         HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(this.class.name))
                  .andCommandPropertiesDefaults(
                  HystrixConfigurationUtility.createHystrixCommandPropertiesSetter().withCircuitBreakerEnabled(true)
                           .withCircuitBreakerSleepWindowInMilliseconds(1000))
      }


      def DodgyStringReverser(String stringIn){
         super(createHystrixCommandSetter())
         println("command created")
         someState = stringIn
      }

      @Override
      def String run(){
         println("command called")
         def multiplier = (Math.random() * 100).toInteger()
         println("multiplier: " + multiplier)

         // indeterminate behavior
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

One thing to note is `HystrixConfigurationUtility.createHystrixCommandPropertiesSetter`
in the `com.neidetcher.hcbp.util` package.  This is a 1-liner function that makes it
easy to create a Setter() that Hystrix has you use to configure their HystrixCommand.

#### Hystrix Stream
Once you do that and your command is run then Hystrix will start streaming information
about how your circuit breakers are doing. 

`http://localhost:8080/your-app/hystrix.stream`

#### Hystrix Dashboard
This is from the [Hystrix Dashboard](https://github.com/Netflix/Hystrix/wiki/Dashboard#run-via-gradle)
documentation.

``` 
$ git clone git@github.com:Netflix/Hystrix.git
$ cd Hystrix/hystrix-dashboard
$ ../gradlew jettyRun
> Building > :hystrix-dashboard:jettyRun > Running at http://localhost:7979/hystrix-dashboard
```
Once running, open http://localhost:7979/hystrix-dashboard.

When that comes up you will be presented with a form for a URL to give Hystrix Dashboard. 
Enter the stream URL from above, probably something like this: http://localhost:8080/hystrix-circuit-breaker/hystrix.stream

#### Hystrix Turbine
You don't need Turbine for your little application but if you are running in an
enterprise and especially if you have a cluster of servers you will probably want
to use the Hystrix Turbine project to consolidate the circuit breaker stream 
information.

TODO: link to Turbine

### Background

### Configuration




You should see your stream available at this URL when your application is running:
http://localhost:8080/hystrix-circuit-breaker/hystrix.stream

But you still need to point an instance of the Hystrix Dashboard to your stream.
Here are instructions to quickly get that going.

