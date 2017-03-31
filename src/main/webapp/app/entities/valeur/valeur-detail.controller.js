(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('ValeurDetailController', ValeurDetailController);

    ValeurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Valeur'];

    function ValeurDetailController($scope, $rootScope, $stateParams, previousState, entity, Valeur) {
        var vm = this;

        vm.valeur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('insatBourseApp:valeurUpdate', function(event, result) {
            vm.valeur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
