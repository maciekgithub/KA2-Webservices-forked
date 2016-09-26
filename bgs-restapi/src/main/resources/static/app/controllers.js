'use strict';

angular.module("myApp")
    .controller('GenapiController', ["$scope", "$http",
        function ($scope, $http) {
            $scope.sub = {};
            $scope.req = {msisdn: true, clientId: true};

            $scope.addSubscription = function () {
                $http({ method: 'GET', url: '/activateSubscription/'+$scope.sub.msisdn+'/' + $scope.sub.clientId})
                    .then(displayResult, displayResult);
            };

            $scope.deleteSubscription = function () {
                $http({ method: 'GET', url: '/deactivateSubscription/'+$scope.sub.msisdn+'/' + $scope.sub.clientId})
                    .then(displayResult, displayResult);
            };
            
            $scope.getSubscriptions = function () {
                $http({ method: 'GET', url: '/subscriptions/' +$scope.sub.msisdn})
                    .then(displayResult, displayResult);
            };

            $scope.isSubscriptionExist = function () {
                $http({ method: 'GET', url: '/isSubscriptionExist/'+$scope.sub.msisdn+'/' +$scope.sub.clientId})
                    .then(displayResult, displayResult);
            };

            $scope.getMsisdnsForSubscription = function () {
                $http({ method: 'GET', url: '/msisdns/' + $scope.sub.clientId}).then(displayResult, displayResult);
            };

            $scope.getMsisdnsForSubscriptionByArea = function () {
                $http({ method: 'GET', url: '/msisdnsByArea/' + $scope.sub.clientId + '/' + $scope.sub.area})
                    .then(displayResult, displayResult);
            };

            $scope.executeAction = function () {
              $scope.selectedOption.action();
            };
            
            $scope.setRequiredFields = function () {
                $scope.req = $scope.selectedOption.req;
            };

            $scope.options = [
                { name: "Dodaj subskrypcje", action: $scope.addSubscription, req: {msisdn: true, clientId: true, area: false} },
                { name: "Usun subskrypcje", action: $scope.deleteSubscription, req: {msisdn: true, clientId: true, area: false} },
                { name: "Pobierz subskrypcje", action: $scope.getSubscriptions, req: {msisdn: true, clientId: false, area: false} },
                { name: "Sprawdz czy istnieje", action: $scope.isSubscriptionExist, req: {msisdn: true, clientId: true, area: false} },
                { name: "Pobierz msisdns", action: $scope.getMsisdnsForSubscription, req: {msisdn: false, clientId: true, area: false}},
                { name: "Pobierz msisdns wg terytorium", action: $scope.getMsisdnsForSubscriptionByArea, req: {msisdn: false, clientId: true, area: true}}
            ];
            $scope.selectedOption = $scope.options[0];

            var displayResult = function successCallback(response) {
                $scope.response = response.data;
            };
            
        }]);