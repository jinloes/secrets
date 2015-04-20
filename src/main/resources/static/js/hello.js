var appConfig = {
    CLIENT_ID: 'acme',
    CLIENT_SECRET: 'acmesecret'
};

var app = angular.module('hello', ['angular-oauth2', 'ngRoute', 'ipCookie', 'LocalStorageModule'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'home.html',
                controller: 'home'
            })
            .when("/login", {
                templateUrl: 'login.html',
                controller: 'login'
            })
            .otherwise('/');
    })
    .config(['OAuthProvider', function (OAuthProvider) {
        OAuthProvider.configure({
            baseUrl: 'http://localhost:8080',
            clientId: appConfig.CLIENT_ID,
            clientSecret: appConfig.CLIENT_SECRET,
            grantPath: '/oauth/token',
            revokePath: '/oauth/revoke'
        });
    }])
    .factory('userService', ['$http', function ($http) {
        var baseUrl = "http://localhost:8080/users";
        return {
            getCurrentUser: function () {
                return $http.get(baseUrl);
            },
            getSecrets: function (userId) {
                return $http.get(baseUrl + "/" + userId + "/secrets")
            }
        }
    }])
    .controller('navigation', function ($rootScope, $scope, $http, $location, $route,
                                        localStorageService, OAuth, ipCookie) {
        $scope.tab = function (route) {
            return $route.current && route === $route.current.controller;
        };
        $scope.logout = function () {
            OAuth.revokeToken();
            $rootScope.authenticated = false;
            ipCookie.remove('token');
            localStorageService.remove("accessToken");
            localStorageService.remove("userId");
        };
    })
    .controller('home', function ($rootScope, $scope, $http, userService, localStorageService,
                                  OAuth) {
        if (OAuth.isAuthenticated()) {
            $rootScope.authenticated = true;
            $http.defaults.headers.common.Authorization = 'Bearer' + localStorageService.get("accessToken");
            userService.getCurrentUser()
                .success(function (data) {
                    localStorageService.set("userId", data.id)
                    userService.getSecrets(data.id)
                        .success(function (data) {
                            $scope.secrets = data.content;
                        });
                });

            $scope.secret = {};
            $scope.createSecret = function () {
                $http.post("http://localhost:8080/secrets", $scope.secret).success(function (data) {
                    $http.get("http://localhost:8080/users/" + localStorageService.get("userId") + "/secrets")
                        .success(function (data) {
                            $scope.secrets = data._embedded.secretResources;
                        });
                });
            }
        }

    })
    .controller('login', function ($rootScope, $scope, $http, $location, $route,
                                   localStorageService, OAuth, ipCookie) {
        $scope.login = function (username, password) {
            var user = {"username": username, "password": password};
            var config = {
                "headers": {
                    'Authorization': 'Basic ' + btoa(appConfig.CLIENT_ID + ':' + appConfig.CLIENT_SECRET),
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }
            OAuth.getAccessToken(user, config)
                .then(function (resp) {
                    $rootScope.authenticated = true;
                    // Set the token for OAuth library
                    ipCookie('token', resp);
                    // Set the access token for local storage
                    localStorageService.set("accessToken", resp.data.access_token);
                    $http.defaults.headers.common.Authorization = 'Bearer' + localStorageService.get("accessToken");
                    $location.path('/')
                });
        }
    });


app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}
]);