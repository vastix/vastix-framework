package name.bpdp.kipo.verticles.dsl

import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.groovy.core.eventbus.EventBus
import io.vertx.groovy.core.Vertx

class DomainSpecificLanguage extends GroovyVerticle {

	void start() {

		println "Hello from Groovy verticle!"

		def vertx = Vertx.vertx()

		def eb = vertx.eventBus()
		
		def consumer = eb.consumer("kipo.dialog")
		consumer.handler({ message ->
			println("I have received a message: ${message.body()}")
			message.reply("how interesting!")
		})

	}

}
