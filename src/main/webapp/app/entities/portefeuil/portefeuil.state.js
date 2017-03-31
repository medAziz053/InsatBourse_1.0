(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('portefeuil', {
            parent: 'entity',
            url: '/portefeuil',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.portefeuil.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/portefeuil/portefeuils.html',
                    controller: 'PortefeuilController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('portefeuil');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('portefeuil-detail', {
            parent: 'entity',
            url: '/portefeuil/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.portefeuil.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/portefeuil/portefeuil-detail.html',
                    controller: 'PortefeuilDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('portefeuil');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Portefeuil', function($stateParams, Portefeuil) {
                    return Portefeuil.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'portefeuil',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('portefeuil-detail.edit', {
            parent: 'portefeuil-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/portefeuil/portefeuil-dialog.html',
                    controller: 'PortefeuilDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Portefeuil', function(Portefeuil) {
                            return Portefeuil.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('portefeuil.new', {
            parent: 'portefeuil',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/portefeuil/portefeuil-dialog.html',
                    controller: 'PortefeuilDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                solde: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('portefeuil', null, { reload: 'portefeuil' });
                }, function() {
                    $state.go('portefeuil');
                });
            }]
        })
        .state('portefeuil.edit', {
            parent: 'portefeuil',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/portefeuil/portefeuil-dialog.html',
                    controller: 'PortefeuilDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Portefeuil', function(Portefeuil) {
                            return Portefeuil.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('portefeuil', null, { reload: 'portefeuil' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('portefeuil.delete', {
            parent: 'portefeuil',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/portefeuil/portefeuil-delete-dialog.html',
                    controller: 'PortefeuilDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Portefeuil', function(Portefeuil) {
                            return Portefeuil.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('portefeuil', null, { reload: 'portefeuil' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
