package com.gmail.caos.cleaner.auth.cli

import java.net.{Socket, ServerSocket}

trait SocketHolder {
  def open(port: Integer): Socket =
    ServerSocket(port).accept
}
