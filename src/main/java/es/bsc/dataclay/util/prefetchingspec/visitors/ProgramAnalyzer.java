
/**
 * @file ProgramAnalysis.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.visitors;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.ibm.wala.cast.java.ipa.callgraph.JavaSourceAnalysisScope;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.types.MethodReference;

import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.prefetchingspec.wala.AbstractMethodIR;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingJavaAnalysisEngine;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingAbstractJavaMethod;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingConcreteJavaMethod;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingJavaMethod;

/**
 * This class represents the program whose methods are going to be analyzed.
 * 
 *
 */
public class ProgramAnalyzer {
	
	/** Flag indicating whether to include loops with branching instructions or not. **/
	public static final boolean INCLUDE_LOOPS_WITH_BRANCHING_HINTS = true;
	
	/** Flag indicating whether to include branch-dependent hints or not. **/
	public static final boolean INCLUDE_BRANCH_DEPENDENT_HINTS = true;
	
	/** Flag indicating whether to include overridden methods' prefetching hints or not. **/
	public static final boolean INCLUDE_OVERRIDDEN_METHODS_HINTS = false;
	
	/** The Wala call graph corresponding to the program. **/
	private PrefetchingJavaAnalysisEngine engine;
	
	/** A list of persistent classes in the applications. Only fields whose type is in this list will be considered. **/
	private Set<String> persistentClasses;

	/** The results of the MethodAnalysis. **/
	private HashMap<String, IR> methodAnalysisResults;

	/**
	 * The signatures of the methods currently being analyzed. Used to avoid
	 * loops.
	 **/
	private ArrayList<String> methodsBeingAnalyzed;
	
	
	/** Statistics generator used to generate statistics after the prefetching analysis is done. **/
	//private StatisticsGenerator psg;

	/**
	 * Creates a new program analysis with the specified parameters.
	 * 
	 * @param newAppName
	 * 			  The new project's name
	 * @param newEngine
	 *            The Wala analysis engine of the new program
	 * @param newPersistentClasses
	 * 			  The project's persistent classes
	 */
	public ProgramAnalyzer(final PrefetchingJavaAnalysisEngine newEngine,
							final Set<String> newPersistentClasses) {
		super();
		this.engine = newEngine;
		this.methodAnalysisResults = new HashMap<String, IR>();
		this.methodsBeingAnalyzed = new ArrayList<String>();
		
		this.persistentClasses = newPersistentClasses;
		if (this.persistentClasses == null) {
			System.err.println("[RT WARNING] No persistent classes specified. "
					+ "All non-primitive fields will be considered in analysis!");
		}
	}

