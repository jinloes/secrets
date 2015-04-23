var appConfig = {
    CLIENT_ID: 'acme',
    CLIENT_SECRET: 'acmesecret'
};

var app = angular.module('hello', ['angular-oauth2', 'ngRoute', 'ipCookie', 'LocalStorageModule',
    'ui.bootstrap'])
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
            getSecrets: function (userId, page) {
                var url = baseUrl + "/" + userId + "/secrets";
                if (page) {
                    url += "?page=" + page;
                }
                return $http.get(url)
            }
        }
    }])
    .factory('secretService', ['$http', function ($http) {
        var baseUrl = "http://localhost:8080/secrets";
        return {
            getSecret: function (id) {
                return $http.get(baseUrl + "/" + id);
            },
            deleteSecret: function (id) {
                return $http.delete(baseUrl + "/" + id);
            },
            updateSecret: function (id, secret) {
                return $http.put(baseUrl + "/" + id, secret);
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
    .controller('home', function ($rootScope, $scope, $http, userService, secretService,
                                  localStorageService, OAuth) {
        if (OAuth.isAuthenticated()) {
            $rootScope.authenticated = true;
            userService.getCurrentUser()
                .success(function (data) {
                    localStorageService.set("userId", data.id)
                    $scope.user = {};
                    $scope.user.first_name = data.first_name;
                });
            $scope.secret = {};
            $scope.createSecret = function () {
                $http.post("http://localhost:8080/secrets", $scope.secret)
                    .success(function () {
                        $rootScope.$broadcast('update_secrets');
                    });
            };
            $scope.deleteSecret = function (id) {
                secretService.deleteSecret(id)
                    .success(function () {
                        $rootScope.$broadcast('update_secrets');
                    });
            };
        }
    })
    .controller('ModalCtrl', function ($scope, $modal) {
        $scope.open = function (size, id, secret) {
            $scope.id = id;
            $scope.secret = secret;
            $modal.open({
                templateUrl: 'myModalContent.html',
                controller: 'ModalInstanceCtrl',
                size: size,
                resolve: {
                    id: function () {
                        return $scope.id;
                    },
                    secret: function () {
                        return $scope.secret
                    }
                }
            });
        };
    })
    .controller('ModalInstanceCtrl', function ($rootScope, $scope, $modalInstance, $location, id, secret,
                                               localStorageService, secretService) {
        $scope.id = id;
        $scope.secret = secret;
        $scope.updateSecret = function (id, secret) {
            var secretModel = {
                secret: secret
            };
            secretService.updateSecret(id, secretModel)
                .success(function () {
                    $modalInstance.close();
                    $rootScope.$broadcast('update_secrets');
                });
        };
        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('PaginationCtrl', function ($scope, $http, localStorageService, userService) {
        $scope.currentPage = 1;
        $scope.secrets = [];

        function getData() {
            userService.getSecrets(localStorageService.get("userId"), $scope.currentPage)
                .success(function (response) {
                    $scope.totalItems = response.page.totalElements;
                    if (response.page.totalElements > 0) {
                        $scope.secrets = response._embedded.secretResources;
                    } else {
                        $scope.secrets = []
                    }
                });
        }

        $scope.pageChanged = function () {
            getData();
        };

        $scope.$on('update_secrets', function () {
            getData();
        })
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