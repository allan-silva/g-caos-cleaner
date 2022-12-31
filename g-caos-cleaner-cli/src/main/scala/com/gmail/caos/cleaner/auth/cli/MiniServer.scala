package com.gmail.caos.cleaner.auth.cli

import java.net.{InetAddress, ServerSocket}

class MiniServer(val socketHolder: SocketHolder)


object MiniServer:
  def default: MiniServer = MiniServer(defaultSocketHolder)

  private def defaultSocketHolder: SocketHolder = new SocketHolder {}
