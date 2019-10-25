
/**
 * 
 */
package es.bsc.dataclay.logic.classmgr.bytecode.java.merger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Class intended to merge bytecodes for enrichment. 
 */
public final class ByteCodeMerger {

	/**
	 * Utility classes should have private constructor.
	 */
	private ByteCodeMerger() { 
		
	}
	
	
	/**
	 * Merge two bytecodes into one
	 * @param originalByteCode Original bytecode
	 * @param enrichmentByteCode Enrichment bytecode
	 * @return Merged bytecode
	 */
	public static byte[] mergeByteCodes(final byte[] originalByteCode, final byte[] enrichmentByteCode) { 
		// Prepare renaming 
		// Modify enrichment method names if they already exists in original class 
		final ClassReader origReader = new ClassReader(originalByteCode);
		final ClassNode classNode = new ClassNode();
		origReader.accept(classNode, 0); 
		// Get all methods 
		final Set<String> methodsNamesAndSignatures = new HashSet<>();
		for (Object method : classNode.methods) { 
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
		
		final String originalClassInternalName = classNode.name;
		final String enrichmentClassInternalName = enrichmentRenameNode.name;
		
		// Modify all methods 
		final Map<String, String> renaming = new HashMap<>();
		renaming.put(enrichmentClassInternalName, originalClassInternalName);
		for (Object method : enrichmentRenameNode.methods) { 
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
				renaming.put(enrichmentClassInternalName + "." + mn.name + mn.desc, methodName);
				// Add to included method names and signature
				methodsNamesAndSignatures.add(methodName + methodDesc);
			}
		}

		// Merge
		final ClassNode enrichmentNode = new ClassNode(); 
		final ClassRemapper classRemapper = new ClassRemapper(enrichmentNode, new SimpleRemapper(renaming));
		final ClassReader enrichmentReader = new ClassReader(enrichmentByteCode);
		enrichmentReader.accept(classRemapper, ClassReader.EXPAND_FRAMES); // Add enrichment bytecode to class node
		
		final ClassWriter mergeWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		final ClassVisitor mergeVisitor = new DataClayClassMerger(mergeWriter, enrichmentNode);
		final ClassReader mergeReader = new ClassReader(originalByteCode);
		mergeReader.accept(mergeVisitor, ClassReader.EXPAND_FRAMES);
		final byte[] newByteCode = mergeWriter.toByteArray();
		
		return newByteCode;
	}
	
}
