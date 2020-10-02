
package es.bsc.dataclay.util.management.classmgr.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantClassInfo;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents Java language dependant information for a MetaClass.
 * 
 */
public final class JavaClassInfo implements LanguageDependantClassInfo {

	/** ID. */
	private UUID id;
	/** Signature of the class. */
	private String signature;
	/** Java parent interfaces. */
	private String[] javaParentInterfaces;
	/** Original class bytecode. Ready to be transformed for stubs and exec. classes */
	private byte[] classByteCode;
	/** Class modifier. */
	private int modifier;
	/** List of sub-types of the type (like arrays, collections...). */
	private List<Type> includes;
	
	/**
	 * Creates an empty JavaClassInfo
	 */
	public JavaClassInfo() {

	}

	/**
	 * Java Class Info constructor with provided specifications and IDs
	 * @param newsignature
	 *            Signature of the class
	 * @param newclassByteCode
	 *            The bytecode of the class.
	 */
	public JavaClassInfo(final String newsignature, final byte[] newclassByteCode) {
		this.setSignature(newsignature);
		this.setJavaParentInterfaces(new String[] {});
		this.setClassByteCode(newclassByteCode);
		this.setIncludes(new ArrayList<Type>());
	}

	/**
	 * Get the JavaClassInfo::signature
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Set the JavaClassInfo::signature
	 * @param newsignature
	 *            the signature to set
	 */
	public void setSignature(final String newsignature) {
		this.signature = newsignature;
	}

	/**
	 * Get the JavaClassInfo::javaParentInterfaces
	 * @return the javaParentInterfaces
	 */
	public String[] getJavaParentInterfaces() {
		return javaParentInterfaces;
	}

	/**
	 * Set the JavaClassInfo::javaParentInterfaces
	 * @param newjavaParentInterfaces
	 *            the javaParentInterfaces to set
	 */
	public void setJavaParentInterfaces(final String[] newjavaParentInterfaces) {
		this.javaParentInterfaces = newjavaParentInterfaces;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.signature);

	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof JavaClassInfo) {
			final JavaClassInfo other = (JavaClassInfo) object;
			if (this.signature != null) {
				if (!this.signature.equals(other.getSignature())) {
					return false;
				}
			} else {
				if (other.getSignature() != null) {
					return false;
				}
			}
			if (this.javaParentInterfaces != null) {
				if (other.getJavaParentInterfaces() == null) {
					return false;
				}
				if (!Arrays.equals(this.javaParentInterfaces, other.getJavaParentInterfaces())) {
					return false;
				}
			} else {
				if (other.getJavaParentInterfaces() != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Get classByteCode
	 * @return the classByteCode
	 */
	public byte[] getClassByteCode() {
		return classByteCode;
	}

	/**
	 * Set classByteCode
	 * @param newclassByteCode
	 *            the classByteCode to set
	 */
	public void setClassByteCode(final byte[] newclassByteCode) {
		this.classByteCode = newclassByteCode;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param theid
	 *            the id to set
	 */
	public void setId(final UUID theid) {
		this.id = theid;
	}

	/**
	 * Gets modifier.
	 * @return the modifier
	 */
	public int getModifier() {
		return modifier;
	}

	/**
	 * Sets modifier
	 * @param themodifier the modifier to set
	 */
	public void setModifier(final int themodifier) {
		this.modifier = themodifier;
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

}
