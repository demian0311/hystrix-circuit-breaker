
## Summary
This plugin gets your application ready to use the Hystrix Circuit Breaker from 
Netflix.  

## Installation

## Description

### Overview

### Background

### Configuration



You should see your stream available at this URL when your application is running:
http://localhost:8080/hystrix-circuit-breaker/hystrix.stream

But you still need to point an instance of the Hystrix Dashboard to your stream.
Here are instructions to quickly get that going.

## Starting Hystrix Dashboard from Gradle 
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

