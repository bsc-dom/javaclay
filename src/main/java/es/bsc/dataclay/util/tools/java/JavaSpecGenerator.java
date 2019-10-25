
/**
 * @file JavaSpecGenerator.java
 * @date Oct 23, 2012
 */

package es.bsc.dataclay.util.tools.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.java.JavaClassInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * This class contains utility functions for the generation of Specifications given Java classes.
 *
 */
public final class JavaSpecGenerator {

	/** Class path of the user. */
	private final String classPath;
	/** Current class loader. */
	private final ClassLoader regClassLoader;

	// CHECKSTYLE:OFF
	/** List of DataClay classes to skip. */
	public static final List<String> DC_CLASSES = new ArrayList<>();
	// CHECKSTYLE:ON
	
	static { 

		// read from dataclay_classnames
		final ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.dataclay_classnames");
		for (final String key : props.keySet()) {
			DC_CLASSES.add(key);
		}
		
		
		
	}

	/**
	 * JavaSpecGenerator constructor.
	 * @param newclassPath
	 *            Class path of the user.
	 */
	public JavaSpecGenerator(final String newclassPath) {
		super();
		this.classPath = newclassPath;

		final String[] classPaths = classPath.split(":");
		final URL[] classUrls = new URL[classPaths.length];
		for (int i = 0; i < classPaths.length; ++i) {
			// Ignore jars
			final File classPathFiles = new File(classPaths[i]);
			try {
				classUrls[i] = classPathFiles.toURI().toURL();
			} catch (final MalformedURLException e) {
				e.printStackTrace();
			}
		}

		regClassLoader = new URLClassLoader(classUrls);
		

		// Add all JAR classes
		/*
		 * String[] classPaths = classPath.split(":"); for (int i = 0; i < classPaths.length; ++i) { if (classPaths[i].endsWith(".jar"))
		 * { List<String> classesInJar; try { classesInJar = JarUtils.getAllClassesInJar(classPaths[i]);
		 * DC_CLASSES.addAll(classesInJar); } catch (Exception e) { e.printStackTrace(); } } }
		 */
	}

	/**
	 * Method that generates a MetaClassSpec from a Java Class with the name specified in the class path provided
	 * @param namespace
	 *            Namespace in which to register enrichment
	 * @param className
	 *            Name of the class to register
	 * @return The specification of the new class
	 */
	public Map<String, MetaClass> generateMetaClassSpecForRegisterClass(final String namespace,
			final String className) {
		final Map<String, String> renaming = new HashMap<>();
		return generateMetaClassSpec(namespace, className, className, renaming, false);
	}

	/**
	 * Method that generates a MetaClassSpec from a Java Class with the name specified in the class path provided and
	 *        modifies the symbols in operations, properties and implementations to use the class name provided.
	 * @param namespace
	 *            Namespace in which to register enrichment
	 * @param className
	 *            Name of the class to register
	 * @param classNameToBeEnriched
	 *            Name of the class to be enriched (needs to be provided since it is used for replacing the symbols)
	 * @return The specification of the new class
	 */
	public Map<String, MetaClass> generateMetaClassSpecForEnrichment(final String namespace,
			final String className,
			final String classNameToBeEnriched) {
		final Map<String, String> renaming = new HashMap<>();
		renaming.put(Reflector.getInternalNameFromTypeName(className),
				Reflector.getInternalNameFromTypeName(classNameToBeEnriched));
		// MetaClasses created should have destination class name and destination namespace
		final StubInfo stubInfoSpec = DataClayObject.getStubInfoFromClass(classNameToBeEnriched);
		final String originalNamespace = stubInfoSpec.getNamespace();
		return generateMetaClassSpec(originalNamespace, className, classNameToBeEnriched, renaming, true);
	}

