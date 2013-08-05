var mainContent, selectedFqcn, deployPath, flowvalid = false;
var handsonredirect = "http://localhost/smartwizard/localcopy/handsonsmart.html";

var toSmart = {
    'flowName' : '',
    'className' : '',
    'packName' : ''//nothing but the tenant name
};

var finalObject = [];

function initialiseSmart() {
    smart.tenant = "bt";
    smart.flowName = "Template";
    smart.submitSuccess = "success";
    smart.submitFailure = "fail";
}

function success(data) {
    //alert(JSON.stringify(data));
}

function fail(err) {
    if (err.exists != false) {
        console.log("Cannot Process your Request. Sorry for the inconvenience. CAUSE : " + JSON.stringify(err));
    }
    if (err.context.startsWith("No deployment found for flow:")) {
        $("#flowduplicateerror").empty().text("Available....");
        $("#flowduplicateerror").removeClass('error-message').addClass('success');
        $('#wizard').smartWizard('setError', {
            stepnum : 3,
            iserror : false
        });
        $("#error").empty();
        flowvalid = true;
    }
    console.log("!!!!Error: " + JSON.stringify(err));
}

function templategetter(data) {
    //$("#loadingtemplates").modal("show");
    mainContent = data;
    populateDD("#template", mainContent);
}

function templatefetch(data) {
    filterAndProcess(data)
}


