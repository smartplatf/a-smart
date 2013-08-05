var loader = {};
for (var i = 0, j = 0; i < _saProp.length; i++) {
    loader[_saProp[i][j]] = _saProp[i][j + 1];
}

require("http://demo.smart-platform.com/js/smartcom2.js");
require("http://demo.smart-platform.com/js/vodafone_demo.js");

function require(script) {
    $.ajax({
        url : script,
        dataType : "script",
        async : false,
        success : function() {
        },
        error : function() {
            throw new Error("Could not load script " + script);
        }
    });
}


$(document).ready(function() {
    //alert(loader.tenant + loader.flow + loader.group + loader.evnt);
    smart.submitSuccess = loader.success;
    smart.submitFailure = loader.fail;
    smart.tenant = loader.tenant;
    smart.flowName = loader.flow;
    smart[loader.evnt]({
        'flow' : loader.flow,
        'group' : loader.group,
        'size' : 100
    }, "dropdown");

});
