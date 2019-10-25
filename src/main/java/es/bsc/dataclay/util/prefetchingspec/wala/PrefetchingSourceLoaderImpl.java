
package es.bsc.dataclay.util.prefetchingspec.wala;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ibm.wala.cast.ir.translator.AstTranslator;
import com.ibm.wala.cast.ir.translator.AstTranslator.AstLexicalInformation;
import com.ibm.wala.cast.java.loader.JavaSourceLoaderImpl;
import com.ibm.wala.cast.java.loader.Util;
import com.ibm.wala.cast.java.translator.jdt.ecj.ECJSourceLoaderImpl;
import com.ibm.wala.cast.loader.AstMethod.DebuggingInformation;
import com.ibm.wala.cast.loader.AstMethod.Retranslatable;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.CAstQualifier;
import com.ibm.wala.cast.tree.CAstSourcePositionMap;
import com.ibm.wala.cast.tree.CAstType;
import com.ibm.wala.cfg.AbstractCFG;
import com.ibm.wala.cfg.IBasicBlock;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IClassLoader;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeName;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.types.annotations.Annotation;

import es.bsc.dataclay.util.prefetchingspec.analysisscopes.AnalysisScope;
import es.bsc.dataclay.util.prefetchingspec.analysisscopes.MethodLevelAnalysisScope;

/**
 * Source loader specific to prefetching code analysis. Inherits from Wala's EJC source loader.
 * 
 *
 */
public class PrefetchingSourceLoaderImpl extends ECJSourceLoaderImpl {

	/**
	 * @param loaderRef
	 *            Class loader reference
	 * @param parent
	 *            Parent class loader reference
	 * @param exclusions
	 *            Java exclusions
	 * @param cha
	 *            Class hierarchy of application to load
	 * @throws IOException
	 *             Thrown from parent constructor
	 */
	public PrefetchingSourceLoaderImpl(final ClassLoaderReference loaderRef, final IClassLoader parent,
			final IClassHierarchy cha) throws IOException {
		this(loaderRef, parent, cha, false);
	}

	/**
	 * @param loaderRef
	 *            Class loader reference
	 * @param parent
	 *            Parent class loader reference
	 * @param exclusions
	 *            Java exclusions
	 * @param cha
	 *            Class hierarchy of application to load
	 * @param dump
	 *            Flag passed to parent constructor
	 * @throws IOException
	 *             Thrown from parent constructor
	 */
	public PrefetchingSourceLoaderImpl(final ClassLoaderReference loaderRef, final IClassLoader parent,
			final IClassHierarchy cha, final boolean dump) {
		super(loaderRef, parent, cha, dump);
	}

	@Override
	public IClass defineType(final CAstEntity type, final String typeName, final CAstEntity owner) {
		final Collection<TypeName> superTypeNames = new ArrayList<TypeName>();
		for (Iterator<?> superTypes = type.getType().getSupertypes().iterator(); superTypes.hasNext();) {
			superTypeNames.add(toWALATypeName(((CAstType) superTypes.next())));
		}

		final PrefetchingJavaClass javaClass = new PrefetchingJavaClass(typeName, superTypeNames, type.getPosition(),
				type.getQualifiers(), this,
				(owner != null) ? (PrefetchingJavaClass) fTypeMap.get(owner) : (PrefetchingJavaClass) null,
				getAnnotations(type));

		if (getParent().lookupClass(javaClass.getName()) != null) {
			return null;
		}

		fTypeMap.put(type, javaClass);
		loadedClasses.put(javaClass.getName(), javaClass);
		return javaClass;
	}

	@Override
	public void defineFunction(final CAstEntity n, final IClass owner, final AbstractCFG<?,?> cfg, final SymbolTable symtab,
			final boolean hasCatchBlock, final Map<IBasicBlock<SSAInstruction>, TypeReference[]> caughtTypes, final boolean hasMonitorOp,
			final AstLexicalInformation lexicalInfo, final DebuggingInformation debugInfo) {
		((PrefetchingJavaClass) owner).addMethod(n, owner, cfg, symtab, hasCatchBlock, caughtTypes, hasMonitorOp,
				lexicalInfo, debugInfo);
	}

	@Override
	public final void defineAbstractFunction(final CAstEntity n, final IClass owner) {
		((PrefetchingJavaClass) owner).addMethod(n, owner);
	}

	/**
	 * WALA representation of a Java class residing in a source file.
	 * 
	 */
	public class PrefetchingJavaClass extends JavaSourceLoaderImpl.JavaClass {

		/**
		 * 
		 * @param typeName
		 *            Name of class
		 * @param superTypeNames
		 *            Name of super classes
		 * @param position
		 *            Position in source file
		 * @param qualifiers
		 *            Class qualifiers
		 * @param loader
		 *            Class loader
		 * @param enclosingClass
		 *            Enclosing class
		 * @param annotations
		 *            Class annotations
		 */
		public PrefetchingJavaClass(final String typeName, final Collection<TypeName> superTypeNames,
				final CAstSourcePositionMap.Position position, final Collection<CAstQualifier> qualifiers,
				final PrefetchingSourceLoaderImpl loader, final IClass enclosingClass,
				final Collection<Annotation> annotations) {
			super(typeName, superTypeNames, position, qualifiers, loader, enclosingClass, annotations);
		}

