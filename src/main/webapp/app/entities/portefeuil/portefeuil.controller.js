(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('PortefeuilController', PortefeuilController);

    PortefeuilController.$inject = ['$scope', '$state', 'UserPortefeuil'];

    function PortefeuilController ($scope, $state, Portefeuil,UserPortefeuil) {
        var vm = this;

        vm.portefeuils = [];

        loadAll();

        function loadAll() {
            Portefeuil.query(function(result) {
                vm.portefeuils = result;
            });
        }
    }
})();
