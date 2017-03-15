

var createEditPanel = function (fieldPrefix, uniqueId, displayName, currentVal) {
    var newNoteVal = currentVal.split("<p>").join("\n");
    newNoteVal = newNoteVal.split("</p>").join("\n");
    newNoteVal = newNoteVal.split("<div>").join("\n");
    newNoteVal = newNoteVal.split("</div>").join("\n");
    
    var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
    var csrfToken = $("meta[name='_csrf']").attr("content");

    var split = fieldPrefix.split('/');
    var type;
    type = split[1];

    var formContainer = $('#editForm .panel-body');
    $(formContainer).empty();
    $(formContainer).append('<input type="hidden" name="' + csrfParameter + '" value="' + csrfToken + '"/>');
    if (uniqueId !== null) {
        $(formContainer).append('<input type="hidden" name="fieldName" value="' + fieldPrefix + '/' + uniqueId + '"/>');
    } else {
        $(formContainer).append('<input type="hidden" name="fieldName" value="' + fieldPrefix + '"/>');
    }


    var templateName;
    if (type === 'address') {
        templateName = 'address-template';
    } else if (type === 'positionHistory') {
        templateName = 'positionHistory-template';
    } else if (type === 'educationalOrganizations') {
        templateName = 'educationalOrganizations-template';
    } else if (type === 'highestEducationLevel') {
        templateName = 'educationLevel-template';
    } else if (type === 'notes') {
        templateName = 'note-text-template';
    } else {
        templateName = 'string-template';
    }

    if (templateName !== undefined) {
        var newPanel = $('#' + templateName).clone();
        $(newPanel).removeAttr('id');
        if (type === 'address') {
            //we getting all crazy up in hure
            if (arguments.length === 7) {
                $('[name=loc-street-address]', newPanel).val(arguments[3]);
                $('[name=loc-city]', newPanel).val(arguments[4]);
                $('[name=loc-state]', newPanel).val(arguments[5]);
                $('[name=loc-zipcode]', newPanel).val(arguments[6]);
            }
        } else if (type === 'positionHistory') {
            //we getting all crazy up in hure
            if (arguments.length === 7) {
                $('[name=emp-name]', newPanel).val(arguments[3]);
                $('[name=emp-pos-title]', newPanel).val(arguments[4]);
                $('[name=emp-start-date]', newPanel).val(arguments[5]);
                $('[name=emp-end-date]', newPanel).val(arguments[6]);
            }
        } else if (type === 'educationalOrganizations') {
            //we getting all crazy up in hure
            if (arguments.length === 6) {
                $('[name=school-name]', newPanel).val(arguments[3]);
                $('[name=school-start-date]', newPanel).val(arguments[5]);
                $('[name=school-end-date]', newPanel).val(arguments[6]);
            }
        } else if (type === 'highestEducationLevel') {
            //we getting all crazy up in hure
            $('[name=educationLevel]', newPanel).val(currentVal);
        } else if (type === 'notes') {
            //we getting all crazy up in hure
            $('[name=note-title]', newPanel).val(displayName);
            $('[name=note-text]', newPanel).val(newNoteVal);
        } else {
            $('.name-label', newPanel).text(displayName);
            $('.edit-field', newPanel).val(currentVal);
        }
        $('select', newPanel).select2();

        if (type === 'positionHistory' || type === 'educationalOrganizations') {
            $('[data-plugin-datepicker]', newPanel).each(function () {
                var $this = $(this),
                        opts = {'autoclose': true, 'endDate': new Date()};

                var pluginOptions = $this.data('plugin-options');
                if (pluginOptions)
                    opts = pluginOptions;

                $this.themePluginDatePicker(opts);
            });
        } else {
            $('[data-plugin-datepicker]', newPanel).each(function () {
                var $this = $(this),
                        opts = {'autoclose': true};

                var pluginOptions = $this.data('plugin-options');
                if (pluginOptions)
                    opts = pluginOptions;

                $this.themePluginDatePicker(opts);
            });
        }
        $('.input-daterange input.start-date', newPanel).on('changeDate', function (selected) {
            var startDate = new Date(selected.date.valueOf());
            $('input.end-date', $(this).parent()).datepicker('setStartDate', startDate);
            $('input.end-date', $(this).parent()).datepicker('show');
        });
        newPanel.removeClass('mfp-hide');
        $(formContainer).append(newPanel);
    }
};

var addFormOpened = function (name) {
    if (name === undefined) {
        $('#add-form-type-select').change();
    } else {
        $('#add-form-type-select').select2('val', name);
        $('#add-form-type-select').change();
    }
};
var loadAddForm = function () {
    $('#addForm .panel-body .form-content').empty();
    var type = $('#add-form-type-select').find('option:selected').attr("value");//$('#add-form-type-select').val();
    var templateName = type + '-template';
    var newPanel = $('#' + templateName).clone();
    $(newPanel).removeAttr('id');
    newPanel.removeClass('mfp-hide');
    //initilize any selects with select2
    $('select', newPanel).select2();
    //initilize any textareas with summernote
    $('textarea', newPanel).summernote({"height": 180, "codemirror": {"theme": "ambiance"}, onChange: function (contents, $editable) {
            //I can't figure out why summernote won't set the value of the text area, so I'm having to do it manually which seems really crappy, but it works. 
            $('textarea', newPanel).val($('textarea', newPanel).code());
        }});

    if (type === 'positionHistory' || type === 'educationalOrganizations') {
        $('[data-plugin-datepicker]', newPanel).each(function () {
            var $this = $(this),
                    opts = {'autoclose': true, 'endDate': new Date()};

            var pluginOptions = $this.data('plugin-options');
            if (pluginOptions)
                opts = pluginOptions;

            $this.themePluginDatePicker(opts);
        });
    } else {
        $('[data-plugin-datepicker]', newPanel).each(function () {
            var $this = $(this),
                    opts = {'autoclose': true};

            var pluginOptions = $this.data('plugin-options');
            if (pluginOptions)
                opts = pluginOptions;

            $this.themePluginDatePicker(opts);
        });
    }
    $('[data-plugin-timepicker]').each(function () {
        var $this = $(this),
                opts = {};

        var pluginOptions = $this.data('plugin-options');
        if (pluginOptions)
            opts = pluginOptions;

        $this.themePluginTimePicker(opts);
    });
    $('.input-daterange input.start-date', newPanel).on('changeDate', function (selected) {
        var startDate = new Date(selected.date.valueOf());
        $('input.end-date', $(this).parent()).datepicker('setStartDate', startDate);
        $('input.end-date', $(this).parent()).datepicker('show');
    });
    $('#addForm .panel-body .form-content').append(newPanel);
};

