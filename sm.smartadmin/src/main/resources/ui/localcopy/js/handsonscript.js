var classname = "";
var updatekey = "";
var metaData;
var primeObjFields = [];

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

$(document).ready(function() {

    //initialize wizard
    $('#wizard').smartWizard({
        // Properties
        selected : 0, // Selected Step, 0 = first step
        keyNavigation : false, // Enable/Disable key navigation(left and right keys are used if enabled)
        enableAllSteps : false, // Enable/Disable all steps on first load
        transitionEffect : 'slide', // Effect on navigation, none/fade/slide/slideleft
        contentURL : null, // specifying content url enables ajax content loading
        contentURLData : null, // override ajax query parameters
        contentCache : true, // cache step contents, if false content is fetched always from ajax url
        cycleSteps : false, // cycle step navigation
        enableFinishButton : false, // makes finish button enabled always
        hideButtonsOnDisabled : false, // when the previous/next/finish buttons are disabled, hide them instead
        errorSteps : [], // array of step numbers to highlighting as error steps
        labelNext : 'Next', // label for Next button
        labelPrevious : 'Previous', // label for Previous button
        labelFinish : 'Finish', // label for Finish button
        noForwardJumping : false,
        //ajaxType : 'POST',
        // Events
        onLeaveStep : null, // triggers when leaving a step
        onShowStep : null, // triggers when showing a step
        onFinish : null // triggers when Finish button is clicked
    });

    //remove action bar
    $(".actionBar").hide();

    initialiseSmart();

    //handle form submit
    $('form').submit(function(event) {
        event.preventDefault();
        var formtype = ($(this).attr('id'));
        var formobj = $(this).toObject({
            mode : 'combine'
        });

        switch(formtype) {
            case "searchform":
                var mapname = formobj.queryMap.name, mapvalue = formobj.queryMap.value;
                formobj.queryMap = {};
                formobj.queryMap[mapname] = mapvalue;

                /*
                for (var i in metaData) {
                if (metaData[i].attributeType == "java.lang.Integer") {
                if (mapname == metaData[i].attributeName) {
                formobj.queryMap[mapname] = parseInt(formobj.queryMap[mapname]);
                }
                }
                }*/

                //smart call
                smart.search(formobj, function(data) {
                    console.log("search-->" + JSON.stringify(data));
                    cleanData = removeJunk(data.searchResult[0]);
                    $("#searchresultdiv").empty().append(JSON.stringify(cleanData));
                });
                break;

            case "lookupform":
                for (var i in metaData) {
                    if (metaData[i].isKey == true) {
                        if ("java.lang.Integer" == metaData[i].attributeType || "int" == metaData[i].attributeType || "long" == metaData[i].attributeType) {
                            formobj.key = parseInt(formobj.key);
                        }
                    }
                }

                //smart call
                smart.lookup(formobj, function(data) {
                    //console.log("lookup-->" + JSON.stringify(data.result[0]));
                    cleanData = removeJunk(data.result[0]);
                    $("#lookupresultdiv").empty().append(JSON.stringify(cleanData));
                });
                break;

            case "listallform":
                if (formobj.size >= 0) {
                    formobj.size = parseInt(formobj.size);
                }
                //smart call
                smart.list(formobj, function(data) {
                    $("#listresultdiv").empty();
                    for (var i in data.resultSet) {
                        cleanData = removeJunk(data.resultSet[i]);
                        $("#listresultdiv").append("<p>" + JSON.stringify(cleanData) + "</p>");
                    }
                });
                break;

            case "addform":
                var result = typeCast(formobj, metaData);
                smart.createPrime(result, function(data) {
                    if (data.success)
                        $("#addresult").empty().append().text("Successfully Added..!").show().fadeOut(2000);
                    console.log("update-->" + JSON.stringify(data));
                });
                break;

            case "updateform":
                var result = typeCast(formobj, metaData);
                result.updatekey = result.data[updatekey];
                smart.updatePrime(result, function(data) {
                    if (data.success)
                        $("#updateresult").empty().append().text("Updated Successfully..!").show().fadeOut(2000);
                    console.log("update-->" + JSON.stringify(data));
                });
                break;
        }
        formobj = {};
    });

});

//initialise smart
function initialiseSmart() {
    smart.submitSuccess = "success";
    smart.submitFailure = "fail";
    smart.flowName = "default";
    smart.tenant = "bt";

    //get tenants id by look up on registration
    smart.lookup({
        'flow' : 'Registration',
        'group' : 'RegisterRecord',
        'key' : urlread['t']
    }, function(data) {

        //fill tenant details
        smart.tenant = data.result[0].tenantId;
        $("#tenantfill").val(smart.tenant);
        //get flows enabled for the tenant
        getTenantFlows();
    });
}

