
package es.bsc.dataclay.util.tools.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.logic.classmgr.bytecode.java.SerializationCodeGenerator;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.events.listeners.ECA;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.classmgr.java.JavaOperationInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaPropertyInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.yaml.CommonYAML;
import storage.StorageObject;

/**
 * DataClay class analyzer.
 */
public final class DataClayClassVisitor extends ClassVisitor {

	/** ECA signature used during the analysis. */
	public static final String ECA_SIGNATURE = Reflector.getSignatureFromTypeName(ECA.class.getName());

	/** MetaClass representing the analyzed class. */
	private final MetaClass metaClass;

	/** Name of class file being analyzed. */
	private final String classFileName;

	/** Destination class name. */
	private final String destClassName;

	/** Dependencies found while analyzing this class. */
	private final Set<String> dependencies = new HashSet<>();

	/** Class loader containing the class to analyze. */
	private final ClassLoader classLoader;

	/** Current property position. */
	private int curPropertyPosition = 0;

	/** Indicates if class being analyzed is for an enrichment. */
	private final boolean isEnrichment;

	/** Signature of java interface methods to control enrichments of interface methods. */
	private List<String> interfaceMethodDesc;

	/**
	 * DataClay Class analyzer constructor
	 * 
	 * @param themetaClass
	 *            MetaClass in which to store information analyzed.
	 * @param newclassFileName
	 *            Name of class file being analyzed.
	 * @param newdestClassName
	 *            Destination class name (can be different from original in case of enrichment)
	 * @param userClassLoader
	 *            Class loader containing the class to analyze.
	 * @param newisEnrichment
	 *            Indicates if class being analyzed is for an enrichment.
	 */
	public DataClayClassVisitor(final MetaClass themetaClass,
			final String newclassFileName,
			final String newdestClassName,
			final ClassLoader userClassLoader,
			final boolean newisEnrichment) {
		super(Opcodes.ASM5);
		this.classFileName = newclassFileName;
		this.destClassName = newdestClassName;
		this.classLoader = userClassLoader;
		this.isEnrichment = newisEnrichment;
		this.metaClass = themetaClass;

		curPropertyPosition++;
	}

	/**
	 * Analyze signature and return dependencies (internal names).
	 * 
	 * @param desc
	 *            Descriptor to analyze
	 * @param signature
	 *            Signature to analyze.
	 * @return Dependencies internal names found.
	 */
	private List<String> getDependenciesInternalNamesFromSignature(final String desc,
			final String signature) {
		String signatureToAnalyze = signature;
		if (signature == null) {
			signatureToAnalyze = desc;
		}
		final DataClaySignatureVisitor sv = new DataClaySignatureVisitor();
		final SignatureReader sr = new SignatureReader(signatureToAnalyze);
		sr.accept(sv);
		return sv.getDependencies();
	}

