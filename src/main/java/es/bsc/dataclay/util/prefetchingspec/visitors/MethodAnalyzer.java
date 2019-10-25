
/**
 * @file SourceCodeAnalysisInstructionVisitor.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.ibm.wala.cast.ir.ssa.AstAssertInstruction;
import com.ibm.wala.cast.ir.ssa.AstEchoInstruction;
import com.ibm.wala.cast.ir.ssa.AstGlobalRead;
import com.ibm.wala.cast.ir.ssa.AstGlobalWrite;
import com.ibm.wala.cast.ir.ssa.AstIsDefinedInstruction;
import com.ibm.wala.cast.ir.ssa.AstLexicalRead;
import com.ibm.wala.cast.ir.ssa.AstLexicalWrite;
import com.ibm.wala.cast.ir.ssa.EachElementGetInstruction;
import com.ibm.wala.cast.ir.ssa.EachElementHasNextInstruction;
import com.ibm.wala.cast.java.ssa.AstJavaInstructionVisitor;
import com.ibm.wala.cast.java.ssa.AstJavaInvokeInstruction;
import com.ibm.wala.cast.java.ssa.EnclosingObjectReference;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAArrayLengthInstruction;
import com.ibm.wala.ssa.SSAArrayLoadInstruction;
import com.ibm.wala.ssa.SSAArrayStoreInstruction;
import com.ibm.wala.ssa.SSABinaryOpInstruction;
import com.ibm.wala.ssa.SSACFG.BasicBlock;
import com.ibm.wala.ssa.SSACheckCastInstruction;
import com.ibm.wala.ssa.SSAComparisonInstruction;
import com.ibm.wala.ssa.SSAConditionalBranchInstruction;
import com.ibm.wala.ssa.SSAConversionInstruction;
import com.ibm.wala.ssa.SSAGetCaughtExceptionInstruction;
import com.ibm.wala.ssa.SSAGetInstruction;
import com.ibm.wala.ssa.SSAGotoInstruction;
import com.ibm.wala.ssa.SSAInstanceofInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInvokeInstruction;
import com.ibm.wala.ssa.SSALoadMetadataInstruction;
import com.ibm.wala.ssa.SSAMonitorInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPiInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;
import com.ibm.wala.ssa.SSASwitchInstruction;
import com.ibm.wala.ssa.SSAThrowInstruction;
import com.ibm.wala.ssa.SSAUnaryOpInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChain;
import es.bsc.dataclay.util.prefetchingspec.accesschains.AccessChainSet;
import es.bsc.dataclay.util.prefetchingspec.analysisscopes.AnalysisScope;
import es.bsc.dataclay.util.prefetchingspec.analysisscopes.BranchAnalysisScope;
import es.bsc.dataclay.util.prefetchingspec.analysisscopes.LoopAnalysisScope;
import es.bsc.dataclay.util.prefetchingspec.analysisscopes.MultiBranchAnalysisScope;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.EmptyPrefetchingElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.PrefetchingElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements.FieldElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.fieldelements.FieldElementFactory;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.parameterelements.ParameterElement;
import es.bsc.dataclay.util.prefetchingspec.prefetchingelements.parameterelements.ParameterElementFactory;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingConcreteJavaMethod;
import es.bsc.dataclay.util.prefetchingspec.wala.PrefetchingSourceLoaderImpl.PrefetchingJavaMethod;

/**
 * A subclass of Wala's AstJavaInstructionVisitor that visits a method's instructions and extracts the information needed for
 * prefetching.
 * 
 *
 */
public class MethodAnalyzer implements AstJavaInstructionVisitor {

	/** The program analysis of the method being analyzed. **/
	protected ProgramAnalyzer programAnalyzer;

	/** The Wala IR of the method being analyzed. **/
	protected IR methodIR;

	/** The Wala method being analyzed. **/
	protected PrefetchingConcreteJavaMethod method;

	/** The main access chain per value number (instruction or local variable) in the method. **/
	private HashMap<Integer, AccessChain> accessChainPerValueNumber;

	/** A variable to hold the current scope being analyzed. **/
	protected AnalysisScope currentScope;

