
/**
 * @file BranchAnalysisScope.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.analysisscopes;

/**
 * This class represents a branch analysis scope (e.g. an "if" or an "else" scope). Its parent is ALWAYS of type
 * MultiBranchAnalysisScope.
 * 
 *
 */
public class BranchAnalysisScope extends AnalysisScope {
	/**
	 * Creates a new BranchAnalysisScope object with the specified parameters.
	 * 
	 * @param startInstrIndex
	 *            the index of the start instruction of the new scope
	 * @param endInstrIndex
	 *            the index of the end instruction of the new scope
	 * @param parent
	 *            the parent of the new scope
	 */
	public BranchAnalysisScope(final int startInstrIndex, final int endInstrIndex, final MultiBranchAnalysisScope parent) {
		super(startInstrIndex, endInstrIndex, parent);
	}
}
