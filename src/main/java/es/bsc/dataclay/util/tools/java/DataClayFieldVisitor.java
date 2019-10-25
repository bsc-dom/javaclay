
package es.bsc.dataclay.util.tools.java;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import es.bsc.dataclay.util.management.classmgr.Annotation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.java.JavaAnnotationInfo;

public final class DataClayFieldVisitor extends FieldVisitor {
	/** Property associated to the field in the metaclass. */
	private final Property property;

	public DataClayFieldVisitor(final Property property) {
		super(Opcodes.ASM5);
		this.property = property;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		final Annotation annotation = new Annotation(desc);

		final JavaAnnotationInfo langInfo = new JavaAnnotationInfo(visible);
		annotation.addLanguageDepInfo(langInfo);

		property.addAnnotation(annotation);

		return new DataClayAnnotationVisitor(annotation);
	}
}
