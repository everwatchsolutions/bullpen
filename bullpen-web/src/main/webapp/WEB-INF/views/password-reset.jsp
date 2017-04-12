<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />
<link rel="stylesheet" href="/assets/vendor/jquery-datatables-bs3/assets/css/datatables.css" />

<%@include file="header-end.jspf" %>
<%@include file="body-start.jspf" %>


<!-- start: page -->
<section class="body-sign">
    <div class="center-sign" align="center">
        <%@include file="message-alert.jspf" %>
        <a href="/" class="logo row">
            <img src="/assets/images/logo.png" height="60" alt="Bullpen Logo" />
        </a>

        <div class="panel">
                <form action="/password/reset" method="post">
                    <c:if test="${(param.error != null && SPRING_SECURITY_LAST_EXCEPTION.message != null) || errMessage != null}">
                        <div class="row">
                            <div class="form-group mb-lg">
                                <div class="alert alert-danger">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">�</button>
                                    <strong>Oh snap!</strong> <c:choose>
                                        <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message != null}">${SPRING_SECURITY_LAST_EXCEPTION.message}</c:when>
                                        <c:when test="${errMessage != null}">${errMessage}</c:when>
                                        <c:otherwise>An error occurred</c:otherwise>
                                    </c:choose>
                                </div>

                            </div>
                        </div>
                    </c:if>

                    <div class="form-group mb-lg">
                        <label>Enter your E-mail Address to reset your password.</label>
                        <div class="input-group input-group-icon">
                            <input name="email" type="email" class="form-control input-lg" placeholder="ex: frodo.baggins@theshire.net"/>
                            <span class="input-group-addon">
                                <span class="icon icon-lg">
                                    <i class="fa fa-envelope"></i>
                                </span>
                            </span>
                        </div>
                    </div>

                    
                    <div class="row">
                            <button type="submit" class="btn btn-danger hidden-xs">Reset</button>
                            <button type="submit" class="btn btn-danger btn-block btn-lg visible-xs mt-lg">Reset</button>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            <hr>
                <p class="text-center">Remember your password? <a href="/login">Sign In!</a>
        </div>
    </div>
</section>
<!-- end: page -->

<%--<%@include file="content-end-to-script-start.jspf" %>--%>
<%@include file="body-scripts-start.jspf" %>

<!-- Specific Page Vendor -->

<%@include file="body-scripts-end.jspf" %>


<%@include file="body-end.jspf" %>