	/**
	 * Creates a new SourceCodeAnalysisInstructionVisitor with the specified parameters.
	 * 
	 * @param newProgramAnalyzer
	 *            the program analysis of the method
	 * @param newMethodIR
	 *            the Wala IR of the method being analyzed
	 */
	public MethodAnalyzer(final ProgramAnalyzer newProgramAnalyzer, final IR newMethodIR) {
		super();
		this.programAnalyzer = newProgramAnalyzer;
		this.methodIR = newMethodIR;
		this.method = (PrefetchingConcreteJavaMethod) methodIR.getMethod();
		this.accessChainPerValueNumber = new HashMap<Integer, AccessChain>();
	}

	/**
	 * Method that starts the analysis process.
	 */
	public final void analyze() {
		//analyzeScope(method.getAnalysisScope());
		//for (SSAInstruction instr : methodIR.getInstructions()) {
		for (int i = 0; i < methodIR.getControlFlowGraph().getMaxNumber(); i++) {
			BasicBlock bb = methodIR.getControlFlowGraph().getBasicBlock(i);
			List<SSAInstruction> instrs = bb.getAllInstructions();
			for (SSAInstruction instr : instrs) {
				// For some reason, IR might contain "null" in its instructions array!
				if (instr == null) {
					continue;
				}
				
				currentScope = method.getInstrScope(instr);
				instr.visit(this);
			}
		}
	}

	/**
	 * Visits a scope to analyze its instructions.
	 * 
	 * @param scope
	 *            Scope to visit
	 */
	@SuppressWarnings("unused")
	private void analyzeScope(final AnalysisScope scope) {
		// 1. Visit this scope's instructions
		currentScope = scope;
		for (SSAInstruction instr : scope.getInstructions()) {
			instr.visit(this);
		}

		// 2. Recursively analyze its children scopes
		for (AnalysisScope childScope : scope.getChildren()) {
			analyzeScope(childScope);
		}
	}

	/***********************************/
	/**** INSTRUCTION VISIT METHODS ****/
	/***********************************/

	@Override
	public void visitGet(final SSAGetInstruction instr) {
		// If field is not of a persistent type OR a collection, ignore it.
		if ((!isPersistentType(instr.getDeclaredFieldType())
				|| (instr.getDeclaredFieldType().isArrayType() && !isPersistentType(instr.getDeclaredFieldType().getArrayElementType())))
				&& !isCollectionType(instr.getDeclaredFieldType())) {
			return;
		}

		// Get accessed field name
		final String fieldName = instr.getDeclaredField().getName().toString();
		String className = instr.getDeclaredField().getDeclaringClass().getName().getClassName().toString();
		if (instr.getDeclaredField().getDeclaringClass().getName().getPackage() != null) {
			className = instr.getDeclaredField().getDeclaringClass().getName().getPackage().toString().replaceAll("/", ".") + "." + className;
		}

		final FieldElement faElement = FieldElementFactory.createFieldAccessElement(fieldName, className, instr.getDeclaredFieldType());

		// Add qualified name and check if instruction defines local variable
		addNewInstrAccessChain(instr, faElement);
	}

