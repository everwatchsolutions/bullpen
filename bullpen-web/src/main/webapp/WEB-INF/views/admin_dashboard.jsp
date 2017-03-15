<%-- 
    Document   : index
    Created on : July 25, 2016
    Author     : dylankolson
--%>
<div class="panel-body">
    <h2 class="panel-title">Companies</h2>
    <br>
    <div class="table-responsive">
        <table class="table table-striped mb-none">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Website</th>
                    <th>Contact</th>
                    <th style='text-align: center;'>Access This Company</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="stats" value="${counts}" />
                <c:set var="count" value="0" scope="page" />
                <c:forEach var="c" items="${companies}">

                    <tr>
                        <td><strong><a href="/">${c.name} </a></strong></td>
                        <td><a href="//${c.websiteUrl}">  ${c.websiteUrl}</a></td>
                        <td><a href="mailto:${c.publicContactEmail}">  ${c.publicContactEmail}</a></td>
                        <td style='text-align: center;'><a href="/setCompany/${c.name}"><i class="fa fa-sign-in" aria-hidden="true"></i></a></td>
                    </tr>

                </c:forEach>

            </tbody>
        </table>
    </div>
</div> <br>



