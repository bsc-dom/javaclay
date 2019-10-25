
package es.bsc.dataclay.util.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeFieldNames;

/**
 * This class contains useful functions when Java Reflection is necessary.
 * 
 * 
 */
public final class Reflector {

	/** Bootstrap class loader. */
	public static final ClassLoader BOOTSTRAP_CLASSLOADER = ClassLoader.getSystemClassLoader().getParent();

	/**
	 * Default constructor for this Utility Class.
	 */
	private Reflector() {

	}

	/**
	 * This function gets the value of the field with name specified in the object instance provided. The field must exists
	 *        in the class of the instance and must be accessible.
	 * @param instance
	 *            Instance from which to get the field value
	 * @param fieldName
	 *            Name of the field
	 * @return The value of the field with name specified in the instance provided
	 * @throws NoSuchFieldException
	 *             If field not exists
	 * @throws IllegalAccessException
	 *             If field is not accessible
	 */
	public static Object getField(final Object instance, final String fieldName) throws NoSuchFieldException,
			IllegalAccessException {

		Class<?> instanceClass = instance.getClass();
		Field field = null;
		while (field == null) {
			try {
				field = instanceClass.getDeclaredField(fieldName);
			} catch (final NoSuchFieldException e) {
				if (instanceClass.getSuperclass().equals(Object.class)) {
					throw new NoSuchFieldException(fieldName);
				}
			}
			instanceClass = instanceClass.getSuperclass();
		}
		field.setAccessible(true);
		return field.get(instance);

	}

	/**
	 * This function gets the value of the static field with name specified in the object instance provided. The field must
	 *        exists in the class and must be static and accessible.
	 * @param staticClass
	 *            Class from which to get the field value
	 * @param fieldName
	 *            Name of the field
	 * @return The value of the static field with name specified in the instance provided
	 * @throws NoSuchFieldException
	 *             If field not exists
	 * @throws IllegalAccessException
	 *             If field is not accessible
	 */
	public static Object getStaticField(final Class<?> staticClass, final String fieldName) throws NoSuchFieldException,
			IllegalAccessException {

		Class<?> instanceClass = staticClass;
		Field field = null;
		while (field == null) {
			try {
				field = instanceClass.getDeclaredField(fieldName);
			} catch (final Exception e) {
				if (instanceClass.getSuperclass().equals(Object.class)) {
					throw new NoSuchFieldException(fieldName);
				}
			}
			instanceClass = instanceClass.getSuperclass();
		}

		field.setAccessible(true);
		return field.get(null);

	}

	/**
	 * Sets the value of the field of instance provided with field name specified
	 * @param instance
	 *            Instance to modify
	 * @param fieldName
	 *            Name of the field to set
	 * @param fieldValue
	 *            Value to set
	 * @throws NoSuchFieldException
	 *             If the field does not exist.
	 * @throws IllegalAccessException
	 *             If the field is not accessible.
	 */
	public static void setField(final Object instance, final String fieldName, final Object fieldValue)
			throws NoSuchFieldException, IllegalAccessException {

		Class<?> instanceClass = instance.getClass();
		Field field = null;
		while (field == null) {
			try {
				field = instanceClass.getDeclaredField(fieldName);
			} catch (final NoSuchFieldException e) {
				if (instanceClass.getSuperclass().equals(Object.class)) {
					throw new NoSuchFieldException(fieldName);
				}
			}
			instanceClass = instanceClass.getSuperclass();
		}

		field.setAccessible(true);
		Object castObject = null;
		if (fieldValue != null) {
			// For retrieving object since it receives always lang types instead of primitive types
			final Class<?> fieldType = field.getType();
			castObject = cast(fieldType, fieldValue);
		}
		field.set(instance, castObject);

	}

