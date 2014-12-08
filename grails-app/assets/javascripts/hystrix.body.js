//= require hystrixCommand
//= require hystrixThreadPool
//= require_self

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

    var stream = window.streamuri;
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