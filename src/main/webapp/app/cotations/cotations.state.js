(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('Cotations', {
            parent: 'app',
            url: '/market',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/cotations/cotations.html',
                    controller: 'CotationsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('cotations');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
