
package es.bsc.dataclay.dbhandler.test_mgm_classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

public final class TestClass extends TestParent {

	// === PRIMITIVE TYPES == //
	private int primitiveInt;
	private double primitiveDouble;
	private long primitiveLong;
	private char primitiveChar;
	private byte primitiveByte;
	private boolean primitiveBool;
	private short primitiveShort;
	private float primitiveFloat;
	
	// === LANG TYPES == //
	private String langString;
	private Boolean langBoolean;
	private Integer langInteger;
	private Double langDouble;
	private Long langLong; 
	private Character langCharacter;
	private Byte langByte;
	private Short langShort;
	private Float langFloat;
	
	
	// === PRIMITIVE ARRAYS == //
	private int[] intArray;
	private double[] doubleArray;
	private long[] longArray;
	private char[] charArray;
	private byte[] byteArray;
	private boolean[] booleanArray;
	private short[] shortArray;
	private float[] floatArray;
	
	// === PRIMITIVE MATRIX == //
	private int[][] intMatrix;
	private double[][] doubleMatrix;
	private long[][] longMatrix;
	private char[][] charMatrix;
	private byte[][] byteMatrix;
	private boolean[][] booleanMatrix;
	private short[][] shortMatrix;
	private float[][] floatMatrix;

	// === LANGUAGE ARRAYS == //
	private Integer[] langIntArray;
	private Double[] langDoubleArray;
	private Long[] langLongArray;
	private Character[] langCharArray;
	private Byte[] langByteArray;
	private Boolean[] langBooleanArray;
	private Short[] langShortArray;
	private Float[] langFloatArray;
	
	// === LANGUAGE MATRIXS == //
	private Integer[][] langIntMatrix;
	private Double[][] langDoubleMatrix;
	private Long[][] langLongMatrix;
	private Character[][] langCharMatrix;
	private Byte[][] langByteMatrix;
	private Boolean[][] langBooleanMatrix;
	private Short[][] langShortMatrix;
	private Float[][] langFloatMatrix;
	
	// === JAVA SETS === //
	private TreeSet<Integer> javaTreeSet;
	private HashSet<Double> javaHashSet;
	private ArrayList<Long> javaEmptyHashSet;
	private LinkedList<Short> javaLinkedList;

	// === JAVA MAPS === //
	private HashMap<Short, Character> javaHashMap;
	
	// === JAVA COMPLEX MAPS === //
	private HashMap<String, Integer[]> tableOfArrays;
	private HashMap<String, HashMap<String, Boolean>> mapOfmaps;
	
	// === USER TYPE === //
	private TestType userType;
	private TestType[] userArray;
	private TestType[][] userMatrix;
	private HashSet<TestType> userHashSet;
	private ArrayList<TestType> userList;

	// === EMPTY SET === //
	private HashSet<TestType> userEmptyHashSet;
	
	// === USER TYPE MAP === //
	private HashMap<TestType, TestType> userHashMap;
	private HashMap<Integer, TestType> userMapWithJavaKey;
	private HashMap<TestType, Long> userMapWithJavaValue;

	// === GENERIC TYPES === //
	private Object generic;
	private Object[] genericArray;
	private Object[][] genericMatrix;
	private HashSet<Object> genericSet;
	private HashMap<Object, Object> genericMap;
	// === SELF === //
	private TestClass self;

	// === NULL === //
	private TestType userTypeNullField;
	private Integer javaNullField;
	private HashMap<TestType, TestType> userMapWithNullValue;
	private HashMap<TestType, TestType> userMapWithNullKey;
	private HashMap<String, Integer> javaMapWithNullKey;
	private HashMap<Double, Integer> javaMapWithNullValue;
	private HashMap<Object, Object> genericMapWithNullKey;
	private HashMap<Object, Object> genericWithNullValue;
	
	//private TestType[] userArrayWithNull;
	//private TestType[][] userMatrixWithNull;
	//private Character[] javaArrayWithNull;
	//private Boolean[][] javaMatrixWithNull;

	private static final int MAX_ARRAY = 5;
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890.=$&";
	
	public TestClass() {
		super();
	}
	
