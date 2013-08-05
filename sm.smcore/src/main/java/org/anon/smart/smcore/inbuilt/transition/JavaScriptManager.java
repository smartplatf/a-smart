package org.anon.smart.smcore.inbuilt.transition;

import static org.anon.utilities.services.ServiceLocator.assertion;

import org.anon.smart.base.tenant.CrossLinkSmartTenant;
import org.anon.smart.base.tenant.shell.CrossLinkDeploymentShell;
import org.anon.smart.base.tenant.shell.RuntimeShell;
import org.anon.smart.codegen.Generator;
import org.anon.smart.codegen.GeneratorMeta;
import org.anon.smart.codegen.Nature;
import org.anon.smart.codegen.Type;
import org.anon.smart.codegen.factory.GeneratorFactory;
import org.anon.smart.smcore.events.SmartEvent;
import org.anon.smart.smcore.inbuilt.events.GetJavaScript;
import org.anon.smart.smcore.inbuilt.responses.JavaScriptSnippet;
import org.anon.utilities.exception.CtxException;

public class JavaScriptManager {

	public JavaScriptManager() {
		// TODO Auto-generated constructor stub
	}

	public void generateSnippet(GetJavaScript script) throws CtxException {
		System.out.println("Got the Event -->" + script);
		String genSnippet;
		
		assertion().assertNotNull(script, "SearchManager: search event is NULL");
			
		CrossLinkSmartTenant tenant = CrossLinkSmartTenant.currentTenant();
		RuntimeShell shell = (RuntimeShell)(tenant.runtimeShell());
		assertion().assertNotNull(shell, "SearchManager: Runtime Shell is NULL");
		CrossLinkDeploymentShell dShell = new CrossLinkDeploymentShell(tenant.deploymentShell());
		Object o = script;
        	SmartEvent sevt = (SmartEvent)o;
        	String flow = sevt.smart___flowname();
		
		//System.out.println("name of the tenant------>" + tenant.getName());
		//System.out.println("name of the flow------>" + flow);
		
		GeneratorMeta JSMeta = new GeneratorMeta();
		// set tenant id
		JSMeta.setTenantId(script.getTenantID());
		// set flow name
		JSMeta.setFlowName(script.getFlowName());

		try {
			// get the code or soa factory
			GeneratorFactory generatorFac = GeneratorFactory.getInstance(Nature.CODE.getNature());
			System.out.println("The class instantiated --> "+ generatorFac.getClass().getName());

			// get the appropriate generator
			Generator gen = generatorFac.getGenerator(Type.JSCRIPT);
			System.out.println("getting generator--->"+ gen.getClass().getCanonicalName());

			genSnippet = gen.generateCode(JSMeta);

			JavaScriptSnippet snippet = new JavaScriptSnippet(genSnippet );
			// Todo: send generated snippet in response
		} catch (CtxException e) {
			e.printStackTrace();
			throw new CtxException("Error while trying to create java script snippet" + e);
		}
	}
}
