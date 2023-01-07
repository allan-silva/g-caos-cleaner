package com.gmail.caos.cleaner.cli.auth.miniserver

import java.net.{InetAddress, ServerSocket}

class MiniServer(
    val socketHolder: SocketHolder,
    val httpRequestURIAuthCodeParser: HttpRequestURIAuthCodeParser
)

object MiniServer:
  def default: MiniServer =
    MiniServer(defaultSocketHolder, defaultUriParamCodeAuthParser)

  private def defaultSocketHolder: SocketHolder =
    new SocketHolder(MiniServerSettings().port) {}

  private def defaultUriParamCodeAuthParser: HttpRequestURIAuthCodeParser =
    new HttpRequestURIAuthCodeParser() {}