$(document).ready(function() {

    //load wizard
    $('#wizard').smartWizard({
        // Properties
        selected : 0, // Selected Step, 0 = first step
        keyNavigation : false, // Enable/Disable key navigation(left and right keys are used if enabled)
        enableAllSteps : false, // Enable/Disable all steps on first load
        transitionEffect : 'slideleft', // Effect on navigation, none/fade/slide/slideleft
        contentURL : null, // specifying content url enables ajax content loading
        contentURLData : null, // override ajax query parameters
        contentCache : true, // cache step contents, if false content is fetched always from ajax url
        cycleSteps : false, // cycle step navigation
        enableFinishButton : false, // makes finish button enabled always
        hideButtonsOnDisabled : true, // when the previous/next/finish buttons are disabled, hide them instead
        errorSteps : [], // array of step numbers to highlighting as error steps
        labelNext : 'Next', // label for Next button
        labelPrevious : 'Previous', // label for Previous button
        labelFinish : 'Finish', // label for Finish button
        noForwardJumping : true,
        //ajaxType : 'POST',
        // Events
        onLeaveStep : validate, // triggers when leaving a step
        onShowStep : null, // triggers when showing a step
        onFinish : finalValidate // triggers when Finish button is clicked
    });

    //hideaction bar fro step1
    $(".actionBar").hide();

    //set smart parameters
    initialiseSmart();

    //get templates from smart.
    smart.getTemplates({
        'flow' : 'Template'
    }, "templategetter");

    //display description when a template gets selected
    $("#template").on("change", function() {
        var selected = $("#template").find(":selected").attr('id');
        if (0 != selected) {
            $("#description").empty().text(mainContent.templateList[selected - 1].description);
            selectedFqcn = mainContent.templateList[selected - 1].fqcn;
            $("#error").empty().hide();
            $("#step5progress").empty();
        } else {
            selectedFqcn = undefined;
            $("#description").empty();
        }
    });

    //get meta data of selected template
    $("#post").click(function() {
        if (selectedFqcn != undefined) {
            var selected_template = {
                "flow" : "Template",
                "fqcn" : selectedFqcn
            };
            smart.getTemplateMeta(selected_template, "templatefetch");

        } else {
            $("#error").empty().append("Please select any 1 template").show();
        }
    });

    //add the new field to the user list
    $("#save").click(function() {
        if (($("#fieldname").val().trim() != "") && ($("#fieldtype").find(":selected").attr("id") != 0)) {
            var name = $("#fieldname").val();
            var type = $("#fieldtype").val();
            var frame = {
                "attributeName" : name,
                "attributeType" : type,
                "isKey" : false
            };
            finalObject.push(frame);
            //show saves message
            $("#success").empty().append().text("Saved Successfully").show().fadeOut(2000);
            //remove errors if any
            $("#error").empty().hide();
            //reset form for next input
            $("#meta").each(function() {
                this.reset();
            });

            showProgress(finalObject);
        }
        enableDeleteContent();
    });

    //deploy the flow
    $("#deploybutton").click(function() {
        $("#flowcreated").modal('hide');
        //populate deployprogress modal when deploy button is clicked
        $("#finalheader").html("<h3>Processing...</h3>")
        $("#deployprogressbody").html("<img src='images/working.gif' width='50px;' class ='span1'><h4 id='deployprogresscontent' class='offset1'>Please wait while SMART is deploying your flow..... </h4>");
        $("#deployprogress").modal('show');

        var obj = {
            'deployJar' : "/" + deployPath + toSmart.flowName + '.jar',
            'flowsoa' : toSmart.flowName + '.soa'
        };
        smart.deploy(obj, "deploySuccess");
    });

    // $("#okbutton").click(function() {
    // //activate proceed button
    // $("#proceed").removeAttr('disabled');
    // });

    //validate if flowname is unique
    $("#flow").focusin(function() {
        $("#flow").change(function() {
            $("#flow").focusout(function() {
                //check if flow name exists
                smart.checkExistence({
                    'flow' : $(this).val(),
                    'group' : 'FlowAdmin',
                    'key' : $(this).val()
                }, function(response) {
                    if (response.exists) {
                        $("#flowduplicateerror").empty().text("NAME already Exists!!!");
                        flowvalid = false;
                        $('#wizard').smartWizard('setError', {
                            stepnum : 3,
                            iserror : true
                        });
                    }
                });
            });

        });
    });

    //redirect to use page
    $("#proceed,#proceeed").click(function() {
        var redirect = handsonredirect + "?t=" + tenantGenerated;
        window.location.href = redirect;
    });

    //register user email go button
    $("#go").click(function() {
        var email = $("#email").val().trim();
        if (is_email(email)) {
            $("#error").empty().hide();
            $('#wizard').smartWizard('setError', {
                stepnum : 1,
                iserror : false
            });
            $(this).attr('disabled', true);
            handleRegistration(email);
            return false;
        } else {
            $("#error").empty().append().text("Valid E-mail required").show();
            $('#wizard').smartWizard('setError', {
                stepnum : 1,
                iserror : true
            });
            return false;
        }
    });

    //send email again button click handler
    $("#sendagain").click(function() {
        sendEmail("old");
    });

    //step 2 choice button events
    $("#choosefromflow").click(function() {
        window.location.href = handsonredirect + "?t=" + tenantGenerated;
    });

    $("#newflow").click(function() {
        $('#wizard').smartWizard('goForward');
    });
    $('#wizard').smartWizard('showMessage', 'Welcome to SMART community');

});

//populate template dropdown
function populateDD(DD, content) {
    content = content.templateList;
    var list = $(DD);
    for (var i = 0; i < content.length; i++) {
        list.append($("<option id=" + (i + 1) + ">" + content[i].name + "</option>"));
    }
}

//show the progress of the user entry
function showProgress(objectList) {
    if (objectList.length != 0) {
        $("#step5progress").empty();
        for (var i in objectList) {
            //show fields enterred so far by the user
            var Name = objectList[i].attributeName.toLowerCase();
            Name = Name.charAt(0).toUpperCase() + Name.slice(1);
            $("#step5progress").append("<div id=\"" + objectList[i].attributeName + "\"><h5>" + Name + "<small> of type </small>" + objectList[i].attributeType + "&nbsp<a class=\"btn btn-mini\"><i class=\"icon-remove-sign\"></i></h5></a><div><br>");
        }
    }
}

// delete event to process the content delete
function enableDeleteContent() {
    $("a.btn,a > i").click(function() {
        var del = $(this).closest('div');
        var what = (del.attr('id'));
        for (var i in finalObject) {
            if (what == finalObject[i].attributeName) {
                finalObject.splice(i, 1);
            }
        }
        del.remove();
    });
}

