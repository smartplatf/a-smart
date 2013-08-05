var emailGlobal, tenantGenerated, random;

function handleRegistration(email) {
    //check if the entered email exists
    emailGlobal = email;
    var firstpart = email.substring(0, email.lastIndexOf("@"))
    tenantGenerated = firstpart.substr(firstpart, 10) + email.substring((email.lastIndexOf("@") + 1), email.indexOf(".", email.lastIndexOf("@")));
    checkRegistration(email);
}

function checkRegistration(email) {
    //raise event on registration flow to check if tenant already exists
    smart.checkExistence({
        'flow' : 'Registration',
        'group' : 'RegisterRecord',
        'key' : email
    }, function(response) {
        //check if usesr is already registered
        if (response.exists) {
            $("#tenant").val(tenantGenerated);
            $("#tenantinfo").text(tenantGenerated);
            //if exists, check for status
            checkStatus(email);
        }
        //if user is not registered, register and send email
        else {
            $("#loading").show();
            registerUser(email);
        }
    });

    //EOE
}

function registerUser(email) {
    console.log("registering user");
    smart.register({
        'flow' : 'Registration',
        'tenantId' : tenantGenerated,
        'email' : email
    }, function(data) {
        if (!data.success) {
            random = data.random;
            sendEmail("new");
        } else {
            alert("User not registered");
        }
    });
}

function checkStatus(email) {
    smart.statusCheck({
        'flow' : 'Registration',
        'key' : email
    }, function(data) {
        //if user is registered, check for activation
        if (data.status) {
            //$('#wizard').smartWizard('goForward');
            $("#notverified").modal('hide');
            $(".actionBar").show();
        }
        // else give option to send email again
        else {
            $("#notverified").modal('show');
        }
    });
}

function sendEmail(user) {
    $("#go").removeAttr('disabled');
    smart.mail({
        'flow' : 'Registration',
        'key' : emailGlobal
    }, function(response) {
        if (response.status == false) {
            $("#loading").hide();
            if (user == "new")
                $("#firstusermodal").modal('show');
        } else {
            alert("cannot send email");
        }

    });

}