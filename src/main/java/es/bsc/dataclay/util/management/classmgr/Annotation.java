
package es.bsc.dataclay.util.management.classmgr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.logicmodule.classmgr.AnnotationDepInfoAlreadyRegisteredException;
import es.bsc.dataclay.util.management.classmgr.java.JavaAnnotationInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonAnnotationInfo;

public class Annotation {
	// === YAML SPECIFICATION === //
	// Annotations must be public for YAML parsing.
	//CHECKSTYLE:OFF
	private UUID id;

	//CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	private Map<Langs, LanguageDependantAnnotationInfo> languageDepInfos;

	private String descr;

	private Map<String, String> parameters;

	public Annotation() {

	}

	public Annotation(String descr) {
		this(descr, new HashMap<>());
	}

	public Annotation(final String descr, final Map<String, String> parameters) {
		this.setDescr(descr);
		this.setParameters(parameters);
		this.setLanguageDepInfos(new HashMap<Langs, LanguageDependantAnnotationInfo>());
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(final String name, final Object value) {
		parameters.put(name, value.toString());
	}

	public String getParameter(final String name) {
		return parameters.get(name);
	}

	public UUID getId() {
		return id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	public Map<Langs, LanguageDependantAnnotationInfo> getLanguageDepInfos() {
		return languageDepInfos;
	}

	public void setLanguageDepInfos(Map<Langs, LanguageDependantAnnotationInfo> languageDepInfos) {
		this.languageDepInfos = languageDepInfos;
	}

	/**
	 * Get Java language-dependant information of the annotation
	 * @return Java language-dependant information of the annotation
	 */
	public JavaAnnotationInfo getJavaAnnotationInfo() {
		return (JavaAnnotationInfo) languageDepInfos.get(Langs.LANG_JAVA);
	}

	/**
	 * Get Python language-dependant information of the annotation
	 * @return Python language-dependant information of the annotation
	 */
	public PythonAnnotationInfo getPythonAnnotationInfo() {
		return (PythonAnnotationInfo) languageDepInfos.get(Langs.LANG_PYTHON);
	}

	/**
	 * Add language dependant information
	 * @param langInfo
	 *            Language information
	 */
	public void addLanguageDepInfo(final LanguageDependantAnnotationInfo langInfo) {
		if (langInfo instanceof JavaAnnotationInfo) {
			if (this.languageDepInfos.get(Langs.LANG_JAVA) != null) {
				throw new AnnotationDepInfoAlreadyRegisteredException(Langs.LANG_JAVA.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_JAVA, langInfo);
			}
		} else if (langInfo instanceof PythonAnnotationInfo) {
			if (this.languageDepInfos.get(Langs.LANG_PYTHON) != null) {
				throw new AnnotationDepInfoAlreadyRegisteredException(Langs.LANG_PYTHON.name());
			} else {
				this.languageDepInfos.put(Langs.LANG_PYTHON, langInfo);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if( o instanceof Annotation ) {
			return ((Annotation)o).descr.equals(this.descr);
		}
		return false;
	}

	@Override
	public String toString() {
		return "DESC " + descr + " | PARAMS " + parameters.toString();
	}
}