		/**
		 * Fuction to add a concrete method to the owner class.
		 * 
		 * @param methodEntity
		 *            CAst Entity
		 * @param owner
		 *            Owner class
		 * @param cfg
		 *            CFG
		 * @param symtab
		 *            Symbol Table
		 * @param hasCatchBlock
		 *            Flag to specifiy if method has catch block
		 * @param caughtTypes
		 *            Types of caught exceptions
		 * @param hasMonitorOp
		 *            Flag to specify if method has operation monitor
		 * @param lexicalInfo
		 *            Lexical info
		 * @param debugInfo
		 *            Debug info
		 */
		protected void addMethod(final CAstEntity methodEntity, final IClass owner, final AbstractCFG<?, ?> cfg,
				final SymbolTable symtab, final boolean hasCatchBlock,
				final Map<IBasicBlock<SSAInstruction>, TypeReference[]> caughtTypes, final boolean hasMonitorOp,
				final AstLexicalInformation lexicalInfo, final DebuggingInformation debugInfo) {
			declaredMethods.put(Util.methodEntityToSelector(methodEntity),
					new PrefetchingConcreteJavaMethod(methodEntity, owner, cfg, symtab, hasCatchBlock, caughtTypes,
							hasMonitorOp, lexicalInfo, debugInfo));
		}

		/**
		 * Function to add an abstract method to the owner class.
		 * 
		 * @param methodEntity
		 *            CAst entity of the method
		 * @param owner
		 *            Owner class of the method
		 */
		protected void addMethod(final CAstEntity methodEntity, final IClass owner) {
			declaredMethods.put(Util.methodEntityToSelector(methodEntity),
					new PrefetchingAbstractJavaMethod(methodEntity, owner));
		}
	}

	/**
	 * Interface create to generalize abstract and concrete java methods specific to prefetching code analysis.
	 * 
	 *
	 */
	public interface PrefetchingJavaMethod extends IMethod {

		/**
		 * Get PrefetchingJavaMetho::analysisScope.
		 * 
		 * @return analysisScope
		 */
		MethodLevelAnalysisScope getAnalysisScope();
	}

	/**
	 * DOMO representation of a concrete method (which has a body) on a Java. type that resides in a source file.
	 * 
	 */
	public class PrefetchingConcreteJavaMethod extends JavaSourceLoaderImpl.ConcreteJavaMethod
			implements Retranslatable, PrefetchingJavaMethod {

		/** CAst entity of the method. **/
		private CAstEntity methodEntity;

		/** Analysis scope of the method. **/
		private MethodLevelAnalysisScope analysisScope;
		
		private Map<SSAInstruction, AnalysisScope> scopePerInstr;

		/**
		 * @param newMethodEntity
		 *            CAst Entity
		 * @param owner
		 *            Owner class
		 * @param cfg
		 *            CFG
		 * @param symtab
		 *            Symbol Table
		 * @param hasCatchBlock
		 *            Flag to specifiy if method has catch block
		 * @param caughtTypes
		 *            Types of caught exceptions
		 * @param hasMonitorOp
		 *            Flag to specify if method has operation monitor
		 * @param lexicalInfo
		 *            Lexical info
		 * @param debugInfo
		 *            Debug info
		 */
		public PrefetchingConcreteJavaMethod(final CAstEntity newMethodEntity, final IClass owner, final AbstractCFG<?, ?> cfg,
				final SymbolTable symtab, final boolean hasCatchBlock,
				final Map<IBasicBlock<SSAInstruction>, TypeReference[]> caughtTypes, final boolean hasMonitorOp,
				final AstLexicalInformation lexicalInfo, final DebuggingInformation debugInfo) {
			super(newMethodEntity, owner, cfg, symtab, hasCatchBlock, caughtTypes, hasMonitorOp, lexicalInfo, debugInfo);
			this.methodEntity = newMethodEntity;
			this.analysisScope = new MethodLevelAnalysisScope(0, cfg.getInstructions().length, getSignature());
			this.scopePerInstr = new HashMap<SSAInstruction, AnalysisScope>();
		}

		@Override
		public void retranslate(final AstTranslator xlator) {
			throw new UnsupportedOperationException(
					"Retranslation of PrefetchingConcreteJavaMethod is not currently supported.");
		}

		@Override
		public CAstEntity getEntity() {
			return this.methodEntity;
		}

		@Override
		public MethodLevelAnalysisScope getAnalysisScope() {
			return analysisScope;
		}
		
		public AnalysisScope getInstrScope(SSAInstruction instr) {
			return scopePerInstr.get(instr);
		}
		
		public void putInstrScope(SSAInstruction instr, AnalysisScope scope) {
			scopePerInstr.put(instr, scope);
		}
	}

	/**
	 * DOMO representation of an abstract (body-less) method on a Java type that resides in a source file.
	 * 
	 */
	public class PrefetchingAbstractJavaMethod extends JavaSourceLoaderImpl.AbstractJavaMethod
			implements PrefetchingJavaMethod {

		/** Analysis scope of the method. **/
		private MethodLevelAnalysisScope analysisScope;

		/**
		 * @param methodEntity
		 *            CAst entity of the method
		 * @param owner
		 *            Owner class of the method
		 */
		public PrefetchingAbstractJavaMethod(final CAstEntity methodEntity, final IClass owner) {
			super(methodEntity, owner);
			this.analysisScope = new MethodLevelAnalysisScope(0, 0, getSignature());
		}

		@Override
		public MethodLevelAnalysisScope getAnalysisScope() {
			return analysisScope;
		}
	}
}
