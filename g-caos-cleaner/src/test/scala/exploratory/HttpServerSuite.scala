package exploratory

import munit.FunSuite

import java.net.ServerSocket
import java.io.InputStreamReader
import java.io.BufferedReader
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets
import scala.util.control.Breaks.{break, breakable}

class HttpServerSuite extends FunSuite {
  test("Setup Standalone HttpServer") {
    val server = ServerSocket(9999)
    val clientSocket = server.accept
    val clientBuffer = BufferedReader(InputStreamReader(clientSocket.getInputStream))

    println(get_body(clientBuffer))

    clientSocket.close()
  }

  def get_body(clientBuffer: BufferedReader): String = get_body(clientBuffer, 0, false)

  def get_body(clientBuffer: BufferedReader, contentLenght: Int, startBody: Boolean): String =
    if contentLenght > 0 && startBody then {
      val buffer = CharBuffer.allocate(contentLenght)
      clientBuffer.read(buffer)
      return String(buffer.array)
    }

    val line = clientBuffer.readLine()

    if line.isBlank then {
      get_body(clientBuffer, contentLenght, true)
    }  else {
      val contentTypeIndex = line indexOf "Content-Length: "
      if contentTypeIndex > -1 then {
        val cl = (line substring "Content-Length: ".length).toInt
        get_body(clientBuffer, cl, startBody)
      } else {
        get_body(clientBuffer, contentLenght, startBody)
      }
    }
}
