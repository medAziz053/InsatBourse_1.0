(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('ActionBourseDialogController', ActionBourseDialogController);

    ActionBourseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ActionBourse', 'Portefeuil'];

    function ActionBourseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ActionBourse, Portefeuil) {
        var vm = this;

        vm.actionBourse = entity;
        vm.clear = clear;
        vm.save = save;
        vm.portefeuils = Portefeuil.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.actionBourse.id !== null) {
                ActionBourse.update(vm.actionBourse, onSaveSuccess, onSaveError);
            } else {
                ActionBourse.save(vm.actionBourse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('insatBourseApp:actionBourseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
