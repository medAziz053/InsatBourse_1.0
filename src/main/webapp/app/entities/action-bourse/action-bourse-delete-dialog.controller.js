(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('ActionBourseDeleteController',ActionBourseDeleteController);

    ActionBourseDeleteController.$inject = ['$uibModalInstance', 'entity', 'ActionBourse'];

    function ActionBourseDeleteController($uibModalInstance, entity, ActionBourse) {
        var vm = this;

        vm.actionBourse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ActionBourse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
