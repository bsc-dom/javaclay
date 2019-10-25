
package es.bsc.dataclay.util.prefetchingspec.visitors;

import com.ibm.wala.cast.java.ipa.callgraph.JavaSourceAnalysisScope;
import com.ibm.wala.cast.java.ssa.AstJavaInvokeInstruction;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAArrayLoadInstruction;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSACheckCastInstruction;
import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChain;
import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.PrefetchingElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements.FieldElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements.FieldElementFactory;

/**
 * Method analyzer that generates primitive prefetching hints (everytime an object is access, prefetch its 
 * related objects). Level is determined by a constant.
 * 
 *
 */
public class ROPMethodAnalyzer extends MethodAnalyzer {
	
	/** Fetch depth of how many levels of objects should be prefetched. **/
	private final int fetchDepth;
	
	/**
	 * Creates a new SourceCodeAnalysisInstructionVisitor with the specified parameters.
	 * 
	 * @param newProgramAnalyzer the program analysis of the method
	 * @param newMethodIR the Wala IR of the method being analyzed
	 */
	public ROPMethodAnalyzer(final ProgramAnalyzer newProgramAnalyzer, final IR newMethodIR) {
		super(newProgramAnalyzer, newMethodIR);
		fetchDepth = Configuration.Flags.PREFETCHING_ROP_DEPTH.getIntValue();
	}


	/***********************************/
	/**** INSTRUCTION VISIT METHODS ****/
	/***********************************/

	@Override
	public final void visitGet(final SSAGetInstruction instr) {
		// At the moment, static get instructions are not processed
		if (instr.isStatic()) {
			//System.err.println("Accessing static field in instruction: " + instr.toString());
			return;
		}

		// If field is not of a persistent type OR a collection, ignore it.
		if ((!isPersistentType(instr.getDeclaredFieldType())
				|| (instr.getDeclaredFieldType().isArrayType() && !isPersistentType(instr.getDeclaredFieldType().getArrayElementType())))
				&& !MethodAnalyzer.isCollectionType(instr.getDeclaredFieldType())) {
			return;
		}

		// Get accessed field name
		final String fieldName = instr.getDeclaredField().getName().toString();
		String className = instr.getDeclaredField().getDeclaringClass().getName().getClassName().toString();
		if (instr.getDeclaredField().getDeclaringClass().getName().getPackage() != null) {
			className = instr.getDeclaredField().getDeclaringClass().getName().getPackage().toString().replaceAll("/", ".") + "." + className;
		}

		final FieldElement faElement = FieldElementFactory.createFieldAccessElement(fieldName, className, instr.getDeclaredFieldType());
		final AccessChain fieldAccessChain = new AccessChain();
		fieldAccessChain.getElements().add(faElement);

		addPrefetchingHintsFromField(fieldAccessChain, instr.getDeclaredFieldType(), 0);
	}

	@Override
	public final void visitJavaInvoke(final AstJavaInvokeInstruction instr) {
		final MethodReference calledMethodRef = instr.getCallSite().getDeclaredTarget();

		// To avoid analysis loops, ignore direct and indirect recursive calls
		if (programAnalyzer.getMethodsBeingAnalyzed().contains(calledMethodRef.getSignature())) {
			return;
		}

		final IR invokedMethodIR = programAnalyzer.findOrCreateMethodAnalysisResult(instr.getCallSite().getDeclaredTarget());

		if (invokedMethodIR != null) {
			// Add prefetching hints TAL CUAL from called method
			/*AccessChainSet invokedMethodAccessChains = ((PrefetchingJavaMethod) invokedMethodIR.getMethod()).getAnalysisScope().getAnalysisResults();
			for (AccessChain ac : invokedMethodAccessChains) {
				if (ac.getElements().size() == 0) {
					continue;
				}
				AccessChain newAc = new AccessChain(ac);
				if (newAc.getElements().get(0) instanceof FieldSingleElement) {
					newAc.getElements().remove(0);
				}
				if (newAc.getElements().size() != 0) {
					currentScope.addAccessChain(newAc);
				}
			}*/
		}

		/******** COLLECTION HANDLING ********/
		if (calledMethodRef.getName().toString().equals("iterator")
				|| calledMethodRef.getName().toString().equals("next")
				|| calledMethodRef.getName().toString().equals("get")
				|| calledMethodRef.getName().toString().equals("add")
				|| calledMethodRef.getName().toString().equals("addAll")) {
			int firstUseIndex = 0;
			if (instr.getUse(0) == 1 && !methodIR.getMethod().isStatic()) {
				firstUseIndex = 1;
			}
			
			int colValueNumber = -1;
			int newColValueNumber = -1;
			// Need to distinguish between instructions because they have different order of used values
			if (calledMethodRef.getName().toString().equals("add")
				|| calledMethodRef.getName().toString().equals("addAll")) {
				newColValueNumber = instr.getUse(firstUseIndex);
				colValueNumber = instr.getUse(++firstUseIndex);
			} else {
				newColValueNumber = instr.getDef(0);
				colValueNumber = instr.getUse(firstUseIndex);
			}

			if (colValueNumber == -1 || newColValueNumber == -1) {
				return;
			}
			
			final AccessChain usedAccessChain = getAccessChainByValueNumber(instr, colValueNumber);
			// If the usedAccessChain is null, that means this arrayload uses a local variable array 
			if (usedAccessChain == null || usedAccessChain.getElements().size() == 0) {
				return;
			}

			addAccessChainPerValueNumber(newColValueNumber, usedAccessChain);
		}
	}

