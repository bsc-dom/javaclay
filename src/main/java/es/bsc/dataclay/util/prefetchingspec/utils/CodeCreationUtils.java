
package es.bsc.dataclay.util.prefetchingspec.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.prefetchingspec.PrefetchingSpecGenerator;

public class CodeCreationUtils {
	
	public static final String METHOD_NAME_SUFFIX = "_prefetch";

	public static final String CLASS_DELIM = "\n}";

	public static final String METHOD_DELIM = "}\n";

	public static final String INSTR_DELIM = ";\n";

	public static final String INITIAL_PREFETCHING_PARAM = "param";

	public static final String INITIAL_INDENT = "\t\t";
	
	public static final String LOAD_METHOD = "$_load";
	
	public static final String GETTER_METHOD = "$_get";

	public static Map<String, String> createPrefetchingClassCode(final Map<String, MetaClass> metaClasses, final String srcPath) {
		Map<String, String> prefetchingClasses = new HashMap<String, String>();

		// Iterate through meta classes and create prefetching classes
		for (String className : metaClasses.keySet()) {			
			String prefetchingClassName = className + METHOD_NAME_SUFFIX;

			// Create prefetching class code
			String prefetchingMethodsCode = "";

			for (Operation op : metaClasses.get(className).getOperations()) {
				// If the method is a dataClay getter/setter, ignore it
				if (op.getName().contains("$$") || op.getName().contains("$_") || op.getName().equals("<init>")) {
					continue;
				}

				// Otherwise, create prefetching method inside new class
				prefetchingMethodsCode += createPrefetchingMethodCode(op, metaClasses.keySet(),
						prefetchingClassName, metaClasses.get(className).getNamespace());
			}

			// If code is empty, it means no method in this class has prefetching info. Should be ignored.
			if (prefetchingMethodsCode.equals("")) {
				continue;
			}

			final String prefetchingClassCode = getClassHeader(prefetchingClassName)
					+ prefetchingMethodsCode
					// + prefetchingClassConstructorsCode
					+ CLASS_DELIM;

			// Save prefetching class in created directory
			saveCodeToFile(prefetchingClassCode, srcPath + "/" + prefetchingClassName.replace('.', '/'));
			prefetchingClasses.put(className, prefetchingClassName);
		}

		PrefetchingSpecGenerator.logger.debug("[==CodeCreationUtils==] Prefetching classes stored successfully in path: " + srcPath);

		return prefetchingClasses;
	}

