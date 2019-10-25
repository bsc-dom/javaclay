
/**
 * 
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.commons.Method;

import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethods;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeMethodsNames;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeTypes;
import es.bsc.dataclay.serialization.java.lang.ObjectWrapper;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * Serialization code generator for stubs and execution classes.
 */
public final class SerializationCodeGenerator {

	/** Indicates maximum serialized fields per method. */
	private static final int MAX_SERIALIZED_FIELDS_PER_METHOD = 10;

	/** Deserialize name. */
	public static final String DESERIALIZE_NAME = "deserialize";

	/** Deserialize signature. */
	public static final String DESERIALIZE_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE,
			ByteCodeTypes.DC_BUF, ByteCodeTypes.MAP, ByteCodeTypes.DC_OBJECT_METADATA,
			ByteCodeTypes.MAP);

	/** Partial deserialize signature. Extracted from ASMifier. */
	public static final String DESERIALIZE_SIGNATURE = "(Ldataclay/serialization/buffer/DataClayByteBuffer;Ljava/util/Map<Ldataclay/util/ids/MetaClassID;[B>;"
			+ "Ldataclay/util/DataClayObjectMetaData;Ljava/util/Map<Ljava/lang/Integer;Ljava/lang/Object;>;"
			+ ")V";

	/** Partial deserialize descriptor. */
	public static final String PARTIAL_DESERIALIZE_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE,
			ByteCodeTypes.DC_BUF, ByteCodeTypes.MAP,
			ByteCodeTypes.DC_OBJECT_METADATA, ByteCodeTypes.BITSET, ByteCodeTypes.BITSET,
			ByteCodeTypes.MAP);

	/** Partial deserialize signature. Extracted from ASMifier. */
	public static final String PARTIAL_DESERIALIZE_SIGNATURE = "(Ldataclay/serialization/buffer/DataClayByteBuffer;Ljava/util/Map<Ldataclay/util/ids/MetaClassID;[B>;"
			+ "Ldataclay/util/DataClayObjectMetaData;Ljava/util/BitSet;Ljava/util/BitSet;"
			+ "Ljava/util/Map<Ljava/lang/Integer;Ljava/lang/Object;>;)V";

	/** Serialize name. */
	public static final String SERIALIZE_NAME = "serialize";

	/** Serialize signature. */
	public static final String SERIALIZE_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE,
			ByteCodeTypes.DC_BUF, Type.BOOLEAN_TYPE, ByteCodeTypes.MAP, ByteCodeTypes.IDENTITYMAP,
			ByteCodeTypes.LISTITERATOR, ByteCodeTypes.REFERENCE_COUNTING);

	/** Serialize signature. Extracted from ASMifier. */
	public static final String SERIALIZE_SIGNATURE = "(Ldataclay/serialization/buffer/DataClayByteBuffer;ZLjava/util/Map<Ldataclay/util/ids/MetaClassID;[B>;"
			+ "Ljava/util/IdentityHashMap<Ljava/lang/Object;Ljava/lang/Integer;>;"
			+ "Ljava/util/ListIterator<Ldataclay/DataClayObject;>;Ldataclay/util/ReferenceCounting;)V";

	/** Partial serialize descriptor. */
	public static final String PARTIAL_SERIALIZE_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE,
			ByteCodeTypes.DC_BUF, Type.BOOLEAN_TYPE,
			ByteCodeTypes.MAP,
			ByteCodeTypes.IDENTITYMAP,
			ByteCodeTypes.BITSET, ByteCodeTypes.BITSET,
			ByteCodeTypes.LISTITERATOR);

	/** Partial serialize signature. Extracted from ASMifier. */
	public static final String PARTIAL_SERIALIZE_SIGNATURE = "(Ldataclay/serialization/buffer/DataClayByteBuffer;ZLjava/util/Map<Ldataclay/util/ids/MetaClassID;[B>;"
			+ "Ljava/util/IdentityHashMap<Ljava/lang/Object;Ljava/lang/Integer;>;Ljava/util/BitSet;"
			+ "Ljava/util/BitSet;Ljava/util/ListIterator<Ldataclay/DataClayObject;>;)V";

	/**
	 * Utility classes should have a private constructor.
	 */
	private SerializationCodeGenerator() {

	}

	/**
	 * Generate serialize method
	 * @param classType
	 *            Class type being created
	 * @param parentInternalName
	 *            Internal name of parent class
	 * @param cv
	 *            Class visitor used for method generation.
	 * @param fields
	 *            Fields to serialize
	 */
	public static void generateSerializeMethod(final ClassVisitor cv,
			final Type classType,
			final String parentInternalName,
			final Set<Property> fields) {

		// First, generate partial serialization methods
		final Iterator<Property> fieldsIterator = fields.iterator();
		int curPartialMethod = 0;
		while (fieldsIterator.hasNext()) {
			generatePartialSerializeMethod(cv, classType, fieldsIterator, curPartialMethod);
			curPartialMethod++;
		}

		// Create method
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "serialize",
				SERIALIZE_DESCRIPTOR, SERIALIZE_SIGNATURE, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, "serialize",
				SERIALIZE_DESCRIPTOR);
		final InstructionAdapter ia = new InstructionAdapter(mv);

		// CHECKSTYLE:OFF
		// Calculate size of bitmap of null fields given the number of present fields
		final int numFields = fields.size();
		int numBytes = 0;
		for (int i = 0; i < numFields; ++i) {
			if (i % 8 == 0) {
				numBytes++;
			}
		}

		// Arguments
		final int dcBufferArgIdx = 0;
		final int ignoreUserTypes = 1;
		final int ifaceBitMaps = 2;
		final int curSerializedObjs = 3;
		final int pendingObjs = 4;
		// CHECKSTYLE:ON

		gn.visitCode();
		final Label startLabel = new Label();
		gn.visitLabel(startLabel);

		// Create not-nulls bitset
		// CODE: BitSet notNullsBitSetVar = new BitSet(numFields);
		gn.newInstance(ByteCodeTypes.BITSET);
		gn.dup();
		ia.iconst(numBytes);
		gn.invokeConstructor(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_CONSTRUCTOR);
		final int notNullsBitSetVar = gn.newLocal(ByteCodeTypes.BITSET);
		gn.storeLocal(notNullsBitSetVar);
		final Label nullBitSetCreatedLabel = new Label();
		gn.visitLabel(nullBitSetCreatedLabel);

		// Serialize bitmap length
		// CODE: dcBuffer.writeVLQInt(numBytes);
		gn.loadArg(dcBufferArgIdx); // Load dataCLay byte buffer
		ia.iconst(numBytes);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_WRITE_VLQ_INT);

		// Store writer index into local variable
		// CODE: int writerIndex = dcBuffer.writerIndex();
		gn.loadArg(dcBufferArgIdx); // Load dataCLay byte buffer
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_WRITER_INDEX);
		final int writerIndex = gn.newLocal(Type.INT_TYPE);
		gn.storeLocal(writerIndex);
		final Label writerIndexCreatedLabel = new Label();
		gn.visitLabel(writerIndexCreatedLabel);

		// Serialize not-nulls bitmap to be updated at the end of the method
		// CODE: dcBuffer.writeBytes(new byte[numFields]);
		gn.loadArg(dcBufferArgIdx);
		ia.iconst(numBytes);
		gn.newArray(Type.BYTE_TYPE);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_WRITE_BYTES);

		// Create bitset for interface
		gn.visitInsn(Opcodes.ACONST_NULL);
		final int ifaceBitSet = gn.newLocal(ByteCodeTypes.BITSET);
		gn.storeLocal(ifaceBitSet, ByteCodeTypes.BITSET);
		final Label ifaceBitSetCreatedLabel = new Label();
		gn.visitLabel(ifaceBitSetCreatedLabel);

		// Check if interface bitmaps is null
		// CODE: if (interfaceBitMaps != null) { ... }
		gn.loadArg(ifaceBitMaps);
		final Label ifInterfaceBitMapIsNull = new Label();
		gn.ifNull(ifInterfaceBitMapIsNull);

		// If not null, load interface bitmap into the bitset
		// CODE: BitSet ifaceBitSet = BitSet.valueOf(ifaceBitmaps.get(this.getMetaClassID()));
		gn.loadArg(ifaceBitMaps);
		gn.loadThis();
		// gn.checkCast(ByteCodeTypes.DCOBJ);
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL,
				classType.getInternalName(),
				ByteCodeMethods.DCOBJ_GET_METACLASSID.getName(),
				ByteCodeMethods.DCOBJ_GET_METACLASSID.getDescriptor(), false);
		gn.invokeInterface(ByteCodeTypes.MAP, ByteCodeMethods.MAP_GET);
		gn.checkCast(ByteCodeTypes.BYTE_ARRAY);
		gn.invokeStatic(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_VALUEOF);
		gn.storeLocal(ifaceBitSet);

		// Continue (is not an else since there is no GOTO instruction)
		// CODE: } ...
		gn.visitLabel(ifInterfaceBitMapIsNull);

		// Call all partial serialize methods
		for (int i = 0; i < curPartialMethod; ++i) {
			gn.loadThis();
			gn.loadArg(dcBufferArgIdx);
			gn.loadArg(ignoreUserTypes);
			gn.loadArg(ifaceBitMaps);
			gn.loadArg(curSerializedObjs);
			gn.loadLocal(notNullsBitSetVar);
			gn.loadLocal(ifaceBitSet);
			gn.loadArg(pendingObjs);
			final Method partialMethod = new Method(ByteCodeMethodsNames.SERIALIZE_MTHD + i,
					PARTIAL_SERIALIZE_DESCRIPTOR);
			gn.invokeVirtual(classType, partialMethod);
		}

		// Update bitmap of not null fields
		// Get writer index
		// CODE: writerIndex2 wIndex2 = dcBuffer.writerIndex();
		gn.loadArg(dcBufferArgIdx);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_WRITER_INDEX);
		final int writerIndex2 = gn.newLocal(Type.INT_TYPE);
		gn.storeLocal(writerIndex2);
		final Label writerIndex2CreatedLabel = new Label();
		gn.visitLabel(writerIndex2CreatedLabel);

		// Set writer index to initial one
		// CODE: dcBuffer.setWriterIndex(writerIndex);
		gn.loadArg(dcBufferArgIdx);
		gn.loadLocal(writerIndex);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_SET_WRITER_INDEX);

		// Serialize bit map of nulls again
		// CODE: dcBuffer.writeBytes(notNullsBitSet.toByteArray());
		gn.loadArg(dcBufferArgIdx);
		gn.loadLocal(notNullsBitSetVar);
		gn.invokeVirtual(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_TOBYTEARRAY);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_WRITE_BYTES);

		// Move writer index again to normal position
		gn.loadArg(dcBufferArgIdx);
		gn.loadLocal(writerIndex2);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_SET_WRITER_INDEX);

		// Serialize parent fields
		gn.loadThis();
		gn.loadArg(dcBufferArgIdx);
		gn.loadArg(ignoreUserTypes);
		gn.loadArg(ifaceBitMaps);
		gn.loadArg(curSerializedObjs);
		gn.loadArg(pendingObjs);
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL, parentInternalName,
				ByteCodeMethodsNames.SERIALIZE_MTHD, SERIALIZE_DESCRIPTOR, false);

		// End of method
		gn.returnValue();

		final Label endLabel = new Label();
		gn.visitLabel(endLabel);

		// Local variables
		/*
		 * mv.visitLocalVariable("this", classType.getDescriptor(), null, startLabel, endLabel, 0); mv.visitLocalVariable("dcBuffer",
		 * ByteCodeTypes.DC_BUF.getDescriptor(), null, startLabel, endLabel, 1); mv.visitLocalVariable("ignoreUserTypes",
		 * Type.BOOLEAN_TYPE.getDescriptor(), null, startLabel, endLabel, 2); mv.visitLocalVariable("ifaceBitMaps",
		 * ByteCodeTypes.MAP.getDescriptor(), "Ljava/util/Map<Lutil/ids/MetaClassID;[B>;", startLabel, endLabel, 3);
		 * mv.visitLocalVariable("curSerializedObjs", ByteCodeTypes.IDENTITYMAP.getDescriptor(),
		 * "Ljava/util/IdentityHashMap<Ljava/lang/Object;Ljava/lang/Integer;>;", startLabel, endLabel, 4);
		 * mv.visitLocalVariable("pendingObjs", ByteCodeTypes.LISTITERATOR.getDescriptor(),
		 * "Ljava/util/ListIterator<Lserialization/DataClayObject;>;", startLabel, endLabel, 5); mv.visitLocalVariable("nullsBitSet",
		 * ByteCodeTypes.BITSET.getDescriptor(), null, nullBitSetCreatedLabel, endLabel, 6); mv.visitLocalVariable("j",
		 * Type.INT_TYPE.getDescriptor(), null, writerIndexCreatedLabel, endLabel, 7); mv.visitLocalVariable("ifaceBitSet",
		 * ByteCodeTypes.BITSET.getDescriptor(), null, ifaceBitSetCreatedLabel, endLabel, 8); mv.visitLocalVariable("k",
		 * Type.INT_TYPE.getDescriptor(), null, writerIndex2CreatedLabel, endLabel, 9);
		 */

		gn.visitMaxs(-1, -1); // Automatically calculated
		mv.visitEnd();

	}

	/**
	 * Generate partial serialize method
	 * @param cv
	 *            Class visitor used for method generation.
	 * @param classType
	 *            Class type being created
	 * @param fieldsIterator
	 *            Iterator of fields to serialize
	 * @param curPartialMethod
	 *            Current partial method
	 */
	private static void generatePartialSerializeMethod(final ClassVisitor cv,
			final Type classType,
			final Iterator<Property> fieldsIterator, final int curPartialMethod) {

		// Create method
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PRIVATE,
				ByteCodeMethodsNames.SERIALIZE_MTHD + curPartialMethod,
				PARTIAL_SERIALIZE_DESCRIPTOR, PARTIAL_SERIALIZE_SIGNATURE, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PRIVATE,
				ByteCodeMethodsNames.SERIALIZE_MTHD + curPartialMethod,
				PARTIAL_SERIALIZE_DESCRIPTOR);
		final InstructionAdapter ia = new InstructionAdapter(mv);

		// Arguments
		// CHECKSTYLE:OFF
		final int dcBuffer = 0;
		final int ignoreUserTypes = 1;
		final int ifaceBitMaps = 2;
		final int curSerializedObjs = 3;
		final int nullsBitSet = 4;
		final int ifaceBitSet = 5;
		final int pendingObjs = 6;
		// CHECKSTYLE:ON

		gn.visitCode();
		final Label startLabel = new Label();
		gn.visitLabel(startLabel);

		// Serialize fields
		for (int i = 0; i < MAX_SERIALIZED_FIELDS_PER_METHOD && fieldsIterator.hasNext(); ++i) {
			final Property prop = fieldsIterator.next();

			final int modProp = prop.getJavaPropertyInfo().getModifier();
			if (Modifier.isStatic(modProp) && Modifier.isFinal(modProp)) {
				continue;
			}

			final String fieldName = prop.getName();
			final int fieldPos = prop.getPosition();
			final String typeName = prop.getType().getTypeName(); // "int", "float", ... "java.lang.Integer"...
			final Type fieldType = Type.getType(prop.getType().getDescriptor());

			// Check if field is in bitmap of ifaces. First, Check if bitmap is null == it is in iface.
			// CODE: if (ifaceBitSet == null ...
			gn.loadArg(ifaceBitSet);
			final Label processField = new Label();
			gn.ifNull(processField);

			// Code: || ifaceBitSet.get(fieldPos)) {
			gn.loadArg(ifaceBitSet);
			ia.iconst(fieldPos); // Use instruction adapter which will redirect call to Mv.
			gn.invokeVirtual(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_GET);
			final Label nextField = new Label();
			gn.ifZCmp(Opcodes.IFEQ, nextField);

			// Visit label
			gn.visitLabel(processField);

			// Immutables and primitive fields are going to be "unboxed" into primitive fields for
			// an easy serialization. However, immutables can be null and this might be taken into account.
			if (Reflector.isPrimitiveTypeName(typeName)) {

				// ==== PRIMITIVE TYPES ==== //
				// All primitive are not null fields
				// Set field as non null field
				gn.loadArg(nullsBitSet);
				ia.iconst(fieldPos);
				gn.invokeVirtual(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_SET);

				// Create call to writeXXx in DataClayByteBuffer
				callDcBufferWriteForPrimitiveAndImmutables(gn, dcBuffer, classType, fieldType, typeName,
						fieldName);

			} else {
				// ==== REFERENCE TYPES ==== //

				final boolean isImmutable = Reflector.isImmutableTypeName(typeName);
				final boolean isJavaType = Reflector.isArrayTypeName(typeName) || Reflector.isJavaTypeName(typeName);

				// Check if field must be added (depending on the flag ignoreUserTypes)
				if (!isJavaType) {
					gn.loadArg(ignoreUserTypes);
					gn.ifZCmp(Opcodes.IFNE, nextField); // if ignoreUserTypes != 0 != false, jump
				}

				// Immutable types and any other type can be null

				// Check if it is null
				gn.loadThis();
				gn.getField(classType, fieldName, fieldType);
				gn.ifNull(nextField); // if field == null, jump

				// Not null, mark it
				gn.loadArg(nullsBitSet);
				ia.iconst(fieldPos);
				gn.invokeVirtual(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_SET);

				if (isImmutable) {
					// ==== IMMUTABLES ==== //
					// Create call to writeXXx in DataClayByteBuffer
					callDcBufferWriteForPrimitiveAndImmutables(gn, dcBuffer, classType, fieldType, typeName,
							fieldName);
				} else if (isJavaType) {
					// ==== JAVA TYPE ==== //

					// Find wrapper type.
					final Tuple<Type, Type> wrapperTypes = findWrapperType(typeName, prop.getType().getDescriptor());
					final Type wrapperType = wrapperTypes.getFirst();
					final Type paramType = wrapperTypes.getSecond();
					// Get constructor. Constructor always contains as the argument the original
					// type to wrap.
					final Method wrapperConstructor = new Method(ByteCodeMethodsNames.INIT_MTHD,
							Type.VOID_TYPE, new Type[] { paramType });

					// Wrap field and call serializeJavaAssociation
					// call serializeJavaField
					// CODE: serializeJavaField(new Wrapper(field), dcBuffer, ignoreUserTypes, ...)
					gn.newInstance(wrapperType);
					gn.dup();
					gn.loadThis();
					gn.getField(classType, fieldName, fieldType);
					gn.invokeConstructor(wrapperType, wrapperConstructor);
					gn.loadArg(dcBuffer);
					gn.loadArg(ignoreUserTypes);
					gn.loadArg(ifaceBitMaps);
					gn.loadArg(curSerializedObjs);
					gn.loadArg(pendingObjs);
					gn.invokeStatic(ByteCodeTypes.DATACLAY_SERIALIZATION_LIB, ByteCodeMethods.SERIALIZE_JAVA_FIELD);
				} else {
					// DataClayObject
					// CODE: serializeAssociation(field, ...)
					gn.loadThis();
					gn.getField(classType, fieldName, fieldType);
					gn.checkCast(ByteCodeTypes.DCOBJ);
					gn.loadArg(dcBuffer);
					gn.loadArg(ignoreUserTypes);
					gn.loadArg(ifaceBitMaps);
					gn.loadArg(curSerializedObjs);
					gn.loadArg(pendingObjs);
					gn.invokeStatic(ByteCodeTypes.DATACLAY_SERIALIZATION_LIB, ByteCodeMethods.SERIALIZE_ASSOC);

				}
			}

			gn.visitLabel(nextField);
			// next field
		}

		// End of method
		final Label endLabel = new Label();
		gn.returnValue();
		gn.visitLabel(endLabel);

		// local variables scope
		/*
		 * gn.visitLocalVariable("this", classType.getDescriptor(), null, startLabel, endLabel, 0); gn.visitLocalVariable("dcBuffer",
		 * ByteCodeTypes.DC_BUF.getDescriptor(), null, startLabel, endLabel, 1); mv.visitLocalVariable("ignoreUserTypes",
		 * Type.BOOLEAN_TYPE.getDescriptor(), null, startLabel, endLabel, 2); mv.visitLocalVariable("ifaceBitMaps",
		 * ByteCodeTypes.MAP.getDescriptor(), "Ljava/util/Map<Lutil/ids/MetaClassID;[B>;", startLabel, endLabel, 3);
		 * mv.visitLocalVariable("curSerializedObjs", ByteCodeTypes.IDENTITYMAP.getDescriptor(),
		 * "Ljava/util/IdentityHashMap<Ljava/lang/Object;Ljava/lang/Integer;>;", startLabel, endLabel, 4);
		 * mv.visitLocalVariable("nullsBitSet", ByteCodeTypes.BITSET.getDescriptor(), null, startLabel, endLabel, 5);
		 * mv.visitLocalVariable("ifaceBitSet", ByteCodeTypes.BITSET.getDescriptor(), null, startLabel, endLabel, 6);
		 * mv.visitLocalVariable("pendingObjs", ByteCodeTypes.LISTITERATOR.getDescriptor(),
		 * "Ljava/util/ListIterator<Lserialization/DataClayObject;>;", startLabel, endLabel, 7);
		 */

		gn.visitMaxs(-1, -1); // Automatically calculated
		mv.visitEnd();

	}

	/**
	 * Generate a call to writeXxx in DataClayByteBuffer for primitive and immutable types
	 * @param gn
	 *            Method generator
	 * @param dcBuffer
	 *            Index to dataclay byte buffer in code
	 * @param classType
	 *            Class type
	 * @param fieldType
	 *            Field type
	 * @param typeName
	 *            Type name (i.e. "int", "float", "java.lang.String", ...)
	 * @param fieldName
	 *            Field name
	 */
	private static void callDcBufferWriteForPrimitiveAndImmutables(
			final GeneratorAdapter gn, final int dcBuffer,
			final Type classType, final Type fieldType,
			final String typeName, final String fieldName) {
		// IMPORTANT: This can be hacky. DataClayByteBuffer contains a set of instructions called
		// writeXxx(...) where Xxx is the primitive type (Int, Float, ...) We will use this to
		// avoid creating a switch-case.
		// First capitalize first character
		final String primitiveTypeName = Reflector.getPrimitiveTypeNameFromImmutableTypeName(typeName);
		String capitalizedTypeName = primitiveTypeName.substring(0, 1).toUpperCase()
				+ primitiveTypeName.substring(1);
		if (typeName.equals(String.class.getName())) {
			capitalizedTypeName = "String";
		}

		// Now prepare method call
		final String desc = Reflector.getSignatureFromTypeName(primitiveTypeName);
		final Type paramType = Type.getType(desc);
		final Method dcBufferWriteMethod = new Method("write" + capitalizedTypeName, Type.VOID_TYPE,
				new Type[] { paramType });

		// Now we invoke it
		// CODE: dcBuffer.writeXxx(field)
		gn.loadArg(dcBuffer);
		gn.loadThis();
		gn.getField(classType, fieldName, fieldType);
		if (!fieldType.equals(paramType)) {
			// If field type is java immutable i.e. not same as param type, we must box it
			gn.unbox(paramType);
		}
		gn.invokeInterface(ByteCodeTypes.DC_BUF, dcBufferWriteMethod);
	}

	/**
	 * Generate deserialize method
	 * @param classType
	 *            Class type being created
	 * @param parentInternalName
	 *            Internal name of parent class
	 * @param cv
	 *            Class visitor used for method generation.
	 * @param fields
	 *            Fields to deserialize
	 */
	public static void generateDeserializeMethod(final ClassVisitor cv,
			final Type classType,
			final String parentInternalName,
			final Set<Property> fields) {
		// First, generate partial deserialization methods
		final Iterator<Property> fieldsIterator = fields.iterator();
		int curPartialMethod = 0;
		while (fieldsIterator.hasNext()) {
			generatePartialDeserializeMethod(cv, classType, fieldsIterator, curPartialMethod);
			curPartialMethod++;
		}

		// Create method
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "deserialize",
				DESERIALIZE_DESCRIPTOR, DESERIALIZE_SIGNATURE, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PUBLIC, "deserialize",
				DESERIALIZE_DESCRIPTOR);

		// CHECKSTYLE:OFF
		// Parameters
		final int dcBuffer = 0;
		final int ifaceBitMaps = 1;
		final int metaData = 2;
		final int alreadyDeserializedObjs = 3;
		// CHECKSTYLE:ON

		gn.visitCode();
		final Label startLabel = new Label();
		gn.visitLabel(startLabel);

		// Create nulls bitset
		gn.visitInsn(Opcodes.ACONST_NULL);
		final int nullsBitSet = gn.newLocal(ByteCodeTypes.BITSET);
		gn.storeLocal(nullsBitSet, ByteCodeTypes.BITSET);

		// Create iface bitset
		gn.visitInsn(Opcodes.ACONST_NULL);
		final int ifaceBitSet = gn.newLocal(ByteCodeTypes.BITSET);
		gn.storeLocal(ifaceBitSet, ByteCodeTypes.BITSET);

		// CODE: nullsBitMapSize = dcBuffer.readVLQInt()
		gn.loadArg(dcBuffer);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_READ_VLQ_INT);
		final int nullsBitMapSize = gn.newLocal(Type.INT_TYPE);
		gn.storeLocal(nullsBitMapSize);

		// CODE: if (nullsBitMapSize != 0) { ...
		gn.loadLocal(nullsBitMapSize);
		final Label noBitSetOfNulls = new Label();
		gn.ifZCmp(Opcodes.IFLE, noBitSetOfNulls);

		// Creat bitmap of nulls
		// CODE: nullsBitSet = BitSet.valueOf(dcBuffer.readBytes(nullsBitMapSize));
		gn.loadArg(dcBuffer);
		gn.loadLocal(nullsBitMapSize);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, ByteCodeMethods.DCBUFFER_READ_BYTES);
		gn.invokeStatic(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_VALUEOF);
		gn.storeLocal(nullsBitSet);

		// Continue
		gn.visitLabel(noBitSetOfNulls);

		// CODE: if (ifaceBitMaps != null) { ... }
		gn.loadArg(ifaceBitMaps);
		final Label ifaceMapsIsNull = new Label();
		gn.ifNull(ifaceMapsIsNull);

		gn.loadArg(ifaceBitMaps);
		gn.loadThis();
		// gn.checkCast(ByteCodeTypes.DCOBJ);
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL,
				classType.getInternalName(),
				ByteCodeMethods.DCOBJ_GET_METACLASSID.getName(),
				ByteCodeMethods.DCOBJ_GET_METACLASSID.getDescriptor(), false);
		gn.invokeInterface(ByteCodeTypes.MAP, ByteCodeMethods.MAP_GET);
		gn.checkCast(ByteCodeTypes.BYTE_ARRAY);
		gn.invokeStatic(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_VALUEOF);
		gn.storeLocal(ifaceBitSet);

		// Continue
		gn.visitLabel(ifaceMapsIsNull);

		// Deserialize each field
		for (int i = 0; i < curPartialMethod; ++i) {
			gn.loadThis();
			gn.loadArg(dcBuffer);
			gn.loadArg(ifaceBitMaps);
			gn.loadArg(metaData);
			gn.loadLocal(nullsBitSet);
			gn.loadLocal(ifaceBitSet);
			gn.loadArg(alreadyDeserializedObjs);
			final Method partialMethod = new Method(ByteCodeMethodsNames.DESERIALIZE_MTHD + i,
					PARTIAL_DESERIALIZE_DESCRIPTOR);
			gn.invokeVirtual(classType, partialMethod);
		}

		// Call parent deserialization
		gn.loadThis();
		gn.loadArg(dcBuffer);
		gn.loadArg(ifaceBitMaps);
		gn.loadArg(metaData);
		gn.loadArg(alreadyDeserializedObjs);
		gn.visitMethodInsn(Opcodes.INVOKESPECIAL, parentInternalName,
				ByteCodeMethodsNames.DESERIALIZE_MTHD, DESERIALIZE_DESCRIPTOR, false);

		// End of method
		gn.returnValue();

		final Label endLabel = new Label();
		gn.visitLabel(endLabel);

		gn.visitMaxs(-1, -1); // Automatically calculated
		mv.visitEnd();
	}

	/**
	 * Generate partial deserialize method
	 * @param cv
	 *            Class visitor used for method generation.
	 * @param classType
	 *            Class type being created
	 * @param fieldsIterator
	 *            Iterator of fields to deserialize
	 * @param curPartialMethod
	 *            Current partial method
	 */
	private static void generatePartialDeserializeMethod(final ClassVisitor cv,
			final Type classType,
			final Iterator<Property> fieldsIterator, final int curPartialMethod) {

		// Create method
		final MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PRIVATE,
				ByteCodeMethodsNames.DESERIALIZE_MTHD + curPartialMethod,
				PARTIAL_DESERIALIZE_DESCRIPTOR, PARTIAL_DESERIALIZE_SIGNATURE, null);
		final GeneratorAdapter gn = new GeneratorAdapter(mv, Opcodes.ACC_PRIVATE,
				ByteCodeMethodsNames.DESERIALIZE_MTHD + curPartialMethod,
				PARTIAL_DESERIALIZE_DESCRIPTOR);
		final InstructionAdapter ia = new InstructionAdapter(mv);

		// Arguments
		// CHECKSTYLE:OFF
		final int dcBuffer = 0;
		final int ifaceBitMaps = 1;
		final int metaData = 2;
		final int nullsBitSet = 3;
		final int ifaceBitSet = 4;
		final int alreadyDeserializedObjs = 5;
		// CHECKSTYLE:ON

		gn.visitCode();
		final Label startLabel = new Label();
		gn.visitLabel(startLabel);

		// Deserialize fields
		for (int i = 0; i < MAX_SERIALIZED_FIELDS_PER_METHOD && fieldsIterator.hasNext(); ++i) {
			final Property prop = fieldsIterator.next();

			final int modProp = prop.getJavaPropertyInfo().getModifier();
			if (Modifier.isStatic(modProp) && Modifier.isFinal(modProp)) {
				continue;
			}

			final String fieldName = prop.getName();
			final int fieldPos = prop.getPosition();
			final String typeName = prop.getType().getTypeName(); // "int", "float", ... "java.lang.Integer"...
			final Type fieldType = Type.getType(prop.getType().getDescriptor());

			// Check if field is in bitmap of ifaces. First, Check if bitmap is null == it is in iface.
			// CODE: if (ifaceBitSet == null ...
			gn.loadArg(ifaceBitSet);
			final Label processField = new Label();
			gn.ifNull(processField);

			// Code: || ifaceBitSet.get(fieldPos)) {
			gn.loadArg(ifaceBitSet);
			ia.iconst(fieldPos); // Use instruction adapter which will redirect call to Mv.
			gn.invokeVirtual(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_GET);
			final Label nextField = new Label();
			gn.ifZCmp(Opcodes.IFEQ, nextField);

			// Visit label
			gn.visitLabel(processField);

			// Check if field is null (primitive are marked as not null)
			gn.loadArg(nullsBitSet);
			ia.iconst(fieldPos); // Use instruction adapter which will redirect call to Mv.
			gn.invokeVirtual(ByteCodeTypes.BITSET, ByteCodeMethods.BITSET_GET);
			gn.ifZCmp(Opcodes.IFEQ, nextField);

			// Immutables and primitive fields are going to be deserialized as Immutable fields and must be
			// boxed if the actual type of field is primitive.
			if (Reflector.isPrimitiveTypeName(typeName)) {

				// ==== PRIMITIVE TYPES ==== //
				// Create call to readXXx in DataClayByteBuffer
				callDcBufferReadForPrimitiveAndImmutables(gn, dcBuffer, classType, fieldType, typeName,
						fieldName);

			} else {
				// ==== REFERENCE TYPES ==== //
				final boolean isImmutable = Reflector.isImmutableTypeName(typeName);
				final boolean isJavaType = Reflector.isArrayTypeName(typeName) || Reflector.isJavaTypeName(typeName);
				if (isImmutable) {
					// ==== IMMUTABLES ==== //
					// Create call to readXXx in DataClayByteBuffer
					callDcBufferReadForPrimitiveAndImmutables(gn, dcBuffer, classType, fieldType,
							typeName,
							fieldName);
				} else if (isJavaType) {
					// ==== JAVA TYPE ==== //

					// Find wrapper type
					final Tuple<Type, Type> wrapperTypes = findWrapperType(typeName, prop.getType().getDescriptor());
					final Type wrapperType = wrapperTypes.getFirst();
					// Get constructor.
					final Method wrapperConstructor = new Method(ByteCodeMethodsNames.INIT_MTHD,
							Type.VOID_TYPE, new Type[] {});

					// Wrap field and call deserializeJavaField
					// CODE: this.field = deserializeJavaField(new Wrapper(field), dcBuffer, ...)

					// deserializeJavaField(new Wrapper(), dcBuffer, ...)
					gn.loadThis();
					gn.newInstance(wrapperType);
					gn.dup();
					gn.invokeConstructor(wrapperType, wrapperConstructor);
					gn.loadArg(dcBuffer);
					gn.loadArg(ifaceBitMaps);
					gn.loadArg(metaData);
					gn.loadArg(alreadyDeserializedObjs);
					gn.invokeStatic(ByteCodeTypes.DATACLAY_SERIALIZATION_LIB, ByteCodeMethods.DESERIALIZE_JAVA_FIELD);

					// Store into field
					gn.checkCast(fieldType);
					gn.putField(classType, fieldName, fieldType);
				} else {
					// DataClayObject
					// CODE: this.field = deserializeAssociation(field, ...)

					// deserializeAssociation(...)
					gn.loadThis();
					gn.loadArg(dcBuffer);
					gn.loadArg(ifaceBitMaps);
					gn.loadArg(metaData);
					gn.loadArg(alreadyDeserializedObjs);
					gn.invokeStatic(ByteCodeTypes.DCOBJ, ByteCodeMethods.DCBOBJ_GET_LIB);
					gn.invokeStatic(ByteCodeTypes.DATACLAY_SERIALIZATION_LIB, ByteCodeMethods.DESERIALIZE_ASSOC);

					// Store into field
					gn.checkCast(fieldType);
					gn.putField(classType, fieldName, fieldType);
				}
			}

			gn.visitLabel(nextField);
			// next field
		}

		// End of method
		final Label endLabel = new Label();
		gn.returnValue();
		gn.visitLabel(endLabel);

		/*
		 * mv.visitLocalVariable("this", "Lclassmgr_test/ASMifierSerialization;", null, l0, l1, 0); mv.visitLocalVariable("dcBuffer",
		 * "Lserialization/buffer/DataClayByteBuffer;", null, l0, l1, 1); mv.visitLocalVariable("ifaceBitMaps", "Ljava/util/Map;",
		 * "Ljava/util/Map<Lutil/ids/MetaClassID;[B>;", l0, l1, 2); mv.visitLocalVariable("objMap", "Ljava/util/Map;",
		 * "Ljava/util/Map<Lutil/ids/ObjectID;Ljava/lang/ref/Reference<Lserialization/DataClayObject;>;>;", l0, l1, 3);
		 * mv.visitLocalVariable("medata", "Lutil/DataClayObjectMetaData;", null, l0, l1, 4); mv.visitLocalVariable("nullsBitSet",
		 * "Ljava/util/BitSet;", null, l0, l1, 5); mv.visitLocalVariable("ifaceBitSet", "Ljava/util/BitSet;", null, l0, l1, 6);
		 * mv.visitLocalVariable("curDeserializedJavaObjs", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/Integer;Ljava/lang/Object;>;",
		 * l0, l1, 7); mv.visitLocalVariable("dsRef", "Ldataservice/api/DataServiceAPI;", null, l0, l1, 8);
		 */
		gn.visitMaxs(-1, -1); // Automatically calculated
		mv.visitEnd();

	}

	/**
	 * Generate a call to readXxx in DataClayByteBuffer for primitive and immutable types
	 * @param gn
	 *            Method generator
	 * @param dcBuffer
	 *            Index to dataclay byte buffer in code
	 * @param classType
	 *            Class type
	 * @param fieldType
	 *            Field type
	 * @param typeName
	 *            Type name (i.e. "int", "float", "java.lang.String", ...)
	 * @param fieldName
	 *            Field name
	 */
	private static void callDcBufferReadForPrimitiveAndImmutables(
			final GeneratorAdapter gn, final int dcBuffer,
			final Type classType, final Type fieldType,
			final String typeName, final String fieldName) {
		// IMPORTANT: This can be hacky. DataClayByteBuffer contains a set of instructions called
		// readXxx(...) where Xxx is the primitive type (Int, Float, ...) We will use this to
		// avoid creating a switch-case.
		// First capitalize first character
		final String primitiveTypeName = Reflector.getPrimitiveTypeNameFromImmutableTypeName(typeName); // In case it's imm.
		String capitalizedTypeName = primitiveTypeName.substring(0, 1).toUpperCase() + primitiveTypeName.substring(1);
		if (typeName.equals(String.class.getName())) {
			capitalizedTypeName = "String";
		}

		// Now prepare method call
		final String desc = Reflector.getSignatureFromTypeName(primitiveTypeName);
		final Type returnType = Type.getType(desc);
		final Method dcBufferReadMethod = new Method("read" + capitalizedTypeName, returnType,
				new Type[] {});

		// Now we invoke it
		// CODE: this.field = dcBuffer.readXxx();
		// CODE for Primitive fields: this.primField = new Integer(dcBuffer.readXxx());
		gn.loadThis();
		gn.loadArg(dcBuffer);
		gn.invokeInterface(ByteCodeTypes.DC_BUF, dcBufferReadMethod);
		if (!fieldType.equals(returnType)) {
			// If field type is java immutable i.e. not same as return type, we must box it
			gn.box(returnType);
		}
		gn.putField(classType, fieldName, fieldType);
	}

	/**
	 * Find wrapper type that must wrap the type with name provided
	 * @param javaTypeName
	 *            Java type name to wrap
	 * @param javaTypeDescriptor
	 *            Java type descriptor
	 * @return Wrapper type and 'actual' java type for constructor
	 */
	public static Tuple<Type, Type> findWrapperType(final String javaTypeName, final String javaTypeDescriptor) {
		// IMPORTANT: This can be hacky. Java types must be wrapped into
		// special serialization classes, or wrapper types.
		// Wrapper types are included in package "serialization"
		// and they contain the same name and package than original java
		// types with "Wrapper" in the end. Using this, we avoid creating
		// a big switch-case.

		// Get wrapper type
		String actualTypeName = javaTypeName;
		Type actualJavaType = null;
		if (Reflector.isPrimitiveTypeName(javaTypeName)) {

			actualTypeName = Reflector.getImmutableTypeNameFromPrimitiveTypeName(javaTypeName);
			actualJavaType = Type.getType(Reflector.getDescriptorFromTypeName(actualTypeName));

		} else if (Reflector.isArrayTypeName(javaTypeName)) {
			// Since arrays are special types we change the name to find the proper
			// wrapper. Imagine that all arrays belongs to class java.util.Array
			actualTypeName = "java.util.Array";
			actualJavaType = ByteCodeTypes.OBJECT;
		} else {
			// For all non-final classes (i.e. classes implementing a super class in Java)
			// we can share the same wrapper and because of that we should get this special case.

			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			final Class<?> javaType = Reflector.getClassFromSignatureAndArray(javaTypeDescriptor,
					classLoader);
			if (Map.class.isAssignableFrom(javaType)) {
				actualTypeName = Map.class.getName();
				actualJavaType = ByteCodeTypes.MAP;
			} else if (Collection.class.isAssignableFrom(javaType)) {
				actualTypeName = Collection.class.getName();
				actualJavaType = ByteCodeTypes.COLL;
			} else if (Map.Entry.class.isAssignableFrom(javaType)) {
				actualTypeName = Map.Entry.class.getName();
				actualJavaType = ByteCodeTypes.MAPENTRY;
			} else {
				actualTypeName = javaTypeName;
				actualJavaType = Type.getType(Reflector.getDescriptorFromTypeName(actualTypeName));
			}

		}
		final String typePreffix = "dataclay.serialization.";
		String wrapperTypeName = typePreffix + actualTypeName + "Wrapper";

		// Check if wrapper exists
		try {
			if (Class.forName(wrapperTypeName) == null) {
				wrapperTypeName = ObjectWrapper.class.getName();
				actualJavaType = Type.getType(Object.class);
			}
		} catch (final ClassNotFoundException e) {
			wrapperTypeName = ObjectWrapper.class.getName();
			actualJavaType = Type.getType(Object.class);
		}

		final String descriptor = Reflector.getDescriptorFromTypeName(wrapperTypeName);
		final Type wrapperType = Type.getType(descriptor);

		return new Tuple<>(wrapperType, actualJavaType);
	}

}
