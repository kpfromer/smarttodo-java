/*================================================================================
	Item Name: Materialize - Material Design Admin Template
	Version: 3.1
	Author: GeeksLabs
	Author URL: http://www.themeforest.net/user/geekslabs
================================================================================

NOTE:
------
PLACE HERE YOUR OWN JS CODES AND IF NEEDED.
WE WILL RELEASE FUTURE UPDATES SO IN ORDER TO NOT OVERWRITE YOUR CUSTOM SCRIPT IT'S BETTER LIKE THIS. */



/*
 * THIS IS MODIFIED CODE THAT I DID NOT ORIGINALLY WRITE!
 * Thanks to egstudio.biz for posting their code, it was very helpful.
 * Here is the link to the article
 * http://www.egstudio.biz/tiny-inline-edit-plugin-for-jquery/
 * */

$.fn.inlineEdit = function(replaceWith) {

    //$(document).on('click', '.description-display .eventText-display', function() {

    $('.description-display, .eventText-display').click(function(event) {

        var elem = $(this).closest('.todo-display');
        replaceWith = elem.parent().find(replaceWith);

        //todo: add validation(will make site faster)

        event.preventDefault();
        event.stopPropagation();

        elem.hide();
        // elem.after(replaceWith);
        replaceWith.show();
        replaceWith.focus();

        replaceWith.blur(function() {

            if (replaceWith.find('.description-text').val() !== "") {
                elem.find('.description-value').val(replaceWith.find('.description-text').val()).change();
                elem.find('.eventText-value').val(replaceWith.find('.eventText-text').val()).change();

                elem.find('.description-display').text(replaceWith.find('.description-text').val());
                elem.find('.eventText-display').text(replaceWith.find('.eventText-text').val());

                replaceWith.hide();
                elem.show();
                elem.closest("form").submit();
            }
        });

        //todo: set replaceWith value with the elem text value

        replaceWith.keydown(function(e) {
            if(e.which === 13)
            {
                replaceWith.blur();
            } else if(e.which === 27) {
                replaceWith.hide();
                elem.show();
            }
        });
    });
};



$(document).ready(function () {

    $("#login").validate({
        rules: {
            username: {
                required: true,
                minlength: 5
            },
            password: {
                required: true,
                minlength: 5
            }
        },
        //For custom messages
        messages: {
            username:{
                required: "Enter a username",
                minlength: "Enter at least 5 characters"
            },
            password: {
                required: "Enter a password",
                minlength: "Enter at least 5 characters"
            }
        },
        errorElement : 'div',
        errorPlacement: function(error, element) {
            var placement = $(element).data('error');
            if (placement) {
                $(placement).append(error)
            } else {
                error.insertAfter(element);
            }
        }
    });


    $("#register").validate({
        rules: {
            username: {
                required: true,
                minlength: 4,
                maxlength: 20
            },
            email: {
                required: true,
                email:true
            },
            password: {
                required: true,
                minlength: 8,
                maxlength: 100
            },
            matchingPassword: {
                required: true,
                minlength: 8,
                maxlength: 100,
                equalTo: "#password"
            }
        },
        //For custom messages
        messages: {
            username:{
                required: "Enter a username",
                minlength: "Enter at least 5 characters",
                maxlength: "Username must be less than 20 characters"
            },
            email:{
                required: "Enter an email address",
                email: "Enter a valid email address"
            },
            password:{
                required: "Enter a password",
                minlength: "Enter at least 5 characters",
                maxlength: "Password must be less than 100 characters"
            },
            matchingPassword:{
                required: "Enter the same password",
                minlength: "Enter at least 5 characters",
                equalTo: "Passwords must match",
                maxlength: "Password must be less than 100 characters"
            }
        },
        errorElement : 'div',
        errorPlacement: function(error, element) {
            var placement = $(element).data('error');
            if (placement) {
                $(placement).append(error)
            } else {
                error.insertAfter(element);
            }
        }
    });


    $('.enter-submit').keypress(function (e) {
        var code = e.keyCode || e.which;

        if (code === 13) {
            e.preventDefault();
            $(this).closest("form").submit();
        }
    });


    $('.todo-checkbox').on('click', function(e) {
        if (e.target !== this)
            return;

        var checkboxInput = $(this).parent().find('input[type=checkbox]');
        checkboxInput.prop('checked', !checkboxInput.prop('checked'));
        $(this).closest('form').submit();
    });


    $('.todo-display').inlineEdit('.todo-input');
});