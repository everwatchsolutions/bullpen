/*
Name: 			Tables / Ajax - Examples
Written by: 	Okler Themes - (http://www.okler.net)
Theme Version: 	1.0.0
*/

(function( $ ) {

	'use strict';

	var datatableInit = function() {

		$('#datatable-ajax').dataTable({
			bProcessing: true,
			sAjaxSource: 'assets/ajax/ajax-datatables-sample.json'
		});

	};

	$(function() {
		datatableInit();
	});

}).apply( this, [ jQuery ]);