
package es.bsc.dataclay.logic.classmgr.bytecode.java.headers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureWriter;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.logic.classmgr.bytecode.java.methods.ConstructorMethodTransformer;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * Class header transformer.
 */
public final class ClassHeaderTransformer extends ClassVisitor {

	/** Descriptor name of class of method. */
	private final String classDesc;

	/** Indicates if we are creating execution class. */
	private final boolean isExecClass;

	/** Indicates if parent is object. */
	private final boolean parentIsObject;

	/**
	 * DataClay Class analyzer constructor
	 * @param thecv
	 *            ClassVisitor to delegate to.
	 * @param newclassDesc
	 *            Class descriptor
	 * @param newisExecClass
	 *            Indicates if method must be transformed into an execution class or not.
	 * @param theparentIsObject
	 *            Indicates if parent is object
	 */
	public ClassHeaderTransformer(final ClassVisitor thecv,
			final String newclassDesc,
			final boolean newisExecClass, final boolean theparentIsObject) {
		super(Opcodes.ASM7, thecv);
		this.classDesc = newclassDesc;
		this.isExecClass = newisExecClass;
		this.parentIsObject = theparentIsObject;
	}

	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName, final String[] interfaces) {
		String actualSuperName = superName;
		String actualSignature = signature;
		if (superName.equals("java/lang/Object") || superName.equals("storage/StorageObject")
				|| superName.equals("dataclay/DataClayObject")) {
			if (isExecClass) {
				actualSuperName = Reflector.getInternalNameFromTypeName(DataClayExecutionObject.class.getName());
			} else {
				actualSuperName = Reflector.getInternalNameFromTypeName(DataClayObject.class.getName());
			}
			if (signature != null) {
				final SignatureWriter sw = new SignatureWriter();
				final SignatureHeaderTransformer sv = new SignatureHeaderTransformer(sw);
				final SignatureReader sr = new SignatureReader(signature);
				sr.accept(sv);
				actualSignature = sw.toString();
			}

		}
		cv.visit(version, access, name, actualSignature, actualSuperName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {

		// if method is constructor, modify the invokespecial
		final MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		if (name.equals("<init>")) {
			return new ConstructorMethodTransformer(mv, access, name, desc,
					classDesc, isExecClass, parentIsObject);
		} else {
			return mv;
		}
	}

}
