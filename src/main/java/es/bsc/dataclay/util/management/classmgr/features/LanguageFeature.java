
/**
 * @file LanguageFeature.java
 * @date OCt 8, 2012
 */
package es.bsc.dataclay.util.management.classmgr.features;

import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents all Language Features (Java, C++,...).
 * 
 */
public final class LanguageFeature extends QualitativeFeature {

	// TODO: We should use SupportedLanguages class instead of a string (1 Apr 2014 jmarti)

	/** ID. */
	private UUID id;
	/** Name of the language. */
	private String languageName;
	/** Version of the language. */
	private String version;

	/**
	 * Empty constructor for deserialization.
	 */
	public LanguageFeature() {

	}

	/**
	 * Constructor with name and version
	 * @param langName
	 *            Name to be set
	 * @param langVersion
	 *            Version to be set
	 * @post Creates a new language with the provided name and version
	 */
	public LanguageFeature(final String langName, final String langVersion) {
		super(FeatureType.LANGUAGE);

		if (langName == null || langName.trim().isEmpty()) {
			throw new IllegalArgumentException("Language name is null or empty");
		}
		if (langVersion == null || langVersion.trim().isEmpty()) {
			throw new IllegalArgumentException("Language version is null or empty");
		}
		this.languageName = langName;
		this.version = langVersion;
	}

	/**
	 * Get the name of the language
	 * @return LanguageFeature::languageName of container LanguageFeature.
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * Set the name of the language
	 * @param newlanguageName
	 *            LanguageFeature::languageName of container LanguageFeature.
	 */
	public void setLanguageName(final String newlanguageName) {
		languageName = newlanguageName;
	}

	/**
	 * Get the version of the language
	 * @return LanguageFeature::version of container LanguageFeature.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the version of the language
	 * @param newversion
	 *            LanguageFeature::version of container LanguageFeature.
	 */
	public void setVersion(final String newversion) {
		version = newversion;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof LanguageFeature) {
			return (this.languageName.equals(((LanguageFeature) obj).getLanguageName())
					&& this.version.equals(((LanguageFeature) obj).getVersion()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.languageName.hashCode() + this.version.hashCode();
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
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

}
