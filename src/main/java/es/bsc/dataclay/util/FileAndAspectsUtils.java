
package es.bsc.dataclay.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for aspects and files.
 */
public final class FileAndAspectsUtils {
	private static final Logger logger = LogManager.getLogger("util.FileAndAspectsUtils");

	/** Indicates error while analyzing package. */
	private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. "
			+ "Are you sure the package '%s' exists?";

	/**
	 * Utility classes should have a private constructor.
	 */
	private FileAndAspectsUtils() {

	}

	/**
	 * Method that creates a directory on the specified path (or does nothing if it already exists)
	 * @param targetDirectoryPath
	 *            the directory path
	 */
	public static void createDirectory(final String targetDirectoryPath) {
		try {
			// Create one directory
			(new File(targetDirectoryPath)).mkdirs();
		} catch (final Exception e) {
			logger.error("Exception in createDirectory", e);
		}
	}

	/**
	 * This function deletes the files inside the folder provided
	 * @param folder
	 *            Folder containing files to delete
	 */
	public static void deleteFolderContent(final File folder) {
		try {
			FileUtils.cleanDirectory(folder);
		} catch (final Exception fe) {
			// ignore
		}
	}

	/**
	 * This function checks if there is any file in folder provided
	 * @param folder
	 *            Folder containing files to delete
	 * @return TRUE if folder is empty
	 */
	public static boolean checkIfSomeFileExists(final File folder) {
		final File[] files = folder.listFiles();
		if (files != null) { // some JVMs return null for empty dirs
			if (files.length == 0) {
				logger.debug("Found no files");
				return true;
			} else {
				logger.debug("Found #{} files", files.length);
				return false;
			}
		} else {
			return true;
		}

	}

	/**
	 * Method that stores a class represented by the provided array of bytes with a certain name into a specific directory
	 * @param targetDirectoryPath
	 *            the directory path
	 * @param className
	 *            the file name for the class
	 * @param bs
	 *            Bytecode of the class to store
	 * @return Path if it succeeds, null otherwise
	 */
	public static String storeClass(final String targetDirectoryPath,
			final String className, final byte[] bs) {
		try {
			String filePath = targetDirectoryPath;
			if (className.contains(".")) {
				final String[] packages = className.split("\\.");
				String actualDir = targetDirectoryPath;
				// Take into account that if no package is defined the array
				// must be "className, '.class'"
				for (int i = 0; i < packages.length - 2; ++i) {
					actualDir += "/" + packages[i];
				}
				new File(actualDir).mkdirs();
				filePath = actualDir + "/" + packages[packages.length - 2] + "." + packages[packages.length - 1];
			} else {
				filePath += className;
			}

			// String filename = fileName.substring(stubName.lastIndexOf(".") +
			// 1, stubName.length());

			final Path inputPath = Paths.get(filePath).normalize();
			final Path fullPath = inputPath.toAbsolutePath();
			final FileOutputStream fos = new FileOutputStream(fullPath.toString());
			fos.write(bs);
			fos.flush();
			fos.close();
			return filePath;
		} catch (final FileNotFoundException e) {
			logger.error("FileNotFoundException in storeClass", e);
		} catch (final IOException e) {
			logger.error("IOException in storeClass", e);
		}
		return null;
	}

	/**
	 * Copy folder
	 * @param src
	 *            Source folder
	 * @param dest
	 *            Dest folder
	 * @throws IOException
	 *             if some exception occurs
	 */
	public static void copyFolder(final File src, final File dest)
			throws IOException {

		if (src.isDirectory()) {
			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				logger.debug("Directory copied from {} to {}", src, dest);
			}

			// list all the directory contents
			final String[] files = src.list();
			for (final String file : files) {
				// construct the src and dest file structure
				final File srcFile = new File(src, file);
				final File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			final InputStream in = new FileInputStream(src);
			final OutputStream out = new FileOutputStream(dest);

			// CHECKSTYLE:OFF
			final byte[] buffer = new byte[1024];
			// CHECKSTYLE:ON

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			logger.debug("File copied from {} to {}", src, dest);
		}
	}

