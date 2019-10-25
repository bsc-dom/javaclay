
package es.bsc.dataclay.logic.classmgr;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.ClientRuntime;
import es.bsc.dataclay.logic.classmgr.bytecode.java.DataClayClassTransformer;
import es.bsc.dataclay.logic.classmgr.bytecode.java.headers.ClassHeaderTransformer;
import es.bsc.dataclay.logic.classmgr.bytecode.java.merger.DataClayClassMerger;
import es.bsc.dataclay.logic.classmgr.bytecode.java.writer.DataClayClassWriter;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.buffer.DirectNettyBuffer;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.stubs.ImplementationStubInfo;
import es.bsc.dataclay.util.management.stubs.PropertyStubInfo;
import es.bsc.dataclay.util.management.stubs.StubClassLoader;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.tools.java.JavaSpecGenerator;

@Ignore
public class ClassManagerByteCodeTest {

	/** Data test case source path. */
	private static final String TEST_CLASSES_PATH = System.getProperty("user.dir") + "/test_bytecode/";

	/** Data test case source path. */
	private static final String TEST_DATA_SRC_PATH = System.getProperty("user.dir") + "/test_classes/notunit/data/registrator/src/";

	/** Data test case bin path. */
	private static final String TEST_DATA_DEST_PATH = System.getProperty("user.dir") + "/test_classes/notunit/data/registrator/bin/";

	/** DataClayCollections test case source path. */
	private static final String COLLECTIONS_SRC_PATH = System.getProperty("user.dir") + "/install_classes/src/";

	/** DataClayCollections test case bin path. */
	private static final String COLLECTIONS_DEST_PATH = System.getProperty("user.dir") + "/install_classes/bin/";

	/** User class loader. */
	private static URLClassLoader userClassLoader;

	/** Class path. */
	private static String finalClassPath;

	/** Destination path. */
	private static String curDestPath;

	/** Namespace name. */
	private static final String NAMESPACE_NAME = "MyNamespace";

	@After
	public void after() {
		// Clean folder
		final Path filePath = Paths.get(curDestPath).normalize();
		final File f = new File(filePath.toAbsolutePath().toString());
		FileAndAspectsUtils.deleteFolderContent(f);

		// Delete folder
		/*
		 * Path stubsPath = Paths.get(TEST_CLASSES_PATH).normalize(); File stubsFile = new File(stubsPath.toAbsolutePath().toString());
		 * FileAndAspectsUtils.deleteFolderContent(stubsFile); stubsFile.delete();
		 */
	}

	@Before
	public void before() {
		// Create a dump library
		final ClientRuntime userLib = new ClientRuntime();
		DataClayObject.setLib(userLib);
	}

	private Set<String> compileAndSetClassLoader(final String srcPath, final String destPath) throws Exception {

		final Path stubsPath = Paths.get(TEST_CLASSES_PATH).normalize();
		final File stubsFile = new File(stubsPath.toAbsolutePath().toString());
		stubsFile.mkdirs();

		curDestPath = destPath;

		// Use a map to avoid repeating paths
		final HashMap<String, URL> classUrls = new HashMap<>();
		if (destPath != null) {
			final Path path = Paths.get(destPath).normalize();
			final String absolutePath = path.toAbsolutePath().toString();
			classUrls.put(absolutePath, (new File(absolutePath)).toURI().toURL());
		}
		final Path path = Paths.get(System.getProperty("user.dir") + File.separatorChar + "bin");
		final String absolutePath = path.toAbsolutePath().toString();
		classUrls.put(absolutePath, (new File(absolutePath)).toURI().toURL());

		final URL[] finalUrls = new URL[classUrls.size()];
		finalClassPath = "";
		int idx = 0;
		for (final Entry<String, URL> urlEntry : classUrls.entrySet()) {
			if (idx != 0) {
				finalClassPath += ":";
			}
			finalUrls[idx] = urlEntry.getValue();
			finalClassPath += urlEntry.getKey();
			idx++;
		}

		// COMPILE
		final String[] cp = classUrls.keySet().toArray(new String[] {});

		// Compile
		final String absCompiledpath = Paths.get(destPath).normalize().toAbsolutePath().toString();
		final String absSrcPath = Paths.get(srcPath).normalize().toAbsolutePath().toString();
		FileAndAspectsUtils.compileClasses(absSrcPath, absCompiledpath, cp, null);

		userClassLoader = new URLClassLoader(finalUrls, DataClayObject.class.getClassLoader());
		Thread.currentThread().setContextClassLoader(userClassLoader);

		final Set<String> classes = new HashSet<>();
		StubClassLoader.getClasses(destPath, new File(destPath), classes, ".class", null);
		return classes;
	}

