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
<link rel="stylesheet" href="/static/css/jquery.fileupload.css">
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
        <form action="/saveapplication" method="POST">
            <div>
                <div class="form-group mb-lg panel">
                    <div class="panel-heading" align="center">
                        <h2 class="panel-title">Edit Application</h2>
                    </div>
                    <div class="panel-body">

                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                            <label for="name" class="col-2 col-form-label">Application Name</label>
                            <input class="form-control" type="text" value="${application.name}" id="name" name="name">
                        </div> <br>

                        <div class="form-group row col-lg-10" style="padding-left: 30px;">
                            <label for="example-url-input" class="col-2 col-form-label">URL</label>
                            <input class="form-control" type="url"value="${application.url.getAddress()}"  placeholder="https://bullpen.com" name="url" id="url">
                        </div><br>

                        <div class="form-group col-lg-10" style="padding-right: 25px;">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3">${application.description}</textarea>
                        </div>

                          <div class="form-group col-lg-10" style="padding-right: 25px;">
                        <h3>Current Photo</h3>
                        <img height="500px" width="350px" <c:choose><c:when test="${application.screenshot != null}">src="/view/file/${application.screenshot.storageId}"</c:when><c:otherwise>src="/assets/images/image-not-found.png"</c:otherwise></c:choose> class="rounded img-responsive" alt="ScreenShot" />
                           

                            <span class="mb-xs mt-xs mr-xs btn btn-success fileinput-button" data-toggle="tooltip" data-placement="left" title="" data-original-title="Click to Upload a Screenshot">
                                <!--<i class="fa fa-plus"></i>-->
                                <span>Screen Shot</span>
                                <!-- The file input field used as target for the file upload widget -->
                                    <input id="fileupload" type="file" name="file" data-upload-url="/app/screenshot.json?name=${application.name}&description=${application.description}">
                        </span><p>Click to upload a screenshot, your file will automatically be uploaded.</p> </div>
                    <br>

                    <div class="form-group row col-lg-10" style="padding-left: 30px;">
                        <h2>Point of Contact Information</h2> <a href="#" onclick="alert('feature not finished')" class="btn btn-primary"><i class="fa fa-plus" aria-hidden="true"></i></a>
                        <p>Click the plus to add more than one point of contact.</p> </div>
                    <div class="form-group row col-lg-10" style="padding-left: 30px;">
                        <label for="name" class="col-2 col-form-label">POC Name</label>
                        <input class="form-control" type="text"  id="name" value="${poc.name}" name="poc[][name]">
                    </div> <br>

                    <div class="form-group row col-lg-10" style="padding-left: 30px;">
                        <label for="example-url-input" class="col-2 col-form-label">POC Email</label>
                        <input class="form-control" type="email" value="${poc.email}" name="poc[][email]" id="email">
                    </div><br>

                    <div class="form-group row col-lg-10" style="padding-left: 30px;">
                        <label for="phone" class="col-2 col-form-label">POC Phone Number</label>
                        <input class="form-control" type="phone" value="${poc.phone}"  id="phone" name="poc[][phone]">
                    </div>
                    <br>

                    <div class="form-group mb-lg col-lg-12">

                        <div class="input-group input-group-icon">
                            <div class="pull-right">
                                <button type="submit" class="btn btn-success">Update Application!</button>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="id" value="${application.id}"/>


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
<script src="/assets/vendor/summernote/summernote.js"></script>
<script src="/assets/vendor/select2/select2.js"></script>
<script src="/assets/vendor/jquery-autosize/jquery.autosize.js"></script>
<script src="/static/js/jquery-ui.min.js"></script> 
<script src="/static/js/jquery.iframe-transport.js"></script> 
<script src="/static/js/jquery.fileupload.js"></script> 
<script src="/static/js/jquery.fileupload-process.js"></script> 
<script src="/static/js/jquery.fileupload-validate.js"></script> 
<script src="/static/js/jquery.fileupload-ui.js"></script> 
<script src="/assets/javascripts/upload.js"></script>



<%@include file="body-end.jspf" %>

<script type="text/javascript">
                            $(document).ready(function () {


                            });
</script>