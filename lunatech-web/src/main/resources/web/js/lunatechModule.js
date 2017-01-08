angular.module('lunatechModule', [ 'ngRoute' ])

.config([ '$routeProvider', '$httpProvider',
		function($routeProvider, $httpProvider) {

			$routeProvider.when('/main', {
                templateUrl : 'partials/main.html',
                controller : 'mainController'
            }).when('/query', {
				templateUrl : 'partials/query.html',
				controller : 'queryController'
			}).when('/report', {
                templateUrl : 'partials/report.html',
                controller : 'reportController'
            }).otherwise({
				redirectTo : '/main'
			});
		} ])

.controller('mainController', [ '$scope', function($scope) {

}])

.controller('queryController', [ '$scope','$http', function($scope,$http) {

    $scope.data ={}

    $scope.search=function() {
        if ( $scope.data.search == null || $scope.data.search=='') {
            return;
        }

        $http.get('/rs/searchAirportsWithRunwaysByCountry/'+$scope.data.search).then(function(d) {
    	    $scope.result=d.data;
    	})
    }
}])

.controller('reportController', [ '$scope','$http', function($scope,$http) {

	$http.get('/rs/report').then(function(d) {
	    $scope.report=d.data;
	})
}]);