
var handleFileProcessingComplete = function (task) {
    $('#candidate-upload-panel').fadeIn().removeClass('mfp-hide');
    var source = $("#candidate-added-template").html();
    var template = Handlebars.compile(source);


    var url = task['taskDetail']['url'];
    var stringArray = url.split('/');
    var id = stringArray[2];

    var first = $("#first").val();
    var last = $("#last").val();

    if (first !== null && first.trim() !== "" && last !== null && last.trim() !== "") {

        var csrfToken = $("meta[name='_csrf']").attr("content");
        $.ajax({
            type: "POST",
            cache: false,
            headers: {
                'X-CSRF-Token': csrfToken
            },
            data: {first: first,
                last: last,
            },
            url: "/candidate/" + id + "/addName",
            success: function (response) {

            }
        });
    }

    //if statement for if first and last are blank
    var context;
     if (first !== null && first.trim() !== "" && last !== null && last.trim() !== "") {
     context = {
        'name': first + " " + last,
        'url': task['taskDetail']['url'],
        'status': task['taskName']
    };
     }else
     {
         context = {
        'name': task['taskDetail'].name,
        'url': task['taskDetail']['url'],
        'status': task['taskName']
    };
    }

    var html = template(context);
    $(html).appendTo('#added-candidate-list').fadeIn();



    var name = "Parsing Resume";
    if (task !== undefined) {
        name = task.taskName;
    }
    new PNotify({
        title: 'Success!',
        text: name + 'Completed Successfully!',
        type: 'success'
    });


};
