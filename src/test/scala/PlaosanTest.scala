import org.specs2.mutable._

class PlaoscriptSpec extends Specification {

  import name.bpdp.plaosan.core.Plaoscript

  "Let" should {
    let x = 42
    "x equal 42 if we set let x = 42" in {
      x mustEqual 42
    }
  }
}
