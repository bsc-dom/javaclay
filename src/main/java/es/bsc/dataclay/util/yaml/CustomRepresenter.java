
/**
 * 
 */
package es.bsc.dataclay.util.yaml;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import es.bsc.dataclay.util.ids.ID;

/**
 * Custom helper class with dataClay special types representation.
 * 
 */
public class CustomRepresenter extends Representer {

	/**
	 * Do *NOT* remove "redundant" global tag for collections --Python needs them.
	 * 
	 * @param property
	 *            - JavaBean property
	 * @param node
	 *            - representation of the property
	 * @param object
	 *            - instance represented by the node
	 */
	/*
	 * @Override protected final void checkGlobalTag(final Property property, final Node node, final Object object) { return; }
	 */

	@Override
	protected NodeTuple representJavaBeanProperty(final Object javaBean,
			final Property property,
			final Object propertyValue,
			final Tag customTag) {
		// Copy paste starts here...

		final ScalarNode nodeKey = (ScalarNode) representData(property.getName());
		// the first occurrence of the node must keep the tag
		final boolean hasAlias = this.representedObjects.containsKey(propertyValue);

		final Node nodeValue = representData(propertyValue);

		if (propertyValue != null && !hasAlias) {
			final NodeId nodeId = nodeValue.getNodeId();
			if (customTag == null) {
				if (nodeId == NodeId.scalar) {
					if (propertyValue instanceof Enum<?>) {
						nodeValue.setTag(Tag.STR);
					}
				}
				// Copy-paste ends here !!!
				// Ignore the else block --always maintain the tag.
			}
		}

		return new NodeTuple(nodeKey, nodeValue);
	}

	/**
	 * Provide a Represent for the generic UUID type.
	 */
	private class RepresentID implements Represent {
		/**
		 * Represent an UUID into a Node.
		 * 
		 * @param data
		 *            The UUID object.
		 * @return The resultant node.
		 */
		@Override
		public Node representData(final Object data) {
			final ID idData = (ID) data;
			return representScalar(new Tag("!!" + data.getClass().getName()), idData.getId().toString());
		}
	}

	/**
	 * Provide a dataClay specific adapted SnakeYAML Representer.
	 */
	public CustomRepresenter() {
		this.multiRepresenters.put(ID.class, new RepresentID());
	}
}
