var TOOLTIP_TITLE = "SMART HandsOn-Demo Help";
var GET_MY_FLOWS_HELP_TEXT = "Click here to get your deployed SMART Flows";

$(document).ready(function() {
    //tab handler
    $('#tabs').click(function(e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $("#getmyflows").popover({
        'animation' : true,
        'html' : true,
        'title' : TOOLTIP_TITLE,
        'content' : GET_MY_FLOWS_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'right',

    });

});
