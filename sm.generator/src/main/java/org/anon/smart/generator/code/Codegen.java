package org.anon.smart.generator.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import org.apache.velocity.app.Velocity;

/**
 * Static source code generation methods.
 * 
 */
public final class Codegen {

	/**
	 * Private default constructor.
	 */
	private Codegen() {
		// empty
	}

	/**
	 * Read license text from the specified file.
	 * 
	 * @param license
	 *            license file to read
	 * @return license text from the specified file or null if the specified
	 *         license file cannot be read
	 */
	public static String readLicense(final String license) {
		String text = null;
		try {
			text = FileUtils.readFileToString(new File(license));
		} catch (IOException e) {
			// ignore
		}
		return text;
	}

	/**
	 * Generate a java source file for the specified interface description.
	 * 
	 * @param id
	 *            interface description, must not be null
	 */
	public static void generateSource(final InterfaceDescription id) {
		if (id == null) {
			throw new IllegalArgumentException("id must not be null");
		}

		FileWriter fw = null;
		try {
			System.out.println("The function was called");
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			fw = new FileWriter(id.getUpper() + ".java");

			VelocityContext context = new VelocityContext();
			context.put("id", id);
			context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity
					.getTemplate("org/dishevelled/codegen/Interface.wm");
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

	/**
	 * Generate an abstract unit test java source file for the specified
	 * interface description.
	 * 
	 * @param id
	 *            interface description, must not be null
	 */
	public static void generateAbstractUnitTest(final InterfaceDescription id) {
		if (id == null) {
			throw new IllegalArgumentException("id must not be null");
		}

		FileWriter fw = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			fw = new FileWriter("Abstract" + id.getUpper() + "Test.java");

			VelocityContext context = new VelocityContext();
			context.put("id", id);
			context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity
					.getTemplate("org/anon/smart/generator/templates/AbstractInterfaceTest.wm");
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

	/**
	 * Generate a java source file for the specified class description and
	 * style.
	 * 
	 * @param cd
	 *            class description, must not be null
	 * @param style
	 *            style, must not be null
	 */
	public static void generateSource(final ClassDescription cd,
			String targetPath, final Style style) {
		if (cd == null) {
			throw new IllegalArgumentException("cd must not be null");
		}
		if (style == null) {
			throw new IllegalArgumentException("style must not be null");
		}

		FileWriter fw = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			File targetFile = new File(targetPath + cd.getUpper() + ".java");
			File parent = targetFile.getParentFile();

			// clear target directory:
			if (parent.exists()) {
				parent.delete();
			}
			// create new target directory:
			parent.mkdirs();

			fw = new FileWriter(targetFile);

			VelocityContext context = new VelocityContext();
			context.put("cd", cd);
			context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity.getTemplate("org/anon/smart/generator/templates/"
					+ style + ".wm");
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

	/**
	 * Generate a java source file for a fluent builder API for the specified
	 * class description and style.
	 * 
	 * @param cd
	 *            class description, must not be null
	 * @param style
	 *            style, must not be null
	 */
	public static void generateBuilderSource(final ClassDescription cd,
			final Style style) {
		// TODO: perhaps style doesn't matter here, if all the templates are the
		// same
		if (cd == null) {
			throw new IllegalArgumentException("cd must not be null");
		}
		if (style == null) {
			throw new IllegalArgumentException("style must not be null");
		}

		FileWriter fw = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			fw = new FileWriter(cd.getUpper() + "Builder.java");

			VelocityContext context = new VelocityContext();
			context.put("cd", cd);
			context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity.getTemplate("org/anon/smart/generator/templates/"
					+ style + "Builder.wm");
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

	/**
	 * Generate a unit test source file for the specified class description and
	 * style.
	 * 
	 * @param cd
	 *            class description, must not be null
	 * @param style
	 *            style, must not be null
	 */
	public static void generateUnitTest(final ClassDescription cd,
			final Style style) {
		if (cd == null) {
			throw new IllegalArgumentException("cd must not be null");
		}
		if (style == null) {
			throw new IllegalArgumentException("style must not be null");
		}

		FileWriter fw = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			fw = new FileWriter(cd.getUpper() + "Test.java");

			VelocityContext context = new VelocityContext();
			context.put("cd", cd);
			context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity.getTemplate("org/anon/smart/generator/templates/"
					+ style + "Test.wm");
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

	/**
	 * Generate an enum source file for the specified class description.
	 * 
	 * @param cd
	 *            class description, must not be null
	 */
	public static void generateEnum(final ClassDescription cd) {
		if (cd == null) {
			throw new IllegalArgumentException("cd must not be null");
		}

		FileWriter fw = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			fw = new FileWriter(cd.getUpper() + ".java");

			VelocityContext context = new VelocityContext();
			context.put("cd", cd);
			context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity
					.getTemplate("org/anon/smart/generator/templates/Enum.wm");
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

	/**
	 * Generate an enum with lookup source file for the specified class
	 * description.
	 * 
	 * @param cd
	 *            class description, must not be null
	 */
	public static void generateEnumWithLookup(final ClassDescription cd) {
		if (cd == null) {
			throw new IllegalArgumentException("cd must not be null");
		}

		FileWriter fw = null;
		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p.setProperty("class.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

			Velocity.init(p);

			fw = new FileWriter(cd.getUpper() + ".java");

			VelocityContext context = new VelocityContext();
			context.put("cd", cd);
			context.put("CodegenUtils", new CodegenUtils());
			context.put("StringUtils", new StringUtils());

			Template template = Velocity
					.getTemplate("org/anon/smart/generator/templates/EnumWithLookup.wm");
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
