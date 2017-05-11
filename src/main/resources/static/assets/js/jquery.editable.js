// (function($){
//   var escape_html = function(str){
//     return str.replace(/</gm, '&lt;').replace(/>/gm, '&gt;');
//   };
//   var unescape_html = function(str){
//     return str.replace(/&lt;/gm, '<').replace(/&gt;/gm, '>');
//   };
//
//   $.fn.editable = function(event, callback){
//     if(typeof callback !== 'function') callback = function(){};
//     if(typeof event === 'string'){
//       var trigger = this;
//       var action = event;
//       var type = 'input';
//     }
//     else if(typeof event === 'object'){
//       var trigger = event.trigger || this;
//       if(typeof trigger === 'string') trigger = $(trigger);
//       var action = event.action || 'click';
//       var type = event.type || 'input';
//     }
//     else{
//       throw('Argument Error - jQuery.editable("click", function(){ ~~ })');
//     }
//
//     var target = this;
//     var edit = {};
//
//     edit.start = function(e){
//       trigger.unbind(action === 'clickhold' ? 'mousedown' : action);
//       if(trigger !== target) trigger.hide();
//       var old_value = (
//         type === 'textarea' ?
//           target.text().replace(/<br( \/)?>/gm, '\n').replace(/&gt;/gm, '>').replace(/&lt;/gm, '<') :
//           target.text()
//       ).replace(/^\s+/,'').replace(/\s+$/,'');
//
//       var input = type === 'textarea' ? $('<textarea>') : $('<input>');
//       input.val(old_value).
//         css('width', type === 'textarea' ? '100%' : target.width()+target.height() ).
//         css('font-size','100%').
//         css('margin',0).attr('id','editable_'+(new Date()*1)).
//         addClass('editable');
//       if(type === 'textarea') input.css('height', target.height());
//
//       var finish = function(){
//         var result = input.val().replace(/^\s+/,'').replace(/\s+$/,'');
//         var html = escape_html(result);
//         if(type === 'textarea') html = html.replace(/[\r\n]/gm, '<br />');
//         target.html(html);
//         callback({value : result, target : target, old_value : old_value});
//         edit.register();
//         if(trigger !== target) trigger.show();
//       };
//
//       input.blur(finish);
//       if(type === 'input'){
//         input.keydown(function(e){
//           if(e.keyCode === 13) finish();
//         });
//       }
//
//       target.html(input);
//       input.focus();
//     };
//
//     edit.register = function(){
//       if(action === 'clickhold'){
//         var tid = null;
//         trigger.bind('mousedown', function(e){
//           tid = setTimeout(function(){
//             edit.start(e);
//           }, 500);
//         });
//         trigger.bind('mouseup mouseout', function(e){
//           clearTimeout(tid);
//         });
//       }
//       else{
//         trigger.bind(action, edit.start);
//       }
//     };
//     edit.register();
//
//     return this;
//   };
// })(jQuery);


// $.fn.inlineEdit = function(replaceWith, connectWith) {
//
//     $(this).hover(function() {
//         $(this).addClass('hover');
//     }, function() {
//         $(this).removeClass('hover');
//     });
//
//     $(this).click(function() {
//
//         var elem = $(this);
//
//         elem.hide();
//         elem.after(replaceWith);
//         replaceWith.focus();
//
//         replaceWith.blur(function() {
//
//             if ($(this).val() != "") {
//                 $(this).closet(connectWith).val($(this).val()).change();
//                 elem.text($(this).val());
//             }
//
//             $(this).remove();
//             elem.show();
//         });
//     });
// };

/*
* THIS IS MODIFIED CODE THAT I DID NOT ORIGINALLY WRITE!
* Thanks to egstudio.biz for posting their code, it was very helpful.
* Here is the link to the article
* http://www.egstudio.biz/tiny-inline-edit-plugin-for-jquery/
* */

$.fn.inlineEdit = function(replaceWith, connectWith) {

    // $(this).hover(function() {
    //     $(this).addClass('hover');
    // }, function() {
    //     $(this).removeClass('hover');
    // });

    $(this).click(function() {

        var elem = $(this);

        // event.preventDefault();

        //todo: add validation(will make site faster)

        elem.hide();
        elem.after(replaceWith);
        replaceWith.focus();

        replaceWith.blur(function() {

            if ($(this).val() !== "") {
                elem.parent().find(connectWith).val($(this).val()).change();
                elem.text($(this).val());
                elem.closest("form").submit();
            }

            $(this).remove();
            elem.show();
        });

        //todo: set replaceWith value with the elem text value

        replaceWith.keydown(function(e) {
            if(e.which === 13)
            {
                replaceWith.blur();
            } else if(e.which === 27) {
                $(this).remove();
                elem.show();
            }
        });
    });
};












// $.fn.inlineEdit = function(replaceWith, connectWith, postUrl)
// {
//     $(this).hover(function() {
//         $(this).addClass('hover');
//     }, function() {
//         $(this).removeClass('hover');
//     });
//
//     $(this).click(function()
//     {
//         var elem = $(this);
//
//         elem.hide();
//         elem.after(replaceWith);
//         replaceWith.focus();
//
//         replaceWith.blur(function()
//         {
//             if ($(this).val() != "") {
//                 connectWith.val($(this).val()).change();
//                 elem.text($(this).val());
//
//                 // Send modification to PHP
//                 $.ajax({
//                     type: "POST",
//                     url: postUrl,
//                     data: $(this).val()
//                 });
//             }
//             $(this).remove();
//             elem.show();
//         });
//
//
//     });
// };
