import google.oauth2.credentials
import google_auth_oauthlib.flow

from flask import Flask, redirect


app = Flask(__name__)


flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
    '/home/allan/code.allan/g-caos-cleaner/middleware/credentials.json',
    scopes=['https://www.googleapis.com/auth/gmail.metadata', 'https://mail.google.com/'])

flow.redirect_uri = 'http://localhost:9999'


@app.route("/", methods=['GET'])
def redirect_to_google():
    authorization_url, state = flow.authorization_url(
    # Enable offline access so that you can refresh an access token without
    # re-prompting the user for permission. Recommended for web server apps.
    access_type='offline',
    # Enable incremental authorization. Recommended as a best practice.
    include_granted_scopes='true')
    return redirect(authorization_url)
