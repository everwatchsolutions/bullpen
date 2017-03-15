<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>
<%@taglib uri="/WEB-INF/HistoryDetailsPopup.tld" prefix="aces" %>
<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->

<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->
<div class="row">
    <div class="col-md-4 col-lg-3">

        <section class="panel">
            <div class="panel-body">
                <div class="thumb-info mb-md">
                    <img src="//www.gravatar.com/avatar/${candidate.email[0].addressMD5Hash}?s=350" class="rounded img-responsive" alt="John Doe">
                    <div class="thumb-info-title">
                        <!--<span class="thumb-info-inner" data-toggle="tooltip" data-placement="right" title="Source: ${history.getMostRecentHistory("/formattedName").source}( ${history.getMostRecentHistory("/formattedName").sourceId}) - ${history.getMostRecentHistory("/formattedName").dateModified}">${candidate.formattedName}</span>-->
                        <span class="thumb-info-inner" <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/formattedName" />>${candidate.formattedName}</span>
                        <!--<span class="thumb-info-type">Employee</span>-->
                    </div>
                </div>

                <hr class="dotted short">

                <c:forEach var="phone" items="${candidate.phone}" varStatus="index">
                    <p ><i class="fa fa-phone mr-xs" <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/phone/" uniqueId="${phone.uniqueId}" />></i> ${phone.number}</p>
                </c:forEach>
                <c:forEach var="email" items="${candidate.email}" varStatus="index">
                    <p><i class="fa fa-envelope mr-xs" <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/email/" uniqueId="${email.uniqueId}" />></i> <a href="mailto:${email.address}">${email.address}</a></p>
                    </c:forEach>
                    <c:forEach var="web" items="${candidate.web}">
                    <p><i class="fa fa-globe mr-xs" <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/web/" uniqueId="${web.uniqueId}" />></i> <a href="${web.address}">${web.address}</a></p>
                    </c:forEach>
                    <c:forEach var="address" items="${candidate.address}">
                    <p><i class="fa fa-home mr-xs" <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/address/" uniqueId="${address.uniqueId}" />></i> ${address.addressLine}, ${address.cityName} ${address.state} ${address.postalCode} ${address.countryCode}</p>
                </c:forEach>

                <hr class="dotted short">

                <div class="social-icons">
                    <a rel="tooltip" data-placement="bottom" target="_blank" href="http://www.facebook.com" data-original-title="Facebook"><i class="fa fa-facebook"></i><span>Facebook</span></a>
                    <a rel="tooltip" data-placement="bottom" href="http://www.twitter.com" data-original-title="Twitter"><i class="fa fa-twitter"></i><span>Twitter</span></a>
                    <a rel="tooltip" data-placement="bottom" href="http://www.linkedin.com" data-original-title="Linkedin"><i class="fa fa-linkedin"></i><span>Linkedin</span></a>
                    <a rel="tooltip" data-placement="bottom" href="http://www.github.com" data-original-title="GitHub"><i class="fa fa-github"></i><span>GitHub</span></a>
                </div>

            </div>
        </section>


        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#" class="fa fa-caret-down"></a>
                    <a href="#" class="fa fa-times"></a>
                </div>

                <h2 class="panel-title">
                    <span class="label label-primary label-sm text-normal va-middle mr-sm">${candidate.personCompetencies.size()}</span>
                    <span class="va-middle">Skills</span>
                </h2>
            </header>
            <div class="panel-body">
                <div class="content">
                    <c:forEach var="skill" items="${candidate.personCompetencies}">
                        <span class="label label-primary" <aces:HistoryDetailsPopover history="${history}" fieldPrefix="/personCompetencies/" uniqueId="${skill.uniqueId}" />>${skill.competencyName}</span>
                    </c:forEach>
                </div>
            </div>

        </section>

        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#" class="fa fa-caret-down"></a>
                    <a href="#" class="fa fa-times"></a>
                </div>

                <h2 class="panel-title">Employment History</h2>
            </header>
            <div class="panel-body">
                <ul class="simple-post-list">
                    <c:forEach var="pos" items="${candidate.positionHistory}">
                        <li data-toggle="popover" data-container="body" data-placement="right" title="Where this data came from" data-content="Source: ${history.getMostRecentHistory("/positionHistory/".concat(pos.uniqueId.replaceAll("\\.","_").replaceAll("/","_").replaceAll(" ","-"))).source}( ${history.getMostRecentHistory("/positionHistory/".concat(pos.uniqueId.replaceAll("\\.","_").replaceAll("/","_").replaceAll(" ","-"))).sourceId}) - ${history.getMostRecentHistory("/positionHistory/".concat(pos.uniqueId.replaceAll("\\.","_").replaceAll("/","_").replaceAll(" ","-"))).dateModified}">
                            <div class="post-image">
                                <div class="img-thumbnail">
                                    <a href="#">
                                        <img src="/assets/images/post-thumb-1.jpg" alt="">
                                    </a>
                                </div>
                            </div>
                            <div class="post-info">
                                <a href="#">${pos.employer}</a>
                                <div class="post-meta">
                                    ${pos.startDate} - ${pos.endDate}
                                </div>
                                ${pos.positionTitle}
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </section>
        <section class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#" class="fa fa-caret-down"></a>
                    <a href="#" class="fa fa-times"></a>
                </div>

                <h2 class="panel-title">Education History</h2>
            </header>
            <div class="panel-body">
                <ul class="simple-post-list">
                    <c:forEach var="school" items="${candidate.educationalOrganizations}">
                        <li data-toggle="popover" data-container="body" data-placement="right" title="Where this data came from" data-content="Source: ${history.getMostRecentHistory("/educationalOrganizations/".concat(school.uniqueId.replaceAll("\\.","_").replaceAll("/","_").replaceAll(" ","-"))).source}( ${history.getMostRecentHistory("/educationalOrganizations/".concat(school.uniqueId.replaceAll("\\.","_").replaceAll("/","_").replaceAll(" ","-"))).sourceId}) - ${history.getMostRecentHistory("/educationalOrganizations/".concat(school.uniqueId.replaceAll("\\.","_").replaceAll("/","_").replaceAll(" ","-"))).dateModified}">
                            <div class="post-image">
                                <div class="img-thumbnail">
                                    <a href="#">
                                        <img src="/assets/images/post-thumb-1.jpg" alt="">
                                    </a>
                                </div>
                            </div>
                            <div class="post-info">
                                <a href="#">${school.school}</a>
                                <c:if test="${school.attendanceEndDate != null}">
                                    <div class="post-meta">
                                        ${school.attendanceStartDate} - ${school.attendanceEndDate}
                                    </div>
                                </c:if>
                                <c:forEach var="degree" items="${school.degreeType}">
                                    ${degree.name}<br/>
                                </c:forEach>

                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </section>

    </div>
    <div class="col-md-8 col-lg-6">
        <div id="resume" class="panel">
            <header class="panel-heading">
                <div class="panel-actions">
                    <a href="#" class="fa fa-caret-down"></a>
                    <a href="#" class="fa fa-times"></a>
                </div>

                <h2 class="panel-title">${prop}</h2>
            </header>
            <div class="panel-body">


                <h4 class="mb-xlg">Resume<div class="pull-right"><a href="/resume/${currentResume.storageId}" class="btn btn-primary">Download</a></div></h4>
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
        </div>
    </div>
</div>
<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/jquery-autosize/jquery.autosize.js"></script>
<script src="/assets/javascripts/candidate.js"></script>

<%@include file="body-scripts-end.jspf" %>

<%@include file="body-end.jspf" %>