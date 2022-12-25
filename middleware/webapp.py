import google_auth_oauthlib.flow

from flask import Flask, redirect, request

import json


app = Flask(__name__)


scopes = [
    "https://www.googleapis.com/auth/gmail.metadata",
    "https://www.googleapis.com/auth/gmail.modify",
]

flow = google_auth_oauthlib.flow.Flow.from_client_secrets_file(
    "/home/allan/code.allan/g-caos-cleaner/middleware/credentials.json", scopes=scopes
)

flow.redirect_uri = "http://localhost:9999"


@app.get("/")
def redirect_to_google():
    authorization_url, state = flow.authorization_url(
        # Enable offline access so that you can refresh an access token without
        # re-prompting the user for permission. Recommended for web server apps.
        access_type="offline",
        # Enable incremental authorization. Recommended as a best practice.
        include_granted_scopes="true",
    )
    return redirect(authorization_url)


@app.post("/access-token")
def request_access_token():
    flow.fetch_token(code=request.json["authorization_code"])
    return (
        json.dumps(
            {
                "access_token": flow.credentials.token,
                "scopes": flow.credentials.scopes,
                "refresh_token": flow.credentials.refresh_token,
            }
        ),
        200,
    )
