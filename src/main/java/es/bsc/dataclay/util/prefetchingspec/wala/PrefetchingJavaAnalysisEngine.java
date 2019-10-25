
/**
 * @file PrefetchingEJCJavaSourceAnalysisEngine.java
 * 
 * @date Dec 11, 2015
 */
package es.bsc.dataclay.util.prefetchingspec.wala;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;

import com.ibm.wala.cast.java.client.ECJJavaSourceAnalysisEngine;
import com.ibm.wala.cast.java.ipa.callgraph.JavaSourceAnalysisScope;
import com.ibm.wala.classLoader.ClassLoaderFactory;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.JarFileModule;
import com.ibm.wala.classLoader.SourceDirectoryTreeModule;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.config.SetOfClasses;

/**
 * A subclass of Wala's EJCJavaSourceAnalysisEngine specific to the needs of generating the prefetching information.
 * 
 *
 */
public class PrefetchingJavaAnalysisEngine extends ECJJavaSourceAnalysisEngine<InstanceKey> {

	/**
	 * Creats a new PrefetchingEJCJavaSourceAnalysisEngine with the specified parameters.
	 * 
	 * @param sources
	 *            the source directories to add to the engine
	 * @param libs
	 *            the libraries (.jar) to add to the engine
	 * @param exclusionsFile
	 *            the path to the exclusions file to add to the engine
	 */
	public PrefetchingJavaAnalysisEngine(final Collection<String> sources, final List<String> libs, final String exclusionsFile) {
		super();
		setExclusionsFile(exclusionsFile);
		populateScope(sources, libs);
		try {
			buildAnalysisScope();
			setClassHierarchy(buildClassHierarchy());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	} 
	
	@Override
	protected final ClassLoaderFactory getClassLoaderFactory(final SetOfClasses exclusions) {
		return new PrefetchingClassLoaderFactory(exclusions);
	}

	@Override
	protected final Iterable<Entrypoint> makeDefaultEntrypoints(final AnalysisScope scope, final IClassHierarchy cha) {
		if (cha == null) {
			throw new IllegalArgumentException("CHA is null");
		}
		final HashSet<Entrypoint> result = HashSetFactory.make();
		for (final IClass klass : cha) {
			if (klass.getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE)
					&& !klass.isInterface() && !klass.isAbstract()) {
				for (final IMethod m : klass.getAllMethods()) {
					if (m != null && !m.isAbstract()) {
						result.add(new DefaultEntrypoint(m, cha));
					}
				}
			}
		}
		return new Iterable<Entrypoint>() {
			@Override
			public Iterator<Entrypoint> iterator() {
				return result.iterator();
			}
		};
	}

	/**
	 * Populates the scope of the current engine with the given sources and libraries.
	 * 
	 * @param sources
	 *            the sources to add to the engine's scope
	 * @param libs
	 *            the libraries (.jar) to add to the engine's scope
	 */
	private void populateScope(final Collection<String> sources, final List<String> libs) {
		// 1. Add libs as System Modules to the engine
		boolean foundLib = false;
		for (final String lib : libs) {
			final File libFile = new File(lib);
			if (libFile.exists()) {
				foundLib = true;
				try {
					addSystemModule(new JarFileModule(new JarFile(libFile)));
				} catch (final IOException e) {
					//Assert.isTrue(true, e.getMessage());
				}
			}
		}
		assert foundLib : "couldn't find library file from " + libs;

		// 2. Add sources as Source Modules to the engine
		for (final String srcFilePath : sources) {
			final String srcFileName = srcFilePath.substring(srcFilePath.lastIndexOf(File.separator) + 1);
			final File f = new File(srcFilePath);
			//Assert.isTrue(f.exists(), "couldn't find " + srcFilePath);
			if (f.isDirectory()) {
				addSourceModule(new SourceDirectoryTreeModule(f));
			} else {
				addSourceModule(new SourceFileModule(f, srcFileName, null));
			}
		}
	}
}
