var stompClient;
(function($) {

    'use strict';

    var init = function() {

        // use SockJS implementation instead of the browser's native implementation
        var ws = new SockJS("/notification");
        stompClient = Stomp.over(ws);
        stompClient.connect("guest", "guest", function(x) {
            console.log('WebSocket Connected');
            stompClient.isConnected = true;
        });


    };

    $(function() {
        var pathname = window.location.pathname;
        if (pathname !== "/login") {
            init();
        }
    });

}).apply(this, [jQuery]);

var subscribeToUploadProgress = function(uploadId, callback) {
    if (stompClient !== undefined && stompClient.isConnected) {
        return stompClient.subscribe("/topic/upload/" + uploadId, callback);
    } else {
        console.log("WebSocket is not connected.  Unable to subscribe");
    }
};
var unsubscribeFromUploadProgress = function(sub) {
    sub.unsubscribe();
};