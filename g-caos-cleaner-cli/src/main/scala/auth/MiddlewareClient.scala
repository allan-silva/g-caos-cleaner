package auth

trait MiddlewareClient {
  def requestAccessToken: String
}