	public TestClass(final int value) {
		super(value);
		Random random = new Random();
				
		// === PRIMITIVE TYPES == //
		this.setPrimitiveInt(random.nextInt());
		this.setPrimitiveBool(random.nextBoolean());
		byte[] bytes = new byte[1];
		random.nextBytes(bytes);
		this.setPrimitiveByte(bytes[0]);
		this.setPrimitiveChar(alphabet.charAt(random.nextInt(alphabet.length())));
		this.setPrimitiveDouble(random.nextDouble());
		this.setPrimitiveFloat(random.nextFloat());
		this.setPrimitiveLong(random.nextLong());
		this.setPrimitiveShort((short) random.nextInt(Short.MAX_VALUE + 1));
		
		// === LANGUAGE TYPES == //
		this.setLangInteger(random.nextInt());
		this.setLangBoolean(random.nextBoolean());
		bytes = new byte[1];
		random.nextBytes(bytes);
		this.setLangByte(bytes[0]);
		this.setLangCharacter(alphabet.charAt(random.nextInt(alphabet.length())));
		this.setLangDouble(random.nextDouble());
		this.setLangFloat(random.nextFloat());
		this.setLangLong(random.nextLong());
		this.setLangShort((short) random.nextInt(Short.MAX_VALUE + 1));
		this.setLangString(generateRandomString(MAX_ARRAY));
		
		// === PRIMITIVE ARRAYS == //
		int[] newintArray = new int[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newintArray.length; ++i) { newintArray[i] = random.nextInt(); }
		this.setIntArray(newintArray);
		boolean[] newbooleanArray = new boolean[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newbooleanArray.length; ++i) { newbooleanArray[i] = random.nextBoolean(); }
		this.setBooleanArray(newbooleanArray);
		byte[] newbyteArray = new byte[random.nextInt(MAX_ARRAY)];
		random.nextBytes(newbyteArray);
		this.setByteArray(newbyteArray);
		char[] newcharArray = new char[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newcharArray.length; ++i) { newcharArray[i] = alphabet.charAt(random.nextInt(alphabet.length())); }
		this.setCharArray(newcharArray);
		double[] newdoubleArray = new double[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newdoubleArray.length; ++i) { newdoubleArray[i] = random.nextDouble(); }
		this.setDoubleArray(newdoubleArray);
		float[] newfloatArray = new float[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newfloatArray.length; ++i) { newfloatArray[i] = random.nextFloat(); }
		this.setFloatArray(newfloatArray);
		long[] newlongArray = new long[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlongArray.length; ++i) { newlongArray[i] = random.nextLong(); }
		this.setLongArray(newlongArray);
		short[] newshortArray = new short[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newshortArray.length; ++i) { newshortArray[i] = (short) random.nextInt(Short.MAX_VALUE + 1); }
		this.setShortArray(newshortArray);
		
		// === LANGUAGE ARRAYS == //
		Integer[] newlangIntArray = new Integer[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlangIntArray.length; ++i) { newlangIntArray[i] = random.nextInt(); }
		this.setLangIntArray(newlangIntArray);
		Boolean[] newlangBooleanArray = new Boolean[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlangBooleanArray.length; ++i) { newlangBooleanArray[i] = random.nextBoolean(); }
		this.setLangBooleanArray(newlangBooleanArray);
		newbyteArray = new byte[random.nextInt(MAX_ARRAY)];
		random.nextBytes(newbyteArray);
		Byte[] newlangByteArray = new Byte[newbyteArray.length];
		for (int i = 0; i < newlangByteArray.length; ++i) { newlangByteArray[i] = newbyteArray[i]; }
		this.setLangByteArray(newlangByteArray);
		Character[] newlangCharArray = new Character[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlangCharArray.length; ++i) { newlangCharArray[i] = alphabet.charAt(random.nextInt(alphabet.length())); }
		this.setLangCharArray(newlangCharArray);
		Double[] newlangDoubleArray = new Double[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlangDoubleArray.length; ++i) { newlangDoubleArray[i] = random.nextDouble(); }
		this.setLangDoubleArray(newlangDoubleArray);
		Float[] newlangFloatArray = new Float[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlangFloatArray.length; ++i) { newlangFloatArray[i] = random.nextFloat(); }
		this.setLangFloatArray(newlangFloatArray);
		Long[] newlangLongArray = new Long[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlangLongArray.length; ++i) { newlangLongArray[i] = random.nextLong(); }
		this.setLangLongArray(newlangLongArray);
		Short[] newlangShortArray = new Short[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlangShortArray.length; ++i) { newlangShortArray[i] = (short) random.nextInt(Short.MAX_VALUE + 1); }
		this.setLangShortArray(newlangShortArray);
		
		// === PRIMITIVE MATRIXS == //
		int[][] newintMatrix = new int[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newintMatrix.length; ++i) {
			for (int j = 0; j < newintMatrix[i].length; ++j) {
				newintMatrix[i][j] = random.nextInt();
			}
		}
		this.setIntMatrix(newintMatrix);
		boolean[][] newbooleanMatrix = new boolean[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newbooleanMatrix.length; ++i) {
			for (int j = 0; j < newbooleanMatrix[i].length; ++j) {
				newbooleanMatrix[i][j] = random.nextBoolean();
			}
		}
		this.setBooleanMatrix(newbooleanMatrix);
		byte[][] newbyteMatrix = new byte[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newbyteMatrix.length; ++i) {
			random.nextBytes(newbyteMatrix[i]);
		}
		this.setByteMatrix(newbyteMatrix);
		char[][] newcharMatrix = new char[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newcharMatrix.length; ++i) {
			for (int j = 0; j < newcharMatrix[i].length; ++j) {
				newcharMatrix[i][j] = alphabet.charAt(random.nextInt(alphabet.length()));
			}
		}
		this.setCharMatrix(newcharMatrix);
		double[][] newdoubleMatrix = new double[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newdoubleMatrix.length; ++i) {
			for (int j = 0; j < newdoubleMatrix[i].length; ++j) {
				newdoubleMatrix[i][j] = random.nextDouble();
			}
		}
		this.setDoubleMatrix(newdoubleMatrix);
		float[][] newfloatMatrix = new float[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newfloatMatrix.length; ++i) {
			for (int j = 0; j < newfloatMatrix[i].length; ++j) {
				newfloatMatrix[i][j] = random.nextFloat();
			}
		}
		this.setFloatMatrix(newfloatMatrix);
		long[][] newlongMatrix = new long[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newlongMatrix.length; ++i) {
			for (int j = 0; j < newlongMatrix[i].length; ++j) {
				newlongMatrix[i][j] = random.nextLong();
			}
		}
		this.setLongMatrix(newlongMatrix);
		short[][] newshortMatrix = new short[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newshortMatrix.length; ++i) {
			for (int j = 0; j < newshortMatrix[i].length; ++j) {
				newshortMatrix[i][j] = (short) random.nextInt(Short.MAX_VALUE + 1);
			}
		}
		this.setShortMatrix(newshortMatrix);
		
		// === LANGUAGE MATRIXS == //
		Integer[][] newLangIntMatrix = new Integer[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangIntMatrix.length; ++i) {
			for (int j = 0; j < newLangIntMatrix[i].length; ++j) {
				newLangIntMatrix[i][j] = random.nextInt();
			}
		}
		this.setLangIntMatrix(newLangIntMatrix);
		Boolean[][] newLangBooleanMatrix = new Boolean[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangBooleanMatrix.length; ++i) {
			for (int j = 0; j < newLangBooleanMatrix[i].length; ++j) {
				newLangBooleanMatrix[i][j] = random.nextBoolean();
			}
		}
		this.setLangBooleanMatrix(newLangBooleanMatrix);
		Byte[][] newLangByteMatrix = new Byte[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangByteMatrix.length; ++i) {
			byte[] bs = new byte[newLangByteMatrix[i].length];
			random.nextBytes(bs);
			for (int j = 0; j < bs.length; ++j) { newLangByteMatrix[i][j] = bs[j]; }
		}
		this.setLangByteMatrix(newLangByteMatrix);
		Character[][] newLangCharMatrix = new Character[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangCharMatrix.length; ++i) {
			for (int j = 0; j < newLangCharMatrix[i].length; ++j) {
				newLangCharMatrix[i][j] = alphabet.charAt(random.nextInt(alphabet.length()));
			}
		}
		this.setLangCharMatrix(newLangCharMatrix);
		Double[][] newLangDoubleMatrix = new Double[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangDoubleMatrix.length; ++i) {
			for (int j = 0; j < newLangDoubleMatrix[i].length; ++j) {
				newLangDoubleMatrix[i][j] = random.nextDouble();
			}
		}
		this.setLangDoubleMatrix(newLangDoubleMatrix);
		Float[][] newLangFloatMatrix = new Float[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangFloatMatrix.length; ++i) {
			for (int j = 0; j < newLangFloatMatrix[i].length; ++j) {
				newLangFloatMatrix[i][j] = random.nextFloat();
			}
		}
		this.setLangFloatMatrix(newLangFloatMatrix);
		Long[][] newLangLongMatrix = new Long[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangLongMatrix.length; ++i) {
			for (int j = 0; j < newLangLongMatrix[i].length; ++j) {
				newLangLongMatrix[i][j] = random.nextLong();
			}
		}
		this.setLangLongMatrix(newLangLongMatrix);
		Short[][] newLangShortMatrix = new Short[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newLangShortMatrix.length; ++i) {
			for (int j = 0; j < newLangShortMatrix[i].length; ++j) {
				newLangShortMatrix[i][j] = (short) random.nextInt(Short.MAX_VALUE + 1);
			}
		}
		this.setLangShortMatrix(newLangShortMatrix);
		
		// ======== JAVA COLLECTIONS ======== // 
		int collSize = random.nextInt(MAX_ARRAY);
		TreeSet<Integer> newjavaTreeSet = new TreeSet<Integer>();
		for (int i = 0; i < collSize; ++i) { newjavaTreeSet.add(random.nextInt()); }
		this.setJavaTreeSet(newjavaTreeSet);
		collSize = random.nextInt(MAX_ARRAY);
		HashSet<Double> newjavaHashSet = new HashSet<Double>();
		for (int i = 0; i < collSize; ++i) { newjavaHashSet.add(random.nextDouble()); }
		this.setJavaHashSet(newjavaHashSet);
		collSize = random.nextInt(MAX_ARRAY);
		LinkedList<Short> newjavaLinkedList = new LinkedList<Short>();
		for (int i = 0; i < collSize; ++i) { newjavaLinkedList.add((short) random.nextInt(Short.MAX_VALUE + 1)); }
		this.setJavaLinkedList(newjavaLinkedList);
		this.setJavaEmptyHashSet(new ArrayList<Long>());

		// ======== JAVA MAPS ======== // 
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<Short, Character> newjavaHashMap = new HashMap<Short, Character>();
		for (int i = 0; i < collSize; ++i) {
			newjavaHashMap.put((short) random.nextInt(Short.MAX_VALUE + 1),
				alphabet.charAt(random.nextInt(alphabet.length()))); 
		}
		this.setJavaHashMap(newjavaHashMap);
		
		// ======== JAVA COMPLEXT MAPS ======== // 
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<String, Integer[]> newtableOfArrays = new HashMap<String, Integer[]>();
		for (int i = 0; i < collSize; ++i) {
			Integer[] intArr = new Integer[random.nextInt(MAX_ARRAY)];
			for (int j = 0; j < intArr.length; ++j) { intArr[j] = random.nextInt(); }
			newtableOfArrays.put(generateRandomString(MAX_ARRAY), intArr);
		}
		this.setTableOfArrays(newtableOfArrays);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<String, HashMap<String, Boolean>> newmapOfmaps = new HashMap<String, HashMap<String, Boolean>>();
		for (int i = 0; i < collSize; ++i) {
			HashMap<String, Boolean> map = new HashMap<String, Boolean>();
			for (int j = 0; j < collSize; ++j) {
				map.put(generateRandomString(MAX_ARRAY), random.nextBoolean());
			}
			newmapOfmaps.put(generateRandomString(MAX_ARRAY), map);
		}
		this.setMapOfmaps(newmapOfmaps);
		
		// ======== USER TYPE ======== // 
		this.setUserType(new TestType(value));
		TestType[] newuserArray = new TestType[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newuserArray.length; ++i) { 
			newuserArray[i] = new TestType(random.nextInt());
		}
		this.setUserArray(newuserArray);
		TestType[][] newuserMatrix = new TestType[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newuserMatrix.length; ++i) { 
			for (int j = 0; j < newuserMatrix[i].length; ++j) { 
				newuserMatrix[i][j] = new TestType(random.nextInt());
			}
		}
		this.setUserMatrix(newuserMatrix);
		collSize = random.nextInt(MAX_ARRAY);
		HashSet<TestType> newuserHashSet = new HashSet<TestType>();
		for (int i = 0; i < collSize; ++i) { newuserHashSet.add(new TestType(random.nextInt())); }
		this.setUserHashSet(newuserHashSet); 
		collSize = random.nextInt(MAX_ARRAY);
		ArrayList<TestType> newuserList = new ArrayList<TestType>();
		for (int i = 0; i < collSize; ++i) { newuserList.add(new TestType(random.nextInt())); }
		this.setUserList(newuserList);
		this.setUserEmptyHashSet(new HashSet<TestType>());
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<TestType, TestType> newuserHashMap = new HashMap<TestType, TestType>();
		for (int i = 0; i < collSize; ++i) {
			newuserHashMap.put(new TestType(random.nextInt()), new TestType(random.nextInt()));
		}
		this.setUserHashMap(newuserHashMap);
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<Integer, TestType> newuserMapWithJavaKey = new HashMap<Integer, TestType>();
		for (int i = 0; i < collSize; ++i) {
			newuserMapWithJavaKey.put(random.nextInt(), new TestType(random.nextInt()));
		}
		this.setUserMapWithJavaKey(newuserMapWithJavaKey);
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<TestType, Long> newuserMapWithJavaValue = new HashMap<TestType, Long>();
		for (int i = 0; i < collSize; ++i) {
			newuserMapWithJavaValue.put(new TestType(random.nextInt()), random.nextLong());
		}
		this.setUserMapWithJavaValue(newuserMapWithJavaValue);
		
		// === GENERIC == //
		this.setGeneric(new TestType(random.nextInt()));
		
		Object[] newgenericArray = new Object[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newgenericArray.length; ++i) { 
			if (i % 2 == 0) { 
				newgenericArray[i] = random.nextInt();
			} else { 
				newgenericArray[i] = new TestType(random.nextInt());
			}
		}
		this.setGenericArray(newgenericArray);
		
		Object[][] newgenericMatrix = new Object[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newgenericMatrix.length; ++i) { 
			for (int j = 0; j < newgenericMatrix[i].length; ++j) { 
				if (i % 2 == 0) { 
					newgenericMatrix[i][j] = random.nextFloat();
				} else { 
					newgenericMatrix[i][j] = new TestType(random.nextInt());
				}
			}
		}
		this.setGenericMatrix(newgenericMatrix);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashSet<Object> newgenericSet = new HashSet<Object>(); 
		for (int i = 0; i < collSize; ++i) { 
			if (i % 2 == 0) { 
				newgenericSet.add(random.nextBoolean());
			} else { 
				newgenericSet.add(new TestType(random.nextInt()));
			}
		}
		this.setGenericSet(newgenericSet);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<Object, Object> newgenericMap = new HashMap<Object, Object>(); 
		for (int i = 0; i < collSize; ++i) { 
			if (i % 2 == 0) { 
				newgenericMap.put(random.nextDouble(), new TestType(random.nextInt()));
			} else { 
				newgenericMap.put(new TestType(random.nextInt()), random.nextLong());
			}
		}
		this.setGenericMap(newgenericMap);
		
		// ==== SELF ==== // 
		this.setSelf(this);
		
		// ==== NULL FIELDS === // 
		this.setUserTypeNullField(null);
		this.setJavaNullField(null);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<TestType, TestType> newuserMapWithNullValue = new HashMap<TestType, TestType>(); 
		for (int i = 0; i < collSize; ++i) { 
			newuserMapWithNullValue.put(new TestType(random.nextInt()), null);
		}
		this.setUserMapWithNullValue(newuserMapWithNullValue);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<TestType, TestType> newuserMapWithNullKey = new HashMap<TestType, TestType>(); 
		newuserMapWithNullValue.put(null, new TestType(random.nextInt()));
		for (int i = 0; i < collSize; ++i) { 
			newuserMapWithNullValue.put(new TestType(random.nextInt()), new TestType(random.nextInt()));
		}
		this.setUserMapWithNullKey(newuserMapWithNullKey);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<Double, Integer> newjavaMapWithNullValue = new HashMap<Double, Integer>(); 
		for (int i = 0; i < collSize; ++i) { 
			newjavaMapWithNullValue.put(random.nextDouble(), null);
		}
		this.setJavaMapWithNullValue(newjavaMapWithNullValue);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<String, Integer> newjavaMapWithNullKey = new HashMap<String, Integer>(); 
		newjavaMapWithNullKey.put(null, random.nextInt());
		for (int i = 0; i < collSize; ++i) { 
			newjavaMapWithNullKey.put(generateRandomString(MAX_ARRAY), random.nextInt());
		}
		this.setJavaMapWithNullKey(newjavaMapWithNullKey);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<Object, Object> newgenericMapWithNullValue = new HashMap<Object, Object>(); 
		for (int i = 0; i < collSize; ++i) { 
			newgenericMapWithNullValue.put(random.nextDouble(), null);
		}
		this.setGenericWithNullValue(newgenericMapWithNullValue);
		
		collSize = random.nextInt(MAX_ARRAY);
		HashMap<Object, Object> newgenericMapWithNullKey = new HashMap<Object, Object>(); 
		newgenericMapWithNullKey.put(null, random.nextInt());
		for (int i = 0; i < collSize; ++i) { 
			newgenericMapWithNullKey.put(generateRandomString(MAX_ARRAY), random.nextInt());
		}
		this.setGenericMapWithNullKey(newgenericMapWithNullKey);
		
		/*TestType[] newuserArrayWithNull = new TestType[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newuserArrayWithNull.length; ++i) { 
			if (i % 2 == 0) { 
				newuserArrayWithNull[i] = null;
			} else { 
				newuserArrayWithNull[i] = new TestType(random.nextInt());
			}
		}
		this.setUserArrayWithNull(newuserArrayWithNull);
		
		TestType[][] newuserMatrixWithNull = new TestType[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newuserMatrixWithNull.length; ++i) { 
			if (i % 2 == 0) { 
				newuserMatrixWithNull[i] = null;
			} else { 
				for (int j = 0; j < newuserMatrixWithNull[i].length; ++j) { 
					if (j % 2 == 0) { 
						newuserMatrixWithNull[i][j] = null;
					} else { 
						newuserMatrixWithNull[i][j] = new TestType(random.nextInt());
					}
				}
			}
		}
		this.setUserMatrixWithNull(newuserMatrixWithNull);*/
		
		/*Character[] newjavaArrayWithNull = new Character[random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newjavaArrayWithNull.length; ++i) { 
			if (i % 2 == 0) { 
				newjavaArrayWithNull[i] = alphabet.charAt(random.nextInt(alphabet.length()));
			} else { 
				newjavaArrayWithNull[i] = null;
			}
		}
		this.setJavaArrayWithNull(newjavaArrayWithNull);*/
		
		/*Boolean[][] newjavaMatrixWithNull = new Boolean[random.nextInt(MAX_ARRAY)][random.nextInt(MAX_ARRAY)];
		for (int i = 0; i < newjavaMatrixWithNull.length; ++i) { 
			if (i % 2 == 0) { 
				for (int j = 0; j < newjavaMatrixWithNull[i].length; ++j) { 
					if (j % 2 == 0) { 
						newjavaMatrixWithNull[i][j] = random.nextBoolean();
					} else { 
						newjavaMatrixWithNull[i][j] = null;
					}
				}
			} else { 
				newjavaMatrixWithNull[i] = null;
			}
		}
		this.setJavaMatrixWithNull(newjavaMatrixWithNull);*/
	}

