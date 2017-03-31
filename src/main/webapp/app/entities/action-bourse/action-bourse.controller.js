(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('ActionBourseController', ActionBourseController);

    ActionBourseController.$inject = ['$scope', '$state', 'ActionBourse'];

    function ActionBourseController ($scope, $state, ActionBourse) {
        var vm = this;
        
        vm.actionBourses = [];

        loadAll();

        function loadAll() {
            ActionBourse.query(function(result) {
                vm.actionBourses = result;
            });
        }
    }
})();
