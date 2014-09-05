package com.neidetcher.hcbp.promise

import spock.lang.Specification

class DynamicHystrixCommandSpec extends Specification {
	void "closure only"() {
		given:'command with closure'
		def command = DynamicHystrixCommand.create() {
			'ABC'
		}
		
		when:'command is called'
		def value = command.execute()
		
		then:'value is returned'
		value == 'ABC'
	}
	
	void "fallback default behavior"() {
		given:'command without fallback'
		def command = DynamicHystrixCommand.create() {
			throw new IOException()
		}
		
		when:'command is called'
		def value = command.execute()
		
		then:'exception is thrown'
		thrown(Throwable)
	}
	
	void "fallback object returned"() {
		given:'command with fallback object'
		def command = DynamicHystrixCommand.create(fallback: 'XYZ') {
			throw new IOException()
		}
		
		when:'command is called'
		def value = command.execute()
		
		then:'value is returned'
		value == 'XYZ'
	}
	
	void "fallback closure called"() {
		given:'command with fallback object'
		def command = DynamicHystrixCommand.create(fallback: {'XYZ'}) {
			throw new IOException()
		}
		
		when:'command is called'
		def value = command.execute()
		
		then:'value is returned'
		value == 'XYZ'
	}
	
	void "settings"() {
		when:'command with settings'
		def command = DynamicHystrixCommand.create(group: 'group', command: 'command', threadpool: 'threadpool', cacheKey: 'cacheKey', timeout: 30001) {
			'ABC'
		}
		
		then:'setting are taken'
		command.getCommandGroup().name() == 'group'
		command.getCommandKey().name() == 'command'
		command.getThreadPoolKey().name() == 'threadpool'
		command.getCacheKey() == 'cacheKey'
		command.getProperties().executionIsolationThreadTimeoutInMilliseconds().get() == 30001
	}
}
