
package es.bsc.dataclay.util.yaml;

import org.yaml.snakeyaml.Yaml;
// import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Common functions used in YAML loading/parsing/interpreting.
 */
public final class CommonYAML {
	
	/**
	 * Disabled default constructor.
	 */
	private CommonYAML() {
	}
	
	/**
	 * Global YAML object, with global valid tags.
	 * @return A newly created Yaml class with valid parsing configuration.
	 */
	public static Yaml getYamlObject() {
		final Yaml retYaml = new Yaml(new CustomRepresenter());
		return retYaml;
	}
}
