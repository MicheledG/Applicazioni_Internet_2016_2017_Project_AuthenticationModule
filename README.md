# Authentication Microservice - Internet Applications Project

The Authentication Microservice part of the final project for Internet Applications course held at Polytechnic University of Turin by Prof. Malnati and Prof. Servetti (a.y. 2016/2017).

## Security

This microservice requires authentication just for request to the endpoint `/password`. 

Authentication is based on a token (JWT), obtained by providing the right credentials to the the endpoint `/login`.

The authentication token must be placed in the http request as `Authorization` header:

`Authorization: Bearer XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX	` 

If a wrong authentication token is provided, a `401 Unauthorized` code is returned.

## REST Services

| API	          | Method | Req. body          | Query params | Status | Resp. body | Meaning    					  |
|:---------------:|:------:|:------------------:|:------------:|:------:|:----------:|:-------------------------------|
| `/login`        | `POST` | LoginCredentials   |              | 200    |            | Get an authentication token in the `Authorization` header of the http response |
|                 |        |                    |              | 400    |            | Login data are not valid, bad request |
|                 |        |                    |              | 401    |            | Login credentials are wrong, bad request |
| `/signup`       | `POST` | SignupCredentials  |              | 200    |            | Register a new user |
|                 |        |                    |              | 400    |            | Signup data are not valid, bad request |
|                 |        |                    |              | 409    |            | A profile for the same username or nickname already exists |
| `/activate`     | `POST` | ActivationToken    | token        | 200    |            | Activate a new account |
|                 |        |                    |              | 400    |            | Token not valid, activation failed |
| `/authenticate` | `POST` | token              |              | 200    |            | Authenticate a user given a token |
|                 |        |                    |              | 401    |            | Token not valid, activation failed |
| `/password`     | `PUT`  | Password           |              | 200    |            | Update the user password |
|                 |        |                    |              | 400    |            | Password is not valid, bad request |