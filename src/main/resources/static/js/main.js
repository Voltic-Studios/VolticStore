$("#showmenu").click(function(e){
    $("#menu").toggleClass("show");
});

$("#menu a").click(function(event){
    if($(this).next('ul').length){
        event.preventDefault();
        $(this).next().toggle('fast');
        $(this).children('i:last-child').toggleClass('fa-caret-down fa-caret-right');
    }
});


$(".faq h3").click(function(){
    $(this).next(".faq-item").toggle('fast');
    $(this).children('i').toggleClass('fa-caret-down fa-caret-right');
});


$(document).ready(function () {
    setTimeout(function(){
        $('.popup').slideDown(500);
    }, 3000);

    $('#close').on('click', function() {
        $('.popup').slideUp(500);
    });
});