<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/static/css/jquery.fileupload.css">
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />
<link rel="stylesheet" href="/assets/vendor/summernote/summernote.css" />
<link rel="stylesheet" href="/assets/vendor/summernote/summernote-bs3.css" />

<%@include file="header-end-to-content-start.jspf" %>
<c:set var="needed" scope="session" value="${'ADMIN'}"/>
<c:set var="needed2" scope="session" value="${'SUPER'}"/>

<!-- start: page -->
<div class="row">
    <div class="col-md-4 col-lg-4">

        <section class="panel">
            <div class="panel-heading">
                <span class="panel-title">${company.name}</span>
            </div>
            <div class="panel-body">
                <img <c:choose><c:when test="${company.companyLogo != null}">src="/view/file/${company.companyLogo.storageId}"</c:when><c:otherwise>src="/assets/images/ats-logo-no-text.png"</c:otherwise></c:choose> class="rounded img-responsive" alt="Company Logo" />
                    </div>
                        <div class="panel-footer"  <c:if test = "${user.getAuthority() != needed &&  user.getAuthority() != needed2}">style="display: none;" </c:if>>
                    <span class="mb-xs mt-xs mr-xs btn btn-success fileinput-button" data-toggle="tooltip" data-placement="left" title="" data-original-title="Click to Upload a new Company Logo">
                        <!--<i class="fa fa-plus"></i>-->
                        <span>Change Logo</span>
                        <!-- The file input field used as target for the file upload widget -->
                        <input id="fileupload" type="file" name="file" data-upload-url="/company/logo.json">
                    </span>
                    <a href="#edit-user-modal" class="modal-with-form btn btn btn-primary">Edit User Roles <i class="fa fa-lock"></i></a>
                </div>
            </section>
            <section class="panel">
                <div class="panel-heading">
                    <span class="panel-title">Company Members
                        <div class="pull-right" <c:if test = "${user.getAuthority() != needed &&  user.getAuthority() != needed2}"> style="display: none;" </c:if>>
                            <a href="#remove-user-modal" class="modal-with-form btn btn-sm btn-success btn-danger"><i class="fa fa-minus"></i></a>
                            <a href="/company/invite" class="btn btn-sm btn-success"><i class="fa fa-plus"></i></a>
                        </div>
                    </span>
                </div>
                <div class="panel-body" style="line-height: 33px;">
                <c:forEach var="user" items="${companyUsers}">

                    <a id="${user.firstName}${user.lastName}" href="mailto:${user.email}">
                        <span class="btn btn-sm btn-primary">${user.firstName} ${user.lastName}</span>
                    </a>             
                </c:forEach>
            </div>
        </section>

                            
        <section class="panel">
            <div class="panel-heading">
                <span class="panel-title">Categories</span>
                <div class="panel-heading-right">
                    <a href="#add-category-modal" class="modal-with-form btn btn-sm btn-success"><i class="fa fa-plus"></i></a>
                </div>

            </div>
            <div class="panel-body">
                <select name="categories" data-plugin-selectTwo class="col-sm-9">
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.name}" >${cat.displayName}</option>
                    </c:forEach>
                </select>
                <a href="#" onclick="showEditPanel('categories', 'category');" class="btn btn-primary col-sm-3"><i class="fa fa-edit"></i> Edit</a>
            </div>
        </section>

                            
        <section class="panel">
            <div class="panel-heading">
                <span class="panel-title">Skills</span>
                <div class="panel-heading-right">
                    <a href="#remove-skill-modal" class="modal-with-form btn btn-sm btn-success btn-danger"><i class="fa fa-minus"></i></a>
                    <a href="#add-skill-modal" class="modal-with-form btn btn-sm btn-success"><i class="fa fa-plus"></i></a>
                </div>

            </div>
            <div class="panel-body">
                <select name="skills" data-plugin-selectTwo class="col-sm-9">
                    <c:choose>
                        <c:when test="${skills != null && skills.size() > 0}">
                            <c:forEach var="cat" items="${skills.keySet()}">
                                <optgroup label="${cat}">
                                    <c:forEach var="item" items="${skills.get(cat)}">
                                        <option value="${item.name}" data-category="${cat}">${item.displayName}</option>
                                    </c:forEach>
                                </optgroup>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="cat" items="${categories}">
                                <optgroup label="${cat.displayName}">
                                </optgroup>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </select>
                <a href="#" onclick="showSkillEditPanel('skills', 'skill');" class="btn btn-primary col-sm-3"><i class="fa fa-edit"></i> Edit</a>
            </div>
        </section>
        <section class="panel">
            <div class="panel-heading">
                <span class="panel-title">Certifications</span>
                <div class="panel-heading-right">
                    <a href="#remove-certification-modal" class="modal-with-form btn btn-sm btn-success btn-danger"><i class="fa fa-minus"></i></a>
                    <a href="#add-certification-modal" class="modal-with-form btn btn-sm btn-success"><i class="fa fa-plus"></i></a>
                </div>

            </div>
            <div class="panel-body">
                <select name="certifications" data-plugin-selectTwo class="col-sm-9">
                    <c:choose>
                        <c:when test="${certifications != null && certifications.size() > 0}">
                            <c:forEach var="cat" items="${certifications.keySet()}">
                                <optgroup label="${cat}">
                                    <c:forEach var="item" items="${certifications.get(cat)}">
                                        <option value="${item.name}" data-category="${cat}">${item.displayName}</option>
                                    </c:forEach>
                                </optgroup>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="cat" items="${categories}">
                                <optgroup label="${cat.displayName}">
                                </optgroup>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </select>
                <a href="#" onclick="showCertificationEditPanel('certifications', 'certification');" class="btn btn-primary col-sm-3"><i class="fa fa-edit"></i> Edit</a>
            </div>
        </section>

    </div>
    <div class="col-md-8 col-lg-8">
        <form action="/company/profile" method="post">
            <section class="panel">
                <div class="panel-body">
                    <div class="col-sm-12 col-md-12 col-lg-12">
                        <div class="row">
                            <div class="col-sm-12">

                                <div class="tabs">
                                    <ul id="address-tab-nav" class="nav nav-tabs">
                                        <li class="active">
                                            <a href="#primaryAddress" data-toggle="tab"><i class="fa fa-star"></i> Primary Address</a>
                                        </li>
                                        <c:forEach var="addy" items="${company.companyLocations}">
                                            <li >
                                                <a href="#${addy.locationName.replaceAll(' ', '-')}" data-toggle="tab">${addy.locationName}</a>
                                            </li>
                                        </c:forEach>
                                        <li <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> style="display: none;" </c:if>>
                                                <a href="#" onclick="addNewAddressTab();"><i class="fa fa-plus"></i></a>
                                            </li>
                                        </ul>
                                        <div id="address-tab-content" class="tab-content">
                                            <div id="primaryAddress" class="tab-pane active">
                                                <div class="form-group mb-lg">
                                                    <div class="row">
                                                        <div class="col-sm-12 mb-lg">
                                                            <label>Company Name</label>
                                                            <div class="input-group input-group-icon">
                                                                <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if>name="companyName" type="text" class="form-control " placeholder="ex: My Business"  value="${company.name}"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12 mb-lg">
                                                            <label>Address</label>
                                                            <div class="input-group input-group-icon">
                                                                <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if>name="companyAddress" type="text" class="form-control " placeholder="ex: 123 Main Street" value="${company.primaryAddress.addressLine}" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-5 mb-lg">
                                                            <label>City</label>
                                                            <div class="input-group input-group-icon">
                                                                <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if>name="companyCity" type="text" class="form-control " value="${company.primaryAddress.cityName}"/>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-4 mb-lg">
                                                            <label>State</label>
                                                            <div class="input-group input-group-icon">
                                                                <select <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> name='companyState' data-plugin-selectTwo class="form-control populate  placeholder" data-plugin-options='{"placeholder": "Select a State", "allowClear": true }'>
                                                                    <option disabled selected hidden>Select...</option>
                                                                <c:forEach var="state" items="${states}">
                                                                    <option value="${state.abbreviation}" <c:if test="${state.abbreviation.equals(company.primaryAddress.state)}">selected</c:if>>${state.name}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3 mb-lg"><div class="input-group input-group-icon">
                                                        <label>Zip Code</label>
                                                        <div class="input-group input-group-icon">
                                                            <input maxlength="5" <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> name="companyZipcode" type="text" class="form-control " value="${company.primaryAddress.postalCode}"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div> </div>

                                        <c:forEach var="addy" items="${company.companyLocations}" varStatus="count">
                                            <div id="${addy.locationName.replaceAll(' ', '-')}" class="tab-pane">
                                                <div class="form-group mb-lg">
                                                    <div class="row pull-right">
                                                        <div class="col-sm-12 ">
                                                          <c:if test = "${user.getAuthority() == needed || user.getAuthority() == needed2}"> <a href="#remove-location-modal" class="modal-with-form btn btn-sm btn-danger"> Delete Location</i></a></c:if>  
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12 mb-lg">
                                                            <label>Location Name</label>
                                                            <div class="input-group input-group-icon">
                                                                <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> name="loc-name-${count.index + 1}" type="text" class="form-control " placeholder="ex: My Business" value="${addy.locationName}"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-12 mb-lg">
                                                            <label>Address</label>
                                                            <div class="input-group input-group-icon">
                                                                <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> name="loc-address-${count.index + 1}" type="text" class="form-control " placeholder="ex: 123 Main Street" value="${addy.addressLine}"/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-sm-5 mb-lg">
                                                            <label>City</label>
                                                            <div class="input-group input-group-icon">
                                                                <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> name="loc-city-${count.index + 1}" type="text" class="form-control " value="${addy.cityName}"/>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-4 mb-lg">
                                                            <label>State</label>
                                                            <div class="input-group input-group-icon">
                                                                <select <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> name='loc-state-${count.index + 1}' data-plugin-selectTwo class="form-control populate  placeholder" data-plugin-options='{"placeholder": "Select a State", "allowClear": true }'>
                                                                    <option disabled selected hidden>Select...</option>
                                                                    <c:forEach var="state" items="${states}">
                                                                        <option value="${state.abbreviation}" <c:if test="${state.abbreviation.equals(addy.state)}">selected</c:if>>${state.name}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-3 mb-lg">
                                                            <label>Zip Code</label>
                                                            <div  class="input-group input-group-icon">
                                                                <input  maxlength="5" name="loc-zipcode-${count.index + 1}" type="text" class="form-control " value="${addy.postalCode}" <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if>/>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>

                                    </div>
                                </div>

                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group mb-lg panel panel-transparent">
                                    <div class="panel-heading">
                                        <h2 class="panel-title">Company Information</h2>
                                    </div>
                                    <div class="panel-body">
                               
                                            <div class="row">
                                                <div class="col-sm-12 mb-lg">
                                                    <label>Company Website</label>
                                                    <div class="input-group input-group-icon">
                                                        <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if>name="companyWebsite" type="text" class="form-control " placeholder="ex: http://mybusiness.com"  value="${company.websiteUrl}"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-12 mb-lg">
                                                    <label>Public Contact Email</label>
                                                    <div class="input-group input-group-icon">
                                                        <input <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> name="companyEmail" type="email" class="form-control " placeholder="ex: jobs@mybusiness.com"  value="${company.publicContactEmail}"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-12 mb-lg">
                                                    <label>Company Description</label>
                                                    <div class="input-group input-group-icon">
                                                        <textarea <c:if test = "${user.getAuthority() != needed && user.getAuthority() != needed2}"> disabled </c:if> data-plugin-summernote data-plugin-options='{ "height": 180, "codemirror": { "theme": "ambiance" } }' class="input-block-level" id="summernote" name="companyDescription" rows="18">
                                                        ${company.companyDescription}
                                                    </textarea>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                   
                                    
                                </div>
                            </div>
                        </div>

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>


                    <div class="row pull-right">
                        <div class="col-sm-12 ">
                            <button type="submit" class="btn btn-primary hidden-xs">Update</button>
                            <button type="submit" class="btn btn-primary btn-block btn-lg visible-xs mt-lg">Update</button>
                        </div>
                    </div>
                </div>
                </div>
            </section>
        </form>


    </div>
