
/**
 * 
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.wrappers;

import java.util.Collection;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.commons.Method;

import es.bsc.dataclay.logic.classmgr.bytecode.java.SerializationCodeGenerator;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethods;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethodsNames;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeTypes;
import es.bsc.dataclay.logic.classmgr.bytecode.java.switches.DataClaySwitchByImplementationID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * Run table switch for wrapping parameters.
 */
public abstract class WrapParametersAndReturnTableSwitchGenerator extends DataClaySwitchByImplementationID {

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
	protected WrapParametersAndReturnTableSwitchGenerator(
			final MetaClass themetaClass,
			final StubInfo thestubInfo, final boolean theisExecClass,
			final GeneratorAdapter thegn, final InstructionAdapter theia) {
		super(themetaClass, thestubInfo, theisExecClass, thegn, theia);
	}

	/**
	 * Get types to wrap
	 * @param operation
	 *            Operation
	 * @return Types to wrap
	 */
	protected abstract Collection<es.bsc.dataclay.util.management.classmgr.Type> getTypesToWrap(final Operation operation);

	/**
	 * Get parameter
	 * @param idx
	 *            Index of parameter
	 */
	protected abstract void getParameter(int idx);

	/**
	 * Check if nothing to serialize
	 * @param operation
	 *            Op. to check.
	 * @return TRUE if nothing to serialize
	 */
	protected abstract boolean checkIfNothingToSerialize(final Operation operation);

	@Override
	public final void generateCase(final Implementation impl) {

		final Operation operation = metaClass.getOperation(impl.getOperationID());

		if (checkIfNothingToSerialize(operation)) {
			return;
		}

		// Create a new List<DataClaySerializable>
		final int listWrappers = gn.newLocal(ByteCodeTypes.LIST);
		gn.newInstance(ByteCodeTypes.ARRLIST);
		gn.dup();
		gn.invokeConstructor(ByteCodeTypes.ARRLIST, ByteCodeMethods.EMPTY_CONSTRUCTOR);
		gn.storeLocal(listWrappers);

		final Collection<es.bsc.dataclay.util.management.classmgr.Type> typesToWrap = getTypesToWrap(operation);

		int k = 0;
		for (final es.bsc.dataclay.util.management.classmgr.Type paramDcType : typesToWrap) {

			// CODE: immutableparams.add(new Wrapper(param[paramIdx])
			gn.loadLocal(listWrappers);

			final int curParamLocal = gn.newLocal(ByteCodeTypes.OBJECT);
			this.getParameter(k);
			gn.storeLocal(curParamLocal);

			final String typeName = paramDcType.getTypeName();
			if (Reflector.isImmutableTypeName(typeName)
					|| Reflector.isPrimitiveTypeName(typeName)
					|| Reflector.isJavaTypeName(typeName)
					|| Reflector.isArrayTypeName(typeName)
					|| typeName.equals(ObjectID.class.getName())) {
				// =========== IMMUTABLES =============== //
				// =========== LANGUAGE =============== //

				// Wrap immutable
				// Find wrapper type.
				final Tuple<Type, Type> wrapperTypes = SerializationCodeGenerator.findWrapperType(typeName,
						paramDcType.getDescriptor());
				final Type wrapperType = wrapperTypes.getFirst();
				final Type paramType = wrapperTypes.getSecond();
				// Get constructor. Constructor always contains as the argument the original
				// type to wrap.
				final Method wrapperConstructor = new Method(ByteCodeMethodsNames.INIT_MTHD,
						Type.VOID_TYPE, new Type[] { paramType });

				gn.newInstance(wrapperType);
				gn.dup();
				gn.loadLocal(curParamLocal);
				gn.checkCast(paramType);
				gn.invokeConstructor(wrapperType, wrapperConstructor);

			} else {
				// If DCobject just add
				gn.loadLocal(curParamLocal);
			}

			gn.invokeInterface(ByteCodeTypes.LIST, ByteCodeMethods.LIST_ADD);
			gn.pop(); // discard result of put.

			k++;
		}

		gn.loadLocal(listWrappers);
		gn.returnValue();
	}

}
