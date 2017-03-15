(function ($) {

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

        var selectedPremisison = $("#userEdit").find('option:first').attr('id');
        selectedPremisison = "ROLE_" + selectedPremisison;
        $("#premission").val(selectedPremisison).change();



        $("#userEdit").change(function () {
            var val = $("#userEdit").find('option:selected').attr('id');
            val = "ROLE_" + val;
            $("#premission").val(val).change();

        });
        /*
         Modal Dismiss
         */
        $(document).on('click', '.modal-dismiss', function (e) {
            e.preventDefault();
            $.magnificPopup.close();
        });
        
        $('.btn').attr('title', '');//removes tooltip from sumernote buttons which gave werid tooltip issues

        /*
         Modal Confirm
         */
        $(document).on('click', '.modal-confirm', function (e) {
            $.magnificPopup.close();
        });

        $('#edit-user-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                }


            }).fail(function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;


                if (code === 418) {
                    new PNotify({
                        title: 'Error',
                        text: 'Cannot remove yourself, please try again',
                        type: 'error'
                    });
                } else if (code === 302) {
                    new PNotify({
                        title: 'Success',
                        text: 'Updated user succesfully',
                        type: 'success'
                    });

                }

            });
        });


        $('#create-department-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function () {
                    var data2 = formData[0];
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully edited department',
                        type: 'success'
                    });
                    $.magnificPopup.close();
                    addStringToSelect("departments", data2.value);
                    addStringToSelect("department", data2.value);
                },    
                error: function (jqXHR, textStatus, errorThrown) {
                    var code = jqXHR.status;

                    //location.reload();

                    if (code === 202) {
                        new PNotify({
                            title: 'Success',
                            text: 'Successfully edited department',
                            type: 'success'
                        }); 
                    } else if (code === 406) {
                        new PNotify({
                            title: 'Error',
                            text: 'Department name already exists',
                            type: 'error'
                        });
                    } else if (code === 403) {
                        new PNotify({
                            title: 'Error',
                            text: 'Department name invalid',
                            type: 'error'
                        });
                    } else if (code === 400) {
                        new PNotify({
                            title: 'Error',
                            text: 'Error editing department',
                            type: 'error'
                        });
                    }
                }
            });
        });
        
        $('#create-preset-note-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                    var title = formData[0];
                    addStringToSelect('PresetNotes', title.value);
                    location.reload();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    var title = formData[0];
                    if (title.value.includes(".")) {
                        new PNotify({
                            title: 'Error',
                            text: 'Please remove punctuation from the title and try again',
                            type: 'error'
                        });
                    } else {
                        new PNotify({
                            title: 'Error',
                            text: 'An Error Occured creating new Preset Note. Please try again later',
                            type: 'error'
                        });
                    }
                }
            });
        });
        

        $('#create-category-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                    addItemToSelect('categories', data.data);
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully created new Category',
                        type: 'success'
                    });
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    new PNotify({
                        title: 'Error',
                        text: 'An Error Occured creating new Category. Please try again later',
                        type: 'error'
                    });
                }
            });
        });
        
        $('#remove-user-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            var user = $("#user").val();
            user = $("option[value='" + user + "']").text();
            user = user.split(" ").join("");

            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                    //removeUserFromList(user);  
                    //location.assign("/company/profile");
                    //location.reload();

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;


                //location.reload();


                if (code === 418) {
                    new PNotify({
                        title: 'Error',
                        text: 'Cannot remove yourself, please try again.',
                        type: 'error'
                    });
                } else if (code === 302) {
                    new PNotify({
                        title: 'Success',
                        text: 'Removed user succesfully.',
                        type: 'success'
                    });
                } else if (code === 406) {
                    new PNotify({
                        title: 'Error',
                        text: 'Cannot remove the only user from a company.',
                        type: 'error'
                    });
                }
            });
        });
        
        
        $('#remove-location-form').submit(function (e) {  
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            $.magnificPopup.close();
            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                    location.assign("/company/profile");
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    new PNotify({
                        title: 'Error',
                        text: 'Error removing the location. Please try again later.',
                        type: 'error'
                    });
                }
            });
        });
        
        $('#remove-preset-note-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();

            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;


                //location.reload();

                var data2 = formData[0];
                if (code === 302) {
                    removeItemFromSelectByInnerText(data2.value);
                    removeItemFromSelect("presetNote", data2.value);
                    
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully removed the note.',
                        type: 'success'
                    });

                } else if (code === 404) {
                    new PNotify({
                        title: 'Error',
                        text: 'Error accessing the note.',
                        type: 'error'
                    });
                }
            });
        });
        
        $('#remove-department-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();

            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;


                //location.reload();

                var data2 = formData[0];
                if (code === 302) {
                    
                    removeItemFromSelect("departments", data2.value);
                    removeItemFromSelect("department", data2.value);
                    
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully removed the department.',
                        type: 'success'
                    });

                } else if (code === 404) {
                    new PNotify({
                        title: 'Error',
                        text: 'Error accessing the department.',
                        type: 'error'
                    });
                }
            });
        });
        
        $('#remove-skill-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();

            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;


                //location.reload();

                var data2 = formData[0];
                if (code === 302) {
                    
                    removeItemFromSelect("skills", data2.value);
                    removeItemFromSelect("skill", data2.value);
                    
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully removed Skill',
                        type: 'success'
                    });

                } else if (code === 404) {
                    new PNotify({
                        title: 'Error',
                        text: 'Error accessing Skill',
                        type: 'error'
                    });
                }
            });
        });      
        
        $('#remove-certification-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();

            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            }).fail(function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;


                //location.reload();

                var data2 = formData[0];
                if (code === 302) {
                    
                    removeItemFromSelect("certifications", data2.value);
                    removeItemFromSelect("certification", data2.value);
                    
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully removed Certification',
                        type: 'success'
                    });

                } else if (code === 404) {
                    new PNotify({
                        title: 'Error',
                        text: 'Error accessing Certification.',
                        type: 'error'
                    });
                }
            });
        });        
        
        $('#create-skill-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                    addItemToSelect('skills', data.data);
                    addItemToSelect('skill', data.data);
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully created new Skill',
                        type: 'success'
                    });
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    new PNotify({
                        title: 'Error',
                        text: 'An Error Occured creating new Skill. Please try again later',
                        type: 'error'
                    });
                }
            });
        });
        $('#create-certification-form').submit(function (e) {
            e.preventDefault();
            var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            var jqxhr = $.ajax({
                url: url,
                method: 'post',
                data: formData,
                success: function (data) {
                    $.magnificPopup.close();
                    var data2 = formData[0];
                    addStringToSelect('certifications', data2.value);
                    addStringToSelect('certification', data2.value);
                    new PNotify({
                        title: 'Success',
                        text: 'Successfully created new Certification',
                        type: 'success'
                    });
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    var code = jqXHR.status;

                    var data2 = formData[0];
                    if (code === 202) {

                        removeItemFromSelect("certifications", data2.value);
                        removeItemFromSelect("certification", data2.value);

                        new PNotify({
                            title: 'Success',
                            text: 'Successfully created new Certification',
                            type: 'success'
                        });
                    } else if (code === 406) {
                        new PNotify({
                            title: 'Error',
                            text: 'Certification name already exists',
                            type: 'error'
                        });
                    } 
                    else if (code === 400) {
                        new PNotify({
                            title: 'Error',
                            text: 'Error accessing Certification.',
                            type: 'error'
                        });
                    }
                }
            });
        });
    };

    $(function () {
        init();
    });

}).apply(this, [jQuery]);