	private ClassLoader getClassLoader(final String cpPath) throws Exception {
		// Load class in new class loader
		final HashMap<String, URL> classUrls = new HashMap<>();
		if (cpPath != null) {
			final Path path = Paths.get(cpPath).normalize();
			final String absolutePath = path.toAbsolutePath().toString();
			classUrls.put(absolutePath, (new File(absolutePath)).toURI().toURL());
		}
		final Path path = Paths.get(System.getProperty("user.dir") + File.separatorChar + "bin");
		final String absolutePath = path.toAbsolutePath().toString();
		classUrls.put(absolutePath, (new File(absolutePath)).toURI().toURL());

		final URL[] finalUrls = new URL[classUrls.size()];
		int idx = 0;
		for (final Entry<String, URL> urlEntry : classUrls.entrySet()) {
			finalUrls[idx] = urlEntry.getValue();
			idx++;
		}

		final URLClassLoader newClassLoader = new URLClassLoader(finalUrls);
		return newClassLoader;
	}

	@Test
	public void testAnalyzeClass() throws Exception {
		final Set<String> classes = this.compileAndSetClassLoader(TEST_DATA_SRC_PATH, TEST_DATA_DEST_PATH);
		for (final String classToTest : classes) {
			final JavaSpecGenerator specGen2 = new JavaSpecGenerator(finalClassPath);
			specGen2.generateMetaClassSpecForRegisterClass("MyNamespace", classToTest);
		}
	}

	@Test
	public void testAnalyzeClassDataClayCollections() throws Exception {
		final Set<String> classes = this.compileAndSetClassLoader(COLLECTIONS_SRC_PATH, COLLECTIONS_DEST_PATH);
		for (final String classToTest : classes) {
			final JavaSpecGenerator specGen2 = new JavaSpecGenerator(finalClassPath);
			assertTrue(!specGen2.generateMetaClassSpecForRegisterClass("MyNamespace", classToTest).isEmpty());
		}

	}

	@Test
	public void testHeaderTransformer() throws Exception {
		this.compileAndSetClassLoader(TEST_DATA_SRC_PATH, TEST_DATA_DEST_PATH);
		// Load class
		final String originalClassName = "TestClassK";
		final Class<?> clazz = userClassLoader.loadClass(originalClassName);

		// =============== REMAP ============== //

		// Add remapper for changing names
		final Map<String, String> renaming = new HashMap<>();
		final SimpleRemapper remapper = new SimpleRemapper(renaming);
		renaming.put("TestClassK.xArray", "jajajajajajajajaj"); // Rename field
		renaming.put("TestClassK.setxArray([LTestClassX;)V", "setxArray$$1"); // Rename method

		// Get resource bytes
		final String resourceName = "/" + originalClassName.replaceAll("\\.", "/") + ".class"; // Any better way?
		final InputStream classInputStream = clazz.getResourceAsStream(resourceName);
		final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		final ClassRemapper classRemapper = new ClassRemapper(classWriter, remapper);
		final ClassHeaderTransformer headerTransformer = new ClassHeaderTransformer(
				classRemapper, originalClassName, false, true);
		final ClassReader classReader = new ClassReader(classInputStream);
		classReader.accept(headerTransformer, 0);

		final byte[] newByteCode = classWriter.toByteArray();
		final ClassReader newClassReader = new ClassReader(newByteCode);

		// Print class
		final TraceClassVisitor traceVisitor = new TraceClassVisitor(new PrintWriter(System.out));
		final CheckClassAdapter checker = new CheckClassAdapter(traceVisitor);
		newClassReader.accept(checker, 0);

	}

