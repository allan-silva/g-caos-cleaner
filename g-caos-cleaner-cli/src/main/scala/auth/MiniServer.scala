package auth

import java.net.{InetAddress, ServerSocket}

class MiniServer(val browserProcess: BrowserProcess, val middlewareClient: MiddlewareClient, val socketHolder: SocketHolder)

object MiniServer:
  def create: MiniServer = MiniServer(defaultBrowser(), defaultMiddleware(), defaultSocketHolder())

  private def defaultBrowser(): BrowserProcess =
    new BrowserProcess("http://localhost:5000") {}

  private def defaultSocketHolder(): SocketHolder =
    new SocketHolder {}

  private def defaultMiddleware(): MiddlewareClient =
    DefaultMiddlewareClient()
