
package es.bsc.dataclay.logic.classmgr.bytecode.java.constants;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.dataservice.DataService;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.lang.ObjectWrapper;
import es.bsc.dataclay.serialization.java.util.ArrayWrapper;
import es.bsc.dataclay.serialization.java.util.CollectionWrapper;
import es.bsc.dataclay.serialization.java.util.MapWrapper;
import es.bsc.dataclay.serialization.java.util.concurrent.atomic.AtomicIntegerWrapper;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.serialization.lib.ImmutableParamOrReturn;
import es.bsc.dataclay.serialization.lib.LanguageParamOrReturn;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.PersistentParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.stubs.ImplementationStubInfo;
import es.bsc.dataclay.util.management.stubs.PropertyStubInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This class contains types of includes of Stubs.
 */
public final class ByteCodeTypes {

	/**
	 * Utility classes should have private constructor.
	 */
	private ByteCodeTypes() {

	}

	/** Object type. */
	public static final Type OBJECT = Type.getType(Object.class);

	/** UUID type. */
	public static final Type UUID = Type.getType(UUID.class);

	/** StubInfo object Type. */
	public static final Type STUBINFO = Type.getType(StubInfo.class);

	/** PropertyStubInfo object Type. */
	public static final Type PROPSTUBINF = Type.getType(PropertyStubInfo.class);

	/** Type object Type. */
	public static final Type TYPE_CLSNAME = Type.getType(es.bsc.dataclay.util.management.classmgr.Type.class);

	/** HashMap object Type. */
	public static final Type HASHMAP = Type.getType(HashMap.class);

	/** HashSet object Type. */
	public static final Type HASHSET = Type.getType(HashSet.class);

	/** InterfaceID object Type. */
	public static final Type IFACEID = Type.getType(InterfaceID.class);

	/** ContractID object Type. */
	public static final Type CNTRCTID = Type.getType(ContractID.class);

	/** ImplementationID object Type. */
	public static final Type IMPLID = Type.getType(ImplementationID.class);

	/** SessionID object Type. */
	public static final Type SESSID = Type.getType(SessionID.class);

	/** AccountID object Type. */
	public static final Type ACCID = Type.getType(AccountID.class);

	/** ObjectID object Type. */
	public static final Type OBJID = Type.getType(ObjectID.class);

	/** MetaClassID obejct Type. */
	public static final Type MCLASSID = Type.getType(MetaClassID.class);

	/** DataSetID obejct Type. */
	public static final Type DATASETID = Type.getType(DataSetID.class);

	/** OperationID object Type. */
	public static final Type OPID = Type.getType(OperationID.class);

	/** PropertyID object Type. */
	public static final Type PROPID = Type.getType(PropertyID.class);

	/** ArrayList object Type. */
	public static final Type ARRLIST = Type.getType(ArrayList.class);

	/** DCByteBuffer object Type. */
	public static final Type DC_BUF = Type.getType(DataClayByteBuffer.class);

	/** DCExecObject object Type. */
	public static final Type DCEXECOBJ = Type.getType(DataClayExecutionObject.class);

	/** DCObject object Type. */
	public static final Type DCOBJ = Type.getType(DataClayObject.class);

	/** DCObject object Type. */
	public static final Type DATACLAY_SERIALIZATION_LIB = Type.getType(DataClaySerializationLib.class);

	/** BitSet object Type. */
	public static final Type BITSET = Type.getType(BitSet.class);

	/** DCJavaArray Object Type. */
	public static final Type DCJAVAARRAY = Type.getType(ArrayWrapper.class);

	/** DCJavaCollection Object Type. */
	public static final Type DCJAVACOLL = Type.getType(CollectionWrapper.class);

	/** DCjavaWrapper Object Type. */
	public static final Type DC_WRAPPER = Type.getType(DataClayJavaWrapper.class);

	/** GenericWrapper Object Type. */
	public static final Type GENERIC_WRAPPER = Type.getType(ObjectWrapper.class);

	/** MapEntry Object Type. */
	public static final Type DC_MAP_ENTRY = Type.getType(es.bsc.dataclay.serialization.java.util.Map.EntryWrapper.class);

	/** AtomicInteger Object Type. */
	public static final Type DC_ATOMIC_INTEGER = Type.getType(AtomicIntegerWrapper.class);

	/** DCJavaMap Object Type. */
	public static final Type DCJAVAMAP = Type.getType(MapWrapper.class);

	/** Runnable Object Type. */
	public static final Type RUNNABLE = Type.getType(Runnable.class);

	/** String Object Type. */
	public static final Type STRING = Type.getType(String.class);

	/** List Object Type. */
	public static final Type LIST = Type.getType(List.class);

	/** ListIterator Object Type. */
	public static final Type LISTITERATOR = Type.getType(ListIterator.class);

