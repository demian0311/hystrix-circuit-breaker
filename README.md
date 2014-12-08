
## Summary
This plugin gets your application set up to use the 
[Hystrix Circuit Breaker](https://github.com/Netflix/Hystrix) from [Netflix](http://netflix.com).  
If you don't know what circuit breakers are in the context of software
here's [the Wikipedia page](https://en.wikipedia.org/wiki/Circuit_breaker_design_pattern).
You should also read [Release It!](http://pragprog.com/book/mnee/release-it).

## Installation
In your `BuildConfig.groovy` file add the following line in the `plugins` section.

```compile ":hystrix-circuit-breaker:0.5"```

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

#### Grails 2.3 and Promise API Support

Grails 2.3 introduced asynchronous support using the [Promise API](http://grails.org/doc/latest/guide/async.html).
The purpose and design of Hystrix works well into this architecture. Grails provides async support in several areas
using this API in which Hystrix is useful. This plugin adds dynamic methods into controller and service classes to
wrap an existing HystrixCommand implementation in a Promise or create a HystrixCommand from a closure and wrap it in a Promise.
This makes using Hystrix with async programming terse and enables use with Grails async utilities such as PromiseList and PromiseMap.

Examples are in [TestController.groovy](https://github.com/demian0311/hystrix-circuit-breaker/blob/master/grails-app/controllers/hystrix/circuit/breaker/TestController.groovy).

Wrap an existing HystrixCommand in a controller or service:
```groovy
def index() {
		def promise = hystrix(new DodgyStringReverser("FOO")) // DodgyStringReverser is a HystrixCommand implementation
		render view:'index', model: tasks( one: promise )
}
```

Create a Promise implemented by Hystrix:
```groovy
def index() {
		def promise = hystrix { "FOO".reverse() }
		render view:'index', model: tasks( one: promise )
}
```

The previous example will use defaults for the command which may not be desirable. It is better to specify at least the command and group keys:
```groovy
def index() {
		def promise = hystrix(command: 'reverse', group: 'strings') { "FOO".reverse() }
		render view:'index', model: tasks( one: promise )
}
```

The full options available with the hystrix method are:
<dl>
<dt>group</dt><dd>(String) group key</dd>
<dt>command</dt><dd>(String) name of the command</dd>
<dt>threadpool</dt><dd>(String) thread pool key</dd>
<dt>cacheKey</dt><dd>(String) cache key</dd>
<dt>fallback</dt><dd>(Closure or Object) fallback value or closure</dd>
<dt>timeout</dt><dd>(long in millis) execution isolation timeout</dd>
</dl>

#### Hystrix Stream
Once you have all that, you fire up your Grails application and your command is run then 
Hystrix will start streaming information about how your circuit breakers are doing. 

If you point your web browser to this then you will see a bunch of JSON.
`http://localhost:8080/your-app/hystrix.stream`

#### Configuration
Hystrix can be configured in Config.groovy in the 'hystrix' configuration tree. See [Hystrix Configuration](https://github.com/Netflix/Hystrix/wiki/Configuration)
for details.

```groovy
hystrix {
    threadpool {
        'default' {
            coreSize = 100
        }
    }
}
```

#### Hystrix Dashboard
To look at a cool spark chart and other data about your circuit breakers you need to use the Hystrix Dashboard. It
is available in your application at /hystrixMonitor/index. Ensure '/hystrix.stream' is allowed for the correct user
role(s).

#### Hystrix Turbine
If you have a cluster of servers you will want to use the Hystrix Turbine project to consolidate the circuit breaker stream 
information. If Turbine is configured in Config.groovy then the dashboard will consolidate information from all application instances. Read
the Turbine configuration at [Netflix Turbine](https://github.com/Netflix/Turbine/wiki/Configuration-(1.x)). The dashboard
is still accessed at /hystrixMonitor/index. When multiple clusters are configured links will be provided for each cluster.
'/hystrix.stream' will need to be permitted without authentication, although you may restrict to IP address in the subnet(s)
of your servers. Ensure '/turbine.stream' is allowed for the correct user role(s).

```groovy
turbine {
    instanceUrlSuffix = ':8080/MyApp/hystrix.stream'
    
    discovery {
        config { // ConfigPropertyBasedDiscovery
            default_ = ['server1','server2'] // default is a reserved word, this becomes 'default'
            cluster2 = ['server3','server4']
        }
        
        OR
        
        file = '/path/to/cluster.txt' // FileBasedInstanceDiscovery, clusters are read from the file
        
        OR
        
        eureka = ['asgname1','asgname2'] // EurekaInstanceDiscovery
        
        OR
        
        other {
            impl = 'com.netflix.turbine.discovery.OtherInstanceDiscovery'
            default_ {
                prop1 = 'value1' // becomes "turbine.OtherInstanceDiscovery.default.prop1 = value1"
                prop2 = 'value2' // becomes "turbine.OtherInstanceDiscovery.default.prop2 = value2"
            }
            cluster2 {
                prop1 = 'value3' // becomes "turbine.OtherInstanceDiscovery.cluster2.prop1 = value3"
                prop2 = 'value4' // becomes "turbine.OtherInstanceDiscovery.cluster2.prop2 = value4"
            }
        }
    }
}
```
