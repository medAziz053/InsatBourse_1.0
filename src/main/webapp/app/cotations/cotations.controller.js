(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .controller('CotationsController', CotationsController);

    CotationsController.$inject = ['$scope', 'Principal', 'LoginService', '$state','$http'];

    function CotationsController ($scope, Principal, LoginService, $state, $http) {

        $http.get('http://localhost:8080/cotation/')
            .success(function(data){
                $scope.items = data;
            });


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
    }
})();
