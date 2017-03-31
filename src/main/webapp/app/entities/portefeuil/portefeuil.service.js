(function() {
    'use strict';
    angular
        .module('insatBourseApp')
        .factory('Portefeuil', Portefeuil);

    Portefeuil.$inject = ['$resource'];

    function Portefeuil ($resource) {
        var resourceUrl =  'api/portefeuils/:id';

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