	/**
	 * @brief Generate a random string
	 * @param stringLen String length
	 * @return A random string
	 */
	private String generateRandomString(final int stringLen) {
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		int charactersLength = alphabet.length();
		for (int i = 0; i < stringLen; i++) {
			buffer.append(alphabet.charAt(random.nextInt(charactersLength)));
		}
		return buffer.toString();
	}
	
	
	/**
	* @brief This operation allows to compare this object with 
	* other object.
	* @param t Object to compare
	* @return If the object has the same name, returns TRUE. FALSE, otherwise.
	* @author dgasull
	*/
	@Override
	public boolean equals(final Object t) {
		
		if (t instanceof TestClass) {
			TestClass other = (TestClass) t;	
		
			if (!this.getDataClayID().equals(other.getDataClayID())) { 
				return false; 
			}
			
			// ===== PRIMITIVES ==== //
			if (this.getPrimitiveByte() != other.getPrimitiveByte()) { 
				return false; 
			}
			if (this.getPrimitiveInt() != other.getPrimitiveInt()) { 
				return false; 
			}
			if (this.getPrimitiveDouble() != other.getPrimitiveDouble()) { 
				return false; 
			}
			if (this.getPrimitiveLong() != other.getPrimitiveLong()) { 
				return false; 
			}
			if (this.getPrimitiveChar() != other.getPrimitiveChar()) { 
				return false; 
			}
			if (this.isPrimitiveBool() != other.isPrimitiveBool()) {
				return false; 
			}
			if (this.getPrimitiveShort() != other.getPrimitiveShort()) { 
				return false;
			}
			if (this.getPrimitiveFloat() != other.getPrimitiveFloat()) { 
				return false; 
			}
			
			// ===== LANGUAGE TYPES ==== //
			if (!this.getLangString().equals(other.getLangString())) { 
				return false;
			}
			if (!this.getLangBoolean().equals(other.getLangBoolean())) { 
				return false; 
			}
			if (!this.getLangInteger().equals(other.getLangInteger())) { 
				return false; 
			}
			if (!this.getLangDouble().equals(other.getLangDouble())) { 
				return false; 
			}
			if (!this.getLangLong().equals(other.getLangLong())) {
				return false; 
			}
			if (!this.getLangCharacter().equals(other.getLangCharacter())) { 
				return false;
			}
			if (!this.getLangByte().equals(other.getLangByte())) { 
				return false; 
			}
			if (!this.getLangShort().equals(other.getLangShort())) {
				return false; 
			}
			if (!this.getLangFloat().equals(other.getLangFloat())) { 
				return false; 
			}

			// ===== PRIMITIVE ARRAYS ==== //
			if (!Arrays.equals(this.getIntArray(), other.getIntArray())) { 
				return false; 
			}
			if (!Arrays.equals(this.getDoubleArray(), other.getDoubleArray())) { 
				return false; 
			}
			if (!Arrays.equals(this.getLongArray(), other.getLongArray())) { 
				return false; 
			}
			if (!Arrays.equals(this.getCharArray(), other.getCharArray())) { 
				return false; 
			}
			if (!Arrays.equals(this.getByteArray(), other.getByteArray())) { 
				return false; 
			}
			if (!Arrays.equals(this.getBooleanArray(), other.getBooleanArray())) { 
				return false; 
			}
			if (!Arrays.equals(this.getShortArray(), other.getShortArray())) { 
				return false; 
			}
			if (!Arrays.equals(this.getFloatArray(), other.getFloatArray())) { 
				return false; 
			}
			
			// ===== PRIMITIVE MATRIXS ==== //
			if (!Arrays.deepEquals(this.getIntMatrix(), other.getIntMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getDoubleMatrix(), other.getDoubleMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getLongMatrix(), other.getLongMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getCharMatrix(), other.getCharMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getByteMatrix(), other.getByteMatrix())) { 
				return false;
			}
			if (!Arrays.deepEquals(this.getBooleanMatrix(), other.getBooleanMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getShortMatrix(), other.getShortMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getFloatMatrix(), other.getFloatMatrix())) { 
				return false; 
			}
			
			// ===== LANGUAGE MATRIXS ==== //
			if (!Arrays.deepEquals(this.getLangIntMatrix(), other.getLangIntMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getLangDoubleMatrix(), other.getLangDoubleMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getLangLongMatrix(), other.getLangLongMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getLangCharMatrix(), other.getLangCharMatrix())) { 
				return false;
			}
			if (!Arrays.deepEquals(this.getLangByteMatrix(), other.getLangByteMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getLangBooleanMatrix(), other.getLangBooleanMatrix())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getLangShortMatrix(), other.getLangShortMatrix())) {
				return false; 
			}
			if (!Arrays.deepEquals(this.getLangFloatMatrix(), other.getLangFloatMatrix())) { 
				return false;
			}
		
			// ==== JAVA COLLECTIONS === // 
			if (!this.getJavaTreeSet().equals(other.getJavaTreeSet())) { 
				return false;
			}
			if (!this.getJavaHashSet().equals(other.getJavaHashSet())) { 
				return false; 
			}
			if (!this.getJavaEmptyHashSet().equals(other.getJavaEmptyHashSet())) { 
				return false; 
			}
			if (!this.getJavaLinkedList().equals(other.getJavaLinkedList())) { 
				return false; 
			}
			
			// ==== JAVA MAPS === // 
			if (!this.getJavaHashMap().equals(other.getJavaHashMap())) { 
				return false; 
			}

			// ==== JAVA COMPLEX MAPS === // 
			for (java.util.Map.Entry<String, Integer[]> entry : this.getTableOfArrays().entrySet()) { 
				String key = entry.getKey();
				Integer[] value = entry.getValue();
				Integer[] otherValue = other.getTableOfArrays().get(key);
				if (otherValue == null) { 
					return false;
				} 
				if (!Arrays.deepEquals(value, otherValue)) { 
					return false;
				}
				
			}
			if (!this.getMapOfmaps().equals(other.getMapOfmaps())) {
				return false; 
			}

			// ==== USER TYPES === // 
			if (!this.getUserType().equals(other.getUserType())) { 
				return false;
			}
			if (!Arrays.deepEquals(this.getUserArray(), other.getUserArray())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getUserMatrix(), other.getUserMatrix())) { 
				return false; 
			}
			if (!this.getUserHashSet().equals(other.getUserHashSet())) { 
				return false; 
			}
			if (!this.getUserList().equals(other.getUserList())) { 
				return false; 
			}
			if (!this.getUserEmptyHashSet().equals(other.getUserEmptyHashSet())) { 
				return false; 
			}
			if (!this.getUserHashMap().equals(other.getUserHashMap())) { 
				return false; 
			}
			if (!this.getUserMapWithJavaKey().equals(other.getUserMapWithJavaKey())) { 
				return false; 
			}
			if (!this.getUserMapWithJavaValue().equals(other.getUserMapWithJavaValue())) { 
				return false; 
			}
			
