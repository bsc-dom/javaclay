
/**
 * @file LoopAnalysisScope.java
 * 
 * @date Jan 25, 2016
 */
package es.bsc.dataclay.util.prefetchingspec.analysisscopes;

import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;
import es.bsc.dataclay.util.prefetchingspec.visitors.ProgramAnalyzer;

/**
 * This class represents a loop analysis scope (e.g. a "for" or a "while" loop).
 * 
 *
 */
public class LoopAnalysisScope extends AnalysisScope {
	
	private boolean hasBranchingStatements;

	/**
	 * Creates a new LoopAnalysisScope object with the specified parameters.
	 * 
	 * @param startInstrIndex
	 *            the index of the start instruction of the new scope
	 * @param endInstrIndex
	 *            the index of the end instruction of the new scope
	 * @param parent
	 *            the parent of the new scope
	 */
	public LoopAnalysisScope(final int startInstrIndex, final int endInstrIndex, final AnalysisScope parent) {
		super(startInstrIndex, endInstrIndex, parent);
		this.hasBranchingStatements = false;
	}

	@Override
	@SuppressWarnings("unused")
	public AccessChainSet getAnalysisResults() {
		if (!ProgramAnalyzer.INCLUDE_LOOPS_WITH_BRANCHING_HINTS && hasBranchingStatements) {
			return new AccessChainSet();
		}
		return super.getAnalysisResults();
	}
	
	public boolean hasBranchingStatements() {
		return hasBranchingStatements;
	}
	
	public void setHasBranchingStatements(boolean hasBranchingStatements) {
		this.hasBranchingStatements = hasBranchingStatements;
	}
}