	@Test
	public void testMergeEnrichment() throws Exception {
		this.compileAndSetClassLoader(TEST_DATA_SRC_PATH, TEST_DATA_DEST_PATH);
		// Load class
		final String originalClassName = "TestClassK";
		final String enrichmentClassName = "TestClassB";
		final Class<?> clazz = userClassLoader.loadClass(originalClassName);
		final Class<?> enrichmentClazz = userClassLoader.loadClass(originalClassName);

		// Get resource bytes
		final String resourceName = "/" + originalClassName.replaceAll("\\.", "/") + ".class"; // Any better way?
		final InputStream classInputStream = clazz.getResourceAsStream(resourceName);
		final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		final ClassHeaderTransformer headerTransformer = new ClassHeaderTransformer(classWriter,
				originalClassName, false, true);
		final ClassReader classReader = new ClassReader(classInputStream);
		classReader.accept(headerTransformer, 0);
		final byte[] origByteCode = classWriter.toByteArray();

		// Get enrichment bytes
		final String enrichResource = "/" + enrichmentClassName.replaceAll("\\.", "/") + ".class"; // Any better way?
		final InputStream enrichClassInputStream = enrichmentClazz.getResourceAsStream(enrichResource);
		final ClassWriter enrichWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		final ClassReader enrichReader = new ClassReader(enrichClassInputStream);
		enrichReader.accept(enrichWriter, 0);
		final byte[] enrichmentByteCode = enrichWriter.toByteArray();

		// ========================== RENAME ================================== //

		// Modify enrichment method names if they already exists in original class
		final ClassReader origReader = new ClassReader(origByteCode);
		final ClassNode classNode = new ClassNode();
		origReader.accept(classNode, 0);
		// Get all methods
		final Set<String> methodsNamesAndSignatures = new HashSet<>();
		for (final Object method : classNode.methods) {
			final MethodNode mn = (MethodNode) method;
			String methodDesc = mn.desc;
			// Ignore return type in signatures
			methodDesc = methodDesc.substring(0, methodDesc.lastIndexOf(")") + 1);
			methodsNamesAndSignatures.add(mn.name + methodDesc);
		}

		// Read enrichment and rename
		final ClassReader enrichmentRenameReader = new ClassReader(enrichmentByteCode);
		final ClassNode enrichmentRenameNode = new ClassNode();
		enrichmentRenameReader.accept(enrichmentRenameNode, 0);

		// Modify all methods
		final Map<String, String> renaming = new HashMap<>();
		renaming.put(enrichmentClassName, originalClassName);
		for (final Object method : enrichmentRenameNode.methods) {
			final MethodNode mn = (MethodNode) method;

			// Modify name of new methods if already exists
			String methodName = mn.name;
			String methodDesc = mn.desc;

			if (methodName.equals("<init>") || methodName.equals("<clinit>")) {
				continue;
			}

			if ((mn.access & Opcodes.ACC_SYNTHETIC) != 0) {
				continue;
			}

			// Ignore return type in signatures
			methodDesc = methodDesc.substring(0, methodDesc.lastIndexOf(")") + 1);
			if (methodsNamesAndSignatures.contains(methodName + methodDesc)) {
				if (methodName.contains("$$")) {
					final String curNumberStr = methodName.substring(methodName.lastIndexOf("$") + 1,
							methodName.length());
					final Integer curNumber = Integer.valueOf(curNumberStr);
					methodName = methodName.replace("$$", String.valueOf(curNumber + 1));
				} else {
					methodName = methodName + "$$1";
				}
				// Add to renaming map
				renaming.put(enrichmentClassName + "." + mn.name + mn.desc, methodName);
				// Add to included method names and signature
				methodsNamesAndSignatures.add(methodName + methodDesc);
			}
		}

		// ========================== MERGE ================================== //
		// Merge
		final ClassReader enrichmentReader = new ClassReader(enrichmentByteCode);
		final ClassNode enrichmentNode = new ClassNode();
		enrichmentReader.accept(enrichmentNode, ClassReader.EXPAND_FRAMES); // Add enrichment bytecode to class node
		final ClassReader mergeReader = new ClassReader(origByteCode);
		final ClassWriter mergeWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		final ClassRemapper classRemapper = new ClassRemapper(mergeWriter, new SimpleRemapper(renaming));
		final ClassVisitor mergeVisitor = new DataClayClassMerger(classRemapper, enrichmentNode);
		mergeReader.accept(mergeVisitor, ClassReader.EXPAND_FRAMES);
		final byte[] newByteCode = mergeWriter.toByteArray();

		// Print class
		final ClassReader newClassReader = new ClassReader(newByteCode);
		final TraceClassVisitor traceVisitor = new TraceClassVisitor(new PrintWriter(System.out));
		final CheckClassAdapter checker = new CheckClassAdapter(traceVisitor);
		newClassReader.accept(checker, ClassReader.EXPAND_FRAMES);
	}

