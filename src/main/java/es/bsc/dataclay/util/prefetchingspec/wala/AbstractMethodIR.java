
package es.bsc.dataclay.util.prefetchingspec.wala;

import com.ibm.wala.cast.loader.AstMethod;
import com.ibm.wala.cast.tree.CAstSourcePositionMap.Position;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAIndirectionData;
import com.ibm.wala.ssa.SSAIndirectionData.Name;

import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingAbstractJavaMethod;

/**
 * Class used to describe the dummy IR of an abstract method. Inherits from Wala IR and does not contain any instructions.
 * 
 *
 */
public class AbstractMethodIR extends IR {
	
	/**
	 * Constructor.
	 * 
	 * @param method
	 * 			Abstract method for which the IR should be created
	 */
	public AbstractMethodIR(final PrefetchingAbstractJavaMethod method) {
	      super(method, null, null, null, new AnalysisOptions().getSSAOptions());
	}

	@Override
	protected SSA2LocalMap getLocalMap() {
	      return null;
	}

	@Override
	protected <T extends Name> SSAIndirectionData<T> getIndirectionData() {
		return null;
	}

	@Override
	protected String instructionPosition(final int instructionIndex) {
      final Position pos = getMethod().getSourcePosition(instructionIndex);
      if (pos == null) {
        return "";
      } else {
        return pos.toString();
      }
	}

    @Override
    public AstMethod getMethod() {
      return (AstMethod) super.getMethod();
    }
}
