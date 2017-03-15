var conditionAttrValue = function(value) {
    return value.replace(/\.+/g, '-').replace(/\s+/g, '-');
};

var addNewRunningTask = function(id, displayText) {
    openRunningTasks();
    $('<li id="' + conditionAttrValue(id) + '" >' +
            '<p class="clearfix mb-xs">' +
            '<span class="message pull-left">' + displayText + '</span>' +
            '<span class="message pull-right text-dark">0%</span>' +
            '</p>' +
            '<div class="progress progress-xs light mb-xs">' +
            '<div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
            '</div>' +
            '</li>').appendTo('#running-task-list-contents');
    var numTasks = $('#running-task-list-contents li').length;
    $('#running-task-list .badge').text(numTasks);
    $('#running-task-list .notification-title span').text(numTasks);
};

var openRunningTasks = function() {
    if (!$('#running-task-list').hasClass("open")) {
        $('#running-task-list .dropdown-toggle').click();
    }
};

var clearCompletedTasks = function() {
    var tasks = $('#running-task-list-contents li');
    $.each(tasks, function(index, task) {
        var progressBar = $(task).find('.progress-bar');
        if (progressBar.attr('aria-valuenow') == 100) {
            task.remove();
        }
    });
    var numTasks = $('#running-task-list-contents li').length;
    if (numTasks == 0) {
        $('#running-task-list .badge').text("");
    } else {
        $('#running-task-list .badge').text(numTasks);
    }
    $('#running-task-list .notification-title span').text(numTasks);
};

var handleShutdown = function() {
    stompClient.disconnect();
};

var createSkill = function(data, callback) {
    var url = "/skill.json";
    $.ajax({
        type: "POST",
        data: data,
        url: url,
        success: function(data) {
            if (data.error) {
                console.error("There was an error creating the skill: " + data.errMessage);
            } else {
                console.info("Successfully create skill");
                new PNotify({
                    title: 'Success!',
                    text: 'New Skill Created Successfully',
                    type: 'success'
                });
                if (callback) {
                    callback(data);
                }
            }
        }
    });
};
var createCertification = function(data, callback) {
    var url = "/certification.json";
    $.ajax({
        type: "POST",
        data: data,
        url: url,
        success: function(data) {
            if (data.error) {
                console.error("There was an error creating the certification: " + data.errMessage);
            } else {
                console.info("Successfully create certification");
                new PNotify({
                    title: 'Success!',
                    text: 'New Certification Created Successfully',
                    type: 'success'
                });
                if (callback) {
                    callback(data);
                }
            }
        }
    });
};

var formatDateToYearMonthDay = function(timeInMillis) {
    var date = new Date(timeInMillis);
    return $.format.date(date, "MM/dd/yyyy");
}