	private void testClasses(final Set<String> classes, final boolean isExec) throws Exception {

		final Set<String> finalClasses = new HashSet<>(classes);

		// FIRST GENERATE METACLASS OF ALL CLASSES
		final Map<String, MetaClass> analyzedClasses = new HashMap<>();
		final Map<String, byte[]> allByteCodes = new HashMap<>();
		for (final String classToTest : classes) {
			if (analyzedClasses.containsKey(classToTest)) {
				continue;
			}
			System.err.println("Generating for " + classToTest);
			final JavaSpecGenerator specGen2 = new JavaSpecGenerator(finalClassPath);
			final Map<String, MetaClass> result = specGen2.generateMetaClassSpecForRegisterClass(NAMESPACE_NAME, classToTest);
			analyzedClasses.putAll(result);
		}

		// Now generate stub/execution class
		for (final String classToTest : classes) {
			final MetaClass mClass = analyzedClasses.get(classToTest);
			mClass.setDataClayID(new MetaClassID());
			final AccountID accountID = new AccountID();
			final NamespaceID namespaceID = new NamespaceID();

			// Add everything in stub info
			final Map<String, ImplementationStubInfo> implsInStub = new HashMap<>();
			final Map<String, ImplementationStubInfo> implsInStubByID = new HashMap<>();
			final Map<String, PropertyStubInfo> propsInStub = new HashMap<>();
			for (final Property prop : mClass.getProperties()) {
				prop.setDataClayID(new PropertyID());
				prop.setGetterImplementationID(new ImplementationID());
				prop.setSetterImplementationID(new ImplementationID());
				final PropertyStubInfo propInfo = new PropertyStubInfo(prop.getDataClayID(), prop.getName(),
						prop.getType(), prop.getGetterOperationID(),
						prop.getSetterOperationID(), prop.getNamespace(),
						prop.getNamespaceID(), prop.getBeforeUpdate(), prop.getAfterUpdate(), prop.getInMaster());
				propsInStub.put(prop.getName(), propInfo);
			}

			for (final Operation op : mClass.getOperations()) {
				op.setDataClayID(new OperationID());
				for (final Implementation impl : op.getImplementations()) {
					impl.setDataClayID(new ImplementationID());
					impl.setOperationID(op.getDataClayID());
					final ImplementationStubInfo implStubInfo = new ImplementationStubInfo(impl.getNamespace(),
							impl.getClassName(), impl.getOpNameAndDescriptor(), op.getParams(), op.getParamsOrder(),
							op.getReturnType(), op.getDataClayID(), impl.getDataClayID(), impl.getDataClayID(),
							null, null, null, null, 0);
					implsInStub.put(impl.getOpNameAndDescriptor(), implStubInfo);
					implsInStubByID.put(implStubInfo.getLocalImplID().toString(), implStubInfo);
					implsInStubByID.put(implStubInfo.getRemoteImplID().toString(), implStubInfo);
				}
			}

			String superClassName = null;
			if (mClass.getParentType() != null) {
				superClassName = mClass.getParentType().getTypeName();
			}
			final StubInfo stubInfo = new StubInfo(mClass.getNamespace(), mClass.getName(),
					superClassName,
					accountID, mClass.getDataClayID(), namespaceID, implsInStubByID, implsInStub,
					propsInStub, null, null);

			// Prepare includes
			final Map<String, MetaClass> includes = new HashMap<>();
			final Map<String, String> renaming = new HashMap<>();
			if (isExec) {
				for (final MetaClass analyzedClass : analyzedClasses.values()) {
					includes.put(analyzedClass.getNamespace() + "." + analyzedClass.getName(), analyzedClass);
					renaming.put(Reflector.getInternalNameFromTypeName(analyzedClass.getName()),
							Reflector.getInternalNameFromTypeName(analyzedClass.getNamespace()
									+ "." + analyzedClass.getName()));
				}
			} else {
				includes.putAll(analyzedClasses);
			}

			final DataClayClassWriter classWriter = new DataClayClassWriter(ClassWriter.COMPUTE_FRAMES, null,
					includes, isExec);
			ClassVisitor visitor = classWriter;
			if (isExec) {
				// Add renaming
				visitor = new ClassRemapper(classWriter, new SimpleRemapper(renaming));
			}
			// Write new bytecode of stub
			final DataClayClassTransformer stubClassVisitor = new DataClayClassTransformer(visitor, mClass,
					stubInfo, isExec);
			final String classDesc = Reflector.getDescriptorFromTypeName(mClass.getName());

			final ClassHeaderTransformer headerTransformer = new ClassHeaderTransformer(stubClassVisitor,
					classDesc, isExec, mClass.getParentType() == null);
			final ClassReader classReader = new ClassReader(mClass.getJavaClassInfo().getClassByteCode());
			classReader.accept(headerTransformer, ClassReader.EXPAND_FRAMES);
			final byte[] newByteCode = classWriter.toByteArray();

			// Print class
			final ClassReader newClassReader = new ClassReader(newByteCode);
			final FileWriter file = new FileWriter(TEST_CLASSES_PATH
					+ File.separatorChar + classToTest + "_ByteCode.txt");
			final TraceClassVisitor traceVisitor = new TraceClassVisitor(new PrintWriter(file));
			newClassReader.accept(traceVisitor, ClassReader.EXPAND_FRAMES);
			allByteCodes.put(classToTest, newByteCode);

			// store class
			if (isExec) {
				FileAndAspectsUtils.storeClass(TEST_CLASSES_PATH, NAMESPACE_NAME
						+ "." + classToTest + ".class", newByteCode);
			} else {
				FileAndAspectsUtils.storeClass(TEST_CLASSES_PATH, classToTest + ".class", newByteCode);
			}

		}

		// Verify them
		final ClassLoader newClassLoader = getClassLoader(TEST_CLASSES_PATH);
		for (final String classToTest : finalClasses) {
			System.out.println("Verifying " + classToTest);
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			CheckClassAdapter.verify(new ClassReader(allByteCodes.get(classToTest)), newClassLoader, false, pw);
			assertTrue(sw.toString(), sw.toString().length() == 0);
		}

	}

