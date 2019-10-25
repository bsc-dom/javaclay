
package es.bsc.dataclay.logic.classmgr.bytecode.java.constants;

/**
 * This class contains names of methods as Stub's constants.
 */
public final class ByteCodeMethodsNames {

	/**
	 * Utility classes should have private constructor.
	 */
	private ByteCodeMethodsNames() {

	}

	/** Set Properties in StubInfo method. */
	public static final String SETPROPS_MTHD = "setProperties";

	/** Set Operations in StubInfo method. */
	public static final String SETOPS_MTHD = "setOperations";

	/** Set Implementations in StubInfo method. */
	public static final String SETIMPLS_MTHD = "setImplementations";

	/** Get Implementation info in StubInfo method. */
	public static final String GETIMPLINFO_MTHD = "getImplementationInfo";

	/** Set Interfaces in contract in StubInfo method. */
	public static final String SETCTRS_MTHD = "setContracts";

	/** Set All fields. */
	public static final String SET_ALL_FIELDS = "setAll";

	/** setObjectID in DataClayObject. */
	public static final String SETOBJID_MTHD = "setObjectID";

	/** Constructor method name. */
	public static final String INIT_MTHD = "<init>";

	/** Class Constructor method name. */
	public static final String CLINIT_MTHD = "<clinit>";

	/** Run method name. */
	public static final String RUN_MTHD = "run";

	/** Equals method name. */
	public static final String EQUALS_MTHD = "equals";

	/** Map get method. */
	public static final String GET_MTHD = "get";

	/** Map put method. */
	public static final String PUT_MTHD = "put";

	/** Collection add method. */
	public static final String ADD_MTHD = "add";

	/** Iterator previous method. */
	public static final String PREVIOUS_MTHD = "previous";

	/** ValueOF method. */
	public static final String VALUEOF_MTHD = "valueOf";

	/** Stub method to retrieve string representation of ObjectID. */
	public static final String GETOID_STR_MTHD = "getID";

	/** Stub getter of SessionID. */
	public static final String GETSESSID_MTHD = "getSessionID";

	/** Stub setter of SessionID. */
	public static final String SETSESSID_MTHD = "setSessionID";

	/** Stub getter of MetaClassID. */
	public static final String GETMCLASSID_MTHD = "getMetaClassID";

	/** Execute remote implementation method. */
	public static final String EXECREMOTE_MTHD = "executeRemoteImplementation";

	/** Run remote method. */
	public static final String RUNREMOTE = "runRemote";

	/** Get object from alias. */
	public static final String GETOBJ_BY_ALIAS = "getByAlias";
	
	/** Get object from alias for extension. */
	public static final String GETOBJ_BY_ALIAS_EXT = "getByAliasExt";
	
	/** Get objectID from alias. */
	public static final String GETOBJID_FROM_ALIAS = "getObjectIDfromAlias";

	/** Get object from alias. */
	public static final String DELETE_ALIAS = "deleteAlias";

	/** getDataSetIDFromObject. */
	public static final String GETDATSET_FROM_OBJECT = "getDataSetIDFromObject";

	/** Get objectID. */
	public static final String GETOBJID = "getObjectID";

	/** Stub make persistent method. */
	public static final String MKPERST_MTHD = "makePersistent";

	/** Stub make createTemporary method. */
	public static final String CREATETMP_MTHD = "createTemporary";

	/** Stub query by example method. */
	public static final String QBE_MTHD = "queryByExample";

	/** Stub copy to local method. */
	public static final String COPYTOLOCAL_MTHD = "copyToLocal";

	/** Stub remove temporary method. */
	public static final String REMOVETMP_MTHD = "removeTemporary";

	/** Stub new replica method. */
	public static final String NEWREP_MTHD = "newReplica";

	/** Stub move object method. */
	public static final String MOVOBJ_MTHD = "moveObject";

	/** Stub set object read only method. */
	public static final String SETRO_MTHD = "setObjectReadOnly";

	/** Stub set object read write method. */
	public static final String SETRW_MTHD = "setObjectReadWrite";

