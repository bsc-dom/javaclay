
/**
 *
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.methods;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.logic.classmgr.bytecode.java.ByteCodeConstants;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethods;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeTypes;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.reflection.Reflector;
import storage.StorageObject;

/**
 * DataClay constructor transformer.
 */
public final class ConstructorMethodTransformer extends AdviceAdapter {

	/** Descriptor name of class of method. */
	private final String classDesc;

	/** Indicates if we are creating execution class. */
	private final boolean isExecClass;

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
	 * @param newisExecClass
	 *            Indicates if method must be transformed into an execution class or not.
	 * @param theparentIsObject
	 *            Indicates if parent is object
	 */
	public ConstructorMethodTransformer(final MethodVisitor next,
			final int accessFlags, final String methodName, final String methodDescriptor,
			final String newclassDesc,
			final boolean newisExecClass,
			final boolean theparentIsObject) {
		super(Opcodes.ASM7, next, accessFlags, methodName, methodDescriptor);
		this.classDesc = newclassDesc;
		this.isExecClass = newisExecClass;
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc,
			final boolean itf) {
		final String dcobjectClassInternalName = Reflector.getInternalNameFromTypeName(DataClayObject.class.getName());
		final String objectClassInternalName = Reflector.getInternalNameFromTypeName(Object.class.getName());
		final String stObjectClassInternalName = Reflector.getInternalNameFromTypeName(StorageObject.class.getName());
		if (opcode == Opcodes.INVOKESPECIAL && owner.equals(objectClassInternalName) && name.equals("<init>")) {
			String newOwner = null;
			if (isExecClass) {
				newOwner = Reflector.getInternalNameFromTypeName(DataClayExecutionObject.class.getName());
			} else {
				newOwner = Reflector.getInternalNameFromTypeName(DataClayObject.class.getName());
			}
			super.visitMethodInsn(opcode, newOwner, name, desc, itf);
		} else if (opcode == Opcodes.INVOKESPECIAL && owner.equals(stObjectClassInternalName) && name.equals("<init>")) {
			String newOwner = null;
			if (isExecClass) {
				newOwner = Reflector.getInternalNameFromTypeName(DataClayExecutionObject.class.getName());
			} else {
				newOwner = Reflector.getInternalNameFromTypeName(DataClayObject.class.getName());
			}
			super.visitMethodInsn(opcode, newOwner, name, desc, itf);
		} else if (opcode == Opcodes.INVOKESPECIAL && owner.equals(dcobjectClassInternalName) && name.equals("<init>")) {
			String newOwner = null;
			if (isExecClass) {
				newOwner = Reflector.getInternalNameFromTypeName(DataClayExecutionObject.class.getName());
			} else {
				newOwner = Reflector.getInternalNameFromTypeName(DataClayObject.class.getName());
			}
			super.visitMethodInsn(opcode, newOwner, name, desc, itf);
		} else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	@Override
	protected void onMethodEnter() {
		/*
		 * if (parentIsObject) { final InstructionAdapter ia = new InstructionAdapter(this); // Call init this.loadThis();
		 * 
		 * ia.aconst(null); if (isExecClass) { this.getStatic(ByteCodeTypes.DCOBJ_STATE, DataClayObjectState.VOLATILE_SERVER.name(),
		 * ByteCodeTypes.DCOBJ_STATE); } else { this.getStatic(ByteCodeTypes.DCOBJ_STATE, DataClayObjectState.VOLATILE_CLIENT.name(),
		 * ByteCodeTypes.DCOBJ_STATE); }
		 * 
		 * this.invokeVirtual(ByteCodeTypes.DCOBJ, ByteCodeMethods.DC_INIT_METHOD); }
		 */

	}

	@Override
	protected void onMethodExit(final int opcode) {

	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		// We know that max stack is original method maxStack + 10 but we let ASM calculate it
		super.visitMaxs(-1, -1); // Calculate it automatically
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		// Replace by setter and getter if not in $$set and $$get
		if (isExecClass) {
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
		} else {
			super.visitFieldInsn(opcode, owner, name, desc);
		}
	}
}
