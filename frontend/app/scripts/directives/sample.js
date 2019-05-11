'use strict';
define(['tutorFinder'], function(tutorFinder) {

	tutorFinder.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
