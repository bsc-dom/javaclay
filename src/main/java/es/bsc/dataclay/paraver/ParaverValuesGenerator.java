package es.bsc.dataclay.paraver;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * This class generates/updates a file with values for each method in dataClay to be used by Paraver.
 *
 */
public class ParaverValuesGenerator {

	/** First value to use as a method in Java. Remember that Java and Python have different starting values. */
	private static final int STARTING_VALUE = 1000; 
	
	/** Ignored packages. */
	private static final String[] IGNORED_PACKAGES = new String[] { "es.bsc.dataclay.communication" };
	
	/**
	 * Main method.
	 * @param args No arguments
	 * @throws IOException if cannot write into file
	 */
	public static void main(final String[] args) throws IOException {
		System.out.println("** Welcome **");

		final List<ClassLoader> classLoadersList = new LinkedList<>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		final Reflections reflections = new Reflections(new ConfigurationBuilder()
		    .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
		    .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
		    .filterInputsBy(new FilterBuilder()
    				.includePackage("es.bsc.dataclay").includePackage("storage"))
				);
		final Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
		final Map<String, Integer> allMethods = new HashMap<>(); //avoid repeated
		int currentValue = STARTING_VALUE;
		final Properties prop = new Properties();

		for (final Class<?> cl : allClasses) { 
			boolean packageAccepted = true;
			for (final String ignoredPackage : IGNORED_PACKAGES) { 
				if (cl.getName().startsWith(ignoredPackage)) {
					packageAccepted = false;
				}
			}
			if (packageAccepted)  {
				final Set<Method> methods = ReflectionUtils.getAllMethods(cl);
				for (final Method m : methods) { 
					final String key = cl.getName() + "." + m.getName();
					if (!allMethods.containsKey(key)) {
						prop.setProperty(key, String.valueOf(currentValue));

						allMethods.put(key, currentValue);
						currentValue++;
					}
				}
			}
		}
		System.out.println("Done! Found " + allMethods.size() + " methods!");
		try (FileWriter writer = new FileWriter("./paraver_values.properties")) {
			prop.store(writer, "Paraver event values");
		}
	}

}
