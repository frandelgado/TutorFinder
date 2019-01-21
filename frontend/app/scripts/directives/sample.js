'use strict';
define(['tutor-finder'], function(tutor-finder) {

	tutor-finder.directive('sample', function() {
		return {
			restrict: 'E',
			template: '<span>Sample</span>'
		};
	});
});
