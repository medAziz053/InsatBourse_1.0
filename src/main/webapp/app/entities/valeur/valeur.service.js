(function() {
    'use strict';
    angular
        .module('insatBourseApp')
        .factory('Valeur', Valeur);

    Valeur.$inject = ['$resource'];

    function Valeur ($resource) {
        var resourceUrl =  'api/valeurs/:id';

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
