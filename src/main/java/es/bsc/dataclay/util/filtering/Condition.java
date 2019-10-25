
package es.bsc.dataclay.util.filtering;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Predicate;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.util.ids.ObjectID;

public final class Condition {
	public static String NESTED_ATTR_DELIMITER = "/";
	private final String attr;
	private final ConditionOp op;
	private final String val;

	/**
	 * Basic constructor.
	 */
	public Condition(final String attribute, final ConditionOp operation, final String value) {
		attr = attribute;
		op = operation;
		val = value;
	}

	/**
	 * Check if the object matches current condition.
	 * @param o
	 *            object to be checked.
	 * @return true if the object matches current condition, false otherwise.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean matches(final Object o) {
		try {

			// Nested attribute
			final StringTokenizer st = new StringTokenizer(attr, NESTED_ATTR_DELIMITER);
			Object fieldValue = o;
			// Nested attributes check
			while (st.hasMoreTokens()) {
				final String next = st.nextToken();
				if (fieldValue instanceof Map) {
					final Map curAttrAsMap = (Map) fieldValue;
					fieldValue = curAttrAsMap.get(next);
				} else {
					fieldValue = fieldValue.getClass().getMethod(ClassManager.GETTER_PREFIX + next).invoke(fieldValue);
				}
			}
			// Infer class of value in condition
			final Class fieldType;
			if (fieldValue instanceof Comparable) { // for direct comparison conditions
				if (fieldValue instanceof String) {
					// CHECK IF FIELD IS DATE
					try {
						final Instant instant = Instant.parse((String) fieldValue);
						fieldValue = Date.from(instant);
					} catch (DateTimeParseException e) {
						// ignore, field value will continue being a string
					}
				}
				fieldType = fieldValue.getClass();

			} else if (fieldValue instanceof Iterable) { // for "contains-like" conditions
				final Object sample = ((Iterable) fieldValue).iterator().next();
				if (sample == null) {
					return false;
				}
				fieldType = sample.getClass();
			} else {
				return false;
			}

			final Object condObjValue = valueAsObject(fieldType);
			if (fieldValue instanceof Comparable) {
				final Comparable castedFieldValue = (Comparable) fieldValue;
				switch (op) {
				case CONTAINS:
					return (castedFieldValue.toString()).contains(condObjValue.toString());
				case PREFIX:
					return (castedFieldValue.toString()).startsWith(condObjValue.toString());
				case DIFFERENT:
					return castedFieldValue.compareTo(condObjValue) != 0;
				case EQUALS:
					return castedFieldValue.compareTo(condObjValue) == 0;
				case GREATER:
					return castedFieldValue.compareTo(condObjValue) > 0;
				case GREATER_EQUALS:
					return castedFieldValue.compareTo(condObjValue) >= 0;
				case LESS:
					return castedFieldValue.compareTo(condObjValue) < 0;
				case LESS_EQUALS:
					return castedFieldValue.compareTo(condObjValue) <= 0;
				default:
					return false;
				}
			} else if (fieldValue instanceof Iterable) {
				switch (this.op) {
				case CONTAINS:
					final Iterator it = ((Iterable) fieldValue).iterator();
					boolean found = false;
					while (it.hasNext() && !found) {
						final Object elem = it.next();
						if (elem.equals(condObjValue)) {
							found = true;
						}
					}
					return found;
				default:
					return false;
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION,
					"Invalid condition for a predicate. Message: " + e.getMessage(),
					true);
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes" })
	public Predicate asPredicate() {
		return p -> {
			return matches(p);
		};
	}

	/**
	 * Retrieve the object representing the value of the current condition, considering provided class type.
	 * @param type
	 *            class to be considered for the object .
	 * @return the value represented as an instance of the given type with the value of the condition.
	 * @throws DataClayException
	 *             if the type is not supported.
	 */
	@SuppressWarnings("rawtypes")
	public Comparable valueAsObject(final Class type) {
		final ConditionValueType supportedType = ConditionValueType.fromClass(type);
		switch (supportedType) {
		case BOOLEAN:
		case BOOLEANCLASS:
			return new Boolean(val); // only true if val == "true" (ignoring case)
		case DOUBLE:
		case DOUBLECLASS:
			return new Double(val);
		case FLOAT:
		case FLOATCLASS:
			return new Float(val);
		case INT:
		case INTCLASS:
			return new Integer(val);
		case SHORT:
		case SHORTCLASS:
			return new Short(val);
		case LONG:
		case LONGCLASS:
			return new Long(val);
		case STRING:
			return val;
		case DATE:
			final Instant instant = Instant.parse(val);
			final Date date = Date.from(instant);
			return date;
		case ID:
			return val;
		default:
			break;
		}
		return null;
	}

