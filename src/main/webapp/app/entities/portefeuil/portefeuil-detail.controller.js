(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('PortefeuilDetailController', PortefeuilDetailController);

    PortefeuilDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Portefeuil', 'User'];

    function PortefeuilDetailController($scope, $rootScope, $stateParams, previousState, entity, Portefeuil, User) {
        var vm = this;

        vm.portefeuil = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('insatBourseApp:portefeuilUpdate', function(event, result) {
            vm.portefeuil = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
