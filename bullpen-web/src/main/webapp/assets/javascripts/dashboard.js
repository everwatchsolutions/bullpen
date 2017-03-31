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

          
        


    };

    $(function () {
        init();
    });

}).apply(this, [jQuery]);




