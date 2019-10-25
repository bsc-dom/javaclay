
package es.bsc.dataclay.util.prefetchingspec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.IR;

import es.bsc.dataclay.util.prefetchingspec.visitors.ProgramAnalyzer;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingJavaAnalysisEngine;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingJavaMethod;

/**
 * Main class in the source code analysis module. Connects the code analysis to the rest of dataClay modules.
 * 
 *
 */
public class SourceCodeAnalyzer {
	
	/** Path to file that includes Java classes to exclude from the analysis. **/
	public static final String JAVA_EXCLUSIONS_FILE = "dataclay" + File.separatorChar + "properties"
									+ File.separatorChar + "wala" + File.separatorChar + "JavaExclusionsFile.txt";
	  
	/** Path to file that includes classes to treat as Java native classes. **/
	public static final String JAVA_NATIVES_FILE = "dataclay" + File.separatorChar + "properties"
									+ File.separatorChar + "wala" + File.separatorChar + "JavaNatives.xml";
	  
	/** Path to the primordial.jar.model file. Used internally by Wala to manipulate standard Java classes. **/
	public static final String JAVA_PRIMORDIAL_PATH = "dataclay" + File.separatorChar + "properties"
									+ File.separatorChar + "wala" + File.separatorChar + "primordial.jar.model";
	
	/** Path to properties file of Wala. This is where the path to Java Standards Libs is specified. **/
	public static final String WALA_PROPERTIES_PATH = "dataclay" + File.separatorChar + "properties"
									+ File.separatorChar + "wala" + File.separatorChar + "wala.properties";
  
	/** The Java version to be used when parsing the source files. 
	 * It MUST match the default Java version used by the system.
	 * Currently it is Java 1.8. When upgrading to later versions, make sure everything works correctly **/
	public static final String JAVA_VERSION = System.getProperty("java.version").substring(0, 3);
	
	private ArrayList<String> sources;
	
	private ArrayList<String> libs;
	
	private Set<String> persistentClasses;
	
	/**
	 * This main method is only used to test analysis manually. Should not be called programatically.
	 * Instead, use generatePrefetchingInfo() method.
	 * 
	 * @param args
	 * 			main method arguments.
	 */
	public static void main(final String[] args) {
		// 1. OO7
		String srcPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/OO7/src/";
		String libPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/OO7/lib/";
		Set<String> persistentClasses = new HashSet<String>(Arrays.asList("model.OO7Benchmark", "model.OO7Database",
				  "model.TraversalInfo", "model.Assembly", "model.AtomicPart", "model.BaseAssembly", 
				  "model.ComplexAssembly", "model.CompositePart", "model.Connection", "model.DesignObj", 
				  "model.Document", "model.Manual", "model.Module", "model.Stopwatch"));
		
		// 2. K-Means
		/*String srcPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/K-means/src/";
		String libPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/K-means/lib/";
		Set<String> persistentClasses = new HashSet<String>(Arrays.asList("model.Clusters", "model.Fragment", "model.KMeansModel", 
				  "model.FragmentCollection", "model.FragmentCollectionIndex", "model.Pair", "model.PairF", "model.SumPoints"));
		*/
		
		// 3. WordCount
		/*String srcPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/Wordcount/src/";
		String libPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/Wordcount/lib/";
		Set<String> persistentClasses = new HashSet<String>(Arrays.asList("model.rt_model.WordcountModel", 
				"model.rt_model.Text", "model.rt_model.TextCollection", "model.rt_model.TextCollectionIndex", 
				"model.rt_model.TextStats", "model.rt_model.chunked.ChunkedWordcountModel", 
				"model.rt_model.chunked.Chunk", "model.rt_model.chunked.ChunkedText", 
				"model.rt_model.chunked.ChunkedTextCollection", "model.rt_model.chunked.ChunkedTextCollectionIndex"));
		*/
		
		// 4. PGA
		/*String srcPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/PGA/src/";
		String libPath = "/home/rtouma/DevTools/eclipse/workspace/poas/Performance/PGA/lib/";
		Set<String> persistentClasses = new HashSet<String>(Arrays.asList("model.PGABenchmark", "model.algos.BellmanFordSP",
				"model.algos.BreadthFirstDirectedPaths", "model.algos.DepthFirstDirectedPaths", "model.graphs.DirectedEdge",
				"model.graphs.EdgeWeightedDigraph", "model.graphs.EdgeWeightedDirectedCycle", "model.graphs.Vertex",
				"model.utils.StdRandom", "model.utils.Stopwatch"));		 
		*/
		
		Map<String, IR> results = new SourceCodeAnalyzer(srcPath, libPath, persistentClasses).generatePrefetchingInfo();
		for (IR methodIR : results.values()) {
			System.out.println(((PrefetchingJavaMethod) methodIR.getMethod()).getAnalysisScope().toString());
		}
	}
	
