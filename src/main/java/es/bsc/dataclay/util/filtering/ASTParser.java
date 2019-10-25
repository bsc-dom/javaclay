
package es.bsc.dataclay.util.filtering;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Predicate;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.util.ids.ObjectID;

public class ASTParser {
	public static String NESTED_ATTR_DELIMITER = "/";
	public static String NULL_VALUE = "null";

	// Filter := AndExpr | AndExpr <'or'> Filter
	// AndExpr := Comp | Comp <'and'> AndExpr
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Predicate asPredicate(final List<Object> filter) {
		Predicate result = null;
		if (filter.size() == 0) {
			return result;
		}
		final String token = filter.get(0).toString();
		final ASTListOp op = ASTListOp.fromString(token);
		switch (op) {
		case FILTER:
			if (filter.size() == 2) { // Filter := AndExpr => token + List
				result = asPredicate((List<Object>) filter.get(1));
			} else { // Filter := AndExpr <'or'> Filter => token + List + List
				result = asPredicate((List<Object>) filter.get(1)).or(asPredicate((List<Object>) filter.get(2)));
			}
			break;
		case ANDEXPR:
			if (filter.size() == 2) { // AndExpr := Comp => token + List
				result = asPredicate((List<Object>) filter.get(1));
			} else { // Filter := Comp <'and'> AndExpr => token + List + List
				result = asPredicate((List<Object>) filter.get(1)).and(asPredicate((List<Object>) filter.get(2)));
			}
			break;
		case COMP:
			if (filter.size() == 2) { // Comp := ( Filter ) => token + List
				result = asPredicate((List<Object>) filter.get(1));
			} else {
				result = attrCompAsPredicate(filter); // Comp:= Atr Op Val | Val Op Atr => token + List + List + List
			}
			break;
		default:
			break;
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Predicate attrCompAsPredicate(List<Object> comp) {
		// comp.get(0) is token :Comp
		final List<Object> factor1 = (List<Object>) comp.get(1);
		final List<Object> operand = (List<Object>) comp.get(2);
		final List<Object> factor2 = (List<Object>) comp.get(3);
		final String kw1 = factor1.get(0).toString();

		final String attr, value;
		if (ASTKeyword.isASTKeyword(kw1)) {
			// factor1 is attribute
			attr = factor1.get(1).toString();
			value = factor2.get(1).toString();

		} else {
			// factor2 is attribute
			attr = factor2.get(1).toString();
			value = factor1.get(1).toString();
		}

		final ASTOperand op = ASTOperand.fromString(operand.get(1).toString());
		return p -> {
			try {
				Comparable castedFieldValue;

				// Nested attribute
				StringTokenizer st = new StringTokenizer(attr, NESTED_ATTR_DELIMITER);
				Object curAttr = p.getClass().getMethod(ClassManager.GETTER_PREFIX + st.nextToken()).invoke(p);
				while (st.hasMoreTokens() && curAttr instanceof Map) {
					String next = st.nextToken();
					Map curAttrAsMap = (Map) curAttr;
					curAttr = curAttrAsMap.get(next);
				}
				castedFieldValue = (Comparable) curAttr;
				if (curAttr == null) {
					switch (op) {
					case EQUALS:
						return value.toString().equals(NULL_VALUE);
					case DIFFERENT:
						return !value.toString().equals(NULL_VALUE);
					default:
						return false;
					}
				}

				// Condition check
				Class<? extends Comparable> fieldType = castedFieldValue.getClass();
				Object condObjValue = valueAsObject(fieldType, value);
				switch (op) {
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
				case PREFIX:
					return castedFieldValue.toString().startsWith(condObjValue.toString());
				case CONTAINS:
					return castedFieldValue.toString().contains(condObjValue.toString());
				default:
					return false;
				}
			} catch (Exception e) {
				throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION,
						"Invalid condition for a predicate. Message: " + e.getMessage(),
						true);
			}
		};
	}

	private interface ASTElem {

	}

	public static enum ASTListOp implements ASTElem {
		FILTER("Filter"), ANDEXPR("AndExpr"), COMP("Comp");
		public String _token;

		ASTListOp(String token) {
			this._token = token;
		}

		public static boolean isASTListOp(final String token) {
			for (ASTListOp x : ASTListOp.values()) {
				if (x._token.equals(token)) {
					return true;
				}
			}
			return false;
		}

		public static ASTListOp fromString(final String token) {
			for (ASTListOp val : ASTListOp.values()) {
				if (val._token.equals(token)) {
					return val;
				}
			}
			throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION, "ASTListOp " + token + " is not valid.",
					false);
		}
	}

	public static enum ASTKeyword implements ASTElem {
		ATTRIBUTE("Attribute"), EQOP("EqOp"), RELOP("RelOp"), INT("IntValue"), STRING("String"), BOOL("BoolValue"), NULL("NullValue");
		public String _token;

		ASTKeyword(String token) {
			this._token = token;
		}

		public static boolean isASTKeyword(final String token) {
			for (ASTKeyword x : ASTKeyword.values()) {
				if (x._token.equals(token)) {
					return true;
				}
			}
			return false;
		}

		public static ASTKeyword fromString(final String token) {
			for (ASTKeyword val : ASTKeyword.values()) {
				if (val._token.equals(token)) {
					return val;
				}
			}
			throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION, "ASTKeyword " + token + " is not valid.",
					false);
		}
	}

	public static enum ASTOperand implements ASTElem {
		EQUALS("="), DIFFERENT("!="), LESS("<"), GREATER(">"), LESS_EQUALS("<="), GREATER_EQUALS(">="), PREFIX("^="), CONTAINS(":=");
		public String _token;

		ASTOperand(String token) {
			this._token = token;
		}

		public static boolean isASTOperand(final String token) {
			for (ASTOperand x : ASTOperand.values()) {
				if (x._token.equals(token)) {
					return true;
				}
			}
			return false;
		}

		public static ASTOperand fromString(final String token) {
			for (ASTOperand val : ASTOperand.values()) {
				if (val._token.equals(token)) {
					return val;
				}
			}
			throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION, "ASTOperand " + token + " is not valid.",
					false);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Comparable valueAsObject(final Class type, final String val) {
		final ValueType supportedType = ValueType.fromClass(type);
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
		case ID:
			return val;
		default:
			break;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public enum ValueType {
		INT(int.class), SHORT(short.class), LONG(long.class), FLOAT(float.class), DOUBLE(double.class), BOOLEAN(boolean.class), INTCLASS(Integer.class), LONGCLASS(
				Long.class), SHORTCLASS(Short.class), FLOATCLASS(Float.class), DOUBLECLASS(Double.class), BOOLEANCLASS(Boolean.class), STRING(String.class), ID(ObjectID.class);

		Class type;

		private ValueType(Class theType) {
			type = theType;
		}

		Class getType() {
			return type;
		}

		public static ValueType fromClass(final Class checktype) {
			for (ValueType cat : ValueType.values()) {
				if (cat.getType().equals(checktype)) {
					return cat;
				}
			}
			throw new DataClayException(ERRORCODE.INVALID_PARAMETER_EXCEPTION,
					"Attribute type: " + checktype.getName() + " is not supported for filtering.", false);
		}
	}
}
