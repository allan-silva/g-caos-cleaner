package com.gmail.caos.cleaner.cli.auth

trait MiddlewareClient {
  def requestAccessToken: String
}
