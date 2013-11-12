var recognized = {
"ListAllEvent":["group", "size"],
"SearchEvent":["group"],
"LookupEvent":["group", "key"],
"CreatePrime":["group"],
"UpdatePrime":["group"]
};
var primedata = [];

function getStd(evt)
{
    return recognized[evt];
}

function addStatus(msg)
{
    var statdiv = $('#statmsg');
    var p = $("<p/>", {
        'text':msg
    });
    statdiv.prepend(p);
}

function setupconn(fsmart, cb)
{
    smartconfig.server = $('#server').val();
    smartconfig.port = $('#port').val();
    smartconfig.smartPort = $('#port').val();
    smartconfig.uploadPort = 9021;
    smartconfig.tenant = $('#tenant').val();
    smartconfig.userSession = $('#sessionid').val();

    fsmart.sessionId = smartconfig.userSession;
    fsmart.submitSuccess = function(response) {
        console.log(JSON.stringify(response));
        if (response.message != '')
            addStatus( response.message );
        else
            addStatus( JSON.stringify(response) );

        cb(response);
    };

    fsmart.submitFailure = function(error) {
        var msg = error.response.errors[0].context;
        addStatus(msg);
    };

    fsmart.submitStart = function() {
        jQuery('.wdtprogress').css({opacity:"1", top:"115px"});
    };

    fsmart.submitEnd = function() {
        jQuery('.wdtprogress').css({opacity:"0", top:"-100px"});
    };

    fsmart.submitProgress = function(pct) {
        $('#progressbar').attr('value', pct);
    };

    fsmart.networkFailure = function(error) {
        addStatus(error);
    };

    fsmart.flowName = $('#flow').val();

    return fsmart;
}

function addStandardRow(evt)
{
    $('#data').empty();
    var stdflds = getStd(evt);
    if (stdflds != undefined)
    {
        var tbody = $('#data');
        for (var s in stdflds)
        {
            var tr = $("<tr/>");
            var td1 = $("<td/>", {
                'text': stdflds[s],
                'title' : 'This is the prime object which you want to create'
            });
            tr.append(td1);
            var td2 = $("<td/>", { 'text':'Double click to enter Value' });
            tr.append(td2);
            td2.dblclick(edittd);
            tbody.append(tr);
        }

        $('#posttotype').val('');
    }
}

function setEvents(resp)
{
    console.log(resp);
    var ldep = resp.response['deployments'];
    for (var evt in ldep)
    {
        var e = ldep[evt];
        var last = e.split(".");
        var nm = last[last.length - 1];
        var opt = $("<option/>", {
            'text':nm,
            'value':nm
        });
        $('#event').append(opt);
    }
};

function setPrimes(resp)
{
   setPrimesIn(resp, $('#posttotype'));
}

function setPrimesIn(resp, obj)
{
    console.log(resp);
    var f = $("<option/>", {
       'text':'Standard Flow',
       'value':''
    });
    $('#posttotype').append(f);
    var ldep = resp.response['deployments'];
    for (var evt in ldep)
    {
        var e = ldep[evt];
        var last = e.split(".");
        var nm = last[last.length - 1];
        var opt = $("<option/>", {
            'text':nm,
            'value':nm
        });
        obj.append(opt);
    }
}

function populateEvents()
{
    var fsmart = new smartadmin();
    setupconn(fsmart, setEvents);
    if ($('#flow').val() != "")
    {
        fsmart.listDeployments("Event", $('#flow').val());
        fsmart.listDeployments("Event", "AllFlows");
    }
}

function populatePrimes()
{
    var fsmart = new smartadmin();
    setupconn(fsmart, setPrimes);
    if ($('#flow').val() != "")
    {
        fsmart.listDeployments("PrimeData", $('#flow').val());
    }
}

function setFlows(resp)
{
    $('#flow').empty();
    console.log(resp);
    var ldep = resp.response['enabledFlows'];
    for (var evt in ldep)
    {
        var nm = ldep[evt];
        var opt = $("<option/>", {
            'text':nm,
            'value':nm
        });
        $('#flow').append(opt);
    }
    populateEvents();
    populatePrimes();
}

function populateFlows()
{
    var fsmart = new smartadmin();
    setupconn(fsmart, setFlows);
    fsmart.flowName = "AdminSmartFlow";
    fsmart.listEnabledFlows($('#tenant').val());
}

function logintenant()
{
    var fsmart = new security();
    //reset, we are now going to work with tenant
    $('#sessionid').val('');
    setupconn(fsmart, function(resp) {
        var sessid = resp.response.sessId;
        $('#sessionid').val(sessid);
        /*$('#setup').attr('disabled', 'disabled');
        var f = $('#flowname').val();
        var opt = $("<option/>", {
            'text':f,
            'value':f
        });
        $('#flow').append(opt);*/
        //populateEvents();
        //populatePrimes();
        populatevalues();
    });

    var usr = $('#tenant').val() + "admin";
    var pwd = $('#tenant').val() + "admin";
    fsmart.flowName = "Security";
    fsmart.authenticate(usr, pwd);
}

