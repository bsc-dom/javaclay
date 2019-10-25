
package es.bsc.dataclay.util.prefetchingspec.wala;

import com.ibm.wala.cast.java.loader.JavaSourceLoaderImpl;
import com.ibm.wala.cast.java.translator.jdt.ecj.ECJClassLoaderFactory;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.config.SetOfClasses;

/**
 * Class loader factory specific to prefetching code analysis. Inhertis from Wala's EJC class loader factory.
 * 
 *
 */
public class PrefetchingClassLoaderFactory extends ECJClassLoaderFactory {

	/**
	 * Constructor.
	 * 
	 * @param exclusions
	 * 			Java exclusions.
	 */

	public PrefetchingClassLoaderFactory(final SetOfClasses exclusions) {
		super(exclusions);
	}

	@Override
	protected JavaSourceLoaderImpl makeSourceLoader(final ClassLoaderReference classLoaderReference, 
			final IClassHierarchy cha, final IClassLoader parent) {
		return new PrefetchingSourceLoaderImpl(classLoaderReference, parent, cha, false);
	}
}