	/**
	 * Since field values, for instance, can be Integer and the field type 'int', we must cast it. This should happen usually
	 *        in Reflection.
	 * @param classToCast
	 *            Class that the object should have
	 * @param objectToCast
	 *            Object to cast
	 * @return Casted object
	 */
	private static Object cast(final Class<?> classToCast, final Object objectToCast) {

		if (objectToCast == null) {
			return null;
		}

		Object castObject = null;
		if (classToCast.isPrimitive()) {

			final Class<?> fieldValueClass = objectToCast.getClass();
			if (fieldValueClass.equals(Integer.class)) {
				castObject = ((Integer) objectToCast).intValue();
			} else if (fieldValueClass.equals(Float.class)) {
				castObject = ((Float) objectToCast).floatValue();
			} else if (fieldValueClass.equals(Double.class)) {
				castObject = ((Double) objectToCast).doubleValue();
			} else if (fieldValueClass.equals(Boolean.class)) {
				castObject = ((Boolean) objectToCast).booleanValue();
			} else if (fieldValueClass.equals(Character.class)) {
				castObject = ((Character) objectToCast).charValue();
			} else if (fieldValueClass.equals(Long.class)) {
				castObject = ((Long) objectToCast).longValue();
			} else if (fieldValueClass.equals(Short.class)) {
				castObject = ((Short) objectToCast).shortValue();
			} else if (fieldValueClass.equals(Byte.class)) {
				castObject = ((Byte) objectToCast).byteValue();
			}

		} else if (classToCast.isArray()) {
			final String signature = classToCast.getName();
			final Class<?> componentType = classToCast.getComponentType();
			final int length = Array.getLength(objectToCast);
			final int[] dimensions = new int[countOccurrences(signature, "[")];
			for (int i = 0; i < dimensions.length; ++i) {
				dimensions[i] = length;
			}

			castObject = Array.newInstance(componentType, length);
			for (int i = 0; i < length; ++i) {
				final Object subArrayToCast = Array.get(objectToCast, i);
				final Object castSubArray = cast(componentType, subArrayToCast);
				Array.set(castObject, i, castSubArray);
			}

		} else {
			castObject = classToCast.cast(objectToCast);
		}

		return castObject;
	}

