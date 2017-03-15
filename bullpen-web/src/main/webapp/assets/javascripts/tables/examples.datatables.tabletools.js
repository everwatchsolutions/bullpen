/*
Name: 			Tables / Advanced - Examples
Written by: 	Okler Themes - (http://www.okler.net)
Theme Version: 	1.0.0
*/

(function( $ ) {

	'use strict';

	var datatableInit = function() {

		$('#datatable-tabletools').dataTable({
			sDom: "<'text-right mb-md'T>" + $.fn.dataTable.defaults.sDom,
			oTableTools: {
				sSwfPath: 'assets/vendor/jquery-datatables/extras/TableTools/media/swf/copy_csv_xls_pdf.swf',
				aButtons: [
					{
						sExtends: 'pdf',
						sButtonText: 'PDF'
					},
					{
						sExtends: 'csv',
						sButtonText: 'CSV'
					},
					{
						sExtends: 'xls',
						sButtonText: 'Excel'
					},
					{
						sExtends: 'print',
						sButtonText: 'Print',
						sInfo: 'Please press CTR+P to print or ESC to quit'
					}
				]
			}
		});

	};

	$(function() {
		datatableInit();
	});

}).apply( this, [ jQuery ]);
