
package es.bsc.dataclay.logic.classmgr.bytecode.pysrc.codeutils;

import es.bsc.dataclay.util.management.classmgr.python.PythonImplementation;

/**
 * Some primitive utilities for generating Python code lines.
 * 
 * 
 */
public final class LineFactory {

	/**
	 * Default constructor for this Utility Class.
	 */
	private LineFactory() {

	}

	/**
	 * Given a certain array of byte, extract the line-to-line python implementation.
	 * 
	 *        We expect a certain sequence of characters of python code. The relative indentation is assumed to be correct, and for
	 *        compliance the initial indentation should be zero.
	 * 
	 * @param baseImplementation
	 *            Contains the byte array.
	 * @return Returns an array of Strings, with relative indentation, of the Python code for the defined function
	 */
	public static String[] extractPythonCodelines(final PythonImplementation baseImplementation) {
		final String impl = baseImplementation.getCode();
		final String[] methodLines = impl.split("\n");

		return methodLines;
	}

	/**
	 * Returns an string with enough spaces to be considered a certain level indentation.
	 * 
	 *        Following Python standards, the indentation used is 4 spaces. For each level, 4 spaces are introduced.
	 * 
	 *        Note that parsed code has several sources, so there may be inconsistencies in the generated indentantion. However,
	 *        Python interpreters should be capable of understanding the code.
	 * 
	 * @param level
	 *            Nested number of levels. Each level is considered to be 4 spaces.
	 * @return An string with \t characters
	 */
	private static String generateIndent(final int level) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < level; i++) {
			// 4 spaces
			result.append("    ");
		}
		return result.toString();
	}

	/**
	 * Generates a class definition in Python for class
	 * @param className
	 *            Name of the class
	 * @param parentName
	 *            Name of the parent class (may be NULL)
	 * @param level
	 *            Indentation level to use.
	 * @return An string defining the class using Python syntax "[_indent_*level]class className(parentName):\n"
	 */
	public static String generatePythonClass(final String className, final String parentName, final int level) {
		final StringBuffer result = new StringBuffer();
		// prepare indentation
		result.append(generateIndent(level));
		// prepare class
		result.append("class " + className + "(");
		if (parentName != null) {
			result.append(parentName);
		} else {
			result.append("object");
		}
		result.append("):\n");
		return result.toString();
	}

	/**
	 * Generates a method definition for a class in Python
	 * @param methodName
	 *            Name of the method
	 * @param parameters
	 *            Array of parameter names
	 * @param level
	 *            Indentation level to use.
	 * @return An string defining the method using Python syntax "[_indent_*level]def methodName(self, parameters[0],
	 *         parameters[1]...):\n"
	 */
	public static String generatePythonMethod(final String methodName, final String[] parameters, final int level) {
		final StringBuffer result = new StringBuffer();
		// prepare indentation
		result.append(generateIndent(level));
		// prepare method
		result.append("def " + methodName + "(self");
		if (parameters != null) {
			for (String p : parameters) {
				result.append(", " + p);
			}
		}
		result.append("):\n");
		return result.toString();
	}

	/**
	 * Generate a single line of Python code taking into account the indentation level.
	 * @param line
	 *            String with the Python line to include.
	 * @param level
	 *            Indentation level to use.
	 * @return An string with the line of code. "[_indent_*level]line\n"
	 */
	public static String generatePythonCodeline(final String line, final int level) {
		final StringBuffer result = new StringBuffer();
		// prepare indentation
		result.append(generateIndent(level));
		// add line
		result.append(line + "\n");
		return result.toString();
	}

	/**
	 * Generate a single line comment of Python code
	 * @param comment
	 *            Readable comment (without # nor any prefix).
	 * @param level
	 *            Indentation level to use.
	 * @return A string with the line of code. "[_indent_*level]# line\n"
	 */
	public static String generatePythonCommentLine(final String comment, final int level) {
		final StringBuffer result = new StringBuffer();
		result.append(generateIndent(level));
		result.append("# " + comment + "\n");
		return result.toString();
	}
}
