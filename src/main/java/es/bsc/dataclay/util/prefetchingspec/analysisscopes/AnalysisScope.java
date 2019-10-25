
/**
 * @file AnalysisScope.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.analysisscopes;

import java.util.ArrayList;
import java.util.List;

import com.ibm.wala.ssa.SSAInstruction;

import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChain;
import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;

/**
 * Abstract class representing an AnalysisScope within a method.
 * 
 *
 */
public abstract class AnalysisScope {

	/** The index of the instruction at which the scope starts. **/
	private int startInstrIndex;

	/** The index of the instruction at which the scope ends. **/
	private int endInstrIndex;
	
	/** List of instructions in the scope. **/
	private List<SSAInstruction> instructions;

	/** The parent scope of the current AnalysisScope (null if the scope is a MethodLevelAnalysisScope). **/
	private AnalysisScope parent;

	/** The children scopes of the current AnalysisScope. **/
	private ArrayList<AnalysisScope> children;

	/** The access chains discovered in the current scope. **/
	private AccessChainSet accessChainSet;

	/**
	 * Creates a new AnalysisScope with the given parameters.
	 * 
	 * @param newStartInstrIndex
	 *            the index of the start instruction of the new scope
	 * @param newEndInstrIndex
	 *            the index of the end instruction of the new scope
	 * @param newParent
	 *            the parent of the new scope
	 */
	public AnalysisScope(final int newStartInstrIndex, final int newEndInstrIndex, final AnalysisScope newParent) {
		super();
		this.startInstrIndex = newStartInstrIndex;
		this.endInstrIndex = newEndInstrIndex;
		this.instructions = new ArrayList<SSAInstruction>();
		this.parent = newParent;
		this.children = new ArrayList<AnalysisScope>();
		this.accessChainSet = new AccessChainSet();
	}

	/**
	 * Get the overall access chains of each scope (including the access chains of its children scopes). The default implementation returns the union
	 * For now, it is only overriden in MultiBranchAnalysisScope.
	 * 
	 * @return the access chains of the scope and its children
	 */
	public AccessChainSet getAnalysisResults() {
		AccessChainSet result = new AccessChainSet();
		result.addAll(accessChainSet);
		
		// Add analysis results of children scopes
		for (AnalysisScope scope : getChildren()) {
			result.addAll(scope.getAnalysisResults());
		}
		return result;
	}
	
	public LoopAnalysisScope isInsideLoopScope() {
		if (parent != null) {
			if (parent instanceof LoopAnalysisScope) {
				return (LoopAnalysisScope) parent;
			} else {
				return parent.isInsideLoopScope();
			}
		}
		
		return null;		
	}
	
	public MultiBranchAnalysisScope isInsideMultiBranchScope() {
		if (parent != null) {
			if (parent instanceof MultiBranchAnalysisScope) {
				return (MultiBranchAnalysisScope) parent;
			} else {
				return parent.isInsideMultiBranchScope();
			}
		}
		
		return null;		
	}

	/***************************/
	/**** GETTERS / SETTERS ****/
	/***************************/

	/**
	 * Starting from any analysis scope, recursively check the parent until reaching the MethodLevelAnalysisScope.
	 * 
	 * @return the MethodLevelAnalysisScope
	 */
	public final MethodLevelAnalysisScope getMethodLevelAnalysisScope() {
		AnalysisScope as = this;
		while (!(as instanceof MethodLevelAnalysisScope) && as.getParent() != null) {
			as = as.getParent();
		}

		if (as instanceof MethodLevelAnalysisScope) {
			return ((MethodLevelAnalysisScope) as);
		}

		return null;
	}

	/**
	 * Get AnalysisScope::startInstrIndex.
	 * 
	 * @return startInstrIndex
	 */
	public final int getStartInstrIndex() {
		return startInstrIndex;
	}

	/**
	 * Set AnalysisScope::startInstrIndex.
	 * 
	 * @param newStartInstrIndex
	 *            the startInstrIndex to set
	 */
	public final void setStartInstrIndex(final int newStartInstrIndex) {
		this.startInstrIndex = newStartInstrIndex;
	}

	/**
	 * Get AnalysisScope::endInstrIndex.
	 * 
	 * @return endInstrIndex
	 */
	public final int getEndInstrIndex() {
		return endInstrIndex;
	}

	/**
	 * Set AnalysisScope::endInstrIndex.
	 * 
	 * @param newEndInstrIndex
	 *            the endInstrIndex to set
	 */
	public final void setEndInstrIndex(final int newEndInstrIndex) {
		this.endInstrIndex = newEndInstrIndex;
	}
	
	public final List<SSAInstruction> getInstructions() {
		return instructions;
	}

	/**
	 * Get AnalysisScope::parent.
	 * 
	 * @return parent
	 */
	public final AnalysisScope getParent() {
		return parent;
	}

	/**
	 * Set AnalysisScope::parent.
	 * 
	 * @param newParent
	 *            the parent to set
	 */
	public final void setParent(final AnalysisScope newParent) {
		this.parent = newParent;
	}
	
	public final void addAccessChain(AccessChain accessChain) {
		accessChainSet.add(accessChain);
	}

	/**
	 * Get AnalysisScope::children.
	 * 
	 * @return children
	 */
	public final ArrayList<AnalysisScope> getChildren() {
		return children;
	}

	/**
	 * Add a child to the children of the current scope.
	 * 
	 * @param newChild
	 *            the new child to add
	 */
	public final void addChild(final AnalysisScope newChild) {
		this.children.add(newChild);
	}
	
	@Override
	public String toString() {
		return "from instr " + startInstrIndex
				+ " to instr " + endInstrIndex
				+ "\n" + accessChainSet.toString();
	}
}
