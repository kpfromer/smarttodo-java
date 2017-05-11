$(document).ready(function () {

    $('input:text').keypress(function (e) {
        var code = e.keyCode || e.which;

        if (code === 13) {
            e.preventDefault();
            $(this).closest("form").submit();
        }
    });


    $('input[type=checkbox]').each(function() {
        if(this.nextSibling.nodeName !== 'label') {
            $(this).after('<label for="'+this.id+'"></label>')
        }
    })

//        $('.todo-item > input[type=checkbox]').change(function() {
//            if($(this).prop('checked') === true) {
//                alert("Checked Box Selected");
//            } else {
//                alert("Checked Box deselect");
//            }
//        });


    // $(".todo-item .editable").editable("click", function(e){
    //     var target = $(e.target);
    //     var classes = target.attr('class').split(/\s+/);
    //     var classText = classes[1];
    //     var className = classText.split("-");
    //
    //     target.siblings("." + className[0] + '-value').ex(0).val(e);
    //     target.closest("form").submit();
    // });//todo: add for event-text

    //todo: simplify
    var replaceWith = $('<input class="col s9" type="text" />'),
        connectWith = '.description-value';

    $('.description-text').inlineEdit(replaceWith, connectWith);

    var replaceWith1 = $('<input class="col s2" type="text" />'),
        connectWith1 = '.eventText-value';

    $('.eventText-text').inlineEdit(replaceWith1, connectWith1);
});