var addNewAddressTab = function () {
    var numAddrs = $("#address-tab-content .tab-pane").length;
    var numAddrsPlus = numAddrs++;
    var newAddrId = "address-" + numAddrsPlus;
    var newAddr = $("#newAddress-tmpl").clone().attr('id', newAddrId);
    $(newAddr).removeClass("mfp-hide");
    var selects = $("select", newAddr);
    $(".select2-container", newAddr).remove();
    for (var i = 0; i < selects.length; i++) {
        var input = selects[i];
        $(input).select2("destroy");
        var curName = $(input).attr("name");
        var newName = curName + "-" + numAddrsPlus;
        $(input).attr("name", newName);
        $(input).select2();
    }

    var inputs = $("input", newAddr);
    for (var i = 0; i < inputs.length; i++) {
        var input = inputs[i];
        var curName = $(input).attr("name");
        var newName = curName + "-" + numAddrsPlus;
        $(input).attr("name", newName);
    }

    $("#address-tab-content").append(newAddr);
    var newItem = $('<li class=""><a href="#' + newAddrId + '" data-toggle="tab">New Address ' + numAddrsPlus + '</a></li>');
    newItem.insertBefore($("#address-tab-nav li:last-child"));
    $("a", newItem).click();
};

var addStringToSelect = function(selectName, str) {
    $('select[name=' + selectName + ']')
            .append($("<option></option>")
            .attr("value", str)
            .text(str));
};

