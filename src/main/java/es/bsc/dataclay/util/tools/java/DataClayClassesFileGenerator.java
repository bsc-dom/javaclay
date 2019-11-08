package es.bsc.dataclay.util.tools.java;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * This class generates/updates a file with names of all dataClay classes for SpecGenerator to ignore.
 *
 */
public class DataClayClassesFileGenerator {

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
		final Properties prop = new Properties();
		int i = 0;
		for (final Class<?> cl : allClasses) { 
			prop.setProperty(cl.getName(), String.valueOf(i));
			i++;
			
		}
		System.out.println("Done! Found " + allClasses.size() + " classes!");
		try (FileWriter writer = new FileWriter("./dataclay_classnames.properties")) {
			prop.store(writer, "DataClay classes");
		}
	}

}
