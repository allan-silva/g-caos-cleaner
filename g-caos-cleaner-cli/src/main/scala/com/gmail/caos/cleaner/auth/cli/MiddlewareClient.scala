package com.gmail.caos.cleaner.auth.cli

trait MiddlewareClient {
  def requestAccessToken: String
}