	/**
	 * Get all classes in package
	 * @param scannedPackage
	 *            Package being scanned
	 * @return List of classes in package
	 */
	public static List<Class<?>> find(final String scannedPackage) {
		final String scannedPath = scannedPackage.replace(".", "/");
		final URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		if (scannedUrl == null) {
			throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
		}
		final File scannedDir = new File(scannedUrl.getFile());
		final List<Class<?>> classes = new ArrayList<>();
		for (final File file : scannedDir.listFiles()) {
			classes.addAll(find(file, scannedPackage));
		}
		return classes;
	}

	/**
	 * Get all classes in package provided
	 * @param file
	 *            Directory or class to check
	 * @param scannedPackage
	 *            package being scanned
	 * @return List of classes in package
	 */
	private static List<Class<?>> find(final File file, final String scannedPackage) {
		final List<Class<?>> classes = new ArrayList<>();
		final String resource = scannedPackage + "." + file.getName();
		if (file.isDirectory()) {
			for (final File child : file.listFiles()) {
				classes.addAll(find(child, resource));
			}
		} else if (resource.endsWith(".class")) {
			final int endIndex = resource.length() - ".class".length();
			final String className = resource.substring(0, endIndex);
			try {
				classes.add(Class.forName(className));
			} catch (final ClassNotFoundException ignore) {
			}
		}
		return classes;
	}