//get all flows enabled for a tenant
function getTenantFlows() {
    smart.listEnabledFlows({}, function(data) {
        var flows = data.enabledFlows;
        $("#flowfill").empty();
        for (var i in flows) {
            if (!(flows[i].startsWith("AdminSmartFlow") || flows[i].startsWith("AllFlows"))) {
                $("#flowfill").append("<label class='radio'><input type='radio' name='flows' id=" + flows[i] + " checked>" + flows[i] + "</label><br>");
            }
        }
        $("#flowfill").append("<input type='button' value='Try' id='trybutton' class='btn btn-medium btn-info'><br><br>");
        $("#flowfill").append("<h4>Please select a flow and hit on 'Try' to use it in further stages.</h4>");

        //try button event handler to get prime of selected flow
        $("#trybutton").click(function() {
            var selectedFlow = $('input[name=flows]:radio:checked').attr('id');
            smart.listDeployments({
                'dType' : 'PrimeData',
                'flow' : selectedFlow
            }, function(data) {
                //alert("PrimeObject list--->" + JSON.stringify(data.deployments));
                var flowClass = data.deployments[0];
                flowClass = flowClass.substr(flowClass.lastIndexOf('.') + 1, flowClass.length);

                //get prime meta for the flow
                getUserMetaData(flowClass, selectedFlow);
            });
        });
    });

}

//clear all forms of prev flow
function clearSteps() {
    $("#lookupform,#addform,#updateform,#searchform,#listallform").empty();
}

//structure createprime and update prime form
function addandUpdateform(dataobj) {
    var obj1 = dataobj;
    obj1.html.unshift({
        "name" : "create",
        "caption" : "Record",
        "type" : "text",
        "placeholder" : "Record To create",
        "validate" : {
            "required" : true,
            "messages" : {
                "required" : "Required input",
            }
        }
    });

    $("#addform").dform(obj1);

    dataobj.html.shift();
    dataobj.html.unshift({
        "name" : "update",
        "caption" : "Update",
        "type" : "text",
        "placeholder" : "Record to update",
        "validate" : {
            "required" : true,
            "messages" : {
                "required" : "Required input",
            }
        }
    });

    $("#updateform").dform(dataobj);
    setGroupInfo();
}

//get meta for the user flow
function getUserMetaData(classname, flow) {
    var metaObj = {
        'flow' : flow,
        'flowClass' : classname
    };
    smart.getPrimeMeta(metaObj, "metadatasuccess");
}

//fill class name in all form automatically
function setGroupInfo() {
    $("input[name=group],input[name=create],input[name=update]").each(function() {
        $(this).val(classname);
        $(this).attr('readonly', 'readonly');
    });
}

//load fresh forms for selected flow
function loadSteps() {
    enableSteps([2, 3, 4, 5, 6]);
    $("#lookupform").dform(ofForm.LOOKUP);
    $("#searchform").dform(ofForm.SEARCH);
    $("#listallform").dform(ofForm.LISTALL);

    //automatically set class name in the form
    setGroupInfo();
    $("#done").show().fadeOut(1500);
}

function enableSteps(list) {
    for (var i = 0; i < list.length; i++)
        $('#wizard').smartWizard("enableStep", list[i]);
}

//display of update and create prime form
function filterAndProcess(data) {
    var resultData = data.attributes;
    metaData = resultData;
    console.log(JSON.stringify(metaData));
    var i = 0;
    formdata = {
        'html' : []
    };

    for ( i = 0; i < resultData.length; i++) {
        if (!(resultData[i].attributeName.startsWith("H"))) {
            formdata.html[i] = {};
            formdata.html[i]["id"] = resultData[i].attributeName;
            formdata.html[i]["name"] = "data." + resultData[i].attributeName;
            formdata.html[i]["caption"] = formdata.html[i].id.toProperCase();
            if (resultData[i].attributeType == "java.lang.String" || resultData[i].attributeType == "java.lang.Integer" || resultData[i].attributeType == "int" || resultData[i].attributeType == "long") {
                formdata.html[i]["type"] = "text";
            }
            if (resultData[i].isKey == true) {
                updatekey = resultData[i].attributeName;
            }
            primeObjFields.push(resultData[i].attributeName);
        }

    }
    return formdata;
}

function addButtons(data) {
    data.html.push({
        'type' : 'submit',
        'value' : 'submit',
        "id" : "submit"
    });

    data.html.push({
        'type' : 'reset',
        'value' : 'reset',
        "id" : "reset"
    });

    return data;
}

function metadatasuccess(data) {
    //clear prev data, if any
    clearSteps();

    classname = data.className;
    smart.flowName = data.flowName;
    var result = filterAndProcess(data);
    var finaldata = addButtons(result);
    addandUpdateform(finaldata);

    //load new steps for selected flow
    loadSteps();
}

function typeCast(obj, orgObj) {
    for (var i in orgObj) {
        if (!(orgObj[i].attributeName.startsWith("H"))) {
            if (orgObj[i].attributeType == "java.lang.Integer" || "int" == orgObj[i].attributeType || "long" == orgObj[i].attributeType) {
                obj.data[orgObj[i].attributeName] = parseInt(obj.data[orgObj[i].attributeName]);
            }
        }
    }
    return obj;
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

function success(data) {
    console.log("Success!!!! :" + JSON.stringify(data));
}

function fail(err) {
    console.log("Error!!! :" + JSON.stringify(err));
}