function newtenant()
{
    var fsmart = new smartadmin();
    setupconn(fsmart, logintenant);
    fsmart.sessionId = $('#adminsessid').val();
    fsmart.flowName = "AdminSmartFlow";
    var tnt = $('#tenant').val();
    var flow = '';
    var farray = [];

    fsmart.newTenant(tnt, farray, flow);
}

function check(resp)
{
    console.log(resp);
    if (resp.message == false)
    {
        newtenant();
    }
    else
    {
       logintenant();
    }
}

function checktenant()
{
    var fsmart = new smartadmin();
    setupconn(fsmart, check);
    fsmart.sessionId = $('#adminsessid').val();
    fsmart.flowName = "AdminSmartFlow";

    fsmart.checkTenantExistence();
}

function populatevalues()
{
    populateFlows();
}

function enablefortenant(links)
{
    var admsmart = new smartadmin();
    setupconn(admsmart, populatevalues);
    admsmart.sessionId = $('#adminsessid').val();
    admsmart.flowName = "AdminSmartFlow";
    var tnt = $('#tenant').val();
    var flow = $('#flowname').val();
    var features = $('#features').val();
    var farray = features.split(",");
    admsmart.enableFlow($('#tenant').val(), flow, farray, links);
}

function getmashup(resp)
{
    console.log(resp);
    var ldep = resp.response['deployments'];
    if ((ldep != undefined) && (ldep.length > 0))
    {
        var div = $('#reqdlinks');
        div.empty();
        $('#linklength').val(ldep.length);
        for (var lnk in ldep)
        {
            addToReqdLinks(lnk, ldep[lnk]);
        }

        showBox($('#configurelinks'), '250px', '200px');
    }
    else
        enablefortenant();
}

function checkmashup()
{
    var admsmart = new smartadmin();
    setupconn(admsmart, getmashup);
    admsmart.sessionId = $('#adminsessid').val();
    admsmart.flowName = "AdminSmartFlow";
    admsmart.listLinks($('#flowname').val());
}

function filedeploy()
{
    var admsmart = new smartadmin();
    setupconn(admsmart, checkmashup);
    admsmart.sessionId = $('#adminsessid').val();
    admsmart.flowName = "AdminSmartFlow";
    admsmart.deploy($('#deployjar').val(), $('#soafile').val());
}

function adminlogin()
{
    var fsmart = new security();
    setupconn(fsmart, function(resp) {
        var sessid = resp.response.sessId;
        $('#adminsessid').val(sessid);
        //filedeploy();
        checktenant();

    });
    fsmart.flowName = "Security";
    fsmart.smartLogin();
}

function setupresult(result)
{
    console.log(result);
    if (result instanceof smartresponse) {
        var lresp = result.response;
        if (lresp instanceof listallresponse)
        {
            console.log(lresp.list.length);
            $('#resulttab').empty();
            var th = $("<thead/>");
            var added = false;
            for (var i = 0; i < lresp.list.length; i++)
            {
                var tr = $("<tr/>");
                for (var nm in lresp.list[i].values)
                {
                    if (nm.indexOf("___smart") < 0)
                    {
                        console.log(nm + ":" + lresp.list[i].values[nm]);
                        if (!added)
                        {
                            var td = $("<th/>", { 'text': nm });
                            th.append(td);
                        }

                        var val = lresp.list[i].values[nm];
                        var td = $("<td/>", {'text':val});
                        tr.append(td);
                    }
                }

                if (!added)
                    $('#resulttab').append(th);
                added = true;
                $('#resulttab').append(tr);
            }
            window.location=document.getElementById('resultaref').href;
        }
    }
    else
        window.location=document.getElementById('stataref').href;
}

$('#setupdata').submit(function(e) {
    e.preventDefault();

    filedeploy();
    return false;
});

$('#connectsmart').submit(function(e) {
    e.preventDefault();
    $('#adminsessid').val(''); //reset the sessionid
    $('#sessionid').val(''); //reset the sessionid
    adminlogin();
    return false;
});

function addto(data, key, val)
{
    /*if (key.indexOf("Double click") >= 0)
        return;

    if (val.indexOf("Double click") >= 0)
        return;*/

    if (data[key] != undefined)
    {
        var exist = data[key];
        if ($.isArray(exist))
        {
            exist.push(val);
            val = exist;
        }
        else
        {
            var arr = [];
            arr.push(exist);
            arr.push(val);
            val = arr;
        }
    }

    data[key] = val;
}