	/**
	 * Generate MetaClass specifications for class with name provided.
	 * @param namespace
	 *            Namespace of the class file
	 * @param className
	 *            Name of class in class loader
	 * @param destClassName
	 *            Name of destination class
	 * @param renaming
	 *            Renaming map (used for enrichments)
	 * @param isEnrichment
	 *            Indicates if is enrichment
	 * @return The specifications of the new classes
	 */
	public Map<String, MetaClass> generateMetaClassSpec(final String namespace, final String className,
			final String destClassName,
			final Map<String, String> renaming, final boolean isEnrichment) {
		final Map<String, MetaClass> specs = new LinkedHashMap<>();
		try {
			// Get metaClassSpec
			final Set<String> dependencies = new HashSet<>();
			final MetaClass spec = generateMetaClassSpecInternal(namespace, className,
					destClassName, renaming, isEnrichment, dependencies);
			if (spec == null) {
				return specs;
			}
			specs.put(destClassName, spec);

			// Analyze dependencies.
			final Set<String> allDependenciesAnalyzed = new HashSet<>();
			allDependenciesAnalyzed.add(destClassName);
			// Include all dataClayClasses that must not be analyzed
			allDependenciesAnalyzed.addAll(DC_CLASSES);

			// Since we cannot modify a collection while iterating it, we save new dependencies found
			// into another structure.
			Set<String> currentDependencies = new HashSet<>(dependencies);
			// Discard already analyzed dependencies
			currentDependencies.removeAll(allDependenciesAnalyzed);
			while (!currentDependencies.isEmpty()) {

				final Set<String> foundDependencies = new HashSet<>();
				for (final String dependency : currentDependencies) {
					allDependenciesAnalyzed.add(dependency);
					// Check if dependency is a stub file
					if (Reflector.isStub(dependency, regClassLoader)) {
						continue;
					}

					final MetaClass depSpec = generateMetaClassSpecInternal(namespace, dependency, dependency, renaming,
							isEnrichment, foundDependencies);
					specs.put(dependency, depSpec);
				}
				currentDependencies = new HashSet<>(foundDependencies);
				// Discard already analyzed dependencies
				currentDependencies.removeAll(allDependenciesAnalyzed);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return specs;
	}

	/**
	 * Generates a MetaClass Specification by getting the class from the context class loader. This function is called
	 *        recursively to generate associated MetaClassSpecs.
	 *
	 * @param namespace
	 *            Namespace in which class will be created
	 * @param originalClassName
	 *            Name of the class
	 * @param destClassName
	 *            Name of the class already registered (in case of enrichment)
	 * @param renaming
	 *            Map of name class file -> name registered class used for multiple enrichment.
	 * @param isEnrichment
	 *            Indicates if the MetaClassSpec is for enrichment. In case of enrichment, no superclass is analyzed and no
	 *            constructor is added.
	 * @param dependenciesFound
	 *            Where to store the dependencies found.
	 * @return MetaClass created
	 * @throws IOException
	 *             If class resource cannot be loaded.
	 */
	private MetaClass generateMetaClassSpecInternal(final String namespace,
			final String originalClassName, final String destClassName,
			final Map<String, String> renaming,
			final boolean isEnrichment,
			final Set<String> dependenciesFound) throws IOException {
		// Load class
		Class<?> clazz = null;
		try {
			clazz = regClassLoader.loadClass(originalClassName);
		} catch (final ClassNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		if (!isEnrichment && Reflector.isStub(originalClassName, regClassLoader)) {
			System.err.println("** " + originalClassName + " is a stub. Not registering.");
			return null;
		}

		// Create MetaClass
		final int classAccessFlags = clazz.getModifiers();

		final boolean isAbstract = Modifier.isAbstract(classAccessFlags);
		final MetaClass metaClass = new MetaClass(namespace, destClassName, null, isAbstract);
		final String signature = Reflector.getSignatureFromTypeName(originalClassName);

		// Rename class first
		final DataClayClassVisitor classVisitor = new DataClayClassVisitor(metaClass,
				originalClassName, destClassName, regClassLoader,
				isEnrichment);

		// Get resource bytes
		final String resourceName = "/" + originalClassName.replaceAll("\\.", "/") + ".class"; // Any better way?
		final InputStream classInputStream = clazz.getResourceAsStream(resourceName);
		final ClassReader classReader = new ClassReader(classInputStream);

		// Get bytecode
		final byte[] byteCode = new byte[classReader.b.length];
		System.arraycopy(classReader.b, 0, byteCode, 0, classReader.b.length);
		final JavaClassInfo javaClassInfo = new JavaClassInfo(signature, byteCode);
		metaClass.addLanguageDepInfo(javaClassInfo);

		// Analyze class
		final ClassRemapper remapper = new ClassRemapper(classVisitor, new SimpleRemapper(renaming));
		classReader.accept(remapper, ClassReader.EXPAND_FRAMES);

		// Get dependency
		dependenciesFound.addAll(classVisitor.getDependencies());

		return metaClass;
	}

}
