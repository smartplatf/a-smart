var stuff;
var primeObjFields = [];
var BUTTON_TEXT = "Click to go to custom page";

var urlread = (function(a) {
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

$("#listalldrop").change(function() {
    var selected = $(this).find(':selected').attr('id');
    var selectedUser = $(this).find(':selected').val();
    //alert(selected + "has" + JSON.stringify(stuff[selected - 1]));
    if (selected != 0) {
        $("#resultdiv").show();
        $("#listresultdiv").empty();
        var cleanData = removeJunk(stuff[selected - 1]);
        linkLang = properList(cleanData);
        $("#wait").show();
        getLink(selectedUser);
    } else {
        $("#resultdiv").hide();
        $("#listresultdiv").empty();
        $("#customlink").empty().hide();
    }
});

function getLink(key){
    var url = "http://demo.smart-platform.com/api/"+ loader.tenant + "/UserProfileFlow/GenerateURL";
    var datastring = "{'UserProfile':{'___smart_action___':'lookup','___smart_value___':'" + key +"'}}";
    submitToSmart(url,datastring,function(data){
        generateLink(data);
    });
}

function generateLink(data) {
    $("#wait").hide();
    //alert("in generate link--->" + JSON.stringify(data));
    var link = data.url;//"http://" + linkLang + ".yahoo.com/";
    $("#customlink").empty().append("<div><img  class='offset1' src='images/image.png'><div> This is a custom link generated based on the selected user profile</div></div>").show();
    $("#customlink").append("<a href='" + link + "' target='_blank'><button class='btn btn-medium btn-warning btn-block' style='background-color:#ea8511;'>"+ BUTTON_TEXT +" </button></a>");
}

function properList(cleanData) {
     $("#listresultdiv").show();
    for (var i = 0; i < primeObjFields.length; i++) {
        var key = primeObjFields[i].toProperCase();
        $("#listresultdiv").append("<p class='row'><h5 class='span2'>" + key + ":</h5><div class='span3' style='margin-top:10px;'>" + cleanData[primeObjFields[i]] + "</div></p>");
    }
    return cleanData.language;
}

//list dropdown of the vodafone subscribers
function dropdown(listData) {
    $("#wait").hide();
    stuff = listData.resultSet;
    $("#listalldrop").empty().append("<option id=0 selected>Select a phone number</option>");
    if (stuff.length) {
        for (var i = 0; i < stuff.length; i++) {
            $("#listalldrop").append("<option id='" + (i + 1) + "'>" + stuff[i].mobileNumber + "</option>");
        }
        getObjKeys(stuff[0]);
    } else {
        $("#listresultdiv").empty().append().text("NO records available");
    }
}

//remove smart parameters from object meta data
function removeJunk(data) {
    var cleanData = {};
    try {
        for (var i in primeObjFields) {
            if (data[primeObjFields[i]] != undefined) {
                cleanData[primeObjFields[i]] = data[primeObjFields[i]];
            }
        }
    } catch (TypeError) {
        console.log("result is null");
    }
    return cleanData;
}

function getObjKeys(data) {
    for (var j in data) {
        if (!j.startsWith("_")) {
            primeObjFields.push(j);
        }
    }
}

function success(data) {
    console.log("Success!!!! :" + JSON.stringify(data));
}

function fail(err) {
    console.log("Error!!! :" + JSON.stringify(err));
}
