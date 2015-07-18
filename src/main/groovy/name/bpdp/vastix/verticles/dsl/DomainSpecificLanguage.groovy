package name.bpdp.vastix.verticles.dsl

import io.vertx.lang.groovy.GroovyVerticle
import io.vertx.groovy.core.eventbus.EventBus
import io.vertx.groovy.core.Vertx

class DomainSpecificLanguage extends GroovyVerticle {

	void start() {

		println "Hello from Groovy verticle!"

		//def vertx = Vertx.vertx()

		def eb = vertx.eventBus()
		
		def consumer = eb.consumer("vastix.dialog")

		consumer.handler({ message ->
			println("A message was received by vastix.dialog: ${message.body()}")
			message.reply("Reply from vastix.dialog!")
		})

		consumer.completionHandler({ res ->
			if (res.succeeded()) {
				println("The handler registration for vastix.dialog has reached all nodes")
			} else {
				println("vastix.dialog registration failed!")
			}
		})
	}

}
