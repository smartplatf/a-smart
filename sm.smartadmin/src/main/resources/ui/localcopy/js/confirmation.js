$(document).ready(function() {

    $("#working").modal("show");

    var qs = (function(a) {
        if (a == "")
            return {};
        var b = {};
        for (var i = 0; i < a.length; ++i) {
            var p = a[i].split('=');
            if (p.length != 2)
                continue;
            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
        }
        return b;
    })(window.location.search.substr(1).split('&'));
   
    var url = "http://localhost:9081/bt/Registration/ActivateUser";
    var datastring = "{'RegisterRecord':{'___smart_action___':'lookup','___smart_value___':'" + qs["c"] + "'}}";
    $.ajax({
        url : url,
        type : "post",
        data : datastring,
        dataType : 'json',

        success : function(data) {
            if (data.responses[0].status) {
                $("#working").modal('hide');
                $("#activationsuccess").modal('show');
            } else {
                alert("Invalid user!!");
            }
        },
        error : function(err) {
            alert("Server not reachable" + JSON.stringify(err));
        }
    });

    $("#ok").click(function() {
        window.location = "http://localhost/smartwizard/localcopy/index.html?c=" + qs["c"];
    });
});
