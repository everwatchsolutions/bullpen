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
    <div class="center-sign">
        <%@include file="message-alert.jspf" %>
        <a href="/" class="logo pull-left">
            <img src="/assets/images/ats-logo-no-text.png" height="54" alt="Polaris ATS Logo" />
        </a>

        <div class="panel panel-sign">
            <div class="panel-title-sign mt-xl text-right">
                <h2 class="title text-uppercase text-bold m-none"><i class="fa fa-user mr-xs"></i> Complete Password Reset</h2>
            </div>
            <div class="panel-body">

                <form action="/password/reset/complete" method="post">
                    <c:if test="${(param.error != null && SPRING_SECURITY_LAST_EXCEPTION.message != null) || errMessage != null}">
                        <div class="row">
                            <div class="form-group mb-lg">
                                <div class="alert alert-danger">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                    <strong>Oh snap!</strong> <c:choose>
                                        <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message != null}">${SPRING_SECURITY_LAST_EXCEPTION.message}</c:when>
                                        <c:when test="${errMessage != null}">${errMessage}</c:when>
                                        <c:otherwise>An error occurred</c:otherwise>
                                    </c:choose>
                                </div>

                            </div>
                        </div>
                    </c:if>

                    <div class="form-group mb-none">
                        <div class="row">
                            <div class="col-sm-12 mb-lg">
                                <h3>Please enter your new password below</h3>
                            </div>
                        </div>
                    </div>
                    <input name="email" type="hidden" value="${email}"/>
                    <input name="resetToken" type="hidden" value="${resetToken}"/>

                    <div class="form-group mb-none">
                        <div class="row">
                            <div class="col-sm-6 mb-lg">
                                <label>Password</label>
                                <div class="input-group input-group-icon">
                                    <input name="password" type="password" class="form-control input-lg"/>
                                    <span class="input-group-addon">
                                        <span class="icon icon-lg">
                                            <i class="fa fa-lock"></i>
                                        </span>
                                    </span>
                                </div>
                                <!--<input name="password" type="password" class="form-control input-lg" />-->
                            </div>
                            <div class="col-sm-6 mb-lg">
                                <label>Password Confirmation</label>
                                <div class="input-group input-group-icon">
                                    <input name="password_confirm" type="password" class="form-control input-lg"/>
                                    <span class="input-group-addon">
                                        <span class="icon icon-lg">
                                            <i class="fa fa-lock"></i>
                                        </span>
                                    </span>
                                </div>
                                <!--<input name="password_confirm" type="password" class="form-control input-lg" />-->
                            </div>
                        </div>
                    </div>

                    <div class="row pull-right">
                        <div class="col-sm-4 text-right">
                            <button type="submit" class="btn btn-primary hidden-xs">Reset</button>
                            <button type="submit" class="btn btn-primary btn-block btn-lg visible-xs mt-lg">Reset</button>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
            <div class="panel-footer">
                <p class="text-center">Already have an account yet? <a href="/login">Sign In!</a>
            </div>
        </div>

        <p class="text-center text-muted mt-md mb-md">&copy; Copyright 2014. All Rights Reserved.</p>
    </div>
</section>
<!-- end: page -->

<%--<%@include file="content-end-to-script-start.jspf" %>--%>
<%@include file="body-scripts-start.jspf" %>

<!-- Specific Page Vendor -->

<%@include file="body-scripts-end.jspf" %>


<%@include file="body-end.jspf" %>