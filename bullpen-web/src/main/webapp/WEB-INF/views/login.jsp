<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->

<%@include file="header-end.jspf" %>
<%@include file="body-start.jspf" %>


<!-- start: page -->
<section class="body-sign">
    <div class="center-sign" align="center">
        <%@include file="message-alert.jspf" %>
        <a href="/" class="logo row">
            <img src="/assets/images/logo.png" height="60" alt="Polaris ATS Logo" />
        </a>
        <div class="login-form">
            <form action="/login" method="post">
                <c:if test="${(param.error != null && SPRING_SECURITY_LAST_EXCEPTION.message != null) || errMessage != null}">
                    <div class="row">
                        <div class="form-group mb-lg">
                            <div class="alert alert-danger">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true"></button>
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
                    <label>Your Email</label>
                    <div class="input-group input-group-icon">
                        <input name="username" type="email" class="form-control input-lg" placeholder="you@example.com" autofocus="autofocus" style="text-align:center;"/>
                        <span class="input-group-addon">
                                <span class="icon icon-lg">
                                    <i class="fa fa-envelope"></i>
                                </span>
                            </span>
                    </div>
                </div>

                <div class="form-group mb-lg">
                    <div class="clearfix">
                        <label>Password</label>
                    </div>
                    <div class="input-group input-group-icon">
                        <input name="password" type="password" class="form-control input-lg" style="text-align:center;" />
                        <span class="input-group-addon">
                                <span class="icon icon-lg">
                                    <i class="fa fa-lock"></i>
                                </span>
                            </span>
                    </div>
                </div>

                <div class="row">
                    <button type="submit" class="btn btn-default hidden-xs">Sign In</button>
                    <button type="submit" class="btn btn-default btn-block btn-lg visible-xs mt-lg">Sign In</button>
                </div>
                <div class="row">
                    <br/>
                    <a href="/password/reset">Forgot Password?</a>
                </div>

                <!--<div class="row">
                    <div class="col-sm-6">
                        <div class="clearfix">
                            <a href="/password/reset" class="">Lost Password?</a>
                        </div>
                    </div>
                    <div class="col-sm-6 text-right">
                        <button type="submit" class="btn btn-primary hidden-xs">Sign In</button>
                        <button type="submit" class="btn btn-primary btn-block btn-lg visible-xs mt-lg">Sign In</button>
                    </div>
                </div>-->

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <!--                    <span class="mt-lg mb-lg line-thru text-center text-uppercase">
                                        <span>or</span>
                                    </span>

                                    <div class="mb-xs text-center">
                                        <a class="btn btn-facebook mb-md ml-xs mr-xs">Connect with <i class="fa fa-facebook"></i></a>
                                        <a class="btn btn-twitter mb-md ml-xs mr-xs">Connect with <i class="fa fa-twitter"></i></a>
                                    </div>-->



            </form>
        </div>
        <br/>
        <hr/>
        <p class="text-center">Don't have an account yet? <a href="/register">Sign Up!</a>
    </div>
</section>
<!-- end: page -->

<%@include file="body-scripts-start.jspf" %>

<!-- Specific Page Vendor -->

<%@include file="body-scripts-end.jspf" %>


<%@include file="body-end.jspf" %>