	/** Stub get location method. */
	public static final String GETLOC_MTHD = "getLocation";

	/** Stub get all locations method. */
	public static final String GETALLLOC_MTHD = "getAllLocations";

	/** DataClayByteBuffer write int method. */
	public static final String WRITE_INT_MTHD = "writeInt";

	/** DataClayByteBuffer write variable-length int method. */
	public static final String WRITE_VLQINT_MTHD = "writeVLQInt";

	/** DataClayByteBuffer write boolean method. */
	public static final String WRITE_BOOLEAN_MTHD = "writeBoolean";

	/** DataClayByteBuffer write byte method. */
	public static final String WRITE_BYTE_MTHD = "writeByte";

	/** DataClayByteBuffer write char method. */
	public static final String WRITE_CHAR_MTHD = "writeChar";

	/** DataClayByteBuffer write double method. */
	public static final String WRITE_DOUBLE_MTHD = "writeDouble";

	/** DataClayByteBuffer write float method. */
	public static final String WRITE_FLOAT_MTHD = "writeFloat";

	/** DataClayByteBuffer write long method. */
	public static final String WRITE_LONG_MTHD = "writeLong";

	/** DataClayByteBuffer write short method. */
	public static final String WRITE_SHORT_MTHD = "writeShort";

	/** DataClayByteBuffer write string method. */
	public static final String WRITE_STRING_MTHD = "writeString";

	/** DataClayByteBuffer write bytes method. */
	public static final String WRITE_BYTES_MTHD = "writeBytes";

	/** DataClayByteBuffer read int method. */
	public static final String READ_INT_MTHD = "readInt";

	/** DataClayByteBuffer read variable-length quantity int method. */
	public static final String READ_VLQINT_MTHD = "readVLQInt";

	/** DataClayByteBuffer read boolean method. */
	public static final String READ_BOOLEAN_MTHD = "readBoolean";

	/** DataClayByteBuffer read byte method. */
	public static final String READ_BYTE_MTHD = "readByte";

	/** DataClayByteBuffer read char method. */
	public static final String READ_CHAR_MTHD = "readChar";

	/** DataClayByteBuffer read double method. */
	public static final String READ_DOUBLE_MTHD = "readDouble";

	/** DataClayByteBuffer read float method. */
	public static final String READ_FLOAT_MTHD = "readFloat";

	/** DataClayByteBuffer read long method. */
	public static final String READ_LONG_MTHD = "readLong";

	/** DataClayByteBuffer read short method. */
	public static final String READ_SHORT_MTHD = "readShort";

	/** DataClayByteBuffer read string method. */
	public static final String READ_STRING_MTHD = "readString";

	/** DataClayByteBuffer read bytes method. */
	public static final String READ_BYTES_MTHD = "readBytes";

	/** DataClaySerializable serialize method. */
	public static final String SERIALIZE_MTHD = "serialize";

	/** DataClaySerializable serializeAssociation method. */
	public static final String SERIALIZE_ASSOCIATION_MTHD = "serializeAssociation";

	/** DataClaySerializable serialize method. */
	public static final String SERIALIZE_EMBEDDED_MTHD = "serializeEmbedded";

	/** DataClaySerializable deserialize method. */
	public static final String DESERIALIZE_MTHD = "deserialize";

	/** DataClaySerializable deserializeAssociation method. */
	public static final String DESERIALIZE_ASSOCIATION_MTHD = "deserializeAssociation";

	/** DataClaySerializable deserialize embedded method. */
	public static final String DESERIALIZE_EMBEDDED_MTHD = "deserializeEmbedded";

	/** BitSet set method name. */
	public static final String BITSET_SET_MTHD = "set";

	/** BitSet get method name. */
	public static final String BITSET_GET_MTHD = "get";

	/** BitSet valueOf method name. */
	public static final String BITSET_VALUEOF_MTHD = "valueOf";

	/** BitSet toByteArray method name. */
	public static final String BITSET_TOBYTE_MTHD = "toByteArray";

