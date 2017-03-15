/**
 * Created by Myles on 1/27/17.
 */
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
            // When element is focused, some mobile browsers in some cases zoom in
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

        $(document).on('click', '.modal-dismiss', function (e) {
            e.preventDefault();
            $.magnificPopup.close();
        });

    };
    $(function () {
        init();
    });

}).apply(this, [jQuery]);

$(document).ready(function () {

    $('.pick-date').datepicker({
        format: 'mm/dd/yyyy',
        orientation: 'auto'
    });

    $('[data-eq]').click(function(){
       var id = $(this).data('eq');
       var csrfToken = $("meta[name='_csrf']").attr('content');
        $.ajax({
            type: 'POST',
            cache: false,
            headers: {
                'X-CSRF-Token': csrfToken
            },
            url: '/equipment/remove/' + id,
            success: function () {
                window.location.reload();
            }
        });
    });

    $('[data-lc]').click(function(){
        var id = $(this).data('lc');
        var csrfToken = $("meta[name='_csrf']").attr('content');
        $.ajax({
            type: 'POST',
            cache: false,
            headers: {
                'X-CSRF-Token': csrfToken
            },
            url: '/license/remove/' + id,
            success: function () {
                window.location.reload();
            }
        });
    });
});




