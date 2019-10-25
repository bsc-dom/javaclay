
package es.bsc.dataclay.util.prefetchingspec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.ibm.wala.properties.WalaProperties;

import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.prefetchingspec.utils.CodeCreationUtils;

/**
 * Generates prefetching class to prefetch objects specified by the source code analyzer.
 * 
 *
 */
public class PrefetchingClassesGenerator {
	
	/** Metaclasses for which prefetching classes should be generated. **/
	private Map<String, MetaClass> metaClasses;
	
	/** Class path of the application's original classes. **/
	private Map<String, String> prefetchingClasses;

	/** Path to the directory where registered classes sources are stored. **/
	private String compiledPath;
	
	/** Path to the libraries used to compile the registered classes. **/
	private String libPath;
	
	/** Path to the directory where prefetching classes will be stored. **/
	private String prefetchingClassesPath;
	
	/** Path to the directory where prefetching classes source code will be stored. **/
	private String prefetchingSrcPath;
	
	/** Path to the directory where prefetching classes will be compiled. **/
	private String prefetchingCompiledPath;
	
	/**
	 * Constructor.
	 * 
	 * @param newAccountID
	 * 			User dataClay account ID
	 * @param newCredentials
	 * 			User dataClay credentials
	 * @param newMetaClasses
	 * 			Meta classes for which prefetching classes should be generated.
	 */
	public PrefetchingClassesGenerator(final Map<String, MetaClass> newMetaClasses, final String newCompiledPath, final String newLibPath) {
		this.metaClasses = newMetaClasses;
		this.compiledPath = newCompiledPath;
		this.libPath = newLibPath;

		try {
			this.prefetchingClassesPath = Files.createTempDirectory("dataClayPrefetchingClasses").toString();
			this.prefetchingSrcPath = prefetchingClassesPath + "/src/";
			new File(prefetchingSrcPath).mkdir();
			this.prefetchingCompiledPath = prefetchingClassesPath + "/bin/";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * For each class, this method generates a prefetching class that contain methods that prefetch the corresponding
	 * objects. Afterwards, it compiles and registers the generated classes to dataClay.
	 * 
	 */
	public final void generateAndRegisterPrefetchingClasses() {
		try {			
			// Create prefetching code for meta classes
			prefetchingClasses = CodeCreationUtils.createPrefetchingClassCode(metaClasses, prefetchingSrcPath);
		
			// Compile directory
			compilePrefetchingClasses();

			// Register directory to dataClay
			addPrefetchingClassesToRegister();
			
			// Remove all files created for the prefetching classes
			//deletePrefetchingClasses();
			
			// Update prefetchingInfo of metaclasses
			removePrefetchingInfoFromMetaClasses();

			PrefetchingSpecGenerator.logger.debug("[==PrefetchingClassesGenerator==] Prefetching classes successfully created and registered in dataClay");
		} catch (Exception e) {
			PrefetchingSpecGenerator.logger.debug("[==PrefetchingClassesGenerator==] Generating prefetching classes failed");
			e.printStackTrace();			
		}
	}
	
	/**
	 * Compiles the generated enrichment classes and stores the resulting .class files in the compiled directory. 
	 * 
	 * @throws Exception thrown when some paths are not defined
	 */
	private void compilePrefetchingClasses() throws Exception {
		if (prefetchingSrcPath != null) {
			if (prefetchingCompiledPath == null) {
				throw new Exception("Path in which to store compiled classes not defined.");
			}

			// Prepare class path
			final HashSet<String> finalUrls = new HashSet<>();
			// Add sources to classPath
			String absoluteSrcPath = Paths.get(compiledPath).toAbsolutePath().toString();
			finalUrls.add(absoluteSrcPath + "/");
			// Add libs to classPath
			String[] libs = WalaProperties.getJarsInDirectory(libPath);
			for (String lib : libs) {
				String absoluteLibPath = Paths.get(lib).toAbsolutePath().toString();
				finalUrls.add(absoluteLibPath);
			}
			final String[] cp = finalUrls.toArray(new String[] {});

			final Path compiledpath = Paths.get(prefetchingCompiledPath);
			final String absCompiledpath = compiledpath.toAbsolutePath().toString();
			final File destDir = new File(absCompiledpath);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}

			PrefetchingSpecGenerator.logger.debug("[==PrefetchingClassesGenerator==] Compiling prefetching classes in source path: "
						+ prefetchingSrcPath + " to compiledPath: " + absCompiledpath
						+ " with cp: " + finalUrls);

			// Compile
			final Path srcpath = Paths.get(prefetchingSrcPath);
			final String absSrcPath = srcpath.toAbsolutePath().toString();
			FileAndAspectsUtils.compileClasses(absSrcPath, absCompiledpath, cp, null);
		}
	}

	/**
	 * Registers the created enrichments in dataClay.
	 * 
	 */
	private void addPrefetchingClassesToRegister() {
		for (Entry<String, String> prefetchingClass : prefetchingClasses.entrySet()) {
			final String originalClassName = prefetchingClass.getKey();
			final String prefetchingClassName = prefetchingClass.getValue();
			
			// Generate specs
			String cp = prefetchingCompiledPath + ":" + compiledPath;
			final Set<String> classNames = new HashSet<>();
			classNames.add(prefetchingClassName);
			final Map<String, MetaClass> prefetchingClassesSpecs = ClientManagementLib.generateSpecs(
					metaClasses.get(originalClassName).getNamespace(), cp, classNames);
			
			// Add prefetching class specs to meta classes
			metaClasses.put(prefetchingClassName, prefetchingClassesSpecs.get(prefetchingClassName));
			PrefetchingSpecGenerator.logger.debug("[==PrefetchingClassesGenerator==] Prefetching class added to classes to register: " + prefetchingClassName);
		}
	}
	
	/**
	 * Deletes the prefetching classes directory with all of its files (source classes, compiled classes).
	 * 
	 */
	@SuppressWarnings("unused")
	private void deletePrefetchingClasses() {
		try {
			FileUtils.deleteDirectory(new File(prefetchingClassesPath));
			PrefetchingSpecGenerator.logger.debug("[==PrefetchingClassesGenerator==] Prefetching class files deleted from client");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the PropertiesToPrefetch from prefetching info to avoid problems with null IDs when registering
	 * the classes.
	 */
	private void removePrefetchingInfoFromMetaClasses() {
		for (String className : metaClasses.keySet()) {
			for (Operation op : metaClasses.get(className).getOperations()) {
				for (Implementation impl : op.getImplementations()) {
					if (impl.getPrefetchingInfo() != null) {
						impl.getPrefetchingInfo().setPropertiesToPrefetch(new ArrayList<List<Property>>());
					}
				}
			}
		}
	}
}
