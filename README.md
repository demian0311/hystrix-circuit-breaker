
## Summary
This plugin gets your application set up to use the Hystrix Circuit Breaker from 
Netflix.  If you don't know what circuit breakers are in the context of software
here's [the Wikipedia page](https://en.wikipedia.org/wiki/Circuit_breaker_design_pattern).
You should also read [Release It!](http://pragprog.com/book/mnee/release-it).

## Installation
In your `BuildConfig.groovy` file add the following line in the `plugins` section.

```compile ":hystrix-circuit-breaker:0.1"```

## Description

### Overview
This is a very brief introduction to how circuit breakers work with Hystrix.  You 
should totally read up on all that Hystrix has to offer. 

To use Hystrix circuit breakers you need to wrap your code in a HystrixCommand.
The is a Controller in this project contains a little sample class that extends  
the HystrixCommand.

[TestController.groovy](https://github.com/demian0311/hystrix-circuit-breaker/blob/master/grails-app/controllers/hystrix/circuit/breaker/TestController.groovy)

One thing to note is [`HystrixConfigurationUtility.createHystrixCommandPropertiesSetter`](https://github.com/demian0311/hystrix-circuit-breaker/blob/master/src/groovy/com/neidetcher/hcbp/util/HystrixConfigurationUtility.groovy)
This is a 1-liner function that makes it easy to create a Setter() that Hystrix 
has you use to configure their HystrixCommand.  It's used in the aforementioned
TestController.groovy if you want to see it in action.

#### Hystrix Stream
Once you have all that, you fire up your Grails application and your command is run then 
Hystrix will start streaming information about how your circuit breakers are doing. 

If you point your web browser to this then you will see a bunch of JSON.
`http://localhost:8080/your-app/hystrix.stream`

#### Hystrix Dashboard
Although the stream has lots of data it isn't pretty.  To look at a cool spark chart and
other data about your circuit breakers you need to use the [Hystrix Dashboard](https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard).

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
You don't need [Turbine](https://github.com/Netflix/Turbine) for your little application but if you are running in an
enterprise and especially if you have a cluster of servers you will probably want
to use the Hystrix Turbine project to consolidate the circuit breaker stream 
information.
