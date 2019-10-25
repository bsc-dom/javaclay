
package es.bsc.dataclay.dbhandler.test_mgm_classes;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

public class TestParent extends MgrObject<ObjectID> {

	public static final MetaClassID metaClassID = new MetaClassID();
	
	private TestType parentType;
	private int parentID;
	
	public TestParent() {

	}
	
	public TestParent(final int newtype) {
		this.setDataClayID(new ObjectID());
		this.parentType = new TestType(newtype + 1);
		this.parentID = newtype + 1;
	}

    public final TestType getParentType() {
		return this.parentType;
    }

	/**
	* @brief Get the TestParent::parentID
	* @return the parentID
	* @author dgasull
	*/
	
	public final int getParentID() {
		return parentID;
	}

	/**
	* @brief Set the TestParent::parentID
	* @param newparentID the parentID to set
	* @author dgasull
	*/
	public final void setParentID(final int newparentID) {
		this.parentID = newparentID;
	}
}
