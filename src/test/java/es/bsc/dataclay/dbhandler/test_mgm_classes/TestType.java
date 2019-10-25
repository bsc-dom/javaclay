
package es.bsc.dataclay.dbhandler.test_mgm_classes;

public final class TestType {
	private int type;
	private String name;

	public TestType() {

	}

	public TestType(final int init) {
		this.type = init;
		this.setName("mytype" + init);
	}

	public int getSubType() {
		return this.type;
	}

	public void setSubType(final int newtype) {
		this.type = newtype;
	}

	/**
	 * @brief Get the {@link #name}
	 * @return the name
	 * @author dgasull
	 */

	public String getName() {
		return name;
	}

	/**
	 * @brief Set the {@link #name}
	 * @param newname the name to set
	 * @author dgasull
	 */
	public void setName(final String newname) {
		this.name = newname;
	}	
		
	
	@Override
	public boolean equals(final Object t) {
		if (t instanceof TestType) {
			TestType other = (TestType) t;	
			return other.getName().equals(this.getName()) && other.getSubType() == this.getSubType();
		} else { 
			return false; 
		}
	}
	
	@Override
	public int hashCode() {
		  return 1; // any arbitrary constant will do
	}
	
}
