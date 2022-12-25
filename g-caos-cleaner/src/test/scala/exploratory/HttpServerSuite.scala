package exploratory

import munit.FunSuite
import org.apache.hc.client5.http.impl.io.LenientHttpResponseParser
import org.apache.hc.core5.http.ProtocolVersion
import org.apache.hc.core5.http.config.Http1Config
import org.apache.hc.core5.http.impl.io.{DefaultClassicHttpResponseFactory, DefaultHttpResponseWriter, SessionInputBufferImpl, SessionOutputBufferImpl}
import org.apache.hc.core5.http.message.{BasicClassicHttpResponse, BasicLineParser}
import org.apache.http.impl.io.HttpTransportMetricsImpl

import java.net.ServerSocket
import java.io.{BufferedReader, ByteArrayInputStream, ByteArrayOutputStream, InputStreamReader}
import java.net.Socket
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets
import scala.language.postfixOps
import scala.util.control.Breaks.{break, breakable}
import scala.util.Using


class HttpServerSuite extends FunSuite {

  val ContentLengthHeader = "Content-Length: "

  test("Setup Standalone HttpServer") {
    println(wait_for_response())
  }

  test("Call Google API") {
    val redirect_uri = "http://127.0.0.1:9999"
  }

  test("Apache response write") {
    val basicResponse = BasicClassicHttpResponse(200, "OK")
    val output = ByteArrayOutputStream()
    val outputSession = SessionOutputBufferImpl(512, StandardCharsets.UTF_8.newEncoder())
    val responseWriter = DefaultHttpResponseWriter()
    responseWriter.write(basicResponse, outputSession, output)
    outputSession.flush(output)

    assert(output.toString(StandardCharsets.UTF_8) == "HTTP/1.1 200 OK\r\n\r\n")

  }

  def wait_for_response(): String =
    val server = ServerSocket(9999)
    val clientSocket = server.accept
    val clientBuffer = BufferedReader(InputStreamReader(clientSocket.getInputStream))
    val body = get_body(clientBuffer)
    write_response(clientSocket)
    clientSocket.close()
    server.close()
    body

  def write_response(clientSocket: Socket) =
    val outputSession = SessionOutputBufferImpl(512, StandardCharsets.UTF_8.newEncoder())
    val responseWriter = DefaultHttpResponseWriter()
    responseWriter.write(httpResponse(), outputSession, clientSocket.getOutputStream)
    outputSession.flush(clientSocket.getOutputStream)

  def httpResponse() = BasicClassicHttpResponse(200, "OK")

  def get_body(clientBuffer: BufferedReader): String = get_body(clientBuffer, 0, false)

  def get_body(clientBuffer: BufferedReader, contentLength: Int, startBody: Boolean): String =
    if contentLength > 0 && startBody then {
      val buffer = CharBuffer allocate contentLength
      clientBuffer.read(buffer)
      return String(buffer.array)
    }

    val line = clientBuffer.readLine()
    println(line)

    if line.isBlank then {
      if contentLength == 0 then {
        return ""
      }
      return get_body(clientBuffer, contentLength, true)
    }

    if line contains ContentLengthHeader then {
      val extractedContentLength = (line substring ContentLengthHeader.length).toInt
      return get_body(clientBuffer, extractedContentLength, startBody)
    }
    get_body(clientBuffer, contentLength, startBody)

}