	private static String createPrefetchingMethodCode(final Operation op, final Set<String> persistentClasses,
			final String prefetchingClassAndPackage, final String prefetchingClassNamespace) {
		// This assumes that at this point every operation only has ONE implementation
		final PrefetchingInformation prefetchInfo = op.getImplementations().get(0).getPrefetchingInfo();
		if (prefetchInfo == null
				|| prefetchInfo.getPropertiesToPrefetch() == null
				|| prefetchInfo.getPropertiesToPrefetch().size() == 0) {
			return "";
		}

		String methodBody = "";

		// Iterate through prefetching hints
		int hintCount = 0;
		for (List<Property> prefetchHint : prefetchInfo.getPropertiesToPrefetch()) {
			String delim = ".";
			String hintInstrs = INITIAL_PREFETCHING_PARAM;
			int collectionCount = 0;
			
			// For each property, call its dataClay getter
			for (int i = 0; i < prefetchHint.size(); i++) {
				Property prop = prefetchHint.get(i);
				if (!persistentClasses.contains(getCollectionElemType(prop.getType()))) {
					break;
				}
				
				// Add property getter instruction
				hintInstrs += delim + getGetterInstr(prop.getName());
				
				String indexCount = String.valueOf(hintCount) + String.valueOf(collectionCount);
				String indentStr = getIndentByCollectionCount(collectionCount);
				
				// Handle collections
				int collectionType = isCollectionType(prop.getType());
				if (collectionType > 0) {
					methodBody += indentStr + getCollectionLoopInstr(prop, hintInstrs, collectionType, indexCount, indentStr);

					collectionCount++;
					indentStr = getIndentByCollectionCount(collectionCount);
					
					// Explicit cast to collection element type
					hintInstrs = getCollectionElemVar(indexCount);
					
					// If collection is of various types, check type of element
					if (i < prefetchHint.size() - 1
							&& !getCollectionElemType(prop.getType()).equals(prefetchHint.get(i + 1).getClassName())) {
						methodBody += getCheckCastInstrs(prefetchHint.get(i + 1).getClassName(), indexCount, indentStr);
						hintInstrs = getCollectionTypedElemVar(indexCount);
					}
				}
			}

			if (!hintInstrs.equals("")) {
				// Load last object
				methodBody += getIndentByCollectionCount(collectionCount) 
						+ hintInstrs + "." + LOAD_METHOD + "()" + INSTR_DELIM;

				// Close all collection loop curly brackets (if any)
				for (int col = 0; col < collectionCount; col++) {
					String indentStr = getIndentByCollectionCount(collectionCount - col - 1);
					if (Configuration.Flags.PREFETCHING_PARALLEL_ENABLED.getBooleanValue()) {
						methodBody += indentStr + "})" + INSTR_DELIM;
					} else {
						methodBody += indentStr + METHOD_DELIM;
					}
				}
				hintCount++;
			}
		}

		// If method code is empty, return empty string without header and delimiter.
		if (methodBody.equals("")) {
			return "";
		}

		String indentStr = INITIAL_INDENT;
		methodBody = getMethodHeader(op.getName(), op.getClassName())
				+ indentStr + "try {\n"
				+ indentStr + setPrefetchingAccessInstr(true) + INSTR_DELIM
				+ indentStr + getStartTimeInstr(op.getName(), indentStr) + INSTR_DELIM
				+ methodBody
				+ indentStr + getEndTimeInstr(op.getName(), indentStr) + INSTR_DELIM
				+ indentStr + setPrefetchingAccessInstr(false) + INSTR_DELIM
				+ indentStr + "} catch (Exception e) {\n"
				//+ indentStr + "\te.printStackTrace()" + INSTR_DELIM
				+ indentStr + METHOD_DELIM
				+ "\t" + METHOD_DELIM;

		// Store prefetching class name and method signature in implementation meta class
		op.getImplementations().get(0).getPrefetchingInfo().setInjectPrefetchingCall(true);
		op.getImplementations().get(0).getPrefetchingInfo().setPrefetchingNameSpace(prefetchingClassNamespace);
		op.getImplementations().get(0).getPrefetchingInfo().setPrefetchingClassName(prefetchingClassAndPackage);
		op.getImplementations().get(0).getPrefetchingInfo().setPrefetchingMethodSignature(
				getMethodSignature(op.getName(), op.getClassName()));

		return methodBody;
	}

	/************************/
	/**** HELPER METHODS ****/
	/************************/

	private static String getClassHeader(final String prefetchingClassName) {		
		String result = "";
		String packageName = "";
		String className = prefetchingClassName;
		if (prefetchingClassName.contains(".")) {
			packageName = prefetchingClassName.substring(0, prefetchingClassName.lastIndexOf('.'));
			className = prefetchingClassName.substring(prefetchingClassName.lastIndexOf('.') + 1, prefetchingClassName.length());
		}
		
		if (!packageName.equals("")) {
			result = "package " + packageName + INSTR_DELIM + "\n";
		}
		result += "import java.util.Iterator" + INSTR_DELIM
				+ "import java.util.Collection" + INSTR_DELIM 
				+ "import java.util.Date" + INSTR_DELIM + "\n"
				+ "import java.util.Arrays" + INSTR_DELIM + "\n"
				+ "public class " + className + " {\n";
		
		return result;
	}

	private static String getMethodHeader(final String methodName, final String className) {
		return "\n\tpublic static final void " + methodName + METHOD_NAME_SUFFIX + "(final "
				+ className + " " + INITIAL_PREFETCHING_PARAM + ") {\n";
	}

	private static String getMethodSignature(final String methodName, final String className) {
		return methodName + METHOD_NAME_SUFFIX + "(L" + className.replace(".", "/") + ";)V";
	}

	public static String getGetterInstr(final String propName) {
		return GETTER_METHOD + propName + "()";
	}

