/** SMART - State Machine ARchiTecture
 /**
 * Copyright (C) 2012 Individual contributors as indicated by
 * the @authors tag
 *
 * This file is a part of SMART.
 *
 * SMART is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SMART is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *
 * ************************************************************
 * HEADERS
 * ************************************************************
 * File:                JS client Library
 * Author:              arjun
 * Revision:            1.0
 * Date:                July 1, 2013
 *
 * ************************************************************
 * REVISIONS
 * ************************************************************
 * library which implements all the current events except security events
 *
 * ************************************************************
 * **/

/****************************************** CONSTANTS *************************************************/
var FUNCTION_NOT_FOUND_EXCEPTION = "FUNCTION_NOT_DEFINED_EXCEPTION: success or/and failure callback function was (were) not defined";
var SMART_FIELDS_MISSING_EXCEPTION = "SMART_FIELDS_MISSING_EXCEPTION: tenant, server, protocol, port fields are mandatory to proceed";
var MULTIPART_FORM_NOT_SUPPORTED = "Your Browser incompatible to upload forms.Try using latest browser."

var URL_SEPARATOR = "/";
var PORT_SEPARATOR = "/";
var PROTOCOL_SEPARATOR = "://";

var SMART_ACTION_LOOKUP = 'lookup';
var FLOW_ADMIN = 'FlowAdmin';
var TENANT_ADMIN = 'TenantAdmin';
var SMART_OWNER = 'SmartOwner';
var ADMIN_SMART_FLOW = 'AdminSmartFlow';

var SEARCH_EVENT = 'SearchEvent';
var LOOKUP_EVENT = 'LookupEvent';
var LISTALL_EVENT = 'ListAllEvent';
var DEPLOY_EVENT = 'DeployEvent';
var NEW_TENANT_EVENT = 'NewTenant';
var CREATE_PRIME_EVENT = 'CreatePrime';
var UPDATE_PRIME_EVENT = 'UpdatePrime';
var CONFIG_EVENT = 'Configure';
var UPLOAD_EVENT = 'UploadEvent';
var LIST_DEPLOYMENTS_EVENT = 'ListDeployments';
var ENABLE_FLOW_EVENT = 'EnableFlow';
var CREATE_USER_EVENT = 'CreateUser';
var ADD_IDENTITY_EVENT = 'AddIdentity';
var AUTHENTICATE_EVENT = 'Authenticate';
var GET_PRIME_META_EVENT = 'GetPrimeMeta';
var GET_TEMPLATE_META_EVENT = 'GetTemplateMeta';
var GET_TEMPLATES_EVENT = 'GetTemplates';
var DEFINE_PRIME_EVENT = 'DefinePrime';
var GET_JAVASCRIPT_EVENT = 'GetJavaScript';
var UPLOAD_EVENT = 'UploadEvent';
var CHECK_EXISTENCE_EVENT = "CheckExistence";
var REGISTER_EVENT = "RegisterEvent";
var MAILER_EVENT = "MailerEvent";
var ACTIVATE_USER_EVENT = "ActivateUser";
var STATUS_CHECK_EVENT = "CheckStatusEvent"
var LIST_ENABLED_FLOWS_EVENT = "ListEnabledFlows";

var REGISTRATION_FLOW = "Registration";

var FORM_EVENT_ATTRIBUTES = ['lookup', 'search', 'list', 'deploy', 'createTenant', 'enableFlow', 'createPrime', 'updatePrime', 'definePrime', 'upload', 'getTemplates', 'getTemplateMeta', 'getPrimeMeta', 'getJavaScript', 'checkExistence', 'listDeployments', 'register', 'activateUser', 'mail', 'statusCheck', 'listEnabledFlows'];

/******************************************* SMART OBJECT ********************************************/
var tempCallBack = "";

