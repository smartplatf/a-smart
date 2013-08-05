function getCookie(Name) {//get cookie value
    var re = new RegExp(Name + "=[^;]+", "i");
    //construct RE to search for target name/value pair
    if (document.cookie.match(re))//if cookie found
        return document.cookie.match(re)[0].split("=")[1]
    //return its value
    return ""
}

function setCookie(name, value, days) {//set cookie value
    var expireDate = new Date()
    //set "expstring" to either future or past date, to set or delete cookie, respectively
    var expstring = expireDate.setDate(expireDate.getDate() + parseInt(days))
    document.cookie = name + "=" + value + "; expires=" + expireDate.toGMTString() + "; path=/";
}

/*
var globalVar;
function init() {
    globalVar = 1;
    setCookie("globalVar", "arjun", 0.1)
}*/

