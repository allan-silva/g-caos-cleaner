package com.gmail.caos.cleaner.cli.auth.miniserver

import org.apache.hc.core5.http.impl.io.{
  DefaultHttpRequestParser,
  SessionInputBufferImpl
}
import org.apache.http.client.utils.URLEncodedUtils

import java.io.InputStream
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters.*
import scala.language.postfixOps

trait HttpRequestURIAuthCodeParser {
  private val AUTH_CODE_PARAM_NAME = "code"

  def parse(stream: InputStream): String =
    val requestParser = DefaultHttpRequestParser()
    val sessionInputBuffer = SessionInputBufferImpl(512)
    val incomingRequest = requestParser.parse(sessionInputBuffer, stream)
    val nameValuePairs =
      URLEncodedUtils.parse(incomingRequest.getUri, StandardCharsets.UTF_8)
    val uriParams =
      nameValuePairs.asScala.map(nvp => nvp.getName -> nvp.getValue).toMap
    uriParams(AUTH_CODE_PARAM_NAME)
}
