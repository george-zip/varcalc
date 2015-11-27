(function() {

  var portfolioservice = function($http) {

    //var serviceAddress = "192.168.1.2";
    var serviceAddress = "localhost";

    var getPortfolios = function() {
      return $http.get("http://" + serviceAddress + ":8080/webapi/portfolios")
        .then(function(response) {
          return response.data;
        });
    };

    var addPortfolio = function(pName) {
      var dataObj = {
        "data": {
          "name": pName
        }
      };
      return $http.post("http://" + serviceAddress + ":8080/webapi/portfolios", dataObj)
        .then(function(data, status, headers, config) {
          return data;
        });
    };

    var getAttribute = function(attribute, porfolioId) {
      return $http.get("http://" + serviceAddress + ":8080/webapi/portfolios/" + porfolioId + "/" + attribute)
        .then(function(response) {
          return response.data;
        });
    };

    var getNMV = function(id) {
      return getAttribute("nmv", id);
    };

    var getVol = function(id) {
      return getAttribute("vol", id);
    };

    var getVaR = function(id, percentile, days) {
      return $http.get("http://" + serviceAddress + ":8080/webapi/portfolios/" + id 
      + "/var?var_percentile=" + percentile
      + "&var_days=" + days)
        .then(function(response) {
          return response.data;
        });
    };
    
    var getGMV = function(id) {
      return getAttribute("gmv", id);
    };


    var getPositions = function(id) {
      //console.log("getPositions");
      return $http.get("http://" + serviceAddress + ":8080/webapi/portfolios/" + id + "/positions")
        .then(function(response) {
          return response.data;
        });
    };

    var addPosition = function(id, symbol, quantity) {
      var dataObj = {
        "data": {
          "symbol": symbol,
          "quantity": quantity
        }
      };
      return $http.post("http://" + serviceAddress + ":8080/webapi/portfolios/" + id + "/positions",
          dataObj)
        .then(function(data, status, headers, config) {
          return data;
        });
    };

    var deletePosition = function(id, symbol) {
      var url = "http://" + serviceAddress + ":8080/webapi/portfolios/" + id + "/positions/" + symbol;
      //console.log(url);
      return $http.delete(url)
        .then(function(response) {
          return response.data;
        });
    };

    return {
      getPortfolios: getPortfolios,
      addPortfolio: addPortfolio,
      getNMV: getNMV,
      getVol: getVol,
      getVaR: getVaR,
      getGMV: getGMV,
      getPositions: getPositions,
      addPosition: addPosition,
      deletePosition: deletePosition
    };

  };

  var module = angular.module("portfolioViewer");
  module.factory("portfolioservice", portfolioservice);

}());