	/**
	 * Accepted types for condition attributes.
	 */
	@SuppressWarnings("rawtypes")
	public enum ConditionValueType {
		INT(int.class), SHORT(short.class), LONG(long.class), FLOAT(float.class), DOUBLE(double.class), BOOLEAN(boolean.class), INTCLASS(Integer.class), LONGCLASS(
				Long.class), SHORTCLASS(
						Short.class), FLOATCLASS(Float.class), DOUBLECLASS(Double.class), BOOLEANCLASS(Boolean.class), STRING(String.class), DATE(Date.class), ID(ObjectID.class);

		Class type;

		private ConditionValueType(final Class theType) {
			type = theType;
		}

		Class getType() {
			return type;
		}

		/**
		 * Method that returns the enum value corresponding to given type.
		 * @param checktype
		 *            type to be considered.
		 * @return enum value corresponding to given type.
		 * @throws DataClayException
		 *             if the class is not supported.
		 */
		public static ConditionValueType fromClass(final Class checktype) {
			for (final ConditionValueType cat : ConditionValueType.values()) {
				if (cat.getType().equals(checktype)) {
					return cat;
				}
			}
			throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION,
					"Attribute type: " + checktype.getName() + " is not supported for filtering.", false);
		}
	}

	/**
	 * Accepted operations for the condition check.
	 */
	public enum ConditionOp {
		DIFFERENT("!="), LESS_EQUALS("<="), GREATER_EQUALS(">="), CONTAINS(":="), PREFIX("^="), LESS("<"), GREATER(">"), EQUALS("="); // WARNING: equals must be the last condition
																																		// to check to make sure other ones
																																		// containing symbol '=' were verified.

		String op;

		ConditionOp(final String theOp) {
			op = theOp;
		}

		public String getOp() {
			return this.op;
		}

		public static ConditionOp[] getNumericOps() {
			return new ConditionOp[] { EQUALS, DIFFERENT, LESS, GREATER, LESS_EQUALS, GREATER_EQUALS };
		}

		public static ConditionOp[] getBooleanOps() {
			return new ConditionOp[] { EQUALS, DIFFERENT };
		}

		/**
		 * Method that retrieves the ConditionOp from the given string.
		 * @param op
		 *            operation in form of string.
		 * @return operation in form of ConditionOp.
		 * @throws DataClayException
		 *             if the operation is not supported.
		 */
		public static ConditionOp fromString(final String op) {
			final ConditionOp[] values = ConditionOp.values();
			for (final ConditionOp val : values) {
				if (val.getOp().equals(op)) {
					return val;
				}
			}
			throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION, "Operation " + op + " is not valid.",
					false);
		}
	}

	public static String buildStringCondition(final String attr, final ConditionOp op, final String value) {
		return attr + op.getOp() + value;
	}

	public static String buildMultiAndCondition(final String[] attrs, final ConditionOp[] ops, final String[] values) {
		if (!(attrs.length == ops.length && ops.length == values.length)) {
			return null;
		}
		String result = buildStringCondition(attrs[0], ops[0], values[0]);
		for (int i = 1; i < attrs.length; i++) {
			result += ConditionParser.CONDITIONS_AND_DELIMITER + buildStringCondition(attrs[i], ops[i], values[i]);
		}
		return result;
	}

	public static String buildMultiOrMultiAndCondition(final String[][] attrs, final ConditionOp[][] ops, final String[][] values) {
		if (!(attrs.length == ops.length && ops.length == values.length)) {
			return null;
		}
		String result = buildMultiAndCondition(attrs[0], ops[0], values[0]);
		for (int i = 0; i < attrs.length; i++) {
			result += ConditionParser.CONDITIONS_OR_DELIMITER + buildMultiAndCondition(attrs[i], ops[i], values[i]);
		}
		return result;
	}
}
