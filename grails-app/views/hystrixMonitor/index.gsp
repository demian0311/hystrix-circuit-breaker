<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="hystrixMonitorLayout">
        <title><g:message code="app.name" default="Hystrix Monitor"/></title>
        <r:require module="hystrix-monitor"/>
    </head>
    <body>
        <dfn style="display:none" id="hystrixCircuit">${resource(dir:'components/hystrixCommand/templates', file: 'hystrixCircuit.html', plugin: 'hystrix-circuit-breaker')}</dfn>
        <dfn style="display:none" id="hystrixCircuitContainer">${resource(dir:'components/hystrixCommand/templates', file: 'hystrixCircuitContainer.html', plugin: 'hystrix-circuit-breaker')}</dfn>
        <dfn style="display:none" id="hystrixThreadPool">${resource(dir:'components/hystrixThreadPool/templates', file: 'hystrixThreadPool.html', plugin: 'hystrix-circuit-breaker')}</dfn>
        <dfn style="display:none" id="hystrixThreadPoolContainer">${resource(dir:'components/hystrixThreadPool/templates', file: 'hystrixThreadPoolContainer.html', plugin: 'hystrix-circuit-breaker')}</dfn>
    
    <div id="header">
        <h2><span id="title_name"><g:message code="app.name" default=""/></span></h2>
    </div>

    <div class="container">
        <div class="row">
            <div class="menubar">
                <div class="title">
                Circuit
                </div>
                <div class="menu_actions">
                    Sort: 
                    <a href="javascript://" onclick="hystrixMonitor.sortByErrorThenVolume();">Error then Volume</a> |
                    <a href="javascript://" onclick="hystrixMonitor.sortAlphabetically();">Alphabetical</a> | 
                    <a href="javascript://" onclick="hystrixMonitor.sortByVolume();">Volume</a> | 
                    <a href="javascript://" onclick="hystrixMonitor.sortByError();">Error</a> | 
                    <a href="javascript://" onclick="hystrixMonitor.sortByLatencyMean();">Mean</a> | 
                    <a href="javascript://" onclick="hystrixMonitor.sortByLatencyMedian();">Median</a> | 
                    <a href="javascript://" onclick="hystrixMonitor.sortByLatency90();">90</a> | 
                    <a href="javascript://" onclick="hystrixMonitor.sortByLatency99();">99</a> | 
                    <a href="javascript://" onclick="hystrixMonitor.sortByLatency995();">99.5</a> 
                </div>
                <div class="menu_legend">
                    <span class="success">Success</span> | <span class="shortCircuited">Short-Circuited</span> | <span class="timeout">Timeout</span> | <span class="rejected">Rejected</span> | <span class="failure">Failure</span> | <span class="errorPercentage">Error %</span>
                </div>
            </div>
        </div>
        <div id="dependencies" class="row dependencies"><span class="loading">Loading ...</span></div>
        
        <div class="spacer"></div>
        <div class="spacer"></div>
        
        <div class="row">
            <div class="menubar">
                <div class="title">
                Thread Pools
                </div>
                <div class="menu_actions">
                    Sort: <a href="javascript://" onclick="dependencyThreadPoolMonitor.sortAlphabetically();">Alphabetical</a> | 
                    <a href="javascript://" onclick="dependencyThreadPoolMonitor.sortByVolume();">Volume</a> | 
                </div>
            </div>
        </div>
        <div id="dependencyThreadPools" class="row dependencyThreadPools"><span class="loading">Loading ...</span></div>
    </div>



<g:javascript>
        if (typeof EventSource === "undefined") {
            $('.loading').text('This browser is not supported.')
        } else {
        
        /**
         * Queue up the monitor to start once the page has finished loading.
         * 
         * This is an inline script and expects to execute once on page load.
         */ 
         
         // commands
        var hystrixMonitor = new HystrixCommandMonitor('dependencies', {includeDetailIcon:false});
        
        var stream = '${streamuri}';
        var commandStream = stream;
        var poolStream = stream;
        
        $(window).load(function() { // within load with a setTimeout to prevent the infinite spinner
            setTimeout(function() {
                // sort by error+volume by default
                hystrixMonitor.sortByErrorThenVolume();
                
                // start the EventSource which will open a streaming connection to the server
                var source = new EventSource(commandStream);
                
                // add the listener that will process incoming events
                source.addEventListener('message', hystrixMonitor.eventSourceMessageListener, false);

                //  source.addEventListener('open', function(e) {
                //      console && console.log(">>> opened connection, phase: " + e.eventPhase);
                //      // Connection was opened.
                //  }, false);

                source.addEventListener('error', function(e) {
                  if (e.eventPhase == EventSource.CLOSED) {
                    // Connection was closed.
                      console && console.log("Connection was closed on error: " + e);
                  } else {
                      console && console.log("Error occurred while streaming: " + e);
                  }
                }, false);
            },0);
        });
        
        // thread pool
        var dependencyThreadPoolMonitor = new HystrixThreadPoolMonitor('dependencyThreadPools');

        $(window).load(function() { // within load with a setTimeout to prevent the infinite spinner
            setTimeout(function() {
                dependencyThreadPoolMonitor.sortByVolume();
                
                // start the EventSource which will open a streaming connection to the server
                var source = new EventSource(poolStream);
                
                // add the listener that will process incoming events
                source.addEventListener('message', dependencyThreadPoolMonitor.eventSourceMessageListener, false);

                //  source.addEventListener('open', function(e) {
                //      console && console.log(">>> opened connection, phase: " + e.eventPhase);
                //      // Connection was opened.
                //  }, false);

                source.addEventListener('error', function(e) {
                  if (e.eventPhase == EventSource.CLOSED) {
                    // Connection was closed.
                      console && console.log("Connection was closed on error: " + e);
                  } else {
                      console && console.log("Error occurred while streaming: " + e);
                  }
                }, false);
            },0);
        });
        
        }       
    </g:javascript>


    </body>
</html>
