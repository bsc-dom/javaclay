
/**
 * 
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.switches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.InstructionAdapter;

import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethods;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeTypes;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.stubs.StubInfo;

/**
 * Run table switch generator.
 */
public abstract class DataClaySwitchByImplementationID {

	// CHECKSTYLE:OFF

	/** Information of class. */
	protected final MetaClass metaClass;

	/** Stub information for stubs. */
	protected final StubInfo stubInfo;

	/** Indicates if it is an execution class. */
	protected final boolean isExecClass;

	/** Method generator. */
	protected final GeneratorAdapter gn;

	/** Instruction adapter. */
	protected final InstructionAdapter ia;

	// CHECKSTYLE:ON

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
	public DataClaySwitchByImplementationID(
			final MetaClass themetaClass,
			final StubInfo thestubInfo, final boolean theisExecClass,
			final GeneratorAdapter thegn, final InstructionAdapter theia) {
		gn = thegn;
		metaClass = themetaClass;
		ia = theia;
		stubInfo = thestubInfo;
		isExecClass = theisExecClass;
	}

	/**
	 * Generate code for each case.
	 * @param impl
	 *            Implementation of case
	 */
	protected abstract void generateCase(final Implementation impl);

	/**
	 * Generate code for calling to parent for each case.
	 */
	protected abstract void generateCallToParentCase();

	/**
	 * Generate switch case by Implementation ID code (IMPLID is parameter 0)
	 * @param ignoreConstructors
	 *            Indicates if constructors must be ignored.
	 */
	public final void generateCode(final boolean ignoreConstructors) {
		// Code
		gn.visitCode(); // Start code
		gn.loadArg(0);
		gn.invokeVirtual(ByteCodeTypes.IMPLID, ByteCodeMethods.TO_STRING);
		gn.dup();
		final int implIDstr = gn.newLocal(ByteCodeTypes.STRING);
		gn.storeLocal(implIDstr);
		gn.invokeVirtual(ByteCodeTypes.STRING, ByteCodeMethods.HASHCODE);

		// Prepare cases
		final Map<Integer, List<Implementation>> implementationsByHashKey = new HashMap<>();
		for (final Operation op : metaClass.getOperations()) {
			if ((op.getName().equals("<init>") || op.getName().equals("<clinit>")) && ignoreConstructors || op.getName().startsWith("$$setupdate$$") && !isExecClass) {
				continue;
			}

			for (final Implementation impl : op.getImplementations()) {

				if (isExecClass || stubInfo.getImplementationByNameAndSignature(impl.getOpNameAndDescriptor()) != null) {
					final int hashCode = impl.getDataClayID().toString().hashCode();
					List<Implementation> implsInHash = implementationsByHashKey.get(hashCode);
					if (implsInHash == null) {
						implsInHash = new ArrayList<>();
						implementationsByHashKey.put(hashCode, implsInHash);
					}
					implsInHash.add(impl);
				}
			}
		}

		// Prepare keys
		final int[] keys = new int[implementationsByHashKey.size()];
		int curKey = 0;
		for (final Integer implHash : implementationsByHashKey.keySet()) {
			keys[curKey] = implHash;
			curKey++;
		}
		Arrays.sort(keys);

		// Create labels
		final Label[] labels = new Label[keys.length];
		final Map<Integer, Integer> hashCodeToLabelIdx = new HashMap<>();
		for (int i = 0; i < keys.length; ++i) {
			labels[i] = new Label();
			hashCodeToLabelIdx.put(keys[i], i);
		}
		final Label lastLabel = new Label();
		gn.visitLookupSwitchInsn(lastLabel, keys, labels);

		// For each implementation case

		for (final int implHash : keys) {
			final List<Implementation> impls = implementationsByHashKey.get(implHash);
			final int idx = hashCodeToLabelIdx.get(implHash);

			// Get label
			final Label thisLabel = labels[idx];
			gn.visitLabel(thisLabel);
			Label nextInsideThis = lastLabel;
			if (impls.size() > 1) { // More than one impl in this case
				nextInsideThis = new Label();
			}
			for (int j = 0; j < impls.size(); ++j) {
				if (j > 0) {
					gn.visitLabel(nextInsideThis);
					nextInsideThis = new Label();
				}
				final Implementation impl = impls.get(j);
				gn.loadLocal(implIDstr);
				gn.visitLdcInsn(impl.getDataClayID().toString());
				gn.invokeVirtual(ByteCodeTypes.STRING, ByteCodeMethods.EQUALS);
				gn.ifZCmp(Opcodes.IFEQ, nextInsideThis);

				// Generate case
				generateCase(impl);
			}
		}

		gn.visitLabel(lastLabel);

		if (metaClass.getParentType() != null) {
			// call super.run
			generateCallToParentCase();
		} else {
			// Throw illegal argument exception
			gn.throwException(ByteCodeTypes.ILLEGAL_ARGUMENT_EXCEPTION, "[DATACLAY] Internal error: Implementation not available");
		}
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();

	}

}
