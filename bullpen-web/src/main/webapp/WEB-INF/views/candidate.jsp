<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>
<%@taglib uri="/WEB-INF/HistoryDetailsPopup.tld" prefix="aces" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="header-start.jspf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />
<link rel="stylesheet" href="/static/css/jquery.fileupload.css">
<link rel="stylesheet" href="/assets/vendor/summernote/summernote.css" />
<link rel="stylesheet" href="/assets/vendor/summernote/summernote-bs3.css" />

<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->
<div class="row">
    <div class="col-md-4 col-lg-3">

        <section class="panel">
            <div class="panel-body">
                <div class="thumb-info mb-md">
                    <img src="//www.gravatar.com/avatar/${candidate.email[0].addressMD5Hash}?s=350&d=mm" class="rounded img-responsive" alt="John Doe">
                    <div class="thumb-info-title">
                        <!--<span class="thumb-info-inner" data-toggle="tooltip" data-placement="right" title="Source: ${history.getMostRecentHistory("/formattedName").source}( ${history.getMostRecentHistory("/formattedName").sourceId}) - ${history.getMostRecentHistory("/formattedName").dateModified}">${candidate.formattedName}</span>-->
                        <span class="thumb-info-inner" >
                            ${candidate.formattedName} 
                            <a <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/formattedName" />></a>
                            <a href="#editModal" class="modal-with-form fa fa-edit" onclick="createEditPanel('/formattedName', null, 'Enter a new name', '${candidate.formattedName}');"></a>
                        </span>

                        <span class="thumb-info-type">
                            ${candidate.title}
                            <a <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/title" />></a>
                            <a href="#editModal" class="modal-with-form fa fa-edit" onclick="createEditPanel('/title', null, 'Enter a title', '${candidate.title}');"></a>
                        </span>
                    </div>
                    <c:if test="${not empty candidate.preferedLocation}">
                        <br><p>Preferred Location: ${candidate.preferedLocation}</p>
                    </c:if>  
                </div>

            </div>
        </section>

        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#addModal" class="modal-with-form fa fa-plus" onclick="addFormOpened('note-text');"></a>
                </div>

                <h2 class="panel-title">
                    <span class="va-middle">Notes</span>
                </h2>
            </header>
            <div class="panel-body">
                <p>Add Notes to this Candidate to keep track of important information such as email communication, scheduled meetings, portfolio documents, or other important data.  After Notes are entered, they are visible in the Candidate's Timeline.</p>
            </div>
        </section>
        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#addModal" class="modal-with-form fa fa-plus" onclick="addFormOpened('phone');"></a>
                    <a href="#" class="fa fa-caret-down"></a>
                </div>

                <h2 class="panel-title">
                    <span class="va-middle">Contact Info</span>
                </h2>
            </header>
            <div class="panel-body" style="line-height:0px">
                <c:forEach var="phone" items="${candidate.phone}" varStatus="index">
                    <%@include file="templates/candidate/display/phone.jspf" %>
                </c:forEach>
                <c:forEach var="email" items="${candidate.email}" varStatus="index">
                    <%@include file="templates/candidate/display/email.jspf" %>
                </c:forEach>
                <c:forEach var="web" items="${candidate.web}">
                    <c:if test="${not fn:contains(web.getAddress(), 'facebook') && not fn:contains(web.getAddress(), 'twitter') && not fn:contains(web.getAddress(), 'linkedin') && not fn:contains(web.getAddress(), 'github.com')}">
                        <%@include file="templates/candidate/display/web.jspf" %>
                    </c:if>
                </c:forEach>
                <c:forEach var="address" items="${candidate.address}">
                    <%@include file="templates/candidate/display/address.jspf" %>
                </c:forEach>
                <hr class="dotted short">

                <div class="social-icons">
                    <a rel="tooltip" data-placement="bottom" target="_blank" href="${candidate.getFacebook()}" data-original-title="Facebook"><i class="fa fa-facebook"></i><span>Facebook</span></a>
                    <a rel="tooltip" data-placement="bottom" target="_blank" href="${candidate.getTwitter()}" data-original-title="Twitter"><i class="fa fa-twitter"></i><span>Twitter</span></a>
                    <a rel="tooltip" data-placement="bottom" target="_blank" href="${candidate.getLinkedin()}" data-original-title="Linkedin"><i class="fa fa-linkedin"></i><span>Linkedin</span></a>
                    <a rel="tooltip" data-placement="bottom" target="_blank" href="${candidate.getGithub()}" data-original-title="GitHub"><i class="fa fa-github"></i><span>GitHub</span></a>
                </div>
            </div>
        </section>

        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#addModal" class="modal-with-form fa fa-plus" onclick="addFormOpened('personCompetencies');"></a>
                    <a href="#" class="fa fa-caret-down"></a>
                </div>

                <h2 class="panel-title">
                    <span class="label label-primary label-sm text-normal va-middle mr-sm">${candidate.personCompetencies.size()}</span>
                    <span class="va-middle">Skills</span>
                </h2>
            </header>
            <div class="panel-body">
                <div class="content">
                    <c:forEach var="skill" items="${candidate.personCompetencies}">
                        <%@include file="templates/candidate/display/skill.jspf" %>
                    </c:forEach>
                </div>
            </div>

        </section>
        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#addModal" class="modal-with-form fa fa-plus" onclick="addFormOpened('certifications');"></a>
                    <a href="#" class="fa fa-caret-down"></a>
                </div>

                <h2 class="panel-title">
                    <span class="label label-primary label-sm text-normal va-middle mr-sm">${candidate.certifications.size()}</span>
                    <span class="va-middle">Certifications</span>
                </h2>
            </header>
            <div class="panel-body">
                <div class="content">
                    <c:forEach var="cert" items="${candidate.certifications}">
                        <%@include file="templates/candidate/display/certificate.jspf" %>
                    </c:forEach>
                </div>
            </div>

        </section>





        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#addModal" class="modal-with-form fa fa-plus" onclick="addFormOpened('positionHistory');"></a>
                    <a href="#" class="fa fa-caret-down"></a>
                </div>

                <h2 class="panel-title">Employment History</h2>
            </header>
            <div class="panel-body">
                <ul class="simple-post-list">
                    <li>
                        <span class="col-lg-9">Total Years of Experience: ${candidate.yearsOfExperience}</span>
                        <div class="field-controls col-lg-3">
                            <a <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/yearsOfExperience" />></a>
                            <a href="#editModal" class="modal-with-form fa fa-edit" onclick="createEditPanel('/yearsOfExperience', null, 'Enter the total years of experience', '${candidate.yearsOfExperience}');"></a>
                        </div>
                    </li>
                    <c:forEach var="pos" items="${candidate.positionHistory}">
                        <li >
                            <%@include file="templates/candidate/display/position.jspf" %>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </section>
        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#addModal" class="modal-with-form fa fa-plus" onclick="addFormOpened('educationalOrganizations');"></a>
                    <a href="#" class="fa fa-caret-down"></a>
                </div>

                <h2 class="panel-title">Education History</h2>
            </header>
            <div class="panel-body">
                <ul class="simple-post-list">
                    <li>
                        <span class="col-lg-9">Highest Education Level: ${candidate.highestEducationLevel.displayName}</span>
                        <div class="field-controls col-lg-3">
                            <a <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/highestEducationLevel" />></a>
                            <a href="#editModal" class="modal-with-form fa fa-edit" onclick="createEditPanel('/highestEducationLevel', null, 'Enter the highest level of education', '${candidate.highestEducationLevel.name}');"></a>
                        </div>
                    </li>
                    <c:forEach var="school" items="${candidate.educationalOrganizations}">
                        <li >
                            <%@include file="templates/candidate/display/school.jspf" %>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </section>

    </div>
    <div class="col-md-8 col-lg-6">

        <div class="tabs">
            <ul class="nav nav-tabs tabs-primary">
                <li class="active">
                    <a href="#resume" data-toggle="tab">Resume</a>
                </li>
                <li class="">
                    <a href="#timeline" data-toggle="tab">Timeline</a>
                </li>
                <li class="">
                    <a href="#history" data-toggle="tab">Audit History</a>
                </li>

            </ul>
            <div class="tab-content">
                <div id="resume" class="tab-pane active">
                    <h4 class="mb-xlg">Resume
                        <div class="pull-right">
                            <span class="mb-xs mt-xs mr-xs btn btn-primary fileinput-button" data-toggle="tooltip" data-placement="left">
                                <form id="reupload">
                                    <span id="resumeButton">Upload New Resume</span>
                                    <!-- The file input field used as target for the file upload widget -->
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <input id="fileupload" value="${file}" type="file" name="file" data-upload-url="/resume/${candidate.id}/reupload.json" data-autoupload="true" data-form-id="reupload">
                                </form>
                            </span>
                            <a href="/resume/${currentResume.storageId}" class="btn btn-primary">Download</a></div></h4>


                    <div class=" mt-xlg mb-md">
                        <c:choose>
                            <c:when test="${resumeViewUrl != null}">
                                <iframe src="${resumeViewUrl}?theme=dark" width="100%" height="800" allowfullscreen="allowfullscreen" style="border: none;"></iframe>
                                </c:when>
                                <c:otherwise>
                                <h4>Sorry, we are either still processing the resume or there was an error converting the resume for this Candidate.  You can try reloading or <a href="/resume/${currentResume.storageId}" class="alternative-font">Download</a> the original file to view it that way.</h4>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div id="timeline" class="tab-pane">
                    <a href="#addModal" class="modal-with-form fa fa-plus pull-right" onclick="addFormOpened('note-text');"></a>
                    <h4 class="mb-xlg">Timeline</h4>

                    <div class="timeline timeline-simple mt-xlg mb-md">
                        <div class="tm-body">
                            <c:forEach var="year" items="${timeline.keySet()}">
                                <c:forEach var="month" items="${timeline.get(year).keySet()}">
                                    <div class="tm-title">
                                        <h3 class="h5 text-uppercase">${month} ${year}</h3>
                                    </div>
                                    <ol class="tm-items">
                                        <c:forEach var="note" items="${timeline.get(year).get(month)}">
                                            <%@include file="templates/candidate/display/note-timeline-item.jspf" %>
                                        </c:forEach>
                                    </ol>
                                </c:forEach>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div id="history" class="tab-pane">
                    <!-- this pane need to be restricted to admins or something -->
                    <h4>Audit History for Candidate</h4>
                    <c:forEach var="prop" items="${history.getPropertyHistoryMap().keySet()}">
                        <div class="panel">
                            <header class="panel-heading">
                                <div class="panel-actions">
                                    <a href="#" class="fa fa-caret-down"></a>
                                    <a href="#" class="fa fa-times"></a>
                                </div>

                                <h2 class="panel-title">${prop}</h2>
                            </header>
                            <div class="panel-body">
                                <c:forEach var="hist" items="${history.propertyHistoryMap.get(prop)}">
                                    <div class="panel panel-transparent">
                                        <header class="panel-heading">
                                            <h2 class="panel-title">${hist.state} - ${hist.dateModified}</h2>
                                        </header>
                                        <div class="panel-body">
                                            <ul>
                                                <li>Source - ${hist.source} - ${hist.sourceId}</li>
                                                <li>Old Value - ${hist.oldValue}</li>
                                                <li>New Value - ${hist.newValue}</li>
                                            </ul>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>

            </div>
        </div>


        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <!-- No actions for this panel at the moment-->
                    <a href="#" class="fa fa-caret-down"></a>
                </div>

                <h2 class="panel-title">

                    <span class="va-middle">Security Clearance</span>
                </h2>
            </header>
            <div class="panel-body">

                <ul class="simple-post-list">


                    <form id="secure-form" class="form-horizontal mb-lg"
                          novalidate="novalidate" action="/candidate/secure/${candidate.id}"
                          method="get">



                        <li>



                            <div class="row">
                                <span class="col-lg-12"> <c:if
                                        test="${candidate.level==0}">
                                        <label center-block>This candidate does not have a
                                            security clearance.</label>
                                        </c:if> <c:if test="${candidate.level==1}">
                                        <label center-block>Level: Confidential</label>
                                    </c:if> <c:if test="${candidate.level==2}">
                                        <label center-block>Level: Secret</label>
                                    </c:if> <c:if test="${candidate.level==3}">
                                        <label center-block>Level: Top-Secret</label>
                                    </c:if>
                                </span>
                            </div>

                            <div class="row" <c:if test = "${candidate.level == 0}"> style="display: none;" </c:if>>
                                <span class="col-lg-12"> <c:if
                                        test="${candidate.securityVerify==1}">
                                        <label center-block><i class="fa fa-check-circle" aria-hidden="true"> Verified.</i>
                                        </label>
                                    </c:if> <c:if test="${candidate.securityVerify==2}">
                                        <label center-block><i class="fa fa-times-circle" aria-hidden="true"> Not verified.</i>
                                        </label>
                                    </c:if> <c:if test="${candidate.securityVerify==3}">
                                        <label center-block><i class="fa fa-question-circle" aria-hidden="true"> Proceed with caution.</i></label>
                                    </c:if>
                                </span>
                            </div>

                            <div class="row" <c:if test = "${candidate.level == 0}"> style="display: none;" </c:if>>
                                    <span class="col-lg-12">
                                        <label center-block><i class="fa fa-calendar" aria-hidden="true"></i> ${candidate.getSecurityStartString()} - ${candidate.getSecurityEndString()}</label>
                                </span>
                            </div>



                        </li>

                        <c:set var="security" scope="session" value="${'SECURITY'}" />

                        <li
                            <c:if test = "${user.getAuthority() != security}"> style="display: none;" </c:if>>


                                <div class="input-group input-group-icon">
                                    <select id="levels" name='levels'
                                            class="form-control">
                                        <option disabled selected value="${candidate.level}">Select Level</option>
                                    <option value="0">None</option>
                                    <option value="1">Confidential</option>
                                    <option value="2">Secret</option>
                                    <option value="3">Top-Secret</option>
                                </select>
                            </div>
                            <br>
                            <div class="input-group input-group-icon">

                                <select id="verify"
                                        name='verify' class="form-control">
                                    <option disabled selected value="${candidate.securityVerify}">Pick one</option>
                                    <option value="1">Verified</option>
                                    <option value="2">Not Verified</option>
                                    <option value="3">Proceed With Caution</option>
                                </select>

                            </div>

                            <div class="input-group input-group-icon">
                                <div class="col-lg-5 col-sm-5">
                                    <br>
                                    <input
                                        id="securityStart" name="securityStart" type="text" class="form-control text-center pick-datestart"
                                        placeholder="Start" value="${candidate.getSecurityStartString()}" />
                                </div>

                                <div class="col-lg-2 col-sm-2">
                                    <h2 class="text-center">-</h2>

                                </div>

                                <div class="col-lg-5 col-sm-5">
                                    <br>
                                    <input
                                        id="securityEnd" name="securityEnd" type="text" class="form-control text-center pick-date"
                                        placeholder="End" value="${candidate.getSecurityEndString()}" />
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary center-block"
                                    id="securebtn">Save</button>


                        </li> <input type="hidden" name="id" id="id" value="${candidate.id}" />
                        <input type="hidden" name="${_csrf.parameterName}"
                               value="${_csrf.token}" />
                    </form>
                </ul>
        </section>


        <div class="panel">
            <div class="panel-heading">
                <h2 class="panel-title">Start Date</h2>
            </div>
            <div class="panel-body">
                <div class="col-sm-12 mb-lg">
                    If this candidate has been hired you may enter a start date here. This information will be sent in any automatic notifications through workflows.<hr> 
                    <form id="start-form" class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/startDate" method="POST"> 

                        <div class="input-group input-group-icon">
                            <input class="form-control pick-date"  id="start" name="start" style="position: initial; height: 37px; width: 50%"  value="${candidate.getStartDateString()}" >
                            <button type="submit" class="btn btn-primary pull-right">Update</button>
                        </div>

                        <input type="hidden" name="id2" id="id2" value="${candidate.id}"/>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>

                </div>
            </div>
        </div>

        <c:if test="${positions != null}">
            <div>
                <section class="panel">
                    <header class="panel-heading">
                        Applied to Positions
                    </header>
                    <div class="panel-body">
                        <c:forEach var="opening" items="${positions}">
                            <a href="/openings/${opening.shortName}">
                                <section class="panel panel-featured-primary">
                                    <div class="panel-body">
                                        <div class="widget-summary widget-summary-xs">
                                            <div class="widget-summary-col widget-summary-col-icon">
                                                <div class="summary-icon bg-primary">
                                                    <i class="fa fa-suitcase"></i>
                                                </div>
                                            </div>
                                            <div class="widget-summary-col">
                                                <div class="summary">
                                                    <h4>${opening.positionTitle}</h4>
                                                    <div class="info">
                                                        <h5>${opening.departmentName}</h5>
                                                    </div>
                                                </div>
                                                <div class="summary-footer">
                                                    <a class="text-muted text-uppercase">Last Updated: ${opening.lastUpdated}</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </a>
                        </c:forEach>
                    </div>
                </section>

            </div>
        </c:if>


    </div>
    <div class="col-md-12 col-lg-3">
        <c:if test="${possibleOpenings != null}">
            <div class="row">

                <section class="panel">
                    <header class="panel-heading">
                        Possible Opening Matches
                    </header>
                    <div class="panel-body">
                        We have ${possibleOpenings.size()} <a href="/openings">Openings</a> that may be a fit for this Candidate!
                        <c:forEach var="opening" items="${possibleOpenings}">
                            <a href="/openings/${opening.shortName}">
                                <section class="panel panel-featured-primary">
                                    <div class="panel-body">
                                        <div class="widget-summary widget-summary-xs">
                                            <div class="widget-summary-col widget-summary-col-icon">
                                                <div class="summary-icon bg-primary">
                                                    <i class="fa fa-suitcase"></i>
                                                </div>
                                            </div>
                                            <div class="widget-summary-col">
                                                <div class="summary">
                                                    <h4>${opening.positionTitle}</h4>
                                                    <div class="info">
                                                        <h5>${opening.departmentName}</h5>
                                                    </div>
                                                </div>
                                                <div class="summary-footer">
                                                    <a class="text-muted text-uppercase">Last Updated: ${opening.lastUpdated}</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                            </a>
                        </c:forEach>
                    </div>
                </section>

            </div>
        </c:if>
        <div class="row">
            <section class="panel">
                <header class="panel-heading">
                    <div class="panel-heading-right">
                        <!--<a href="#updateWorkflowStateModal" class="btn btn-primary modal-with-form">Update State</a>-->
                    </div>
                    <h2 class="panel-title">Workflow</h2>
                </header>
                <div class="panel-body">

                    <c:if test="${candidate.workflow == null}">
                        <a href="#assignWorkflowModal" class="btn btn-primary modal-with-form">Assign Workflow</a>
                    </c:if>

                    <c:if test="${candidate.workflow != null}">
                        <a href="#removeFromWorkflowModal" class="btn btn-primary modal-with-form" style="margin-top: 10px;">Remove From Workflow</a>
                    </c:if>

                    <h5>Current Workflow: ${candidate.workflow.name}</h5>
                    <h5>Current State: ${candidate.currentState.displayName}</h5>
                    <hr class="dotted short"></hr>
                    <h5>Next Steps: </h5>

                    <c:forEach var="state" items="${candidate.workflow.allowedStateMap.get(candidate.currentState.name)}" varStatus="index">
                        <a href="#${state.name}Modal" class="btn btn-default modal-with-form">${state.displayName}</a>
                        <div id="${state.name}Modal" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                            <section class="panel">
                                <form action="/candidate/${candidate.id}/updateState?${_csrf.parameterName}=${_csrf.token}" method="POST" style="display: inline;" enctype="multipart/form-data">
                                    <header class="panel-heading">
                                        <h2 class="panel-title">Transition to State ${state.displayName}?</h2>
                                    </header>

                                    <div class="panel-body">
                                        <c:choose>
                                            <c:when test="${candidate.workflow.getAttachmentsForTransition(candidate.currentState, state) == null}">
                                                Are you sure you want to move ${candidate.givenName} to the new State ${state.displayName}? 
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${!candidate.workflow.getRequiredAttachmentsForTransition(candidate.currentState, state).isEmpty()}">
                                                        <h4>The following Actions are <span class="alternative-font">Required</span> during this State Transition:</h4>
                                                        <c:forEach var="a" items="${candidate.workflow.getRequiredAttachmentsForTransition(candidate.currentState, state)}">
                                                            <%@include file="templates/candidate/edit/state-transition-attachment.jspf" %>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <!--<h5>There are no required Actions for this Transition</h5>-->
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:choose>
                                                    <c:when test="${!candidate.workflow.getOptionalAttachmentsForTransition(candidate.currentState, state).isEmpty()}">
                                                        <h4>The following Actions are <span class="alternative-font">Optional</span> during this State Transition:</h4>
                                                        <c:forEach var="a" items="${candidate.workflow.getOptionalAttachmentsForTransition(candidate.currentState, state)}">
                                                            <%@include file="templates/candidate/edit/state-transition-attachment.jspf" %>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <!--<h5>There are no optional Actions for this Transition</h5>-->
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                        <input type="hidden" name="newState" value="${state.name}"/>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </div>
                                    <footer class="panel-footer">
                                        <div class="row">
                                            <div class="col-md-12 text-right">
                                                <button class="btn btn-danger modal-dismiss">Cancel</button>
                                                <button class="btn btn-success modal-confirm">Do It!</button>
                                            </div>
                                        </div>
                                    </footer>
                                </form>
                            </section>
                        </div>
                        <c:if test="${!index.last}"> - or - </c:if>
                    </c:forEach>
                </div>
            </section>
        </div>
        <div class="row">
            <div class="panel">
                <div class="panel-heading">
                    <h2 class="panel-title">Notifications</h2>
                </div>
                <div class="panel-body">
                    <div class="col-sm-12 mb-lg">
                        <label>Users to Notify when there's activity on this Candidate</label>
                        <select id="usersToNotify" data-plugin-selectTwo name='usersToNotify' class="form-control populate" multiple >
                            <c:forEach var="user" items="${candidate.interestedUsers}">
                                <option value="${user.id}" selected>${user.firstName} ${user.lastName}</option>
                            </c:forEach>

                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">

            <c:set var="needed" scope="session" value="${'HR'}"/>
            <c:set var="admin" scope="session" value="${'ADMIN'}"/>
            <div class="panel" <c:if test = "${user.getAuthority() != needed && user.getAuthority() != admin}"> style="display: none;" </c:if>>
                    <div class="panel-heading">
                        <h2 class="panel-title">EEO</h2>
                        <div class="panel-actions">
                            <a href="#addModal" class="modal-with-form fa fa-plus" onclick="addFormOpened('eeo');"></a>
                            <a href="#" class="fa fa-caret-down"></a>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="col-sm-12 mb-lg">
                            <p> 
                            <c:forEach var="eeo" items="${candidate.EEO}" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index == 0}">
                                        <b>Gender:</b> <c:out value="${eeo}"/> <br>
                                    </c:when>
                                    <c:when test="${loop.index == 1}">
                                        <b>Race:</b> <c:out value="${eeo}"/> <br>
                                    </c:when>
                                    <c:when test="${loop.index == 2}">
                                        <b>Veteran Status:</b> <c:out value="${eeo}"/> <br>
                                    </c:when>
                                    <c:when test="${loop.index == 3}">
                                        <b>Disability Status:</b> <c:out value="${eeo}"/> <br>
                                    </c:when>

                                </c:choose>

                            </c:forEach></p>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="panel">
                <div class="panel-heading">
                    <h2 class="panel-title">Active Candidate</h2>
                </div>
                <div class="panel-body">
                    <div class="col-sm-12 mb-lg">

                        Changing this will archive/dearchive this candidate from the system.<hr>
                        <form id="archive-form" class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/archive/${candidate.id}" method="get"> 
                            <button type="submit" class="btn btn-primary" style="display: block;margin: 0 auto;"><c:if test="${not candidate.archive}">Archive</c:if><c:if test="${candidate.archive}">Unarchive</c:if></button>
                            <input type="hidden" name="id" id="id" value="${candidate.id}"/>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>

                    </div>
                </div>
            </div>
        </div>


    </div>
