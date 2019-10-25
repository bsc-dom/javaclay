
package es.bsc.dataclay.logic.classmgr.bytecode.java.constants;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

/** Constants of methods for bytecode generation. */
public final class ByteCodeMethods {

	/** Bitset constructor. */
	public static final Method BITSET_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, new Type[] { Type.INT_TYPE });

	/** BitSet get. */
	public static final Method BITSET_GET = new Method(ByteCodeMethodsNames.BITSET_GET_MTHD, Type.BOOLEAN_TYPE,
			new Type[] { Type.INT_TYPE });

	/** BitSet set. */
	public static final Method BITSET_SET = new Method(ByteCodeMethodsNames.BITSET_SET_MTHD, Type.VOID_TYPE,
			new Type[] { Type.INT_TYPE });

	/** BitSet toByteArray. */
	public static final Method BITSET_TOBYTEARRAY = new Method(ByteCodeMethodsNames.BITSET_TOBYTE_MTHD, ByteCodeTypes.BYTE_ARRAY,
			ByteCodeTypes.NO_ARGS);

	/** Bitset value of method. */
	public static final Method BITSET_VALUEOF = new Method(ByteCodeMethodsNames.VALUEOF_MTHD, ByteCodeTypes.BITSET,
			new Type[] { ByteCodeTypes.BYTE_ARRAY });

	/** Constructor method with objID. */
	public static final Method DC_INIT_METHOD = new Method(ByteCodeMethodsNames.INIT_MTHD, Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.OBJID });

	/** Get lib. */
	public static final Method DCBOBJ_GET_LIB = new Method(ByteCodeMethodsNames.GET_LIB,
			ByteCodeTypes.DATACLAY_CLIENT_LIB, new Type[] {});

	/** Get sessionID. */
	public static final Method DCBOBJ_GET_SESSID = new Method(ByteCodeMethodsNames.GETSESSID_MTHD,
			ByteCodeTypes.SESSID, new Type[] {});

	/** DCByteBuffer read bytes method. */
	public static final Method DCBUFFER_READ_BYTES = new Method(ByteCodeMethodsNames.READ_BYTES_MTHD,
			ByteCodeTypes.BYTE_ARRAY,
			new Type[] { Type.INT_TYPE });

	/** DCByteBuffer read VLQ int method. */
	public static final Method DCBUFFER_READ_VLQ_INT = new Method(ByteCodeMethodsNames.READ_VLQINT_MTHD,
			Type.INT_TYPE, ByteCodeTypes.NO_ARGS);

	/** DCByteBuffer set writer index method. */
	public static final Method DCBUFFER_SET_WRITER_INDEX = new Method(ByteCodeMethodsNames.SET_WRITER_INDEX,
			Type.VOID_TYPE, new Type[] { Type.INT_TYPE });

	/** DCByteBuffer write bytes method. */
	public static final Method DCBUFFER_WRITE_BYTES = new Method(ByteCodeMethodsNames.WRITE_BYTES_MTHD,
			Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.BYTE_ARRAY });

	/** DCByteBuffer write VLQ int method. */
	public static final Method DCBUFFER_WRITE_VLQ_INT = new Method(ByteCodeMethodsNames.WRITE_VLQINT_MTHD,
			Type.VOID_TYPE,
			new Type[] { Type.INT_TYPE });

	/** DCByteBuffer writer index method. */
	public static final Method DCBUFFER_WRITER_INDEX = new Method(ByteCodeMethodsNames.WRITER_INDEX,
			Type.INT_TYPE,
			ByteCodeTypes.NO_ARGS);

	/** DataClayobject addLazyTask method. */
	public static final Method DCOBJ_ADD_LAZY_TASK = new Method("addLazyTask",
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.OBJID, ByteCodeTypes.IMPLID,
					ByteCodeTypes.MCLASSID });

	/** DataClayObject constructor with alias ASM method. */
	public static final Method DCOBJ_ALIAS_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.STRING });

	/** Empty Constructor ASM method. */
	public static final Method DCOBJ_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, ByteCodeTypes.NO_ARGS);

	/** DataClayobject debugEnd method. */
	public static final Method DCOBJ_DEBUG_END = new Method("debugEnd",
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.STRING });

	/** DataClayobject debug start method. */
	public static final Method DCOBJ_DEBUG_START = new Method("debugStart",
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.STRING });

	/** End method. */
	public static final Method DCOBJ_END_METHOD = new Method("endMethod",
			Type.VOID_TYPE, new Type[] {});

	/** Execute implementation ASM Method. */
	public static final Method DCOBJ_EXECUTE_IMPL = new Method(ByteCodeMethodsNames.EXECREMOTE_MTHD,
			ByteCodeTypes.OBJECT, new Type[] { ByteCodeTypes.STRING, ByteCodeTypes.STRING,
					ByteCodeTypes.OBJECT_ARRAY });

	/** Run remote ASM Method. */
	public static final Method DCOBJ_RUN_REMOTE = new Method(ByteCodeMethodsNames.RUNREMOTE,
			ByteCodeTypes.OBJECT, new Type[] { ByteCodeTypes.BackendID, ByteCodeTypes.STRING,
					ByteCodeTypes.OBJECT_ARRAY });

	/** Get master location ASM Method. */
	public static final Method DCOBJ_GET_MASTER_LOCATION = new Method(ByteCodeMethodsNames.DCOBJ_GET_MASTER_LOCATION,
			ByteCodeTypes.BackendID, new Type[] {});

	/** getByAlias ASM Method. */
	public static final Method DCOBJ_GET_BY_ALIAS = new Method(ByteCodeMethodsNames.GETOBJ_BY_ALIAS,
			ByteCodeTypes.DCOBJ, new Type[] { ByteCodeTypes.STRING, ByteCodeTypes.STRING });
	
	/** deleteAlias ASM Method. */
	public static final Method DCOBJ_DELETE_ALIAS = new Method(ByteCodeMethodsNames.DELETE_ALIAS,
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.STRING, ByteCodeTypes.STRING });

	/** DCObject getMetaClassID method. */
	public static final Method DCOBJ_GET_METACLASSID = new Method(ByteCodeMethodsNames.GETMCLASSID_MTHD,
			ByteCodeTypes.MCLASSID, ByteCodeTypes.NO_ARGS);

	/** Is loaded method. */
	public static final Method DCOBJ_IS_LOADED = new Method("isLoaded",
			Type.BOOLEAN_TYPE, new Type[] {});

	/** Is master storage location method. */
	public static final Method DCOBJ_IS_MASTER_LOCATION = new Method("isMasterLocation",
			Type.BOOLEAN_TYPE, new Type[] {});

	/** Is persistent method. */
	public static final Method DCOBJ_IS_PERSISTENT = new Method("isPersistent",
			Type.BOOLEAN_TYPE, new Type[] {});

	/** setAsDirty ASM Method. */
	public static final Method DCOBJ_SETASDIRTY = new Method("setAsDirty",
			Type.VOID_TYPE, ByteCodeTypes.NO_ARGS);

	/** Logger debug method. */
	public static final Method DEBUG_METHOD = new Method("debug",
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.OBJECT });

	/** Decrement and get. */
	public static final Method DECREMENT_AND_GET = new Method("decrementAndGet",
			Type.INT_TYPE, new Type[] {});

	/** Deserialize association method in DCObj. */
	public static final Method DESERIALIZE_ASSOC = new Method(ByteCodeMethodsNames.DESERIALIZE_ASSOCIATION_MTHD, ByteCodeTypes.DCOBJ,
			new Type[] { ByteCodeTypes.DC_BUF, ByteCodeTypes.MAP,
					ByteCodeTypes.DC_OBJECT_METADATA,
					ByteCodeTypes.MAP, ByteCodeTypes.DATACLAY_CLIENT_LIB });

	/** Deserialize java field method in DCObj. */
	public static final Method DESERIALIZE_JAVA_FIELD = new Method("deserializeJavaField", ByteCodeTypes.OBJECT,
			new Type[] { ByteCodeTypes.DC_WRAPPER, ByteCodeTypes.DC_BUF, ByteCodeTypes.MAP,
					ByteCodeTypes.DC_OBJECT_METADATA,
					ByteCodeTypes.MAP });

	/** Empty Constructor ASM method. */
	public static final Method EMPTY_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, ByteCodeTypes.NO_ARGS);

	/** Equals method. */
	public static final Method EQUALS = new Method(ByteCodeMethodsNames.EQUALS_MTHD, Type.BOOLEAN_TYPE,
			new Type[] { ByteCodeTypes.OBJECT });

	/** Serialized params or return getImmObjs. */
	public static final Method GET_IMM_OBJS = new Method("getImmObjs",
			ByteCodeTypes.MAP, new Type[] {});

	/** Serialized params or return getLangObjs. */
	public static final Method GET_LANG_OBJS = new Method("getLangObjs",
			ByteCodeTypes.MAP, new Type[] {});

	/** Serialized params or return getPersistentRefs. */
	public static final Method GET_PERS_REFS = new Method("getPersistentRefs",
			ByteCodeTypes.MAP, new Type[] {});

	/** Serialized params or return getVolatileObjs. */
	public static final Method GET_VOLATILE_OBJS = new Method("getVolatileObjs",
			ByteCodeTypes.MAP, new Type[] {});

	/** Hashcode method. */
	public static final Method HASHCODE = new Method("hashCode", Type.INT_TYPE,
			new Type[] {});

	/** Immutable param or return constructor. */
	public static final Method IMM_PARAM_RETURN_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.DC_WRAPPER });

	/** ImplId constructor. */
	public static final Method IMPLID_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD, Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.STRING });

	/** Init method. */
	public static final Method IMPLID_INIT_METHOD = new Method(ByteCodeMethodsNames.INIT_MTHD, Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.UUID });

	/** Init method. */
	public static final Method INIT_METHOD = new Method(ByteCodeMethodsNames.INIT_MTHD, Type.VOID_TYPE,
			new Type[] {});

	/** Language param or return constructor. */
	public static final Method LANG_PARAM_RETURN_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.DC_WRAPPER });

	/** List add. */
	public static final Method LIST_ADD = new Method(ByteCodeMethodsNames.ADD_MTHD,
			Type.BOOLEAN_TYPE, new Type[] { ByteCodeTypes.OBJECT });

	/** List get element. */
	public static final Method LIST_GET = new Method("get",
			ByteCodeTypes.OBJECT, new Type[] { ByteCodeTypes.OBJECT });

	/** Get method. */
	public static final Method MAP_GET = new Method(ByteCodeMethodsNames.GET_MTHD, ByteCodeTypes.OBJECT, new Type[] { ByteCodeTypes.OBJECT });

	/** Put method. */
	public static final Method MAP_PUT = new Method("put",
			ByteCodeTypes.OBJECT, new Type[] { ByteCodeTypes.OBJECT, ByteCodeTypes.OBJECT });

	/** Init method. */
	public static final Method MCLASSID_INIT_METHOD = new Method(ByteCodeMethodsNames.INIT_MTHD, Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.UUID });

	/** Object with data param or return constructor. */
	public static final Method OBJDATA_PARAM_RETURN_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.DCOBJ });

	/** Trace method. */
	public static final Method PARAVER_TRACE = new Method("trace",
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.PARAVER_EVENT_TYPE,
					ByteCodeTypes.STRING });

	/** Persistent param or return constructor. */
	public static final Method PERS_PARAM_RETURN_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.DCOBJ });

	/** Stack pop element. */
	public static final Method QUEUE_POLL = new Method("poll",
			ByteCodeTypes.OBJECT, new Type[] {});

	/** Run method. */
	public static final Method RUN_METHOD = new Method(ByteCodeMethodsNames.RUN_MTHD, ByteCodeTypes.OBJECT,
			new Type[] { ByteCodeTypes.IMPLID, ByteCodeTypes.OBJECT_ARRAY });

	/** Run method. */
	public static final Method RUNNABLE_RUN_METHOD = new Method(ByteCodeMethodsNames.RUN_MTHD, Type.VOID_TYPE,
			new Type[] {});

	/** Serialize association method in DCObj. */
	public static final Method SERIALIZE_ASSOC = new Method(ByteCodeMethodsNames.SERIALIZE_ASSOCIATION_MTHD, Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.DCOBJ, ByteCodeTypes.DC_BUF, Type.BOOLEAN_TYPE,
					ByteCodeTypes.MAP, ByteCodeTypes.IDENTITYMAP, ByteCodeTypes.LISTITERATOR, ByteCodeTypes.REFERENCE_COUNTING });

	/** Serialize java field method in DCObj. */
	public static final Method SERIALIZE_JAVA_FIELD = new Method("serializeJavaField",
			Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.DC_WRAPPER, ByteCodeTypes.DC_BUF, Type.BOOLEAN_TYPE,
					ByteCodeTypes.MAP, ByteCodeTypes.IDENTITYMAP, ByteCodeTypes.LISTITERATOR, ByteCodeTypes.REFERENCE_COUNTING });

	/** Serialized params or return constructor. */
	public static final Method SERIALIZED_PARAMS_OR_RETURN_CONSTRUCTOR = new Method(ByteCodeMethodsNames.INIT_MTHD,
			Type.VOID_TYPE, new Type[] { Type.INT_TYPE, ByteCodeTypes.MAP,
					ByteCodeTypes.MAP, ByteCodeTypes.MAP, ByteCodeTypes.MAP });

	/** Set field method. */
	public static final Method SET_FIELDS_DESER = new Method("setFieldsDeserialization",
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.JAVA_QUEUE });

	/** setWrappersParams method. */
	public static final Method SET_WRAP_PARAMETERS = new Method("setWrappersParams",
			Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.IMPLID, ByteCodeTypes.SERIALIZED_PARAMS_OR_RETURN });

	/** setWrappersReturn method. */
	public static final Method SET_WRAP_RETURN = new Method("setWrappersReturn",
			Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.IMPLID, ByteCodeTypes.SERIALIZED_PARAMS_OR_RETURN });

	/** Set wrapper. */
	public static final Method SET_WRAPPER = new Method("setWrapper",
			Type.VOID_TYPE, new Type[] { ByteCodeTypes.DC_WRAPPER });

	/** Stack pop element. */
	public static final Method STACK_POP = new Method("pop",
			ByteCodeTypes.OBJECT, new Type[] {});

	/** To string method. */
	public static final Method TO_STRING = new Method("toString", ByteCodeTypes.STRING,
			new Type[] {});

	/** Init method. */
	public static final Method UUID_INIT_METHOD = new Method(ByteCodeMethodsNames.INIT_MTHD, Type.VOID_TYPE,
			new Type[] { Type.LONG_TYPE, Type.LONG_TYPE });

	/** Wrap fields. */
	public static final Method WRAP_FIELDS_DESER = new Method("wrapFieldsDeserialization",
			Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.LIST });

	/** Wrap fields. */
	public static final Method WRAP_FIELDS_SER = new Method("wrapFieldsSerialization",
			Type.VOID_TYPE,
			new Type[] { ByteCodeTypes.LIST });

	/** Wrap params. */
	public static final Method WRAP_PARAMETERS = new Method("wrapParameters",
			ByteCodeTypes.LIST,
			new Type[] { ByteCodeTypes.IMPLID, ByteCodeTypes.OBJECT_ARRAY });

	/** Wrap return. */
	public static final Method WRAP_RETURN = new Method("wrapReturn",
			ByteCodeTypes.LIST,
			new Type[] { ByteCodeTypes.IMPLID, ByteCodeTypes.OBJECT });

	/**
	 * Utility classes should have a private constructor.
	 */
	private ByteCodeMethods() {

	}

}
