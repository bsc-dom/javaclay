
package es.bsc.dataclay.util.filtering;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Predicate;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.filtering.Condition.ConditionOp;
import es.bsc.dataclay.util.ids.MetaClassID;

@SuppressWarnings("rawtypes")
public class DataClayPredicate implements Predicate, DataClaySerializable {
	private String att;
	private String op;
	private String val;

	public DataClayPredicate(final String attribute, final String operator, final String value) {
		att = attribute;
		op = operator;
		val = value;
	}

	public static Predicate build(final String attribute, final String operator, final String value) {
		return new DataClayPredicate(attribute, operator, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean test(Object t) {
		try {
			final Method getter = t.getClass().getMethod(ClassManager.GETTER_PREFIX + this.att);
			final Comparable fieldValue = (Comparable) getter.invoke(t);
			final Comparable valueTyped = (Comparable) t.getClass().getConstructor(String.class).newInstance(this.val);
			final ConditionOp condOp = ConditionOp.fromString(this.op);
			switch (condOp) {
			case DIFFERENT:
				return fieldValue.compareTo(valueTyped) != 0;
			case EQUALS:
				return fieldValue.compareTo(valueTyped) == 0;
			case GREATER:
				return fieldValue.compareTo(valueTyped) > 0;
			case GREATER_EQUALS:
				return fieldValue.compareTo(valueTyped) >= 0;
			case LESS:
				return fieldValue.compareTo(valueTyped) < 0;
			case LESS_EQUALS:
				return fieldValue.compareTo(valueTyped) <= 0;
			default:
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public DataClayPredicate and(Predicate other) {
		if (other instanceof DataClayPredicate) {
			return this.and(other);
		} else {
			return null;
		}
	}

	public String toString() {
		return att + op + val;
	}

	@Override
	public void serialize(DataClayByteBuffer dcBuffer, boolean ignoreUserTypes, Map<MetaClassID, byte[]> ifaceBitMaps, IdentityHashMap<Object, Integer> curSerializedObjs,
			ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeString(att);
		dcBuffer.writeString(op);
		dcBuffer.writeString(val);
	}

	@Override
	public void deserialize(DataClayByteBuffer dcBuffer, Map<MetaClassID, byte[]> ifaceBitMaps, DataClayObjectMetaData metadata, Map<Integer, Object> curDeserializedJavaObjs) {
		att = dcBuffer.readString();
		op = dcBuffer.readString();
		val = dcBuffer.readString();
	}
}
