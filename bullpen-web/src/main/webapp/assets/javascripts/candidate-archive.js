var dataTable;

var getMostRecentResume = function(resumes) {
    var mostRecent = null;
    for (var i = 0; i < resumes.length; i++) {
        var r = resumes[i];
        if (mostRecent === null) {
            mostRecent = r;
        } else {
            if (r.dateAdded > mostRecent.dateAdded && r.format !== 'HRXML') {
                mostRecent = r;
            }
        }
    }
    return mostRecent;
};
var addNewCandidateCard = function (item) {
    var source = $("#candidate-card-template").html();
    var template = Handlebars.compile(source);

    var mostRecentResume = getMostRecentResume(item['resumes']);
    var context = {
        'name': item['FormattedName'],
        'title': item['title'],
        'lastContact': moment(item['lastContact']).format('DD MMM YYYY'),
        'url': '/candidate/' + item['id'],
        'resumeUrl': '/resume/' + mostRecentResume.storageId,
        'resumeName': mostRecentResume.filename

    };

    var emails = [];
    if (item['Email'] !== undefined && item['Email'] !== null && item['Email'].length > 0) {
        context['imageUrl'] = '//www.gravatar.com/avatar/' + item['Email'][0].addressMD5Hash + '?s=100&d=mm';
        item["Email"].every(function (element, index, array) {
            if (index < 2) {
                emails.push({'email': element['Address']});
                return true;
            } else {
                return false;
            }
        });
        context['emails'] = emails;
    } else {
        context['imageUrl'] = '//www.gravatar.com/avatar/?s=100&d=mm';
    }

    var phones = [];
    if (item['Phone'] !== undefined && item['Phone'] !== null && item['Phone'].length > 0) {
        item["Phone"].every(function (element, index, array) {
            if (index < 2) {
                phones.push({'number': element['Number']});
                return true;
            } else {
                return false;
            }
        });
        context['phones'] = phones;
    }

    var html = template(context);

    $('#result-list').append(html);
};

var handleFileProcessingComplete = function () {
    getAllCandidates();
};

var populateCandidates = function (data,showall) {
    $('#result-list').empty();
    var results = data.data;
    for (var i = 0; i < results.length; i++) {
        var item = results[i];
        if(item["archive"] === true || showall){
        addNewCandidateCard(item);}
    }
};
var getAllCandidates = function () {
    var jqxhr = $.ajax("/candidate/all.json")
            .done(function (data) {
                populateCandidates(data,false);
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                new PNotify({
                    title: 'Error',
                    text: 'An Error Occured while querying for Candidates.  Please try again later',
                    type: 'error'
                });
            });
};

var setupSearchForm = function () {
    $("#search-form").submit(function (event) {
        event.preventDefault();
        var $form = $(this);
        var data = $form.serializeArray();
        var url = "/candidates/search.json";
        $.ajax({
            type: "GET",
            data: data,
            url: url,
            success: function (data) {
                populateCandidates(data,true);
                new PNotify({
                    title: 'Query Complete',
                    text: 'Query Completed Successfully and found ' + data.data.length + ' Candidates',
                    type: 'success'
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                new PNotify({
                    title: 'Error',
                    text: 'An Error Occured while querying for Candidates.  Please try again later',
                    type: 'error'
                });
            }
        });
    });

    $("select").closest("form").on("reset", function (ev) {
        var targetJQForm = $(ev.target);
        setTimeout((function () {
            $('select[name=state]').select2().val('none');
            $('select[name=educationLevel]').select2().val('none');
            this.find("select").trigger("change");
            getAllCandidates();
        }).bind(targetJQForm), 0);
    });
};

(function ($) {

    'use strict';

    var pageInit = function () {
        getAllCandidates();
        setupSearchForm();
    };
    var datatableInit = function () {

        dataTable = $('#datatable-ajax').dataTable({
            "bProcessing": true,
            "sAjaxSource": '/candidate/all',
            "sAjaxDataProp": "data",
            "aaSorting": [[1, "desc"]],
            "iDisplayLength": 50,
            "aoColumns": [
                {"sDefaultContent": "",
                    "fnRender": function (oObj) {
                        return '<a class="fa fa-search" href="/candidate/' + oObj.aData['id'] + '"></a>';

                    }
                },
                {"sDefaultContent": "",
                    "fnRender": function (oObj) {
                        return '<a href="/candidate/' + oObj.aData['id'] + '">' + oObj.aData['FormattedName'] + '</a>';

                    }
                },
                {"sDefaultContent": "",
                    "fnRender": function (oObj) {
                        if (oObj.aData['Email'] !== undefined && oObj.aData['Email'] !== null) {
                            return '<a class="" href="mailto:' + oObj.aData.Email[0].Address + '">' + oObj.aData.Email[0].Address + '</a>';
                        } else {
                            return "";
                        }
                    }
                },
                {"sDefaultContent": "",
                    "fnRender": function (oObj) {
                        if (oObj.aData['Phone'] !== undefined && oObj.aData['Phone'] !== null) {
                            return oObj.aData.Phone[0].Number;
                        } else {
                            return "";
                        }
                    }
                },
                {"sDefaultContent": "",
                    "fnRender": function (oObj) {
                        if (oObj.aData.resumes !== undefined && oObj.aData.resumes !== null && oObj.aData.resumes.length > 0) {
                            return '<a class="" href="/resume/' + oObj.aData.resumes[0].storageId + '">' + oObj.aData.resumes[0].filename + '</a>';
                        } else {
                            return 'error';
                        }
                    }
                }
            ]
        });

    };

    $(function () {
//        datatableInit();
        pageInit();
    });

}).apply(this, [jQuery]);