	@Override
	public void visitJavaInvoke(final AstJavaInvokeInstruction instr) {
		final MethodReference calledMethodRef = instr.getCallSite().getDeclaredTarget();
		// To avoid analysis loops, ignore direct and indirect recursive calls
		if (programAnalyzer.getMethodsBeingAnalyzed().contains(calledMethodRef.getSignature())) {
			return;
		}

		final IR invokedMethodIR = programAnalyzer.findOrCreateMethodAnalysisResult(instr.getCallSite().getDeclaredTarget());
		if (invokedMethodIR != null) {
			addAccessChainsFromMethodInvocation(instr, invokedMethodIR);
			
			// Store current method in invoking method
			PrefetchingJavaMethod invokedMethod = (PrefetchingJavaMethod) invokedMethodIR.getMethod();
			invokedMethod.getAnalysisScope().getInvokingMethods().add(method);
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
	public void visitReturn(final SSAReturnInstruction instr) {
		// If instr.getResult() is -1, then it is an empty "return" statement and should be ignored
		if (instr.getResult() < 0) {
			return;
		}

		final AccessChain returnInstrAnalysisResult = getAccessChainByValueNumber(instr, instr.getResult());
		if (returnInstrAnalysisResult != null) {
			returnInstrAnalysisResult.setReturnAccessChain(true);
			// Make sure that the return chain is added to the current scope with true
			updateAccessChainReturnFlag(returnInstrAnalysisResult);
		}
	}

	@Override
	public void visitConditionalBranch(final SSAConditionalBranchInstruction instr) {
		// Only visit conditional branch instructions if we are in a BranchAnalysisScope
		if (!(currentScope instanceof BranchAnalysisScope) && !(currentScope instanceof MultiBranchAnalysisScope)) {
			return;
		}

		try {
			final int lineNum = methodIR.getMethod().getSourcePosition(instr.iindex).getFirstLine();

			// ASSUMPTION: all "conditionalbranch" instructions that are on the first line of the branch scope
			// are part of the conditional statements of this branch
			if (lineNum == currentScope.getStartInstrIndex()) {
				if (currentScope instanceof BranchAnalysisScope) {
					((MultiBranchAnalysisScope) currentScope.getParent()).setDependsOnParameters(checkIfInstrDependsOnParams(instr));
				} else if (currentScope instanceof MultiBranchAnalysisScope) {
					((MultiBranchAnalysisScope) currentScope).setDependsOnParameters(checkIfInstrDependsOnParams(instr));
				}
			}
		} catch (InvalidClassFileException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visitArrayLoad(final SSAArrayLoadInstruction instr) {
		// Only handle arrayload instructions if they appear within loops
		if (!isInsideLoopAnalysisScope(currentScope)) {
			return;
		}

		final AccessChain usedAccessChain = getAccessChainByValueNumber(instr, instr.getArrayRef());
		// If the usedAccessChain is null, that means this arrayload uses a local variable array
		if (usedAccessChain == null || usedAccessChain.getElements().size() == 0) {
			return;
		}

		addAccessChainPerValueNumber(instr.getDef(0), usedAccessChain);
	}

	@Override
	public void visitArrayStore(final SSAArrayStoreInstruction instr) {
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

		/******** COLLECTION HANDLING ********/
		final AccessChain usedAccessChain = getAccessChainByValueNumber(instr, instr.getUse(0));
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
	 * Avoids code repetition by performing checks common to more than one instruction type: 1. Constructs the qualified name of
	 * the instruction, if it exists. 2. Checks whether the instruction defines a local variable or not.
	 * 
	 * @param instr
	 *            Wala SSAInstruction
	 * @param prefetchingElement
	 *            prefetching element
	 */
	private void addNewInstrAccessChain(final SSAInstruction instr, final PrefetchingElement prefetchingElement) {
		// Add qualified name to instruction
		final AccessChain newAccessChain = addQualifiedNameToInstruction(prefetchingElement, constructQualifiedName(instr));
		if (newAccessChain == null || !instr.hasDef()) {
			return;
		}

		// Add new AccessChain to the hashmap entry of the instruction
		addAccessChainPerValueNumber(instr.getDef(0), newAccessChain);
		currentScope.addAccessChain(newAccessChain);
	}

	/**
	 * Adds the qualified name to the instruction if it is not of type EmptyPrefetchingInstruction.
	 * 
	 * @param prefetchingElement
	 *            the prefetching element to which the qualified name is added
	 * @param qualifiedName
	 *            the qualified name to add
	 * 
	 * @return the access chain composed of the qualified name and the instruction
	 */
	private AccessChain addQualifiedNameToInstruction(final PrefetchingElement prefetchingElement, final AccessChain qualifiedName) {

		// If qualified name is null, instruction starts with a local variable and should be ignored
		if (qualifiedName == null) {
			return null;
		}

		// Add instruction to the methodAnalysisResult
		if (qualifiedName.getElements().get(0) instanceof EmptyPrefetchingElement) {
			qualifiedName.getElements().remove(0);
		}
		qualifiedName.getElements().add(prefetchingElement);
		return qualifiedName;
	}

	/**
	 * The qualified name of an instruction is constructed based on the SSAInstruction.getUses() API. It returns a list of
	 * identifiers of all the local variables and / or SSA instructions used by the current instruction. In
	 * SSAInstruction.getUses(), "this" reference is stored first followed by parameters and then by any other Value Numbers
	 * used. Hence, start analysing from index getNumberOfParameters() + 1.
	 * 
	 * Assumption 1: Any Value Numbers used by an instruction other than "this" reference and parameters, are part of the
	 * qualified name of this instruction. Assumption 2: Other than "this" and the parameters, each instruction only has one
	 * used Value Number.
	 * 
	 * @param instr
	 *            the Wala SSAInstruction for which to construct a qualified name
	 * 
	 * @return the InstructionAnalysisResult corresponding to the qualified name of the passed instruction
	 */
	private AccessChain constructQualifiedName(final SSAInstruction instr) {
		final int numberOfParameters = getNumberOfParameters(instr);
		for (int i = 0; i < instr.getNumberOfUses() - numberOfParameters; i++) {
			final int valueNumber = instr.getUse(i);
			/**
			 * For non-static methods, value number 1 is always "this" reference. For now, this is ignored as it is present for
			 * ALL methods. In the future, the actual use of "this" as a parameter to a method call should be distinguished.
			 */
			if (valueNumber == 1 && !methodIR.getMethod().isStatic()) {
				continue;
			}

			final AccessChain usedAccessChain = getAccessChainByValueNumber(instr, valueNumber);
			if (usedAccessChain == null || usedAccessChain.getElements().size() == 0) {
				return null;
			}

			return new AccessChain(usedAccessChain);
		}

		// If no qualified name exists, return an empty instruction analysis result
		return new AccessChain(new ArrayList<PrefetchingElement>(Arrays.asList(new EmptyPrefetchingElement())));
	}

	/**
	 * Adds the access chains resulting from a method invocation to the current scope. It checks parameter-based access chains
	 * and binds them with the parameters passed to this specific method invocation.
	 * 
	 * @param invokeInstr
	 *            the Wala invocation SSAInstructions
	 * @param invokedMethodIR
	 *            the MethodAnalysis of the invoked method
	 */
	private void addAccessChainsFromMethodInvocation(final SSAAbstractInvokeInstruction invokeInstr, final IR invokedMethodIR) {
		final HashMap<Integer, AccessChain> invocationParamsBindings = bindInvocationParams(invokeInstr, invokedMethodIR);
		final PrefetchingJavaMethod invokedMethod = (PrefetchingJavaMethod) invokedMethodIR.getMethod();
		final AccessChainSet invokedMethodAccessChains = invokedMethod.getAnalysisScope().getAnalysisResults();

		// Construct the qualified name of the invocation instruction (to be used only with non-param based access chains).
		final AccessChain qualifiedName = constructQualifiedName(invokeInstr);

		for (AccessChain accessChain : invokedMethodAccessChains) {
			AccessChain boundParamAccessChain = null;
			if (accessChain.getElements().get(0) instanceof ParameterElement) {
				if (invocationParamsBindings.get(((ParameterElement) accessChain.getElements().get(0)).getParamValueNumber()) != null) {
					boundParamAccessChain = new AccessChain(
							invocationParamsBindings.get(((ParameterElement) accessChain.getElements().get(0)).getParamValueNumber()));
					if (accessChain.getElements().size() > 1) {
						boundParamAccessChain.getElements().addAll(accessChain.getElements().subList(1, accessChain.getElements().size()));
					}
				}
			} else if (qualifiedName != null) {
				boundParamAccessChain = new AccessChain(qualifiedName);
				if (boundParamAccessChain.getElements().get(0) instanceof EmptyPrefetchingElement) {
					boundParamAccessChain.getElements().remove(0);
				}
				boundParamAccessChain.getElements().addAll(accessChain.getElements());
			}

			if (boundParamAccessChain != null) {
				if (accessChain.isReturnAccessChain()) {
					addAccessChainPerValueNumber(invokeInstr.getDef(0), boundParamAccessChain);
				}
				currentScope.addAccessChain(boundParamAccessChain);
			}
		}
	}

	/**
	 * Checks the parameters of the calledMethodAnalysisto see if any of them can be bound to a field in this invocation. If so,
	 * it performs the binding and adds the modified AccessChain to the return result.
	 * 
	 * @param invokeInstr
	 *            the Wala invocation SSAInstruction
	 * @param invokedMethodIR
	 *            the MethodAnalysis of the invoked method
	 * 
	 * @return the list of bound parameters of this invocation
	 */
	private HashMap<Integer, AccessChain> bindInvocationParams(final SSAAbstractInvokeInstruction invokeInstr, final IR invokedMethodIR) {
		final HashMap<Integer, AccessChain> invocationParamsBindings = new HashMap<Integer, AccessChain>();

		final int numberOfParams = invokedMethodIR.getNumberOfParameters();
		for (int i = 0; i < numberOfParams; i++) {
			final int paramValueNumber = invokedMethodIR.getParameter(i);

			// Value Number "1" refers to the "this" reference. It is a default parameter in all non-static methods
			if (paramValueNumber == 1 && !invokedMethodIR.getMethod().isStatic()) {
				continue;
			}

			/*
			 * Get the Value Number of the parameter passed to this invocation of the method. This calculation is based on the
			 * order of value number within the instruction IR. For any non-static instruction, the order of value numbers is
			 * the following: - v1: "this" reference - v2: qualified name uses (in theory, it is not restricted to one value but
			 * in practice it is) - vn: the actual parameters passed to this invocation of the method
			 */
			final int invocationParamIndex = invokeInstr.getNumberOfUses() - 1 - (getNumberOfParameters(invokeInstr) - i);

			/*
			 * In case the calculation is wrong, ignore the current parameter and continue with the others.
			 */
			if (invocationParamIndex >= invokeInstr.getNumberOfUses() || invocationParamIndex < 0) {
				continue;
			}
			final int invocationParamValueNumber = invokeInstr.getUse(invocationParamIndex);

			// Map each parameter to its real value in this invocation
			final AccessChain invocationParamValue = getAccessChainByValueNumber(invokeInstr, invocationParamValueNumber);

			// Check if the mapped value is of interest to the prefetching information
			if (invocationParamValue == null) {
				continue;
			}

			invocationParamsBindings.put(paramValueNumber, invocationParamValue);
		}

		return invocationParamsBindings;
	}
	
	protected final void updateAccessChainReturnFlag(AccessChain returnAccessChain) {
		if (currentScope.getAnalysisResults().contains(returnAccessChain)) {
			for (AccessChain ac : currentScope.getAnalysisResults()) {
				if (ac.equals(returnAccessChain)) {
					ac.setReturnAccessChain(true);
				}
			}
		}
	}

	/**
	 * Checks whether the parameter Value Number of the parameter SSAInstruction corresponds to a local variable or another
	 * SSAInstruction identifier. In both cases, it returns the corresponding AccessChain.
	 * 
	 * @param instr
	 *            The SSAInstruction of the parameter Value Number
	 * @param valueNumber
	 *            The Value Number to be checked
	 * 
	 * @return The corresponding AccessChain
	 */
	protected final AccessChain getAccessChainByValueNumber(final SSAInstruction instr, final int valueNumber) {

		// 1.2. Check if value number corresponds to a parameter
		final int paramIndex = isValueNumberAParameter(valueNumber);
		if (paramIndex >= 0) {

			// If parameter is not of a persistent type OR a collection, ignore it.
			if ((!isPersistentType(methodIR.getParameterType(paramIndex))
					|| (methodIR.getParameterType(paramIndex).isArrayType() && !isPersistentType(methodIR.getParameterType(paramIndex).getArrayElementType())))
					&& !isCollectionType(methodIR.getParameterType(paramIndex))) {
				return null;
			}

			final ParameterElement paInstr = ParameterElementFactory.createParameterAccessElement(
					getLocalVarNames(instr, valueNumber).get(0),
					valueNumber,
					methodIR.getParameterType(paramIndex));

			// addAccessChainToInstr(instr, newAccessChain);
			return new AccessChain(new ArrayList<PrefetchingElement>(Arrays.asList(paInstr)));
		}

		// 2. If value number doesn't define a local variable name, then it defines an instruction
		return accessChainPerValueNumber.get(valueNumber);
	}

	/**
	 * If the parameter SSAInstruction is an SSAAbstractInvokeInstruction, it returns the number of parameters of the invoked
	 * method. Otherwise, returns 0.
	 * 
	 * @param instr
	 *            The SSAInstruction for which the number of parameters are to be calculated
	 * 
	 * @return the number of parameters of the instruction
	 */
	private int getNumberOfParameters(final SSAInstruction instr) {
		if (instr instanceof SSAAbstractInvokeInstruction) {
			return ((SSAAbstractInvokeInstruction) instr).getCallSite().getDeclaredTarget().getNumberOfParameters();
		}

		return 0;
	}

	/**
	 * Checks if an instruction uses a parameter of its parent method.
	 * 
	 * @param instr
	 *            Instruction to check
	 * @return flag
	 */
	private boolean checkIfInstrDependsOnParams(final SSAInstruction instr) {
		for (int i = 0; i < instr.getNumberOfUses(); i++) {
			final int valueNumber = instr.getUse(i);

			// valueNumber 1 is "this" in all non-static methods and should be ignored
			if (valueNumber == 1 && !methodIR.getMethod().isStatic()) {
				continue;
			}

			// 1. Check if value number corresponds to a parameter
			if (isValueNumberAParameter(valueNumber) >= 0) {
				return true;
			}
			// 2. If not, recursively call this function for each instruction that the original instruction uses
			final SSAInstruction[] instrs = methodIR.getInstructions();
			for (int j = 0; j < instrs.length; j++) {
				if (instrs[j] != null
						&& instrs[j].getNumberOfDefs() > 0 && instrs[j].getDef(0) == valueNumber) {
					return checkIfInstrDependsOnParams(instrs[j]);
				}
			}
		}
		return false;
	}

	/**
	 * Returns the corresponding local variable name of the parameter Value Number and SSAInstruction. Can return more than one
	 * element if the instruction sets the value of two or more local variables.
	 * 
	 * @param instr
	 *            The SSAInstruction of the parameter Value Number
	 * @param valueNumber
	 *            The Value Number to be this.paramBasedInstructions = new HashMap<String, ArrayList
	 *            <PrefetchingInstruction>>();checked
	 * @return The name of the first local variable that valueNumber defines
	 */
	private ArrayList<String> getLocalVarNames(final SSAInstruction instr, final int valueNumber) {
		final ArrayList<String> localVars = new ArrayList<String>(
				Arrays.asList(methodIR.getLocalNames(instr.iindex, valueNumber)));
		if (localVars == null || localVars.size() == 0) {
			return null;
		}

		return new ArrayList<String>(localVars);
	}

	/**
	 * Checks if the given value number corresponds to a parameter or not.
	 * 
	 * @param valueNumber
	 *            the value number to check
	 * @return True if value number is parameter. False otherwise
	 */
	private int isValueNumberAParameter(final int valueNumber) {
		for (int i = 0; i < methodIR.getNumberOfParameters(); i++) {
			if (methodIR.getParameter(i) == valueNumber) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Checks if a given type is persistent or non-primitive.
	 * 
	 * @param typeRef
	 *            The type reference to check
	 * @return flag
	 */
	protected final boolean isPersistentType(final TypeReference typeRef) {
		String typeName = typeRef.getName().getClassName().toString();
		if (typeRef.getName().getPackage() != null) {
			typeName = typeRef.getName().getPackage().toString().replace("/", ".") + "." + typeName;
		}

		if ((programAnalyzer.getPersistentClasses() != null
				&& !programAnalyzer.getPersistentClasses().contains(typeName))
				|| (typeRef.isPrimitiveType())) {
			return false;
		}

		return true;
	}

	/**
	 * Checks if a given scope is within a loop scope, directly or indirectly.
	 * 
	 * @param scope
	 *            Scope to check
	 * @return flag
	 */
	protected boolean isInsideLoopAnalysisScope(final AnalysisScope scope) {
		if (scope instanceof LoopAnalysisScope) {
			return true;
		}

		if (scope.getParent() == null) {
			return false;
		}

		return isInsideLoopAnalysisScope(scope.getParent());
	}

	/**
	 * Add an access chain to the given value number.
	 * 
	 * @param valueNumber
	 *            Value number
	 * @param accessChain
	 *            Access chain to add
	 */
	protected final void addAccessChainPerValueNumber(final int valueNumber, final AccessChain accessChain) {
		accessChainPerValueNumber.put(valueNumber, accessChain);
	}

	/*
	 * Does not check if invocations or loops occur before current instruction because this is done during analysis and
	 * instructions are visited iteratively (when visiting instruction with instrIndex, only instructions with lower indexes
	 * would have been visited).
	 */
	/*
	 * private final int expandInvokesBeforeInstrIndex(PrefetchingConcreteJavaMethod method, int instrIndex) { int
	 * modifiedInstrIndex = instrIndex; for (Integer invokeInstrIndex :
	 * method.getAnalysisScope().getInstrCountPerInvokeInstr().keySet()) { modifiedInstrIndex +=
	 * method.getAnalysisScope().getInstrCountPerInvokeInstr().get(invokeInstrIndex); }
	 * 
	 * return modifiedInstrIndex; }
	 */

	/***************************/
	/**** GETTERS / SETTERS ****/
	/***************************/
	/**
	 * Get MethodAnalyzer::method.
	 * 
	 * @return method
	 */
	public final PrefetchingConcreteJavaMethod getMethod() {
		return method;
	}

	/**
	 * Get MethodAnalyzer::methodIR.
	 * 
	 * @return methodIR
	 */
	public final IR getMethodIR() {
		return methodIR;
	}

	public static boolean isCollectionType(final TypeReference typeRef) {
		Class<?> c = null;
		try {
			String fieldTypeName = typeRef.getName().toString().substring(1).replaceAll("/", ".");
			c = Class.forName(fieldTypeName);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			// Do Nothing. If class not found, it does not affect the running of the program.
		}
		
		if (c != null && Collection.class.isAssignableFrom(c)) {
			return true;
		}
		
		return false;
	}

	/******************************/
	/**** IGNORED INSTRUCTIONS ****/
	/******************************/

	@Override
	public final void visitInvoke(final SSAInvokeInstruction instr) {

	}

	@Override
	public final void visitNew(final SSANewInstruction instr) {

	}

	@Override
	public final void visitPut(final SSAPutInstruction instr) {

	}

	@Override
	public final void visitGoto(final SSAGotoInstruction instr) {

	}

	@Override
	public final void visitUnaryOp(final SSAUnaryOpInstruction instr) {

	}

	@Override
	public final void visitThrow(final SSAThrowInstruction instr) {

	}

	@Override
	public final void visitSwitch(final SSASwitchInstruction instr) {

	}

	@Override
	public final void visitPi(final SSAPiInstruction instr) {

	}

	@Override
	public final void visitPhi(final SSAPhiInstruction instr) {

	}

	@Override
	public final void visitMonitor(final SSAMonitorInstruction instr) {

	}

	@Override
	public final void visitLoadMetadata(final SSALoadMetadataInstruction instr) {

	}

	@Override
	public final void visitInstanceof(final SSAInstanceofInstruction instr) {

	}

	@Override
	public final void visitGetCaughtException(final SSAGetCaughtExceptionInstruction instr) {

	}

	@Override
	public final void visitConversion(final SSAConversionInstruction instr) {

	}

	@Override
	public final void visitComparison(final SSAComparisonInstruction instr) {

	}

	@Override
	public final void visitBinaryOp(final SSABinaryOpInstruction instr) {

	}

	@Override
	public final void visitArrayLength(final SSAArrayLengthInstruction instr) {

	}

	@Override
	public final void visitIsDefined(final AstIsDefinedInstruction instr) {

	}

	@Override
	public final void visitEcho(final AstEchoInstruction instr) {

	}

	@Override
	public final void visitEachElementHasNext(final EachElementHasNextInstruction instr) {

	}

	@Override
	public final void visitEachElementGet(final EachElementGetInstruction instr) {

	}

	@Override
	public final void visitAstLexicalWrite(final AstLexicalWrite instr) {

	}

	@Override
	public final void visitAstLexicalRead(final AstLexicalRead instr) {

	}

	@Override
	public final void visitAstGlobalWrite(final AstGlobalWrite instr) {

	}

	@Override
	public final void visitAstGlobalRead(final AstGlobalRead instr) {

	}

	@Override
	public final void visitAssert(final AstAssertInstruction instr) {

	}

	@Override
	public final void visitEnclosingObjectReference(final EnclosingObjectReference instr) {

	}

}
