(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('maportefeuil', {
            parent: 'app',
            url: '/maportefeuil',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/maportefeuil/maportefeuil.html',
                    controller: 'MaPortefeuilController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
