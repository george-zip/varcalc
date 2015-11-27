(function() {

  var app = angular.module("portfolioViewer");

  var portfoliocontroller = function($scope, $routeParams, $filter, portfolioservice) {

    var onNMVComplete = function(data) {
      //console.log(angular.toJson(data));
      $scope.nmv = data.data.nmv;
      var newPercentile = ($scope.percentage / 100);
      portfolioservice.getVaR($scope.id, newPercentile, $scope.days)
        .then(onVaRComplete, onError);
    };

    var onVolComplete = function(data) {
      //console.log(angular.toJson(data));
      $scope.vol = data.data.vol;
      portfolioservice.getGMV($scope.id)
        .then(onGMVComplete, onError);
    };

    var onGMVComplete = function(data) {
      //console.log(angular.toJson(data));
      $scope.gmv = data.data.gmv;
      portfolioservice.getPositions($scope.id)
        .then(onPositionsComplete, onError);
    };

    var onPositionsComplete = function(data) {
      //console.log(angular.toJson(data));
      $scope.positions = data;
    };

    var onVaRComplete = function(data) {
      //console.log(angular.toJson(data));
      $scope.var = data.data.var;
      portfolioservice.getVol($scope.id)
        .then(onVolComplete, onError);
    };

    var onNewPositionComplete = function(data) {
      $scope.init();
    };

    var onError = function(reason) {
      $scope.error = "Could not fetch the data: " + reason;
    };

    $scope.init = function() {
      portfolioservice.getNMV($scope.id)
        .then(onNMVComplete, onError);
    };

    $scope.addNewPosition = function(response) {
      if (response !== undefined && response.length > 2) {
        $scope.array = response.split(' ');
        if ($scope.array.length == 2 && !isNaN($scope.array[0]) &&
          isNaN($scope.array[1])) {
          portfolioservice.addPosition($scope.id, $scope.array[1],
              $scope.array[0])
            .then(onNewPositionComplete, onError);
        }
      }
    };

    $scope.removeRow = function(symbol) {
      portfolioservice.deletePosition($scope.id, symbol)
        .then(onNewPositionComplete, onError);
    };

    $scope.newPercentile = function(newValue) {
      $scope.percentage = newValue;
      var newPercentile = ($scope.percentage / 100);
      portfolioservice.getVaR($scope.id, newPercentile, $scope.days)
        .then(onVaRComplete, onError);
    };

    $scope.newDays = function(newValue) {
      //console.log(newValue);
      $scope.days = newValue;
      if ($scope.days < 250) {
        $scope.timePeriod = $scope.days + " day";
      } else {
        $scope.timePeriod = "1 year";
      }
      var newPercentile = ($scope.percentage / 100);
      portfolioservice.getVaR($scope.id, newPercentile, $scope.days)
        .then(onVaRComplete, onError);
    };

    $scope.id = $routeParams.id;
    $scope.percentage = 95;
    $scope.days = 1;
    $scope.timePeriod = "1 day";
    $scope.portfolioName = $routeParams.portfolioName;
    $scope.init();

  };

  app.controller("portfoliocontroller", portfoliocontroller);

})();