var updateStateSubmit = function (workflow) {
    if (workflow) {
        console.log(workflow);
    }
};


var userSearchThread;
var setupUserQuerySelect = function () {
    $('#s2id_usersToNotify > .select2-choices > .select2-search-field > input').keyup(function () {
        clearTimeout(userSearchThread);
        var target = $(this);
        userSearchThread = setTimeout(function () {
            findUser(target.val());
        }, 500);
    });

};
var findUser = function (query) {
    if (query.length > 0) {
        $.ajax({
            type: "GET",
            cache: false,
            data: {"name": query},
            url: "/users.json",
            success: function (response) {
                if (!response.error) {
                    var users = response.data;
                    if (users.length > 0) {
                        var newOptions = "";
                        for (var i = 0; i < users.length; i++) {
                            if ($("#usersToNotify option[value='" + users[i].id + "']").length === 0) {
                                newOptions += "<option value='" + users[i].id + "'>" + users[i].firstName + " " + users[i].lastName + "</option>";
                            }
                        }

                        $('#usersToNotify').append(newOptions);
                        var temp = $('#usersToNotify').select2('val');
                        $('#usersToNotify').select2().select2('val', temp);

                        setupUserQuerySelect();
                        $('#usersToNotify').select2("open");
                    } else {
                        console.log("no users found");
                    }
                } else {
                    console.error("Error getting users: " + response.message);
                }
            }
        });
    }
};

var addInterestedUser = function (user) {
    var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
    var csrfToken = $("meta[name='_csrf']").attr("content");

    var url = window.location.pathname + '/add/interestedUser.json';
    var data = {
        userId: user.id
    }
    data[csrfParameter] = csrfToken;
    var jqxhr = $.ajax({
        url: url,
        method: 'post',
        data: data,
        success: function (data) {
            //yay!
            new PNotify({
                title: 'Success',
                text: 'Successfully added ' + user.text + ' to this Candidates Notification List',
                type: 'success'
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            new PNotify({
                title: 'Error',
                text: 'An Error Occured saving Users to notify. Please try again later',
                type: 'error'
            });
        }
    });
};
var removeInterestedUser = function (user) {
    var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
    var csrfToken = $("meta[name='_csrf']").attr("content");

    var url = window.location.pathname + '/remove/interestedUser.json';
    var data = {
        userId: user.id
    }
    data[csrfParameter] = csrfToken;
    var jqxhr = $.ajax({
        url: url,
        method: 'post',
        data: data,
        success: function (data) {
            //yay!
            new PNotify({
                title: 'Success',
                text: 'Successfully removed ' + user.text + ' from this Candidates Notification List',
                type: 'success'
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            new PNotify({
                title: 'Error',
                text: 'An Error Occured Removing Users to notify. Please try again later',
                type: 'error'
            });
        }
    });
};

(function ($) {

    $('body').on('click', function (e) {
        $('[data-toggle="popover"]').each(function () {
            //the 'is' for buttons that trigger popups
            //the 'has' for icons within a button that triggers a popup
            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                $(this).popover('hide');
            }
        });
    });

    'use strict';


    var init = function () {
        $('.modal-with-form').magnificPopup({
            type: 'inline',
            preloader: false,
            focus: '.edit-field',
            midClick: true,
            removalDelay: 300,
            mainClass: 'my-mfp-slide-bottom',
            modal: true,
            // When elemened is focused, some mobile browsers in some cases zoom in
            // It looks not nice, so we disable it:
            callbacks: {
                beforeOpen: function () {
                    if ($(window).width() < 700) {
                        this.st.focus = false;
                    } else {
                        this.st.focus = '.edit-field';
                    }
                }
            }
        });

        $(document).ready(function () {
            $('input.pick-date').datepicker({
                format: 'mm/dd/yyyy',
                startDate: 'now',
                orientation: 'bottom'
            });

            $('input.pick-datestart').datepicker({
                format: 'mm/dd/yyyy',
                orientation: 'bottom'
            });


        });

        /*
         Modal Dismiss
         */
        $(document).on('click', '.modal-dismiss', function (e) {
            e.preventDefault();
            $.magnificPopup.close();
        });

        /*
         Modal Confirm
         */
        $(document).on('click', '.modal-confirm', function (e) {
            $.magnificPopup.close();
        });

        setupUserQuerySelect();

        $('#usersToNotify').change(function (evtData) {
            var added = evtData.added;
            var removed = evtData.removed;
            if (added) {
                addInterestedUser(added);
            } else if (removed) {
                removeInterestedUser(removed);
            }
        });

    };

    $(function () {
        init();
    });

}).apply(this, [jQuery]);