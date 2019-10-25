
package es.bsc.dataclay.util.tools.java;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import es.bsc.dataclay.util.management.classmgr.Annotation;

public class DataClayAnnotationVisitor extends AnnotationVisitor {
	private final Annotation annotation;

	public DataClayAnnotationVisitor(final Annotation annotation) {
		super(Opcodes.ASM5);
		this.annotation = annotation;
	}

	@Override
	public void visit(String name, Object value) {
		annotation.addParameter(name, value);
	}
}
