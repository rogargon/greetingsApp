/**
 * Created by http://rhizomik.net/~roberto/
 */

(function(){
    var app = angular.module("greetingForm", [ ]);

    app.directive("greetingForm", function(){
        return {
            restrict: "E",
            templateUrl: "greeting-form/greeting-form.html"
        };
    });

})();
