<%--
  Created by IntelliJ IDEA.
  User: Myles
  Date: 1/12/17
  Time: 9:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@include file="header-start.jspf" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/summernote/summernote.css" />
<link rel="stylesheet" href="/assets/vendor/summernote/summernote-bs3.css" />
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />

<c:set var="normal" scope="session" value="${'USER'}"/>

<%@include file="header-end-to-content-start.jspf" %>

<div class="col-lg-12">
    <div class="spacer"></div>
    <section class="panel panel-transparent">
        <div class="panel-body">
            <div class="form-group mb-lg panel">
                <div class="panel-heading" align="center">
                    <h2 class="panel-title">Manage Equipment</h2>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped mb-none">
                            <%@include file="add-equipment.jspf" %>
                            <thead>
                            <tr>
                                <th>Equipment</th>
                                <th>Model</th>
                                <th>Serial No.</th>
                                <th>Date Purchased</th>
                                <th>Assigned To:</th>
                                <c:if test="${user.getAuthority() != normal}">
                                <th><a href="#addEquipment" class="modal-with-form btn btn-primary pull-right">Add Equipment</a></th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${equipmentList}" varStatus="tableItems">
                                <tr>
                                    <td>${item.getType()}</td>
                                    <td><strong>${item.getModel()}</strong></td>
                                    <td>${item.getSerial()}</td>
                                    <td>${item.getDatePurchasedString()}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.getAuthority() != normal}">
                                                <c:choose>
                                                    <c:when test="${empty item.getAssignedTo()}">
                                                        <a href="/equipment/${item.getId()}">Click to Assign</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="/equipment/${item.getId()}">${item.getAssignedTo()}</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>

                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${empty item.getAssignedTo()}">
                                                        <p>Available</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p>${item.getAssignedTo()}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <c:if test="${user.getAuthority() != normal && empty item.getAssignedTo()}">
                                        <td><a class="pull-right" data-eq="${item.getId()}" href="#">Remove Item</a></td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <hr/>
                    <h5 align="center">Product Licenses</h5>

                    <%--\\\\\\\\\\\\\\\\\\\\\\ LICENSE TABLE///////////////////////--%>
                    <div class="table-responsive">
                        <table class="table table-striped mb-none">
                            <thead>
                            <%@include file="add-license.jspf" %>
                            <tr></tr>
                            <tr>
                                <th>Product Name</th>
                                <th>License Key</th>
                                <th>Expiration Date:</th>
                                <c:if test="${user.getAuthority() != normal}">
                                <th><a href="#addLicense" class="modal-with-form btn btn-primary pull-right">Add a License</a></th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${lcList}">
                                <tr>

                                    <td>${item.getProductName()}</td>
                                    <td><strong>${item.getLicenseKey()}</strong></td>
                                    <td>${item.getExpDateString()}</td>
                                    <c:if test="${user.getAuthority() != normal}">
                                    <td><a data-lc="${item.getId()}" href="#" class="pull-right">Remove License</a></td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
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
<script src="/assets/vendor/magnific-popup/magnific-popup.js"></script>
<script src="/assets/javascripts/equipment.js"></script>


<%@include file="body-scripts-end.jspf" %>


<%@include file="body-end.jspf" %>
