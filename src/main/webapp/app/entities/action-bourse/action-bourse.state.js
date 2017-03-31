(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('action-bourse', {
            parent: 'entity',
            url: '/action-bourse',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.actionBourse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action-bourse/action-bourses.html',
                    controller: 'ActionBourseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('actionBourse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('action-bourse-detail', {
            parent: 'entity',
            url: '/action-bourse/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.actionBourse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/action-bourse/action-bourse-detail.html',
                    controller: 'ActionBourseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('actionBourse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ActionBourse', function($stateParams, ActionBourse) {
                    return ActionBourse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'action-bourse',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('action-bourse-detail.edit', {
            parent: 'action-bourse-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-bourse/action-bourse-dialog.html',
                    controller: 'ActionBourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActionBourse', function(ActionBourse) {
                            return ActionBourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('action-bourse.new', {
            parent: 'action-bourse',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-bourse/action-bourse-dialog.html',
                    controller: 'ActionBourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                price: null,
                                quantity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('action-bourse', null, { reload: 'action-bourse' });
                }, function() {
                    $state.go('action-bourse');
                });
            }]
        })
        .state('action-bourse.edit', {
            parent: 'action-bourse',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-bourse/action-bourse-dialog.html',
                    controller: 'ActionBourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActionBourse', function(ActionBourse) {
                            return ActionBourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('action-bourse', null, { reload: 'action-bourse' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('action-bourse.delete', {
            parent: 'action-bourse',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/action-bourse/action-bourse-delete-dialog.html',
                    controller: 'ActionBourseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ActionBourse', function(ActionBourse) {
                            return ActionBourse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('action-bourse', null, { reload: 'action-bourse' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
