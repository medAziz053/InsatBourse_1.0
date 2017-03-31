(function() {
    'use strict';
    angular
        .module('insatBourseApp')
        .factory('UserWallet', UserWallet);

    UserWallet.$inject = ['$resource'];

    function UserWallet ($resource) {
        var resourceUrl =  'api/user-wallets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
