
package es.bsc.dataclay.logic.classmgr;

import java.lang.ref.Reference;
import java.util.BitSet;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

@SuppressWarnings("serial")
/** Class used to be parsed by an ASMifier to study which ASM calls should be used. */
public class ASMifierSerialization extends DataClayObject {

	/** Int field to serialize. */
	private int i;
	/** Java field to serialize. */
	private Character c;

	public int modifiedMethod(final int param) {
		if (this.isPersistent()) {
			return ((Integer) super.executeRemoteImplementation("modifiedMethod", "modifiedMethod",
					new Object[] { Integer.valueOf(param) })).intValue();
		}
		return -1;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		final BitSet nullsBitSet = new BitSet(1);
		dcBuffer.writeVLQInt(1);
		final int j = dcBuffer.writerIndex();
		dcBuffer.writeBytes(new byte[1]);
		BitSet ifaceBitSet = null;
		if (ifaceBitMaps != null) {
			ifaceBitSet = BitSet.valueOf(ifaceBitMaps.get(super.getMetaClassID()));
		}
		serialize0(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, nullsBitSet, ifaceBitSet, pendingObjs);
		final int k = dcBuffer.writerIndex();
		dcBuffer.setWriterIndex(j);
		dcBuffer.writeBytes(nullsBitSet.toByteArray());
		dcBuffer.setWriterIndex(k);
		super.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
	}

	public void serialize0(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final BitSet nullsBitSet, final BitSet ifaceBitSet,
			final ListIterator<DataClayObject> pendingObjs) {
		if ((ifaceBitSet == null) || (ifaceBitSet.get(0))) {
			nullsBitSet.set(0);
			dcBuffer.writeInt(this.i);
		}
		if ((ifaceBitSet == null) || (ifaceBitSet.get(1))) {
			nullsBitSet.set(1);
			dcBuffer.writeInt(c.charValue());
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedJavaObjs) {

	}

	public void deserialize0(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final Map<ObjectID, Reference<DataClayObject>> objMap,
			final DataClayObjectMetaData medata, final BitSet nullsBitSet, final BitSet ifaceBitSet,
			final Map<Integer, Object> curDeserializedJavaObjs, final DataServiceAPI dsRef) {

	}

	public void switchExample(final String s) {
		switch (s) {
		case "Aa":
			System.out.println("Hola1");
			break;
		case "aA":
			System.out.println("Hola2");
			break;
		case "BB":
			System.out.println("Hola3");
			break;
		case "que tal":
			System.out.println("QueTal");
			break;
		default:
			System.out.println("Nada");
			break;
		}
	}

	public void dynamicClassExample() {
		final Runnable r = new Runnable() {
			@Override
			public void run() {
				@SuppressWarnings("unused")
				final int x = 100;
			}
		};
		r.run();
	}

}
