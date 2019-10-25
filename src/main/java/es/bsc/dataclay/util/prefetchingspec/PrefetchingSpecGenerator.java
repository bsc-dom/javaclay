
package es.bsc.dataclay.util.prefetchingspec;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.IR;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChain;
import es.bsc.dataclay.util.prefetchingspec.analysisscopes.MethodLevelAnalysisScope;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.PrefetchingElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements.FieldElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.parameterelements.ParameterElement;
import es.bsc.dataclay.util.prefetchingspec.utils.CodeCreationUtils;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingJavaMethod;

/**
 * 
 *
 */
public class PrefetchingSpecGenerator {

	/** Logger. */
	public static final Logger logger = LogManager.getLogger("PrefetchingLogger");

	/**
	 * Main method in this module. Generates the prefetching info, stores it in dataClay metaclasses and creates
	 * the prefetching methods that are called at runtime.
	 * 
	 * @param classPath
	 * 			Class path of classes to analyze
	 * @param metaClasses
	 * 			DataClay meta classes where the prefetching info is stored
	 * @param accountID
	 * 			DataClay account ID
	 * @param credentials
	 * 			DataClay credentials
	 */
	public static final void generateAndStorePrefetchingSpec(final Map<String, MetaClass> metaClasses, 
			final String classPath, final String srcPath, final String libPath) throws PrefetchingClassesNotGeneratedException {
		try {
			// 1. Copy classes to be registered to a new directory
			final String classesCopyPath = copySourceClasses(metaClasses.keySet(), srcPath, libPath);
			final String copySrcPath = classesCopyPath + File.separatorChar + "src";
			
			// 2. Generate prefetching info
			logger.debug("[==PerfetchingSpecGenerator==] Generating prefetching info started for classes in path: " + srcPath
					+ " using libraries in path: " + libPath);
			final SourceCodeAnalyzer prefetchSCA = new SourceCodeAnalyzer(copySrcPath, libPath, metaClasses.keySet());
			final Map<String, IR> prefetchingInfo = prefetchSCA.generatePrefetchingInfo();
			if (prefetchingInfo == null) {
				return;
			}
			
			// 3. Inject getters and load method into registered classes source
			final String copyBinPath = classesCopyPath + File.separatorChar + "bin";
			modifyAndCompileSourceClasses(metaClasses, copySrcPath, copyBinPath, libPath);
			
			// 4. Store prefetching info in meta classes
			storePrefetchingInfoInMetaClasses(prefetchingInfo, metaClasses);
			logger.debug("[==PerfetchingSpecGenerator==] Prefetching info stored in dataClay metaclasses.");
			
			// 5. Create prefetching classes and methods
			final PrefetchingClassesGenerator prefetchEG = new PrefetchingClassesGenerator(metaClasses, copyBinPath, libPath);
			prefetchEG.generateAndRegisterPrefetchingClasses();
			
			// 6. Delete copy of registered classes from client
			/*try {
				FileUtils.deleteDirectory(new File(classesCopyPath));
				logger.debug("[==PerfetchingSpecGenerator==] Prefetching class files deleted from client");
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			
		} catch (Exception e) {
			logger.debug("[==PerfetchingSpecGenerator==] Error! Generating prefetching specs failed");
			e.printStackTrace();
			throw new PrefetchingClassesNotGeneratedException(e);
		}
	}

	/**
	 * Creates a copy of the classes to register in the default temp directory.
	 * 
	 * @param classes
	 * 			list of class names to copy
	 * @param srcPath
	 * 			path to the source files of the classes
	 * @param libPath
	 * 			path to the libraries needed to compile the application
	 * @return
	 */
	private static String copySourceClasses(Set<String> classes, String srcPath, String libPath) {
		try {
			// Create source directory
			final Path classesCopyPath = Files.createTempDirectory("dataClayClassesCopy");
			final String copySrcPath = classesCopyPath.toString() + File.separatorChar + "src";
			new File (copySrcPath).mkdir();
			
			for (String className : classes) {
				// Copy class file (automatically creates needed directory if not found)
				FileUtils.copyFile(new File(srcPath + File.separatorChar + getSourcePathFromClassName(className)) , 
						new File(copySrcPath + File.separatorChar + getSourcePathFromClassName(className)));
			}
			
			return classesCopyPath.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Injects getters and load method needed by the prefetching classes into the classes to register and then compiles them.
	 * 
	 * @param metaClasses
	 * 			Meta classes to register
	 * @param srcPath
	 * 			path to the source files of the classes
	 * @param binPath
	 * 			path to the compiled classes
	 * @param libPath
	 * 			path to the libraries needed to compile the application
	 * @throws Exception
	 */
	private static void modifyAndCompileSourceClasses(final Map<String, MetaClass> metaClasses, final String srcPath, 
			final String binPath, final String libPath) throws Exception {
		final Map<String, MetaClass> originalMetaClasses = new HashMap<String, MetaClass>(metaClasses);
		metaClasses.clear();

		for (Entry<String, MetaClass> metaClassAndName : originalMetaClasses.entrySet()) {
			final MetaClass metaClass = metaClassAndName.getValue();
			/**** 1. Modify copied classes (inject getters for each field and load method) ****/
			String codeToInject = "";
			for (Property prop : metaClass.getProperties()) {
				if (prop.getName().contains("$")) {
					continue;
				}

				String propType = prop.getType().getTypeName();
				
				// Check if it's Collection
				Class<?> c = null;
				try {
					c = Class.forName(propType);
				} catch (ClassNotFoundException | NoClassDefFoundError e) {
					// Do Nothing. If class not found, it does not affect the running of the program.
				}
				if (c != null && Collection.class.isAssignableFrom(c)) {
					if (prop.getType().getIncludes() != null && prop.getType().getIncludes().size() > 0) {
						propType += "<" + prop.getType().getIncludes().get(0).getTypeName() + ">";
					}
				}
				
				codeToInject += "\n\n\tpublic " + propType + " " + CodeCreationUtils.getGetterInstr(prop.getName()) + " {"
						+ "\n\t\t return " + prop.getName() + ";"
						+ "\n\t}";
			}
			
			// Add load method for class
			codeToInject += "\n\n\tpublic void " + CodeCreationUtils.LOAD_METHOD + "() {"
					+ "\n\n\t}";
		
			// Write new methods to class .java file
			try {
				Path classSrcPath = Paths.get(srcPath + File.separatorChar + getSourcePathFromClassName(metaClass.getName()));
				List<String> lines = Files.readAllLines(classSrcPath, StandardCharsets.UTF_8);
				int lastClassLine = -1;
				for (int i = lines.size() - 1; i > 0; i--) {
					String line = lines.get(i);
					if (line.contains("}")) {
						lastClassLine = i;
						break;
					}
				}
				lines.add(lastClassLine, codeToInject);
				Files.write(classSrcPath, lines, StandardCharsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			/**** 2. Compile new modified copy of classes ****/
			new File (binPath).mkdir();
			
			// Prepare class path
			final HashSet<String> finalUrls = new HashSet<>();
			// Add libs to classPath
			String[] libs = WalaProperties.getJarsInDirectory(libPath);
			for (String lib : libs) {
				String absoluteLibPath = Paths.get(lib).toAbsolutePath().toString();
				finalUrls.add(absoluteLibPath);
			}
			final String[] cp = finalUrls.toArray(new String[] {});

			FileAndAspectsUtils.compileClasses(Paths.get(srcPath).toAbsolutePath().toString(), 
					Paths.get(binPath).toAbsolutePath().toString(), cp, null);

			/**** 3. Substitute original meta class with copied modified meta classes ****/
			final String metaClassName = metaClassAndName.getKey();
			// Generate specs
			final Set<String> classNames = new HashSet<>();
			classNames.add(metaClassName);
			final Map<String, MetaClass> copiedClassSpecs = ClientManagementLib.generateSpecs(
					metaClass.getNamespace(), binPath, classNames);
			
			// Add new class specs to meta classes
			metaClasses.put(metaClassName, copiedClassSpecs.get(metaClassName));
		}
	}
	
	/**
	 * Retrieves the source path of the class from its name (including packages).
	 * 
	 * @param className
	 * @return
	 */
	private static String getSourcePathFromClassName(final String className) {
		String classSrcPath = className;
		if (className.contains(".")) {
			classSrcPath = className.replace('.', File.separatorChar);
		}
		return classSrcPath + ".java";
	}
	
	/**
	 * Stores the generated prefetching info into the dataClay meta classes after performing a series of transformations.
	 * 
	 * @param prefetchingInfo
	 * 			Wala IR containing the generated prefetching info
	 * @param metaClasses
	 * 			dataClay meta classes to modify
	 * @return a new version of the meta classes
	 */
	private static Map<String, MetaClass> storePrefetchingInfoInMetaClasses(final Map<String, IR> prefetchingInfo,
			final Map<String, MetaClass> metaClasses) {
		for (IR methodIR : prefetchingInfo.values()) {
			final MethodLevelAnalysisScope scope = ((PrefetchingJavaMethod) methodIR.getMethod()).getAnalysisScope();
			final PrefetchingInformation dataClayPrefetchingInfo = new PrefetchingInformation();
			dataClayPrefetchingInfo.setDisableDynamicPrefetching(scope.isDisabledAtRuntime());

			// Get the class name of the analyzed method. Include the name of the packages if it exists.
			String className = methodIR.getMethod().getDeclaringClass().getName().getClassName().toString();
			if (methodIR.getMethod().getDeclaringClass().getName().getPackage() != null) {
				String packageName = methodIR.getMethod().getDeclaringClass().getName().getPackage().toString();
				packageName = packageName.replaceAll("/", ".");
				className = packageName + "." + className;
			}

			// Only look for the class to update in the new registered classes. If the class is already registered,
			// then it has already been updated with the prefetching info.
			final MetaClass newMetaClass = metaClasses.get(className);
			if (newMetaClass == null) {
				continue;
			}

			// Get default implementation of operation
			final String methodSignature = methodIR.getMethod().getSignature();
			final Implementation impl = getDefaultImplementationByOperationSignature(newMetaClass,
					methodSignature.substring(methodSignature.lastIndexOf(".") + 1));
			if (impl == null) {
				continue;
			}

			// Convert prefetching hints to property lists
			final List<List<Property>> propsToPrefetch = new ArrayList<List<Property>>();
			for (AccessChain chain : scope.removeInvokingMethodsChains()) {
				final ArrayList<Property> propsChain = new ArrayList<Property>();
				boolean chainBroken = false;
				for (PrefetchingElement prefetchingElem : chain.getElements()) {
					if (prefetchingElem instanceof ParameterElement) {
						chainBroken = true;
						break;
					}
					final FieldElement fieldAccessInstr = (FieldElement) prefetchingElem;
					final MetaClass fieldMetaClass = metaClasses.get(fieldAccessInstr.getFieldClassName());
					if (fieldMetaClass == null) {
						chainBroken = true;
						break;
					}
					final Property prop = getPropertyByName(fieldMetaClass, fieldAccessInstr.getName());
					if (prop == null) {
						chainBroken = true;
						break;
					}
					propsChain.add(prop);
				}

				if (!chainBroken) {
					propsToPrefetch.add(propsChain);
				}
			}

			dataClayPrefetchingInfo.setPropertiesToPrefetch(propsToPrefetch);
			impl.setPrefetchingInfo(dataClayPrefetchingInfo);
		}
		return metaClasses;
	}


	/************************/
	/**** HELPER METHODS ****/
	/************************/
	
	/**
	 * Gets the default implementation of an operation from the operation signature.
	 * 
	 * @param metaClass
	 *            Meta class where to look for the operation
	 * @param opSignature
	 *            Signature of the operation to look for
	 * 
	 * @return the corresponding Implementation object or null if no such implementation is found
	 */
	private static Implementation getDefaultImplementationByOperationSignature(final MetaClass metaClass, 
			final String opSignature) {
		for (Operation op : metaClass.getOperations()) {
			if (op.getNameAndDescriptor().equals(opSignature)) {
				return op.getImplementations().get(0);
			}
		}
		logger.debug("[==PerfetchingSpecGenerator==] Error! Operation " + opSignature + " not found in meta classes!");
		return null;
	}

	/**
	 * Get a property from its name.
	 * 
	 * @param metaClass
	 *            Meta class where to look for the operation
	 * @param propName
	 *            Name of the property to look for
	 * 
	 * @return the corresponding Property object or null if no such property is found
	 */
	private static Property getPropertyByName(final MetaClass metaClass, final String propName) {
		for (Property prop : metaClass.getProperties()) {
			if (prop.getName().equals(propName)) {
				return prop;
			}
		}
		logger.debug("[==PerfetchingSpecGenerator==] Error! Poperty " + propName + " not found in meta classes!");
		return null;
	}

}