	private void testClassesWithJVMVerifier(final Set<String> classes, final boolean isExec) throws Exception {
		final ClassLoader newClassLoader = getClassLoader(TEST_CLASSES_PATH);
		for (final String className : classes) {

			String classToTest = className;
			if (isExec) {
				classToTest = NAMESPACE_NAME + "." + className;
			}

			System.err.println("** Loading class " + classToTest);
			final Class<?> stubClass = newClassLoader.loadClass(classToTest);

			// Instance it if not abstract
			if (Modifier.isAbstract(stubClass.getModifiers())) {
				continue;
			}

			DataClayObject stubInstance = null;
			try {
				stubInstance = (DataClayObject) stubClass.getConstructor(ObjectID.class)
						.newInstance(new ObjectID());
			} catch (final Exception e) {
				e.printStackTrace();
				throw e;
			}

			try {
				// Call serialize

				final DataClayByteBuffer byteBuffer = new DirectNettyBuffer();
				final IdentityHashMap<Object, Integer> curSerializedObjs = new IdentityHashMap<>();
				final List<DataClayObject> pendingObjs = new LinkedList<>();
				final ListIterator<DataClayObject> it = pendingObjs.listIterator();
				final ReferenceCounting referenceCounting = new ReferenceCounting();
				stubInstance.serialize(byteBuffer, false, null, curSerializedObjs, it, referenceCounting);

				// Call deserialize
				final Map<Integer, Object> curDeserializedJavaObj = new HashMap<>();
				stubInstance.deserialize(byteBuffer, null,
						null, curDeserializedJavaObj);

			} catch (final Exception e) {
				e.printStackTrace();
				// ignore
			}

		}
	}