var smart = {
    'protocol' : 'http',
    'server' : 'demo.smart-platform.com',
    'smartPort' : 'api',
    'uploadPort' : 'dapi',
    'tenant' : '',
    'flowName' : '',
    'formEventName' : '',
    'submitSuccess' : '',
    'submitFailure' : '',
    'targetUrl' : '',
    'dataSubmit' : {},
    'dataResponse' : {},
    'errors' : {},
    'exception' : '',

    'connect' : function() {
        smartConnect();
    },
    //callBack parameter is optional.
    'lookup' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartLookUp(obj);
    },

    'search' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartSearch(obj);
    },

    'list' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartList(obj);
    },

    'deploy' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartDeploy(obj);
    },

    'createTenant' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartCreateTenant(obj);
    },

    'enableFlow' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartEnableFlow(obj);
    },

    'createPrime' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartCreatePrime(obj);
    },

    'updatePrime' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartUpdatePrime(obj);
    },

    'definePrime' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartDefinePrime(obj);
    },

    'config' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartConfig(obj);
    },

    'upload' : function(FormData, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartUpload(FormData);
    },

    'getTemplates' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartTemplates(obj);
    },

    'getTemplateMeta' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartTemplateMeta(obj);
    },

    'getPrimeMeta' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartPrimeMeta(obj);
    },

    'getJavaScript' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartJSSnippet(obj);
    },

    'checkExistence' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartCheckExistence(obj);
    },

    'listDeployments' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartListDeployments(obj);
    },

    'register' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartRegisteration(obj);
    },

    'activateUser' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartUserActivate(obj);
    },

    'mail' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartMailUser(obj);
    },

    //check user activation status
    'statusCheck' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartRegistrationStatusCheck(obj);
    },

    //list all flows enabled for a user
    'listEnabledFlows' : function(obj, callBack) {
        checkPrerequisits();
        tempCallBack = callBack;
        smartlistEnabledFlows(obj);
    },

    // form upload to smart
    'form' : function(formId, e) {
        e.preventDefault();
        checkPrerequisits();
        // make sure that the form has an event attribute. Else throw an error
        var formEvent = $("#" + formId).attr('event').trim();
        var formEventAvailable = CheckFormEvent(formEvent);
        if (formEventAvailable) {
            smart.formEventName = formEvent;
            handleForm(formId);
        } else {
            var formErrObj = ["Form Event attribute '" + formEvent + "' not valid.Please add a valid 'event' attribute to the Form. It can be one of " + FORM_EVENT_ATTRIBUTES.toString()];
            window[smart.submitFailure](formErrObj);
            return false;
        }
    }
};

/********************************************* VALIDATIONS *************************************************/
var allSetFlag = false;

function checkPrerequisits() {
    //check if the success function and failure function are defined
    if (!validateUserFunctionNames()) {
        allSetFlag = false;
        smart.exception = FUNCTION_NOT_FOUND_EXCEPTION;
        throw FUNCTION_NOT_FOUND_EXCEPTION;
    }
    if (!validateSmartAttributes()) {
        allSetFlag = false;
        smart.exception += SMART_FIELDS_MISSING_EXCEPTION;
        throw SMART_FIELDS_MISSING_EXCEPTION;
    } else {
        allSetFlag = true;
        smart.errors = {};
    }
}

//check if the  form has a valid event attribute
function CheckFormEvent(formevent) {
    for (var each in FORM_EVENT_ATTRIBUTES) {
        if (formevent == FORM_EVENT_ATTRIBUTES[each]) {
            return true;
        }
    }
    return false;
}

//validate if the functions given by the user actually exists. else throw an error.
function validateUserFunctionNames() {
    console.log("validating callback functions");
    var functionCheckFlag = false;
    // function presence flag
    // Since its name is being dynamically generated, always ensure your function actually exists
    if ( typeof eval(smart.submitSuccess) === "function" && typeof window[smart.submitFailure] === "function") {
        functionCheckFlag = true;
    } else {
        functionCheckFlag = false;
    }
    return functionCheckFlag;
}

//validate minimum mandatory smart fields.
function validateSmartAttributes() {
    console.log("validating smart fields");
    var smartCheckFlag = false;

    if (smart.tenant == "" || smart.protocol == "" || smart.server == "" || smart.smartPort == "") {
        smartCheckFlag = false;
    } else {
        smartCheckFlag = true;
    }
    return smartCheckFlag;
}

