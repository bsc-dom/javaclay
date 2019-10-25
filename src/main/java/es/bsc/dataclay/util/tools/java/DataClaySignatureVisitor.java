
package es.bsc.dataclay.util.tools.java;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

/** Signature visitor. */
public final class DataClaySignatureVisitor extends SignatureVisitor {

	/** All Found dependencies. */
	private final List<String> dependencies = new ArrayList<String>();
	
	/**
	 * DataClaySignatureVisitor constructor.
	 */
	public DataClaySignatureVisitor() {
		super(Opcodes.ASM5);
	}

	@Override
	public SignatureVisitor visitArrayType() {
		return super.visitArrayType();
	}

	@Override
	public void visitBaseType(final char descriptor) {
		dependencies.add(String.valueOf(descriptor));
		super.visitBaseType(descriptor);
	}

	@Override
	public void visitClassType(final String name) {
		dependencies.add(name);
		super.visitClassType(name);
	}

	@Override
	public void visitInnerClassType(final String name) {
		dependencies.add(name);
		super.visitInnerClassType(name);
	}

	@Override
	public SignatureVisitor	visitParameterType() {
		return super.visitParameterType();
	}

	@Override
	public SignatureVisitor	visitReturnType() {
		return super.visitReturnType();
	}


	@Override
	public void visitFormalTypeParameter(final String name) { 
		super.visitFormalTypeParameter(name);
	}
	
    
	@Override
	public SignatureVisitor visitTypeArgument(final char wildcard) {
		return super.visitTypeArgument(wildcard);
	}
	
	@Override
	public void visitTypeVariable(final String name) { 
		//dependencies.add(name);
		super.visitTypeVariable(name);
	}
	
	/**
	 * Get set of names of classes found during analysis of the signature.
	 * @return set of names of classes found during analysis of the signature.
	 */
	public List<String> getDependencies() {
		return this.dependencies;
	}
}
