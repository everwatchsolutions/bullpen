<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />
<link rel="stylesheet" href="/assets/vendor/jquery-datatables-bs3/assets/css/datatables.css" />
<link rel="stylesheet" href="/static/css/jquery.fileupload.css">

<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->
<div class="row">
    <div class="col-lg-12">
        <!--<a href="/upload" type="button" class="mb-xs mt-xs mr-xs btn btn-success pull-right"><i class="fa fa-plus"></i> Add Candidates</a>-->
        <!--<button type="button" class="btn btn-primary m-xs" >Left</button>-->
        <div class="pull-right">
            <span class="">Click to add Candidates by uploading their Resumes</span>
            <span class="mb-xs mt-xs mr-xs btn btn-success fileinput-button" data-toggle="tooltip" data-placement="left" title="" data-original-title="Click to add Candidates by uploading their Resumes">
                <i class="fa fa-plus"></i>
                <span>Add Candidates...</span>
                <!-- The file input field used as target for the file upload widget -->
                <input id="fileupload" type="file" name="file" data-upload-url="/resume/upload" multiple>
            </span>

        </div>
    </div>
</div>
<section class="panel">
    <header class="panel-heading">
        <div class="panel-actions">
            <a href="#" class="fa fa-caret-down"></a>
            <a href="#" class="fa fa-times"></a>
        </div>

        <h2 class="panel-title">Candidates</h2>
    </header>
    <div class="panel-body">
        <table class="table table-bordered table-striped" id="datatable-ajax">
            <thead>
                <tr>
                    <th width="1%"></th>
                    <th width="20%">Name</th>
                    <th width="25%">Email</th>
                    <th width="10%">Phone</th>
                    <th width="20%">Resume</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</section>
<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/select2/select2.js"></script>
<script src="/assets/vendor/jquery-datatables/media/js/jquery.dataTables.js"></script>
<script src="/assets/vendor/jquery-datatables-bs3/assets/js/datatables.js"></script>
<script src="//cdn.datatables.net/plug-ins/725b2a2115b/api/fnReloadAjax.js"></script>


<script src="/static/js/jquery-ui.min.js"></script> 
<!--<script src="/static/js/tmpl.min.js"></script>--> 
<script src="/static/js/jquery.iframe-transport.js"></script> 
<script src="/static/js/jquery.fileupload.js"></script> 
<script src="/static/js/jquery.fileupload-process.js"></script> 
<script src="/static/js/jquery.fileupload-validate.js"></script> 
<script src="/static/js/jquery.fileupload-ui.js"></script> 
<script src="/assets/javascripts/upload.js"></script>

<%@include file="body-scripts-end.jspf" %>

<script src="/assets/javascripts/candidates.js"></script>

<%@include file="body-end.jspf" %>