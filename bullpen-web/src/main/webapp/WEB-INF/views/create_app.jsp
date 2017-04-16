<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/summernote/summernote.css" />
<link rel="stylesheet" href="/assets/vendor/summernote/summernote-bs3.css" />
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- Specific Page Vendor CSS -->
<script src="/assets/vendor/jquery/jquery.js"></script>
<script src="/assets/vendor/jquery-browser-mobile/jquery.browser.mobile.js"></script>
<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->



<div class="col-lg-12">
    <div class="spacer"></div>

    <section class="panel panel-transparent">
        <form action="/createapplication" method="POST">
            <div>
                <div class="form-group mb-lg panel">
                    <div class="panel-heading" align="center">
                        <h2 class="panel-title">Add Application</h2>
                    </div>
                    <div class="panel-body">

                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                            <label for="name" class="col-2 col-form-label">Application Name</label>
                            <input class="form-control" type="text"  id="name" name="name">
                        </div> <br>

                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                            <label for="example-url-input" class="col-2 col-form-label">URL</label>
                            <input class="form-control" type="url" placeholder="https://bullpen.com" name="url" id="url">
                        </div><br>

                        <div class="form-group col-lg-10" style="padding-right: 25px;">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                        </div><br>
                        
                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                        <h2>Point of Contact Information</h2> <a href="#" onclick="alert('feature not finished')" class="btn btn-primary"><i class="fa fa-plus" aria-hidden="true"></i></a>
                        <p>Click the plus to add more than one point of contact.</p> </div>
                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                            <label for="name" class="col-2 col-form-label">POC Name</label>
                            <input class="form-control" type="text"  id="name" name="poc[][name]">
                        </div> <br>

                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                            <label for="example-url-input" class="col-2 col-form-label">POC Email</label>
                            <input class="form-control" type="email"  name="poc[][email]" id="email">
                        </div><br>
                        
                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                            <label for="phone" class="col-2 col-form-label">POC Phone Number</label>
                            <input class="form-control" type="phone"  id="phone" name="poc[][phone]">
                        </div>
                        <br>
                        <p>You will be able to upload a screenshot after you create the app.</p>

                        <div class="form-group mb-lg col-lg-12">

                            <div class="input-group input-group-icon">
                                <div class="pull-right">
                                    <button type="submit" class="btn btn-success">Create Application!</button>
                                </div>
                            </div>
                        </div>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>


                    </div>
                </div>
            </div>
                        
                        
            </div>
        </form>
    </section>
</div>

<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/summernote/summernote.js"></script>
<script src="/assets/vendor/select2/select2.js"></script>
<script src="/assets/vendor/ios7-switch/ios7-switch.js"></script>

<%@include file="body-scripts-end.jspf" %>

<%@include file="body-end.jspf" %>


<script type="text/javascript">
    $(document).ready(function () {
        var copyOfUpload = $('#fileupload');
        $('<input>').attr({
            type: 'hidden',
            id: 'fileSize',
            name: 'fileSize',
            value: "0"
        }).appendTo('form');
        $('#fileupload').on('change', function () {
            var filename = $('#fileupload').val().split('\\').pop();
            $('#resumeButton').text('Selected Resume: ' + filename);
            var size = this.files[0].size / 1024 / 1024;

            if (size > 1.9)
            {
                $("#fileupload").val('');
            }
            $('#fileSize').val(size);

        });

    });
</script>