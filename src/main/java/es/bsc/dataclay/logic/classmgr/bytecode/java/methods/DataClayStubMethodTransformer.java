
package es.bsc.dataclay.logic.classmgr.bytecode.java.methods;

import java.lang.reflect.Modifier;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import es.bsc.dataclay.logic.classmgr.bytecode.java.ByteCodeConstants;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeFieldNames;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethods;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethodsNames;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeTypes;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.management.classmgr.Annotation;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;

public class DataClayStubMethodTransformer extends DataClayMethodTransformer {

	public DataClayStubMethodTransformer(MethodVisitor next, int accessFlags, String methodName, String methodDescriptor, String newclassDesc, Operation theoperation,
			Implementation theimpl, final List<Property> properties) {
		super(next, accessFlags, methodName, methodDescriptor, newclassDesc, theoperation, theimpl, properties);
	}

	@Override
	protected void onMethodEnter() {
		// Code injection (in constructors, just after super call)
		if (operation.getName().equals(ByteCodeMethodsNames.INIT_MTHD)
				|| operation.getName().equals(ByteCodeMethodsNames.CLINIT_MTHD)
				|| Modifier.isStatic(operation.getJavaOperationInfo().getModifier())) {
			// Currently not injecting code for constructors or statics
			return;
		}

		injectStartMethod(this, this.methodDesc, classDesc, operation.getName(), operation.getNameAndDescriptor(),
				implementation.getDataClayID().toString(), null, null);

	}

	protected static void injectSetterStartMethod(final GeneratorAdapter genAd,
			final String methodDescriptor,
			final String thisclassDesc, final String opName,
			final String opNameAndDesc,
			final String implIDAsStr,
			final List<Annotation> annotations) {

		/**
		 * if (isPersistent) { Object[] params = ... super.executeRemote(signature, params); }
		 */
		final String setterOpNameAndDesc = "$$set" + opNameAndDesc;

		final Type returnType = Type.getReturnType(methodDescriptor);
		final Type classType = Type.getType(thisclassDesc);

		// Check if isPersistent
		genAd.loadThis(); // Stack: <This>
		// this.checkCast(ByteCodeTypes.DCOBJ);
		final Method methodToCall = ByteCodeMethods.DCOBJ_IS_PERSISTENT;
		final int opCode = Opcodes.IFEQ;

		genAd.invokeVirtual(classType, methodToCall);
		final Label execLocalLabel = genAd.newLabel(); // Stack: Z
		genAd.ifZCmp(opCode, execLocalLabel); // Stack:


		// Call executeRemote
		genAd.loadThis(); // Stack: <This>
		// this.checkCast(ByteCodeTypes.DCOBJ);
		genAd.visitLdcInsn(setterOpNameAndDesc);
		genAd.visitLdcInsn(implIDAsStr);
		genAd.loadArgArray();

		genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_EXECUTE_IMPL); // Stack: <ResultObj>
		// Unbox: cast Object -> Integer and then call intValue (for example)
		genAd.unbox(returnType);
		genAd.pop();

		genAd.returnValue();
		genAd.visitLabel(execLocalLabel);
	}

	protected static void injectStartMethod(final GeneratorAdapter genAd,
			final String methodDescriptor,
			final String thisclassDesc, final String opName,
			final String opNameAndDesc,
			final String implIDAsStr,
			final ImplementationID prefetchingID, final MetaClassID prefetchingClassID) {

		/**
		 * if (isPersistent) { Object[] params = ... super.executeRemote(signature, params); }
		 */

		final Type returnType = Type.getReturnType(methodDescriptor);
		final Type classType = Type.getType(thisclassDesc);

		// DEBUG
		if (Configuration.Flags.ADD_DEBUG_INFO_ON_METHODS.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.DCOBJ,
					ByteCodeFieldNames.DEBUG_ENABLED_FIELDNAME,
					Type.BOOLEAN_TYPE);
			final Label isNotDebugLabel = genAd.newLabel(); // Stack: Z
			genAd.ifZCmp(Opcodes.IFEQ, isNotDebugLabel); // Stack:
			genAd.loadThis();
			genAd.visitLdcInsn(opNameAndDesc);
			genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_DEBUG_START);
			genAd.visitLabel(isNotDebugLabel);

		}

		// Check if isPersistent
		genAd.loadThis(); // Stack: <This>
		// this.checkCast(ByteCodeTypes.DCOBJ);
		final Method methodToCall = ByteCodeMethods.DCOBJ_IS_PERSISTENT;
		final int opCode = Opcodes.IFEQ;

		genAd.invokeVirtual(classType, methodToCall);
		final Label execLocalLabel = genAd.newLabel(); // Stack: Z
		genAd.ifZCmp(opCode, execLocalLabel); // Stack:

		// Call executeRemote
		genAd.loadThis(); // Stack: <This>
		// this.checkCast(ByteCodeTypes.DCOBJ);
		genAd.visitLdcInsn(opNameAndDesc);
		genAd.visitLdcInsn(implIDAsStr);
		genAd.loadArgArray();

		genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_EXECUTE_IMPL); // Stack: <ResultObj>
		// Unbox: cast Object -> Integer and then call intValue (for example)
		genAd.unbox(returnType);
		genAd.returnValue();
		genAd.visitLabel(execLocalLabel);
	}
	

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
		// Remove all methodName$$...
		String actualName = name;
		if (name.contains(ByteCodeConstants.DATACLAY_PREFIX_MARK)) {
			actualName = name.substring(0, name.indexOf(ByteCodeConstants.DATACLAY_PREFIX_MARK));
		}
		super.visitMethodInsn(opcode, owner, actualName, desc, itf);
	}
}
