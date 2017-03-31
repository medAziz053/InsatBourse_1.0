(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('UserWalletDeleteController',UserWalletDeleteController);

    UserWalletDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserWallet'];

    function UserWalletDeleteController($uibModalInstance, entity, UserWallet) {
        var vm = this;

        vm.userWallet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserWallet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
