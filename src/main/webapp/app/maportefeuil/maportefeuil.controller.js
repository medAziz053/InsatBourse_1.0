(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('MaPortefeuilController', MaPortefeuilController);

    MaPortefeuilController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$http'];

    function MaPortefeuilController ($scope, Principal, LoginService, $state, $http) {

        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        vm.currentUser = null;
        var id = 1;
        var idWallet = 0;

        $http.get('http://localhost:8080/api/currentuser')
            .success(function(data){
                id = data.id;
                $http.get('http://localhost:8080/api/portfeuils/user/'+ id)
                    .success(function(data){
                        $scope.myWallet = data;
                        idWallet = $scope.myWallet.id;

                        $http.get('http://localhost:8080/api/action-bourses/wallet/'+idWallet)
                            .success(function(data){
                                $scope.actions = data;
                            });

                    });
            });

    }
})();
