/*******************************************************************************
 * SMART - State Machine ARchiTecture /** Copyright (C) 2012 Individual
 * contributors as indicated by the
 *
 * @authors tag
 *
 * This file is a part of SMART.
 *
 * SMART is a free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SMART is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/
 *
 *
 *
 *
 *
 * ************************************************************ HEADERS
 * ************************************************************ File:
 * org.anon.smart.d2cache.AbstractD2Cache Author: arjun Revision: 1.0 Date: May
 * 15, 2013
 *
 * ************************************************************ REVISIONS
 * ************************************************************ Purpose
 *
 * ************************************************************
 ******************************************************************************/

var slash = "/";

// SMART object to build the target url
smart = {
    'protocol' : 'http',
    'server' : '',
    'portNo' : 9081,
    'tenant' : '',
    'flowName' : '',
    'eventName' : '',
    'successFunction' : '',
    'failFunction' : '',
    'targetUrl' : '',
    'dataSubmit' : {},
    'responseObj' : {},

    'connect' : function() {
        smartConnect();
    },

    'lookup' : function(obj) {
        smartLookUp(obj);
    },

    'search' : function(obj) {
        smartSearch(obj);
    },

    'list' : function(obj) {
        smartList(obj);
    },

    'deploy' : function(obj) {
        smartDeploy(obj);
    },

    'createTenant' : function(obj) {
        smartCreateTenant(obj);
    },

    'createPrime' : function(obj) {
        smartCreatePrime(obj);
    },

    'updatePrime' : function(obj) {
        smartUpdatePrime(obj);
    },

    'config' : function(obj) {
        smartConfig(obj);
    },

    'upload' : function(FormData) {
        smartUpload(FormData);
    },

    // form upload to smart
    'form' : function(formId, e) {
        e.preventDefault();
        // make sure that the form has an event attribute. Else throw an error
        formEvent = $("#" + formId).attr('event');
        if (formEvent != undefined) {
            smart.eventName = formEvent;
            handleForm(formId);
        } else {
            var formErrObj = ["please add a valid \"event\" attribute to the Form"];
            validateUserFunctionNames(smart.failFunction, formErrObj);
        }
    }
};

var eventObjectFields = {
    lookup : ["group", "key"],
    search : ["group", "queryMap"],
    list : ["group", "size"],
    deploy : ["deployJar", "flowsoa"],
    createTenant : ["tenant", "enableFlow", "enableFeatures"],
    createPrime : ["create", "data"],
    updatePrime : ["update", "data"],
    config : ["configName", "configFor", "configValues"]
};

var connectionObject = ["protocol", "tenant", "eventName", "successFunction", "failFunction", "flowName"];

function smartConnect() {
    url = getTargetUrl();
    smart.dataSubmit = JSON.stringify(smart.dataSubmit);
    submitToSmart(url, smart.dataSubmit);
}

function getTargetUrl() {
    smart.targetUrl = smart.protocol + "://" + smart.server + ":" + smart.portNo + slash + smart.tenant + slash + smart.flowName + slash + smart.eventName;
    return smart.targetUrl;
}

// for lookup event
function smartLookUp(eventUserData) {
    var count = 0;
    var goodToGoFLag = false;
    var admin = "FlowAdmin";

    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};
    smart.eventName = "LookupEvent";

    /*
     * if (eventUserData.tenant) { //set tenant id if supplied by client
     * smart.tenant = eventUserData.tenant; }
     */

    if (eventUserData.flow) {
        // set flow name if supplied by client
        smart.flowName = smart.dataSubmit[admin].___smart_value___ = eventUserData.flow;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.lookup);

    if (goodToGoFLag) {
        smart.dataSubmit[admin].___smart_action___ = "lookup";
        smart.dataSubmit[admin].___smart_value___ = smart.flowName;
        // set group
        smart.dataSubmit.group = eventUserData.group;
        // set key
        smart.dataSubmit.key = eventUserData.key;

        smart.connect();
    }
}

