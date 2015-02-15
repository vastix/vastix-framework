/*
 * An angine for pragmatic interaction between client and server
 * on the Web
 *
 */

package name.bpdp.kipo.core

import scala.io.Source
import alice.tuprolog.{Parser => tuParser, Int => tuInt, _}
import scala.util.control._

// parboiled2
import scala.annotation.tailrec
import scala.util.{Failure, Success}
import scala.io.StdIn
import org.parboiled2.{Parser => pbParser, _}

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


  repl()

  @tailrec
  def repl(): Unit = {
    // TODO: Replace next three lines with `scala.Predef.readLine(text: String, args: Any*)`
    // once BUG https://issues.scala-lang.org/browse/SI-8167 is fixed
    print("---\nEnter calculator expression > ")
    Console.out.flush()
    StdIn.readLine() match {
      case "" =>
      case line =>
        val parser = new Kiposcript(line)
        parser.InputLine.run() match {
          case Success(exprAst)       => println("Result: " + eval(exprAst))
          case Failure(e: ParseError) => println("Expression is not valid: " + parser.formatError(e))
          case Failure(e)             => println("Unexpected error during parsing run: " + e)
        }
        repl()
    }
  }

  def eval(expr: Expr): Int =
    expr match {
      case Value(v)             => v.toInt
      case Addition(a, b)       => eval(a) + eval(b)
      case Subtraction(a, b)   => eval(a) - eval(b)
      case Multiplication(a, b) => eval(a) * eval(b)
      case Division(a, b)       => eval(a) / eval(b)
    }
  
  // our abstract syntax tree model
  sealed trait Expr
  case class Value(value: String) extends Expr
  case class Addition(lhs: Expr, rhs: Expr) extends Expr
  case class Subtraction(lhs: Expr, rhs: Expr) extends Expr
  case class Multiplication(lhs: Expr, rhs: Expr) extends Expr
  case class Division(lhs: Expr, rhs: Expr) extends Expr
}

/**
 * This parser reads simple calculator expressions and builds an AST
 * for them, to be evaluated in a separate phase, after parsing is completed.
 */
class Kiposcript(val input: ParserInput) extends pbParser {
  import Kiposcript._
  
  def InputLine = rule { Expression ~ EOI }

  def Expression: Rule1[Expr] = rule {
    Term ~ zeroOrMore(
      '+' ~ Term ~> Addition
    | '-' ~ Term ~> Subtraction)
  }

  def Term = rule {
    Factor ~ zeroOrMore(
      '*' ~ Factor ~> Multiplication
    | '/' ~ Factor ~> Division)
  }

  def Factor = rule { Number | Parens }

  def Parens = rule { '(' ~ Expression ~ ')' }

  def Number = rule { capture(Digits) ~> Value }

  def Digits = rule { oneOrMore(CharPredicate.Digit) }
}

