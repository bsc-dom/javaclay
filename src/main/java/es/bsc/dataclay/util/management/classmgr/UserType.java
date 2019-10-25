
package es.bsc.dataclay.util.management.classmgr;

import java.util.HashMap;
import java.util.List;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * This class represents a User Type.
 */
public final class UserType extends Type {

	// === YAML SPECIFICATION === // 
	// Properties must be public for YAML parsing.
	//CHECKSTYLE:OFF
	/** Namespace of type if not language type. */
	public String namespace;

	//CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	
	/** ID of the Class if user Type. */
	private MetaClassID classID;

	/**
	 * Creates an empty Type
	 * @note This function does not generate an TypeID. It is necessary for the queries by example used in db4o.
	 * @see
	 */
	public UserType() {

	}

	/**
	 * Type constructor with provided signature
	 * @param newdescriptor Descriptor of the type
	 * @param newsignature
	 *            Signature of the type
	 * @param newnamespace Namespace of the type     
	 * @param newtypename
	 * 			  Type fully qualified name
	 * @param subTypes Sub-types.
	 * @post Creates a new Type with provided name and signature and generates a new TypeID.
	 */
	public UserType(final String newnamespace, final String newtypename, 
			final String newdescriptor,
			final String newsignature, final List<Type> subTypes) {
		super(newdescriptor, newsignature, newtypename, subTypes);
		this.setNamespace(newnamespace);
	}

	/**
	 * Get a valid (identical) Type but without any LanguageDependant* stuff.
	 * @return A stripped-down copy of the current Type
	 * 
	 *         See MetaClass::cloneLanguageless for some insight on the purpose.
	 */
	public UserType cloneLanguageless() {
		final UserType ret = new UserType();

		ret.setClassID(this.getClassID());
		ret.setSignature(this.getSignature());
		ret.setDescriptor(this.getDescriptor());
		ret.setTypeName(this.getTypeName());
		ret.setNamespace(this.getNamespace());
		// This is the actual strip-down operation
		// (we are ignoring this.languageDepInfos field)
		ret.setLanguageDepInfos(new HashMap<Langs, LanguageDependantTypeInfo>());

		return ret;
	}

	/**
	 * Get the Type::id
	 * @return the id
	 */

	public MetaClassID getClassID() {
		return classID;
	}

	/**
	 * Set the Type::id
	 * @param newclassID
	 *            the id to set
	 */
	public void setClassID(final MetaClassID newclassID) {
		this.classID = newclassID;
	}

	/**
	 * Get the Type::namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Set the Type::namespace
	 * @param newnamespace the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

}
