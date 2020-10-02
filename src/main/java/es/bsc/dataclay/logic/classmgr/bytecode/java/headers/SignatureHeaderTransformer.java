
package es.bsc.dataclay.logic.classmgr.bytecode.java.headers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.util.reflection.Reflector;

/** Signature visitor for renaming super class. */
public final class SignatureHeaderTransformer extends SignatureVisitor {

	/** Indicates it is a super type. */
	private boolean isSuperType = false;

	/** Signature next. */
	private final SignatureVisitor sw;

	/**
	 * DataClaySignatureVisitor constructor.
	 * @param nextVs
	 *            Next visitor
	 */
	public SignatureHeaderTransformer(final SignatureVisitor nextVs) {
		super(Opcodes.ASM7);
		sw = nextVs;
	}

	@Override
	public void visitClassType(final String name) {
		if (isSuperType) {
			if (name.equals("java/lang/Object")) {
				sw.visitClassType(Reflector.getInternalNameFromTypeName(DataClayObject.class.getName()));
			} else {
				sw.visitClassType(name);
			}
			isSuperType = false;
		} else {
			sw.visitClassType(name);
		}
	}

	@Override
	public SignatureVisitor visitSuperclass() {
		isSuperType = true;
		sw.visitSuperclass();
		return super.visitSuperclass();
	}

	/**
	 * Visits a formal type parameter.
	 * 
	 * @param name
	 *            the name of the formal parameter.
	 */
	@Override
	public void visitFormalTypeParameter(final String name) {
		sw.visitFormalTypeParameter(name);
	}

	/**
	 * Visits the class bound of the last visited formal type parameter.
	 * 
	 * @return a non null visitor to visit the signature of the class bound.
	 */
	@Override
	public SignatureVisitor visitClassBound() {
		sw.visitClassBound();
		return super.visitClassBound();
	}

	/**
	 * Visits an interface bound of the last visited formal type parameter.
	 * 
	 * @return a non null visitor to visit the signature of the interface bound.
	 */
	@Override
	public SignatureVisitor visitInterfaceBound() {
		sw.visitInterfaceBound();
		return super.visitInterfaceBound();
	}

	/**
	 * Visits the type of an interface implemented by the class.
	 * 
	 * @return a non null visitor to visit the signature of the interface type.
	 */
	@Override
	public SignatureVisitor visitInterface() {
		sw.visitInterface();
		return super.visitInterface();
	}

	/**
	 * Visits the type of a method parameter.
	 * 
	 * @return a non null visitor to visit the signature of the parameter type.
	 */
	@Override
	public SignatureVisitor visitParameterType() {
		sw.visitParameterType();
		return super.visitParameterType();
	}

	/**
	 * Visits the return type of the method.
	 * 
	 * @return a non null visitor to visit the signature of the return type.
	 */
	@Override
	public SignatureVisitor visitReturnType() {
		sw.visitReturnType();
		return super.visitReturnType();
	}

	/**
	 * Visits the type of a method exception.
	 * 
	 * @return a non null visitor to visit the signature of the exception type.
	 */
	@Override
	public SignatureVisitor visitExceptionType() {
		sw.visitExceptionType();
		return super.visitExceptionType();
	}

	/**
	 * Visits a signature corresponding to a primitive type.
	 * 
	 * @param descriptor
	 *            the descriptor of the primitive type, or 'V' for <tt>void</tt> .
	 */
	@Override
	public void visitBaseType(final char descriptor) {
		sw.visitBaseType(descriptor);
		super.visitBaseType(descriptor);
	}

	/**
	 * Visits a signature corresponding to a type variable.
	 * 
	 * @param name
	 *            the name of the type variable.
	 */
	@Override
	public void visitTypeVariable(final String name) {
		sw.visitTypeVariable(name);
		super.visitTypeVariable(name);
	}

	/**
	 * Visits a signature corresponding to an array type.
	 * 
	 * @return a non null visitor to visit the signature of the array element type.
	 */
	@Override
	public SignatureVisitor visitArrayType() {
		sw.visitArrayType();
		return super.visitArrayType();
	}

	/**
	 * Visits an inner class.
	 * 
	 * @param name
	 *            the local name of the inner class in its enclosing class.
	 */
	@Override
	public void visitInnerClassType(final String name) {
		sw.visitInnerClassType(name);
		super.visitInnerClassType(name);
	}

	/**
	 * Visits an unbounded type argument of the last visited class or inner class type.
	 */
	@Override
	public void visitTypeArgument() {
		sw.visitTypeArgument();
		super.visitTypeArgument();
	}

	/**
	 * Visits a type argument of the last visited class or inner class type.
	 * 
	 * @param wildcard
	 *            '+', '-' or '='.
	 * @return a non null visitor to visit the signature of the type argument.
	 */
	@Override
	public SignatureVisitor visitTypeArgument(final char wildcard) {
		sw.visitTypeArgument(wildcard);
		return super.visitTypeArgument(wildcard);
	}

	/**
	 * Ends the visit of a signature corresponding to a class or interface type.
	 */
	@Override
	public void visitEnd() {
		sw.visitEnd();
		super.visitEnd();
	}
}
