
package es.bsc.dataclay.logic.classmgr.bytecode.pysrc.codeutils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class may be used to synthesize Python source code files.
 * 
 * 
 *         The current implementation can be used to synthesize a single Python source file which must contain one and only one
 *         class. Although this is expected in Java, Python source files may contain more than one class, amongst other things.
 */
public final class SourceContainer {

	/** Lines associated with the header of the file. */
	private List<String> headerLines;
	/** Lines associated with the definition of the class. */
	private List<String> classDef;
	/** Lines associated with the class body (functions). */
	private List<String> classBody;
	/** Lines associated with the class closure (not required by Python). */
	private List<String> classEnd;
	
	/** Track whether any method has been added to this classs. */
	private boolean hasAnyMethod = false;

	/**
	 * Empty default constructor.
	 */
	public SourceContainer() {
		
	}

	/**
	 * Internal function to add lines to a certain sink.
	 * 
	 * @param sink
	 *            A list of strings to where the lines will be appended.
	 * @param lineList
	 *            The list of strings.
	 */
	private static void addLines(final StringBuffer sink, final List<String> lineList) {
		for (String line : lineList) {
			sink.append(line);
		}
	}

	/**
	 * Initialize the header of the Python source file with a simple comment.
	 * @param extraComment
	 *            An extra line that will be put as a comment in the "header". This initialization should be called before any
	 *            function related to the header.
	 */
	public void initHeader(final String extraComment) {
		headerLines = new ArrayList<String>();
		headerLines.add(LineFactory.generatePythonCommentLine("dataClay header", 0));
		headerLines.add(LineFactory.generatePythonCommentLine(extraComment, 0));
		headerLines.add(LineFactory.generatePythonCodeline("from dataclay import dclayMethod, DataClayObject", 0));
		headerLines.add(LineFactory.generatePythonCodeline("", 0));
	}

	/**
	 * Put the import information into the Stub.
	 * @param importInfo
	 *            A list of tuples, containing the data provided by the registrator.
	 */
	public void addImportInfo(final List<String> importInfo) {
		// Documentation / debugging purposes
		headerLines.add(LineFactory.generatePythonCommentLine(
				"Imports required by the following class", 0));
		
		// Imports, verbatim
		for (String importLine : importInfo) {
			headerLines.add(LineFactory.generatePythonCodeline(importLine, 0));
		}
	}

	/**
	 * Add an import line to the header of the file.
	 * 
	 * @param clsName
	 *            The name of the import. The result will be simply _import clsName_.
	 */
	public void importModule(final String clsName) {
		headerLines.add(LineFactory.generatePythonCodeline("import " + clsName, 0));
	}

	/**
	 * Initialize the class definition.
	 * 
	 *        This initialization should be called before any function related to the class definition and body.
	 * 
	 * @param name
	 *            The name of the class.
	 * @param parentName
	 *            The name of the parent class.
	 */
	public void initClassDef(final String name, final String parentName) {
		classDef = new ArrayList<String>();
		classBody = new ArrayList<String>();

		classDef.add(LineFactory.generatePythonCommentLine("Definition of dataClay object class: " + name, 0));
		classDef.add(LineFactory.generatePythonClass(name, parentName, 0));
	}

	/**
	 * Build a certain method definition and put it into the class.
	 * 
	 * @param mthdName
	 *            Name of the method.
	 * @param params
	 *            List of parameter names.
	 * @param mthdCode
	 *            A list of lines for the class --zero indentation.
	 */
	public void addMethod(final String mthdName, final String[] params, final String[] mthdCode) {
		hasAnyMethod = true;
		classBody.add(LineFactory.generatePythonMethod(mthdName, params, 1));
		for (int i = 0; i < mthdCode.length; i++) {
			classBody.add(LineFactory.generatePythonCodeline(mthdCode[i], 1));
		}
	}

	/**
	 * End the class.
	 * 
	 *        This may be done at any moment --not necessarily at the end. It simply puts a certain comment at the end of the
	 *        class definition. Esthetic.
	 */
	public void endClass() {
		if (!hasAnyMethod) {
			classBody.add(LineFactory.generatePythonCodeline("pass", 1));
		}
		classEnd = new ArrayList<String>();
		classEnd.add(LineFactory.generatePythonCommentLine("End of class definition", 0));
	}

	/**
	 * Return the full source. Should be called at last.
	 * 
	 * @return A very long string containing all the source code.
	 */
	public String sourceToString() {
		final StringBuffer ret = new StringBuffer();

		addLines(ret, headerLines);
		ret.append("\n");

		addLines(ret, classDef);
		ret.append("\n");
		addLines(ret, classBody);
		addLines(ret, classEnd);

		return ret.toString();
	}

	/**
	 * Add an almost verbatim method into the body of the class.
	 * 
	 * @param implLines
	 *            List of code lines of the method. It should include the definition --at zero indentation-- and the body
	 *            definition with correct relative indent.
	 */
	public void addMethod(final String[] implLines) {
		// Now the rest of the implementation is verbatim (relative indentation is present)
		for (String line : implLines) {
			classBody.add(LineFactory.generatePythonCodeline(line, 0));
		}

	}

}
