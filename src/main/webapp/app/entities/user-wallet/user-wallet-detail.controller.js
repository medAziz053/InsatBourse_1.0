(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('UserWalletDetailController', UserWalletDetailController);

    UserWalletDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserWallet'];

    function UserWalletDetailController($scope, $rootScope, $stateParams, previousState, entity, UserWallet) {
        var vm = this;

        vm.userWallet = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('insatBourseApp:userWalletUpdate', function(event, result) {
            vm.userWallet = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
