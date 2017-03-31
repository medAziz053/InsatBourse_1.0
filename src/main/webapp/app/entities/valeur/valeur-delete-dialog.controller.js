(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('ValeurDeleteController',ValeurDeleteController);

    ValeurDeleteController.$inject = ['$uibModalInstance', 'entity', 'Valeur'];

    function ValeurDeleteController($uibModalInstance, entity, Valeur) {
        var vm = this;

        vm.valeur = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Valeur.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
