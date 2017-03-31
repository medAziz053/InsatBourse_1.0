(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('PortefeuilDeleteController',PortefeuilDeleteController);

    PortefeuilDeleteController.$inject = ['$uibModalInstance', 'entity', 'Portefeuil'];

    function PortefeuilDeleteController($uibModalInstance, entity, Portefeuil) {
        var vm = this;

        vm.portefeuil = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Portefeuil.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
