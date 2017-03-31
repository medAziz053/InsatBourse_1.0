(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('valeur', {
            parent: 'entity',
            url: '/valeur?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.valeur.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/valeur/valeurs.html',
                    controller: 'ValeurController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('valeur');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('valeur-detail', {
            parent: 'entity',
            url: '/valeur/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.valeur.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/valeur/valeur-detail.html',
                    controller: 'ValeurDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('valeur');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Valeur', function($stateParams, Valeur) {
                    return Valeur.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'valeur',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('valeur-detail.edit', {
            parent: 'valeur-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valeur/valeur-dialog.html',
                    controller: 'ValeurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Valeur', function(Valeur) {
                            return Valeur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('valeur.new', {
            parent: 'valeur',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valeur/valeur-dialog.html',
                    controller: 'ValeurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                qty: null,
                                price: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('valeur', null, { reload: 'valeur' });
                }, function() {
                    $state.go('valeur');
                });
            }]
        })
        .state('valeur.edit', {
            parent: 'valeur',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valeur/valeur-dialog.html',
                    controller: 'ValeurDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Valeur', function(Valeur) {
                            return Valeur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('valeur', null, { reload: 'valeur' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('valeur.delete', {
            parent: 'valeur',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/valeur/valeur-delete-dialog.html',
                    controller: 'ValeurDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Valeur', function(Valeur) {
                            return Valeur.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('valeur', null, { reload: 'valeur' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
