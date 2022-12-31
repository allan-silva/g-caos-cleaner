package exploratory

import munit.FunSuite

import scala.sys.process.*

class OpenBrowserExploratorySuite extends FunSuite {
  test("Open Browser".ignore) {
    assert(Seq("xdg-open", "https://google.com").! == 0)
  }
}
