package hystrix.circuit.breaker

class HystrixMonitorController {
	static allowedMethods = [ index: 'GET']
	
	def index() {
		render view: 'index', model: [streamuri: "${request.contextPath}/hystrix.stream"]
	}
}
