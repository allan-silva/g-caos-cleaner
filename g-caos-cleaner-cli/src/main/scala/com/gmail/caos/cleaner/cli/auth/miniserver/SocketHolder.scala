package com.gmail.caos.cleaner.cli.auth.miniserver

import java.net.{ServerSocket, Socket}

trait SocketHolder(val port: Int) {
  private var serverSocket: ServerSocket = ServerSocket(port)

  def clientSocket: Socket = serverSocket.accept()

  def reconnect(): Unit = serverSocket = ServerSocket(port)
}
