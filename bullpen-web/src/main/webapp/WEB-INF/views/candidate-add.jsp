<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />
<link rel="stylesheet" href="/static/css/jquery.fileupload.css">

<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->
<section class="panel">
    <div class="candidate-diagram panel-body ">
        <div class="col-lg-12 text-center">
            <h2>Adding Candidates is Easy!</h2>
        </div> 
        <div class="">
            <div class="col-md-12 span12">


                <div class="center">
                    <div class="diagram">
                        <div class="diagram-col center-block">


                            <div class="vert-center">
                                <h4>Simply Upload a Resume</h4><hr>
                                <!--<form id="upload" name="upload" method="post" action="/resume/upload" enctype="multipart/form-data"> --->
                                First Name<input id="first" name="first" class="form-control " type="text" />Last Name<input id="last" name="last" class="form-control "  type="text"/>
                                <label class="btn btn-primary" style="margin-top: 10px;">
                                    Upload File <input id="fileupload" type="file" name="file" data-upload-url="/resume/upload.json" multiple style="display: none;" />
                                </label>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                              
                                <!----->
                                </form>
                            </div>
                        </div>
                    </div> 




                    <div class="center" style="padding-top:20px;">

                        <div class="diagram"> <hr>
                            <h2>The Process</h2>
                            <div class="col-lg-3" >
                                <div class="diagram-col center-block">
                                    <div class="vert-center">
                                        <h4>Candidate's Resume Uploads</h4>
                                        <span class="fa fa-fw fa-file" style="font-size: 75pt;"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2" >
                                <div class="diagram-col center-block">

                                    <div class="vert-center">
                                        <i class="fa fa-arrow-right" aria-hidden="true" style="font-size: 40pt;"></i>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <div class="diagram-col center-block">

                                    <div class="vert-center">
                                        <h4>We Gather the Important Information</h4>    
                                        <i class="fa fa-fw fa-cloud" style="font-size: 75pt;"></i>
                                        <span style="color: white;">
                                            <i class="fa fa-gears" style="font-size: 25pt;position: absolute;top: 60px;left: 65px;"></i>
                                            <span style="display: block; font-size: 10pt; position: absolute;top: 105px;left: 55px;">Resume Parser</span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-2" >
                                <div class="diagram-col center-block">

                                    <div class="vert-center">
                                        <i class="fa fa-arrow-right" style="font-size: 40pt;" aria-hidden="true"></i>

                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="diagram-col center-block">

                                    <div class="vert-center">
                                        <h4>We'll Generate a Candidate Profile</h4>
                                        <i class="fa fa-fw fa-user" style="font-size: 75pt;"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>    
</section>

<div >
    <section id="candidate-upload-panel" class="panel mfp-hide">
        <header class="panel-heading">
            Candidate Upload Progress
        </header>
        <div id="added-candidate-list" class="panel-body" >

        </div>
    </section>
</div>

<!--<a class="btn btn-primary" onclick="handleFileProcessingComplete({taskName: 'Parsing Resume test.pdf', taskDetail: {url: '/candidate/890453', name: 'Test Man'}})">Test</a>-->
<script id="candidate-added-template" type="text/x-handlebars-template">
    <%@include file="templates/candidate/display/candidate-added.jspf" %>
</script>
<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->

<script src="/assets/vendor/select2/select2.js"></script>

<script src="/static/js/jquery-ui.min.js"></script> 
<!--<script src="/static/js/tmpl.min.js"></script>--> 
<script src="/static/js/jquery.iframe-transport.js"></script> 
<script src="/static/js/jquery.fileupload.js"></script> 
<script src="/static/js/jquery.fileupload-process.js"></script> 
<script src="/static/js/jquery.fileupload-validate.js"></script> 
<script src="/static/js/jquery.fileupload-ui.js"></script> 
<!--<script src="/assets/javascripts/upload.js"></script>-->

<%@include file="body-scripts-end.jspf" %>

<script src="/assets/javascripts/candidate-add.js"></script>
<script>

    var updateFileProgress = function (message) {
        // called when the client receives a STOMP message from the server
        if (message.body) {
            console.log("got message with body " + message.body);
            var taskProgress = JSON.parse(message.body).payload;
            var progress = taskProgress.percentComplete;
            var id = conditionAttrValue(taskProgress.taskId);
            $('#' + id + ' .message.text-dark').text(progress + '%');
            $('#' + id + ' .progress-bar').css('width', progress + '%');
            $('#' + id + ' .progress-bar').attr('aria-valuenow', progress);

            if (taskProgress.complete) {
                var sub = uploadSubscriptionMap[taskProgress.taskId];
                unsubscribeFromUploadProgress(sub);

//            $('#datatable-ajax').dataTable().fnReloadAjax();
                handleFileProcessingComplete(taskProgress);
            }
            if (taskProgress.error) {
                $('#' + id + ' .progress-bar').addClass('progress-bar-danger');
                $('#' + id + ' .message.pull-left').text(taskProgress.status);
            }
        } else {
            console.log("got empty message");
        }
    };

    var uploadSubscriptionMap = {};

    (function ($) {


        'use strict';

        var uploadInit = function () {
             $('#fileupload').click(function () {
            var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
            var csrfToken = $("meta[name='_csrf']").attr("content");

            var data = {};
            var formId = $("#fileupload").data("form-id");
            if (formId !== undefined) {
                data = $("#" + formId).serialize();
            }

            data[csrfParameter] = csrfToken;
            data['firstName'] = "First";
            data['lastName'] = "Last";

            var autoUpload = $("#fileupload").data("autoupload");
            if (autoUpload === undefined) {
                autoUpload = true;
            }

            
           
                data['firstName'] = $('#first').val();
                data['lastName'] = $('#last').val();
               
            


            $('#fileupload').fileupload({
                url: $("#fileupload").data("upload-url"),
                dataType: 'json',
                autoUpload: autoUpload,
                formData: data,
                maxFileSize: 50000000 // 50 MB
            }).on('fileuploadadd', function (e, data) {
                $('#running-task-list a').click();
                $.each(data.files, function (index, file) {
                    addNewRunningTask(file.name, "Uploading file " + file.name);
                });
            }).on('fileuploadprogress', function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $.each(data.files, function (index, file) {
                    var id = conditionAttrValue(file.name);
                    $('#' + id + ' .message.text-dark').text(progress + '%');
                    $('#' + id + ' .progress-bar').css('width', progress + '%');
                    $('#' + id + ' .progress-bar').attr('aria-valuenow', progress);
                });
            }).on('fileuploadfail', function (e, data) {
                console.log("An upload failed");
                $.each(data.files, function (index, file) {
                    var id = conditionAttrValue(file.name);
                    $('#' + id + ' .progress-bar').addClass('progress-bar-danger');
                    $('#' + id + ' .message.pull-left').text('File ' + file.name + ' Upload Failed');
                });
            }).on('fileuploaddone', function (e, data) {
                console.log("Upload finished");
                $.each(data.result.files, function (index, file) {
                    if (file.hasChildProcesses) {
                        var uploadId = file.requestId;
                        addNewRunningTask(uploadId, "Processing file " + file.name);
                        var sub = subscribeToUploadProgress(uploadId, updateFileProgress);
                        uploadSubscriptionMap[uploadId] = sub;
                    }
                });
            });
        });
        };

        $(function () {
            uploadInit();
        });

    }).apply(this, [jQuery]);

</script>

<%@include file="body-end.jspf" %>