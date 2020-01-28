
package es.bsc.dataclay.logic.classmgr.bytecode.java;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.commons.Method;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeFieldNames;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethods;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethodsNames;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeTypes;
import es.bsc.dataclay.logic.classmgr.bytecode.java.methods.DataClayExecutionMethodTransformer;
import es.bsc.dataclay.logic.classmgr.bytecode.java.methods.DataClayMethodTransformer;
import es.bsc.dataclay.logic.classmgr.bytecode.java.methods.DataClayStubMethodTransformer;
import es.bsc.dataclay.logic.classmgr.bytecode.java.run.RunTableSwitchGenerator;
import es.bsc.dataclay.logic.classmgr.bytecode.java.wrappers.SetWrapperParametersTableSwitchGenerator;
import es.bsc.dataclay.logic.classmgr.bytecode.java.wrappers.SetWrapperReturnTableSwitchGenerator;
import es.bsc.dataclay.logic.classmgr.bytecode.java.wrappers.WrapParametersTableSwitchGenerator;
import es.bsc.dataclay.logic.classmgr.bytecode.java.wrappers.WrapReturnTableSwitchGenerator;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.management.classmgr.Annotation;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.stubs.ImplementationStubInfo;
import es.bsc.dataclay.util.management.stubs.PropertyStubInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.replication.Replication;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.util.tools.java.DataClayClassVisitor;

/**
 * DataClayClassTransformer.
 */
public final class DataClayClassTransformer extends ClassVisitor {

	/** MetaClass of the stub to create. */
	private final MetaClass metaClass;

	/** Information of which methods, properties, ... the stub must contains. */
	private final StubInfo stubInfo;

	/** Parent class name. */
	private final String parentName;

	/** Indicates if we are generating an execution class. */
	private final boolean isExecClass;


	/**
	 * Constructor
	 * 
	 * @param cv
	 *            Class visitor this class visitor will delegate to.
	 * @param theMetaClass
	 *            MetaClass of the stub to create.
	 * @param thestubInfo
	 *            Information of which methods, properties, ... the stub must contains. Or null if exec. class.
	 * @param newisExecClass
	 *            Indicates if we are generating an execution class
	 */
	public DataClayClassTransformer(final ClassVisitor cv, final MetaClass theMetaClass, final StubInfo thestubInfo,
			final boolean newisExecClass) {
		super(Opcodes.ASM7, cv);
		this.metaClass = theMetaClass;
		this.stubInfo = thestubInfo;
		this.isExecClass = newisExecClass;
		if (metaClass.getParentType() != null) {
			parentName = Reflector.getInternalNameFromTypeName(metaClass.getParentType().getTypeName());
		} else {
			if (this.isExecClass) {
				parentName = ByteCodeTypes.DCEXECOBJ.getInternalName();
			} else {
				parentName = ByteCodeTypes.DCOBJ.getInternalName();
			}
		}
	}

