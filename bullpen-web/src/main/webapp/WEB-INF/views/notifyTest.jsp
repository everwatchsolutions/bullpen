<%-- 
    Document   : index
    Created on : Aug 12, 2014, 2:19:19 PM
    Author     : andrewserff
--%>

<%@include file="header-start.jspf" %>

<!-- Specific Page Vendor CSS -->
<link rel="stylesheet" href="/static/css/jquery.fileupload.css">
<!--<link rel="stylesheet" href="/static/css/jquery.fileupload-ui.css">-->

<%@include file="header-end-to-content-start.jspf" %>


<!-- start: page -->
<button type="button" class="button success" onclick="startTest();">Start</button>
<div id="message-panel" class="panel">

    <span class="mb-xs mt-xs mr-xs btn btn-success fileinput-button">
        <i class="fa fa-plus"></i>
        <span>Select files...</span>
        <!-- The file input field used as target for the file upload widget -->
        <input id="fileupload" type="file" name="file" multiple>
    </span>
</div>
<!-- end: page -->

<%@include file="content-end-to-script-start.jspf" %>

<!-- Specific Page Vendor -->

<!--<script src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
<script src="/assets/vendor/stompjs/stomp.min.js"></script>
<script>
    var startTest = function() {
        $.ajax("/notify/test/start")
                .done(function() {
                    console.log("Started Test");
                })
                .fail(function() {
                    console.error("Error starting Test");
                });
    };
    var handleNotify = function(message) {
        // called when the client receives a STOMP message from the server
        if (message.body) {
            console.log("got message with body " + message.body);
        } else {
            console.log("got empty message");
        }
    };

    (function($) {

        'use strict';

        var init = function() {

            // use SockJS implementation instead of the browser's native implementation
            var ws = new SockJS("/notification");
            var client = Stomp.over(ws);
            client.connect("guest", "guest", function(x) {
                console.log('[*] Connected');
                var subId = client.subscribe("/topic/test", handleNotify);
//                client.subscribe("/queue/test", function(d) {
//                    console.log('[.] got', d.body);
//                });
            });
            

        };

        $(function() {
            init();
        });

    }).apply(this, [jQuery]);

</script>-->

<script src="/static/js/jquery-ui.min.js"></script> 
<!--<script src="/static/js/tmpl.min.js"></script>--> 
<script src="/static/js/jquery.iframe-transport.js"></script> 
<script src="/static/js/jquery.fileupload.js"></script> 
<script src="/static/js/jquery.fileupload-process.js"></script> 
<script src="/static/js/jquery.fileupload-validate.js"></script> 
<script src="/static/js/jquery.fileupload-ui.js"></script> 
<script src="/assets/javascripts/upload.js"></script>

<%@include file="body-scripts-end.jspf" %>

<%@include file="body-end.jspf" %>