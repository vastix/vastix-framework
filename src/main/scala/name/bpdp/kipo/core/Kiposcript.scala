/*
 * An angine for pragmatic interaction between client and server
 * on the Web
 *
 * For the first time, it was stolen from Daniel Spiewak source code:
 * http://www.codecommit.com/blog/scala/formal-language-processing-in-scala
 *
 * Other resources:
 *  http://stackoverflow.com/questions/1348121/scala-parser-issues
 *
 */

package name.bpdp.kipo.core

import scala.io.Source
import alice.tuprolog._
import scala.util.control._

object Kiposcript extends App {
 
  val pEngine = new Prolog
  var info = pEngine.solve("append(X,Y,[1,2]).")
  val loop = new Breaks

  loop.breakable {
      
    while (info.isSuccess()) {

      println("solution: " + info.getSolution() + 
        " - bindings: " + info)
      if (pEngine.hasOpenAlternatives()) {
        info = pEngine.solveNext()
      } else {
        loop.break
      }

    }

  }

  val input = Source.fromFile("langs/prolog/likes.pl").getLines.reduceLeft[String](_ + '\n' + _)

  pEngine.addTheory(new Theory(input))

  var infoFromFile = pEngine.solve("likes(sam,Food).")

  loop.breakable {
      
    while (infoFromFile.isSuccess()) {

      println("solution: " + infoFromFile.getSolution() + 
        " - bindings: " + infoFromFile)
      if (pEngine.hasOpenAlternatives()) {
        infoFromFile = pEngine.solveNext()
      } else {
        loop.break
      }

    }

  }



}
