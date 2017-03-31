(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('UserWalletDialogController', UserWalletDialogController);

    UserWalletDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserWallet'];

    function UserWalletDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserWallet) {
        var vm = this;

        vm.userWallet = entity;
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
            if (vm.userWallet.id !== null) {
                UserWallet.update(vm.userWallet, onSaveSuccess, onSaveError);
            } else {
                UserWallet.save(vm.userWallet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('insatBourseApp:userWalletUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
