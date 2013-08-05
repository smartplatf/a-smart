describe("SMART js library testing", function(){
	
		beforeEach(function(){
			var err;
		 smart.server = "localhost";
		 smart.successFunction = "pass";
		 smart.failFunction = "fail";
		 
		 function pass(data){
				 smart.responseObj = data;
				 }
		 function fail(data){
			 smart.responseObj = data;
		 }
		});
	
	
	xit("look up event test",function(){
		var objreqd = {
				"group":"abcd",
				"key":1};
		smart.protocol = "https";
		var objExpect = {FlowAdmin:{"___smart_action___":"lookup","___smart_value___":"someflow"},"group":"abcd","key":1};
		smart.lookup(objreqd);
		expect(smart.dataSubmit).toEqual(objExpect);
		});
	
	xit("look up event test without group, FAILS!! :)",function(){
		var objreqd = {
				"key":1};
		var objExpect = {FlowAdmin:{}};
		smart.lookup(objreqd);
		expect(smart.dataSubmit).toEqual(objExpect);
		});
	
	xit("search event test PASSED!",function(){
		var objreqd = {
				"flow":"anyflow",
				"group":"abcd",
				"queryMap":"test@gmail.com"};
		var objExpect = {FlowAdmin:{"___smart_action___":"lookup","___smart_value___":"anyflow"},"group":"abcd","queryMap":"test@gmail.com"};
		smart.search(objreqd);
		expect(smart.dataSubmit).toEqual(objExpect);
		});
	
	xit("search event test with missing data, FAILS!! :)",function(){
		var objreqd = {	};
		var objExpect = {FlowAdmin:{"___smart_action___":"lookup","___smart_value___":"someflow"},"group":"abcd","queryMap":"test@gmail.com"};
		smart.search(objreqd);
		expect(smart.dataSubmit).not.toEqual(objExpect);
		});
	
	xit("search event data fields with case mismatch, also FAILS!! :) ",function(){
		var objreqd = {"group":"abcd",
				"querymap":"test@gmail.com"
				};
		url = "http://www.smart-platform.com:9000/abcd1234/someflow/SearchEvent";
		smart.search(objreqd);
		expect(smart.targetUrl).not.toEqual(url);
		});
	
	xit("testing for call back functions", function(){
		expect(validateUserFunctionNames(smart.failFunction,"testing")).toEqual("failed to post");
	});
	
	it("testing Deploy",function(){
		obj = {"deployJar":"/E:/Survey.jar",
				"flowsoa":"Survey.soa"
		}
		var expected = '{"TenantAdmin":{"___smart_action___":"lookup","___smart_value___":"SmartOwner"},"deployJar":"/E:/Survey.jar","flowsoa":"Survey.soa"}';
		smart.deploy(obj);
		expect(smart.targetUrl).toEqual("http://localhost:9081/SmartOwner/AdminSmartFlow/DeployEvent");
		expect(smart.dataSubmit).toEqual(expected);
	});
});