</div>

<div id="newAddress-tmpl" class="tab-pane mfp-hide">
    <div class="form-group mb-lg">
        <div class="row">
            <div class="col-sm-12 mb-lg">
                <label>Location Name</label>
                <div class="input-group input-group-icon">
                    <input name="loc-name" type="text" class="form-control " placeholder="ex: My Business"/>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12 mb-lg">
                <label>Address</label>
                <div class="input-group input-group-icon">
                    <input name="loc-address" type="text" class="form-control " placeholder="ex: 123 Main Street" />
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-5 mb-lg">
                <label>City</label>
                <div class="input-group input-group-icon">
                    <input name="loc-city" type="text" class="form-control "/>
                </div>
            </div>
            <div class="col-sm-4 mb-lg">
                <label>State</label>
                <div class="input-group input-group-icon">
                    <select name='loc-state' data-plugin-selectTwo class="form-control populate  placeholder" data-plugin-options='{"placeholder": "Select a State", "allowClear": true }'>
                        <option disabled selected hidden>Select...</option>
                        <c:forEach var="state" items="${states}">
                            <option value="${state.abbreviation}" >${state.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-sm-3 mb-lg">
                <label>Zip Code</label>
                <div class="input-group input-group-icon">
                    <input maxlength="5" name="loc-zipcode" type="text" class="form-control " />
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="templates/company/add-category.jspf" %>
<script id="edit-category-template" type="text/x-handlebars-template">
    <div class="zoom-anim-dialog modal-block modal-block-primary">
    <%@include file="templates/company/edit-category.jspf" %>
    </div>
</script>
<%@include file="templates/company/add-skill.jspf" %>
<script id="edit-skill-template" type="text/x-handlebars-template">
    <div class="zoom-anim-dialog modal-block modal-block-primary">
    <%@include file="templates/company/edit-skill.jspf" %>
    </div>
</script>
<%@include file="templates/company/add-certification.jspf" %>
<script id="edit-certification-template" type="text/x-handlebars-template">
    <div class="zoom-anim-dialog modal-block modal-block-primary">
    <%@include file="templates/company/edit-certification.jspf" %>
    </div>
</script>

<%@include file="templates/company/remove-user.jspf" %>
<%@include file="templates/company/edit-user.jspf" %>
<%@include file="templates/company/remove-certification.jspf" %>
<%@include file="templates/company/remove-skill.jspf" %>
<%@include file="templates/company/remove-location.jspf" %>

<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/handlebars/handlebars-v2.0.0.js"></script>

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

<%@include file="body-scripts-end.jspf" %>

<script src="/assets/javascripts/handlebar-ext.js"></script>
<script src="/assets/javascripts/company.js"></script>

<%@include file="body-end.jspf" %>