// for search event
function smartSearch(eventUserData) {
    var count = 0;
    var goodToGoFLag = false;
    var admin = "FlowAdmin";

    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};
    smart.eventName = "SearchEvent";

    if (eventUserData.flow) {
        // set flow name if supplied by client
        smart.flowName = smart.dataSubmit[admin].___smart_value___ = eventUserData.flow;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.search);

    if (goodToGoFLag) {
        smart.dataSubmit[admin].___smart_action___ = "lookup";
        smart.dataSubmit[admin].___smart_value___ = smart.flowName;
        // set group
        smart.dataSubmit.group = eventUserData.group;
        // set key
        smart.dataSubmit.queryMap = eventUserData.queryMap;

        smart.connect();
    }
}

// for list all event
function smartList(eventUserData) {
    var count = 0;
    var goodToGoFLag = false;
    var admin = "FlowAdmin";

    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};
    smart.eventName = "ListAllEvent";

    if (eventUserData.flow) {
        // set flow name if supplied by client
        smart.flowName = smart.dataSubmit[admin].___smart_value___ = eventUserData.flow;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.list);

    if (goodToGoFLag) {
        smart.dataSubmit[admin].___smart_action___ = "lookup";
        smart.dataSubmit[admin].___smart_value___ = smart.flowName;
        // set object name
        smart.dataSubmit.group = eventUserData.group;
        // set key
        smart.dataSubmit.size = eventUserData.size;

        smart.connect();
    }
}

// for deploy event
function smartDeploy(eventUserData) {
    var admin = 'TenantAdmin';

    smart.tenant = 'SmartOwner';
    smart.eventName = 'DeployEvent';
    smart.flowName = 'AdminSmartFlow';
    smart.dataSubmit = {};

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.deploy);

    if (goodToGoFLag) {
        smart.dataSubmit[admin] = {};
        smart.dataSubmit[admin].___smart_action___ = 'lookup';
        smart.dataSubmit[admin].___smart_value___ = smart.tenant;

        // set object name
        smart.dataSubmit.deployJar = eventUserData.deployJar;
        // set key
        smart.dataSubmit.flowsoa = eventUserData.flowsoa;

        smart.connect();
    }
}

// for create tenant event
function smartCreateTenant(eventUserData) {
    var admin = "TenantAdmin";
    var goodToGoFLag = false;

    smart.tenant = "SmartOwner";
    smart.eventName = "NewTenant";
    smart.flowName = "AdminSmartFlow";
    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.createTenant);

    if (goodToGoFLag) {
        smart.dataSubmit[admin].___smart_action___ = "lookup";
        smart.dataSubmit[admin].___smart_value___ = smart.tenant;
        // set new tenant name
        smart.dataSubmit.tenant = eventUserData.tenant;
        // flows to enable for new tenant
        smart.dataSubmit.enableFlow = eventUserData.enableFlow;
        // features to enable for new tenant
        smart.dataSubmit.enableFeatures = eventUserData.enableFeatures;

        smart.connect();
    }
}

// for create prime event
function smartCreatePrime(eventUserData) {
    var count = 0;
    var goodToGoFLag = false;
    var admin = "FlowAdmin";

    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};
    smart.eventName = "CreatePrime";

    if (eventUserData.flow) {
        // set flow name if supplied by client
        smart.flowName = smart.dataSubmit[admin].___smart_value___ = eventUserData.flow;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.createPrime);

    if (goodToGoFLag) {
        smart.dataSubmit[admin].___smart_action___ = "lookup";
        smart.dataSubmit[admin].___smart_value___ = smart.flowName;
        // set object name
        smart.dataSubmit.create = eventUserData.create;
        // set key
        smart.dataSubmit.data = eventUserData.data;

        smart.connect();
    }
}

function smartUpdatePrime(eventUserData) {
    var count = 0;
    var goodToGoFLag = false;
    var admin = "FlowAdmin";

    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};
    smart.eventName = "UpdatePrime";

    if (eventUserData.flow) {
        // set flow name if supplied by client
        smart.flowName = smart.dataSubmit[admin].___smart_value___ = eventUserData.flow;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.updatePrime);

    if (goodToGoFLag) {
        smart.dataSubmit[admin].___smart_action___ = "lookup";
        smart.dataSubmit[admin].___smart_value___ = smart.flowName;
        // set object name
        smart.dataSubmit.update = eventUserData.update;
        // set key
        smart.dataSubmit.data = eventUserData.data;

        smart.connect();
    }
}