	/**
	 * Analyze all of the methods of the current program.
	 */
	public final void analyze() {		
		for (IClass klass : engine.getClassHierarchy()) {
			if (klass.getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE)) {
				// Start analyzing methods
				for (IMethod method : klass.getDeclaredMethods()) {
					if (method != null && !method.isNative()) {
						findOrCreateMethodAnalysisResult(method);
					}
				}
			}
		}
	}

	/**
	 * Print the results of the methods' analysis to the specified output
	 * stream.
	 * 
	 * @param out
	 *            the output stream where to print the analysis results
	 * 
	 * @throws IOException
	 *             an Exception to throw
	 */
	public final void printResults(final OutputStream out) throws IOException {
		for (IR methodIR : methodAnalysisResults.values()) {
			String output = methodIR.getMethod().getSignature();
			output += ((PrefetchingJavaMethod) methodIR.getMethod()).getAnalysisScope().toString();
			out.write(output.getBytes());
		}
	}

	/************************/
	/**** HELPER METHODS ****/
	/************************/

	/**
	 * Finds or creates a MethodAnalysisResult of the parameter method
	 * signature.
	 * 
	 * @param methodReference
	 *            The method reference for which to find or create the MethodAnalysisResult
	 * @return The MethodAnalysisResult of the parameter method reference
	 */
	public final IR findOrCreateMethodAnalysisResult(final MethodReference methodReference) {
		final IMethod method = engine.getClassHierarchy().resolveMethod(methodReference);
		return findOrCreateMethodAnalysisResult(method);
	}

	/**
	 * Finds or creates a MethodAnalysisResult of the parameter method
	 * signature.
	 * 
	 * @param method
	 *            The method for which to find or create the MethodAnalysisResult
	 * @return The MethodAnalysisResult of the parameter method
	 */		
	public final IR findOrCreateMethodAnalysisResult(final IMethod method) {
		// Check that it is not null AND that it is a user-defined method
		if (method == null
				|| !method.getDeclaringClass().getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE)
				|| !(method instanceof PrefetchingJavaMethod)) {
			return null;
		}

		if (methodAnalysisResults.get(method.getSignature()) != null) {
			return methodAnalysisResults.get(method.getSignature());
		}

		// If method not found in database, analyze it
		return createMethodAnalysisResult((PrefetchingJavaMethod) method);
	}

	/**
	 * Creates a new MethodAnalysis object for the method specified in the
	 * parameter. Furthermore, it calls the analyze method of the newly create
	 * MethodAnalysis.
	 * 
	 * @param method
	 *            the method for which to create a new MethodAnalysis
	 * 
	 * @return the MethodAnalysis object
	 */
	private IR createMethodAnalysisResult(final PrefetchingJavaMethod method) {
		// Add method to the methodsBeingAnalyzed list
		methodsBeingAnalyzed.add(method.getSignature());

		// First, analyze any methods that override this method
		analyzeOverridenMethods(method);
		
		// If the method is abstract, only generate the IR information that we need, there's no code to analyze
		IR methodIR = null;
		if (method instanceof PrefetchingAbstractJavaMethod) {
			methodIR = new AbstractMethodIR((PrefetchingAbstractJavaMethod) method);
		} else if (method instanceof PrefetchingConcreteJavaMethod) {
			// If methodIR is null, that means it's a special case and should be ignored (e.g. abstract or native methods)
			methodIR = engine.getCache().getIR(method);
			if (methodIR == null) {
				return null;
			}
			
			// Generate the scopes for the method to be analyzed (this step is done now because it requires the IR
			// of the method to be completed successfully)
			final CAst2ScopesTranslator translator = new CAst2ScopesTranslator(methodIR);
			translator.translate();
			
			// Analyze the method and generate the prefetching information
			MethodAnalyzer methodAnalyzer;
			if (Configuration.Flags.PREFETCHING_ROP_ENABLED.getBooleanValue()) {
				methodAnalyzer = new ROPMethodAnalyzer(this, methodIR);
			} else {
				methodAnalyzer = new MethodAnalyzer(this, methodIR);
			}
			methodAnalyzer.analyze();	
		}

		methodAnalysisResults.put(method.getSignature(), methodIR);
		methodsBeingAnalyzed.remove(method.getSignature());

		return methodIR;
	}

	/**
	 * Looks up all overridden versions of the given method and analyzes them before the given method.
	 * 
	 * @param method
	 * 			Method whose overridden versions should be looked up.
	 */
	private final void analyzeOverridenMethods(IMethod method) {
		final String methodSignature = method.getSignature().substring(method.getSignature().lastIndexOf('.') + 1, 
				method.getSignature().length() - 1);

		// <init> indicates that the analyzed method is a constructor. A constructor cannot be overridden.
		if (methodSignature.contains("<init>")) {
			return;
		}

		for (IClass klass : engine.getClassHierarchy()) {
			if (klass.getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE) 
					&& klass.getSuperclass() != null && klass.getSuperclass().getClassLoader().getReference().equals(
							JavaSourceAnalysisScope.SOURCE)
					&& (klass.getSuperclass().equals(method.getDeclaringClass()))
						|| klass.getDirectInterfaces().contains(method.getDeclaringClass())) {
				for (IMethod m : klass.getDeclaredMethods()) {
					final String mSignature = m.getSignature().substring(m.getSignature().lastIndexOf('.') + 1, 
							m.getSignature().length() - 1);
					if (mSignature.equals(methodSignature)) {
						((PrefetchingJavaMethod) method).getAnalysisScope().getOverridenMethods().add((
								PrefetchingJavaMethod) m);
						findOrCreateMethodAnalysisResult(m);
					}
				}
			}
		}
	}

	/***************************/
	/**** GETTERS / SETTERS ****/
	/***************************/

	/**
	 * Get ProgramAnalysis::engine.
	 * 
	 * @return engine
	 */
	public final PrefetchingJavaAnalysisEngine getEngine() {
		return engine;
	}
	
	/**
	 * Get ProgramAnalyzer::persistentClasses.
	 * 
	 * @return persistent classes
	 */
	public final Set<String> getPersistentClasses() {
		return persistentClasses;
	}

	/**
	 * Get ProgramAnalysis::methodAnalysisResults.
	 * 
	 * @return methodAnalysisResults
	 */
	public final HashMap<String, IR> getMethodAnalysisResults() {
		return methodAnalysisResults;
	}

	/**
	 * Get ProgramAnalysis::methodsBeingAnalyzed.
	 * 
	 * @return methodsBeingAnalyzed
	 */
	public final ArrayList<String> getMethodsBeingAnalyzed() {
		return methodsBeingAnalyzed;
	}
}