	/** Iterator next method. */
	public static final String IT_NEXT_MTHD = "next";

	/** Iterator has next method. */
	public static final String IT_HASNEXT_MTHD = "hasNext";

	/** Map entrySet method. */
	public static final String MAP_ENTRYSET_MTHD = "entrySet";

	/** Map entry getValue method. */
	public static final String ENTRY_GETVAL_MTHD = "getValue";

	/** Map entry getKey method. */
	public static final String ENTRY_GETKEY_MTHD = "getKey";

	/** Serialize generic java type. */
	public static final String SERIALIZE_GENERIC_JAVA = "serializeGenericJavaType";

	/** Deserialize generic java type. */
	public static final String DESERIALIZE_GENERIC_JAVA = "deserializeGenericJavaType";

	/** DataClayJavaWrapper getJavaObject method name. */
	public static final String GETJAVAOBJ_MTHD = "getJavaObject";

	/** Java Integer intValue method name. */
	public static final String INTVALUE_MTHD = "intValue";

	/** Java booleanValue method name. */
	public static final String BOOLEANVALUE_MTHD = "booleanValue";

	/** Java charValue method name. */
	public static final String CHARVALUE_MTHD = "charValue";

	/** Java byteValue method name. */
	public static final String BYTEVALUE_MTHD = "byteValue";

	/** Java floatValue method name. */
	public static final String FLOATVALUE_MTHD = "floatValue";

	/** Java longValue method name. */
	public static final String LONGVALUE_MTHD = "longValue";

	/** Java shortValue method name. */
	public static final String SHORTVALUE_MTHD = "shortValue";

	/** Java doubleValue method name. */
	public static final String DOUBLEVALUE_MTHD = "doubleValue";

	/** Map size method name. */
	public static final String SIZE_MTHD = "size";

	/** getArray method name. */
	public static final String GET_ARRAY = "getArray";

	/** Release method name. */
	public static final String RELEASE = "release";

	/** setIsRegistered method name. */
	public static final String SET_ISPERSISTENT = "setIsPersistent";

	/** setDataSetID method name. */
	public static final String SET_DATASETID = "setDataSetID";

	/** writerIndex method name. */
	public static final String WRITER_INDEX = "writerIndex";

	/** setWriterIndex method name. */
	public static final String SET_WRITER_INDEX = "setWriterIndex";

	/** newInstance method name. */
	public static final String NEW_INSTANCE = "newInstance";

	/** getDeclaredConstructor method name. */
	public static final String GET_DECLARED_CONSTRUCTOR = "getDeclaredConstructor";

	/** getDsLib method name. */
	public static final String GET_LIB = "getLib";

	/** getFirst method name. */
	public static final String GET_FIRST = "getFirst";

	/** getSecond method name. */
	public static final String GET_SECOND = "getSecond";

	/** initializeDataClayOps method name. */
	public static final String INITIALIZE_STATIC_OPS = "initializeDataClayOps";

	/** initializeDataClayImpls method name. */
	public static final String INITIALIZE_STATIC_IMPLS = "initializeDataClayImpls";

	/** initializeDataClayProps method name. */
	public static final String INITIALIZE_STATIC_PROPS = "initializeDataClayProps";

	/** initializeDataClayContracts method name. */
	public static final String INITIALIZE_STATIC_CONTRACTS = "initializeDataClayContracts";

	/** getOperations method name. */
	public static final String STUBINFO_GETOPERATIONS = "getOperations";

	/** getImplementations method name. */
	public static final String STUBINFO_GETIMPLS = "getImplementations";

	/** getProperties method name. */
	public static final String STUBINFO_GETPROPS = "getProperties";

	/** getContracts method name. */
	public static final String STUBINFO_GETCONTRACTS = "getContracts";

	/** setDsForObjectUpdate method name. */
	public static final String DCOBJ_SETDSFORUPDATE = "setDsForObjectUpdate";

	public static final String DCOBJ_GET_MASTER_LOCATION = "getMasterLocation";
}
