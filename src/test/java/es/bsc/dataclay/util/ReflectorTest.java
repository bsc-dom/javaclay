
package es.bsc.dataclay.util;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import es.bsc.dataclay.util.reflection.Reflector;

public final class ReflectorTest {

	@Test
	public void getSignatureSubTypesTest() {
	
		String signature = "IFLjava/util/List<Ljava/util/List<Ljava/lang/String;>;>;TX;Ljava/util/LinkedList<Ljava/util/Map<Ljava/lang/Integer;Ljava/lang/Float;>;>;";
		List<String> subTypes = Reflector.getSubtypes(signature);
		assertTrue(subTypes.contains("I"));
		assertTrue(subTypes.contains("F"));
		assertTrue(subTypes.contains("Ljava/util/Map;"));
		assertTrue(subTypes.contains("Ljava/lang/Integer;"));
		assertTrue(subTypes.contains("Ljava/lang/Float;"));
		assertTrue(subTypes.contains("Ljava/util/LinkedList;"));
		assertTrue(subTypes.contains("Ljava/lang/String;"));
		assertTrue(subTypes.contains("Ljava/util/List;"));

	}
	
}
