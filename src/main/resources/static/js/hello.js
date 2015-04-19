var appConfig = {
    CLIENT_ID: 'acme',
    CLIENT_SECRET: 'acmesecret'
};

var app = angular.module('hello', ['angular-oauth2', 'ngRoute', 'ipCookie', 'ngCookies'])
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
    .controller('navigation', function ($rootScope, $scope, $http, $location, $route, OAuth, ipCookie) {
        $scope.tab = function (route) {
            return $route.current && route === $route.current.controller;
        };
        $scope.logout = function () {
            OAuth.revokeToken();
            $rootScope.authenticated = false;
            ipCookie.remove('token');
        };
    })
    .controller('home', function ($rootScope, $scope, $http, OAuth, ipCookie) {
        if (OAuth.isAuthenticated()) {
            $rootScope.authenticated = true;
            var token = ipCookie('token');
            $http.defaults.headers.common.Authorization = 'Bearer' + token.data.access_token;
            $http.get("http://localhost:8080/user").success(function (data) {
                $scope.user = data;
            });
        }
    })
    .controller('login', function ($rootScope, $scope, $http, $location, OAuth, ipCookie) {
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
                    ipCookie('token', resp);
                    $http.defaults.headers.common.Authorization = 'Bearer' + resp.data.access_token;
                    $location.path('/')
                });
        }
    });


app.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}
]);