</div>

<!-- Modal Form -->
<div id="assignWorkflowModal" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
    <section class="panel">
        <form class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/${candidate.id}/assignWorkflow" method="POST">
            <header class="panel-heading">
                <h2 class="panel-title">Assign ${candidate.formattedName} to a New Workflow?</h2>
            </header>

            <div class="panel-body">
                <label class="name-label">Select a New Workflow</label>
                <div class="input-group input-group-icon">
                    <select data-plugin-selectTwo name='workflowId' class="form-control populate">
                        <c:forEach var="w" items="${workflows}">
                            <option value="${w.id}">${w.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </div>
            <footer class="panel-footer">
                <div class="row">
                    <div class="col-md-12 text-right">
                        <button class="btn btn-primary modal-confirm">Submit</button>
                        <button class="btn btn-default modal-dismiss">Cancel</button>
                    </div>
                </div>
            </footer>
        </form>
    </section>
</div>


<div id="removeFromWorkflowModal" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide"> 
    <section class="panel">
        <form class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/${candidate.id}/removeFromWorkflow" method="POST">
            <c:if test="${candidate.workflow != null}">


                <header class="panel-heading">
                    <h2 class="panel-title">Are you sure you want to remove ${candidate.formattedName} from ${candidate.workflow.name}?</h2>
                </header>

                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12 text-right">
                            <button class="btn btn-primary modal-confirm">Yes</button>
                            <button class="btn btn-default modal-dismiss">Cancel</button>
                        </div>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </div>

            </c:if>
            </div> 

            <div id="updateWorkflowStateModal" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                <section class="panel">
                    <form class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/${candidate.id}/updateState" method="POST">
                        <header class="panel-heading">
                            <h2 class="panel-title">Update ${candidate.formattedName} Workflow State?</h2>
                        </header>

                        </div> 

                        <div id="updateWorkflowStateModal" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                            <section class="panel">
                                <form class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/${candidate.id}/updateState" method="POST">
                                    <header class="panel-heading">
                                        <h2 class="panel-title">Update ${candidate.formattedName} Workflow State?</h2>
                                    </header>

                                    <div class="panel-body">
                                        <label class="name-label">Select a New State</label>
                                        <div class="input-group input-group-icon">
                                            <select data-plugin-selectTwo name='newState' class="form-control populate">
                                                <c:forEach var="s" items="${candidate.workflow.allowedStateMap.get(candidate.currentState.name)}">
                                                    <option value="${s.name}">${s.displayName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </div>
                                    <footer class="panel-footer">
                                        <div class="row">
                                            <div class="col-md-12 text-right">
                                                <button class="btn btn-primary modal-confirm">Submit</button>
                                                <button class="btn btn-default modal-dismiss">Cancel</button>
                                            </div>
                                        </div>
                                    </footer>
                                </form>
                            </section>
                        </div>
                        <div id="editModal" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                            <section class="panel">
                                <form id="editForm" class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/${candidate.id}/edit" method="POST">
                                    <header class="panel-heading">
                                        <h2 class="panel-title">Edit Candidate Info</h2>
                                    </header>

                                    <div class="panel-body">
                                    </div>
                                    <footer class="panel-footer">
                                        <div class="row">
                                            <div class="col-md-12 text-right">
                                                <button class="btn btn-primary modal-confirm">Submit</button>
                                                <button class="btn btn-default modal-dismiss">Cancel</button>
                                            </div>
                                        </div>
                                    </footer>
                                </form>
                            </section>
                        </div>

                        <div id="addModal" class="zoom-anim-dialog modal-block modal-block-primary mfp-hide">
                            <section class="panel">
                                <form id="addForm" class="form-horizontal mb-lg" novalidate="novalidate" action="/candidate/${candidate.id}/add?${_csrf.parameterName}=${_csrf.token}" method="POST" enctype="multipart/form-data">
                                    <header class="panel-heading">
                                        <h2 class="panel-title">Add Candidate Info</h2>
                                    </header>

                                    <div class="panel-body">
                                        <div class="col-sm-12 form-group mb-lg">
                                            <label class="name-label">What would you like to add?</label>
                                            <div class="input-group input-group-icon">
                                                <select id="add-form-type-select" data-plugin-selectTwo style="z-index: 100000;" name="itemType" class="form-control input-sm" onchange="loadAddForm();">

                                                    <option value="phone">Phone</option>
                                                    <option value="eeo"  <c:if test = "${user.getAuthority() != needed && user.getAuthority() && admin}"> disabled </c:if>>EEO</option>
                                                        <option value="email">Email</option>
                                                        <option value="web">Website</option>
                                                        <option value="facebook">Facebook</option>
                                                        <option value="twitter">Twitter</option>
                                                        <option value="linkedin">LinkedIn</option>
                                                        <option value="github">GitHub</option>
                                                        <option value="address">Address</option>
                                                        <option value="personCompetencies">Skills</option>
                                                        <option value="certifications">Certifications</option>
                                                        <option value="positionHistory">Employer</option>
                                                        <option value="educationalOrganizations">School</option>
                                                    <c:forEach var="n" items="${availableNotes}">
                                                        <option value="${n.name}">${n.displayName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                        </div>
                                        <div class="form-content">

                                        </div>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </div>
                                    <footer class="panel-footer">
                                        <div class="row">
                                            <div class="col-md-12 text-right">
                                                <button class="btn btn-primary modal-confirm">Submit</button>
                                                <button class="btn btn-default modal-dismiss">Cancel</button>
                                            </div>
                                        </div>
                                    </footer>
                                </form>
                            </section>
                        </div>

                        <%@include file="templates/candidate/edit/string-edit.jspf" %>
                        <%@include file="templates/candidate/edit/phone-edit.jspf" %>
                        <%@include file="templates/candidate/edit/email-edit.jspf" %>
                        <%@include file="templates/candidate/edit/web-edit.jspf" %>
                        <%@include file="templates/candidate/edit/social-media/facebook-edit.jspf" %>
                        <%@include file="templates/candidate/edit/social-media/twitter-edit.jspf" %>
                        <%@include file="templates/candidate/edit/social-media/linkedin-edit.jspf" %>
                        <%@include file="templates/candidate/edit/social-media/github-edit.jspf" %>
                        <%@include file="templates/candidate/edit/address-edit.jspf" %>
                        <%@include file="templates/candidate/edit/position-edit.jspf" %>
                        <%@include file="templates/candidate/edit/school-edit.jspf" %>
                        <%@include file="templates/candidate/edit/skills-edit.jspf" %>
                        <%@include file="templates/candidate/edit/certifications-edit.jspf" %>
                        <%@include file="templates/candidate/edit/educationLevel-edit.jspf" %>
                        <%@include file="templates/candidate/edit/note-text-edit.jspf" %>
                        <%@include file="templates/candidate/edit/note-meeting-edit.jspf" %>
                        <%@include file="templates/candidate/edit/note-file-edit.jspf" %>
                        <%@include file="templates/candidate/edit/eeo-edit.jspf" %>
                        <!-- end: page -->

                        <%@include file="content-end-to-script-start.jspf" %>

                        <!-- Specific Page Vendor -->
                        <script src="/assets/vendor/summernote/summernote.js"></script>
                        <script src="/assets/vendor/magnific-popup/magnific-popup.js"></script>
                        <script src="/assets/vendor/jquery-autosize/jquery.autosize.js"></script>
                        <script src="/assets/vendor/select2/select2.js"></script>
                        <script src="/assets/vendor/ios7-switch/ios7-switch.js"></script>

                        <script type="text/javascript">
                                                    $(document).ready(function () {
                                                        $('#fileupload').on('change', function () {

                                                            $.ajax({
                                                                url: '/resume/${candidate.id}/reupload.json',
                                                                type: "POST",
                                                                data: new FormData(document.getElementById("reupload")),
                                                                enctype: 'multipart/form-data',
                                                                processData: false,
                                                                contentType: false
                                                            }).done(function (data) {
                                                                location.reload();



                                                            }).fail(function (jqXHR, textStatus) {
                                                                alert("Failed");

                                                            });
                                                        });

                                                    });
                        </script>


                        <%@include file="body-scripts-end.jspf" %>

                        <script src="/assets/javascripts/candidate.js"></script>
                        <%@include file="body-end.jspf" %>