	@Override
	public void visitArrayLoad(final SSAArrayLoadInstruction instr) {
		// Only handle arrayload instructions if they appear within loops
		if (!isInsideLoopAnalysisScope(currentScope)) {
			return;
		}

		AccessChain usedAccessChain = getAccessChainByValueNumber(instr, instr.getArrayRef());
		// If the usedAccessChain is null, that means this arrayload uses a local variable array
		if (usedAccessChain == null || usedAccessChain.getElements().size() == 0) {
			return;
		}

		addAccessChainPerValueNumber(instr.getDef(0), usedAccessChain);
	}

	@Override
	public final void visitArrayStore(final SSAArrayStoreInstruction instr) {
		// Only handle checkcast instructions if they appear within loops
		if (!isInsideLoopAnalysisScope(currentScope)) {
			return;
		}

		AccessChain usedAccessChain = getAccessChainByValueNumber(instr, instr.getUse(2));
		// If the usedAccessChain is null, that means this arraystore uses a local variable
		if (usedAccessChain == null || usedAccessChain.getElements().size() == 0) {
			return;
		}

		// If we are storing persistent elements in an array, the arrayref should be tied to the access chain of the persistent collection
		addAccessChainPerValueNumber(instr.getArrayRef(), usedAccessChain);
	}

	@Override
	public void visitCheckCast(final SSACheckCastInstruction instr) {
		// Only handle checkcast instructions if they appear within loops
		if (!isInsideLoopAnalysisScope(currentScope)) {
			return;
		}

		AccessChain usedAccessChain = getAccessChainByValueNumber(instr, instr.getUse(0));
		// If the usedAccessChain is null, that means this arrayload uses a local variable array
		if (usedAccessChain == null || usedAccessChain.getElements().size() == 0) {
			return;
		}

		addAccessChainPerValueNumber(instr.getDef(0), usedAccessChain);
	}


	/************************/
	/**** HELPER METHODS ****/
	/************************/
	
	/**
	 * Adds prefetching hints starting from a given access chain and type reference.
	 * 
	 * @param accessChain
	 * 			Access chain
	 * @param typeRef
	 * 			Type reference
	 * @param currentDepth
	 * 			Current depth
	 */
	private void addPrefetchingHintsFromField(final AccessChain accessChain, final TypeReference typeRef, 
			int currentDepth) {
		if (currentDepth == fetchDepth) {
			return;
		}

		final AccessChainSet accessChains = constructAccessChainsFromField(accessChain, typeRef);
		// For each access chain in the result, add it to the scope's access chains AND call this method
		currentDepth++;
		for (AccessChain ac : accessChains) {
			if (ac.getElements().size() == 0) {
				continue;
			}
			currentScope.addAccessChain(ac);
			addPrefetchingHintsFromField(ac, ac.getElements().get(ac.getElements().size() - 1).getType(), currentDepth);
		}

	}
	
	/**
	 * Constructs a set of access chains from a given access chain and type reference.
	 * 
	 * @param chain
	 * 			Access chain
	 * @param typeRef
	 * 			Type reference
	 * @return constructed access chain set
	 */
	private AccessChainSet constructAccessChainsFromField(final AccessChain chain, final TypeReference typeRef) {
		final AccessChainSet result = new AccessChainSet();
		
		// Add the input access chain to the result set
		result.add(chain);

		// Get class corresponding to type reference
		final IClass typeClass = getClassFromTypeReference(typeRef);
		if (typeClass == null) {
			return result;
		}
		
		// Add element corresponding to each property of the type class
		for (IField field : typeClass.getAllInstanceFields()) {
			// Only add fields of persistent types. Collections are only fetched when explicitly accessed.
			if (!isPersistentType(field.getFieldTypeReference())) {
				continue;
			}
			
			// Check for type cycles (A has a field of type B and B has a field of type A)
			for (PrefetchingElement elem : chain.getElements()) {
				if (elem.getType().equals(field.getFieldTypeReference())) {
					continue;
				}
			}

			// Get accessed field name
			final String fieldName = field.getName().toString();
			String className = field.getDeclaringClass().getName().getClassName().toString();
			if (field.getDeclaringClass().getName().getPackage() != null) {
				className = field.getDeclaringClass().getName().getPackage().toString().replaceAll("/", ".") + "." + className;
			}
			
			// Add each field to a new access chain to the result
			final AccessChain newAccessChain = new AccessChain(chain);
			newAccessChain.getElements().add(
					FieldElementFactory.createFieldAccessElement(fieldName, className, field.getFieldTypeReference()));
			result.add(newAccessChain);
		}
		
		return result;
	}
	
	/**
	 * Gets the class of a given type reference.
	 * 
	 * @param typeRef
	 * 			Type reference
	 * @return correponding Wala class
	 */
	private IClass getClassFromTypeReference(final TypeReference typeRef) {
		for (IClass klass : programAnalyzer.getEngine().getClassHierarchy()) {
			if (klass.getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE)
					&& klass.getReference().equals(typeRef)) {
				return klass;
			}
		}
		
		return null;
	}
}
