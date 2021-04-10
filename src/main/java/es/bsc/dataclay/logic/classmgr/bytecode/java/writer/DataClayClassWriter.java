
/**
 * 
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;

import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * Specialization of ClassWriter for loading resources from our own class loader or Class Manager.
 */
public class DataClayClassWriter extends ClassWriter {

	/** Class Manager used to extract class information. */
	private final ClassManager classMgr;

	/** MetaClass information of all included classes. Name -> MetaClass or Namespace+Name -> Metaclass if Exec. */
	private final Map<String, MetaClass> includes;

	/** Is execution class. */
	private final boolean isExec;

	/**
	 * Constructor
	 * @param flags
	 *            Flags
	 * @param theClassMgr
	 *            Class Manager to use
	 * @param theincludes
	 *            MetaClass information of all included classes
	 * @param theisExec
	 *            Is execution class.
	 */
	public DataClayClassWriter(final int flags, final ClassManager theClassMgr,
			final Map<String, MetaClass> theincludes, final boolean theisExec) {
		super(flags);
		this.classMgr = theClassMgr;
		this.includes = theincludes;
		this.isExec = theisExec;
	}

	@Override
	public final String getCommonSuperClass(final String type1, final String type2) {
		// If exec. type1 and type2 will include the namespace, but our includes structure should be aware.
		final String type1Name = Reflector.getTypeNameFromInternalName(type1);
		final String type2Name = Reflector.getTypeNameFromInternalName(type2);
		if (type1Name.equals(Object.class.getName())) {
			return type1;
		}
		if (type2Name.equals(Object.class.getName())) {
			return type2;
		}
		if (Reflector.isJavaPrimitiveOrArrayTypeName(type1Name) && Reflector.isJavaPrimitiveOrArrayTypeName(type2Name)) {
			return super.getCommonSuperClass(type1, type2);
		}
		if (Reflector.isJavaPrimitiveOrArrayTypeName(type1Name) && !Reflector.isJavaPrimitiveOrArrayTypeName(type2Name)) {
			return "java/lang/Object";
		}
		if (!Reflector.isJavaPrimitiveOrArrayTypeName(type1Name) && Reflector.isJavaPrimitiveOrArrayTypeName(type2Name)) {
			return "java/lang/Object";
		}

		MetaClass type1Class = includes.get(type1Name);
		MetaClass type2Class = includes.get(type2Name);
		if (type1Class == null) {
			System.err.println("Not found include " + type1Name);

		}
		if (type2Class == null) {
			System.err.println("Not found include " + type2Name);
		}
		// Look for common parent type
		if (type1Class.getParentType() == null || type2Class.getParentType() == null) {
			return "java/lang/Object";
		}

		if (type1.equals(type2)) {
			return type1;
		}

		// Get all parent names
		final List<String> parent1Names = new ArrayList<>();
		while (type1Class.getParentType() != null) {
			final String parentTypeName = type1Class.getParentType().getTypeName();
			final UserType parentType = type1Class.getParentType();
			// Check if in includes
			String key = null;
			if (isExec) {
				key = parentType.getNamespace() + "." + parentType.getTypeName();
			} else {
				key = parentType.getTypeName();
			}
			type1Class = includes.get(key);
			parent1Names.add(key);

			if (type1Class == null) {
				type1Class = classMgr.getMetaClassByNameAndNamespace(parentType.getNamespace(),
						parentTypeName);
			}

		}

		// Get all parent 2 names
		final List<String> parent2Names = new ArrayList<>();
		while (type2Class.getParentType() != null) {
			final String parentTypeName = type2Class.getParentType().getTypeName();
			final UserType parentType = type2Class.getParentType();
			// Check if in includes
			String key = null;
			if (isExec) {
				key = parentType.getNamespace() + "." + parentType.getTypeName();
			} else {
				key = parentType.getTypeName();
			}
			type2Class = includes.get(key);
			parent2Names.add(key);

			if (type2Class == null) {
				type2Class = classMgr.getMetaClassByNameAndNamespace(parentType.getNamespace(),
						parentTypeName);
			}

		}

		// Check if type 1 is same, superclass of type 2
		for (final String parent2Name : parent2Names) {
			if (type1.equals(parent2Name)) {
				final String internalName = Reflector.getInternalNameFromTypeName(type1);
				return internalName;
			}
		}

		// Check if type 2 is same, superclass of type 2
		for (final String parent1Name : parent1Names) {
			if (type2.equals(parent1Name)) {
				final String internalName = Reflector.getInternalNameFromTypeName(type2);
				return internalName;
			}
		}

		// Check
		for (final String parent1Name : parent1Names) {
			for (final String parent2Name : parent2Names) {
				if (parent1Name.equals(parent2Name)) {
					final String internalName = Reflector.getInternalNameFromTypeName(parent1Name);
					return internalName;
				}
			}
		}

		return null;

	}
}