// for config event
function smartConfig(eventUserData) {
    var count = 0;
    var goodToGoFLag = false;
    var admin = "FlowAdmin";

    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};
    smart.eventName = " ";

    if (eventUserData.flow) {
        // set flow name if supplied by client
        smart.flowName = smart.dataSubmit[admin].___smart_value___ = eventUserData.flow;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, eventObjectFields.config);

    if (goodToGoFLag) {
        smart.dataSubmit[admin].___smart_action___ = "lookup";
        smart.dataSubmit[admin].___smart_value___ = smart.flowName;
        // configuration name
        smart.dataSubmit.configName = eventUserData.configName;
        // configure object
        smart.dataSubmit.configFor = eventUserData.configFor;
        // configuration
        smart.dataSubmit.configValues = eventUserData.configValues;

        smart.connect();
    }
}

// submit details
function submitToSmart(posturl, submitData) {

    $.ajax({
        url : posturl,
        type : "post",
        data : submitData,
        dataType : 'json',

        success : function(data) {
            validateUserFunctionNames(smart.successFunction, data);
        },
        error : function(err) {
            validateUserFunctionNames(smart.failFunction, err);
        }
    });
}

// upload multipart form to smart
function uploadToSmart(posturl, submitData) {
    $.ajax({
        url : posturl,
        type : "post",
        data : submitData,
        contentType : 'multipart/form-data',
        processData : false,

        success : function(data) {
            alert("Upload Success  " + data);
        },
        error : function(err) {
            alert("Upload Failed" + err);
        }
    });
}

//validate event fields
function validation(receivedObj, mandatoryFields) {
    var errorObj = ["FOR " + smart.eventName + " :"];
    // //connection object field validation
    for ( i = 0; i < connectionObject.length; i++) {
        if (!smart[connectionObject[i]]) {
            errorObj.push(connectionObject[i] + "--> is mandatory")
        }
    }

    // event object fields validation
    var requiredFields = mandatoryFields.length;
    for ( i = 0; i < requiredFields; i++) {
        if (!receivedObj[mandatoryFields[i]]) {
            errorObj.push(mandatoryFields[i] + "--> is missing");
        }
    }

    if (errorObj.length == 1) {
        errorObj = [];
        return true;
    } else {
        errorObj.push(".!PROBABLE CAUSE: Above information(s) was not provided (or) a wrong Event has been raised.");
        validateUserFunctionNames(smart.failFunction, errorObj);
        return false;
    }
}

//validate if the functions given by the user actually exists. else throw an error.
function validateUserFunctionNames(fname, toBePassed) {
    try {
        var function_name = fname;
        var functionAvailable = false;
        // function presence flag
        // Since its name is being dynamically generated, always ensure your function actually exists
        if ( typeof (window[function_name]) === "function") {
            functionAvailable = true;
        } else {
            throw ("Error.  Function " + function_name + " does not exist.");
        }
    } catch(err) {
        //alert("error : "+ err);

    }
    if (functionAvailable == true) {
        window[function_name](toBePassed);
        functionAvailable = false;
    }
}

// form to JSON conversion
function handleForm(formId) {
    if (smart.Event == "upload") {
        smart.upload(formId);
    } else {
        jsonResult = {};
        jsonResult = $("#" + formId).serializeObject();
        smart[smart.eventName](jsonResult);
    }
}

//upload file event
function smartUpload(formData) {

    var multiPartFormAvailable = false;
    if (window.FormData) {
        // alert("supported");
        multiPartFormAvailable = true;
        var newData = new FormData();
        $.each($('#file').prop("files"), function(i, file) {
            newData.append('file', file);
        });

        if (multiPartFormAvailable) {
            // upload file
            uploadToSmart("multiparttest.php", newData);
        } else {
            // browser not compatible
            console.log("multi part forms not supported by your browser");
            alert("multi part forms not supported by your browser");
        }
    }

}

// convert html form elements to objects
$.fn.serializeObject = function() {
    var arrayData, objectData;
    arrayData = this.serializeArray();
    objectData = {};

    $.each(arrayData, function() {
        var value;

        if (this.value != null) {
            value = this.value;
        } else {
            value = '';
        }

        if (objectData[this.name] != null) {
            if (!objectData[this.name].push) {

                objectData[this.name] = [objectData[this.name]];
            }

            objectData[this.name].push(value);
        } else {
            objectData[this.name] = value;
        }
    });

    return objectData;
}
