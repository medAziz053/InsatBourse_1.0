(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('ActionBourseDetailController', ActionBourseDetailController);

    ActionBourseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ActionBourse', 'Portefeuil'];

    function ActionBourseDetailController($scope, $rootScope, $stateParams, previousState, entity, ActionBourse, Portefeuil) {
        var vm = this;

        vm.actionBourse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('insatBourseApp:actionBourseUpdate', function(event, result) {
            vm.actionBourse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
