
/**
 * @file MethodLevelAnalysisScope.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.analysisscopes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChain;
import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;
import es.bsc.dataclay.util.prefetchingspec.visitors.ProgramAnalyzer;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingJavaMethod;

/**
 * This class represents a method-level analysis scope. It is the uppermost analysis scope in any MethodAnalysis object
 * 
 *
 */
public class MethodLevelAnalysisScope extends AnalysisScope {
	
	/** Signature of the method conatining this scope. **/
	private String methodSignature;

	/** A flag indicating whether dynamic prefetching should be disabled for this method. **/
	private boolean isDisabledAtRuntime;
	
	/** A flag indicating whether this method has a loop scope that contains a branching statement. **/
	private boolean hasLoopsWithBranchingStmt;
	
	/** A flag indicating whether the parameters of this method should be monitored at runtime. **/
	private boolean monitorWithparams;
	
	/** An array list containing the ratios for all of the multi-branch scopes in this method. **/
	private ArrayList<Double> multiBranchScopeRatios;

	/** List of methods that invoke this method. **/
	private Set<PrefetchingJavaMethod> invokingMethods;
	
	/** List of overridden methods of this method. **/
	private Set<PrefetchingJavaMethod> overridenMethods;

	/**
	 * Creates a new MethodLevelAnalysisScope object with the specified parameters.
	 * 
	 * @param startInstrIndex
	 *            the index of the start instruction of the new scope
	 * @param endInstrIndex
	 *            the index of the end instruction of the new scope
	 */
	public MethodLevelAnalysisScope(final int startInstrIndex, final int endInstrIndex, final String methodSignature) {
		super(startInstrIndex, endInstrIndex, null);
		this.methodSignature = methodSignature;
		this.isDisabledAtRuntime = true;
		this.hasLoopsWithBranchingStmt = false;
		this.monitorWithparams = false;
		this.multiBranchScopeRatios = new ArrayList<Double>();
		this.invokingMethods = new HashSet<PrefetchingJavaMethod>();
		this.overridenMethods = new HashSet<PrefetchingJavaMethod>();
	}
	
	@Override
	public AccessChainSet getAnalysisResults() {
		final AccessChainSet result = super.getAnalysisResults();
		
		// Only keep the intersection / union with the results of all of its overriden versions
		for (PrefetchingJavaMethod om : overridenMethods) {
			if (ProgramAnalyzer.INCLUDE_OVERRIDDEN_METHODS_HINTS) {
				result.addAll(om.getAnalysisScope().getAnalysisResults());
			} else {
				result.retainAll(om.getAnalysisScope().getAnalysisResults());
			}
		}

		return result;
	}
	
	public AccessChainSet removeInvokingMethodsChains() {
		AccessChainSet methodACs = getAnalysisResults();
		if (invokingMethods.size() == 0) {
			return methodACs;
		}

		AccessChainSet result = new AccessChainSet();
		for (AccessChain ac : methodACs) {
			for (PrefetchingJavaMethod invokingMethod : invokingMethods) {
				AccessChainSet invokingMethodACs = invokingMethod.getAnalysisScope().getAnalysisResults();
				boolean acFound = false;
				for (AccessChain iac : invokingMethodACs) {
					if (Collections.indexOfSubList(iac.getElements(), ac.getElements()) > -1) {
						acFound = true;
						break;
					}
				}
				
				if (!acFound) {
					result.add(ac);
					break;
				}
			}
		}
		
		return result;
	}

	@Override
	public final String toString() {
		return  methodSignature 
				+ " [" + isDisabledAtRuntime + "]:" + "\n\t"
				+ removeInvokingMethodsChains().toString()
				+ "\n-----------------------------------\n";
	}

	/***************************/
	/**** GETTERS / SETTERS ****/
	/***************************/
	public boolean isDisabledAtRuntime() {
		return isDisabledAtRuntime;
	}
	
	public void setDisabledAtRuntime(boolean isDisabledAtRuntime) {
		this.isDisabledAtRuntime = isDisabledAtRuntime;
	}
	
	public boolean hasLoopsWithBranchingStmt() {
		return hasLoopsWithBranchingStmt;
	}
	
	public void setHasLoopsWithBranchingStmt(boolean hasLoopWithBranchingStmt) {
		this.hasLoopsWithBranchingStmt = hasLoopWithBranchingStmt;
	}
	
	public boolean monitorWithparams() {
		return monitorWithparams;
	}
	
	public void setMonitorWithparams(boolean monitorWithparams) {
		this.monitorWithparams = monitorWithparams;
	}
	
	public ArrayList<Double> getMultiBranchScopeRatios() {
		return multiBranchScopeRatios;
	}
	
	public Set<PrefetchingJavaMethod> getInvokingMethods() {
		return invokingMethods;
	}
	
	public String getMethodSignature() {
		return methodSignature;
	}
	
	public Set<PrefetchingJavaMethod> getOverridenMethods() {
		return overridenMethods;
	}
}
