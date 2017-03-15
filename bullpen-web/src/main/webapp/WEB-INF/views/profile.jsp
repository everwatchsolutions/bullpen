<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->

<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->
<div class="row">
    <div class="col-md-3 col-lg-2">

        <section class="panel">
            <div class="panel-body">
                <div class="thumb-info mb-md">
                    <img <c:choose><c:when test="${user.profilePictureUrl} != null">src="${user.profilePictureUrl}}"</c:when><c:otherwise>src="//www.gravatar.com/avatar/${user.emailMD5Hash}?s=350}"</c:otherwise></c:choose> class="rounded img-responsive" alt="John Doe">
                    <div class="thumb-info-title">
                        <span class="thumb-info-inner">${user.firstName} ${user.lastName}</span>
                        
                    </div>
                </div>
                    <span class="badge"><label>Company Role: ${role}</label></span>
            </div>
        </section>
    </div>
    <div class="col-md-9 col-lg-7">
        <section class="panel">
            <div class="panel-body">
                <form action="/profile" method="post">
                    
                    <div class="form-group mb-none">
                        <div class="row">
                            <div class="col-sm-6 mb-lg">
                                <label>First Name</label>
                                <input name="firstName" type="text" class="form-control input-lg" value="${user.firstName}" placeholder="ex: Frodo" />
                            </div>
                            <div class="col-sm-6 mb-lg">
                                <label>Last Name</label>
                                <input name="lastName" type="text" class="form-control input-lg" value="${user.lastName}" placeholder="ex: Baggins" />
                            </div>
                        </div>
                    </div>

                    <div class="form-group mb-lg">
                        <label>E-mail Address</label>
                        <div class="input-group input-group-icon">
                            <input name="email" type="email" class="form-control input-lg" placeholder="ex: frodo.baggins@theshire.net" value="${user.email}" />
                            <span class="input-group-addon">
                                <span class="icon icon-lg">
                                    <i class="fa fa-envelope"></i>
                                </span>
                            </span>
                        </div>
                        <!--<input name="email" type="email" class="form-control input-lg" placeholder="ex: frodo.baggins@theshire.net"/>-->
                    </div>

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
                            <button type="submit" class="btn btn-primary hidden-xs">Update</button>
                            <button type="submit" class="btn btn-primary btn-block btn-lg visible-xs mt-lg">Update</button>
                        </div>
                    </div>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </section>
        
        
    </div>
</div>
<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/jquery-autosize/jquery.autosize.js"></script>

<%@include file="body-scripts-end.jspf" %>

<%@include file="body-end.jspf" %>