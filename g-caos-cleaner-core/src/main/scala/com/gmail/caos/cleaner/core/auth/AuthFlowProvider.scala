package com.gmail.caos.cleaner.core.auth

trait AuthFlowProvider:
  def execute(): AuthSettings