function createfrom(tbl)
{
   var data = {};
   var tbody = $($(tbl).children()[1]);
   $(tbody.children()).each(function() {
        var keyvals = $(this).children();
        var key = $(keyvals[0]).text();
        var val = $(keyvals[1]).text();
        console.log(": " + key + "=" + val + ":" + $(keyvals[1]).children().length);
        if ($(keyvals[1]).children().length > 0)
        {
            val = createfrom($(keyvals[1]).children()[0]);
        }

        addto(data, key, val);
   });
   return data;
}

$('#postgeneral').submit(function(e) {
    e.preventDefault();
    var fsmart = new smart();
    setupconn(fsmart, setupresult);

    var evt = $('#event').val();
    var elems = getStd(evt);

    var data = {};
    var std = {};

    var tbody = $('#data');
    $(tbody.children()).each(function() {
        var keyvals = $(this).children();
        var key = $(keyvals[0]).text();
        var val = $(keyvals[1]).text();
        console.log(": " + key + "=" + val + ":" + $(keyvals[1]).children().length);

        if ($(keyvals[1]).children().length > 0)
        {
            val = createfrom($(keyvals[1]).children()[0]);
        }

        if ($.inArray(key, elems) >= 0)
            std[key] = val;
        else
           addto(data, key, val);
    });

    console.log(data);
    console.log(std);

    if (elems != undefined)
    {
        runStandard(fsmart, evt, std, data);
    }
    else
    {
       if ($('#posttotype').val() == "")
       {
           alert("Please select an object to post to.");
           return;
       }

       if (($('#posttoval').val() == "") || ($('#posttoval').val() == undefined))
       {
           alert("Please enter a value of the key object");
           return;
       }
       fsmart.postDataTo(data, evt, $('#posttotype').val(), $('#posttoval').val());
    }

    return false;
});


$('#configlinks').submit(function(e) {
    e.preventDefault();
    var len = $('#linklength').val();
    var links = [];
    for (var i = 0; i < len; i++)
    {
        var lnk = {};
        lnk['name'] = $('#name' + i).val();
        lnk['flow'] = $('#flow' + i).val();
        lnk['object'] = $('#object' + i).val();
        lnk['attribute'] = $('#attribute' + i).val();
        if (lnk['attribute'] != '')
            links.push(lnk);
    }

    enablefortenant(links);
    hideBox($('#configurelinks'));
    return false;
});

function runStandard(fsmart, evt, std, data)
{
    if (evt == 'ListAllEvent')
    {
        fsmart.list(std["group"], parseInt(std["size"]));
    }
    else if (evt == 'CreatePrime')
    {
        fsmart.createPrime(std["group"], data);
    }
    else if (evt == 'SearchEvent')
    {
        searchObj = {};
        searchObj['group'] = std['group'];
        searchObj['queryMap'] = data;
        fsmart.searchdata(searchObj);
    }
    else if (evt == 'LookupEvent')
    {
       fsmart.lookup(std['group'], std['key']);
    }
    else
       alert("Not Supported");
}

function addToReqdLinks(cnt, lnk)
{
    var div = $('#reqdlinks');
    var tr = $("<tr/>");
    var td1 = $("<td/>");
    var td2 = $("<td/>");
    var td3 = $("<td/>");
    var td4 = $("<td/>");
    var lnkin = $('<input/>', {
        'class':'input-block-level',
        'type':'text',
        'value':lnk,
        'readonly':'readonly',
        'id':'name' + cnt
    });

    td1.append(lnkin);

    var flows = $('<select/>', {
        'class':'input-block-level',
        'id':'flow' + cnt,
        'placeholder':'Select a flow from which to link'
    });

    $("#flow option").each(function() {
        var val = $(this).val();
        var txt = $(this).html();
        flows.append(
            $('<option/>').val(val).html(txt)
        );
    });

    td2.append(flows);

    var objs = $('<input/>', {
        'class':'input-block-level',
        'type':'text',
        'id':'object' + cnt,
        'placeholder':'Enter an object to link to'
    });

    td3.append(objs);

    var attr = $('<input/>', {
        'class':'input-block-level',
        'type':'text',
        'id':'attribute' + cnt,
        'placeholder': 'Enter the attribute of the object to link this to'
    });

    td4.append(attr);

    tr.append(td1);
    tr.append(td2);
    tr.append(td3);
    tr.append(td4);
    div.append(tr);
}

function showBox(div, x, y)
{
    div.css({left: x});
    div.css({top: y});
    div.css({opacity:"1"});
    $('#body-container').css({'pointer-events':'none'})
}

function hideBox(div)
{
    div.css({left: "-1000px"});
    div.css({top: "-1000px"});
    div.css({opacity:"0"});
    $('#body-container').css({'pointer-events':'auto'})
}
