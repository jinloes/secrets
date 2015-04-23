# secrets

Storing your secrets

Requirements
============

1. Java 7 at minimum
2. Unlimited Strength JCE installed (for encryption)
    * Java 7 http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
    * Java 8 http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
3. Gradle 2.3 or later (for building)
    * https://gradle.org/downloads/

How to run
==========

1. From gradle
    * gradle bootRun
2. Executable jar
    * java -jar secrets-0.1.0.jar
3. Check localhost:8080 for the ui

Default User
============

* Username - user@email.com
* Password - password

Example operations (with curl):
===============================
**NOTE:** Responses are given in the JSON HAL spec: https://tools.ietf.org/html/draft-kelly-json-hal-06
**NOTE:** Authenication is done through OAuth2 resource own password credentials grant

1. Get OAuth Token (using resource owner password credentials grant)
    * `curl -X POST -u acme:acmesecret http://localhost:8080/oauth/token -H "Accept: application/json" -d "password=password&username=user@email.com&grant_type=password&scope=read%20write&client_secret=acmesecret&client_id=acme"`

    Example Response (200 - OK):
    ```
        {
            "access_token":"38a5b12d-49b9-4cc0-b288-ad528a94f67a",
            "token_type":"bearer",
            "refresh_token":"8d46f29f-c119-47f3-bece-55701a5af100",
            "expires_in":42589,"
            scope":"read write"
        }
    ```
2. Get current user
    * `curl -H "Authorization: Bearer {access token}" localhost:8080/user`
    * `{access token}` is the access token from the previous call

    Example Response (200 - OK):
    ```
        {
          "email" : "user@email.com",
          "first_name" : "Joe",
          "id" : "ddc1f9f3-2e8e-47ce-b388-27624d964288",
          "last_name" : "Somebody",
          "_links" : {
            "self" : {
              "href" : "http://localhost:8080/users"
            }
        }
    ```


3. Create a secret
    * `curl -X POST -H "Authorization: Bearer {access token}" -H "Content-Type: application/json" localhost:8080/secrets -d '{ "secret": "1234"}'`
    * `{access token}` is the token from the previous call

    Example Response (201 - CREATED):
    ```
        {
          "_links" : {
            "self" : {
              "href" : "http://localhost:8080/secrets/ab470905-7fcc-4b14-af57-ad95bc17c36b"
            }
          },
          "id" : "ab470905-7fcc-4b14-af57-ad95bc17c36b"
        }
    ```

4. Get a user's secrets
    * `curl -H "Authorization: Bearer {access token} localhost:8080/users/{userId}/secrets`
    * `{access token}` is the access token from the previous call
    * `{userId}` is the user's UUID
    * Supports page, size, and sort parameters See: http://docs.spring.io/spring-data/rest/docs/2.3.0.RELEASE/reference/html/#paging-and-sorting for more information

    Example Response (200 - OK)
    ```
        {
          "_links" : {
            "self" : {
              "href" : "http://localhost:8080/users/ddc1f9f3-2e8e-47ce-b388-27624d964288/secrets{?page,size,sort}",
              "templated" : true
            }
          },
          "_embedded" : {
            "secretResources" : [
                {
                  "secret" : "my secret1",
                  "id" : "97b5905c-db12-4f44-8a0e-99efa8d932b3"
                }, {
                  "secret" : "my secret2",
                  "id" : "7295f571-4d31-4544-a590-b2f601e711b3"
                }, {
                  "secret" : "my secret3",
                  "id" : "5a9ed2fb-cac9-4e30-9937-4f96923fd003"
                }
            ]
          },
          "page" : {
            "size" : 10,
            "totalElements" : 3,
            "totalPages" : 1,
            "number" : 0
          }
        }
    ```
5. Update a secret
    * `curl -X PUT -H "Authorization: Bearer {access token}" -H "Content-Type: application/json" localhost:8080/secrets/{secretId} -d '{ "secret": "1234"}'`
    * `{access token}` is the access token from the previous call
    * `{secretId}` is the secret's UUID

    Example Response (200 - OK):
    ```
        {
           "_links" : {
             "self" : {
               "href" : "http://localhost:8080/secrets/97b5905c-db12-4f44-8a0e-99efa8d932b3"
             }
           },
           "id" : "97b5905c-db12-4f44-8a0e-99efa8d932b3"
        }
    ```

6. Delete a secret
    * `curl -X DELETE -H "Authorization: Bearer {access token}" localhost:8080/secrets/{secretId}`
    * `{access token}` is the access token from the previous call
    * `{secretId}` is the secret's UUID

    Example Response (204 - NO CONTENT)
    **There is no response body.**

7. Get a secret
    * `curl -X GET -H "Authorization: Bearer {access token}" localhost:8080/secrets/{secretId}`
    * `{access token} is the access token from the previous call
    * `{secretId}` is a secret's UUID

    Example Response (200 - OK)
    ```
        {
          "secret" : "123",
          "_links" : {
            "self" : {
              "href" : "http://localhost:8080/secrets/4cf4f8cb-ed21-4256-bb74-fa83aa12f177"
            }
          },
          "id" : "4cf4f8cb-ed21-4256-bb74-fa83aa12f177"
        }
    ```