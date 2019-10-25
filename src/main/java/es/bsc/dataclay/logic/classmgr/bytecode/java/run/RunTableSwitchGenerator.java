
/**
 * 
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.run;

import java.util.Map.Entry;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.commons.Method;

import es.bsc.dataclay.logic.classmgr.bytecode.java.ByteCodeConstants;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethods;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeTypes;
import es.bsc.dataclay.logic.classmgr.bytecode.java.switches.DataClaySwitchByImplementationID;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * Run table switch generator.
 */
public final class RunTableSwitchGenerator extends DataClaySwitchByImplementationID {

	/**
	 * Constructor
	 * @param themetaClass
	 *            Information of class.
	 * @param thestubInfo
	 *            Stub info
	 * @param theisExecClass
	 *            isExec class
	 * @param thegn
	 *            Method generator.
	 * @param theia
	 *            Instruction adapter
	 */
	public RunTableSwitchGenerator(
			final MetaClass themetaClass,
			final StubInfo thestubInfo, final boolean theisExecClass,
			final GeneratorAdapter thegn, final InstructionAdapter theia) {
		super(themetaClass, thestubInfo, theisExecClass, thegn, theia);
	}

	@Override
	public void generateCase(final Implementation impl) {

		final Operation operation = metaClass.getOperation(impl.getOperationID());

		// Prepare parameter types
		final org.objectweb.asm.Type[] paramTypes = new org.objectweb.asm.Type[operation.getParams().size()];
		int k = 0;
		for (final Entry<String, es.bsc.dataclay.util.management.classmgr.Type> param : operation.getParams().entrySet()) {
			final org.objectweb.asm.Type pType = org.objectweb.asm.Type.getType(param.getValue().getDescriptor());
			paramTypes[k] = pType;
			k++;
		}

		// Get return type
		final org.objectweb.asm.Type returnType = org.objectweb.asm.Type.getType(operation.getReturnType().getDescriptor());

		// Get method name
		String methodName = operation.getName();
		if (impl.getPosition() > 0) {
			methodName += ByteCodeConstants.DATACLAY_PREFIX_MARK + impl.getPosition();
		}

		// Prepare call
		gn.loadThis();

		// Load all arguments into stack
		for (int i = 0; i < paramTypes.length; ++i) {
			gn.loadArg(1); // Stack: <This> <Array>
			ia.aconst(i); // Stack: <This> <Array> <Idx>
			gn.arrayLoad(ByteCodeTypes.OBJECT); // Stack: <This> <Arg>
			// unbox if needed
			gn.unbox(paramTypes[i]);
		}

		// Prepare method
		final Method methodToRun = new Method(methodName, returnType, paramTypes);
		final String thisClassDesc = Reflector.getDescriptorFromTypeName(metaClass.getName());
		gn.invokeVirtual(org.objectweb.asm.Type.getType(thisClassDesc), methodToRun);

		if (returnType.getSort() == org.objectweb.asm.Type.VOID) {
			ia.aconst(null);
			gn.returnValue();
		} else {
			// Return result
			// Box if needed
			gn.box(returnType);
			gn.returnValue();
		}

	}

	@Override
	protected void generateCallToParentCase() {

		gn.loadThis();
		gn.loadArg(0);
		gn.loadArg(1);
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL,
				Reflector.getInternalNameFromTypeName(metaClass.getParentType().getTypeName()),
				ByteCodeMethods.RUN_METHOD.getName(), ByteCodeMethods.RUN_METHOD.getDescriptor(), false);
		gn.returnValue();
	}

}
