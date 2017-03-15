<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />
<link rel="stylesheet" href="/assets/vendor/jquery-datatables-bs3/assets/css/datatables.css" />

<%@include file="header-end-to-content-start.jspf" %>
 <c:set var="needed" scope="session" value="${'ADMIN'}"/>

<!-- start: page -->
<section class="panel col-lg-8" <c:if test = "${user.getAuthority() != needed}"> style="display: none;" </c:if>>
    <header class="panel-heading">
        <h2 class="panel-title">Invite a user to your company</h2>
    </header>
    <div class="panel-body">
        <form action="/company/invite" method="post">
            <div class="form-group mb-none">
                <div class="row">
                    <div class="col-sm-12 mb-lg">
                        <h3>Enter the email address of the person you would like to Invite</h3>
                    </div>
                </div>
            </div>
            <div class="form-group mb-lg">
                <label>E-mail Address</label>
                <div class="input-group input-group-icon">
                    <input name="emailToInvite" type="email" class="form-control input-lg" placeholder="ex: frodo.baggins@theshire.net"/>
                    <span class="input-group-addon">
                        <span class="icon icon-lg">
                            <i class="fa fa-envelope"></i>
                        </span>
                    </span>
                </div>
            </div>
            <div class="form-group mb-lg">
                <label>User Permission </label>
                <div class="input-group input-group-icon">
                    <select name='premission' id="premission" data-plugin-selectTwo class="form-control populate input-lg placeholder" >
                        <option value="ROLE_USER">Standard User</option>
                        <option value="ROLE_ADMIN" title="Administrator's have full access to the company">Administrator</option>
                        <option value="ROLE_HR" title='HR Staff are the only ones allowed to view EEO data'>HR Staff</option>
                        <option value="ROLE_SECURITY">Security Staff</option>
                    </select>
                </div>   
            </div>


            <div class="row pull-right">
                <div class="col-sm-4 text-right">
                    <button type="submit" class="btn btn-primary hidden-xs">Invite</button>
                    <button type="submit" class="btn btn-primary btn-block btn-lg visible-xs mt-lg">Invite</button>
                </div>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
</section>
<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/select2/select2.js"></script>
<script src="/assets/vendor/jquery-datatables/media/js/jquery.dataTables.js"></script>
<script src="/assets/vendor/jquery-datatables-bs3/assets/js/datatables.js"></script>
<script src="/assets/vendor/jquery-dateFormat/dist/jquery-dateFormat.min.js"></script>

<%@include file="body-scripts-end.jspf" %>

<script src="/assets/javascripts/openings.js"></script>
<script src="/assets/vendor/jquery/jquery.js"></script>
<script src="/assets/vendor/jquery-browser-mobile/jquery.browser.mobile.js"></script>

<%@include file="body-end.jspf" %>