	/**
	 * Get class namespace
	 * 
	 * @param className
	 *            Name of class
	 * @return Namespace of class
	 */
	public String getClassNamespace(final String className) {
		final boolean isStub = Reflector.isStub(className, classLoader);
		String typeNamespace = metaClass.getNamespace(); // If not stub, new class will be in same
		// namespace than the class being registered.
		if (isStub) {
			try {
				// final StubInfo stubInfoSpec = DataClayObject.getStubInfoFromClass(className);
				final Class<?> currentClass;
				try {
					currentClass = classLoader.loadClass(className);
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}

				final Yaml yaml = CommonYAML.getYamlObject();
				final URL classURL = currentClass.getProtectionDomain().getCodeSource().getLocation();
				final String path = className.replace(".", "/") + "Yaml.yaml";
				final String fileName = classURL.getPath() + path;
				final InputStream ios = new FileInputStream(new File(fileName));
				final StubInfo stubInfoSpec = (StubInfo) yaml.load(ios);
				typeNamespace = stubInfoSpec.getNamespace();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return typeNamespace;
	}

	/**
	 * Get type specification from signature and descriptor
	 * 
	 * @param descriptor
	 *            Descriptor of the type (main type in the signature i.e. without generics!)
	 * @param signature
	 *            Signature of the type being analyzed
	 * @return Type specification. NOTE: The namespace of the type is the same as the class being registered until a later
	 *         analysis modifies it.
	 */
	private Type getTypeSpec(final String descriptor, final String signature) {

		// ===== ANALYZE DEPENDENCIES ==== //
		final List<String> dependenciesOfType = this.getDependenciesInternalNamesFromSignature(descriptor, signature);
		final Type result = getTypeSpecGivenDependencies(descriptor, signature, dependenciesOfType);
		return result;
	}

	/**
	 * Get type specification from signature and descriptor and given dependencies found. This function is used from parameter
	 * analysis and getTypeSpec.
	 * 
	 * @param descriptor
	 *            Descriptor of the type
	 * @param signature
	 *            Signature of the type
	 * @param thedependencies
	 *            Dependencies of the signature
	 * @return Type specification. NOTE: The namespace of the type is the same as the class being registered until a later
	 *         analysis modifies it.
	 */
	private Type getTypeSpecGivenDependencies(final String descriptor, final String signature,
			final List<String> thedependencies) {
		final Set<Type> subIncludes = new HashSet<>();
		for (final String dependencyInternalName : thedependencies) {
			// Get signature
			final String subSignature = Reflector.getSignatureFromInternalName(dependencyInternalName);

			// We do not want to include as sub-includes the main type and not subtypes with same signature
			if (subSignature.equals(signature) || subSignature.equals(descriptor)) {
				continue;
			}

			// since we already analyzed the signature, we should call 'givenDependencies' function
			final Type subInclude = getTypeSpecGivenDependencies(subSignature, subSignature, new ArrayList<String>());
			subIncludes.add(subInclude);
		}

		// Get type
		Type typeSpec = null;

		// Uses descriptor to check since we want to check if 'main type' is java, array or primitive.
		if (JavaSpecGenerator.DC_CLASSES.contains(Reflector.getTypeNameFromSignatureOrDescriptor(descriptor))
				|| Reflector.isJavaPrimitiveOrArraySignature(descriptor)) {
			// ====== LANGUAGE OR DATACLAY TYPE ===== //
			typeSpec = new Type(descriptor, signature, Reflector.getTypeNameFromSignatureOrDescriptor(descriptor),
					new ArrayList<>(subIncludes));
		} else {
			// ====== USER TYPE ===== //

			// Check if class is an stub (uses old signature to check files in class loader)
			// uses descriptor since we want the type name and generics in signature will not allow us to
			// extract the main type name.
			final String typeName = Reflector.getTypeNameFromSignatureOrDescriptor(descriptor);
			final String typeNamespace = getClassNamespace(typeName);

			// Generate spec
			typeSpec = new UserType(typeNamespace, typeName, descriptor, signature,
					new ArrayList<>(subIncludes));

			// Add dependency
			dependencies.add(typeName);
		}

		return typeSpec;

	}

	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName, final String[] interfaces) {
		final String superTypeName = Reflector.getTypeNameFromInternalName(superName);

		// ==== PARENT TYPE ==== //
		// No parent if class extends DataClayObject since it is useful for registering DataClay functionalities.
		if (!superTypeName.equals(DataClayObject.class.getName())
				&& !superTypeName.equals(Object.class.getName())
				&& !superTypeName.equals(StorageObject.class.getName())) {
			// Parent class will be analyzed later
			dependencies.add(superTypeName);
			// Get signature from parent name
			final String parentSignature = Reflector.getSignatureFromTypeName(superTypeName);
			final String parentNamespace = this.getClassNamespace(superTypeName);
			final UserType parentSpec = new UserType(parentNamespace, superTypeName,
					parentSignature, parentSignature, null);
			metaClass.setParentType(parentSpec);
		}

		// == JAVA INTEFACES == //
		final List<String> javaIfaces = new ArrayList<>();
		for (final String ifaceName : interfaces) {
			if (Reflector.isJavaTypeName(ifaceName)) {
				javaIfaces.add(ifaceName);
			}
		}
		final String[] finalJavaIfaces = javaIfaces.toArray(new String[] {});
		metaClass.getJavaClassInfo().setJavaParentInterfaces(finalJavaIfaces);
		metaClass.getJavaClassInfo().setModifier(access);

		
		// Save java interface methods so they cannot be enriched
		interfaceMethodDesc = new ArrayList<>();
		try {
			for (final String ifaceInternalName : interfaces) {
				final String ifaceName = Reflector.getTypeNameFromInternalName(ifaceInternalName);
				final Class<?> interfaceClass = classLoader.loadClass(ifaceName);
				for (final Method m : interfaceClass.getMethods()) {
					final String ifaceSignature = Reflector.getSignatureFromMethod(m);
					interfaceMethodDesc.add(m.getName() + ifaceSignature);
				}
			}
		} catch (final ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public FieldVisitor visitField(final int access, final String name, final String desc,
			final String signature, final Object value) {

		// ==== ECA Fields ===== //
		if (desc.equals(ECA_SIGNATURE)) {
			if (!Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
				return null; // do nothing
			}
			if (!Modifier.isStatic(access) || !Modifier.isFinal(access)) {
				throw new UnsupportedOperationException("Aborting. " + metaClass.getName() + "." + name
						+ " field is an ECA field that is not static final."
						+ ". All ECAs defined should be static final fields.");
			}

			// Read ECA field and add it to list of ecas.
			try {
				final Class<?> javaClass = classLoader.loadClass(classFileName);
				final Field f = javaClass.getField(name);
				f.setAccessible(true);
				final ECA eca = (ECA) f.get(null);

				// Add eca
				metaClass.getEcas().add(eca);
			} catch (final Exception e) {
				e.printStackTrace();
			}

			return null;
		} else {
			// ==== Normal Fields ===== //
			if (Modifier.isStatic(access) && !Modifier.isFinal(access)) {
				throw new UnsupportedOperationException("Aborting. " + metaClass.getName() + "." + name
						+ " is a non-final static field. Currently only static final fields are supported.");

			}

			final Type propertyTypeSpec = getTypeSpec(desc, signature);
			// Note that <K> type or any generic type is included in the signature of the Type

			final Property prop = new Property(curPropertyPosition, name, propertyTypeSpec,
					metaClass.getNamespace(), destClassName);
			final JavaPropertyInfo javaPropInfo = new JavaPropertyInfo(access);
			prop.addLanguageDepInfo(javaPropInfo);
			metaClass.addProperty(prop, curPropertyPosition);
			curPropertyPosition++;

			return new DataClayFieldVisitor(prop);
		}
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {

		// Enrichments of constructors are not allowed.
		if ((name.equals("<init>") || name.equals("<clinit>")) && isEnrichment) {
			return null;
		}

		// Static methods are not suported
		/*
		 * if (Modifier.isStatic(access)) { if (!name.equals("<clinit>")) { throw new UnsupportedOperationException("Aborting. "
		 * + name + " is static. Static methods " + "are currently not supported"); } }
		 */

		// Check if method has same serialize signature
		if (name.equals(SerializationCodeGenerator.SERIALIZE_NAME)) {
			if (!desc.equals(SerializationCodeGenerator.SERIALIZE_DESCRIPTOR)) {
				System.err.println("[dataClay] Warning: Found 'serialize' method that does not match "
						+ " the arguments of a DataClay user-defined serialization. This method is being "
						+ " registered as a normal method. ");
			}
		}

		// Check if method has same deserialize signature
		if (name.equals(SerializationCodeGenerator.DESERIALIZE_NAME)) {
			if (!desc.equals(SerializationCodeGenerator.DESERIALIZE_DESCRIPTOR)) {
				System.err.println("[dataClay] Warning: Found 'deserialize' method that does not match "
						+ " the arguments of a DataClay user-defined deserialization. This method is being "
						+ " registered as a normal method. ");
			}
		}

		// Enrichment of interface methods are not allowed.
		final String methodNameAndDesc = name + desc;
		if (interfaceMethodDesc.contains(methodNameAndDesc) && isEnrichment) {
			throw new UnsupportedOperationException("Aborting. " + name + " implements an interface method. "
					+ " Enrichment of interface methods are currently not supported.");
		}

		// Enrichment of abstract methods not allowed
		if (isEnrichment && Modifier.isAbstract(access)) {
			throw new UnsupportedOperationException("Aborting. Cannot use an abstract method for enrichment "
					+ name);
		}

		// Operation
		final boolean isAbstract = Modifier.isAbstract(access);
		final Operation operation = new Operation(name, desc, signature,
				methodNameAndDesc,
				metaClass.getNamespace(), destClassName, isAbstract);
		metaClass.addOperation(operation);

		// Add java operation information
		final JavaOperationInfo javaOpInfo = new JavaOperationInfo(access);
		operation.addLanguageDepInfo(javaOpInfo);

		// Get parameter's type and order (parameter name is modified during method analysis)
		final org.objectweb.asm.Type[] argumentTypes = org.objectweb.asm.Type.getArgumentTypes(desc);
		for (int i = 0; i < argumentTypes.length; ++i) {
			final Type type = this.getTypeSpec(argumentTypes[i].getDescriptor(), null);
			operation.addParam("param" + i, type);
		}

		final org.objectweb.asm.Type returnType = org.objectweb.asm.Type.getReturnType(desc);
		final Type type = this.getTypeSpec(returnType.getDescriptor(), null);
		operation.setReturnType(type);

		// Rename method
		final DataClayMethodVisitor methodVisitor = new DataClayMethodVisitor(this, operation,
				classLoader);

		// Analyze code
		return methodVisitor;
	}

	/**
	 * Get metaClass
	 * 
	 * @return the metaClass
	 */
	public MetaClass getMetaClass() {
		return metaClass;
	}

	/**
	 * Get classFileName
	 * 
	 * @return the classFileName
	 */
	public String getClassFileName() {
		return classFileName;
	}

	/**
	 * Get destClassName
	 * 
	 * @return the destClassName
	 */
	public String getDestClassName() {
		return destClassName;
	}

	/**
	 * Get dependencies
	 * 
	 * @return the dependencies
	 */
	public Set<String> getDependencies() {
		return dependencies;
	}

	/**
	 * Get classLoader
	 * 
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * Get isEnrichment
	 * 
	 * @return the isEnrichment
	 */
	public boolean isEnrichment() {
		return isEnrichment;
	}

}