	/**
	 * Apply aspects
	 * @param binPath
	 *            Path of classes to be weaved
	 * @param aspectpath
	 *            Aspects path
	 * @param classPath
	 *            Class path (aspects are not applied here)
	 * @param destPath
	 *            Destination path of the class with aspects
	 */
	public static void weaveAspects(final String binPath, final String aspectpath,
			final String[] classPath, final String destPath) {
		try {
			char classPathToken = ':';
			final String opSystem = System.getProperty("os.name").toLowerCase();
			String app = Configuration.Flags.ASPECTS_HOME.getStringValue() + "ajc";
			final boolean isWindows = opSystem.contains("win");
			if (isWindows) {
				classPathToken = ';';
				app = "ajc.bat";
			}

			String javaClassPath = "";
			for (final String curPath : classPath) {
				javaClassPath += curPath + classPathToken;
			}

			if (Configuration.Flags.PRINT_ASPECTS_INFO.getBooleanValue()) {
				System.out.println("** Weaving aspects... ** ");
				System.out.println("AJC> AspectJ Sourceroots "
						+ "(Find and build all .java or .aj source files under any directory listed in DirPaths. "
						+ "DirPaths, like classpath, is a single argument containing a list of paths to directories, delimited "
						+ "by the platform- specific classpath delimiter) ");
				System.out.println("AJC> --- " + aspectpath);
				System.out.println("AJC> --- " + "Contents of Path: ");
				final File folder = new File(aspectpath);
				final File[] files = folder.listFiles();
				for (final File f : files) {
					System.out.println("AJC> ------- " + f.getName());
				}

				System.out.println("AJC> AspectJ ClassPath (Specify where to find user class files): ");
				for (final String curPath : classPath) {
					System.out.println("AJC> --- " + curPath);
				}
				System.out.println("AJC> AspectJ In Path "
						+ "(Accept as source bytecode any .class files in the .jar files or directories on Path): ");
				System.out.println("AJC> --- " + binPath);
				System.out.println("AJC> AspectJ Dest Path: " + destPath);
			}
			final String[] command = { app, "-nowarn", "-Xlint:ignore",
					// "-g",
					"-sourceroots", aspectpath, "-cp", javaClassPath,
					"-inpath", binPath, "-d", destPath };
			// String[] command = {app, "-sourceroots", aspectpath, "-cp", javaClassPath,
			// "-inpath", binPath, "-d", destPath};
			executeCommand(command, isWindows, Configuration.Flags.PRINT_ASPECTS_INFO.getBooleanValue(), null, null, null);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute command
	 * @param command
	 *            Command to execute
	 * @param isWindows
	 *            Indicates if windows or Linux
	 * @param printOutput
	 *            Print output
	 * @param redirectedOutput
	 *            Path to file in which to store output of the command
	 * @param redirectedInput TODO
	 * @param envVariables
	 *            Environment variables for the command.
	 */
	public static String executeCommand(final String[] command, final boolean isWindows,
			final boolean printOutput, final Path redirectedOutput, final String redirectedInput, final Map<String, String> envVariables) {
		String result = null;
		try {
			logger.debug("$> Running command " + Arrays.toString(command));

			final ProcessBuilder pb = new ProcessBuilder(command);
			if (redirectedOutput != null) {
			    pb.redirectOutput(Redirect.to(redirectedOutput.toFile()));
			}
			
			// set environment variables
			if (envVariables != null) {
				pb.environment().putAll(envVariables);
			}

			final Process process = pb.start();
			
			if (redirectedInput != null) {
				final OutputStream os = process.getOutputStream();
		        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		        // this will send the string to command
		        bw.write(redirectedInput);
		        bw.close();
			}
	        final StringBuilder outputBuilder = new StringBuilder();
			final StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR", printOutput, outputBuilder);
			final StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "INFO", printOutput, outputBuilder);

			// kick them off
			errorGobbler.start();
			outputGobbler.start();


			// any error???
			final int exitVal = process.waitFor();
			if (printOutput) {
				logger.debug("ExitValue: " + exitVal);
				if (exitVal != 0) { 
					System.err.println("Command failed");
				}
			}
			errorGobbler.join();
			outputGobbler.join();
			result = outputBuilder.toString();

		} catch (final Throwable e) {
			logger.debug("Exception in executeCommand", e);
		}

		return result;
	}

	/**
	 * Get source files except ones in package provided
	 * @param srcDirectory
	 *            Source directories
	 * @param excludedPackages
	 *            Exclude packages
	 * @return List of files
	 * @throws FileNotFoundException
	 */
	private static ArrayList<File> getSourceFiles(final File srcDirectory, final Set<String> excludedPackages) throws FileNotFoundException {
		final ArrayList<File> sourceFiles = new ArrayList<>();
		if (srcDirectory.isDirectory()) {
			boolean go = true;
			if (excludedPackages != null) {
				for (final String excludedPackage : excludedPackages) {
					if (srcDirectory.getName().equals(excludedPackage)) {
						go = false;
						break;
					}
				}
			}
			if (go) {
				final File[] files = srcDirectory.listFiles();
				if (files.length > 0) {
					for (final File file : files) {
						if (file.isDirectory()) {
							// Loop through its listFiles() recursively.
							sourceFiles.addAll(getSourceFiles(file, excludedPackages));
						} else {
							final String name = file.getName();
							if (name.endsWith(".java")) {
								sourceFiles.add(file);
							}

						}
					}
				}
			}
		}
		return sourceFiles;
	}

	/**
	 * Compile classes
	 * @param srcPath
	 *            Source path
	 * @param destPath
	 *            Destination path
	 * @param classPath
	 *            Class path
	 * @param excludePackages
	 *            Packages to exclude
	 * @throws Exception
	 *             if some exception occurs
	 */
	public static void compileClasses(final String srcPath, final String destPath,
			final String[] classPath, final String excludePackages)
			throws Exception {
		Set<String> excPackages = null;
		if (excludePackages != null) {
			excPackages = new HashSet<>();
			excPackages.add(excludePackages);
		}
		compileClassesInternal(srcPath, destPath, classPath, excPackages);
	}

	/**
	 * Compile classes
	 * @param srcPath
	 *            Source path
	 * @param destPath
	 *            Destination path
	 * @param classPath
	 *            Class path
	 * @param excludePackages
	 *            Packages to exclude
	 * @throws Exception
	 *             if some exception occurs
	 */
	public static void compileClassesExcluding(final String srcPath, final String destPath,
			final String[] classPath, final Set<String> excludePackages)
			throws Exception {
		compileClassesInternal(srcPath, destPath, classPath, excludePackages);
	}

	/**
	 * Compile classes
	 * @param srcPath
	 *            Source path
	 * @param destPath
	 *            Destination path
	 * @param classPath
	 *            Class path
	 * @param excludePackages
	 *            Packages to exclude
	 * @throws Exception
	 *             if some exception occurs
	 */
	private static void compileClassesInternal(final String srcPath, final String destPath,
			final String[] classPath, final Set<String> excludePackages)
			throws Exception {
		char classPathToken = ':';
		final String opSystem = System.getProperty("os.name").toLowerCase();
		if (opSystem.contains("win")) {
			classPathToken = ';';
		}

		Path inputPath = Paths.get(srcPath).normalize();
		final Path fullSrcPath = inputPath.toAbsolutePath();

		inputPath = Paths.get(destPath).normalize();
		final Path fullDestPath = inputPath.toAbsolutePath();

		try {
			deleteFolderContent(fullDestPath.toFile());
		} catch (final Exception e) { 
			//ignore
		}
		
		// create destination
		fullDestPath.toFile().mkdirs();

		
		final File srcDir = new File(fullSrcPath.toString());

		Set<String> exPackage = null;
		if (excludePackages != null) {
			exPackage = excludePackages;
		}

		final File[] srcFiles = getSourceFiles(srcDir, exPackage).toArray(new File[0]);
		// ATTENTION EXCLUDE ALL SOURCE FILES THAT USES JUNIT
		if (srcFiles.length > 0) {
			final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
			final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			String javaClassPath = "";
			for (final String curPath : classPath) {
				inputPath = Paths.get(curPath).normalize();
				final Path fullClassPath = inputPath.toAbsolutePath();
				javaClassPath += fullClassPath.toString() + classPathToken;
			}
			final String[] compileOptions = new String[] { "-d", fullDestPath.toString(),
					"-classpath", javaClassPath };

			final Iterable<String> compilationOptions = Arrays.asList(compileOptions);

			final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(srcFiles);
			final CompilationTask task = compiler.getTask(null, null, diagnostics, compilationOptions, null, compilationUnits);

			task.call();
			for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {

				System.err.println("File name: " + diagnostic.getSource().getName());

				System.err.println(diagnostic.getMessage(null));
				if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
					throw new Exception("Compilation failed");
				}
			}
		}

	}

