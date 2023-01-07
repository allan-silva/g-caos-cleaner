package exploratory

import com.google.api.client.auth.oauth2.{BearerToken, Credential}
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.gmail.Gmail
import munit.FunSuite
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.LenientHttpResponseParser
import org.apache.hc.core5.http.ProtocolVersion
import org.apache.hc.core5.http.config.Http1Config
import org.apache.hc.core5.http.impl.io.*
import org.apache.hc.core5.http.message.{BasicClassicHttpResponse, BasicLineParser}
import org.apache.http.client.utils.URLEncodedUtils
import org.apache.http.impl.io.HttpTransportMetricsImpl
import sttp.client3.*
import sttp.client3.upicklejson.*
import upickle.default.*

import java.io.{BufferedReader, ByteArrayInputStream, ByteArrayOutputStream, InputStreamReader}
import java.net.{ServerSocket, Socket}
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets
import scala.collection.mutable
import scala.language.postfixOps
import scala.sys.process.*
import scala.util.Using
import scala.util.control.Breaks.{break, breakable}

class AuthFlowExploratory extends FunSuite {

  val ContentLengthHeader = "Content-Length: "

  test("Setup Standalone HttpServer".ignore) {
    println(wait_for_response())
  }

  test("Apache response write") {
    val basicResponse = BasicClassicHttpResponse(200, "OK")
    val output = ByteArrayOutputStream()
    val outputSession =
      SessionOutputBufferImpl(512, StandardCharsets.UTF_8.newEncoder())
    val responseWriter = DefaultHttpResponseWriter()
    responseWriter.write(basicResponse, outputSession, output)
    outputSession.flush(output)

    assert(output.toString(StandardCharsets.UTF_8) == "HTTP/1.1 200 OK\r\n\r\n")

  }

  test("Apache read request".ignore) {
    val server = ServerSocket(9999)
    val clientSocket = server.accept

    val requestParser = DefaultHttpRequestParser()
    val sessionInputBuffer = SessionInputBufferImpl(512)
    val parsedRequest =
      requestParser.parse(sessionInputBuffer, clientSocket.getInputStream)
    val queryParams =
      URLEncodedUtils.parse(parsedRequest.getUri, StandardCharsets.UTF_8)
    val queryMap = mutable.Map.empty[String, String]

    queryParams.forEach(queryParam =>
      queryMap += (queryParam.getName -> queryParam.getValue)
    )

    println(queryMap("test"))

    assert(queryMap contains "test")
  }

  test("Auth flow - Open Browser") {
    "xdg-open http://localhost:5000".!
    val code_response = wait_for_response()
    println(code_response)
  }

  test("Auth flow - Request access token".ignore) {
    val client = SimpleHttpClient()
    val request = basicRequest
      .post(uri"http://localhost:5000/access-token")
      .body(
        AuthAccessTokenRequest(
          "4/0AWgavdcVShLIgShUCn6uIjUWuUBgLv9lBvsK3CxxwAwaspG4amANyVZbqRzej_qPgedUCA"
        )
      )
    val response = client.send(request)
    response.body match {
      case Left(e)  => println(s"Got response exception:\n$e")
      case Right(r) => println(r)
    }
  }

  test("Call google api".ignore) {
    val transport = GoogleNetHttpTransport.newTrustedTransport()
    val credential = Credential
      .Builder(BearerToken.authorizationHeaderAccessMethod())
      .setTransport(transport)
      .setJsonFactory(GsonFactory.getDefaultInstance)
      .build()
    credential.setAccessToken(
      "ya29.a0AX9GBdXqoF-NpnF10CqVXqW3EQLsP_yl_m8kUb6MV6lc9ox5xrwseVkNZth32gAT2UK0qE_I6rLdstIxC9upR3F0xp0zWZcZjoqWC7KX0otlM3hv0yqrqIUoWg0M-qhGUKxIociy4elhtn5DB2sfvhzZ93hlaCgYKAdoSARMSFQHUCsbCMmFjs5FqK7ykvRk94KR6zw0163"
    )
    val gmail = Gmail
      .Builder(transport, GsonFactory.getDefaultInstance, credential)
      .setApplicationName("g-caos-cleaner")
      .build()
    val labelsResponse = gmail.users.labels.list("me").execute
    labelsResponse.getLabels
      .stream()
      .map(label => label.getName)
      .forEach(println)
  }

  case class AuthAccessTokenRequest(authorization_code: String)
  implicit val myRequestRW: ReadWriter[AuthAccessTokenRequest] =
    macroRW[AuthAccessTokenRequest]

  def wait_for_response(): String =
    val server = ServerSocket(9999)
    val clientSocket = server.accept
    val uri = read_response(clientSocket)
    write_response(clientSocket)
    clientSocket.close()
    server.close()
    uri

  def write_response(clientSocket: Socket): Unit =
    val outputSession =
      SessionOutputBufferImpl(512, StandardCharsets.UTF_8.newEncoder())
    val responseWriter = DefaultHttpResponseWriter()
    responseWriter.write(
      httpResponse(),
      outputSession,
      clientSocket.getOutputStream
    )
    outputSession.flush(clientSocket.getOutputStream)

  def httpResponse(): BasicClassicHttpResponse =
    BasicClassicHttpResponse(200, "OK")

  def get_body(clientBuffer: BufferedReader): String =
    get_body(clientBuffer, 0, false)

  def read_response(clientSocket: Socket): String =
    val requestParser = DefaultHttpRequestParser()
    val sessionInputBuffer = SessionInputBufferImpl(512)
    val parsedRequest =
      requestParser.parse(sessionInputBuffer, clientSocket.getInputStream)
    parsedRequest.getRequestUri

  def get_body(
      clientBuffer: BufferedReader,
      contentLength: Int,
      startBody: Boolean
  ): String =
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
      val extractedContentLength =
        (line substring ContentLengthHeader.length).toInt
      return get_body(clientBuffer, extractedContentLength, startBody)
    }
    get_body(clientBuffer, contentLength, startBody)
}
