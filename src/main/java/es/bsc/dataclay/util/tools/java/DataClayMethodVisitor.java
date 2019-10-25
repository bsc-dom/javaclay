
/**
 *
 */
package es.bsc.dataclay.util.tools.java;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Annotation;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.features.LanguageFeature;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;
import es.bsc.dataclay.util.management.classmgr.java.JavaAnnotationInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaImplementation;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * DataClay Method analyzer.
 */
public final class DataClayMethodVisitor extends MethodVisitor {

	/** Operation being created. */
	private final Operation operation;

	/** Java implementation. */
	private final JavaImplementation implementation;

	/** Class visitor. For using parsing utility functions. */
	private final DataClayClassVisitor cv;

	/** Current param index being analyzed. */
	private int curParamIdx = 0;

	/** Class loader containing the class to analyze. */
	private final ClassLoader classLoader;

	/**
	 * Constructs a new method visitor
	 * @param thecv
	 *            Class visitor. For using parsing utility functions.
	 * @param newoperation
	 *            Operation in which to add information of methods.
	 * @param newclassLoader
	 *            Class loader containing analyzed classes.
	 */
	public DataClayMethodVisitor(final DataClayClassVisitor thecv, final Operation newoperation,
			final ClassLoader newclassLoader) {
		super(Opcodes.ASM5);
		operation = newoperation;
		final int implNum = operation.getImplementations().size();
		implementation = new JavaImplementation(implNum, new ArrayList<AccessedProperty>(),
				new ArrayList<AccessedImplementation>(), new ArrayList<Type>(),
				null, new HashMap<FeatureType, QuantitativeFeature>(),
				new HashMap<FeatureType, QualitativeFeature>(),
				operation.getNamespace(), operation.getClassName(), operation.getNameAndDescriptor());
		implementation.getRequiredQualitativeFeatures().put(FeatureType.LANGUAGE,
				new LanguageFeature("Java", "1.7"));
		operation.addImplementation(implementation);
		cv = thecv;
		classLoader = newclassLoader;
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		newAccessedProperty(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc) {
		newAccessedImplementation(opcode, owner, name, desc);
	}

	/**
	 * Get the number of a stub method identified by signature provided and specified in stub information.
	 * @param className
	 *            Name of class
	 * @param opNameAndSignature
	 *            Name and Signature of the operation
	 * @return the number of a stub method identified by signature provided and specified in stub information.
	 */
	public int getStubMethodNumber(final String className, final String opNameAndSignature) {
		// TODO: Revie this for ENRICHMENT stuff (jmarti 2018)
		// final boolean isStub = Reflector.isStub(className, classLoader);
		final int id = 0;
		// if (isStub) {

		// First check if it is present in current metaClass
		// StubInfo stubInfoSpec = DataClayObject.getStubInfoFromClass(className);
		// id = stubInfoSpec.getImplementationByNameAndSignature(opNameAndSignature).getImplPosition();
		// }
		return id;
	}

	/**
	 * Creates new accessed property
	 * @param opcode
	 *            Opcode
	 * @param owner
	 *            Owner class
	 * @param name
	 *            Method name
	 * @param desc
	 *            Method descriptor
	 */
	private void newAccessedProperty(final int opcode, final String owner,
			final String name, final String desc) {

		// Accessed property
		final String typeName = Reflector.getTypeNameFromInternalName(owner);
		if (!Reflector.isJavaTypeName(typeName)) {
			final String actualTypeName = getClassWithProperty(typeName, name);
			if (JavaSpecGenerator.DC_CLASSES.contains(actualTypeName)
					|| desc.equals(DataClayClassVisitor.ECA_SIGNATURE)) {
				// Ignore accesses to DataClay properties
				return;
			}
			final String typeNamespace = cv.getClassNamespace(actualTypeName);
			final AccessedProperty accProperty = new AccessedProperty(typeNamespace, actualTypeName, name);
			implementation.getAccessedProperties().add(accProperty);
			// Dependencies
			if (!Reflector.isJavaPrimitiveOrArrayTypeName(actualTypeName)) {
				cv.getDependencies().add(actualTypeName);
			}
		}

	}

	/**
	 * Creates new accessed implementation
	 * @param opcode
	 *            Opcode
	 * @param owner
	 *            Owner class
	 * @param name
	 *            Method name
	 * @param desc
	 *            Method descriptor
	 */
	private void newAccessedImplementation(final int opcode, final String owner,
			final String name, final String desc) {

		// Accessed implementation
		final String typeName = Reflector.getTypeNameFromInternalName(owner);
		if (!Reflector.isJavaTypeName(typeName) && !Reflector.isArrayTypeName(typeName)) {
			final String actualTypeName = getClassWithMethod(typeName, name, desc);
			if (JavaSpecGenerator.DC_CLASSES.contains(actualTypeName)) {
				// Ignore accesses to DataClay methods (makePersistent, ...)
				return;
			}
			final String typeNamespace = cv.getClassNamespace(actualTypeName);

			final int actualPos = this.getStubMethodNumber(actualTypeName, name + desc);

			final AccessedImplementation accImplementation = new AccessedImplementation(typeNamespace,
					actualTypeName, name + desc, actualPos);
			implementation.getAccessedImplementations().add(accImplementation);
			// Dependencies
			if (!Reflector.isJavaPrimitiveOrArrayTypeName(actualTypeName)) {
				cv.getDependencies().add(actualTypeName);
			}
		}

	}

	/**
	 * Get class containing method with name and descriptor provided
	 * @param classToAnalyze
	 *            Name of the class
	 * @param methodName
	 *            Method name
	 * @param methodDesc
	 *            Method descriptor
	 * @return Name of the class containing method.
	 */
	private String getClassWithMethod(final String classToAnalyze, final String methodName,
			final String methodDesc) {
		String classWithMethod = classToAnalyze;
		try {
			final org.objectweb.asm.Type[] paramTypes = org.objectweb.asm.Type.getArgumentTypes(methodDesc);
			final Class<?>[] javaClassParams = new Class<?>[paramTypes.length];
			for (int i = 0; i < paramTypes.length; ++i) {
				javaClassParams[i] = Reflector.getClassFromSignatureAndArray(paramTypes[i].getDescriptor(),
						classLoader);
			}
			// Check if method is in current class
			Class<?> currentClass = classLoader.loadClass(classToAnalyze);
			boolean found = false;
			while (!found && currentClass != null) {
				try {
					if (methodName.equals("<init>")) {
						currentClass.getConstructor(javaClassParams);
					} else {
						currentClass.getDeclaredMethod(methodName, javaClassParams);
					}
					classWithMethod = currentClass.getName();
					found = true;
				} catch (final NoSuchMethodException e) {
					currentClass = currentClass.getSuperclass();
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return classWithMethod;
	}

	/**
	 * Get class containing property with name provided
	 * @param classToAnalyze
	 *            Name of the class
	 * @param propertyName
	 *            Property name
	 * @return Name of the class containing property.
	 */
	private String getClassWithProperty(final String classToAnalyze, final String propertyName) {
		String classWithMethod = classToAnalyze;
		try {
			// Check if method is in current class
			Class<?> currentClass = classLoader.loadClass(classToAnalyze);
			boolean found = false;
			while (!found && currentClass != null) {
				try {
					currentClass.getDeclaredField(propertyName);
					classWithMethod = currentClass.getName();
					found = true;
				} catch (final NoSuchFieldException e) {
					currentClass = currentClass.getSuperclass();
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return classWithMethod;
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
		newAccessedImplementation(opcode, owner, name, desc);
	}

	@Override
	public void visitParameter(final String name, final int access) {
		// Since parameters are analyzed in order, we can modify names in Operation args specified
		// during class visitor.
		final String paramPrevName = "param" + curParamIdx;
		final Type paramType = operation.getParams().get(paramPrevName);
		operation.getParams().remove(paramPrevName);
		operation.getParams().put(name, paramType);
		operation.getParamsOrder().set(curParamIdx, name);
		curParamIdx++;
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
		final Annotation annotation = new Annotation(desc);

		final JavaAnnotationInfo langInfo = new JavaAnnotationInfo(visible);
		annotation.addLanguageDepInfo(langInfo);

		operation.addAnnotation(annotation);

		return new DataClayAnnotationVisitor(annotation);
	}
}
