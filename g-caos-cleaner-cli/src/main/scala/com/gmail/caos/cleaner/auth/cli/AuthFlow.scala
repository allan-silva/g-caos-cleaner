package com.gmail.caos.cleaner.auth.cli

import com.gmail.caos.cleaner.core.auth.{AuthFlowProvider, AuthSettings}

class AuthFlow(val miniServer: MiniServer, browserProcess: BrowserProcess, middlewareClient: MiddlewareClient):
  private def requestUserConsent(): Unit = print("")
  private def getAuthResponse(): AuthCodeResponse = AuthCodeResponse("")
  private def requestAuthSettings(code: String): AuthSettings = AuthSettings("")

object AuthFlow extends AuthFlowProvider:
  def execute(): AuthSettings =
    val authFlow: AuthFlow = AuthFlow(MiniServer.default, defaultBrowser, defaultMiddleware)
    authFlow.requestUserConsent()
    authFlow.requestAuthSettings(authFlow.getAuthResponse().code)

  private def defaultBrowser: BrowserProcess =
    new BrowserProcess("http://localhost:5000") {}

  private def defaultMiddleware: MiddlewareClient =
    DefaultMiddlewareClient()
