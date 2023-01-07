package com.gmail.caos.cleaner.cli.auth.miniserver

import munit.FunSuite

import java.io.{
  BufferedInputStream,
  ByteArrayInputStream,
  InputStream,
  StringReader
}
import java.nio.charset.StandardCharsets

class HttpRequestURIAuthCodeParserTest extends FunSuite {
  private val HTTP_AUTH_CONFIMATION_REQUEST: String =
    """GET /?state=Gqfddf8Bhna7fyXskvP&code=HEY_I_AM_THE_CODE&scope=https://www.googleapis.com/auth/gmail.metadata%20https://www.googleapis.com/auth/gmail.modify HTTP/1.1
      |Host: localhost:9999
      |User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:108.0) Gecko/20100101 Firefox/108.0
      |Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
      |Accept-Language: en-US,en;q=0.5
      |Accept-Encoding: gzip, deflate, br
      |Connection: keep-alive
      |Upgrade-Insecure-Requests: 1
      |Sec-Fetch-Dest: document
      |Sec-Fetch-Mode: navigate
      |Sec-Fetch-Site: cross-site
      |Sec-Fetch-User: ?1""".stripMargin

  test("Should parse HTTP request URI code parameter") {
    val inputStream = ByteArrayInputStream(
      HTTP_AUTH_CONFIMATION_REQUEST.getBytes(StandardCharsets.UTF_8)
    )
    val parser = new HttpRequestURIAuthCodeParser() {}
    val code = parser.parse(inputStream)
    assertEquals(code, "HEY_I_AM_THE_CODE")
  }
}
