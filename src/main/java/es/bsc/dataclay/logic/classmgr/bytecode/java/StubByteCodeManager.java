
package es.bsc.dataclay.logic.classmgr.bytecode.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.logic.classmgr.bytecode.java.headers.ClassHeaderTransformer;
import es.bsc.dataclay.logic.classmgr.bytecode.java.writer.DataClayClassWriter;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import javassist.Modifier;

/**
 * This class is responsible to manage bytecode of stubs (create stubs).
 * 
 */
public final class StubByteCodeManager {

	/** Tab. */
	private static final String TAB = "    ";

	/** Line separator. */
	private static final String LINE_SEP = System.getProperty("line.separator");

	/**
	 * Default constructor for this Utility Class.
	 */
	private StubByteCodeManager() {

	}

	/**
	 * Generates a Java bytecode stub corresponding to the MetaClass provided using the Stub info specified.
	 * @param metaclass
	 *            MetaClass from which to generate the stub
	 * @param mClassStubInfo
	 *            Information the stub must have
	 * @param classMgrRef
	 *            Reference to class manager
	 * @param accessedClasses Accessed classes of the class
	 * @return An array of bytes representing the stub of the MetaClass provided.
	 */
	public static synchronized byte[] generateJavaStub(final MetaClass metaclass, 
			final StubInfo mClassStubInfo, 
			final List<MetaClass> accessedClasses, 
			final ClassManager classMgrRef) {

		// Prepare a map of includes for the writer just in case 
		final Map<String, MetaClass> allIncludes = new HashMap<>();
		for (final MetaClass accClass : accessedClasses) { 
			allIncludes.put(accClass.getName(), accClass);
		}
		final boolean parentIsObject = metaclass.getParentType() == null;
		final String classDesc = Reflector.getDescriptorFromTypeName(metaclass.getName());
		
		// Write new bytecode of stub 
		final DataClayClassWriter classWriter = new DataClayClassWriter(ClassWriter.COMPUTE_FRAMES, 
				classMgrRef, allIncludes, false);
		
		if (Modifier.isInterface(metaclass.getJavaClassInfo().getModifier())) { 
			final ClassReader classReader = new ClassReader(metaclass.getJavaClassInfo().getClassByteCode());
			classReader.accept(classWriter, ClassReader.EXPAND_FRAMES);		
		} else {
			final DataClayClassTransformer stubClassVisitor = new DataClayClassTransformer(classWriter,
					metaclass, mClassStubInfo, false);
			final ClassHeaderTransformer headerTransformer = new ClassHeaderTransformer(stubClassVisitor, 
					classDesc, false, parentIsObject);
			final ClassReader classReader = new ClassReader(metaclass.getJavaClassInfo().getClassByteCode());
			classReader.accept(headerTransformer, ClassReader.EXPAND_FRAMES);		
		}
		final byte[] newByteCode = classWriter.toByteArray();		
		
		return newByteCode;
	}

	/**
	 * Given a DataClay Type this function returns the namespace containing the type.
	 * If the type is a user type then NAMESPACE and a final "." is returned, an empty string otherwise.
	 * @param t		The DataClay Type
	 * @return	An string containing the type's namespace and a final dot, or an empty string otherwise.
	 */
	private static String getNamespaceFromType(final es.bsc.dataclay.util.management.classmgr.Type t) {
		String typename = "";
		final String desc = t.getDescriptor();
		if (desc.startsWith("[")) {
			// Array, get component type 
			final es.bsc.dataclay.util.management.classmgr.Type compType = t.getIncludes().iterator().next();
			if (compType instanceof UserType) {
				final UserType utype = (UserType) compType;
				final String propNamespace = utype.getNamespace();
				typename = propNamespace + ".";
			}
		} else {
			if (t instanceof UserType) {
				final UserType utype = (UserType) t;
				final String propNamespace = utype.getNamespace();
				typename = propNamespace + ".";
			}
		}
		return typename;
	}

	/**
	 * Generate stub aspect
	 * @param metaclass
	 *            Class of the stub
	 * @param forExec
	 *            Indicates if aspects are for execution environment
	 * @param classMgrRef
	 *            Reference to class Manager.
	 * @return Serialized string representing the aspect
	 */
	public static synchronized byte[] generateStubAspect(final MetaClass metaclass,
			final boolean forExec, final ClassManager classMgrRef) {
		final StringBuilder strBuffer = new StringBuilder();
		String className = metaclass.getName();
		if (forExec) {
			className = metaclass.getNamespace() + "." + metaclass.getName();
		}
		final String[] classPackages = className.split("\\.");
		String classPckage = null;
		String actualClassName = className;
		if (classPackages.length > 1) {
			classPckage = classPackages[0];
			actualClassName = classPackages[classPackages.length - 1];
		}
		for (int i = 1; i < classPackages.length - 1; ++i) {
			classPckage = classPckage + "." + classPackages[i];
		}
		if (classPckage != null) {
			strBuffer.append("package " + classPckage + ";" + LINE_SEP);
		}

		strBuffer.append("public aspect " + actualClassName + "Aspect {" + LINE_SEP);
		for (final Property prop : metaclass.getProperties()) {
			String typename = 
					Reflector.getTypeNameFromSignatureOrDescriptor(prop.getType().getDescriptor()).replace("$", ".");
			final String javaDesc = prop.getType().getDescriptor();
			if (forExec) { 
				typename = getNamespaceFromType(prop.getType()) + typename;
			}

			final String fieldName = prop.getName();
			final String fullFieldName = className + "." + fieldName;

			final String setField = ".$$set" + fieldName;
			final String getField = ".$$get" + fieldName;

			final String withinGetter = "!withincode(* " + className + getField + "()) ";
			final String withinSetter = "!withincode(void " + className + setField + "(..)) ";

			// Before set
			strBuffer.append(TAB + "pointcut " + fieldName + "SetPC(): " + withinSetter + " && !withincode(void " + className
					+ ".serialize*(..))" + " && !withincode(void " + className + ".deserialize*(..))" + " && set(* "
					+ " " + fullFieldName + ");" + LINE_SEP);
			strBuffer.append(TAB + "void around(" + className + " instance, " + typename + " newval): "
					+ fieldName + "SetPC() "
					+ "&& args(newval)  && target(instance) { ");
			strBuffer.append("instance" + setField + "(newval);" + LINE_SEP);

			if (forExec && !Reflector.isJavaPrimitiveOrArraySignature(javaDesc)
					&& !javaDesc.contains("[")) {
				strBuffer.append("instance.checkVolatile(newval);");
			}
			strBuffer.append("}" + LINE_SEP);

			// Before get
			strBuffer.append(TAB + "pointcut " + fieldName + "GetPC(): " + withinGetter + " && !withincode(void " + className
					+ ".serialize*(..))" + " && !withincode(void " + className + ".deserialize*(..))" + " && get(* "
					+ " " + fullFieldName + ");" + LINE_SEP);

			strBuffer.append(TAB);
			strBuffer.append(typename + " around(" + className + " instance): "
					+ fieldName + "GetPC() " + " && target(instance) { ");
			strBuffer.append(typename + " val = instance" + getField + "(); ");
			strBuffer.append("return val; ");
			strBuffer.append("}" + LINE_SEP);

		}
		strBuffer.append("}" + LINE_SEP);
		final String aspect = strBuffer.toString();
		final byte[] aspectBytes = aspect.getBytes();
		return aspectBytes;

	}
}