	@Test
	public void testGenerateStub() throws Exception {
		final Set<String> classes = this.compileAndSetClassLoader(TEST_DATA_SRC_PATH, TEST_DATA_DEST_PATH);
		testClasses(classes, false);
		testClassesWithJVMVerifier(classes, false);
	}

	@Test
	public void testGenerateExecClasses() throws Exception {
		final Set<String> classes = this.compileAndSetClassLoader(TEST_DATA_SRC_PATH, TEST_DATA_DEST_PATH);
		testClasses(classes, true);
		testClassesWithJVMVerifier(classes, true);
	}

	@Test
	public void testGenerateStubDataClayCollections() throws Exception {
		final Set<String> classes = this.compileAndSetClassLoader(COLLECTIONS_SRC_PATH, COLLECTIONS_DEST_PATH);
		testClasses(classes, false);
		testClassesWithJVMVerifier(classes, false);
	}

	@Test
	public void testGenerateExecClassesDataClayCollections() throws Exception {
		final Set<String> classes = this.compileAndSetClassLoader(COLLECTIONS_SRC_PATH, COLLECTIONS_DEST_PATH);
		testClasses(classes, true);
		testClassesWithJVMVerifier(classes, true);
	}

	@Test
	public void testAsmifier() throws Exception {
		this.compileAndSetClassLoader(TEST_DATA_SRC_PATH, TEST_DATA_DEST_PATH);
		// Get resource bytes
		final String resourceName = "/" + ASMifierSerialization.class.getName().replaceAll("\\.", "/") + ".class"; // Any better
		// way?
		final InputStream classInputStream = ASMifierSerialization.class.getResourceAsStream(resourceName);

		// Print class
		ClassReader newClassReader = new ClassReader(classInputStream);
		final Printer printer = new ASMifier();
		FileWriter file = new FileWriter("Asmcode.txt");
		TraceClassVisitor traceVisitor = new TraceClassVisitor(null,
				printer, new PrintWriter(file));
		newClassReader.accept(traceVisitor, ClassReader.EXPAND_FRAMES);

		// Print class
		final InputStream otherInput = ASMifierSerialization.class.getResourceAsStream(resourceName);
		newClassReader = new ClassReader(otherInput);
		file = new FileWriter("ASMifierTest_ByteCode.txt");
		traceVisitor = new TraceClassVisitor(new PrintWriter(file));
		newClassReader.accept(traceVisitor, ClassReader.EXPAND_FRAMES);

	}

}
