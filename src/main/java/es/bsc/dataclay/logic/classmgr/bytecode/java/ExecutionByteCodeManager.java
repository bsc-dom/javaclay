
package es.bsc.dataclay.logic.classmgr.bytecode.java;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.logic.classmgr.bytecode.java.headers.ClassHeaderTransformer;
import es.bsc.dataclay.logic.classmgr.bytecode.java.writer.DataClayClassWriter;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * This class is responsible to generate the ExecutionClass.
 */
public final class ExecutionByteCodeManager {

	/**
	 * Default constructor for this Utility Class.
	 */
	private ExecutionByteCodeManager() {

	}

	/**
	 * Generates a execution class corresponding to the MetaClass provided using the Stub info specified.
	 * @param metaclass
	 *            MetaClass from which to generate the stub
	 * @param realParentName
	 *            Name of the parent class, including its namespace or DataClayObject if needed.
	 * @param classMgrRef
	 *            Reference to class manager
	 * @param accessedClasses
	 *            List of accessed classes from this class. Used to modify symbols.
	 * @return An array of bytes representing the stub of the MetaClass provided.
	 */
	public static synchronized byte[] generateExecutionClass(final MetaClass metaclass,
			final String realParentName,
			final ClassManager classMgrRef, final List<MetaClass> accessedClasses) {

		// Prepare a map of includes for the writer just in case
		final Map<String, MetaClass> allIncludes = new HashMap<>();
		// Prepare renaming CLASSNAME -> NAMESPACE+CLASSNAME
		final Map<String, String> renaming = new HashMap<>();
		// TODO: Use includes, but we could think in a better way to avoid using them? Using a Visitor?
		for (final MetaClass accessedClass : accessedClasses) {
			final String accClassNameInternal = Reflector.getInternalNameFromTypeName(accessedClass.getName());
			final String newClassName = accessedClass.getNamespace() + "." + accessedClass.getName();
			final String newClassNameInternal = Reflector.getInternalNameFromTypeName(newClassName);
			renaming.put(accClassNameInternal, newClassNameInternal);
			allIncludes.put(accessedClass.getNamespace() + "." + accessedClass.getName(), accessedClass);
		} 
		
		

		// Add the metaclass as well
		final String accClassNameInternal = Reflector.getInternalNameFromTypeName(metaclass.getName());
		final String newClassName = metaclass.getNamespace() + "." + metaclass.getName();
		final String newClassNameInternal = Reflector.getInternalNameFromTypeName(newClassName);
		renaming.put(accClassNameInternal, newClassNameInternal);

		final boolean parentIsObject = metaclass.getParentType() == null;
		final String classDesc = Reflector.getDescriptorFromTypeName(metaclass.getName());

		// Write new bytecode
		final DataClayClassWriter classWriter = new DataClayClassWriter(ClassWriter.COMPUTE_FRAMES, classMgrRef,
				allIncludes, true);
		final ClassRemapper remapper = new ClassRemapper(classWriter, new SimpleRemapper(renaming));
		
		if (Modifier.isInterface(metaclass.getJavaClassInfo().getModifier())) { 
			final ClassReader classReader = new ClassReader(metaclass.getJavaClassInfo().getClassByteCode());
			classReader.accept(remapper, ClassReader.EXPAND_FRAMES);
		} else {
			final DataClayClassTransformer execClassVisitor = new DataClayClassTransformer(remapper,
					metaclass, null, true);
			final ClassHeaderTransformer headerTransformer = new ClassHeaderTransformer(execClassVisitor,
					classDesc, true, parentIsObject, renaming);
			final ClassReader classReader = new ClassReader(metaclass.getJavaClassInfo().getClassByteCode());
			classReader.accept(headerTransformer, ClassReader.EXPAND_FRAMES);
		}
		
		final byte[] newByteCode = classWriter.toByteArray();

		return newByteCode;

	}

}
