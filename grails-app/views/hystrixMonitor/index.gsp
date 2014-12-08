<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="hystrixMonitorLayout">
    <title><g:message code="app.name" default="Hystrix Monitor"/></title>
</head>

<body>
<dfn style="display:none"
     id="hystrixCircuit">${resource(dir: 'components/hystrixCommand/templates', file: 'hystrixCircuit.html', plugin: 'hystrix-circuit-breaker')}</dfn>
<dfn style="display:none"
     id="hystrixCircuitContainer">${resource(dir: 'components/hystrixCommand/templates', file: 'hystrixCircuitContainer.html', plugin: 'hystrix-circuit-breaker')}</dfn>
<dfn style="display:none"
     id="hystrixThreadPool">${resource(dir: 'components/hystrixThreadPool/templates', file: 'hystrixThreadPool.html', plugin: 'hystrix-circuit-breaker')}</dfn>
<dfn style="display:none"
     id="hystrixThreadPoolContainer">${resource(dir: 'components/hystrixThreadPool/templates', file: 'hystrixThreadPoolContainer.html', plugin: 'hystrix-circuit-breaker')}</dfn>

<div id="header">
    <h2><span id="title_name"><g:message code="app.name" default=""/><g:if
            test="${clusters.size() > 1}">${cluster}</g:if></span></h2>
</div>

<div class="container">
    <g:if test="${clusters.size() > 1}">
        <div class="row">
            <div class="menubar">
                <div class="title">
                    Cluster
                </div>

                <div class="menu_actions">
                    <g:each var="c" status="i" in="${clusters}"><g:link params="[cluster: c]">${c}</g:link><g:if
                            test="${i < clusters.size() - 1}">|</g:if></g:each>
                </div>
            </div>
        </div>

        <div class="spacer"></div>
    </g:if>

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
                <span class="success">Success</span> | <span class="shortCircuited">Short-Circuited</span> | <span
                    class="timeout">Timeout</span> | <span class="rejected">Rejected</span> | <span
                    class="failure">Failure</span> | <span class="errorPercentage">Error %</span>
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
                Sort: <a href="javascript://"
                         onclick="dependencyThreadPoolMonitor.sortAlphabetically();">Alphabetical</a> |
                <a href="javascript://" onclick="dependencyThreadPoolMonitor.sortByVolume();">Volume</a> |
            </div>
        </div>
    </div>
    <div id="dependencyThreadPools" class="row dependencyThreadPools"><span class="loading">Loading ...</span></div>
</div>

</body>
</html>
