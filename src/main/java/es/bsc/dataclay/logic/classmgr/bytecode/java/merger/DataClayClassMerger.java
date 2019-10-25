
package es.bsc.dataclay.logic.classmgr.bytecode.java.merger;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

/** Class intended to merge bytecode of two classes into one. */
public final class DataClayClassMerger extends ClassVisitor {

	/** Class node from which to read extra information (enrichment). */
	private final ClassNode cn;
	/** Methods added. */
	private final Set<String> methodsAdded = new HashSet<>();

	/**
	 * Constructor
	 * @param cv
	 *            Class visitor in which to delegate calls.
	 * @param thecn
	 *            Class node from which to read information to add into class being read.
	 */
	public DataClayClassMerger(final ClassVisitor cv, final ClassNode thecn) {
		super(Opcodes.ASM7, cv);
		this.cn = thecn;
	}

	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName, final String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {
		final String methodDesc = desc.substring(0, desc.lastIndexOf(")") + 1);
		methodsAdded.add(name + methodDesc);
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitEnd() {

		// Inject new fields
		for (final Object fn : cn.fields) {
			((FieldNode) fn).accept(this);
		}

		// Inject new methods
		for (final Object method : cn.methods) {
			final MethodNode mn = (MethodNode) method;

			// Constructors cannot be enriched
			if (mn.name.equals("<init>") || mn.name.equals("<clinit>")) {
				continue;
			}

			// Synthetic methods are not added
			if ((mn.access & Opcodes.ACC_SYNTHETIC) != 0) {
				continue;
			}

			// Add method
			final String[] exceptions = new String[mn.exceptions.size()];
			final MethodVisitor mv = cv.visitMethod(mn.access, mn.name,
					mn.desc, mn.signature, exceptions);
			mn.instructions.resetLabels();
			mn.accept(mv);
		}

		super.visitEnd();
	}
}
