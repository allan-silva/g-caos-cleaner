package com.gmail.caos.cleaner.cli.auth

import scala.sys.process.*

trait BrowserProcess(val url: String) {
  def open(): Unit = "xdg-open http://localhost:5000".!
}
