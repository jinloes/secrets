# secrets

How to interact with the command line:

curl -X POST -u acme:acmesecret http://localhost:8080/oauth/token -H "Accept: application/json" -d "password=password&username=user@email.com&grant_type=password&scope=read%20write&client_secret=acmesecret&client_id=acme"

curl -H "Authorization: Bearer ad5749e7-1e4d-47de-b580-780899b44b44" localhost:8080/user


curl -X POST -H "Authorization: Bearer 31f62b3c-da39-4451-8f1a-88c9caa92aeb" -H "Content-Type: application/json" localhost:8080/secrets -d '{ "secret": "1234"}'