//Mandatory event fields to validate
var eventObjectFields = {
    LookupEvent : ["group", "key"],
    SearchEvent : ["group", "queryMap"],
    ListAllEvent : ["group", "size"],
    DeployEvent : ["deployJar", "flowsoa"],
    NewTenant : ["tenant", "enableFeatures"],
    EnableFlow : ["tenant", "enableFlow", "enableFeatures", "links"],
    CreatePrime : ["create", "data"],
    UpdatePrime : ["updatekey", "update", "data"],
    DefinePrime : ["flowName", "className", "tenantID", "attrDefinitions"],
    Config : ["configName", "configFor", "configValues"],
    GetTemplates : [],
    GetTemplateMeta : ["fqcn"],
    GetPrimeMeta : ["flowClass"],
    GetJavaScript : ["flowName", "tenantID"],
    ListDeployments : ["dType", "flow"],
    CheckExistence : ["group", "key"],
    RegisterEvent : ['email', 'tenantId'],
    ActivateEvent : [],
    MailerEvent : [],
    CheckStatusEvent : [],
    ListEnabledFlows : []
};

//mandatory connect fields to validate
var connectionObject = ["protocol", "tenant", "flowName", "submitSuccess", "submitFailure"];

//validate fields before object construction, for the event raised
function validation(receivedObj, forEvent) {
    console.log("validating for --->" + JSON.stringify(receivedObj) + "  " + forEvent);

    var mandatoryFields = eventObjectFields[forEvent];
    var errorObj = ["TO Raise " + forEvent + " :"];

    //connection object field validation
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
        window[smart.submitFailure](errorObj);
        return false;
    }
}

/****************************************** IMPLEMENTATIONS ****************************************/

function smartConnect() {
    url = smart.targetUrl;
    submitData = JSON.stringify(smart.dataSubmit);
    console.log(submitData);
    console.log(url);
    submitToSmart(url, submitData, tempCallBack);
    tempCallBack = "";
}

function checkAllSetFlag() {
    if (!allSetFlag) {
        throw smart.exception;
    }
}

//prepares the JSON data object. Takes eventname and user object as params
function prepareDataObject(userDt, forEvent, flow, key) {
    var admin;
    if (forEvent == undefined) {
        throw "event is not supplied to build the object";
    }

    //decide and assign the admin for the event
    switch(forEvent) {
        case DEPLOY_EVENT:
        case NEW_TENANT_EVENT:
        case DEFINE_PRIME_EVENT:
        case ENABLE_FLOW_EVENT:
        case LIST_ENABLED_FLOWS_EVENT:
            admin = TENANT_ADMIN;
            break;
        case LOOKUP_EVENT:
        case SEARCH_EVENT:
        case LISTALL_EVENT:
        case CREATE_PRIME_EVENT:
        case CONFIG_EVENT:
        case UPLOAD_EVENT:
        case GET_TEMPLATES_EVENT:
        case GET_TEMPLATE_META_EVENT:
        case GET_PRIME_META_EVENT:
        case GET_JAVASCRIPT_EVENT:
        case CHECK_EXISTENCE_EVENT:
        case LIST_DEPLOYMENTS_EVENT:
        case REGISTER_EVENT:
            admin = FLOW_ADMIN;
            break;
        case UPDATE_PRIME_EVENT:
            admin = userDt.update;
            break;
        case MAILER_EVENT:
        case ACTIVATE_USER_EVENT:
        case STATUS_CHECK_EVENT:
            admin = "RegisterRecord";
            break;
    }

    smart.dataSubmit = {};
    smart.dataSubmit[admin] = {};
    smart.dataSubmit[admin].___smart_action___ = SMART_ACTION_LOOKUP;
    smart.dataSubmit[admin].___smart_value___ = (flow != undefined) ? flow : smart.flowName;

    switch(forEvent) {
        case DEPLOY_EVENT:
        case NEW_TENANT_EVENT:
        case DEFINE_PRIME_EVENT:
        case ENABLE_FLOW_EVENT:
            smart.dataSubmit[admin].___smart_value___ = SMART_OWNER;
            break;
        case LIST_DEPLOYMENTS_EVENT:
            smart.dataSubmit[admin].___smart_value___ = ADMIN_SMART_FLOW;
            break;
        case UPDATE_PRIME_EVENT:
            //tailered according to ui. need  to change
            smart.dataSubmit[admin].___smart_value___ = userDt.updatekey;
            //remove key info from user obj as it does not appear in the final submit
            delete userDt["updatekey"];
            delete userDt.data[updatekey];
            break;
        case MAILER_EVENT:
        case ACTIVATE_USER_EVENT:
        case STATUS_CHECK_EVENT:
            smart.dataSubmit[admin].___smart_value___ = key;
            break;
        case LIST_ENABLED_FLOWS_EVENT:
            smart.dataSubmit[admin].___smart_value___ = smart.tenant;
            break;
    }

    //loop over the user object data to extract and set event object
    for (var i = 0; i < eventObjectFields[forEvent].length; i++) {
        smart.dataSubmit[eventObjectFields[forEvent][i]] = userDt[eventObjectFields[forEvent][i]];
    }

    //if the create tenat has flow to enable
    if (forEvent == NEW_TENANT_EVENT && userDt["enableFlow"]) {
        smart.dataSubmit.enableFlow = userDt.enableFlow;
    }

    return smart.dataSubmit;
}

