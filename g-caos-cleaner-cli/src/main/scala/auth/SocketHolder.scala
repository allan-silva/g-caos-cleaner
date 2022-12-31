package auth

import java.net.{Socket, ServerSocket}

trait SocketHolder {
  def connect(port: Integer): Socket =
    ServerSocket(port).accept
}
