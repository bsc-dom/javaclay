
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
import es.bsc.dataclay.paraver.ParaverEventType;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.management.classmgr.Annotation;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.replication.Replication;

/**
 * This class injects code in a concrete method that is executed in dataClay execution classes / stubs.
 *
 */
public class DataClayExecutionMethodTransformer extends DataClayMethodTransformer {

	/**
	 * Constructor of the method transformer.
	 * @param next Next visitor after the method is transformed (visitor pattern)
	 * @param accessFlags Method access flags
	 * @param methodName Method name being transformed
	 * @param methodDescriptor Method descriptor 
	 * @param newclassDesc Descriptor of the class containing the method
	 * @param theoperation DataClay operation information
	 * @param theimpl DataClay implementation information
	 * @param properties DataClay properties
	 */
	public DataClayExecutionMethodTransformer(final MethodVisitor next, final int accessFlags, final String methodName,
			final String methodDescriptor, final String newclassDesc, final Operation theoperation,
			final Implementation theimpl, final List<Property> properties) {
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

		ImplementationID implIDPF = null;
		MetaClassID classIDPF = null;
		if (implementation.getPrefetchingInfo() != null
				&& implementation.getPrefetchingInfo().getInjectPrefetchingCall()) {
			implIDPF = implementation.getPrefetchingInfo().getPrefetchingImplementationID();
			classIDPF = implementation.getPrefetchingInfo().getPrefetchingClassID();
		}

		injectStartMethod(this, this.methodDesc, classDesc, operation.getName(), operation.getNameAndDescriptor(),
				implementation.getDataClayID().toString(), implIDPF, classIDPF);

	}