	private static String getCollectionLoopInstr(Property prop, String colVar, int collectionType, String iCount, String indentStr) {		
		String colObjVar = getCollectionElemVar(iCount);
		String colElemType = getCollectionElemType(prop.getType());

		if (collectionType == 1) {
			if (Configuration.Flags.PREFETCHING_PARALLEL_ENABLED.getBooleanValue()) {
				return "Arrays.stream(" + colVar + ").parallel().forEach(" + colObjVar + " -> {\n";	
			} else {
				String counterVar = "i" + iCount;
				return "for (int " + counterVar + " = 0; " 
						+ counterVar + " < " + colVar + ".length; "
						+ counterVar + "++) {\n"
						+ indentStr + "\t" + colElemType + " " + colObjVar + " = " + colVar + "[i" + iCount + "]" + INSTR_DELIM;	
			}
		} else if (collectionType == 2) {
			if (Configuration.Flags.PREFETCHING_PARALLEL_ENABLED.getBooleanValue()) {
				return colVar + ".parallelStream().forEach(" + colObjVar + " -> {\n";
			} else {
				String itrVar = "itr" + iCount;
				return "Iterator " + itrVar + " = " + colVar + ".iterator()" + INSTR_DELIM
						+ indentStr + "while (" + itrVar + ".hasNext()) {\n"
						+ indentStr + "\t" + colElemType + " " + colObjVar + " = (" + colElemType + ") " + itrVar + ".next()" + INSTR_DELIM;	
			}
		}

		return "";
	}
	
	private static String getCollectionElemVar(String iCount) {
		return "colObj" + iCount;
	}
	
	private static String getCollectionTypedElemVar(String iCount) {
		return "typedColObj" + iCount;
	}
	
	private static String getCollectionElemType(Type propType) {
		if (propType.getIncludes() != null && propType.getIncludes().size() > 0) {
			return propType.getIncludes().get(0).getTypeName();
		} else if (propType.getTypeName().contains("[]")) {
			return propType.getTypeName().substring(0, propType.getTypeName().length() - 2);
		} else {
			return propType.getTypeName();
		}
	}
	
	private static String getCheckCastInstrs(String type, String indexCount , String indentStr) {		
		return indentStr + type + " " + getCollectionTypedElemVar(indexCount) + " = null" + INSTR_DELIM
				+ indentStr + "if(!(" + getCollectionElemVar(indexCount) + " instanceof " + type + ")) {\n"
				+ indentStr + "\t" + "return" + INSTR_DELIM
				+ indentStr + METHOD_DELIM
				+ indentStr + getCollectionTypedElemVar(indexCount) + " = (" + type + ") " + getCollectionElemVar(indexCount) + INSTR_DELIM;
	}
	
	private static String getIndentByCollectionCount(int collectionCount) {
		String indent = INITIAL_INDENT;
		for (int col = 0; col < collectionCount; col++) {
			indent += "\t";
		}
		return indent;
	}
	
	private static String setPrefetchingAccessInstr(boolean value) {
		return "es.bsc.dataclay.DataClayObject.setPrefetchingAccess(" + value + ")";
	}
	
	private static String getStartTimeInstr(String methodSignature, String indentStr) {
		return "long startTime = System.currentTimeMillis()" + INSTR_DELIM
				+ indentStr + "es.bsc.dataclay.DataClayObject.logger.debug(\"[==PREFETCHING==] Prefetching method '" + methodSignature + METHOD_NAME_SUFFIX 
				+ " started at: \" + new Date(startTime).toString())";
	}
	
	private static String getEndTimeInstr(String methodSignature, String indentStr) {
		return "long estimatedTime = System.currentTimeMillis() - startTime" + INSTR_DELIM
				+ indentStr + "es.bsc.dataclay.DataClayObject.logger.debug(\"[==PREFETCHING==] Prefetching method '" + methodSignature + METHOD_NAME_SUFFIX
				+ " ended: \" + estimatedTime + \" millis.\")";
	}

	private static void saveCodeToFile(final String code, final String filePath) {
		File file = new File(filePath + ".java");
		file.getParentFile().mkdirs();
		try (PrintWriter out = new PrintWriter(file)) {
			out.println(code);
		} catch (FileNotFoundException e) {
			PrefetchingSpecGenerator.logger.debug("[==CodeCreationUtils==] Error! Unable to save prefetching class: " + filePath);
			e.printStackTrace();
		}
	}

	// Returns 1 if it's Array or 2 if it's Collection
	private static int isCollectionType(final Type propType) {
		// 1. Check if the type is String
		if (getCollectionElemType(propType).equals("java.lang.String")) {
			return -1;
		}
		
		// 2. Check if it's Array
		if (propType.getTypeName().contains("[]")) {
			return 1;
		}
		
		// 3. Check if it's Collection
		Class<?> c = null;
		try {
			c = Class.forName(propType.getTypeName());
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			// Do Nothing. If class not found, it does not affect the running of the program.
		}
		
		if (c != null && Collection.class.isAssignableFrom(c)) {
			return 2;
		}
		
		return -1;
	}
}
