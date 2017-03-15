<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start-boxed.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/assets/stylesheets/fullscreen.css" />

<%@include file="header-end.jspf" %>
<body style="background: white;">

    <div class="fullscreen background" style="background-image:url('/assets/images/homepage-background.png');" data-img-width="1280" data-img-height="720">
        <div class="row" style="height: 50px;">
            <div class="col-sm-12 text-right" style="margin-top: 15px; margin-right: 15px; margin-bottom: -15px; margin-left: -15px;color: lightgray">
               <a href='/register' class="btn btn-default" style="margin-left: 15px;">Register</a> <a href='/login' class="btn btn-default" style="margin-left: 15px;">Login</a>
            </div>
        </div>
        <div class="content-a">
            <div class="content-b" style='color: white;'>
                <div class="container" style=''>
                    <div class="row">
                        <div class="col-sm-12">
                            <img style='width: 100%; max-width: 700px;margin-top:-65px' src="/assets/images/homepage-design-logo.png"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8 col-sm-offset-2 " style='background-color:rgba(0, 0, 0, 0.5); border-radius: 15px; padding: 15px;'>
                            <h3>Thanks for stopping by!</h3>
                            <p style="margin-bottom: 30px;">We are busy working on a next generation hiring platform to help small businesses.  If you would be interested in hearing more when we are ready to share the details, please sign up for our newsletter below!</p>
                            <form action="//acesinc.us9.list-manage.com/subscribe/post?u=1bf1555def48272df417bb1dc&amp;id=08dd57e3ce" method="post" id="mc-embedded-subscribe-form" name="mc-embedded-subscribe-form" class="validate" target="_blank" novalidate>
                                <div class="form-group mb-none">
                                    <div class="row">
                                        <div class="col-sm-6 mb-lg">
                                            <input name="FNAME" type="text" class="form-control" placeholder="First Name" />
                                        </div>
                                        <div class="col-sm-6 mb-lg">
                                            <input name="LNAME" type="text" class="form-control" placeholder="Last Name" />
                                        </div>
                                    </div>
                                </div>
                                <div class="input-group mb-md">
                                    <input type="email" name="EMAIL" class="form-control" placeholder="Email Address"/>
                                    <div class="input-group-btn">
                                        <button tabindex="-1" class="btn btn-success" style="width: 100px;" type="submit">Notify Me!</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <%@include file="body-scripts-start.jspf" %>

    <!-- Specific Page Vendor -->
    <script src="/assets/javascripts/fullscreen.js"></script>                

    <%@include file="body-scripts-end.jspf" %>

    <%@include file="body-end.jspf" %>