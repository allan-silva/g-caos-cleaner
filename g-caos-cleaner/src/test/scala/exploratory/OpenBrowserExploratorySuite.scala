package exploratory

import munit.FunSuite

import scala.sys.process._

class OpenBrowserExploratorySuite extends FunSuite {
  test("Open Browser") {
    assert(Seq("xdg-open", "https://google.com").! == 0)
  }
}
