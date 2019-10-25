
package es.bsc.dataclay.util.management.stubs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This utility class is used for class loading of stubs and set of Session IDs.
 * 
 * 
 */
public final class StubClassLoader {

	/**
	 * Default constructor for this Utility Class.
	 */
	private StubClassLoader() {

	}

	/**
	 * Get class names except ones in package provided
	 * @param actualSrcDir
	 *            Actual directory
	 * @param srcDirectory
	 *            directories
	 * @param classNames
	 *            [out] File names
	 * @param extension
	 *            Extension of classes to check
	 * @param packagesToExclude
	 *            Packages to exclude
	 */
	public static void getClasses(final String actualSrcDir,
			final File srcDirectory, final Set<String> classNames, final String extension,
			final Set<String> packagesToExclude) {

		if (srcDirectory.isDirectory()) {
			final File[] files = srcDirectory.listFiles();
			if (files.length > 0) {
				for (final File file : files) {
					if (file.isDirectory()) {
						// Loop through its listFiles() recursively.
						getClasses(actualSrcDir, file, classNames, extension, packagesToExclude);
					} else {
						final String name = file.getName();
						if (name.endsWith(extension)) {
							String fullName = file.getPath();
							fullName = fullName.replace(actualSrcDir, "");
							fullName = fullName.replace(extension, "");
							// Remove starting bar "/" so we can later replace all bars for '.' as packages
							fullName = fullName.replace(File.separator, ".");
							if (fullName.startsWith(".")) {
								fullName = fullName.substring(1);
							}

							boolean addClass = true;
							if (packagesToExclude != null) {
								for (final String packageToExclude : packagesToExclude) {
									if (fullName.contains(packageToExclude)) {
										addClass = false;
										break;
									}
								}
							}
							if (addClass) {
								classNames.add(fullName);
							}
						}

					}
				}
			}
		}
	}

	/**
	 * Get the stub info of all stub classes in the provided class path
	 * @param stubClassPath
	 *            Class path
	 * @return A set of information of all stubs in provided class path
	 * @throws Exception
	 *             If some class was not found in class path and should be, or could not initialize paraver trace if needed.
	 */
	public static Map<String, StubInfo> getStubInfosFromClassPath(final String stubClassPath) throws Exception {
		// Get class Loader
		// Now the consumer will registers the class Enrichment
		final Map<String, StubInfo> stubInfos = new HashMap<>();
		final String[] classPaths = stubClassPath.split(File.pathSeparator);
		for (final String classPath : classPaths) {
			final Set<String> classNames = new HashSet<>();
			final Path inputPath = Paths.get(classPath).normalize();
			final Path fullSrcPath = inputPath.toAbsolutePath();
			final String fullPath = fullSrcPath.toString().replace(File.separatorChar + "." + File.separatorChar,
					File.separator);
			StubClassLoader.getClasses(fullPath, new File(fullPath), classNames, ".class", null);

			for (final String userClass : classNames) {
				try {
					final Yaml yaml = CommonYAML.getYamlObject();
					final String fileName = userClass.replace(".", File.separator) + "Yaml.yaml";
					final InputStream ios = new FileInputStream(new File(fullPath + File.separatorChar + fileName));
					final StubInfo stubInfo = (StubInfo) yaml.load(ios);

					stubInfos.put(userClass, stubInfo);
				} catch (final Exception e) {
					// file without yaml
					continue;
				}
			}
		}
		return stubInfos;
	}
}