var addItemToSelect = function (selectName, item) {
    var el = $('select[name=' + selectName + ']');

    var optgroup;
    if (item.category && item.category.displayName) {
        optgroup = $("optgroup[label='" + item.category.displayName + "']", el);
        if (typeof optgroup === 'undefined' || optgroup.length === 0) {
            var newoptgroup = '<optgroup label="' + item.category.displayName + '"></optgroup>';
            el.append(newoptgroup);
            optgroup = $("optgroup[label='" + item.category.displayName + "']", el);
        }
    }

    var newOptions = "<option value='" + item.name + "' selected>" + item.displayName + "</option>";
    if (typeof optgroup !== 'undefined') {
        optgroup.append(newOptions);
    } else {
        el.append(newOptions);
    }
    el.select2('destroy').select2().select2('val', item.name);
};      
var removeItemFromSelect = function (selectName, item) {
    $('select[name="' + selectName + '"] option[value="' + item + '"]').remove()
};

var removeItemFromSelectByInnerText = function (item) {
    $("option:contains('" + item + "')").remove()
};

var removeUserFromList = function (user) {
    $('#' + user).remove();
};

var showEditPanel = function (selectName, type) {
    var name = $('select[name=' + selectName + '] option:selected').attr('value');
    var label = $('select[name=' + selectName + '] option:selected').text();
    var category = $('select[name=' + selectName + '] option:selected').data('category');

    var source = $('#edit-' + type + '-template').html();
    var template = Handlebars.compile(source);

    var context = {
        label: label,
        name: name,
        category: category
    };

    var html = template(context);

    var popup = $.magnificPopup.open({
        items: {src: html},
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
            },
            open: function () {

            }
        }
    }, 0);
    $('select', popup.content).select2(); 

    var form = $('#edit-' + type + '-form', popup.content);
    form.submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        var jqxhr = $.ajax({
            url: url,
            method: 'post',
            data: formData,
            success: function (data) {
                $.magnificPopup.close();
                removeItemFromSelect(selectName, name);
                addItemToSelect(selectName, data.data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                new PNotify({
                    title: 'Error',
                    text: 'An Error Occured Editing Item. Please try again later',
                    type: 'error'
                });
            }
        });

    });
    $('input', form).keypress(function (e) {
        if (e.which == 13) {
            $(e.target).closest('form').submit();
            return false;
        }
    });
};

var showPresetNoteEditPanel = function (selectName, type) {
    var name = $('select[name=' + selectName + '] option:selected').attr('value');
    var label = $('select[name=' + selectName + '] option:selected').text();
    var category = $('select[name=' + selectName + '] option:selected').data('category');

    var source = $('#edit-' + type + '-template').html();
    var template = Handlebars.compile(source);

    var context = {
        label: label,
        name: name,
        category: category
    };

    var html = template(context);

    var popup = $.magnificPopup.open({
        items: {src: html},
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
            },
            open: function () {

            }
        }
    }, 0);
    $('select', popup.content).select2(); 

    var form = $('#edit-' + type + '-form', popup.content);
    form.submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        var jqxhr = $.ajax({
            url: url,
            method: 'post',
            data: formData,
            success: function (data) {
                $.magnificPopup.close();
                var title = formData[0];
                removeItemFromSelect(selectName, name);
                addStringToSelect(selectName, title.value);
                location.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                new PNotify({
                    title: 'Error',
                    text: 'An Error Occured Editing Item. Please try again later',
                    type: 'error'
                });
            }
        });

    });
    $('input', form).keypress(function (e) {
        if (e.which == 13) {
            $(e.target).closest('form').submit();
            return false;
        }
    });
};
var showDepartmentEditPanel = function (selectName, type) {
    var name = $('select[name=' + selectName + '] option:selected').attr('value');
    var label = $('select[name=' + selectName + '] option:selected').text();
    var category = $('select[name=' + selectName + '] option:selected').data('category');

    var source = $('#edit-' + type + '-template').html();
    var template = Handlebars.compile(source);

    var context = {
        label: label,
        name: name,
        category: category
    };

    var html = template(context);

    var popup = $.magnificPopup.open({
        items: {src: html},
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
            },
            open: function () {

            }
        }
    }, 0);
    $('select', popup.content).select2(); 

    var form = $('#edit-' + type + '-form', popup.content);
    form.submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        var jqxhr = $.ajax({
            url: url,
            method: 'post',
            data: formData,
            success: function (data) {
                $.magnificPopup.close();
                
                var data2 = formData[0];
                removeItemFromSelect("departments", name);
                removeItemFromSelect("department", name);
                addStringToSelect("departments", data2.value);
                addStringToSelect("department", data2.value);
                
                new PNotify({
                    title: 'Success',
                    text: 'Successfully edited department.',
                    type: 'success'
                }); 
                
            },
            error: function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;
                
                if (code === 406) {
                    new PNotify({
                        title: 'Error',
                        text: 'Department name already exists.',
                        type: 'error'
                    });
                } else if (code === 403) {
                    new PNotify({
                        title: 'Error',
                        text: 'Department name invalid.',
                        type: 'error'
                    });
                } else if (code === 400) {
                    new PNotify({
                        title: 'Error',
                        text: 'Error editing department.',
                        type: 'error'
                    });
                }  
                $.magnificPopup.close();
            }
            
        });

    });
    $('input', form).keypress(function (e) {
        if (e.which == 13) {
            $(e.target).closest('form').submit();
            return false;
        }
    });
};