	/**
	 * Count the occurrences of some string into another
	 * @param str
	 *            String to analyze
	 * @param findStr
	 *            String to count
	 * @return Number of occurrences of 'findStr' into 'str'.
	 */
	public static int countOccurrences(final String str, final String findStr) {
		int lastIndex = 0;
		int count = 0;
		while (lastIndex != -1) {
			lastIndex = str.indexOf(findStr, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}

	/**
	 * Get sub types
	 * @param signature
	 *            Signature from which to get subtypes
	 * @return List of sub types found
	 */
	public static List<String> getSubtypes(final String signature) {
		final String cursignature = signature.replace(">", "").replace("(", "").replace("[", "").replace(")", "");
		final String[] tmpSplit = cursignature.split("<");
		final HashSet<String> splitted = new HashSet<>();
		for (final String tmp : tmpSplit) {
			final String[] tmpSplit2 = tmp.split(";");
			for (final String tmp2 : tmpSplit2) {
				splitted.add(tmp2);
			}
		}
		final ArrayList<String> signatures = new ArrayList<>();
		for (final String splitt : splitted) {
			if (splitt.trim().isEmpty()) {
				continue;
			}
			String temp = splitt;
			if (temp.contains("L")) {
				int lIndex = temp.indexOf("L");
				while (lIndex != 0) {
					// Get primitive types
					signatures.add(String.valueOf(temp.charAt(0)));
					temp = temp.substring(1, temp.length());
					lIndex = temp.indexOf("L");
				}
				signatures.add(temp + ";");
			}
		}
		return signatures;
	}

	/**
	 * Check if a name/signature is a Primitive type (quite close to Java primitives, but agnostic).
	 * @param typeName
	 *            type name to check.
	 * @return TRUE if the signature provided belongs to a Primitive type. FALSE otherwise.
	 */
	public static boolean isPrimitiveTypeName(final String typeName) {
		switch (typeName) {
		case "boolean":
			return true;
		case "int":
			return true;
		case "float":
			return true;
		case "long":
			return true;
		case "short":
			return true;
		case "char":
			return true;
		case "byte":
			return true;
		case "double":
			return true;
		case "void":
			return true;
		default:
			return false;
		}
	}

	/**
	 * Indicates if type name is an array type.
	 * @param typeName
	 *            Type name
	 * @return TRUE if type name is array type.
	 */
	public static boolean isArrayTypeName(final String typeName) {
		return typeName.contains("[");
	}

	/**
	 * This function verifies if there is a Java type with the name provided
	 * @param className
	 *            name of the class
	 * @return TRUE if there is a java type with name provided. FALSE otherwise.
	 */
	public static boolean isJavaTypeName(final String className) {
		try {
			BOOTSTRAP_CLASSLOADER.loadClass(className);
		} catch (final ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * This function verifies if type name is java, array or primitive.
	 * @param typeName
	 *            Type name.
	 * @return TRUE if type name is java, array or primitive.
	 */
	public static boolean isJavaPrimitiveOrArrayTypeName(final String typeName) {
		return isPrimitiveTypeName(typeName) || isArrayTypeName(typeName) || isJavaTypeName(typeName);
	}

	/**
	 * This function verifies if there is a lang type with the name provided.
	 * @param typeName
	 *            name of the type
	 * @return TRUE if there is a lang type with name provided. FALSE otherwise.
	 */
	public static boolean isImmutableTypeName(final String typeName) {
		switch (typeName) {
		default:
			return false;
		case "java.lang.Boolean":
			return true;
		case "java.lang.Integer":
			return true;
		case "java.lang.String":
			return true;
		case "java.lang.Double":
			return true;
		case "java.lang.Float":
			return true;
		case "java.lang.Long":
			return true;
		case "java.lang.Short":
			return true;
		case "java.lang.Character":
			return true;
		case "java.lang.Byte":
			return true;
		}
	}

	/**
	 * This function returns the primitive type name from immutable type name. WARNING: this function return "string" for
	 *        immutable "java.lang.String". Used in ByteCode generation.
	 * @param typeName
	 *            name of the type
	 * @return the primitive type name from immutable type name or the same type name if not immutable.
	 */
	public static String getPrimitiveTypeNameFromImmutableTypeName(final String typeName) {
		switch (typeName) {
		default:
			return typeName;
		case "java.lang.Boolean":
			return "boolean";
		case "java.lang.Integer":
			return "int";
		case "java.lang.Double":
			return "double";
		case "java.lang.Float":
			return "float";
		case "java.lang.Long":
			return "long";
		case "java.lang.Short":
			return "short";
		case "java.lang.Character":
			return "char";
		case "java.lang.Byte":
			return "byte";
		}
	}

	/**
	 * This function returns the primitive type name from immutable type name. WARNING: this function return "string" for
	 *        immutable "java.lang.String". Used in ByteCode generation.
	 * @param typeName
	 *            name of the type
	 * @return the primitive type name from immutable type name or the same type name if not immutable.
	 */
	public static String getImmutableTypeNameFromPrimitiveTypeName(final String typeName) {
		switch (typeName) {
		default:
			return typeName;
		case "boolean":
			return "java.lang.Boolean";
		case "int":
			return "java.lang.Integer";
		case "double":
			return "java.lang.Double";
		case "float":
			return "java.lang.Float";
		case "long":
			return "java.lang.Long";
		case "short":
			return "java.lang.Short";
		case "char":
			return "java.lang.Character";
		case "byte":
			return "java.lang.Byte";
		}
	}

	/**
	 * Check if the signature provided belongs to a Java type or to a Primitive type.
	 * @param signature
	 *            Signature to check.
	 * @return TRUE if the signature provided belongs to a Java type or to a Primitive type. FALSE otherwise.
	 */
	public static boolean isJavaPrimitiveOrArraySignature(final String signature) {
		return isPrimitiveSignature(signature) || isArraySignature(signature) || isJavaSignature(signature);
	}

	/**
	 * Check if signature is an array type
	 * @param signature
	 *            Signature to check
	 * @return TRUE if signature belongs to array type.
	 */
	private static boolean isArraySignature(final String signature) {
		return signature.contains("[");
	}

	/**
	 * Indicates signature is a Java type
	 * @param signature
	 *            Signature to check
	 * @return TRUE if java type, false otherwise.
	 */
	private static boolean isJavaSignature(final String signature) {
		return isJavaTypeName(getTypeNameFromSignatureOrDescriptor(signature));
	}

	/**
	 * This function verifies if there is a primitive type with the signature provided
	 * @param signature
	 *            signature of the type
	 * @return TRUE if there is a primitive type with signature provided. FALSE otherwise.
	 */
	public static boolean isPrimitiveSignature(final String signature) {
		switch (signature) {
		default:
			return false;
		case "Z":
			return true;
		case "I":
			return true;
		case "F":
			return true;
		case "J":
			return true;
		case "S":
			return true;
		case "C":
			return true;
		case "B":
			return true;
		case "D":
			return true;
		case "V":
			return true;
		}
	}

	/**
	 * This function verifies if there is a immutable type with the signature provided
	 * @param signature
	 *            signature of the type
	 * @return TRUE if there is a immutable type with signature provided. FALSE otherwise.
	 */
	public static boolean isImmutableSignature(final String signature) {
		switch (signature) {
		default:
			return false;
		case "Ljava/lang/Boolean;":
			return true;
		case "Ljava/lang/Integer;":
			return true;
		case "Ljava/lang/Float;":
			return true;
		case "Ljava/lang/Long;":
			return true;
		case "Ljava/lang/Short;":
			return true;
		case "Ljava/lang/Character;":
			return true;
		case "Ljava/lang/Byte;":
			return true;
		case "Ljava/lang/Double;":
			return true;
		case "Ljava/lang/String;":
			return true;
		}
	}

	/**
	 * Get the descriptor from of the type with name provided
	 * @param typeName
	 *            Name of the type
	 * @return The descriptor of the type with name provided
	 */
	public static String getDescriptorFromTypeName(final String typeName) {
		return getSignatureFromTypeName(typeName);
	}

	/**
	 * Get the signature from of the type with name provided
	 * @param typeName
	 *            Name of the type
	 * @return The signature of the type with name provided
	 */
	public static String getSignatureFromTypeName(final String typeName) {
		switch (typeName) {
		default:
			return "L" + typeName.replace(".", "/") + ";";
		case "boolean":
			return "Z";
		case "int":
			return "I";
		case "float":
			return "F";
		case "double":
			return "D";
		case "long":
			return "J";
		case "short":
			return "S";
		case "char":
			return "C";
		case "byte":
			return "B";
		case "void":
			return "V";
		}
	}

	/**
	 * Get the signature from of the fully qualified type name (internal name) provided
	 * @param internalName
	 *            Internal Name of the type
	 * @return The signature from of the fully qualified type name (internal name) provided
	 */
	public static String getSignatureFromInternalName(final String internalName) {
		final int arrayDimension = countOccurrences(internalName, "[");
		String finalTypeName = internalName.replace("[", "");
		if (!isPrimitiveSignature(finalTypeName)) {
			finalTypeName = "L" + finalTypeName.replace(".", "/") + ";";
		}
		String prefix = "";
		for (int i = 0; i < arrayDimension; ++i) {
			prefix += "[";
		}
		return prefix + finalTypeName;
	}

	/**
	 * Get internal name from type name
	 * @param typeName
	 *            Type name
	 * @return Internal name.
	 */
	public static String getInternalNameFromTypeName(final String typeName) {
		return typeName.replace(".", "/");
	}

	/**
	 * Get type name from internal name
	 * @param internalName
	 *            Internal name
	 * @return Type name
	 */
	public static String getTypeNameFromInternalName(final String internalName) {
		return getTypeNameFromSignatureOrDescriptor(getSignatureFromInternalName(internalName));
	}

	/**
	 * Get the name of the type corresponding to the provided signature.
	 * @param signature
	 *            Signature to analyze
	 * @return The name of the type corresponding to the provided signature.
	 */
	public static String getTypeNameFromSignatureOrDescriptor(final String signature) {
		final int arrayDimension = countOccurrences(signature, "[");
		String typeName = signature.replace("[", "");
		switch (typeName) {
		default:
			typeName = typeName.substring(1, typeName.length() - 1).replace("/", ".");
			break;
		case "Z":
			typeName = "boolean";
			break;
		case "I":
			typeName = "int";
			break;
		case "F":
			typeName = "float";
			break;
		case "J":
			typeName = "long";
			break;
		case "S":
			typeName = "short";
			break;
		case "C":
			typeName = "char";
			break;
		case "B":
			typeName = "byte";
			break;
		case "D":
			typeName = "double";
			break;
		case "V":
			typeName = "void";
			break;
		}
		for (int i = 0; i < arrayDimension; ++i) {
			typeName += "[]";
		}
		return typeName;

	}

	/**
	 * Get the name of the type corresponding to the provided signature and also get the [] array representation.
	 * @param signature
	 *            Signature to analyze
	 * @return The name of the type corresponding to the provided signature.
	 */
	public static String getCanonicalTypeNameFromSignature(final String signature) {
		final int arrayDimension = countOccurrences(signature, "[");
		String typeName = signature.replace("[", "").replace("/", ".");
		switch (typeName) {
		default:
			if (typeName.contains("<")) {
				final int startIndexSub = typeName.lastIndexOf("<") + 1;
				final int endIndexSub = typeName.lastIndexOf(">");
				final String subSignature = typeName.substring(startIndexSub, endIndexSub);
				final String newsubSignature = getTypeNameFromSignatureOrDescriptor(subSignature);
				typeName = typeName.replace(subSignature, newsubSignature);
			}
			typeName = typeName.substring(1, typeName.length() - 1);
			break;
		case "Z":
			typeName = "boolean";
			break;
		case "I":
			typeName = "int";
			break;
		case "F":
			typeName = "float";
			break;
		case "J":
			typeName = "long";
			break;
		case "S":
			typeName = "short";
			break;
		case "C":
			typeName = "char";
			break;
		case "B":
			typeName = "byte";
			break;
		case "D":
			typeName = "double";
			break;
		case "V":
			typeName = "void";
			break;
		}
		for (int i = 0; i < arrayDimension; ++i) {
			typeName += "[]";
		}
		return typeName;
	}

	/**
	 * Check if the field provided is DataClay field
	 * @param fieldName
	 *            Name of the field
	 * @return TRUE if the class provided is some of the types in a stub. FALSE otherwise.
	 */
	public static boolean isStubField(final String fieldName) {
		for (final Field f : DataClayObject.class.getDeclaredFields()) {
			if (fieldName.equals(f.getName())) {
				return true;
			}
		}
		for (final Field f : DataClayExecutionObject.class.getDeclaredFields()) {
			if (fieldName.equals(f.getName())) {
				return true;
			}
		}
		if (fieldName.equals(ByteCodeFieldNames.IS_STUB_FIELDNAME)) {
			return true;
		}
		return false;
	}

	/**
	 * Get the signature from method provided
	 * @param method
	 *            Method
	 * @return The signature of method provided
	 */
	public static String getSignatureFromMethod(final Method method) {
		final Class<?>[] paramTypes = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();
		String paramsSignature = "";
		for (Class<?> paramType : paramTypes) {

			if (paramType.isArray()) {
				paramsSignature += "[";
				paramType = paramType.getComponentType();
				while (paramType.isArray()) {
					paramType = paramType.getComponentType();
				}
			}

			paramsSignature += getSignatureFromTypeName(paramType.getName());
		}
		String returnSignature = "";
		if (returnType.isArray()) {
			returnSignature += "[";
			returnType = returnType.getComponentType();
			while (returnType.isArray()) {
				returnType = returnType.getComponentType();
			}
		}
		returnSignature += getSignatureFromTypeName(returnType.getName());
		return "(" + paramsSignature + ")" + returnSignature;
	}

	/**
	 * Get the signature from constructor provided
	 * @param constructor
	 *            Method
	 * @return The signature of constructor provided
	 */
	public static String getSignatureFromConstructor(final Constructor<?> constructor) {
		final Class<?>[] paramTypes = constructor.getParameterTypes();
		String paramsSignature = "";
		for (Class<?> paramType : paramTypes) {

			if (paramType.isArray()) {
				paramsSignature += "[";
				paramType = paramType.getComponentType();
				while (paramType.isArray()) {
					paramType = paramType.getComponentType();
				}
			}

			paramsSignature += getSignatureFromTypeName(paramType.getName());
		}
		return "(" + paramsSignature + ")V";
	}

	/**
	 * Get the class corresponding to the provided signature. If the signature belongs to an array get its component type.
	 * @param signature
	 *            Signature to check.
	 * @param classLoader
	 *            Class loader to use
	 * @return The class corresponding to the provided signature. If the signature belongs to an array get its component type.
	 */
	public static Class<?> getClassFromSignatureAndArray(final String signature, final ClassLoader classLoader) {
		final int arrayDimension = countOccurrences(signature, "[");
		String typeName = signature.replace("[", "").replace("/", ".");
		final Class<?> typeOfSignature;
		switch (typeName) {
		default:
			/*
			 * @abarcelo "P" (Python Wildcard) is, indeed, a byte array. (Additionally, some extra semantic on serialization/deserialization
			 * in the Python Execution Environment).
			 */
			if (typeName.startsWith("P")) {
				return null;
			}

			try {
				typeName = typeName.substring(1, typeName.length() - 1);
				typeOfSignature = classLoader.loadClass(typeName);
			} catch (final ClassNotFoundException e) {
				return null;
			}
			break;
		case "Z":
			typeOfSignature = boolean.class;
			break;
		case "I":
			typeOfSignature = int.class;
			break;
		case "F":
			typeOfSignature = float.class;
			break;
		case "J":
			typeOfSignature = long.class;
			break;
		case "S":
			typeOfSignature = short.class;
			break;
		case "C":
			typeOfSignature = char.class;
			break;
		case "B":
			typeOfSignature = byte.class;
			break;
		case "D":
			typeOfSignature = double.class;
			break;
		case "V":
			typeOfSignature = void.class;
			break;
		}

		if (arrayDimension > 0) {
			final int[] dimensions = new int[arrayDimension];
			return Array.newInstance(typeOfSignature, dimensions).getClass();
		}

		return typeOfSignature;

	}

	/**
	 * Check if class with name provided is an stub.
	 * @param className
	 *            Name of the class to analyze.
	 * @param theclassLoader
	 *            Class loader being used
	 * @return TRUE if it is an stub for the class loader used for this class. FALSE otherwise.
	 */
	public static boolean isStub(final String className, final ClassLoader theclassLoader) {
		try {
			final Class<?> clazz = theclassLoader.loadClass(className);
			try {
				clazz.getField(ByteCodeFieldNames.IS_STUB_FIELDNAME);
				/** It is an stub **/
				return true;
			} catch (final NoSuchFieldException e2) {
				return false;
			} catch (final Exception e1) {
				return false;
			}
		} catch (final ClassNotFoundException e2) {
			return false;
		}

	}

	/**
	 * Nullify all fields of the object in order to free space.
	 * @param object
	 *            Object to nullify.
	 */
	public static void nullifyAllFields(final Object object) {
		try {
			final Field[] fields = object.getClass().getDeclaredFields();
			for (final Field f : fields) {
				try {
					f.setAccessible(true);
					if (!Reflector.isStubField(f.getName()) && !f.getType().isPrimitive()) {
						f.set(object, null);
					}
					// Primitive fields spend same memory space, do not bother to nullify,
					// this function is used to free memory
				} catch (final Exception e) {
					// ignore, security or illegal access exceptions
				}
			}
		} catch (final SecurityException e) {
			// ignore, security exceptions
		}
	}

	/**
	 * Get package name from full class name
	 * @param fullName
	 *            Full package name
	 * @return Package name
	 */
	public static String getPackageName(final String fullName) {
		final int lastDot = fullName.lastIndexOf('.');
		if (lastDot == -1) {
			return "default"; // the class is in the default package
		}
		return fullName.substring(0, lastDot);
	}

}
