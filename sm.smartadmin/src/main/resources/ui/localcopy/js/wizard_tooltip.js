var TOOLTIP_TITLE = "SMART Demo Help";
var EMAIL_FIELD_HELP_TEXT = "Enter your email id. This email id is used for verification.";
var GO_BUTTON_TEXT = "This verifies if email is already registered."

var CHOOSE_FROM_FLOWS_BUTTON_HELP_TEXT = "If you have any Flow deployed in SMART, then click on this button to try it.";
var NEW_FLOW_BUTTON_HELP_TEXT = "Click here to create a new Flow.";

var TENANT_FIELD_HELP_TEXT = "This is the autogenerated SMART Tenant ID for all your applications."
var FLOW_FIELD_HELP_TEXT = "Name your new Flow here. It has to be unique."

var OBJECT_FIELD_HELP_TEXT = "Name your Prime Object here. This identifies the data stored for your Flow."

var CREATE_FRESH_HELP_TEXT = "<span class='highlight_green'>You can also add your own.</span>";
var SELECT_FROM_TEMPLATE_HELP_TEXT = "<span class='highlight_green'>Select from a pre-defined SMART template list</span>";
var PREVIEW_HELP_TEXT = "Have a preview of the entered field history so far.";

var KEY_HELP_TEXT = "Key acts a index to your data record. Will be necessary to carry out Find & Search operations on a record";
var PREVIEW_SCREEN_HELP_TEXT = "Have a preview and confirm the data fields entered so far and select a key to this record.";
var SMART_TRY_SCREEN_HELP_TEXT = "SMART Try Screen helps you have a hands on experience of your current or any previously created Application/Flows.";

var NEXT_BUTTON_TEXT = "<span class='highlight_red'>Click Next to continue</span>";
var FINISH_BUTTON_TEXT = "<span class='highlight_red'>Click Finish to continue</span>";

$(document).ready(function() {
    //tab handler
    $('#tabs').click(function(e) {
        e.preventDefault();
        $(this).tab('show');
        // $('ul li:nth-child(2)').on('shown', function() {
        // window.scroll(0, 180);
        // });
    });

    $("#email").popover({
        'animation' : true,
        'html' : true,
        'title' : TOOLTIP_TITLE,
        'content' : EMAIL_FIELD_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'bottom',

    });

    $("#go").popover({
        'animation' : true,
        'html' : true,
        'title' : TOOLTIP_TITLE,
        'content' : GO_BUTTON_TEXT,
        'trigger' : 'hover',
        'placement' : 'right',

    });
    
     $("#choosefromflow").popover({
     'animation' : true,
     'html' : true,
     //'title' : TOOLTIP_TITLE,
     'content' : CHOOSE_FROM_FLOWS_BUTTON_HELP_TEXT,
     'trigger' : 'hover',
     'placement' : 'top'
     });

    $("#newflow").popover({
        'animation' : true,
        'html' : true,
        //'title' : TOOLTIP_TITLE,
        'content' : NEW_FLOW_BUTTON_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'left'
    });

    $("#tenant").popover({
        'animation' : true,
        'html' : true,
        'title' : TOOLTIP_TITLE,
        'content' : TENANT_FIELD_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'left',
    });

    $("#flow").popover({
        'animation' : true,
        'html' : true,
        'title' : TOOLTIP_TITLE,
        'content' : FLOW_FIELD_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'left',
    });

    $("#objname").popover({
        'animation' : true,
        'html' : true,
        'title' : TOOLTIP_TITLE,
        'content' : OBJECT_FIELD_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'right'
    });

    $("#addfield").popover({
        'animation' : true,
        'html' : true,
        //'title' : TOOLTIP_TITLE,
        'content' : CREATE_FRESH_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'bottom'
    });

    $("#templatepopover").popover({
        'animation' : true,
        'html' : true,
        //'title' : TOOLTIP_TITLE,
        'content' : SELECT_FROM_TEMPLATE_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'top'
    });

    $("#forpopover").popover({
        'animation' : true,
        'html' : true,
        //'title' : TOOLTIP_TITLE,
        'content' : PREVIEW_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'right'
    });

    $("#key").popover({
        'animation' : true,
        'html' : false,
        'title' : TOOLTIP_TITLE,
        'content' : KEY_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'top'
    });

    $("#step6").popover({
        'animation' : true,
        'html' : true,
        'title' : TOOLTIP_TITLE,
        'content' : PREVIEW_SCREEN_HELP_TEXT,
        'trigger' : 'hover',
        'placement' : 'right'
    });

    $(".buttonNext").popover({
        'animation' : true,
        'html' : true,
        //'title' : TOOLTIP_TITLE,
        'content' : NEXT_BUTTON_TEXT,
        'trigger' : 'hover',
        'placement' : 'top'
    });

    $(".buttonFinish").popover({
        'animation' : true,
        'html' : true,
        //'title' : TOOLTIP_TITLE,
        'content' : FINISH_BUTTON_TEXT,
        'trigger' : 'hover',
        'placement' : 'top'
    });
});

function popovercontrol(obj, context) {

}
