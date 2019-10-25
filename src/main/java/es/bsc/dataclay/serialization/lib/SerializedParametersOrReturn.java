
package es.bsc.dataclay.serialization.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Serialized parameters or returns.
 */
public final class SerializedParametersOrReturn {

	/** Number of 'actual' parameters. */
	private int numParams;

	/** Immutable parameters or returns. */
	private Map<Integer, ImmutableParamOrReturn> immObjs;

	/**
	 * Language parameters or returns. Important: only parameters or returns, not
	 * language objects inside volatiles.
	 */
	private Map<Integer, LanguageParamOrReturn> langObjs;

	/** Volatile parameters or returns. */
	private Map<Integer, ObjectWithDataParamOrReturn> volatileObjs;

	/** Persistent parameters or returns. */
	private Map<Integer, PersistentParamOrReturn> persistentRefs;

	/**
	 * Empty constructor for YAML.
	 */
	public SerializedParametersOrReturn() {

	}

	/**
	 * Constructor
	 * 
	 * @param theNumParams
	 *            Number of declared parameters of the method.
	 * @param theimmObjs
	 *            Immutable parameters or return
	 * @param thelangObjs
	 *            Language parameters or reutrn
	 * @param thevolatileObjs
	 *            Volatile parameters or return
	 * @param thepersistentRefs
	 *            Persistent params or return Mandatory for: Returns to client and
	 *            Volatile parameters (execute or mkPersistent)
	 */
	public SerializedParametersOrReturn(final int theNumParams, final Map<Integer, ImmutableParamOrReturn> theimmObjs,
			final Map<Integer, LanguageParamOrReturn> thelangObjs,
			final Map<Integer, ObjectWithDataParamOrReturn> thevolatileObjs,
			final Map<Integer, PersistentParamOrReturn> thepersistentRefs) {
		this.numParams = theNumParams;
		this.immObjs = theimmObjs;
		this.langObjs = thelangObjs;
		this.volatileObjs = thevolatileObjs;
		this.persistentRefs = thepersistentRefs;
	}

	/**
	 * Constructor for volatiles
	 * 
	 * @param thevolatileObjs
	 *            Volatile parameters or return
	 */
	public SerializedParametersOrReturn(final List<ObjectWithDataParamOrReturn> thevolatileObjs) {
		this.numParams = thevolatileObjs.size();
		this.immObjs = new HashMap<>();
		this.langObjs = new HashMap<>();
		this.volatileObjs = new HashMap<>();
		this.persistentRefs = new HashMap<>();
		int i = 0;
		for (ObjectWithDataParamOrReturn obj : thevolatileObjs) {
			volatileObjs.put(i, obj);
			i++;
		}
	}

	/**
	 * @return the langObjs
	 */
	public Map<Integer, LanguageParamOrReturn> getLangObjs() {
		return langObjs;
	}

	/**
	 * @return the volatileObjs
	 */
	public Map<Integer, ObjectWithDataParamOrReturn> getVolatileObjs() {
		return volatileObjs;
	}

	/**
	 * @return the persistentRefs
	 */
	public Map<Integer, PersistentParamOrReturn> getPersistentRefs() {
		return persistentRefs;
	}

	/**
	 * @return the numParams
	 */
	public int getNumParams() {
		return numParams;
	}

	/**
	 * @return the immObjs
	 */
	public Map<Integer, ImmutableParamOrReturn> getImmObjs() {
		return immObjs;
	}

	/**
	 * Remove DataClayObjects for yaml serialization
	 */
	public void removeReferencesForYaml() {
		for (final ObjectWithDataParamOrReturn volatil : this.volatileObjs.values()) {
			volatil.setDataClayObject(null);
		}
		for (final LanguageParamOrReturn lang : this.langObjs.values()) {
			lang.setWrapper(null);
		}
		for (final ImmutableParamOrReturn imm : this.immObjs.values()) {
			imm.setWrapper(null);
		}
		for (final PersistentParamOrReturn pers : this.persistentRefs.values()) {
			pers.setDcObject(null);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(numParams, immObjs, langObjs, volatileObjs, persistentRefs);
	}

	/**
	 * Set numParams
	 * 
	 * @param newnumParams
	 *            the numParams to set
	 */
	public void setNumParams(final int newnumParams) {
		this.numParams = newnumParams;
	}

	/**
	 * Set immObjs
	 * 
	 * @param newimmObjs
	 *            the immObjs to set
	 */
	public void setImmObjs(final Map<Integer, ImmutableParamOrReturn> newimmObjs) {
		this.immObjs = newimmObjs;
	}

	/**
	 * Set langObjs
	 * 
	 * @param newlangObjs
	 *            the langObjs to set
	 */
	public void setLangObjs(final Map<Integer, LanguageParamOrReturn> newlangObjs) {
		this.langObjs = newlangObjs;
	}

	/**
	 * Set volatileObjs
	 * 
	 * @param newvolatileObjs
	 *            the volatileObjs to set
	 */
	public void setVolatileObjs(final Map<Integer, ObjectWithDataParamOrReturn> newvolatileObjs) {
		this.volatileObjs = newvolatileObjs;
	}

	/**
	 * Set persistentRefs
	 * 
	 * @param newpersistentRefs
	 *            the persistentRefs to set
	 */
	public void setPersistentRefs(final Map<Integer, PersistentParamOrReturn> newpersistentRefs) {
		this.persistentRefs = newpersistentRefs;
	}

	/**
	 * Indicates if serialized parameters need wrappers for language/immutable
	 * objects.
	 * 
	 * @return TRUE if serialized parameters need wrappers for language/immutable
	 *         objects.
	 */
	public boolean needWrappers() {
		return this.immObjs.size() > 0 || this.langObjs.size() > 0;
	}

}
