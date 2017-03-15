<%--
  Created by IntelliJ IDEA.
  User: Myles
  Date: 1/15/17
  Time: 10:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/summernote/summernote.css" />
<link rel="stylesheet" href="/assets/vendor/summernote/summernote-bs3.css" />
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />

<%@include file="header-end-to-content-start.jspf" %>

<c:set var="normal" scope="session" value="${'USER'}"/>
<div class="col-lg-12">
    <div class="spacer"></div>
    <section class="panel panel-transparent">
        <div class="panel-body">

            <div class="form-group mb-lg panel">
                <div class="panel-heading">
                    <h2 class="panel-title">Assign ${equipment.getModel()} (${equipment.getSerial()})</h2>
                </div>
                <div class="panel-body">

                    <c:if test="${empty equipment.getAssignedTo()}">
                        <form action="/equipment/assign/${equipment.getId()}" method="post" enctype="multipart/form-data">
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>First Name</label>
                                    <input name="first" type="text" class="form-control"/>
                                </div>
                                <div class="col-lg-3">
                                    <label>Last Name</label>
                                    <input name="last" type="text" class="form-control"/>
                                </div>
                                <div class="col-lg-3">
                                    <label>Main Use</label>
                                    <select data-plugin-selectTwo name="mainUse" name="title" type="text" class="form-control ">
                                        <option>Corporate</option>
                                        <option>Contract</option>
                                        <option>Independent Research & Dev</option>
                                        <option>Other</option>
                                    </select>
                                </div>
                            </div>

                            <hr>

                            <div class="row">
                                <div class="col-lg-5">
                                    <p><i class="fa fa-folder-open-o" aria-hidden="true"></i>  <sub>Please attach a copy of the borrower's signed agreement.</sub></p>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-4">
                                    <input type="file" name="equipment-agreement">
                                </div>
                                <div class="col-lg-3">
                                    <button type="submit" class="btn btn-success">Assign</button>
                                </div>
                            </div>

                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </c:if>

                    <c:if test="${not empty equipment.getAssignedTo()}">
                        <h4>This item was assigned to ${equipment.getAssignedTo()} on ${equipment.getDateAssignedString()}.</h4>
                        <a href="/view/file/${equipment.getAgreement().getStorageId()}">Download the agreement</a>
                        <br/>
                        <div class=" mt-xlg mb-md">
                            <c:choose>
                                <c:when test="${agreementViewUrl != null}">
                                    <iframe src="${agreementViewUrl}?theme=dark" width="100%" height="800" allowfullscreen="allowfullscreen" style="border: none;"></iframe>
                                </c:when>
                                <c:otherwise>
                                    <p>Sorry,we could not load the agreement. Try downloading the original and opening it on your computer!</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <br/>
                        <form action="/equipment/clear/${equipment.getId()}" method="post">
                            <button type="submit" class="btn btn-danger">Clear Assignment</button>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>

                    </c:if>

                </div>
            </div>
        </div>
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