function filterAndProcess(data) {
    finalObject = [];
    var resultData = data.attributes;
    for ( i = 0; i < resultData.length; i++) {
        if (!(resultData[i].attributeName.startsWith("H"))) {
            finalObject.push(resultData[i]);
            if (resultData[i].attributeType == "java.lang.String") {
                finalObject[i].attributeType = "Character";
            }
            if (resultData[i].attributeType == "java.lang.Integer") {
                finalObject[i].attributeType = "Number";
            }
        }
    }
    showProgress(finalObject);
    enableDeleteContent();
}

//lists all the fields entered by the user in step 4
function showPreview(objectList) {
    $("#step6,#comment").empty();
    //insert table header
    $("#step6").append("<tr><th>#</th><th>Key</th><th>Field</th><th>Type</th></tr>");
    //populate table rows
    if (objectList.length > 0) {
        for (var i in objectList) {
            if (i == 0)
                $("#step6").append("<tr><td>" + (parseInt(i) + 1) + "</td><td id='key'><input type='radio' name='field' id=" + objectList[i].attributeName + " checked></td><td>" + objectList[i].attributeName.toUpperCase() + "</td><td>" + objectList[i].attributeType + "</td></tr>");
            else
                $("#step6").append("<tr><td>" + (parseInt(i) + 1) + "</td><td id='key'><input type='radio' name='field' id=" + objectList[i].attributeName + "></td><td>" + objectList[i].attributeName.toUpperCase() + "</td><td>" + objectList[i].attributeType + "</td></tr>");
        }
        $("#comment").append("<h3>To Edit go to Previous Step</h3>");
    } else {
        $("#comment").append("<h3>Nothing to show</h3>");
    }
}

function validate(obj, context) {
    var currentStep = context.fromStep;
    var nextStep = context.toStep;
    //validation for step 1
    if (nextStep==1 || nextStep == 2) {
        return true;
    } 
    else if (nextStep == 3) {
        return true;
    }
    //validate step 3
    else if (nextStep == 4) {
        var tenant = $("#tenant").val().trim();
        var flow = $("#flow").val().trim();
        if ((tenant == "" || flow == "" || flowvalid == false)) {
            $("#error").empty().append().text("Please give a valid Name").show();
            $('#wizard').smartWizard('setError', {
                stepnum : currentStep,
                iserror : true
            });
            return false;
        } else {
            $("#error").empty().hide();
            $('#wizard').smartWizard('setError', {
                stepnum : currentStep,
                iserror : false
            });
            toSmart.flowName = flow;
            toSmart.packName = tenant;
            return true;
        }
    }
    // validation for step 4
    else if (nextStep == 5) {
        if ($("#objname").val().trim() == "") {
            $("#error").empty().append().text("Name cannot be empty!!").show();
            $('#wizard').smartWizard('setError', {
                stepnum : currentStep,
                iserror : true
            });
            return false;
        } else {
            toSmart.className = $("#objname").val().trim();
            $("#error").empty().hide();
            $('#wizard').smartWizard('setError', {
                stepnum : currentStep,
                iserror : false
            });
            //show user entered object name in step 3
            $("#object").text($("#objname").val() + " ");
            return true;
        }

    }
    //validation for step 5
    else if (currentStep == 5) {
        if (dataCheck()) {
            showPreview(finalObject);
            return true;
        } else {
            return false;
        }
    } else if (currentStep == 6) {
        return true;
    }
}

//check if the object data is empty
function dataCheck() {
    if (!(finalObject.length > 0)) {
        $("#error").empty().append().text("Object cannot be empty!!").show();
        $('#wizard').smartWizard('setError', {
            stepnum : 3,
            iserror : true
        });
        return false;
    } else {
        $("#error").empty().hide();
        $('#wizard').smartWizard('setError', {
            stepnum : 3,
            iserror : false
        });
        return true;
    }
}