//builds url for the event. takes eventname and flowname as params
function buildUrl(evnt, flow, type) {
    var tenant = smart.tenant;
    var flownme;

    //if flow is missing in the user object, take it from global space
    if (flow == undefined) {
        flownme = smart.flowName;
    } else {
        //if user provides the flow name as a part of the object
        flownme = flow;
    }

    //assigns tenant and flow for deploy and new tenant event
    switch(evnt) {
        case DEPLOY_EVENT:
        case NEW_TENANT_EVENT:
        case DEFINE_PRIME_EVENT:
        case ENABLE_FLOW_EVENT:
        case LIST_ENABLED_FLOWS_EVENT:
            tenant = SMART_OWNER;
            flownme = ADMIN_SMART_FLOW;
            break;
        case LIST_DEPLOYMENTS_EVENT:
            flownme = ADMIN_SMART_FLOW;
            break;
        case CHECK_EXISTENCE_EVENT:
            if (type == "tenantCheck") {
                tenant = SMART_OWNER;
                flownme = ADMIN_SMART_FLOW;
            }
            break;
    }
    //build Url
    if (evnt == "UploadEvent") {
        smart.targetUrl = smart.protocol + PROTOCOL_SEPARATOR + smart.server + PORT_SEPARATOR + smart.uploadPort + URL_SEPARATOR + tenant + URL_SEPARATOR + flownme + URL_SEPARATOR + evnt;
    } else {
        smart.targetUrl = smart.protocol + PROTOCOL_SEPARATOR + smart.server + PORT_SEPARATOR + smart.smartPort + URL_SEPARATOR + tenant + URL_SEPARATOR + flownme + URL_SEPARATOR + evnt;
    }
    return smart.targetUrl;
}

/************************************************ EVENT SIGNATURES ***************************************/

