package org.anon.smart.generator.flowgen;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.anon.smart.generator.code.ClassDescription;
import org.anon.smart.generator.code.CodegenUtils;

public final class SoaFileGen {

	public SoaFileGen() {
		// TODO Auto-generated constructor stub
	}

	public static void generateSoa(final FlowDescription fd, String targetPath) {
		if (fd == null) {
			throw new IllegalArgumentException("cd must not be null");
		}

		FileWriter fw = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			File targetFile = new File(targetPath + fd.getName() + ".soa");
			File parent = targetFile.getParentFile();

			// clear target directory:
			if (parent.exists()) {
				parent.delete();
			}
			// create new target directory:
			parent.mkdirs();

			fw = new FileWriter(targetFile);

			VelocityContext context = new VelocityContext();
			context.put("fd", fd);
			context.put("newline","\n");
			// context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity
					.getTemplate("org/anon/smart/generator/templates/FlowSoa.wm");
			template.merge(context, fw);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				fw.close();
			} catch (Exception e) {
				// empty
			}
		}
	}
}
