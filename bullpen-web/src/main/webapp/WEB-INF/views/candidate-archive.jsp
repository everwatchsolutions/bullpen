<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/vendor/select2/select2.css" />

<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->
<div class="row">
    <div class="col-lg-12"> 
        <%@include file="search-panel.jspf" %>
    </div>
</div>
<div class="row">
    <ul id="result-list" style="list-style: none; padding-left: 0; margin-left: 0;">
    </ul>
</div>
<script id="candidate-card-template" type="text/x-handlebars-template">
    <%@include file="templates/candidate/display/candidate-card.jspf" %>
</script>
<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->
<script src="/assets/vendor/handlebars/handlebars-v2.0.0.js"></script>
<script src="/assets/vendor/momentjs/moment.min.js"></script>
<script src="/assets/vendor/select2/select2.js"></script>

<%@include file="body-scripts-end.jspf" %>

<script src="/assets/javascripts/candidate-archive.js"></script>

<%@include file="body-end.jspf" %>