(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('ValeurDialogController', ValeurDialogController);

    ValeurDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Valeur'];

    function ValeurDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Valeur) {
        var vm = this;

        vm.valeur = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.valeur.id !== null) {
                Valeur.update(vm.valeur, onSaveSuccess, onSaveError);
            } else {
                Valeur.save(vm.valeur, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('insatBourseApp:valeurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
