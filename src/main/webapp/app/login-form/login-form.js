/**
 * Created by http://rhizomik.net/~roberto/
 */

(function(){
    var app = angular.module("loginForm", [ ]);

    app.directive("loginForm", function(){
        return {
            restrict: "E",
            templateUrl: "login-form/login-form.html",
            controller: function($rootScope, $scope, $http, $location) {

                var authenticate = function(credentials, callback) {

                    /*var headers = credentials ? {authorization : "Basic "
                    + btoa(credentials.username + ":" + credentials.password)
                    } : {};*/

                    $http.get('../api/users/current').success(function(data) {
                        if (data.username) {
                            $rootScope.authenticated = true;
                        } else {
                            $rootScope.authenticated = false;
                        }
                        callback && callback();
                    }).error(function() {
                        $rootScope.authenticated = false;
                        callback && callback();
                    });

                }

                $scope.credentials = {};
                $scope.login = function() {
                    authenticate($scope.credentials, function() {
                        if ($rootScope.authenticated) {
                            $scope.error = false;
                        } else {
                            $scope.error = true;
                        }
                    });
                };

            },
            controllerAs: 'login'
        };
    });

})();