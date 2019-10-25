
/**
 * 
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.wrappers;

import java.util.Collection;

import org.objectweb.asm.Label;
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
public abstract class SetWrapperParametersAndReturnSwitchGenerator extends DataClaySwitchByImplementationID {

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
	protected SetWrapperParametersAndReturnSwitchGenerator(
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
	 * Check if nothing to serialize
	 * @param operation
	 *            Op. to check.
	 * @return TRUE if nothing to serialize.
	 */
	protected abstract boolean checkIfNothingToSerialize(final Operation operation);

	@Override
	public final void generateCase(final Implementation impl) {

		final Operation operation = metaClass.getOperation(impl.getOperationID());
		if (checkIfNothingToSerialize(operation) || operation.getName().startsWith("$$setupdate$$") && !isExecClass) {
			return;
		}

		final Collection<es.bsc.dataclay.util.management.classmgr.Type> typesToWrap = getTypesToWrap(operation);

		int k = 0;
		for (final es.bsc.dataclay.util.management.classmgr.Type paramDcType : typesToWrap) {

			Method methodToCall = null;
			Type typeToUse = null;
			final String typeName = paramDcType.getTypeName();
			if (Reflector.isImmutableTypeName(typeName)
					|| Reflector.isPrimitiveTypeName(typeName)
					|| Reflector.isJavaTypeName(typeName)
					|| Reflector.isArrayTypeName(typeName)
					|| typeName.equals(ObjectID.class.getName())) {

				if (Reflector.isImmutableTypeName(typeName)
						|| Reflector.isPrimitiveTypeName(typeName)
						|| typeName.equals(ObjectID.class.getName())) {
					// =========== IMMUTABLES =============== //
					methodToCall = ByteCodeMethods.GET_IMM_OBJS;
					typeToUse = ByteCodeTypes.IMM_PARAM_RETURN;
				} else if (Reflector.isJavaTypeName(typeName)
						|| Reflector.isArrayTypeName(typeName)) {
					methodToCall = ByteCodeMethods.GET_LANG_OBJS;
					typeToUse = ByteCodeTypes.LANG_PARAM_RETURN;
				}

				// Wrap immutable
				// Find wrapper type.
				final Tuple<Type, Type> wrapperTypes = SerializationCodeGenerator.findWrapperType(typeName,
						paramDcType.getDescriptor());
				final Type wrapperType = wrapperTypes.getFirst();
				// Get constructor. Constructor always contains as the argument the original
				// type to wrap.
				final Method wrapperConstructor = new Method(ByteCodeMethodsNames.INIT_MTHD,
						Type.VOID_TYPE, new Type[] {});

				// CODE:
				// serializedParamsAndReturn.getImmObjs().get(idx).setWrapper(new Wrapper());
				final int curVar = gn.newLocal(typeToUse);
				gn.loadArg(1);
				gn.invokeVirtual(ByteCodeTypes.SERIALIZED_PARAMS_OR_RETURN, methodToCall);
				ia.aconst(k);
				gn.box(Type.INT_TYPE);
				gn.invokeInterface(ByteCodeTypes.MAP, ByteCodeMethods.MAP_GET);
				gn.checkCast(typeToUse);
				gn.storeLocal(curVar);
				// check if it is null
				gn.loadLocal(curVar);
				final Label isNullLabel = new Label();
				gn.ifNull(isNullLabel);

				gn.loadLocal(curVar);
				gn.newInstance(wrapperType);
				gn.dup();
				gn.invokeConstructor(wrapperType, wrapperConstructor);
				gn.invokeVirtual(typeToUse, ByteCodeMethods.SET_WRAPPER);

				gn.visitLabel(isNullLabel);
			}
			k++;
		}

		gn.returnValue();
	}

}
