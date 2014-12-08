<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="shortcut icon"
          href="${resource(dir: 'images', file: 'hystrix-logo.png', plugin: 'hystrix-circuit-breaker')}"/>
    <title><g:layoutTitle default="Hystrix Monitor"/></title>

    <asset:stylesheet href="hystrix.css"/>
    <asset:javascript src="hystrix.js"/>
</head>

<body>
<g:layoutBody/>
<script type="application/javascript">
    window.streamuri = '${streamuri ?: ''}'
</script>
<asset:javascript src="hystrix.body.js"/>
</body>
</html>