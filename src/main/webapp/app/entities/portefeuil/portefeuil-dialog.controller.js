(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('PortefeuilDialogController', PortefeuilDialogController);

    PortefeuilDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Portefeuil', 'User'];

    function PortefeuilDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Portefeuil, User) {
        var vm = this;

        vm.portefeuil = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.portefeuil.id !== null) {
                Portefeuil.update(vm.portefeuil, onSaveSuccess, onSaveError);
            } else {
                Portefeuil.save(vm.portefeuil, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('insatBourseApp:portefeuilUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
