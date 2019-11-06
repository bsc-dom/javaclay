
/**
 *
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.methods;

import java.lang.reflect.Modifier;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.GeneratorAdapter;

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

/**
 * DataClay Method visitor.
 */
public abstract class DataClayMethodTransformer extends AdviceAdapter {

	/** DataClay operation used for creation. */
	protected final Operation operation;

	/** DataClay implementation used for creation. */
	protected final Implementation implementation;

	/** Descriptor name of class of method. */
	protected final String classDesc;

	protected final List<Property> properties;

	/**
	 * Constructs a new method visitor
	 * @param next
	 *            Method visitor this method should delegate to.
	 * @param accessFlags
	 *            Access flags
	 * @param methodName
	 *            Method name
	 * @param methodDescriptor
	 *            Method descriptor
	 * @param newclassDesc
	 *            Class descriptor
	 * @param theoperation
	 *            DataClay operation used for creation.
	 * @param theimpl
	 *            the implementation
	 * @param theproperties
	 *            Properties of the class containing the method to transform.
	 */
	public DataClayMethodTransformer(final MethodVisitor next,
			final int accessFlags, final String methodName, final String methodDescriptor,
			final String newclassDesc,
			final Operation theoperation, final Implementation theimpl, final List<Property> theproperties) {
		super(Opcodes.ASM7, next, accessFlags, methodName, methodDescriptor);
		this.operation = theoperation;
		this.classDesc = newclassDesc;
		this.implementation = theimpl;
		this.properties = theproperties;
	}

	/**
	 * Inject entry code
	 * @param genAd
	 *            Generator adapter
	 * @param methodDescriptor
	 *            Method being modified descriptor
	 * @param thisclassDesc
	 *            Descriptor of the class being modified
	 * @param opName
	 *            Operation name
	 * @param opNameAndDesc
	 *            Operation name and descriptor for 'executeRemote'
	 * @param implIDAsStr
	 *            ImplID as string for 'executeRemote'
	 * @param theisExecClass
	 *            Indicates if it is for an execution class.
	 * @param prefetchingID
	 *            ID of the method to call for prefetching or NULL if no prefetching
	 * @param prefetchingClassID
	 *            ID of the class in which pf method is located or NULL if no prefetching.
	 */
	public static void injectStartMethod(final GeneratorAdapter genAd,
			final String methodDescriptor,
			final String thisclassDesc, final String opName,
			final String opNameAndDesc,
			final String implIDAsStr, final boolean theisExecClass,
			final ImplementationID prefetchingID, final MetaClassID prefetchingClassID) {

		if (theisExecClass) {
			DataClayExecutionMethodTransformer.injectStartMethod(genAd, methodDescriptor, thisclassDesc, opName, opNameAndDesc, implIDAsStr, prefetchingID, prefetchingClassID);
		} else {
			DataClayStubMethodTransformer.injectStartMethod(genAd, methodDescriptor, thisclassDesc, opName, opNameAndDesc, implIDAsStr, prefetchingID, prefetchingClassID);

		}
	}

	/**
	 * Inject entry code
	 * @param genAd
	 *            Generator adapter
	 * @param methodDescriptor
	 *            Method being modified descriptor
	 * @param thisclassDesc
	 *            Descriptor of the class being modified
	 * @param opName
	 *            Operation name
	 * @param opNameAndDesc
	 *            Operation name and descriptor for 'executeRemote'
	 * @param implIDAsStr
	 *            ImplID as string for 'executeRemote'
	 * @param theisExecClass
	 *            Indicates if it is for an execution class.
	 * @param annotations
	 *            Annotations of the setter
	 * @param update 
	 * 			  Indicates whether to update or not
	 */
	public static void injectSetterStartMethod(final GeneratorAdapter genAd,
			final String methodDescriptor,
			final String thisclassDesc, final String opName,
			final String opNameAndDesc,
			final String implIDAsStr, final boolean theisExecClass,
			final List<Annotation> annotations, final boolean update) {

		if (theisExecClass) {
			if (update) {
				DataClayExecutionMethodTransformer.injectReplicatedStartMethod(genAd, methodDescriptor, thisclassDesc, opName, opNameAndDesc, implIDAsStr, annotations);
			} else {
				DataClayExecutionMethodTransformer.injectSetterStartMethod(genAd, methodDescriptor, thisclassDesc, opName, opNameAndDesc, implIDAsStr, annotations);
			}
		} else {
			DataClayStubMethodTransformer.injectSetterStartMethod(genAd, methodDescriptor, thisclassDesc, opName, opNameAndDesc, implIDAsStr, annotations);
		}
	}

