(function() {
    'use strict';

    angular
        .module('insatBourseApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-wallet', {
            parent: 'entity',
            url: '/user-wallet?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.userWallet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-wallet/user-wallets.html',
                    controller: 'UserWalletController',
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
                    $translatePartialLoader.addPart('userWallet');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-wallet-detail', {
            parent: 'entity',
            url: '/user-wallet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'insatBourseApp.userWallet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-wallet/user-wallet-detail.html',
                    controller: 'UserWalletDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userWallet');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserWallet', function($stateParams, UserWallet) {
                    return UserWallet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-wallet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-wallet-detail.edit', {
            parent: 'user-wallet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-wallet/user-wallet-dialog.html',
                    controller: 'UserWalletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserWallet', function(UserWallet) {
                            return UserWallet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-wallet.new', {
            parent: 'user-wallet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-wallet/user-wallet-dialog.html',
                    controller: 'UserWalletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                balance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-wallet', null, { reload: 'user-wallet' });
                }, function() {
                    $state.go('user-wallet');
                });
            }]
        })
        .state('user-wallet.edit', {
            parent: 'user-wallet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-wallet/user-wallet-dialog.html',
                    controller: 'UserWalletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserWallet', function(UserWallet) {
                            return UserWallet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-wallet', null, { reload: 'user-wallet' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-wallet.delete', {
            parent: 'user-wallet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-wallet/user-wallet-delete-dialog.html',
                    controller: 'UserWalletDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserWallet', function(UserWallet) {
                            return UserWallet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-wallet', null, { reload: 'user-wallet' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