	@Override
	public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
			final Object value) {

		int actualFieldAccess = access & (~Opcodes.ACC_FINAL);

		if (Modifier.isInterface(metaClass.getJavaClassInfo().getModifier())) {
			// if interface, do not modify code
			return super.visitField(actualFieldAccess, name, desc, signature, value);
		}
		if (desc.equals(DataClayClassVisitor.ECA_SIGNATURE)) {
			// ECA fields are always added?
			// Static constructors initialize ecas, and some stubs obtain the static
			// constructor
			// if we do not want users to see the eca fields, change it.
			return cv.visitField(actualFieldAccess, name, desc, signature, value);
		}

		if (isExecClass) {
			return cv.visitField(actualFieldAccess, name, desc, signature, value);
		} else {
			// Check if add field
			final PropertyStubInfo propStubInfo = stubInfo.getPropertyWithName(name);
			if (propStubInfo != null) {
				return cv.visitField(actualFieldAccess, name, desc, signature, value);
			} else {
				return null;
			}
		}
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		if (Modifier.isInterface(metaClass.getJavaClassInfo().getModifier())) {
			// if interface, do not modify code
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
		/**
		 * New methods (enrichments) has different name in bytecode of the class, for example for method setName(), a new method
		 * added will be setName$$1(). For stubs: Therefore, if the stub information indicates that the method present in the
		 * stub must be setName$$1, we just modifies the name, descriptor and signature to setName(). For execution classes all
		 * methods are included.
		 */

		// Take into account that operations has always signature WITHOUT $$1,...
		String nameWithoutMark = name;
		if (name.contains(ByteCodeConstants.DATACLAY_PREFIX_MARK)) {
			nameWithoutMark = name.substring(0, name.indexOf(ByteCodeConstants.DATACLAY_PREFIX_MARK));
		}
		final Operation operation = metaClass.getOperation(nameWithoutMark + desc);
		String actualMethodName = name;
		String actualMethodDesc = desc;
		String actualMethodSignature = signature;

		// Get proper implementation, local implementation id is the code used 'inside'
		// the method
		// remoteImplementationID is used by "executeRemote".
		int implPosition = 0;
		boolean addMethod = false;
		if (isExecClass) {
			// All methods are added
			addMethod = true;
			if (name.contains(ByteCodeConstants.DATACLAY_PREFIX_MARK)) {
				// since there is no enrichments, get first implementation
				implPosition = 0;
			}
		} else {
			// ==== CHECK IF ADD METHOD OR NOT === //
			final ImplementationStubInfo implSpec = stubInfo
					.getImplementationByNameAndSignature(nameWithoutMark + desc);
			final ImplementationID localImplementationID = implSpec.getLocalImplID();
			final Implementation localImpl = operation.getImplementationInOperation(localImplementationID);
			implPosition = localImpl.getPosition();
			actualMethodName = operation.getName();
			actualMethodDesc = operation.getDescriptor();
			actualMethodSignature = operation.getSignature();
			if (name.contains(ByteCodeConstants.DATACLAY_PREFIX_MARK)) {
				// Check if we must add it. If implPosition is 0, it means we should add the
				// default method without
				// any $$.
				if (name.equals(operation.getName() + ByteCodeConstants.DATACLAY_PREFIX_MARK + implPosition)) {
					addMethod = true;
				}
			} else if (implPosition == 0) {
				// Add it if and only if impl position is 0.
				addMethod = true;
			}
		}

		if (addMethod) {

			// Get implementation
			Implementation implToAdd = null;
			for (final Implementation impl : operation.getImplementations()) {
				if (impl.getPosition() == implPosition) {
					implToAdd = impl;
				}
			}

			// Normal method (without $$)
			final MethodVisitor mv = cv.visitMethod(access, actualMethodName, actualMethodDesc, actualMethodSignature,
					exceptions);

			// Check if method has same serialize signature
			if (desc.equals(SerializationCodeGenerator.SERIALIZE_DESCRIPTOR)
					&& name.equals(SerializationCodeGenerator.SERIALIZE_NAME)) {
				return mv; // Do not inject code before serialization method.
			}
			// Check if method has same deserialize signature
			if (desc.equals(SerializationCodeGenerator.DESERIALIZE_DESCRIPTOR)
					&& name.equals(SerializationCodeGenerator.DESERIALIZE_NAME)) {
				return mv; // Do not inject code before serialization method.
			}

			// Inject code TODO refactor
			final DataClayMethodTransformer mVisitor = isExecClass
					? new DataClayExecutionMethodTransformer(mv, access, actualMethodName, actualMethodDesc,
							Reflector.getDescriptorFromTypeName(metaClass.getName()), operation, implToAdd,
							metaClass.getProperties())
							: new DataClayStubMethodTransformer(mv, access, actualMethodName, actualMethodDesc,
									Reflector.getDescriptorFromTypeName(metaClass.getName()), operation, implToAdd,
									metaClass.getProperties());
					return mVisitor;
		}

		return null;

	}

	@Override
	public void visitEnd() {

		/**
		 * Adding new fields or methods: You cannot do this in the visit method, for example, because this may result in a call
		 * to visitField followed by visitSource, visitOuterClass, visitAnnotation or visitAttribute, which is not valid. You
		 * cannot put this new call in the visitSource, visitOuterClass, visitAnnotation or visitAttribute methods, for the same
		 * reason. The only possibilities are the visitInnerClass, visitField, visitMethod or visitEnd methods
		 **/
		if (!Modifier.isInterface(metaClass.getJavaClassInfo().getModifier())) {
			injectFields();
			injectSettersAndGetters();
			injectMethods();
		}
		cv.visitEnd();
	}

	/**
	 * Inject new fields in class
	 */
	private void injectFields() {
		// Add new fields
		if (!isExecClass) {
			// isStubField
			final FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, ByteCodeFieldNames.IS_STUB_FIELDNAME,
					org.objectweb.asm.Type.BOOLEAN_TYPE.getDescriptor(), null, 1);
			fv.visitEnd();
		}
	}

	/**
	 * Inject new methods in class
	 */
	private void injectMethods() {
		// New constructors
		generateConstructorObjectIDState();
		// generateConstructorAlias();
		generateGetByAlias();
		generateDeleteAlias();
		// Serialization methods
		// Check if there is a method with serialize signature
		/*
		 * if (!personalizedSerialization) { SerializationCodeGenerator.generateSerializeMethod(cv, classType,
		 * parentInternalName, fields); } // Check if there is a method with deserialize signature if
		 * (!personalizedDeserialization) { SerializationCodeGenerator.generateDeserializeMethod(cv, classType,
		 * parentInternalName, fields); }
		 */

		this.generateWrapFieldsSerializationMethod();
		this.generateWrapFieldsDeserializationMethod();
		this.generateSetFieldsDeserializationMethod();
		this.generateRunMethod();
		this.generateWrapParametersMethod();
		this.generateWrapReturnMethod();
		this.generateSetWrappersParameters();
		this.generateSetWrappersReturn();

		if (this.isExecClass) {
			this.generateSetAll();
		}
	}

	/**
	 * Inject setters and getters ($$get... and $$set)
	 */
	private void injectSettersAndGetters() {
		if (isExecClass) {
			// All properties
			for (final Property prop : metaClass.getProperties()) {
				generateGetter(prop);
				if (prop.isReplicated()) {
					generateSetter(prop, true);
				}
				generateSetter(prop, false);
			}
		} else {
			// Check if they are in stub
			for (final PropertyStubInfo propInfo : stubInfo.getProperties().values()) {
				final Property prop = metaClass.getProperty(propInfo.getPropertyID());
				generateGetter(prop);
				generateSetter(prop, false);
			}
		}
	}

	/**
	 * Generate getter
	 * 
	 * @param property
	 *            Property for which to generate getter
	 */
	private void generateGetter(final Property property) {

		final Type propertyType = property.getType();
		final String propertyTypeDesc = propertyType.getDescriptor();
		if (propertyTypeDesc.equals(DataClayClassVisitor.ECA_SIGNATURE)) {
			// ECA fields has no getter and setter
			return;
		}
		final String propertyName = property.getName();
		final String propertyTypeSignature = propertyType.getSignature();

		final String classDesc = Reflector.getDescriptorFromTypeName(metaClass.getName());
		final String getterDesc = "()" + propertyTypeDesc;
		String getterSignature = null;
		if (propertyTypeSignature != null) {
			getterSignature = "()" + propertyTypeSignature;
		}

		// Create method header
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "$$get" + propertyName, getterDesc, getterSignature,
				null);

		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, "$$get" + propertyName, getterDesc);

		final org.objectweb.asm.Type propTypeASM = org.objectweb.asm.Type.getType(propertyTypeDesc);
		final org.objectweb.asm.Type ownerTypeASM = org.objectweb.asm.Type.getType(classDesc);

		// Code
		gn.visitCode(); // Start code

		DataClayMethodTransformer.injectStartMethod(gn, getterDesc, classDesc, "$$get" + propertyName,
				"$$get" + propertyName + getterDesc, property.getGetterImplementationID().toString(), isExecClass, null,
				null);

		DataClayMethodTransformer.injectEndMethod(gn, getterDesc, classDesc, "$$get" + propertyName + getterDesc,
				property.getGetterImplementationID().toString());

		if (isExecClass) {
			if (Reflector.isJavaTypeName(propertyType.getTypeName())
					|| Reflector.isJavaPrimitiveOrArraySignature(propertyType.getSignatureOrDescriptor())) {
				// Set dirty flag to true
				gn.loadThis();
				gn.invokeVirtual(ownerTypeASM, ByteCodeMethods.DCOBJ_SETASDIRTY);
			}
		}

		gn.loadThis(); // Load this //Stack: <This>
		gn.getField(ownerTypeASM, propertyName, propTypeASM); // Stack: <Field> <Field> (Can be 2 if long)
		gn.returnValue();

		/** MAX Stack for a Getter: 2 */
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();
	}

	/**
	 * Generate setter
	 * 
	 * @param property
	 *            Property for which to generate setter
	 * @param replicated Indicates if setter was defined to use replication mechanism
	 */
	private void generateSetter(final Property property, final boolean replicated) {

		final Type propertyType = property.getType();
		final String propertyTypeDesc = propertyType.getDescriptor();
		if (propertyTypeDesc.equals(DataClayClassVisitor.ECA_SIGNATURE)) {
			// ECA fields has no setter
			return;
		}

		final String propertyName = property.getName();
		final String classDesc = Reflector.getDescriptorFromTypeName(metaClass.getName());
		final String propertyTypeSignature = propertyType.getSignature();
		final String setterDesc = replicated ? "(" + propertyTypeDesc + "Ljava/lang/Boolean;)V"
				: "(" + propertyTypeDesc + ")V";

		String setterSignature = null;
		if (propertyTypeSignature != null) {
			setterSignature = "(" + propertyTypeSignature + ")V";
		}

		final String set = replicated ? ByteCodeConstants.DATACLAY_SET_UPDATE_PREFIX
				: ByteCodeConstants.DATACLAY_PREFIX_MARK + "set";

		// Create method header
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, set + propertyName, setterDesc, setterSignature,
				null);

		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, set + propertyName, setterDesc);

		final org.objectweb.asm.Type propTypeASM = org.objectweb.asm.Type.getType(propertyTypeDesc);
		final org.objectweb.asm.Type ownerTypeASM = org.objectweb.asm.Type.getType(classDesc);

		// Code
		gn.visitCode(); // Start code

		if (replicated) {
			DataClayMethodTransformer.injectSetterStartMethod(gn, setterDesc, classDesc, propertyName,
					propertyName + setterDesc.replace(")", "Ljava/lang/Boolean;)"),
					property.getUpdateImplementationID().toString(), isExecClass, property.getAnnotations(),
					replicated);

			// TODO needs a deep refactoring: the replicated setter should be generated
			// indipendently from the normal setter
			final List<Annotation> annotations = property.getAnnotations();
			for (final Annotation a : annotations) {
				final String beforeAnnotationDescr = "L" + Replication.BeforeUpdate.class.getName().replace('.', '/')
						+ ";";
				if (a.getDescr().equals(beforeAnnotationDescr)) {
					DataClayExecutionMethodTransformer.injectMethodFromAnnotation(gn, a,
							property.getSetterImplementationID().toString(), 
							property.getUpdateImplementationID().toString(), 
							propertyTypeDesc);
					break;
				}
			}
		} else {
			DataClayMethodTransformer.injectSetterStartMethod(gn, setterDesc, classDesc, propertyName,
					propertyName + setterDesc, property.getSetterImplementationID().toString(), isExecClass,
					property.getAnnotations(), replicated);
		}

		// Set dirty flag to true
		if (isExecClass) {
			gn.loadThis();
			gn.invokeVirtual(ownerTypeASM, ByteCodeMethods.DCOBJ_SETASDIRTY);
		}

		gn.loadThis(); // Stack: <This>
		gn.loadArg(0); // Stack: <This> <Arg> <Arg> (can be two slots if long)
		gn.putField(ownerTypeASM, propertyName, propTypeASM); // Stack: <>

		if (replicated) {
			DataClayExecutionMethodTransformer.injectReplicatedEndMethod(gn, setterDesc, classDesc,
					set + propertyName + setterDesc, property.getSetterImplementationID().toString(),
					property.getUpdateImplementationID().toString(),
					property.getAnnotations());
		} else {
			DataClayMethodTransformer.injectEndMethod(gn, setterDesc, classDesc, set + propertyName + setterDesc,
					property.getSetterImplementationID().toString());
		}

		gn.returnValue();
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();
	}

	/**
	 * Generate setAll method
	 */
	private void generateSetAll() {

		final String classDesc = Reflector.getDescriptorFromTypeName(metaClass.getName());
		final String signature = Reflector.getSignatureFromTypeName(DataClayObject.class.getName());

		final String opDesc = "(" + signature + ")V";

		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethodsNames.SET_ALL_FIELDS, opDesc, null,
				null);

		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, ByteCodeMethodsNames.SET_ALL_FIELDS,
				opDesc);

		// Parameters
		gn.visitParameter("otherObject", Opcodes.ACC_FINAL);

		// Code
		gn.visitCode(); // Start code

		final org.objectweb.asm.Type currentClassType = org.objectweb.asm.Type.getType(classDesc);

		for (final Property property : metaClass.getProperties()) {

			final Type propertyType = property.getType();
			final String propertyName = property.getName();
			final String propertyDesc = propertyType.getDescriptor();
			final org.objectweb.asm.Type currentPropertyType = org.objectweb.asm.Type.getType(propertyDesc);
			final Method getter = new Method("$$get" + propertyName, currentPropertyType, ByteCodeTypes.NO_ARGS);

			// SETTER CALL this.$$setField(...)
			gn.loadThis();

			// GETTER CALL otherObject.$$getField()
			gn.loadArg(0); // Stack: <This> <DataClayObject>
			gn.checkCast(currentClassType); // Stack: <This> <TypeA>
			gn.invokeVirtual(currentClassType, getter);

			final Method setter;
			if (!property.isReplicated()) {
				setter = new Method("$$set" + propertyName, org.objectweb.asm.Type.VOID_TYPE,
						new org.objectweb.asm.Type[] { currentPropertyType });
			} else {
				setter = new Method("$$setUpdate$$" + propertyName, org.objectweb.asm.Type.VOID_TYPE,
						new org.objectweb.asm.Type[] { currentPropertyType, ByteCodeTypes.BOOLEAN });
				gn.push(false);
				gn.box(org.objectweb.asm.Type.BOOLEAN_TYPE);
			}

			// call setter
			gn.invokeVirtual(currentClassType, setter);

		}

		gn.returnValue();
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();

	}

	/**
	 * Generates the constructor that receives ObjectID
	 */
	private void generateConstructorObjectIDState() {

		// Create method header
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.DC_INIT_METHOD.getName(),
				ByteCodeMethods.DC_INIT_METHOD.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC,
				ByteCodeMethods.DC_INIT_METHOD.getName(), ByteCodeMethods.DC_INIT_METHOD.getDescriptor());

		// Code
		gn.visitCode(); // Start code
		gn.loadThis(); // Stack: <This>
		// gn.checkCast(ByteCodeTypes.DCOBJ);
		gn.loadArg(0); // Stack: <This> <Oid>
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, parentName, ByteCodeMethods.DC_INIT_METHOD.getName(),
				ByteCodeMethods.DC_INIT_METHOD.getDescriptor(), false);
		gn.returnValue();
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();
	}

	/**
	 * Generates static getByAlias method
	 */
	private void generateGetByAlias() {
		String fullClassName = metaClass.getName();
		if (this.isExecClass) {
			fullClassName = metaClass.getNamespace() + "." + metaClass.getName();
		}
		generateInternalGetByAliasSafe(ByteCodeMethodsNames.GETOBJ_BY_ALIAS, 
				Reflector.getDescriptorFromTypeName(fullClassName),
				fullClassName);
		generateInternalGetByAliasSafe(ByteCodeMethodsNames.GETOBJ_BY_ALIAS_EXT, 
				Reflector.getDescriptorFromTypeName(ByteCodeTypes.OBJECT.getInternalName()),
				fullClassName);

		generateInternalGetByAliasUnsafe(ByteCodeMethodsNames.GETOBJ_BY_ALIAS, 
				Reflector.getDescriptorFromTypeName(fullClassName),
				fullClassName);
		generateInternalGetByAliasUnsafe(ByteCodeMethodsNames.GETOBJ_BY_ALIAS_EXT, 
				Reflector.getDescriptorFromTypeName(ByteCodeTypes.OBJECT.getInternalName()),
				fullClassName);
	}

	/**
	 * Generate internal getByAlias method.
	 * 
	 * @param methodName Name of the getByAlias method to generate
	 * @param classDesc
	 *            Return type
	 * @param className
	 *            name of the class of the object
	 * 
	 */
	private void generateInternalGetByAliasSafe(final String methodName, 
			final String classDesc, final String className) {
		final String signature = Reflector.getSignatureFromTypeName(String.class.getName());

		final String opDesc = "(" + signature + ")" + classDesc;
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				methodName, opDesc, null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				methodName, opDesc);

		// Parameters
		gn.visitParameter("alias", Opcodes.ACC_FINAL);

		// Code
		gn.visitCode(); // Start code

		// ==== CLASSID === /
		gn.newInstance(ByteCodeTypes.MCLASSID); // Stack: <This> <ClassID>
		gn.dup();

		gn.newInstance(ByteCodeTypes.UUID);
		gn.dup();
		final UUID classID = metaClass.getDataClayID().getId();
		gn.visitLdcInsn(classID.getMostSignificantBits());
		gn.visitLdcInsn(classID.getLeastSignificantBits());
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.UUID.getInternalName(),
				ByteCodeMethods.UUID_INIT_METHOD.getName(), ByteCodeMethods.UUID_INIT_METHOD.getDescriptor(),
				false);
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.MCLASSID.getInternalName(),
				ByteCodeMethods.MCLASSID_INIT_METHOD.getName(),
				ByteCodeMethods.MCLASSID_INIT_METHOD.getDescriptor(), false);


		gn.loadArg(0); // Stack: <This> <ClassID> <Alias>

		gn.push(true); // Stack: <This> <ClassID> <Alias> <true>

		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ByteCodeTypes.DCOBJ.getInternalName(),
				ByteCodeMethods.DCOBJ_GET_BY_ALIAS.getName(), ByteCodeMethods.DCOBJ_GET_BY_ALIAS.getDescriptor(),
				false);
		gn.checkCast(org.objectweb.asm.Type.getType(classDesc));
		gn.returnValue();

		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();
	}

	/**
	 * Generate internal getByAlias method.
	 * 
	 * @param methodName Name of the getByAlias method to generate
	 * @param classDesc
	 *            Return type
	 * @param className
	 *            name of the class of the object
	 * 
	 */
	private void generateInternalGetByAliasUnsafe(final String methodName, 
			final String classDesc, final String className) {
		final String signature = Reflector.getSignatureFromTypeName(String.class.getName())
				+ Reflector.getSignatureFromTypeName("boolean");

		final String opDesc = "(" + signature + ")" + classDesc;
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				methodName, opDesc, null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				methodName, opDesc);

		// Parameters
		gn.visitParameter("alias", Opcodes.ACC_FINAL);

		// Code
		gn.visitCode(); // Start code

		// ==== CLASSID === /
		gn.newInstance(ByteCodeTypes.MCLASSID); // Stack: <This> <ClassID>
		gn.dup();

		gn.newInstance(ByteCodeTypes.UUID);
		gn.dup();
		final UUID classID = metaClass.getDataClayID().getId();
		gn.visitLdcInsn(classID.getMostSignificantBits());
		gn.visitLdcInsn(classID.getLeastSignificantBits());
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.UUID.getInternalName(),
				ByteCodeMethods.UUID_INIT_METHOD.getName(), ByteCodeMethods.UUID_INIT_METHOD.getDescriptor(),
				false);
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL, ByteCodeTypes.MCLASSID.getInternalName(),
				ByteCodeMethods.MCLASSID_INIT_METHOD.getName(),
				ByteCodeMethods.MCLASSID_INIT_METHOD.getDescriptor(), false);


		gn.loadArg(0); // Stack: <This> <ClassID> <Alias>
		gn.loadArg(1); // Stack: <This> <ClassID> <Alias> <Safe>

		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ByteCodeTypes.DCOBJ.getInternalName(),
				ByteCodeMethods.DCOBJ_GET_BY_ALIAS.getName(), ByteCodeMethods.DCOBJ_GET_BY_ALIAS.getDescriptor(),
				false);
		gn.checkCast(org.objectweb.asm.Type.getType(classDesc));
		gn.returnValue();

		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();
	}


	/**
	 * Generates static deleteAlias method
	 */
	private void generateDeleteAlias() {

		// Create method header
		final String signature = Reflector.getSignatureFromTypeName(String.class.getName());
		final String opDesc = "(" + signature + ")V";
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				ByteCodeMethodsNames.DELETE_ALIAS, opDesc, null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				ByteCodeMethodsNames.DELETE_ALIAS, opDesc);

		// Parameters
		gn.visitParameter("alias", Opcodes.ACC_FINAL);

		// Code
		gn.visitCode(); // Start code

		gn.push(metaClass.getName()); // Stack: <This> <ClassName>
		gn.loadArg(0); // Stack: <This> <ClassName> <Alias>
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ByteCodeTypes.DCOBJ.getInternalName(),
				ByteCodeMethods.DCOBJ_DELETE_ALIAS.getName(), ByteCodeMethods.DCOBJ_DELETE_ALIAS.getDescriptor(),
				false);
		gn.returnValue();

		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();
	}

	/**
	 * Generate wrap fields method.
	 */
	private void generateWrapFieldsSerializationMethod() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.WRAP_FIELDS_SER.getName(),
				ByteCodeMethods.WRAP_FIELDS_SER.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC,
				ByteCodeMethods.WRAP_FIELDS_SER.getName(), ByteCodeMethods.WRAP_FIELDS_SER.getDescriptor());

		gn.visitCode();

		// Get class type
		final String classDescriptor = Reflector.getDescriptorFromTypeName(metaClass.getName());
		final org.objectweb.asm.Type classType = org.objectweb.asm.Type.getType(classDescriptor);

		// Create a new List<DataClaySerializable>
		// Check which fields to add
		for (final Property prop : metaClass.getProperties()) {
			final int modProp = prop.getJavaPropertyInfo().getModifier();
			if (Modifier.isStatic(modProp) && Modifier.isFinal(modProp)) {
				continue;
			}
			if (isExecClass || stubInfo.containsProperty(prop.getName())) {

				final String fieldName = prop.getName();
				final Type paramDcType = prop.getType();
				final String typeName = paramDcType.getTypeName(); // "int", "float", ... "java.lang.Integer"...
				final org.objectweb.asm.Type fieldType = org.objectweb.asm.Type.getType(prop.getType().getDescriptor());

				// CODE: wrappedParams.add(new Wrapper(field)
				gn.loadArg(0);

				if (Reflector.isImmutableTypeName(typeName) || Reflector.isPrimitiveTypeName(typeName)
						|| Reflector.isArrayTypeName(typeName) || Reflector.isJavaTypeName(typeName)
						|| typeName.equals(ObjectID.class.getName())) {
					// =========== IMMUTABLES =============== //
					// =========== LANGUAGE =============== //

					// Wrap immutable
					// Find wrapper type.
					final Tuple<org.objectweb.asm.Type, org.objectweb.asm.Type> wrapperTypes = SerializationCodeGenerator
							.findWrapperType(typeName, paramDcType.getDescriptor());
					final org.objectweb.asm.Type wrapperType = wrapperTypes.getFirst();
					final org.objectweb.asm.Type paramType = wrapperTypes.getSecond();
					// Get constructor. Constructor always contains as the argument the original
					// type to wrap.
					final Method wrapperConstructor = new Method(ByteCodeMethodsNames.INIT_MTHD,
							org.objectweb.asm.Type.VOID_TYPE, new org.objectweb.asm.Type[] { paramType });

					gn.newInstance(wrapperType);
					gn.dup();
					gn.loadThis();
					gn.getField(classType, fieldName, fieldType);
					if (!fieldType.equals(paramType)) {
						// If field type is java immutable i.e. not same as param type, we must box it
						gn.box(fieldType);
					} else {
						gn.checkCast(paramType);
					}
					gn.invokeConstructor(wrapperType, wrapperConstructor);

				} else {
					// If DCobject just add
					gn.loadThis();
					gn.getField(classType, fieldName, fieldType);
				}

				gn.invokeInterface(ByteCodeTypes.LIST, ByteCodeMethods.LIST_ADD);
				gn.pop(); // discard result of put.

			}
		}

		if (metaClass.getParentType() != null) {
			gn.loadThis();
			gn.loadArg(0);
			gn.visitMethodInsn(Opcodes.INVOKESPECIAL,
					Reflector.getInternalNameFromTypeName(metaClass.getParentType().getTypeName()),
					ByteCodeMethods.WRAP_FIELDS_SER.getName(), ByteCodeMethods.WRAP_FIELDS_SER.getDescriptor(), false);
			gn.returnValue();
		} else {
			gn.returnValue();
		}

		/** MAX Stack */
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();

	}

	/**
	 * Generate wrap fields method.
	 */
	private void generateWrapFieldsDeserializationMethod() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.WRAP_FIELDS_DESER.getName(),
				ByteCodeMethods.WRAP_FIELDS_DESER.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC,
				ByteCodeMethods.WRAP_FIELDS_DESER.getName(), ByteCodeMethods.WRAP_FIELDS_DESER.getDescriptor());
		final InstructionAdapter ia = new InstructionAdapter(mv);

		gn.visitCode();

		// Create a new List<DataClaySerializable>
		// Check which fields to add
		for (final Property prop : metaClass.getProperties()) {
			final int modProp = prop.getJavaPropertyInfo().getModifier();
			if (Modifier.isStatic(modProp) && Modifier.isFinal(modProp)) {
				continue;
			}
			if (isExecClass || stubInfo.containsProperty(prop.getName())) {

				final Type paramDcType = prop.getType();
				final String typeName = paramDcType.getTypeName(); // "int", "float", ... "java.lang.Integer"...

				// CODE: wrappedParams.add(new Wrapper())
				gn.loadArg(0);

				if (Reflector.isImmutableTypeName(typeName) || Reflector.isPrimitiveTypeName(typeName)
						|| Reflector.isArrayTypeName(typeName) || Reflector.isJavaTypeName(typeName)
						|| typeName.equals(ObjectID.class.getName())) {
					// =========== IMMUTABLES =============== //
					// =========== LANGUAGE =============== //

					// Wrap immutable
					// Find wrapper type.
					final Tuple<org.objectweb.asm.Type, org.objectweb.asm.Type> wrapperTypes = SerializationCodeGenerator
							.findWrapperType(typeName, paramDcType.getDescriptor());
					final org.objectweb.asm.Type wrapperType = wrapperTypes.getFirst();
					// Get constructor. Constructor always contains as the argument the original
					// type to wrap.
					final Method wrapperConstructor = new Method(ByteCodeMethodsNames.INIT_MTHD,
							org.objectweb.asm.Type.VOID_TYPE, new org.objectweb.asm.Type[] {});

					gn.newInstance(wrapperType);
					gn.dup();
					gn.invokeConstructor(wrapperType, wrapperConstructor);

				} else {
					// If DCobject, null means no wrapper
					ia.aconst(null);
				}

				gn.invokeInterface(ByteCodeTypes.LIST, ByteCodeMethods.LIST_ADD);
				gn.pop(); // discard result of put.

			}
		}

		if (metaClass.getParentType() != null) {
			gn.loadThis();
			gn.loadArg(0);
			gn.visitMethodInsn(Opcodes.INVOKESPECIAL,
					Reflector.getInternalNameFromTypeName(metaClass.getParentType().getTypeName()),
					ByteCodeMethods.WRAP_FIELDS_DESER.getName(), ByteCodeMethods.WRAP_FIELDS_DESER.getDescriptor(),
					false);
			gn.returnValue();
		} else {
			gn.returnValue();
		}

		/** MAX Stack */
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();

	}

	/**
	 * Generate set fields deserialization method.
	 */
	private void generateSetFieldsDeserializationMethod() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.SET_FIELDS_DESER.getName(),
				ByteCodeMethods.SET_FIELDS_DESER.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC,
				ByteCodeMethods.SET_FIELDS_DESER.getName(), ByteCodeMethods.SET_FIELDS_DESER.getDescriptor());

		gn.visitCode();

		// Get class type
		final String classDescriptor = Reflector.getDescriptorFromTypeName(metaClass.getName());
		final org.objectweb.asm.Type classType = org.objectweb.asm.Type.getType(classDescriptor);

		final int objVar = gn.newLocal(ByteCodeTypes.OBJECT);

		// Create a new List<DataClaySerializable>
		// Check which fields to add
		for (final Property prop : metaClass.getProperties()) {
			if (isExecClass || stubInfo.containsProperty(prop.getName())) {

				final String fieldName = prop.getName();
				final Type paramDcType = prop.getType();
				final String typeName = paramDcType.getTypeName(); // "int", "float", ... "java.lang.Integer"...
				final org.objectweb.asm.Type fieldType = org.objectweb.asm.Type.getType(prop.getType().getDescriptor());

				// CODE: field = (cast) queue.poll();
				gn.loadArg(0);
				gn.invokeInterface(ByteCodeTypes.JAVA_QUEUE, ByteCodeMethods.QUEUE_POLL);
				gn.storeLocal(objVar);

				gn.loadLocal(objVar);
				final Label continueLabel = new Label();
				gn.ifNull(continueLabel);

				gn.loadThis();
				gn.loadLocal(objVar);

				if (Reflector.isImmutableTypeName(typeName) || Reflector.isPrimitiveTypeName(typeName)) {
					// If field type is java immutable i.e. not same as param type, we must box it
					gn.unbox(fieldType);

				} else {
					gn.checkCast(fieldType);
				}
				gn.putField(classType, fieldName, fieldType);

				gn.visitLabel(continueLabel);

			}
		}

		if (metaClass.getParentType() != null) {
			gn.loadThis();
			gn.loadArg(0);
			gn.visitMethodInsn(Opcodes.INVOKESPECIAL,
					Reflector.getInternalNameFromTypeName(metaClass.getParentType().getTypeName()),
					ByteCodeMethods.SET_FIELDS_DESER.getName(), ByteCodeMethods.SET_FIELDS_DESER.getDescriptor(),
					false);
			gn.returnValue();
		} else {
			gn.returnValue();
		}

		/** MAX Stack */
		gn.visitMaxs(-1, -1); // Calculate it automatically
		gn.visitEnd();

	}

	/**
	 * Generate run method.
	 */
	private void generateRunMethod() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.RUN_METHOD.getName(),
				ByteCodeMethods.RUN_METHOD.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, ByteCodeMethods.RUN_METHOD.getName(),
				ByteCodeMethods.RUN_METHOD.getDescriptor());
		final InstructionAdapter ia = new InstructionAdapter(mv);

		final RunTableSwitchGenerator runGen = new RunTableSwitchGenerator(metaClass, stubInfo, isExecClass, gn, ia);
		runGen.generateCode(true);
	}

	/**
	 * Generate wrapParameters method.
	 */
	private void generateWrapParametersMethod() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.WRAP_PARAMETERS.getName(),
				ByteCodeMethods.WRAP_PARAMETERS.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC,
				ByteCodeMethods.WRAP_PARAMETERS.getName(), ByteCodeMethods.WRAP_PARAMETERS.getDescriptor());
		final InstructionAdapter ia = new InstructionAdapter(mv);

		final WrapParametersTableSwitchGenerator switchGen = new WrapParametersTableSwitchGenerator(metaClass, stubInfo,
				isExecClass, gn, ia);
		switchGen.generateCode(false);
	}

	/**
	 * Generate wrapReturn method.
	 */
	private void generateWrapReturnMethod() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.WRAP_RETURN.getName(),
				ByteCodeMethods.WRAP_RETURN.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, ByteCodeMethods.WRAP_RETURN.getName(),
				ByteCodeMethods.WRAP_RETURN.getDescriptor());
		final InstructionAdapter ia = new InstructionAdapter(mv);

		final WrapReturnTableSwitchGenerator switchGen = new WrapReturnTableSwitchGenerator(metaClass, stubInfo,
				isExecClass, gn, ia);
		switchGen.generateCode(true);
	}

	/**
	 * Generate setWrappersParams method.
	 */
	private void generateSetWrappersParameters() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.SET_WRAP_PARAMETERS.getName(),
				ByteCodeMethods.SET_WRAP_PARAMETERS.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC,
				ByteCodeMethods.SET_WRAP_PARAMETERS.getName(), ByteCodeMethods.SET_WRAP_PARAMETERS.getDescriptor());
		final InstructionAdapter ia = new InstructionAdapter(mv);

		final SetWrapperParametersTableSwitchGenerator switchGen = new SetWrapperParametersTableSwitchGenerator(
				metaClass, stubInfo, isExecClass, gn, ia);
		switchGen.generateCode(false);
	}

	/**
	 * Generate setWrappersReturn method.
	 */
	private void generateSetWrappersReturn() {
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, ByteCodeMethods.SET_WRAP_RETURN.getName(),
				ByteCodeMethods.SET_WRAP_RETURN.getDescriptor(), null, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC,
				ByteCodeMethods.SET_WRAP_RETURN.getName(), ByteCodeMethods.SET_WRAP_RETURN.getDescriptor());
		final InstructionAdapter ia = new InstructionAdapter(mv);

		final SetWrapperReturnTableSwitchGenerator switchGen = new SetWrapperReturnTableSwitchGenerator(metaClass,
				stubInfo, isExecClass, gn, ia);
		switchGen.generateCode(true);
	}

}
