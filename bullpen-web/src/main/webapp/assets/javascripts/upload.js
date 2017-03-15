var updateFileProgress = function(message) {
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

(function($) {

    'use strict';

    var uploadInit = function() {

        var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var csrfToken = $("meta[name='_csrf']").attr("content");
        
        var data = {};
        var formId = $("#fileupload").data("form-id");
        if (formId !== undefined) {
            data = $("#"+ formId).serialize();
        }
        
        data[csrfParameter] = csrfToken;

        var autoUpload = $("#fileupload").data("autoupload");
        if (autoUpload === undefined) {
            autoUpload = true;
        }
        $('#fileupload').fileupload({
            url: $("#fileupload").data("upload-url"),
            dataType: 'json',
            autoUpload: autoUpload,
            formData: data,
            maxFileSize: 50000000 // 50 MB
        }).on('fileuploadadd', function(e, data) {
            $('#running-task-list a').click();
            $.each(data.files, function(index, file) {
                addNewRunningTask(file.name, "Uploading file " + file.name);
            });
        }).on('fileuploadprogress', function(e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $.each(data.files, function(index, file) {
                var id = conditionAttrValue(file.name);
                $('#' + id + ' .message.text-dark').text(progress + '%');
                $('#' + id + ' .progress-bar').css('width', progress + '%');
                $('#' + id + ' .progress-bar').attr('aria-valuenow', progress);
            });
        }).on('fileuploadfail', function(e, data) {
            console.log("An upload failed");
            $.each(data.files, function(index, file) {
                var id = conditionAttrValue(file.name);
                $('#' + id + ' .progress-bar').addClass('progress-bar-danger');
                $('#' + id + ' .message.pull-left').text('File ' + file.name + ' Upload Failed');
            });
        }).on('fileuploaddone', function(e, data) {
            console.log("Upload finished");
            $.each(data.result.files, function(index, file) {
                if (file.hasChildProcesses) {
                    var uploadId = file.requestId;
                    addNewRunningTask(uploadId, "Processing file " + file.name);
                    var sub = subscribeToUploadProgress(uploadId, updateFileProgress);
                    uploadSubscriptionMap[uploadId] = sub;
                }
            });
        });

    };

    $(function() {
        uploadInit();
    });

}).apply(this, [jQuery]);