//validation on click of finish
function finalValidate(obj, context) {
    //validate and update key information
    if (!fillKeyInfo()) {
        $("#error").empty().append().text("Please select a Key!!").show();
        $('#wizard').smartWizard('setError', {
            stepnum : 4,
            iserror : true
        });
        return false;
    } else {
        $("#error").empty().hide();
        $('#wizard').smartWizard('setError', {
            stepnum : 4,
            iserror : false
        });
    }

    //if content available
    if (dataCheck()) {
        //itterate through the attributes and replace its type
        for (var i in finalObject) {
            switch(finalObject[i].attributeType) {
                case 'Character':
                case 'AlphaNumeric':
                    finalObject[i].attributeType = "java.lang.String";
                    break;
                case 'Number':
                    finalObject[i].attributeType = "java.lang.Integer";
                    break;
            }
        }

        var datastring = {
            'flowName' : toSmart.flowName,
            'className' : toSmart.className,
            'tenantID' : toSmart.packName,
            'attrDefinitions' : finalObject
        };

        $("#loading").show();
        //raise Define prime event
        smart.definePrime(datastring, "flowCreated");
    }
}

//add key field to the data
function fillKeyInfo() {
    var key = $('input[name=field]:radio:checked').attr('id');
    if (key == undefined) {
        return false;
    } else {
        for (var i in finalObject) {
            if (finalObject[i].attributeName == key) {
                finalObject[i].isKey = true;
            } else {
                finalObject[i].isKey = false;
            }
        }
    }
    return true;
}

function is_email(email) {
    var emailReg = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailReg.test(email);
}

String.prototype.startsWith = function(string) {
    if (this.indexOf(string) == 0)
        return true;
    return false;
}
//show flow creation success modal. define prime callback
function flowCreated(data) {
    $("#loading").hide();
    $("#flowcreated").modal('show');
    if (data.message = "success") {
        $("#flowcreatedbody").html("<p> Your flow was Successfully Created. Would you like to Deploy it NOW? <br> <b>Note:Deploying a flow will activate it for you.</b></p>");
        deployPath = data.rootPath;
    } else {
        $("#flowcreatedbody").html("<p>!!!Oops.. Flow Creation Failed.</p>");
        console.log(JSON.stringify(data));
        $("#flowfooter").hide();
    }
}

//create tenant and enable flow on deploy success callback
function deploySuccess(data) {
    if (data.message = "success") {
        $("#deployprogresscontent").text("Deploy Success!!. Enabling Flow for you....");
    } else {
        $("#deployprogressbody").html("<p>!!!Oops... Deploy Failed.</p>");
        console.log(JSON.stringify(data));
        return false;
    }

    smart.checkExistence({
        'flow' : 'AdminSmartFlow',
        'group' : 'TenantAdmin',
        'key' : toSmart.packName,
        'type' : 'tenantCheck',
    }, function(response) {
        //check if tenant already exists, yes- use enable flow
        if (response.exists) {
            oldTenant();
        }
        //if tenant does not exists, use create tenant
        else {
            freshTenant();
        }
    });
}

//if the tenant is new, use create tenant event
function freshTenant() {
    var obj = {
        'tenant' : toSmart.packName,
        'enableFlow' : toSmart.flowName,
        'enableFeatures' : ['all']
    };
    smart.createTenant(obj, "tenantSuccess");
}

//if tenant already exists, use enable flow event
function oldTenant() {
    var obj = {
        'tenant' : toSmart.packName,
        'enableFlow' : toSmart.flowName,
        'enableFeatures' : ['all'],
        'links' : []
    };
    smart.enableFlow(obj, "tenantSuccess");
}

//tenant creation success callback
function tenantSuccess(data) {
    if (data.message = "success") {
        $("#finalheader").html("<h3>Congratulations!!!!</h3>")
        $("#deployprogressbody").html("<h4 class=\"offset1\">Successfully Completed. Click on 'Proceed' button to test your creation.</h4>");
        $('#okbutton,#proceeed,#proceed').removeAttr('disabled');
    } else {
        $("#deployprogressbody").html("<p>!!!Oops.. Tenant Creation Failed.</p>");
        console.log(JSON.stringify(data));
    }
}
