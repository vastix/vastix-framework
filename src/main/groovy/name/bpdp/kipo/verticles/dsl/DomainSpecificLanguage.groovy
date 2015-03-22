//  will try to test this whenever @GenIgnore is removed from Vertx.java

package name.bpdp.kipo.verticles.dsl

import io.vertx.lang.groovy.GroovyVerticle

class DomainSpecificLanguage extends GroovyVerticle {

	def start() {

		println "Hello from Groovy verticle!"

	}

}