	/** LinkedList Object Type. */
	public static final Type LINKLIST = Type.getType(LinkedList.class);

	/** LinkedHashMap Object Type. */
	public static final Type LINKMAP = Type.getType(LinkedHashMap.class);

	/** Collection Object Type. */
	public static final Type COLL = Type.getType(Collection.class);

	/** Map Object Type. */
	public static final Type MAP = Type.getType(Map.class);

	/** Weak Reference Object Type. */
	public static final Type WEAKREF = Type.getType(WeakReference.class);

	/** Soft Reference Object Type. */
	public static final Type SOFTREF = Type.getType(SoftReference.class);

	/** DataService Object Type. */
	public static final Type DATASERVICE = Type.getType(DataService.class);

	/** DataService Object Type. */
	public static final Type DATASERVICEAPI = Type.getType(DataServiceAPI.class);

	/** IdentityHashMap Object Type. */
	public static final Type IDENTITYMAP = Type.getType(IdentityHashMap.class);

	/** Iterator Object Type. */
	public static final Type IT = Type.getType(Iterator.class);

	/** Set Object Type. */
	public static final Type SET = Type.getType(Set.class);

	/** Tuple Type. */
	public static final Type TUPLE = Type.getType(Tuple.class);

	/** Map.Entry Object Type. */
	public static final Type MAPENTRY = Type.getType(Map.Entry.class);

	/** AtomicInteger Object Type. */
	public static final Type ATOMIC_INTEGER = Type.getType(AtomicInteger.class);

	/** Java Integer Object Type. */
	public static final Type INTEGER = Type.getType(Integer.class);

	/** Java Byte Object Type. */
	public static final Type BYTE = Type.getType(Byte.class);

	/** Java Character Object Type. */
	public static final Type CHARACTER = Type.getType(Character.class);

	/** Java Double Object Type. */
	public static final Type DOUBLE = Type.getType(Double.class);

	/** Java Float Object Type. */
	public static final Type FLOAT = Type.getType(Float.class);

	/** Java Long Object Type. */
	public static final Type LONG = Type.getType(Long.class);

	/** Java Boolean Object Type. */
	public static final Type BOOLEAN = Type.getType(Boolean.class);

	/** Java boolean primitive Type. */
	public static final Type BOOLEAN_PRIMITIVE = Type.getType(boolean.class);

	/** Java Short Object Type. */
	public static final Type SHORT = Type.getType(Short.class);

	/** Constructor Type. */
	public static final Type CONSTRUCTOR = Type.getType(Constructor.class);

	/** ImplementationStubInfo Type. */
	public static final Type IMPLSTUBINFO = Type.getType(ImplementationStubInfo.class);

	/** DataClayObjectMetaData Type. */
	public static final Type DC_OBJECT_METADATA = Type.getType(DataClayObjectMetaData.class);

	/** Object array Type. */
	public static final Type OBJECT_ARRAY = Type.getType(Object[].class);

	/** No arguments. */
	public static final Type[] NO_ARGS = new Type[] {};

	/** Byte array type. */
	public static final Type BYTE_ARRAY = Type.getType(byte[].class);

	/** Serialized parameters or return type. */
	public static final Type SERIALIZED_PARAMS_OR_RETURN = Type.getType(SerializedParametersOrReturn.class);

	/** Ref counting type. */
	public static final Type REFERENCE_COUNTING = Type.getType(ReferenceCounting.class);

	/** Illegal argument exception. */
	public static final Type ILLEGAL_ARGUMENT_EXCEPTION = Type.getType(IllegalArgumentException.class);

	/** Immutable parameter or return class. */
	public static final Type IMM_PARAM_RETURN = Type.getType(ImmutableParamOrReturn.class);

	/** Language parameter or return class. */
	public static final Type LANG_PARAM_RETURN = Type.getType(LanguageParamOrReturn.class);

	/** Object with Data parameter or return class. */
	public static final Type OBJDATA_PARAM_RETURN = Type.getType(ObjectWithDataParamOrReturn.class);

	/** Persistent parameter or return class. */
	public static final Type PERS_PARAM_RETURN = Type.getType(PersistentParamOrReturn.class);

	/** Serializable type. */
	public static final Type DATACLAY_SERIALIZABLE = Type.getType(DataClaySerializable.class);

	/** Client lib. */
	public static final Type DATACLAY_CLIENT_LIB = Type.getType(DataClayRuntime.class);

	/** DataClayObject type. */
	public static final Type DATACLAY_OBJECT = Type.getType(DataClayObject.class);

	/** Stack. */
	public static final Type JAVA_STACK = Type.getType(Stack.class);

	/** Queue. */
	public static final Type JAVA_QUEUE = Type.getType(Queue.class);

	/** Logger. */
	public static final Type LOGGER = Type.getType(Logger.class);

	/** BackendID. */
	public static final Type BackendID = Type.getType(BackendID.class);

}
