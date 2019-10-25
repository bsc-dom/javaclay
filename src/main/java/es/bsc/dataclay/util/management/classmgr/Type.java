
package es.bsc.dataclay.util.management.classmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.TypeDepInfoAlreadyRegisteredException;
import es.bsc.dataclay.util.management.classmgr.java.JavaTypeInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonTypeInfo;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents a Type.
 */
public class Type {

	/** ID. */
	private UUID id;
	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	// CHECKSTYLE:OFF
	/** Descriptor of the type. */
	public String descriptor;
	/** Signature of the type. */
	public String signature;
	/** Type name. */
	public String typeName;
	/** List of sub-types of the type (like arrays, collections...). */
	public List<Type> includes;
	// CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** Language dependant property information. */
	private Map<Langs, LanguageDependantTypeInfo> languageDepInfos;

	/**
	 * Creates an empty Type
	 * @note This function does not generate an TypeID. It is necessary for the queries by example used in db4o.
	 * @see
	 */
	public Type() {

	}

	/**
	 * Type constructor with provided descriptor and signature
	 * @param newdescriptor
	 *            Descriptor of the type
	 * @param newsignature
	 *            Signature of the type
	 * @param newtypeName
	 *            Type name
	 */
	public Type(final String newdescriptor, final String newsignature, final String newtypeName) {
		this(newdescriptor, newsignature, newtypeName, null);
	}

	/**
	 * Type constructor with provided signature
	 * @param newdescriptor
	 *            Descriptor of the type
	 * @param newsignature
	 *            Signature of the type
	 * @param newtypeName
	 *            Type name
	 * @param newincludes
	 *            Subtypes
	 */
	public Type(final String newdescriptor, final String newsignature, final String newtypeName,
			final List<Type> newincludes) {
		this.setDescriptor(newdescriptor);
		this.setSignature(newsignature);
		this.setIncludes(newincludes);
		this.setTypeName(newtypeName);
		this.languageDepInfos = new HashMap<>();
	}

	/**
	 * Get the Type::languageDepInfos
	 * @return the languageDepInfos
	 */
	public final Map<Langs, LanguageDependantTypeInfo> getLanguageDepInfos() {
		return languageDepInfos;
	}

	/**
	 * Get Java language-dependant information of the Type
	 * @return Java language-dependant information of the Type
	 */
	public final JavaTypeInfo getJavaTypeInfo() {
		return (JavaTypeInfo) languageDepInfos.get(Langs.LANG_JAVA);
	}

	/**
	 * Get Python language-dependant information of the Type
	 * @return Python language-dependant information of the Type
	 */
	public final PythonTypeInfo getPythonTypeInfo() {
		return (PythonTypeInfo) languageDepInfos.get(Langs.LANG_PYTHON);
	}

	/**
	 * Get the Type::signature
	 * @return the signature
	 */
	public final String getSignature() {
		return this.signature;
	}

	/**
	 * Get the Type::signature or descriptor if null
	 * @return the signature or descriptor if null
	 */
	public final String getSignatureOrDescriptor() {
		if (this.signature != null) {
			return signature;
		} else {
			return descriptor;
		}
	}

	/**
	 * Set the Type::signature
	 * @param newsignature
	 *            the signature to set
	 */
	public final void setSignature(final String newsignature) {
		this.signature = newsignature;
	}

	/**
	 * Set the Type::languageDepInfos
	 * @param newlanguageDepInfos
	 *            the languageDepInfos to set
	 */
	public final void setLanguageDepInfos(final Map<Langs, LanguageDependantTypeInfo> newlanguageDepInfos) {
		this.languageDepInfos = newlanguageDepInfos;
	}

	/**
	 * Add language dependant information
	 * @param langInfo
	 *            Language information
	 */
	public final void addLanguageDepInfo(final LanguageDependantTypeInfo langInfo) {
		if (langInfo instanceof JavaTypeInfo) {
			if (this.languageDepInfos.get(Langs.LANG_JAVA) != null) {
				throw new TypeDepInfoAlreadyRegisteredException(Langs.LANG_JAVA.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_JAVA, langInfo);
			}
		} else if (langInfo instanceof PythonTypeInfo) {
			if (this.languageDepInfos.get(Langs.LANG_PYTHON) != null) {
				throw new TypeDepInfoAlreadyRegisteredException(Langs.LANG_PYTHON.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_PYTHON, langInfo);
			}
		}
	}

	/**
	 * Get includes
	 * @return the includes
	 */
	public final List<Type> getIncludes() {
		return includes;
	}

	/**
	 * Set includes
	 * @param newincludes
	 *            the includes to set
	 */
	public final void setIncludes(final List<Type> newincludes) {
		this.includes = newincludes;
	}

	// CHECKSTYLE:OFF

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.descriptor, this.signature);
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof Type) {
			final Type other = (Type) object;
			if (this.descriptor != null) {
				if (!this.descriptor.equals(other.getDescriptor())) {
					return false;
				}
			} else {
				if (other.getDescriptor() != null) {
					return false;
				}
			}
			if (this.signature != null) {
				if (!this.signature.equals(other.getSignature())) {
					return false;
				}
			} else {
				if (other.getSignature() != null) {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * Get descriptor
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * Set descriptor
	 * @param newdescriptor
	 *            the descriptor to set
	 */
	public void setDescriptor(final String newdescriptor) {
		this.descriptor = newdescriptor;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final UUID id) {
		this.id = id;
	}

	// CHECKSTYLE:ON

}