			// ==== GENERIC === // 
			if (!this.getGeneric().equals(other.getGeneric())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getGenericArray(), other.getGenericArray())) { 
				return false; 
			}
			if (!Arrays.deepEquals(this.getGenericMatrix(), other.getGenericMatrix())) { 
				return false; 
			}
			if (!this.getGenericSet().equals(other.getGenericSet())) { 
				return false; 
			}
			if (!this.getGenericMap().equals(other.getGenericMap())) {
				return false; 
			}

			// ==== SELF === // 
			if (!this.getSelf().getDataClayID().equals(this.getDataClayID())) { 
				return false; 
			} 
			
			// ==== NULLS === // 
			if (!(this.getUserTypeNullField() == null && other.getUserTypeNullField() == null)) {
				return false; 
			} 
			if (!(this.getJavaNullField() == null && other.getJavaNullField() == null)) { 
				return false; 
			} 
			if (!this.getUserMapWithNullKey().equals(other.getUserMapWithNullKey())) {
				return false; 
			} 
			if (!this.getUserMapWithNullValue().equals(other.getUserMapWithNullValue())) { 
				return false; 
			} 
			if (!this.getJavaMapWithNullKey().equals(other.getJavaMapWithNullKey())) { 
				return false; 
			} 
			if (!this.getJavaMapWithNullValue().equals(other.getJavaMapWithNullValue())) { 
				return false; 
			} 
			if (!this.getGenericMapWithNullKey().equals(other.getGenericMapWithNullKey())) { 
				return false; 
			} 
			if (!this.getGenericWithNullValue().equals(other.getGenericWithNullValue())) { 
				return false; 
			} 
			//if (!Arrays.deepEquals(this.getUserArrayWithNull(), other.getUserArrayWithNull())) { return false; }
			//if (!Arrays.deepEquals(this.getUserMatrixWithNull(), other.getUserMatrixWithNull())) { return false; }
			//if (!Arrays.deepEquals(this.getJavaArrayWithNull(), other.getJavaArrayWithNull())) { return false; }
			//if (!Arrays.deepEquals(this.getJavaMatrixWithNull(), other.getJavaMatrixWithNull())) { return false; }
			return true;
		}
		return false;
	}

	/**
	 * @brief Get the TestClass::primitiveInt
	 * @return the primitiveInt
	 */
	public int getPrimitiveInt() {
		return primitiveInt;
	}

	/**
	 * @brief Set the TestClass::primitiveInt
	 * @param newprimitiveInt the primitiveInt to set
	 */
	public void setPrimitiveInt(final int newprimitiveInt) {
		this.primitiveInt = newprimitiveInt;
	}

	/**
	 * @brief Get the TestClass::primitiveDouble
	 * @return the primitiveDouble
	 */
	public double getPrimitiveDouble() {
		return primitiveDouble;
	}

	/**
	 * @brief Set the TestClass::primitiveDouble
	 * @param newprimitiveDouble the primitiveDouble to set
	 */
	public void setPrimitiveDouble(final double newprimitiveDouble) {
		this.primitiveDouble = newprimitiveDouble;
	}

	/**
	 * @brief Get the TestClass::primitiveLong
	 * @return the primitiveLong
	 */
	public long getPrimitiveLong() {
		return primitiveLong;
	}

	/**
	 * @brief Set the TestClass::primitiveLong
	 * @param newprimitiveLong the primitiveLong to set
	 */
	public void setPrimitiveLong(final long newprimitiveLong) {
		this.primitiveLong = newprimitiveLong;
	}

	/**
	 * @brief Get the TestClass::primitiveChar
	 * @return the primitiveChar
	 */
	public char getPrimitiveChar() {
		return primitiveChar;
	}

	/**
	 * @brief Set the TestClass::primitiveChar
	 * @param newprimitiveChar the primitiveChar to set
	 */
	public void setPrimitiveChar(final char newprimitiveChar) {
		this.primitiveChar = newprimitiveChar;
	}

	/**
	 * @brief Get the TestClass::primitiveByte
	 * @return the primitiveByte
	 */
	public byte getPrimitiveByte() {
		return primitiveByte;
	}

	/**
	 * @brief Set the TestClass::primitiveByte
	 * @param newprimitiveByte the primitiveByte to set
	 */
	public void setPrimitiveByte(final byte newprimitiveByte) {
		this.primitiveByte = newprimitiveByte;
	}

	/**
	 * @brief Get the TestClass::primitiveBool
	 * @return the primitiveBool
	 */
	public boolean isPrimitiveBool() {
		return primitiveBool;
	}

	/**
	 * @brief Set the TestClass::primitiveBool
	 * @param newprimitiveBool the primitiveBool to set
	 */
	public void setPrimitiveBool(final boolean newprimitiveBool) {
		this.primitiveBool = newprimitiveBool;
	}

	/**
	 * @brief Get the TestClass::primitiveShort
	 * @return the primitiveShort
	 */
	public short getPrimitiveShort() {
		return primitiveShort;
	}

	/**
	 * @brief Set the TestClass::primitiveShort
	 * @param newprimitiveShort the primitiveShort to set
	 */
	public void setPrimitiveShort(final short newprimitiveShort) {
		this.primitiveShort = newprimitiveShort;
	}

	/**
	 * @brief Get the TestClass::primitiveFloat
	 * @return the primitiveFloat
	 */
	public float getPrimitiveFloat() {
		return primitiveFloat;
	}

	/**
	 * @brief Set the TestClass::primitiveFloat
	 * @param newprimitiveFloat the primitiveFloat to set
	 */
	public void setPrimitiveFloat(final float newprimitiveFloat) {
		this.primitiveFloat = newprimitiveFloat;
	}

	/**
	 * @brief Get the TestClass::langString
	 * @return the langString
	 */
	public String getLangString() {
		return langString;
	}

	/**
	 * @brief Set the TestClass::langString
	 * @param newlangString the langString to set
	 */
	public void setLangString(final String newlangString) {
		if (newlangString == null) {
			throw new IllegalArgumentException("langString cannot be null");
		}
		this.langString = newlangString;
	}

	/**
	 * @brief Get the TestClass::langInteger
	 * @return the langInteger
	 */
	public Integer getLangInteger() {
		return langInteger;
	}

	/**
	 * @brief Set the TestClass::langInteger
	 * @param newlangInteger the langInteger to set
	 */
	public void setLangInteger(final Integer newlangInteger) {
		if (newlangInteger == null) {
			throw new IllegalArgumentException("langInteger cannot be null");
		}
		this.langInteger = newlangInteger;
	}

	/**
	 * @brief Get the TestClass::langBoolean
	 * @return the langBoolean
	 */
	public Boolean getLangBoolean() {
		return langBoolean;
	}

	/**
	 * @brief Set the TestClass::langBoolean
	 * @param newlangBoolean the langBoolean to set
	 */
	public void setLangBoolean(final Boolean newlangBoolean) {
		if (newlangBoolean == null) {
			throw new IllegalArgumentException("langBoolean cannot be null");
		}
		this.langBoolean = newlangBoolean;
	}

	/**
	 * @brief Get the TestClass::langDouble
	 * @return the langDouble
	 */
	public Double getLangDouble() {
		return langDouble;
	}

	/**
	 * @brief Set the TestClass::langDouble
	 * @param newlangDouble the langDouble to set
	 */
	public void setLangDouble(final Double newlangDouble) {
		if (newlangDouble == null) {
			throw new IllegalArgumentException("langDouble cannot be null");
		}
		this.langDouble = newlangDouble;
	}

	/**
	 * @brief Get the TestClass::langLong
	 * @return the langLong
	 */
	public Long getLangLong() {
		return langLong;
	}

	/**
	 * @brief Set the TestClass::langLong
	 * @param newlangLong the langLong to set
	 */
	public void setLangLong(final Long newlangLong) {
		if (newlangLong == null) {
			throw new IllegalArgumentException("langLong cannot be null");
		}
		this.langLong = newlangLong;
	}

	/**
	 * @brief Get the TestClass::langCharacter
	 * @return the langCharacter
	 */
	public Character getLangCharacter() {
		return langCharacter;
	}

	/**
	 * @brief Set the TestClass::langCharacter
	 * @param newlangCharacter the langCharacter to set
	 */
	public void setLangCharacter(final Character newlangCharacter) {
		if (newlangCharacter == null) {
			throw new IllegalArgumentException("langCharacter cannot be null");
		}
		this.langCharacter = newlangCharacter;
	}

	/**
	 * @brief Get the TestClass::langByte
	 * @return the langByte
	 */
	public Byte getLangByte() {
		return langByte;
	}

	/**
	 * @brief Set the TestClass::langByte
	 * @param newlangByte the langByte to set
	 */
	public void setLangByte(final Byte newlangByte) {
		if (newlangByte == null) {
			throw new IllegalArgumentException("langByte cannot be null");
		}
		this.langByte = newlangByte;
	}

	/**
	 * @brief Get the TestClass::langShort
	 * @return the langShort
	 */
	public Short getLangShort() {
		return langShort;
	}

	/**
	 * @brief Set the TestClass::langShort
	 * @param newlangShort the langShort to set
	 */
	public void setLangShort(final Short newlangShort) {
		if (newlangShort == null) {
			throw new IllegalArgumentException("langShort cannot be null");
		}
		this.langShort = newlangShort;
	}

	/**
	 * @brief Get the TestClass::langFloat
	 * @return the langFloat
	 */
	public Float getLangFloat() {
		return langFloat;
	}

	/**
	 * @brief Set the TestClass::langFloat
	 * @param newlangFloat the langFloat to set
	 */
	public void setLangFloat(final Float newlangFloat) {
		if (newlangFloat == null) {
			throw new IllegalArgumentException("langFloat cannot be null");
		}
		this.langFloat = newlangFloat;
	}

	/**
	 * @brief Get the TestClass::intArray
	 * @return the intArray
	 */
	public int[] getIntArray() {
		return intArray;
	}

	/**
	 * @brief Set the TestClass::intArray
	 * @param newintArray the intArray to set
	 */
	public void setIntArray(final int[] newintArray) {
		if (newintArray == null) {
			throw new IllegalArgumentException("intArray cannot be null");
		}
		this.intArray = newintArray;
	}

	/**
	 * @brief Get the TestClass::doubleArray
	 * @return the doubleArray
	 */
	public double[] getDoubleArray() {
		return doubleArray;
	}

	/**
	 * @brief Set the TestClass::doubleArray
	 * @param newdoubleArray the doubleArray to set
	 */
	public void setDoubleArray(final double[] newdoubleArray) {
		if (newdoubleArray == null) {
			throw new IllegalArgumentException("doubleArray cannot be null");
		}
		this.doubleArray = newdoubleArray;
	}

	/**
	 * @brief Get the TestClass::longArray
	 * @return the longArray
	 */
	public long[] getLongArray() {
		return longArray;
	}

	/**
	 * @brief Set the TestClass::longArray
	 * @param newlongArray the longArray to set
	 */
	public void setLongArray(final long[] newlongArray) {
		if (newlongArray == null) {
			throw new IllegalArgumentException("longArray cannot be null");
		}
		this.longArray = newlongArray;
	}

	/**
	 * @brief Get the TestClass::charArray
	 * @return the charArray
	 */
	public char[] getCharArray() {
		return charArray;
	}

	/**
	 * @brief Set the TestClass::charArray
	 * @param newcharArray the charArray to set
	 */
	public void setCharArray(final char[] newcharArray) {
		if (newcharArray == null) {
			throw new IllegalArgumentException("charArray cannot be null");
		}
		this.charArray = newcharArray;
	}

	/**
	 * @brief Get the TestClass::byteArray
	 * @return the byteArray
	 */
	public byte[] getByteArray() {
		return byteArray;
	}

	/**
	 * @brief Set the TestClass::byteArray
	 * @param newbyteArray the byteArray to set
	 */
	public void setByteArray(final byte[] newbyteArray) {
		if (newbyteArray == null) {
			throw new IllegalArgumentException("byteArray cannot be null");
		}
		this.byteArray = newbyteArray;
	}

	/**
	 * @brief Get the TestClass::booleanArray
	 * @return the booleanArray
	 */
	public boolean[] getBooleanArray() {
		return booleanArray;
	}

	/**
	 * @brief Set the TestClass::booleanArray
	 * @param newbooleanArray the booleanArray to set
	 */
	public void setBooleanArray(final boolean[] newbooleanArray) {
		if (newbooleanArray == null) {
			throw new IllegalArgumentException("booleanArray cannot be null");
		}
		this.booleanArray = newbooleanArray;
	}

	/**
	 * @brief Get the TestClass::shortArray
	 * @return the shortArray
	 */
	public short[] getShortArray() {
		return shortArray;
	}

	/**
	 * @brief Set the TestClass::shortArray
	 * @param newshortArray the shortArray to set
	 */
	public void setShortArray(final short[] newshortArray) {
		if (newshortArray == null) {
			throw new IllegalArgumentException("shortArray cannot be null");
		}
		this.shortArray = newshortArray;
	}

	/**
	 * @brief Get the TestClass::floatArray
	 * @return the floatArray
	 */
	public float[] getFloatArray() {
		return floatArray;
	}

	/**
	 * @brief Set the TestClass::floatArray
	 * @param newfloatArray the floatArray to set
	 */
	public void setFloatArray(final float[] newfloatArray) {
		if (newfloatArray == null) {
			throw new IllegalArgumentException("floatArray cannot be null");
		}
		this.floatArray = newfloatArray;
	}

	/**
	 * @brief Get the TestClass::intMatrix
	 * @return the intMatrix
	 */
	public int[][] getIntMatrix() {
		return intMatrix;
	}

	/**
	 * @brief Set the TestClass::intMatrix
	 * @param newintMatrix the intMatrix to set
	 */
	public void setIntMatrix(final int[][] newintMatrix) {
		if (newintMatrix == null) {
			throw new IllegalArgumentException("intMatrix cannot be null");
		}
		this.intMatrix = newintMatrix;
	}

	/**
	 * @brief Get the TestClass::doubleMatrix
	 * @return the doubleMatrix
	 */
	public double[][] getDoubleMatrix() {
		return doubleMatrix;
	}

	/**
	 * @brief Set the TestClass::doubleMatrix
	 * @param newdoubleMatrix the doubleMatrix to set
	 */
	public void setDoubleMatrix(final double[][] newdoubleMatrix) {
		if (newdoubleMatrix == null) {
			throw new IllegalArgumentException("doubleMatrix cannot be null");
		}
		this.doubleMatrix = newdoubleMatrix;
	}

	/**
	 * @brief Get the TestClass::longMatrix
	 * @return the longMatrix
	 */
	public long[][] getLongMatrix() {
		return longMatrix;
	}

	/**
	 * @brief Set the TestClass::longMatrix
	 * @param newlongMatrix the longMatrix to set
	 */
	public void setLongMatrix(final long[][] newlongMatrix) {
		if (newlongMatrix == null) {
			throw new IllegalArgumentException("longMatrix cannot be null");
		}
		this.longMatrix = newlongMatrix;
	}

	/**
	 * @brief Get the TestClass::charMatrix
	 * @return the charMatrix
	 */
	public char[][] getCharMatrix() {
		return charMatrix;
	}

	/**
	 * @brief Set the TestClass::charMatrix
	 * @param newcharMatrix the charMatrix to set
	 */
	public void setCharMatrix(final char[][] newcharMatrix) {
		if (newcharMatrix == null) {
			throw new IllegalArgumentException("charMatrix cannot be null");
		}
		this.charMatrix = newcharMatrix;
	}

	/**
	 * @brief Get the TestClass::byteMatrix
	 * @return the byteMatrix
	 */
	public byte[][] getByteMatrix() {
		return byteMatrix;
	}

	/**
	 * @brief Set the TestClass::byteMatrix
	 * @param newbyteMatrix the byteMatrix to set
	 */
	public void setByteMatrix(final byte[][] newbyteMatrix) {
		if (newbyteMatrix == null) {
			throw new IllegalArgumentException("byteMatrix cannot be null");
		}
		this.byteMatrix = newbyteMatrix;
	}

	/**
	 * @brief Get the TestClass::booleanMatrix
	 * @return the booleanMatrix
	 */
	public boolean[][] getBooleanMatrix() {
		return booleanMatrix;
	}

	/**
	 * @brief Set the TestClass::booleanMatrix
	 * @param newbooleanMatrix the booleanMatrix to set
	 */
	public void setBooleanMatrix(final boolean[][] newbooleanMatrix) {
		if (newbooleanMatrix == null) {
			throw new IllegalArgumentException("booleanMatrix cannot be null");
		}
		this.booleanMatrix = newbooleanMatrix;
	}

	/**
	 * @brief Get the TestClass::shortMatrix
	 * @return the shortMatrix
	 */
	public short[][] getShortMatrix() {
		return shortMatrix;
	}

	/**
	 * @brief Set the TestClass::shortMatrix
	 * @param newshortMatrix the shortMatrix to set
	 */
	public void setShortMatrix(final short[][] newshortMatrix) {
		if (newshortMatrix == null) {
			throw new IllegalArgumentException("shortMatrix cannot be null");
		}
		this.shortMatrix = newshortMatrix;
	}

	/**
	 * @brief Get the TestClass::floatMatrix
	 * @return the floatMatrix
	 */
	public float[][] getFloatMatrix() {
		return floatMatrix;
	}

	/**
	 * @brief Set the TestClass::floatMatrix
	 * @param newfloatMatrix the floatMatrix to set
	 */
	public void setFloatMatrix(final float[][] newfloatMatrix) {
		if (newfloatMatrix == null) {
			throw new IllegalArgumentException("floatMatrix cannot be null");
		}
		this.floatMatrix = newfloatMatrix;
	}

	/**
	 * @brief Get the TestClass::langIntArray
	 * @return the langIntArray
	 */
	public Integer[] getLangIntArray() {
		return langIntArray;
	}

	/**
	 * @brief Set the TestClass::langIntArray
	 * @param newlangIntArray the langIntArray to set
	 */
	public void setLangIntArray(final Integer[] newlangIntArray) {
		if (newlangIntArray == null) {
			throw new IllegalArgumentException("langIntArray cannot be null");
		}
		this.langIntArray = newlangIntArray;
	}

	/**
	 * @brief Get the TestClass::langDoubleArray
	 * @return the langDoubleArray
	 */
	public Double[] getLangDoubleArray() {
		return langDoubleArray;
	}

	/**
	 * @brief Set the TestClass::langDoubleArray
	 * @param newlangDoubleArray the langDoubleArray to set
	 */
	public void setLangDoubleArray(final Double[] newlangDoubleArray) {
		if (newlangDoubleArray == null) {
			throw new IllegalArgumentException("langDoubleArray cannot be null");
		}
		this.langDoubleArray = newlangDoubleArray;
	}

	/**
	 * @brief Get the TestClass::langLongArray
	 * @return the langLongArray
	 */
	public Long[] getLangLongArray() {
		return langLongArray;
	}

	/**
	 * @brief Set the TestClass::langLongArray
	 * @param newlangLongArray the langLongArray to set
	 */
	public void setLangLongArray(final Long[] newlangLongArray) {
		if (newlangLongArray == null) {
			throw new IllegalArgumentException("langLongArray cannot be null");
		}
		this.langLongArray = newlangLongArray;
	}

	/**
	 * @brief Get the TestClass::langCharArray
	 * @return the langCharArray
	 */
	public Character[] getLangCharArray() {
		return langCharArray;
	}

	/**
	 * @brief Set the TestClass::langCharArray
	 * @param newlangCharArray the langCharArray to set
	 */
	public void setLangCharArray(final Character[] newlangCharArray) {
		if (newlangCharArray == null) {
			throw new IllegalArgumentException("langCharArray cannot be null");
		}
		this.langCharArray = newlangCharArray;
	}

	/**
	 * @brief Get the TestClass::langByteArray
	 * @return the langByteArray
	 */
	public Byte[] getLangByteArray() {
		return langByteArray;
	}

	/**
	 * @brief Set the TestClass::langByteArray
	 * @param newlangByteArray the langByteArray to set
	 */
	public void setLangByteArray(final Byte[] newlangByteArray) {
		if (newlangByteArray == null) {
			throw new IllegalArgumentException("langByteArray cannot be null");
		}
		this.langByteArray = newlangByteArray;
	}

	/**
	 * @brief Get the TestClass::langBooleanArray
	 * @return the langBooleanArray
	 */
	public Boolean[] getLangBooleanArray() {
		return langBooleanArray;
	}

	/**
	 * @brief Set the TestClass::langBooleanArray
	 * @param newlangBooleanArray the langBooleanArray to set
	 */
	public void setLangBooleanArray(final Boolean[] newlangBooleanArray) {
		if (newlangBooleanArray == null) {
			throw new IllegalArgumentException("langBooleanArray cannot be null");
		}
		this.langBooleanArray = newlangBooleanArray;
	}

	/**
	 * @brief Get the TestClass::langShortArray
	 * @return the langShortArray
	 */
	public Short[] getLangShortArray() {
		return langShortArray;
	}

	/**
	 * @brief Set the TestClass::langShortArray
	 * @param newlangShortArray the langShortArray to set
	 */
	public void setLangShortArray(final Short[] newlangShortArray) {
		if (newlangShortArray == null) {
			throw new IllegalArgumentException("langShortArray cannot be null");
		}
		this.langShortArray = newlangShortArray;
	}

	/**
	 * @brief Get the TestClass::langFloatArray
	 * @return the langFloatArray
	 */
	public Float[] getLangFloatArray() {
		return langFloatArray;
	}

	/**
	 * @brief Set the TestClass::langFloatArray
	 * @param newlangFloatArray the langFloatArray to set
	 */
	public void setLangFloatArray(final Float[] newlangFloatArray) {
		if (newlangFloatArray == null) {
			throw new IllegalArgumentException("langFloatArray cannot be null");
		}
		this.langFloatArray = newlangFloatArray;
	}

	/**
	 * @brief Get the TestClass::langIntMatrix
	 * @return the langIntMatrix
	 */
	public Integer[][] getLangIntMatrix() {
		return langIntMatrix;
	}

	/**
	 * @brief Set the TestClass::langIntMatrix
	 * @param newlangIntMatrix the langIntMatrix to set
	 */
	public void setLangIntMatrix(final Integer[][] newlangIntMatrix) {
		if (newlangIntMatrix == null) {
			throw new IllegalArgumentException("langIntMatrix cannot be null");
		}
		this.langIntMatrix = newlangIntMatrix;
	}

	/**
	 * @brief Get the TestClass::langDoubleMatrix
	 * @return the langDoubleMatrix
	 */
	public Double[][] getLangDoubleMatrix() {
		return langDoubleMatrix;
	}

	/**
	 * @brief Set the TestClass::langDoubleMatrix
	 * @param newlangDoubleMatrix the langDoubleMatrix to set
	 */
	public void setLangDoubleMatrix(final Double[][] newlangDoubleMatrix) {
		if (newlangDoubleMatrix == null) {
			throw new IllegalArgumentException("langDoubleMatrix cannot be null");
		}
		this.langDoubleMatrix = newlangDoubleMatrix;
	}

	/**
	 * @brief Get the TestClass::langLongMatrix
	 * @return the langLongMatrix
	 */
	public Long[][] getLangLongMatrix() {
		return langLongMatrix;
	}

	/**
	 * @brief Set the TestClass::langLongMatrix
	 * @param newlangLongMatrix the langLongMatrix to set
	 */
	public void setLangLongMatrix(final Long[][] newlangLongMatrix) {
		if (newlangLongMatrix == null) {
			throw new IllegalArgumentException("langLongMatrix cannot be null");
		}
		this.langLongMatrix = newlangLongMatrix;
	}

	/**
	 * @brief Get the TestClass::langCharMatrix
	 * @return the langCharMatrix
	 */
	public Character[][] getLangCharMatrix() {
		return langCharMatrix;
	}

	/**
	 * @brief Set the TestClass::langCharMatrix
	 * @param newlangCharMatrix the langCharMatrix to set
	 */
	public void setLangCharMatrix(final Character[][] newlangCharMatrix) {
		if (newlangCharMatrix == null) {
			throw new IllegalArgumentException("langCharMatrix cannot be null");
		}
		this.langCharMatrix = newlangCharMatrix;
	}

	/**
	 * @brief Get the TestClass::langByteMatrix
	 * @return the langByteMatrix
	 */
	public Byte[][] getLangByteMatrix() {
		return langByteMatrix;
	}

	/**
	 * @brief Set the TestClass::langByteMatrix
	 * @param newlangByteMatrix the langByteMatrix to set
	 */
	public void setLangByteMatrix(final Byte[][] newlangByteMatrix) {
		if (newlangByteMatrix == null) {
			throw new IllegalArgumentException("langByteMatrix cannot be null");
		}
		this.langByteMatrix = newlangByteMatrix;
	}

	/**
	 * @brief Get the TestClass::langBooleanMatrix
	 * @return the langBooleanMatrix
	 */
	public Boolean[][] getLangBooleanMatrix() {
		return langBooleanMatrix;
	}

	/**
	 * @brief Set the TestClass::langBooleanMatrix
	 * @param newlangBooleanMatrix the langBooleanMatrix to set
	 */
	public void setLangBooleanMatrix(final Boolean[][] newlangBooleanMatrix) {
		if (newlangBooleanMatrix == null) {
			throw new IllegalArgumentException("langBooleanMatrix cannot be null");
		}
		this.langBooleanMatrix = newlangBooleanMatrix;
	}

	/**
	 * @brief Get the TestClass::langShortMatrix
	 * @return the langShortMatrix
	 */
	public Short[][] getLangShortMatrix() {
		return langShortMatrix;
	}

	/**
	 * @brief Set the TestClass::langShortMatrix
	 * @param newlangShortMatrix the langShortMatrix to set
	 */
	public void setLangShortMatrix(final Short[][] newlangShortMatrix) {
		if (newlangShortMatrix == null) {
			throw new IllegalArgumentException("langShortMatrix cannot be null");
		}
		this.langShortMatrix = newlangShortMatrix;
	}

	/**
	 * @brief Get the TestClass::langFloatMatrix
	 * @return the langFloatMatrix
	 */
	public Float[][] getLangFloatMatrix() {
		return langFloatMatrix;
	}

	/**
	 * @brief Set the TestClass::langFloatMatrix
	 * @param newlangFloatMatrix the langFloatMatrix to set
	 */
	public void setLangFloatMatrix(final Float[][] newlangFloatMatrix) {
		if (newlangFloatMatrix == null) {
			throw new IllegalArgumentException("langFloatMatrix cannot be null");
		}
		this.langFloatMatrix = newlangFloatMatrix;
	}

	/**
	 * @brief Get the TestClass::javaTreeSet
	 * @return the javaTreeSet
	 */
	public TreeSet<Integer> getJavaTreeSet() {
		return javaTreeSet;
	}

	/**
	 * @brief Set the TestClass::javaTreeSet
	 * @param newjavaTreeSet the javaTreeSet to set
	 */
	public void setJavaTreeSet(final TreeSet<Integer> newjavaTreeSet) {
		if (newjavaTreeSet == null) {
			throw new IllegalArgumentException("javaTreeSet cannot be null");
		}
		this.javaTreeSet = newjavaTreeSet;
	}

	/**
	 * @brief Get the TestClass::javaHashSet
	 * @return the javaHashSet
	 */
	public HashSet<Double> getJavaHashSet() {
		return javaHashSet;
	}

	/**
	 * @brief Set the TestClass::javaHashSet
	 * @param newjavaHashSet the javaHashSet to set
	 */
	public void setJavaHashSet(final HashSet<Double> newjavaHashSet) {
		if (newjavaHashSet == null) {
			throw new IllegalArgumentException("javaHashSet cannot be null");
		}
		this.javaHashSet = newjavaHashSet;
	}

	/**
	 * @brief Get the TestClass::javaEmptyHashSet
	 * @return the javaEmptyHashSet
	 */
	public ArrayList<Long> getJavaEmptyHashSet() {
		return javaEmptyHashSet;
	}

	/**
	 * @brief Set the TestClass::javaEmptyHashSet
	 * @param newjavaEmptyHashSet the javaEmptyHashSet to set
	 */
	public void setJavaEmptyHashSet(final ArrayList<Long> newjavaEmptyHashSet) {
		if (newjavaEmptyHashSet == null) {
			throw new IllegalArgumentException("javaEmptyHashSet cannot be null");
		}
		this.javaEmptyHashSet = newjavaEmptyHashSet;
	}

	/**
	 * @brief Get the TestClass::javaLinkedList
	 * @return the javaLinkedList
	 */
	public LinkedList<Short> getJavaLinkedList() {
		return javaLinkedList;
	}

	/**
	 * @brief Set the TestClass::javaLinkedList
	 * @param newjavaLinkedList the javaLinkedList to set
	 */
	public void setJavaLinkedList(final LinkedList<Short> newjavaLinkedList) {
		if (newjavaLinkedList == null) {
			throw new IllegalArgumentException("javaLinkedList cannot be null");
		}
		this.javaLinkedList = newjavaLinkedList;
	}

	/**
	 * @brief Get the TestClass::javaHashMap
	 * @return the javaHashMap
	 */
	public HashMap<Short, Character> getJavaHashMap() {
		return javaHashMap;
	}

	/**
	 * @brief Set the TestClass::javaHashMap
	 * @param newjavaHashMap the javaHashMap to set
	 */
	public void setJavaHashMap(final HashMap<Short, Character> newjavaHashMap) {
		if (newjavaHashMap == null) {
			throw new IllegalArgumentException("javaHashMap cannot be null");
		}
		this.javaHashMap = newjavaHashMap;
	}

	/**
	 * @brief Get the TestClass::tableOfArrays
	 * @return the tableOfArrays
	 */
	public HashMap<String, Integer[]> getTableOfArrays() {
		return tableOfArrays;
	}

	/**
	 * @brief Set the TestClass::tableOfArrays
	 * @param newtableOfArrays the tableOfArrays to set
	 */
	public void setTableOfArrays(final HashMap<String, Integer[]> newtableOfArrays) {
		if (newtableOfArrays == null) {
			throw new IllegalArgumentException("tableOfArrays cannot be null");
		}
		this.tableOfArrays = newtableOfArrays;
	}

	/**
	 * @brief Get the TestClass::mapOfmaps
	 * @return the mapOfmaps
	 */
	public HashMap<String, HashMap<String, Boolean>> getMapOfmaps() {
		return mapOfmaps;
	}

	/**
	 * @brief Set the TestClass::mapOfmaps
	 * @param newmapOfmaps the mapOfmaps to set
	 */
	public void setMapOfmaps(final HashMap<String, HashMap<String, Boolean>> newmapOfmaps) {
		if (newmapOfmaps == null) {
			throw new IllegalArgumentException("mapOfmaps cannot be null");
		}
		this.mapOfmaps = newmapOfmaps;
	}

	/**
	 * @brief Get the TestClass::userType
	 * @return the userType
	 */
	public TestType getUserType() {
		return userType;
	}

	/**
	 * @brief Set the TestClass::userType
	 * @param newuserType the userType to set
	 */
	public void setUserType(final TestType newuserType) {
		if (newuserType == null) {
			throw new IllegalArgumentException("userType cannot be null");
		}
		this.userType = newuserType;
	}

	/**
	 * @brief Get the TestClass::userArray
	 * @return the userArray
	 */
	public TestType[] getUserArray() {
		return userArray;
	}

	/**
	 * @brief Set the TestClass::userArray
	 * @param newuserArray the userArray to set
	 */
	public void setUserArray(final TestType[] newuserArray) {
		if (newuserArray == null) {
			throw new IllegalArgumentException("userArray cannot be null");
		}
		this.userArray = newuserArray;
	}

	/**
	 * @brief Get the TestClass::userMatrix
	 * @return the userMatrix
	 */
	public TestType[][] getUserMatrix() {
		return userMatrix;
	}

	/**
	 * @brief Set the TestClass::userMatrix
	 * @param newuserMatrix the userMatrix to set
	 */
	public void setUserMatrix(final TestType[][] newuserMatrix) {
		if (newuserMatrix == null) {
			throw new IllegalArgumentException("userMatrix cannot be null");
		}
		this.userMatrix = newuserMatrix;
	}

	/**
	 * @brief Get the TestClass::userHashSet
	 * @return the userHashSet
	 */
	public HashSet<TestType> getUserHashSet() {
		return userHashSet;
	}

	/**
	 * @brief Set the TestClass::userHashSet
	 * @param newuserHashSet the userHashSet to set
	 */
	public void setUserHashSet(final HashSet<TestType> newuserHashSet) {
		if (newuserHashSet == null) {
			throw new IllegalArgumentException("userHashSet cannot be null");
		}
		this.userHashSet = newuserHashSet;
	}

	/**
	 * @brief Get the TestClass::userList
	 * @return the userList
	 */
	public ArrayList<TestType> getUserList() {
		return userList;
	}

	/**
	 * @brief Set the TestClass::userList
	 * @param newuserList the userList to set
	 */
	public void setUserList(final ArrayList<TestType> newuserList) {
		if (newuserList == null) {
			throw new IllegalArgumentException("userList cannot be null");
		}
		this.userList = newuserList;
	}

	/**
	 * @brief Get the TestClass::userEmptyHashSet
	 * @return the userEmptyHashSet
	 */
	public HashSet<TestType> getUserEmptyHashSet() {
		return userEmptyHashSet;
	}

	/**
	 * @brief Set the TestClass::userEmptyHashSet
	 * @param newuserEmptyHashSet the userEmptyHashSet to set
	 */
	public void setUserEmptyHashSet(final HashSet<TestType> newuserEmptyHashSet) {
		if (newuserEmptyHashSet == null) {
			throw new IllegalArgumentException("userEmptyHashSet cannot be null");
		}
		this.userEmptyHashSet = newuserEmptyHashSet;
	}

	/**
	 * @brief Get the TestClass::userHashMap
	 * @return the userHashMap
	 */
	public HashMap<TestType, TestType> getUserHashMap() {
		return userHashMap;
	}

	/**
	 * @brief Set the TestClass::userHashMap
	 * @param newuserHashMap the userHashMap to set
	 */
	public void setUserHashMap(final HashMap<TestType, TestType> newuserHashMap) {
		if (newuserHashMap == null) {
			throw new IllegalArgumentException("userHashMap cannot be null");
		}
		this.userHashMap = newuserHashMap;
	}

	/**
	 * @brief Get the TestClass::generic
	 * @return the generic
	 */
	public Object getGeneric() {
		return generic;
	}

	/**
	 * @brief Set the TestClass::generic
	 * @param newgeneric the generic to set
	 */
	public void setGeneric(final Object newgeneric) {
		if (newgeneric == null) {
			throw new IllegalArgumentException("generic cannot be null");
		}
		this.generic = newgeneric;
	}

	/**
	 * @brief Get the TestClass::genericMap
	 * @return the genericMap
	 */
	public HashMap<Object, Object> getGenericMap() {
		return genericMap;
	}

	/**
	 * @brief Set the TestClass::genericMap
	 * @param newgenericMap the genericMap to set
	 */
	public void setGenericMap(final HashMap<Object, Object> newgenericMap) {
		if (newgenericMap == null) {
			throw new IllegalArgumentException("genericMap cannot be null");
		}
		this.genericMap = newgenericMap;
	}

	/**
	 * @brief Get the TestClass::self
	 * @return the self
	 */
	public TestClass getSelf() {
		return self;
	}

	/**
	 * @brief Set the TestClass::self
	 * @param newself the self to set
	 */
	public void setSelf(final TestClass newself) {
		if (newself == null) {
			throw new IllegalArgumentException("self cannot be null");
		}
		this.self = newself;
	}

	/**
	 * @brief Get the TestClass::userMapWithJavaKey
	 * @return the userMapWithJavaKey
	 */
	public HashMap<Integer, TestType> getUserMapWithJavaKey() {
		return userMapWithJavaKey;
	}

	/**
	 * @brief Set the TestClass::userMapWithJavaKey
	 * @param newuserMapWithJavaKey the userMapWithJavaKey to set
	 */
	public void setUserMapWithJavaKey(final HashMap<Integer, TestType> newuserMapWithJavaKey) {
		if (newuserMapWithJavaKey == null) {
			throw new IllegalArgumentException("userMapWithJavaKey cannot be null");
		}
		this.userMapWithJavaKey = newuserMapWithJavaKey;
	}

	/**
	 * @brief Get the TestClass::userMapWithJavaValue
	 * @return the userMapWithJavaValue
	 */
	public HashMap<TestType, Long> getUserMapWithJavaValue() {
		return userMapWithJavaValue;
	}

	/**
	 * @brief Set the TestClass::userMapWithJavaValue
	 * @param newuserMapWithJavaValue the userMapWithJavaValue to set
	 */
	public void setUserMapWithJavaValue(final HashMap<TestType, Long> newuserMapWithJavaValue) {
		if (newuserMapWithJavaValue == null) {
			throw new IllegalArgumentException("userMapWithJavaValue cannot be null");
		}
		this.userMapWithJavaValue = newuserMapWithJavaValue;
	}

	/**
	 * @brief Get the TestClass::userTypeNullField
	 * @return the userTypeNullField
	 */
	public TestType getUserTypeNullField() {
		return userTypeNullField;
	}

	/**
	 * @brief Set the TestClass::userTypeNullField
	 * @param newuserTypeNullField the userTypeNullField to set
	 */
	public void setUserTypeNullField(final TestType newuserTypeNullField) {
		this.userTypeNullField = newuserTypeNullField;
	}

	/**
	 * @brief Get the TestClass::javaNullField
	 * @return the javaNullField
	 */
	public Integer getJavaNullField() {
		return javaNullField;
	}

	/**
	 * @brief Set the TestClass::javaNullField
	 * @param newjavaNullField the javaNullField to set
	 */
	public void setJavaNullField(final Integer newjavaNullField) {
		this.javaNullField = newjavaNullField;
	}

	/**
	 * @brief Get the TestClass::userMapWithNullValue
	 * @return the userMapWithNullValue
	 */
	public HashMap<TestType, TestType> getUserMapWithNullValue() {
		return userMapWithNullValue;
	}

	/**
	 * @brief Set the TestClass::userMapWithNullValue
	 * @param newuserMapWithNullValue the userMapWithNullValue to set
	 */
	public void setUserMapWithNullValue(final HashMap<TestType, TestType> newuserMapWithNullValue) {
		if (newuserMapWithNullValue == null) {
			throw new IllegalArgumentException("userMapWithNullValue cannot be null");
		}
		this.userMapWithNullValue = newuserMapWithNullValue;
	}

	/**
	 * @brief Get the TestClass::userMapWithNullKey
	 * @return the userMapWithNullKey
	 */
	public HashMap<TestType, TestType> getUserMapWithNullKey() {
		return userMapWithNullKey;
	}

	/**
	 * @brief Set the TestClass::userMapWithNullKey
	 * @param newuserMapWithNullKey the userMapWithNullKey to set
	 */
	public void setUserMapWithNullKey(final HashMap<TestType, TestType> newuserMapWithNullKey) {
		if (newuserMapWithNullKey == null) {
			throw new IllegalArgumentException("userMapWithNullKey cannot be null");
		}
		this.userMapWithNullKey = newuserMapWithNullKey;
	}

	/**
	 * @brief Get the TestClass::javaMapWithNullKey
	 * @return the javaMapWithNullKey
	 */
	public HashMap<String, Integer> getJavaMapWithNullKey() {
		return javaMapWithNullKey;
	}

	/**
	 * @brief Set the TestClass::javaMapWithNullKey
	 * @param newjavaMapWithNullKey the javaMapWithNullKey to set
	 */
	public void setJavaMapWithNullKey(final HashMap<String, Integer> newjavaMapWithNullKey) {
		if (newjavaMapWithNullKey == null) {
			throw new IllegalArgumentException("javaMapWithNullKey cannot be null");
		}
		this.javaMapWithNullKey = newjavaMapWithNullKey;
	}

	/**
	 * @brief Get the TestClass::javaMapWithNullValue
	 * @return the javaMapWithNullValue
	 */
	public HashMap<Double, Integer> getJavaMapWithNullValue() {
		return javaMapWithNullValue;
	}

	/**
	 * @brief Set the TestClass::javaMapWithNullValue
	 * @param newjavaMapWithNullValue the javaMapWithNullValue to set
	 */
	public void setJavaMapWithNullValue(final HashMap<Double, Integer> newjavaMapWithNullValue) {
		if (newjavaMapWithNullValue == null) {
			throw new IllegalArgumentException("javaMapWithNullValue cannot be null");
		}
		this.javaMapWithNullValue = newjavaMapWithNullValue;
	}

	/**
	 * @brief Get the TestClass::genericMapWithNullKey
	 * @return the genericMapWithNullKey
	 */
	public HashMap<Object, Object> getGenericMapWithNullKey() {
		return genericMapWithNullKey;
	}

	/**
	 * @brief Set the TestClass::genericMapWithNullKey
	 * @param newgenericMapWithNullKey the genericMapWithNullKey to set
	 */
	public void setGenericMapWithNullKey(final HashMap<Object, Object> newgenericMapWithNullKey) {
		if (newgenericMapWithNullKey == null) {
			throw new IllegalArgumentException("genericMapWithNullKey cannot be null");
		}
		this.genericMapWithNullKey = newgenericMapWithNullKey;
	}

	/**
	 * @brief Get the TestClass::genericWithNullValue
	 * @return the genericWithNullValue
	 */
	public HashMap<Object, Object> getGenericWithNullValue() {
		return genericWithNullValue;
	}

	/**
	 * @brief Set the TestClass::genericWithNullValue
	 * @param newgenericWithNullValue the genericWithNullValue to set
	 */
	public void setGenericWithNullValue(final HashMap<Object, Object> newgenericWithNullValue) {
		if (newgenericWithNullValue == null) {
			throw new IllegalArgumentException("genericWithNullValue cannot be null");
		}
		this.genericWithNullValue = newgenericWithNullValue;
	}

	/**
	 * @brief Get the TestClass::userArrayWithNull
	 * @return the userArrayWithNull
	 */
	/*public TestType[] getUserArrayWithNull() {
		return userArrayWithNull;
	}*/

	/**
	 * @brief Set the TestClass::userArrayWithNull
	 * @param newuserArrayWithNull the userArrayWithNull to set
	 */
	/*public void setUserArrayWithNull(final TestType[] newuserArrayWithNull) {
		if (newuserArrayWithNull == null) {
			throw new IllegalArgumentException("userArrayWithNull cannot be null");
		}
		this.userArrayWithNull = newuserArrayWithNull;
	}*/

	/**
	 * @brief Get the TestClass::userMatrixWithNull
	 * @return the userMatrixWithNull
	 */
	/*public TestType[][] getUserMatrixWithNull() {
		return userMatrixWithNull;
	}*/

	/**
	 * @brief Set the TestClass::userMatrixWithNull
	 * @param newuserMatrixWithNull the userMatrixWithNull to set
	 */
	/*public void setUserMatrixWithNull(final TestType[][] newuserMatrixWithNull) {
		if (newuserMatrixWithNull == null) {
			throw new IllegalArgumentException("userMatrixWithNull cannot be null");
		}
		this.userMatrixWithNull = newuserMatrixWithNull;
	}*/

	/**
	 * @brief Get the TestClass::javaArrayWithNull
	 * @return the javaArrayWithNull
	 */
	/*public Character[] getJavaArrayWithNull() {
		return javaArrayWithNull;
	}*/

	/**
	 * @brief Set the TestClass::javaArrayWithNull
	 * @param newjavaArrayWithNull the javaArrayWithNull to set
	 */
	/*public void setJavaArrayWithNull(final Character[] newjavaArrayWithNull) {
		if (newjavaArrayWithNull == null) {
			throw new IllegalArgumentException("javaArrayWithNull cannot be null");
		}
		this.javaArrayWithNull = newjavaArrayWithNull;
	}*/

	/**
	 * @brief Get the TestClass::javaMatrixWithNull
	 * @return the javaMatrixWithNull
	 */
	/*public Boolean[][] getJavaMatrixWithNull() {
		return javaMatrixWithNull;
	}*/

	/**
	 * @brief Set the TestClass::javaMatrixWithNull
	 * @param newjavaMatrixWithNull the javaMatrixWithNull to set
	 */
	/*public void setJavaMatrixWithNull(final Boolean[][] newjavaMatrixWithNull) {
		if (newjavaMatrixWithNull == null) {
			throw new IllegalArgumentException("javaMatrixWithNull cannot be null");
		}
		this.javaMatrixWithNull = newjavaMatrixWithNull;
	}*/

	/**
	 * @brief Get the TestClass::genericArray
	 * @return the genericArray
	 */
	public Object[] getGenericArray() {
		return genericArray;
	}

	/**
	 * @brief Set the TestClass::genericArray
	 * @param newgenericArray the genericArray to set
	 */
	public void setGenericArray(final Object[] newgenericArray) {
		if (newgenericArray == null) {
			throw new IllegalArgumentException("genericArray cannot be null");
		}
		this.genericArray = newgenericArray;
	}

	/**
	 * @brief Get the TestClass::genericMatrix
	 * @return the genericMatrix
	 */
	public Object[][] getGenericMatrix() {
		return genericMatrix;
	}

	/**
	 * @brief Set the TestClass::genericMatrix
	 * @param newgenericMatrix the genericMatrix to set
	 */
	public void setGenericMatrix(final Object[][] newgenericMatrix) {
		if (newgenericMatrix == null) {
			throw new IllegalArgumentException("genericMatrix cannot be null");
		}
		this.genericMatrix = newgenericMatrix;
	}

	/**
	 * @brief Get the TestClass::genericSet
	 * @return the genericSet
	 */
	public HashSet<Object> getGenericSet() {
		return genericSet;
	}

	/**
	 * @brief Set the TestClass::genericSet
	 * @param newgenericSet the genericSet to set
	 */
	public void setGenericSet(final HashSet<Object> newgenericSet) {
		if (newgenericSet == null) {
			throw new IllegalArgumentException("genericSet cannot be null");
		}
		this.genericSet = newgenericSet;
	}
}