	@Override
	protected void onMethodExit(final int opcode) {
		// Code injection (in constructors, just after super call)
		if (operation.getName().equals(ByteCodeMethodsNames.INIT_MTHD)
				|| operation.getName().equals(ByteCodeMethodsNames.CLINIT_MTHD)
				|| Modifier.isStatic(operation.getJavaOperationInfo().getModifier())) {
			// Currently not injecting code for constructors or statics
			return;
		}

		injectEndMethod(this, this.methodDesc, classDesc, operation.getNameAndDescriptor(),
				implementation.getDataClayID().toString());
	}

	/**
	 * Inject end method
	 * @param genAd
	 *            Generator adapter
	 * @param methodDescriptor
	 *            Method being modified descriptor
	 * @param thisclassDesc
	 *            Descriptor of the class being modified
	 * @param opNameAndDesc
	 *            Operation name and descriptor for 'executeRemote'
	 * @param implIDAsStr
	 *            ImplID as string for 'executeRemote'
	 */
	public static void injectEndMethod(final GeneratorAdapter genAd,
			final String methodDescriptor,
			final String thisclassDesc, final String opNameAndDesc,
			final String implIDAsStr) {


		final Type classType = Type.getType(thisclassDesc);

		if (Configuration.Flags.ADD_DEBUG_INFO_ON_METHODS.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.DCOBJ,
					ByteCodeFieldNames.DEBUG_ENABLED_FIELDNAME,
					Type.BOOLEAN_TYPE);
			final Label isNotDebugLabel = genAd.newLabel(); // Stack: Z
			genAd.ifZCmp(Opcodes.IFEQ, isNotDebugLabel); // Stack:
			genAd.loadThis();
			genAd.visitLdcInsn(opNameAndDesc);
			genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_DEBUG_END);
			genAd.visitLabel(isNotDebugLabel);
		}

		/** endMethod(); */
		// genAd.loadThis();
		// genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_END_METHOD);
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		// Replace by setter and getter if not in $$set and $$get
		if (opcode == Opcodes.PUTFIELD) {
			if (name.startsWith(ByteCodeConstants.DATACLAY_PREFIX_MARK + "set")) {
				// don't inject code, just put field
				super.visitFieldInsn(opcode, owner, name, desc);
			} else {
				// change to a method instruction
				final String setterName = ByteCodeConstants.DATACLAY_PREFIX_MARK + "set" + name;
				final String setterDesc = "(" + desc + ")V";
				super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, owner, setterName, setterDesc, false);
			}
		} else if (opcode == Opcodes.GETFIELD) {
			if (name.startsWith(ByteCodeConstants.DATACLAY_PREFIX_MARK + "get")) {
				// don't inject code, just put field
				super.visitFieldInsn(opcode, owner, name, desc);
			} else {
				// change to a method instruction
				final String getterName = ByteCodeConstants.DATACLAY_PREFIX_MARK + "get" + name;
				final String getterDesc = "()" + desc;
				super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, owner, getterName, getterDesc, false);
			}
		} else {
			super.visitFieldInsn(opcode, owner, name, desc);
		}
	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		// We know that max stack is original method maxStack + 10 but we let ASM calculate it
		super.visitMaxs(-1, -1); // Calculate it automatically
	}
}
