package org.anon.smart.generator.script;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.ResourceManager;
import org.apache.velocity.runtime.resource.ResourceManagerImpl;

public final class ScriptGen {

	public ScriptGen() {
		// TODO Auto-generated constructor stub
	}

	public static String generateScript(final ScriptDescription sd) {
		String result = "";
		if (sd == null) {
			throw new IllegalArgumentException(
					"ScriptDecsriptor must not be null");
		}

		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			VelocityEngine engine = new VelocityEngine();
			engine.init(p);

			// Velocity.init(p);

			StringWriter sw = new StringWriter();

			VelocityContext context = new VelocityContext();
			context.put("sd", sd);
			// context.put("newline", "\n");
			context.put("StringUtils", new StringUtils());

			Template template = engine
					.getTemplate("org/anon/smart/generator/templates/Script.wm");
			template.merge(context, sw);
			result = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return result;
	}
}