	/**
	 * Compile class
	 * @param srcPath
	 *            Source path
	 * @param destPath
	 *            Destination path
	 * @param classPath
	 *            Class path
	 * @throws Exception
	 *             if some exception occurs
	 */
	public static void compileClass(final String srcPath, final String destPath,
			final String[] classPath)
			throws Exception {
		char classPathToken = ':';
		final String opSystem = System.getProperty("os.name").toLowerCase();
		if (opSystem.contains("win")) {
			classPathToken = ';';
		}

		String javaClassPath = "";
		for (final String curPath : classPath) {
			javaClassPath += curPath + classPathToken;
		}

		final File[] srcFiles = new File[] { new File(srcPath) };
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		final String[] compileOptions = new String[] { "-d", destPath, "-classpath", javaClassPath };
		final Iterable<String> compilationOptions = Arrays.asList(compileOptions);

		final Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(srcFiles);
		final CompilationTask task = compiler.getTask(null, null, diagnostics, compilationOptions, null, compilationUnits);

		task.call();
		for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
			if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
				System.out.println(diagnostic.getLineNumber());
				System.out.println(diagnostic.getPosition());
				System.out.println(diagnostic.getStartPosition());
				System.out.println(diagnostic.getEndPosition());
				System.out.println(diagnostic.getSource());

				System.out.println(diagnostic.getMessage(null));
				throw new Exception("Compilation failed");
			}
		}
	}

	/**
	 * This function return a list of class names in a source path
	 * @param dir
	 *            Source path to analyze
	 * @param isRoot
	 *            Must be true, used for recursive analysis
	 * @return List of classes found in source path
	 */
	public static ArrayList<String> getClassNamesFromSourcePath(final String dir, final boolean isRoot) {

		final ArrayList<String> classNames = new ArrayList<>();

		final Path inputPath = Paths.get(dir).normalize();
		final Path fullSrcPath = inputPath.toAbsolutePath();
		final File folder = new File(fullSrcPath.toString());

		String classPackage = "";
		if (!isRoot) {
			classPackage = folder.getName() + ".";
		}

		// Check if there is a subfolder
		for (final File file : folder.listFiles()) {

			if (file.isDirectory()) {

				classNames.addAll(getClassNamesFromSourcePath(file.getAbsolutePath(), false));

			} else {
				if (file.getName().endsWith(".java")) {
					final String className = classPackage + file.getName().replace(".java", "");
					classNames.add(className);
				}
			}

		}

		return classNames;

	}

}
