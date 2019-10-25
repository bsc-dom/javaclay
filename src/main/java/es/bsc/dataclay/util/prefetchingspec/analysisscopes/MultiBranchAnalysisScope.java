
/**
 * @file MultiBranchAnalysisScope.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.analysisscopes;

import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;
import es.bsc.dataclay.util.prefetchingspec.visitors.ProgramAnalyzer;

/**
 * This class represents a multi-branch analysis scope (e.g. the entire "if-else" segment of the code). Its immediate children
 * are ALWAYS of type BranchAnalysisScope.
 * 
 *
 */
public class MultiBranchAnalysisScope extends AnalysisScope {

	private boolean dependsOnParameters;

	/**
	 * Creates a new MultiBranchAnalysisScope object with the specified parameters.
	 * 
	 * @param startInstrIndex
	 *            the index of the start instruction of the new scope
	 * @param endInstrIndex
	 *            the index of the end instruction of the new scope
	 * @param parent
	 *            the parent of the new scope
	 */
	public MultiBranchAnalysisScope(final int startInstrIndex, final int endInstrIndex, final AnalysisScope parent) {
		super(startInstrIndex, endInstrIndex, parent);
		this.dependsOnParameters = false;
	}

	@Override
	public final AccessChainSet getAnalysisResults() {
		if (ProgramAnalyzer.INCLUDE_BRANCH_DEPENDENT_HINTS) {
			return getUnionOfChildrenAccessChains();
		} else {
			return getIntersectionOfChildrenAccessChains();
		}
	}

	/**
	 * Calculates the INTERSECTION of the access chains of the children of the current multi-branch analysis scope. This is one
	 * of the strategies implemented in the generation of the prefetching information.
	 * 
	 * @return the intersection of the access chains of the children
	 */
	public final AccessChainSet getIntersectionOfChildrenAccessChains() {
		AccessChainSet result = new AccessChainSet();
		// If this MultiBranch only has one children, directly return an empty ArrayList
		if (getChildren().size() == 1) {
			return result;
		}

		result = getChildren().get(0).getAnalysisResults();
		for (AnalysisScope scope : getChildren()) {
			result.retainAll(scope.getAnalysisResults());
		}

		return result;
	}

	/**
	 * Calculates the UNION of the access chains of the children of the current multi-branch analysis scope. This is one of the
	 * strategies implemented in the generation of the prefetching information.
	 * 
	 * @return the union of the access chains of the children
	 */
	public final AccessChainSet getUnionOfChildrenAccessChains() {
		final AccessChainSet result = new AccessChainSet();
		for (AnalysisScope scope : getChildren()) {
			result.addAll(scope.getAnalysisResults());
		}
		return result;
	}

	public boolean dependsOnParameters() {
		return dependsOnParameters;
	}

	public void setDependsOnParameters(boolean dependsOnParam) {
		// This condition avoids setting dependsOnParameters to "false" if it has already been set to "true"
		if (this.dependsOnParameters) {
			return;
		}

		this.dependsOnParameters = dependsOnParam;
	}
}
