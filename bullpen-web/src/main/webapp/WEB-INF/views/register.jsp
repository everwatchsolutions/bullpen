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
        <a href="/" class="logo pull-left">
            <img src="/assets/images/ats-logo-no-text.png" height="54" alt="Polaris ATS Logo" />
        </a>

        <div class="panel panel-sign">
            <div class="panel-title-sign mt-xl text-right">
                <h2 class="title text-uppercase text-bold m-none"><i class="fa fa-user mr-xs"></i> Sign Up</h2>
            </div>
            <div class="panel-body">
                <form id="register-form" action="/register" method="post">
                    <c:if test="${invite != null}"><input type="hidden" name="invite" value="${invite.id}"/></c:if>
                    <c:if test="${(param.error != null && SPRING_SECURITY_LAST_EXCEPTION.message != null) || errMessage != null}">
                        <div class="row">
                            <div class="col-lg-12">
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
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${registrationMode == 'closed'}">
                            <div class="row">
                                <div class="col-lg-12">
                                    <h3 style="text-align: center">Sorry - Registrations are closed</h3>
                                    <p>We aren't taking any new registrations at the moment. If you are interested in our product, please sign up for the newsletter on the <a href='//polarisats.com'>Homepage</a> and we'll get in touch as soon as we are allowing new signups!</p>
                                    <p>Thanks for your patience!</p>
                                    <p style="text-align: center">The <img style="vertical-align: bottom"src="/assets/images/ats-logo-no-text-25h.png"/> Team</p>
                                    
                                </div>
                            </div>
                        </c:when>
                        <c:when test="${company == null && registrationMode == 'inviteOnly'}">
                            <div class="row">
                                <div class="col-lg-12">
                                    <h3 style="text-align: center">Sorry - You're not on the list...</h3>
                                    <p>We are only accepting registrations via Invite at the moment. If your company already uses PolarisATS, please have your administrator add you. If you are interested in our product, please sign up for the newsletter on the <a href='//polarisats.com'>Homepage</a> and we'll get in touch as soon as we are allowing new signups!</p>
                                    <p>Thanks for your patience!</p>
                                    <p style="text-align: center">The <img style="vertical-align: bottom"src="/assets/images/ats-logo-no-text-25h.png"/> Team</p>
                                    
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="panel panel-transparent">
                                        <div class="panel-heading">
                                            <span class="panel-title" >Account Info</span>
                                        </div>
                                        <div class="">
                                            <div class="form-group mb-none">
                                                <div class="row">
                                                    <div class="col-sm-6 mb-lg">
                                                        <label>First Name</label>
                                                        <input name="firstName" value="${firstName}" type="text" class="form-control input-lg" placeholder="ex: Frodo" />
                                                    </div>
                                                    <div class="col-sm-6 mb-lg">
                                                        <label>Last Name</label>
                                                        <input name="lastName"  value="${lastName}" type="text" class="form-control input-lg" placeholder="ex: Baggins" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group mb-lg">
                                                <label>E-mail Address</label>
                                                <div class="input-group input-group-icon">
                                                    <input name="email" type="email" class="form-control input-lg" <c:if test="${email_notInvite != null}">value='${email_notInvite}'</c:if> placeholder="ex: frodo.baggins@theshire.net" <c:if test="${email != null}">value='${email}' disabled</c:if>/>
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
                                                                <input name="password" type="password" class="form-control input-lg" value="${password}"/>
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
                                            </div>
                                        </div>
                                    </div>
                                </div>



                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="panel panel-transparent">
                                            <div class="panel-heading">
                                                <span class="panel-title" >Company Info</span>
                                            <c:if test="${company == null && registrationMode == 'inviteOnly'}">
                                                <p style="color: red">Please note, we are only accepting registrations via Invite at the moment. If your company already uses PolarisATS, please have your administrator add you.</p>
                                            </c:if>
                                            </div>
                                            <div class="">
                                                <div class="form-group mb-lg">
                                                    <label>Company Name</label>
                                                    <div class="input-group input-group-icon">
                                                        <input name="companyName" type="text" class="form-control input-lg " <c:if test="${company != null || registrationMode == 'inviteOnly'}">value="${company.name}" disabled</c:if>/>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-12 mb-lg">
                                                        <label>Primary Address</label>
                                                        <div class="input-group input-group-icon">
                                                            <input name="companyAddress" type="text" class="form-control input-lg" placeholder="ex: 123 Main Street" <c:if test="${company != null || registrationMode == 'inviteOnly'}">value="${company.primaryAddress.addressLine}" disabled</c:if> />
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-5 mb-lg">
                                                        <label>City</label>
                                                        <div class="input-group input-group-icon">
                                                            <input name="companyCity" type="text" class="form-control input-lg" <c:if test="${company != null || registrationMode == 'inviteOnly'}">value="${company.primaryAddress.cityName}" disabled</c:if> >
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-4 mb-lg">
                                                        <label>State</label>
                                                        <div class="input-group input-group-icon">
                                                            <select name='companyState' data-plugin-selectTwo class="form-control populate input-lg placeholder" data-plugin-options='{"placeholder": "Select a State", "allowClear": true }' <c:if test="${company != null || registrationMode == 'inviteOnly'}">disabled</c:if>>
                                                            <c:forEach var="state" items="${states}">
                                                                <option value="${state.abbreviation}" <c:if test="${state.abbreviation.equals(company.primaryAddress.state)}">selected</c:if>>${state.name}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="col-sm-3 mb-lg">
                                                    <label>Zip Code</label>
                                                    <div class="input-group input-group-icon">
                                                        <input name="companyZipcode" type="text" class="form-control input-lg " <c:if test="${company != null || registrationMode == 'inviteOnly'}">value="${company.primaryAddress.postalCode}" disabled</c:if>/>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-8">
                                        <div class="checkbox-custom checkbox-default">
                                            <input id="agreeTerms" name="agreeterms" type="checkbox"/>
                                            <label for="AgreeTerms">I agree with <a href="#">terms of use</a></label>
                                        </div>
                                    </div>
                                    <div class="col-sm-4 text-right">
                                        <button type="submit" class="btn btn-primary hidden-xs">Sign Up</button>
                                        <button type="submit" class="btn btn-primary btn-block btn-lg visible-xs mt-lg">Sign Up</button>
                                    </div>
                                </div>
                        </c:otherwise>
                    </c:choose>


                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
            <div class="panel-footer">
                <p  class="text-center">Already have an account yet? <a href="/login">Sign In!</a>
            </div>
        </div>

        <p class="text-center text-muted mt-md mb-md">&copy; Copyright PolarisATS ${currentYear}. All Rights Reserved.</p>
    </div>
</section>
<!-- end: page -->

<%--<%@include file="content-end-to-script-start.jspf" %>--%>
<%@include file="body-scripts-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/select2/select2.js"></script>

<%@include file="body-scripts-end.jspf" %>

<script>
    $('#register-form').submit(function () {
        $("#register-form :disabled").removeAttr('disabled');
    });
</script>


<!-- Reload the invite if there was an error when they submited the form -->
<script>
$( document ).ready(function() {
    var s="${invite_error}";  
   
   if(s.length > 0)
   {
       
       location.assign("register?companyInvite="+s)
       
    }
});
    
</script>
<%@include file="body-end.jspf" %>