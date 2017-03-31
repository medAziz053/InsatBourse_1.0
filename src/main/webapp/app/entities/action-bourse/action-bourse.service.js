(function() {
    'use strict';
    angular
        .module('insatBourseApp')
        .factory('ActionBourse', ActionBourse);

    ActionBourse.$inject = ['$resource'];

    function ActionBourse ($resource) {
        var resourceUrl =  'api/action-bourses/:id';

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
