package hystrix.circuit.breaker

class HystrixMonitorController {
	/** A List of String describing the available clusters when Turbine is in use. */
	static final def clusters = []
	static allowedMethods = [ index: 'GET']
	
	def index() {
		def cluster = params.cluster
		def streamuri = "${request.contextPath}/hystrix.stream"
		if (clusters) {
			// ensure the parameter is limited to the configured clusters
			cluster = clusters.find{ it == cluster } ?: clusters[0]
			streamuri = "${request.contextPath}/turbine.stream?cluster="+cluster.encodeAsURL()
		} else {
			cluster = ''
		}
		render view: 'index', model: [streamuri: streamuri, clusters : clusters, cluster: cluster]
	}
}