//Lookup Event
function smartLookUp(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, LOOKUP_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(LOOKUP_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, LOOKUP_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for search event
function smartSearch(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, SEARCH_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(SEARCH_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, SEARCH_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for list all event
function smartList(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, LISTALL_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(LISTALL_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, LISTALL_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for deploy event
function smartDeploy(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, DEPLOY_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(DEPLOY_EVENT);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, DEPLOY_EVENT);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for create tenant event
function smartCreateTenant(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, NEW_TENANT_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(NEW_TENANT_EVENT);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, NEW_TENANT_EVENT);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for enable flow event
function smartEnableFlow(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, ENABLE_FLOW_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(ENABLE_FLOW_EVENT);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, ENABLE_FLOW_EVENT);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for create prime event
function smartCreatePrime(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, CREATE_PRIME_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(CREATE_PRIME_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, CREATE_PRIME_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//for update prime event
function smartUpdatePrime(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, UPDATE_PRIME_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(UPDATE_PRIME_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, UPDATE_PRIME_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//for define prime event
function smartDefinePrime(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, DEFINE_PRIME_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(DEFINE_PRIME_EVENT);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, DEFINE_PRIME_EVENT);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//for get templates event
function smartTemplates(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;
    if (eventUserData && eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;

    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, GET_TEMPLATES_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(GET_TEMPLATES_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, GET_TEMPLATES_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for getting the meta data of the template
function smartTemplateMeta(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, GET_TEMPLATE_META_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(GET_TEMPLATE_META_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, GET_TEMPLATE_META_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//for getting meta data of the prime object in the deployed flow
// for search event
function smartPrimeMeta(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, GET_PRIME_META_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(GET_PRIME_META_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, GET_PRIME_META_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//for getting the javascript snippet
function smartJSSnippet(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, GET_JAVASCRIPT_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(GET_JAVASCRIPT_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, GET_JAVASCRIPT_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for config event
function smartConfig(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }
    // validate user object before event post
    goodToGoFLag = validation(eventUserData, UPDATE_PRIME_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(UPDATE_PRIME_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, UPDATE_PRIME_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//Check Existence Event
function smartCheckExistence(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
        var check = eventUserData.type;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, CHECK_EXISTENCE_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(CHECK_EXISTENCE_EVENT, flow, check);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, CHECK_EXISTENCE_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

function smartListDeployments(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, LIST_DEPLOYMENTS_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(LIST_DEPLOYMENTS_EVENT);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, LIST_DEPLOYMENTS_EVENT);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//register user
function smartRegisteration(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, REGISTER_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(REGISTER_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, REGISTER_EVENT, flow);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//mail user
function smartMailUser(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
        var key = eventUserData.key;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, MAILER_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(MAILER_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, MAILER_EVENT, flow, key);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//activate user
function smartUserActivate(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
        var key = eventUserData.key;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, ACTIVATE_USER_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(ACTIVATE_USER_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, ACTIVATE_USER_EVENT, flow, key);

        //connect with smart server and raise the query
        smart.connect();
    }
}

//user registration status check
function smartRegistrationStatusCheck(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    if (eventUserData.flow) {
        // set flow name if supplied by client
        var flow = eventUserData.flow;
        var key = eventUserData.key;
    }

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, STATUS_CHECK_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(STATUS_CHECK_EVENT, flow);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, STATUS_CHECK_EVENT, flow, key);

        //connect with smart server and raise the query
        smart.connect();
    }
}

// for list enabled flows event
function smartlistEnabledFlows(eventUserData) {
    checkAllSetFlag();
    var goodToGoFLag = false;

    // validate user object before event post
    goodToGoFLag = validation(eventUserData, LIST_ENABLED_FLOWS_EVENT);

    if (goodToGoFLag) {
        //construct target url
        buildUrl(LIST_ENABLED_FLOWS_EVENT);

        //prepare the JSOn for the event
        prepareDataObject(eventUserData, LIST_ENABLED_FLOWS_EVENT);

        //connect with smart server and raise the query
        smart.connect();
    }
}

/********************************************** UTILITIES*************************************************/

// submit details
function submitToSmart(posturl, submitData, callback) {

    $.ajax({
        url : posturl,
        type : "post",
        data : submitData,
        dataType : 'json',

        success : function(data) {
            //check if callback function was provided along with the call.Else call the main callback functions

            if (callback == "" || callback == undefined) {
                if (data['responses'] != undefined)
                    window[smart.submitSuccess](data.responses[0]);
                else
                    window[smart.submitFailure](data.errors[0]);
            } else {
                if (data['responses'] != undefined) {
                    window[callback] ? window[callback](data.responses[0]) : callback(data.responses[0]);
                } else {
                    window[smart.submitFailure](data.errors[0]);
                }
            }
        },
        error : function(err) {
            window[smart.submitFailure](err);
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

// form to JSON conversion
function handleForm(formId) {
    if (smart.formEventName == "upload") {
        smart.upload(formId);
    } else {
        jsonResult = {};
        jsonResult = $("#" + formId).serializeObject();
        smart[smart.formEventName](jsonResult);
    }
}

//upload file event
function smartUpload(formData) {

    var multiPartFormAvailable = false;
    if (window.FormData) {
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
            if (this.value >= 0) {
                value = parseInt(this.value, 10);
            } else {
                value = this.value;
            }
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

String.prototype.startsWith = function(string) {
    if (this.indexOf(string) == 0)
        return true;
    return false;
}

String.prototype.toProperCase = function() {
    return this.toLowerCase().replace(/^(.)|\s(.)/g, function($1) {
        return $1.toUpperCase();
    });
}