var showCertificationEditPanel = function (selectName, type) {
    var name = $('select[name=' + selectName + '] option:selected').attr('value');
    var label = $('select[name=' + selectName + '] option:selected').text();
    var category = $('select[name=' + selectName + '] option:selected').data('category');

    var source = $('#edit-' + type + '-template').html();
    var template = Handlebars.compile(source);

    var context = {
        label: label,
        name: name,
        category: category
    };

    var html = template(context);

    var popup = $.magnificPopup.open({
        items: {src: html},
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
            },
            open: function () {

            }
        }
    }, 0);
    $('select', popup.content).select2(); 

    var form = $('#edit-certification-form', popup.content);
    form.submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        var jqxhr = $.ajax({
            url: url,
            method: 'post',
            data: formData,
            success: function (data) {
                $.magnificPopup.close();
                
                var data2 = formData[0];
                removeItemFromSelect("certifications", name);
                removeItemFromSelect("certification", name);
                addStringToSelect("certifications", data2.value);
                addStringToSelect("certification", data2.value);
                
                new PNotify({
                    title: 'Success',
                    text: 'Successfully edited department.',
                    type: 'success'
                }); 
                
            },
            error: function (jqXHR, textStatus, errorThrown) {
                var code = jqXHR.status;
                
                if (code === 406) {
                    new PNotify({
                        title: 'Error',
                        text: 'Department name already exists.',
                        type: 'error'
                    });
                } else if (code === 403) {
                    new PNotify({
                        title: 'Error',
                        text: 'Department name invalid.',
                        type: 'error'
                    });
                } else if (code === 400) {
                    new PNotify({
                        title: 'Error',
                        text: 'Error editing department.',
                        type: 'error'
                    });
                }  
                $.magnificPopup.close();
            }
            
        });

    });
    $('input', form).keypress(function (e) {
        if (e.which == 13) {
            $(e.target).closest('form').submit();
            return false;
        }
    });
};

var showSkillEditPanel = function (selectName, type) {
    var name = $('select[name=' + selectName + '] option:selected').attr('value');
    var label = $('select[name=' + selectName + '] option:selected').text();
    var category = $('select[name=' + selectName + '] option:selected').data('category');

    var source = $('#edit-' + type + '-template').html();
    var template = Handlebars.compile(source);

    var context = {
        label: label,
        name: name,
        category: category
    };

    var html = template(context);

    var popup = $.magnificPopup.open({
        items: {src: html},
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
            },
            open: function () {

            }
        }
    }, 0);
    $('select', popup.content).select2(); 

    var form = $('#edit-skill-form', popup.content);
    form.submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        var jqxhr = $.ajax({
            url: url,
            method: 'post',
            data: formData,
            success: function (data) {
                $.magnificPopup.close();
                removeItemFromSelect('skills', name);
                removeItemFromSelect('skill', name)
                addItemToSelect('skills', data.data);
                addItemToSelect('skill', data.data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                new PNotify({
                    title: 'Error',
                    text: 'An Error Occured Editing Item. Please try again later',
                    type: 'error'
                });
            }
        });

    });
    $('input', form).keypress(function (e) {
        if (e.which == 13) {
            $(e.target).closest('form').submit();
            return false;
        }
    });
};