	/**
	 * Constructor that initializes all of the necessary class fields.
	 * 
	 * @param projectPath
	 * 			Path to the directory of the project to analyze 
	 * @param newPersistentClasses
	 * 			Set of persistent classes of the project to analyze
	 */
	public SourceCodeAnalyzer(final String srcPath, final String libPath, final Set<String> newPersistentClasses) {
		setPersistentClasses(newPersistentClasses);
		setSources(srcPath);
		setLibs(libPath);
	}
	

	/**
	 * Sets up the WALA environment and generates the prefetching info.
	 * 
	 * @return WALA IR containing the generated prefetching info
	 */
	public final Map<String, IR> generatePrefetchingInfo() {
		
		// Initialize Wala properties
		PrefetchingSpecGenerator.logger.debug("[==SourceCodeAnalyzer==] Initializing Wala...");
		com.ibm.wala.ipa.callgraph.impl.Util.setNativeSpec(JAVA_NATIVES_FILE);
		
		// Create new analysis engine
		final PrefetchingJavaAnalysisEngine engine = new PrefetchingJavaAnalysisEngine(sources, libs, JAVA_EXCLUSIONS_FILE);
		try {
			// Create new program analyzer and analyze the project
			final ProgramAnalyzer pa = new ProgramAnalyzer(engine, persistentClasses);
			pa.analyze();

			PrefetchingSpecGenerator.logger.debug("[==SourceCodeAnalyzer==] Source code analysis successfully completed");

			return pa.getMethodAnalysisResults();

		} catch (Exception e) {
			PrefetchingSpecGenerator.logger.debug("[==SourceCodeAnalyzer==] Error while analyzing source code! Generating prefetching info will be aborted ");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  Set source directories to be analyzed.
	 *  
	 * @param srcPath
	 * 			path to the source files to be analyzed
	 */
	private final void setSources(String srcPath) {		
		// Add sources from copied path
		sources = new ArrayList<String>();
		sources.add(srcPath);
	}
	
	/**
	 * Set default and application libraries (automatically adds java standard libraries from JS2E directory).
	 * 
	 * @param libPath
	 * 			path to the libraries (jars) to add
	 */
	private final void setLibs(String libPath) {
		libs = new ArrayList<String>();
		
		// 1. Add Java Primordial Jar
		libs.add(JAVA_PRIMORDIAL_PATH);
		
		// 2 Add Java standard libraries
		List<String> javaStdLibs;
		try {
			Properties p = WalaProperties.loadPropertiesFromFile(WalaProperties.class.getClassLoader(), WALA_PROPERTIES_PATH);
			PrefetchingSpecGenerator.logger.debug("[==SourceCodeAnalyzer==] Wala using Java libraries in J2SE_DIR: " + p.getProperty(WalaProperties.J2SE_DIR));
			javaStdLibs = Arrays.asList(WalaProperties.getJarsInDirectory(p.getProperty(WalaProperties.J2SE_DIR)));
		} catch (Exception e) {
			// If file not found, load libs from default run environment
			javaStdLibs = Arrays.asList(WalaProperties.getJ2SEJarFiles());
		}
		libs.addAll(javaStdLibs);
		
		// 3. Add project library files if they exist
		if (new File(libPath).isDirectory()) {
			libs.addAll(Arrays.asList(WalaProperties.getJarsInDirectory(libPath)));
		}
	}
	
	private void setPersistentClasses(Set<String> newPersistentClasses) {
		this.persistentClasses = newPersistentClasses;
	}
}
