(function() {
  
  var app = angular.module("portfolioViewer", ["ngRoute"]);
  
  app.config(function($routeProvider) {
    $routeProvider
      .when("/main", {
        templateUrl: "main.html",
        controller: "maincontroller"
      })
      .when("/portfolio/:id/:portfolioName", {
        templateUrl: "portfolio.html",
        controller: "portfoliocontroller"
      })
      .otherwise( {redirectTo:"/main"} );
  });
  
}());