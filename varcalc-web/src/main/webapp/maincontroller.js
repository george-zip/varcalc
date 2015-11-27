(function() {

  var app = angular.module("portfolioViewer");

  var maincontroller = function($scope, portfolioservice) {

    var onPortfoliosComplete = function(data) {
      //console.log(angular.toJson(data));
      $scope.portfolios = data;
    };

    var onNewPortfoliosComplete = function(data) {
      //console.log(angular.toJson(data));
      //$scope.portfolios = data;
      $scope.init();
    };

    var onError = function(reason) {
      $scope.error = "Could not fetch the data: " + reason;
    };

    $scope.addPortfolio = function() {
      //console.log("addPortfolio: " + $scope.newPortfolio);  
      portfolioservice.addPortfolio($scope.newPortfolio)
        .then(onNewPortfoliosComplete, onError);
    };
    
    $scope.makeUrl = function(id, name) {
          return "#/portfolio/" + id + "/" + name;
    };

    $scope.init = function() {
      portfolioservice.getPortfolios()
        .then(onPortfoliosComplete, onError);
    };
    
    $scope.init();

  };

  app.controller("maincontroller", maincontroller);

}());