	/**
	 * Inject code at the beginning of a dataClay setter ($$setField)
	 * @param genAd ASM bytecode generator - helper
	 * @param methodDescriptor Setter method descriptor
	 * @param thisclassDesc Descriptor of the class containing the method
	 * @param opName Setter operation name
	 * @param opNameAndDesc Setter operation name and descriptor
	 * @param implIDAsStr dataClay Implementation ID of the setter represented as string
 	 * @param annotations Annotations of the setter defined by the user 
	 */
	protected static void injectSetterStartMethod(final GeneratorAdapter genAd, final String methodDescriptor,
			final String thisclassDesc, final String opName, final String opNameAndDesc, final String implIDAsStr,
			final List<Annotation> annotations) {

		/**
		 * if (isPersistent) { Object[] params = ... super.executeRemote(signature, params); }
		 */
		final String setterOpNameAndDesc = "$$set" + opNameAndDesc;
		
		final Type returnType = Type.getReturnType(methodDescriptor);
		final Type classType = Type.getType(thisclassDesc);

		// DEBUG
		if (Configuration.Flags.ADD_DEBUG_INFO_ON_METHODS.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.DCOBJ, ByteCodeFieldNames.DEBUG_ENABLED_FIELDNAME, Type.BOOLEAN_TYPE);
			final Label isNotDebugLabel = genAd.newLabel(); // Stack: Z
			genAd.ifZCmp(Opcodes.IFEQ, isNotDebugLabel); // Stack:
			genAd.loadThis();
			genAd.visitLdcInsn(setterOpNameAndDesc);
			genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_DEBUG_START);
			genAd.visitLabel(isNotDebugLabel);

		}
		// Check if isPersistent
		genAd.loadThis(); // Stack: <This>
		// this.checkCast(ByteCodeTypes.DCOBJ);
		final Method methodToCall = ByteCodeMethods.DCOBJ_IS_LOADED;
		final int opCode = Opcodes.IFNE;

		genAd.invokeVirtual(classType, methodToCall);
		final Label execLocalLabel = genAd.newLabel(); // Stack: Z
		genAd.ifZCmp(opCode, execLocalLabel); // Stack:

		// PARAVER
		if (Configuration.Flags.PARAVER_INTERCEPTOR_BYTECODE.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.PARAVER_EVENT_TYPE, ParaverEventType.EXIT_LOCAL_METHOD.name(),
					ByteCodeTypes.PARAVER_EVENT_TYPE);

			genAd.visitLdcInsn(thisclassDesc + "." + setterOpNameAndDesc);
			genAd.invokeStatic(ByteCodeTypes.PARAVER, ByteCodeMethods.PARAVER_TRACE);
		}

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

	/**
	 * Inject code at the beginning of a dataClay setter for replication ($$setUpdate$$Field)
	 * @param genAd ASM bytecode generator - helper
	 * @param methodDescriptor Setter method descriptor
	 * @param thisclassDesc Descriptor of the class containing the method
	 * @param opName Setter operation name
	 * @param opNameAndDesc Setter operation name and descriptor
	 * @param implIDAsStr dataClay Implementation ID of the setter represented as string
 	 * @param annotations Annotations of the setter defined by the user 
	 */
	protected static void injectReplicatedStartMethod(final GeneratorAdapter genAd, final String methodDescriptor,
			final String thisclassDesc, final String opName, final String opNameAndDesc, final String implIDAsStr,
			final List<Annotation> annotations) {
		final String setterOpNameAndDesc = ByteCodeConstants.DATACLAY_SET_UPDATE_PREFIX + opNameAndDesc;

		final Type returnType = Type.getReturnType(methodDescriptor);
		final Type classType = Type.getType(thisclassDesc);

		// DEBUG
		if (Configuration.Flags.ADD_DEBUG_INFO_ON_METHODS.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.DCOBJ, ByteCodeFieldNames.DEBUG_ENABLED_FIELDNAME, Type.BOOLEAN_TYPE);
			final Label isNotDebugLabel = genAd.newLabel(); // Stack: Z
			genAd.ifZCmp(Opcodes.IFEQ, isNotDebugLabel); // Stack:
			genAd.loadThis();
			genAd.visitLdcInsn(setterOpNameAndDesc);
			genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_DEBUG_START);
			genAd.visitLabel(isNotDebugLabel);

		}
		
		int opCode = Opcodes.IFNE;

		// genAd.invokeStatic(Type.getType(Boolean.class), new Method("valueOf",
		// ByteCodeTypes.BOOLEAN, new Type[] { Type.BOOLEAN_TYPE
		// }));
		genAd.loadArg(1);
		genAd.unbox(Type.BOOLEAN_TYPE);
		final Label runRemote = genAd.newLabel();
		genAd.ifZCmp(opCode, runRemote);

		genAd.push(true);
		genAd.box(Type.BOOLEAN_TYPE);
		genAd.storeArg(1);

		// Call getMasterLocation
		genAd.loadThis();
		// genAd.invokeVirtual(ByteCodeTypes.BackendID,
		// ByteCodeMethods.DCOBJ_GET_MASTER_LOCATION);

		// Call executeRemote
		genAd.loadThis();
		// genAd.push(ByteCodeTypes.BackendID);
		genAd.invokeVirtual(ByteCodeTypes.DCOBJ, ByteCodeMethods.DCOBJ_GET_MASTER_LOCATION);
		genAd.visitLdcInsn(implIDAsStr);
		genAd.loadArgArray();

		genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_RUN_REMOTE); 
		genAd.unbox(returnType);
		genAd.pop();

		genAd.returnValue();
		genAd.visitLabel(runRemote);

		// Check if is loaded
		genAd.loadThis(); // Stack: <This>
		// this.checkCast(ByteCodeTypes.DCOBJ);
		opCode = Opcodes.IFNE;

		genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_IS_LOADED);

		final Label execLocal = genAd.newLabel(); // Stack: Z
		genAd.ifZCmp(opCode, execLocal); // Stack:

		// PARAVER
		if (Configuration.Flags.PARAVER_INTERCEPTOR_BYTECODE.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.PARAVER_EVENT_TYPE, ParaverEventType.EXIT_LOCAL_METHOD.name(),
					ByteCodeTypes.PARAVER_EVENT_TYPE);

			genAd.visitLdcInsn(thisclassDesc + "." + setterOpNameAndDesc);
			genAd.invokeStatic(ByteCodeTypes.PARAVER, ByteCodeMethods.PARAVER_TRACE);
		}

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
		genAd.visitLabel(execLocal);
	}

	/**
	 * Inject code at the end of a dataClay setter for replication ($$setUpdate$$Field)
	 * @param genAd ASM bytecode generator - helper
	 * @param methodDescriptor Setter method descriptor
	 * @param thisclassDesc Descriptor of the class containing the method
	 * @param opNameAndDesc Setter operation name and descriptor
	 * @param implIDAsStr dataClay Implementation ID of the setter represented as string
	 * @param updateImplIDAsStr update implementation id of the setter as string
 	 * @param annotations Annotations of the setter defined by the user 
	 */
	public static void injectReplicatedEndMethod(final GeneratorAdapter genAd, final String methodDescriptor,
			final String thisclassDesc, final String opNameAndDesc, final String implIDAsStr,
			final String updateImplIDAsStr,
			final List<Annotation> annotations) {
		final String setterOpNameAndDesc = "$$set" + opNameAndDesc;

		// PARAVER
		if (Configuration.Flags.PARAVER_INTERCEPTOR_BYTECODE.getBooleanValue()) {
			if (!opNameAndDesc.matches(".*\\$.*")) {
				genAd.getStatic(ByteCodeTypes.PARAVER_EVENT_TYPE, ParaverEventType.EXIT_LOCAL_METHOD.name(),
						ByteCodeTypes.PARAVER_EVENT_TYPE);
				genAd.visitLdcInsn(thisclassDesc + "." + opNameAndDesc);
				genAd.invokeStatic(ByteCodeTypes.PARAVER, ByteCodeMethods.PARAVER_TRACE);
			}
		}

		final Type classType = Type.getType(thisclassDesc);
		final String desc = opNameAndDesc.split("\\(")[1];
		final String paramTypeDesc = desc.split("L")[0];
		for (final Annotation a : annotations) {
			if (a.getDescr().equals(Replication.afterUpdateAnnotationDescr)) {
				injectMethodFromAnnotation(genAd, a, implIDAsStr, updateImplIDAsStr, paramTypeDesc);
				break;
			}
		}

		if (Configuration.Flags.ADD_DEBUG_INFO_ON_METHODS.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.DCOBJ, ByteCodeFieldNames.DEBUG_ENABLED_FIELDNAME, Type.BOOLEAN_TYPE);
			final Label isNotDebugLabel = genAd.newLabel(); // Stack: Z
			genAd.ifZCmp(Opcodes.IFEQ, isNotDebugLabel); // Stack:
			genAd.loadThis();
			genAd.visitLdcInsn(setterOpNameAndDesc);
			genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_DEBUG_END);
			genAd.visitLabel(isNotDebugLabel);
		}

		/** endMethod(); */
		// genAd.loadThis();
		// genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_END_METHOD);
	}

	/**
	 * Inject call to a concrete method depending on the method defined in an annotation for a replication 
	 * behaviour.
	 * @param genAd ASM bytecode generator - helper
	 * @param a Annotation of the method
	 * @param implementationID dataClay Implementation ID of the setter represented as string
	 * @param updateImplementationID dataClay Implementation ID of the set updated as string
	 * @param propertyTypeDesc Descriptor of the type being replicated
	 */
	public static void injectMethodFromAnnotation(final GeneratorAdapter genAd, final Annotation a,
			final String implementationID, final String updateImplementationID, final String propertyTypeDesc) {
		genAd.loadThis();
		genAd.visitLdcInsn(implementationID);
		genAd.visitLdcInsn(updateImplementationID);

		genAd.loadArg(0);
		if (Reflector.isPrimitiveSignature(propertyTypeDesc)) { 
			genAd.box(Type.getType(propertyTypeDesc));
		}
		
		final Method toCall = new Method(a.getParameter("method"), Type.VOID_TYPE,
				new Type[] { ByteCodeTypes.DATACLAY_OBJECT, ByteCodeTypes.STRING, ByteCodeTypes.STRING, ByteCodeTypes.OBJECT });
		final String paramTypeSignature = a.getParameter("clazz").replace('.', '/');
		final Type paramType = Type.getType("L" + paramTypeSignature + ";");
		genAd.invokeStatic(paramType, toCall);
	}

	/**
	 * Inject code at the beginning of a dataClay registered method
	 * @param genAd ASM bytecode generator - helper
	 * @param methodDescriptor Setter method descriptor
	 * @param thisclassDesc Descriptor of the class containing the method
	 * @param opName Setter operation name
	 * @param opNameAndDesc Setter operation name and descriptor
	 * @param implIDAsStr dataClay Implementation ID of the setter represented as string
 	 * @param prefetchingID ID of implementation method to execute in case of prefetching 
 	 * @param prefetchingClassID ID of the class containing the prefetching method defined before
	 */
	protected static void injectStartMethod(final GeneratorAdapter genAd, final String methodDescriptor,
			final String thisclassDesc, final String opName, final String opNameAndDesc, final String implIDAsStr,
			final ImplementationID prefetchingID, final MetaClassID prefetchingClassID) {

		/**
		 * if (isPersistent) { Object[] params = ... super.executeRemote(signature, params); }
		 */

		// PARAVER
		if (Configuration.Flags.PARAVER_INTERCEPTOR_BYTECODE.getBooleanValue()) {
			if (!opNameAndDesc.matches(".*\\$.*")) {
				genAd.getStatic(ByteCodeTypes.PARAVER_EVENT_TYPE, ParaverEventType.ENTER_LOCAL_METHOD.name(),
						ByteCodeTypes.PARAVER_EVENT_TYPE);
				genAd.visitLdcInsn(thisclassDesc + "." + opNameAndDesc);
				genAd.invokeStatic(ByteCodeTypes.PARAVER, ByteCodeMethods.PARAVER_TRACE);
			}
		}

		final Type returnType = Type.getReturnType(methodDescriptor);
		final Type classType = Type.getType(thisclassDesc);

		// DEBUG
		if (Configuration.Flags.ADD_DEBUG_INFO_ON_METHODS.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.DCOBJ, ByteCodeFieldNames.DEBUG_ENABLED_FIELDNAME, Type.BOOLEAN_TYPE);
			final Label isNotDebugLabel = genAd.newLabel(); // Stack: Z
			genAd.ifZCmp(Opcodes.IFEQ, isNotDebugLabel); // Stack:
			genAd.loadThis();
			genAd.visitLdcInsn(opNameAndDesc);
			genAd.invokeVirtual(classType, ByteCodeMethods.DCOBJ_DEBUG_START);
			genAd.visitLabel(isNotDebugLabel);

		}

		// PREFETCHING TASK
		if (prefetchingID != null) {
			if (!opName.startsWith("$$get") && !opName.startsWith("$$set")) {

				genAd.loadThis(); // Stack: <This> final String pfClassDesc = "L" +

				genAd.loadThis(); // Stack: <This> final String pfClassDesc = "L" +
				genAd.getField(classType, ByteCodeFieldNames.OBJECTID_FIELDNAME, ByteCodeTypes.OBJID);

				// ==== IMPLID === /
				genAd.newInstance(ByteCodeTypes.IMPLID);
				genAd.dup();

				genAd.newInstance(ByteCodeTypes.UUID);
				genAd.dup();
				genAd.visitLdcInsn(prefetchingID.getId().getMostSignificantBits());
				genAd.visitLdcInsn(prefetchingID.getId().getLeastSignificantBits());
				genAd.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.UUID.getInternalName(),
						ByteCodeMethods.UUID_INIT_METHOD.getName(), ByteCodeMethods.UUID_INIT_METHOD.getDescriptor(),
						false);

				genAd.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.IMPLID.getInternalName(),
						ByteCodeMethods.IMPLID_INIT_METHOD.getName(),
						ByteCodeMethods.IMPLID_INIT_METHOD.getDescriptor(), false);

				// ==== CLASSID === /
				genAd.newInstance(ByteCodeTypes.MCLASSID);
				genAd.dup();

				genAd.newInstance(ByteCodeTypes.UUID);
				genAd.dup();
				genAd.visitLdcInsn(prefetchingClassID.getId().getMostSignificantBits());
				genAd.visitLdcInsn(prefetchingClassID.getId().getLeastSignificantBits());
				genAd.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.UUID.getInternalName(),
						ByteCodeMethods.UUID_INIT_METHOD.getName(), ByteCodeMethods.UUID_INIT_METHOD.getDescriptor(),
						false);
				genAd.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.MCLASSID.getInternalName(),
						ByteCodeMethods.MCLASSID_INIT_METHOD.getName(),
						ByteCodeMethods.MCLASSID_INIT_METHOD.getDescriptor(), false);

				genAd.visitMethodInsn(Opcodes.INVOKESPECIAL, classType.getInternalName(),
						ByteCodeMethods.DCOBJ_ADD_LAZY_TASK.getName(),
						ByteCodeMethods.DCOBJ_ADD_LAZY_TASK.getDescriptor(), false);

				// call super.add... genAd.invokeVirtual(classType,
				// ByteCodeMethods.DCOBJ_ADD_LAZY_TASK);

			}
		}

		// Check if isPersistent
		genAd.loadThis(); // Stack: <This>
		// this.checkCast(ByteCodeTypes.DCOBJ);
		final Method methodToCall = ByteCodeMethods.DCOBJ_IS_LOADED;
		final int opCode = Opcodes.IFNE;

		genAd.invokeVirtual(classType, methodToCall);
		final Label execLocalLabel = genAd.newLabel(); // Stack: Z
		genAd.ifZCmp(opCode, execLocalLabel); // Stack:

		// PARAVER
		if (Configuration.Flags.PARAVER_INTERCEPTOR_BYTECODE.getBooleanValue()) {
			genAd.getStatic(ByteCodeTypes.PARAVER_EVENT_TYPE, ParaverEventType.EXIT_LOCAL_METHOD.name(),
					ByteCodeTypes.PARAVER_EVENT_TYPE);

			genAd.visitLdcInsn(thisclassDesc + "." + opNameAndDesc);
			genAd.invokeStatic(ByteCodeTypes.PARAVER, ByteCodeMethods.PARAVER_TRACE);
		}

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
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		// Replace by setter and getter if not in $$set and $$get
		if (opcode == Opcodes.PUTFIELD) {
			if (name.startsWith(ByteCodeConstants.DATACLAY_PREFIX_MARK + "set")) {
				// don't inject code, just put field
				super.visitFieldInsn(opcode, owner, name, desc);
			} else {
				// change to a method instruction
				String setterName = ByteCodeConstants.DATACLAY_PREFIX_MARK + "set" + name;
				String setterDesc = "(" + desc + ")V";
				boolean inMaster = false, isReplicated = false;
				for (final Property p : properties) {
					if (p.getName().equals(name)) {
						isReplicated = p.isReplicated();
						inMaster = p.getInMaster();
						break;
					}
				}
				if (isReplicated) {
					setterName = ByteCodeConstants.DATACLAY_SET_UPDATE_PREFIX + name;
					setterDesc = "(" + desc + "Ljava/lang/Boolean;)V";
					super.push(inMaster);
					super.box(Type.BOOLEAN_TYPE);
				}
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
}
