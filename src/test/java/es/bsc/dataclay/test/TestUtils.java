
/**
 * @file TestUtils.java
 * @date May 16, 2013
 * @author dgasull
 */

/**
 * @package testutils
 * @brief Module intended to provide utility functions to tests
 */
package es.bsc.dataclay.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import es.bsc.dataclay.util.Configuration;
import org.apache.commons.io.FileUtils;

import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * @class TestUtils
 * @brief This class represents some Utility functions for testing
 * @author dgasull
 */
public final class TestUtils {

	private TestUtils() {

	}

	private static final int ARRAYSIZE = 2; // !<@brief Size of arrays of generated instances

	private static final int MAXARRAY_LEN = 10;
	// !<@brief Maximum array length in a randomly generated arrays
	private static final int NUM_CHARS = 26;
	// !<@brief Number of seconds per hour for contract registration
	private static final Class<?>[] PRIMITIVEORBASICCLASS = new Class<?>[] { boolean.class, int.class, float.class,
			long.class, short.class, byte.class, double.class, char.class, int[].class, double[][].class };

	public static final String GETTER_PREFFIX = "$$get";
	// !<@brief Preffix of getter operations
	public static final String SETTER_PREFFIX = "$$set";
	// !<@brief Preffix of getter operations

	/**
	 * @brief Given a class name this function gets its Class using the provided class loader It is useful in case of arrays and for
	 *        loading basic types.
	 * @param clName
	 *            Name of the class to load
	 * @param cl
	 *            Classloader containing the class
	 * @return The class with name provided in the classloader provided
	 */
	public static Class<?> getClass(final String clName, final ClassLoader cl) {

		String typeName = clName;
		if (typeName.contains("[")) {
			// The symbols will be B,Z,I,... in case of arrays

			int dim = typeName.lastIndexOf("[") + 1;
			String componentName = typeName.replace("[", "");
			Class<?> componentType = null;
			switch (componentName) {
			default:
				componentName = componentName.replace("L", "");
				componentName = componentName.replace(";", "");
				try {
					componentType = cl.loadClass(componentName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case "Z":
				componentType = boolean.class;
				break;
			case "B":
				componentType = byte.class;
				break;
			case "C":
				componentType = char.class;
				break;
			case "D":
				componentType = double.class;
				break;
			case "F":
				componentType = float.class;
				break;
			case "I":
				componentType = int.class;
				break;
			case "J":
				componentType = long.class;
				break;
			case "S":
				componentType = short.class;
				break;
			}

			Object array = Array.newInstance(componentType, new int[dim]);
			return array.getClass();

		} else {

			switch (typeName) {
			default:
				try {
					return cl.loadClass(typeName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			case "boolean":
				return boolean.class;
			case "byte":
				return byte.class;
			case "char":
				return char.class;
			case "double":
				return double.class;
			case "float":
				return float.class;
			case "int":
				return int.class;
			case "long":
				return long.class;
			case "short":
				return short.class;
			}

		}
	}

	/**
	 * @brief Given a class name this function gets its Class using the provided class loader It is useful in case of arrays. If the
	 *        class is primitive, this class loads the object primitive type Integer, Double, Character,... instead of int, float...
	 *        This is used to verify the result of operations since stubs does not return basic types yet.
	 * @param clName
	 *            Name of the class
	 * @param cl
	 *            ClassLoader containing the class
	 * @return The class with name provided in the classloader provided. If the class name indicates a primitive type, then the
	 *         object class representing the primitive type is returned (Integer, Double,...)
	 */
	public static Class<?> getClassNonPrimitive(final String clName, final ClassLoader cl) {

		String typeName = clName;
		if (typeName.contains("[")) {
			// The symbols will be B,Z,I,... in case of arrays

			int dim = typeName.lastIndexOf("[") + 1;
			String componentName = typeName.replace("[", "");
			Class<?> componentType = null;
			switch (componentName) {
			default:
				componentName = componentName.replace("L", "");
				componentName = componentName.replace(";", "");
				try {
					componentType = cl.loadClass(componentName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case "Z":
				componentType = Boolean.class;
				break;
			case "B":
				componentType = Byte.class;
				break;
			case "C":
				componentType = Character.class;
				break;
			case "D":
				componentType = Double.class;
				break;
			case "F":
				componentType = Float.class;
				break;
			case "I":
				componentType = Integer.class;
				break;
			case "J":
				componentType = Long.class;
				break;
			case "S":
				componentType = Short.class;
				break;
			}

			Object array = Array.newInstance(componentType, new int[dim]);
			return array.getClass();

		} else {

			switch (typeName) {
			default:
				try {
					return cl.loadClass(typeName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			case "boolean":
				return Boolean.class;
			case "byte":
				return Byte.class;
			case "char":
				return Character.class;
			case "double":
				return Double.class;
			case "float":
				return Float.class;
			case "int":
				return Integer.class;
			case "long":
				return Long.class;
			case "short":
				return Short.class;
			}

		}
	}

	private static int actualSize = 0;

	/**
	 * @brief Returns a random value of the class provided. If it is an array it generates an array with random values. If the class
	 *        is not primitive, then null is returned (or an array of nulls).
	 * @param classParam
	 *            Class of the type to generate
	 * @param first
	 *            Must be true. Used in recursive calls.
	 * @return An object representing a random value of the class provided (including arrays of random values) or null if the class
	 *         is not primitive.
	 */
	public static Object getRandomValue(final Class<?> classParam, final boolean first) {

		Random rand = new Random();
		if (classParam.isArray()) {

			if (first) {
				actualSize = rand.nextInt(MAXARRAY_LEN);
				while (actualSize < 0) {
					actualSize = rand.nextInt(MAXARRAY_LEN);
				}
			}

			int dim = classParam.getName().lastIndexOf("[") + 1;
			Class<?> componentType = classParam.getComponentType();
			Class<?> nonArrayType = componentType;
			while (nonArrayType.isArray()) {
				nonArrayType = nonArrayType.getComponentType();
			}

			int[] dimSizes = new int[dim];
			for (int i = 0; i < dim; ++i) {
				dimSizes[i] = actualSize;
			}

			Object array = Array.newInstance(nonArrayType, dimSizes);
			for (int j = 0; j < actualSize; ++j) {
				Array.set(array, j, getRandomValue(componentType, false));
			}

			return array;

		} else if (classParam.equals(int.class) || classParam.equals(Integer.class)) {
			return rand.nextInt();
		} else if (classParam.equals(float.class) || classParam.equals(Float.class)) {
			return rand.nextFloat();
		} else if (classParam.equals(boolean.class) || classParam.equals(Boolean.class)) {
			return rand.nextBoolean();
		} else if (classParam.equals(double.class) || classParam.equals(Double.class)) {
			return rand.nextDouble();
		} else if (classParam.equals(long.class) || classParam.equals(Long.class)) {
			return rand.nextLong();
		} else if (classParam.equals(char.class) || classParam.equals(Character.class)) {
			return (char) (rand.nextInt(NUM_CHARS) + 'a');
		} else if (classParam.equals(short.class) || classParam.equals(Short.class)) {
			return (short) rand.nextInt(Short.MAX_VALUE + 1);
		} else if (classParam.equals(byte.class) || classParam.equals(Byte.class)) {
			byte[] result = new byte[1];
			rand.nextBytes(result);
			return result[0];
		} else if (classParam.equals(String.class)) {
			return TestingTools.generateRandomString(MAXARRAY_LEN);
		} else {
			return null;
		}
	}

	public static Object getRandomValue(final boolean first) {

		Random rand = new Random();
		int typeIndx = rand.nextInt(PRIMITIVEORBASICCLASS.length);
		Class<?> type = PRIMITIVEORBASICCLASS[typeIndx];
		if (type.isArray()) {

			if (first) {
				actualSize = rand.nextInt(MAXARRAY_LEN);
				while (actualSize < 0) {
					actualSize = rand.nextInt(MAXARRAY_LEN);
				}
			}

			int dim = type.getName().lastIndexOf("[") + 1;
			Class<?> componentType = type.getComponentType();
			Class<?> nonArrayType = componentType;
			while (nonArrayType.isArray()) {
				nonArrayType = nonArrayType.getComponentType();
			}

			int[] dimSizes = new int[dim];
			for (int i = 0; i < dim; ++i) {
				dimSizes[i] = actualSize;
			}

			Object array = Array.newInstance(nonArrayType, dimSizes);
			for (int j = 0; j < actualSize; ++j) {
				Array.set(array, j, getRandomValue(componentType, false));
			}

			return array;

		} else if (type.equals(int.class)) {
			return rand.nextInt();
		} else if (type.equals(float.class)) {
			return rand.nextFloat();
		} else if (type.equals(boolean.class)) {
			return rand.nextBoolean();
		} else if (type.equals(double.class)) {
			return rand.nextDouble();
		} else if (type.equals(long.class)) {
			return rand.nextLong();
		} else if (type.equals(char.class)) {
			return (char) (rand.nextInt(NUM_CHARS) + 'a');
		} else if (type.equals(short.class)) {
			return (short) rand.nextInt(Short.MAX_VALUE + 1);
		} else if (type.equals(byte.class)) {
			byte[] result = new byte[1];
			rand.nextBytes(result);
			return result[0];
		} else if (type.equals(String.class)) {
			return TestingTools.generateRandomString(MAXARRAY_LEN);
		} else {
			return null;
		}
	}

	/**
	 * @brief Generate field values for the provided stubs (two instances for query or clone cases)
	 * @param stubInstance
	 * @param anotherStubInstance
	 * @throws Exception
	 */

	public static void generateFieldValuesRecursively(
			final Object stubInstance, final Object anotherStubInstance,
			final boolean useSelfLoop) throws Exception {

		// Set the primitive AND the user type fields.
		Object self = null;
		if (useSelfLoop) {
			self = stubInstance;
		}
		Field[] fields = stubInstance.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; ++i) {
			Class<?> fieldType = fields[i].getType();
			String fieldName = fields[i].getName();
			if (Modifier.isStatic(fields[i].getModifiers())
					|| Reflector.isStubField(fieldName)) {
				continue;
			}

			Object fieldValueToMatch = generateFieldValue(stubInstance.getClass(), fieldType, self);
			if (fieldValueToMatch != null) {
				Reflector.setField(stubInstance, fields[i].getName(), fieldValueToMatch);
				if (anotherStubInstance != null) {
					Reflector.setField(anotherStubInstance, fields[i].getName(), fieldValueToMatch);
				}
				// fieldValues.put(fields[i].getName(), fieldValueAndValueToMatch.getSecond());

			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object generateFieldValue(final Class<?> classOfField, final Class<?> fieldType,
			final Object self)
			throws Exception {

		if (fieldType.equals(Object.class)) {

			return null;

		} else if (fieldType.isArray()) {

			int length = ARRAYSIZE;
			Object array = Array.newInstance(fieldType.getComponentType(), length);

			// For each element in the array
			for (int i = 0; i < length; ++i) {
				Object arrayElement = generateFieldValue(classOfField, fieldType.getComponentType(), self);
				if (arrayElement != null) {
					Array.set(array, i, arrayElement);
				}

			}
			return array;

		} else if (Collection.class.isAssignableFrom(fieldType)) {

			String specificClassName = fieldType.getName();
			Class<? extends Collection> collectionClass = null;
			Collection<Object> newCollection = null;

			try {
				collectionClass = (Class<? extends Collection>) Thread.currentThread().getContextClassLoader().loadClass(specificClassName);
				newCollection = collectionClass.newInstance();

			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}

			int length = ARRAYSIZE;
			for (int i = 0; i < length; ++i) {
				Object arrayElement = generateFieldValue(classOfField, Float.class, self);
				if (arrayElement != null) {
					newCollection.add(arrayElement);
				}
			}

			return newCollection;

		} else if (Map.class.isAssignableFrom(fieldType)) {

			String specificClassName = fieldType.getName();
			Class<? extends Map> mapClass = null;
			Map<Object, Object> newMap = null;

			try {
				mapClass = (Class<? extends Map>) Thread.currentThread().getContextClassLoader().loadClass(specificClassName);
				newMap = mapClass.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}

			int length = ARRAYSIZE;
			for (int i = 0; i < length; ++i) {
				Object arrayElement = generateFieldValue(classOfField, Float.class, self);
				if (arrayElement != null) {

					Integer key = (Integer) TestUtils.getRandomValue(Integer.class, true);
					newMap.put(key, arrayElement);
				}

			}

			return newMap;

		} else if (fieldType.isPrimitive() || Reflector.isJavaTypeName(fieldType.getName())) {
			// Primitive or Java primitive type
			Object fieldValue = TestUtils.getRandomValue(fieldType, true);
			return fieldValue;
		} else if (classOfField.equals(fieldType)) {
			return self;
		} else {
			// it is user type
			// Instantiate it. Since it can be a stub or not we just seek for a constructor
			Object fieldValue = createInstance(fieldType);
			assertTrue(fieldValue != null);
			generateFieldValuesRecursively(fieldValue, null, false);
			return fieldValue;

		}

	}

	public static Object createInstance(final Class<?> instanceClass)
			throws Exception {
		Object instance = instanceClass.getConstructor(ObjectID.class).newInstance(new ObjectID());
		return instance;
	}

	public static void createOrCleanDirectory(final String dirName) {
		File fileDir = new File(dirName);
		try {
			FileUtils.deleteDirectory(fileDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileDir.mkdirs();
	}

	public static void cleanDirectory(final String dirName) {

	}

	public static void deleteDirectory(final String dirName) {
		File fileDir = new File(dirName);
		try {
			FileUtils.deleteDirectory(fileDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
