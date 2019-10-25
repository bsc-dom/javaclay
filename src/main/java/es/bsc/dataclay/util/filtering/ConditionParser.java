
package es.bsc.dataclay.util.filtering;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import es.bsc.dataclay.util.filtering.Condition.ConditionOp;

public class ConditionParser {

	public static final String CONDITIONS_OR_DELIMITER = ")or(";
	public static final String CONDITIONS_AND_DELIMITER = ")and(";
	public static final String CONDITIONS_OR_DELIMITER_PATTERN = Pattern.quote(CONDITIONS_OR_DELIMITER);
	public static final String CONDITIONS_AND_DELIMITER_PATTERN = Pattern.quote(CONDITIONS_AND_DELIMITER);
	public static final String CONDITION_PARTS_DELIMITER = " ";

	/**
	 * Builder for or separated and conditions.
	 * @param queryFilter
	 *            filter to be parsed
	 * @return a new list of and queries (list of conditions per query)
	 */
	public static List<List<Condition>> parseOrsOfAnds(final String queryFilter) {
		final List<List<Condition>> result = new ArrayList<>();
		final String[] st = queryFilter.split(CONDITIONS_OR_DELIMITER_PATTERN);
		for (final String token : st) {
			result.add(parseAnds(token));
		}
		return result;
	}

	/**
	 * Builder for and conditions.
	 * @param queryFilter
	 *            filter to be parsed
	 * @return a list of and condition objects with checked operation
	 */
	public static List<Condition> parseAnds(final String queryFilter) {
		final List<Condition> result = new ArrayList<>();
		final String[] st = queryFilter.split(CONDITIONS_AND_DELIMITER_PATTERN);
		for (final String token : st) {
			final Condition cond = nextCondition(token);
			result.add(cond);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Predicate asOrOfAndsPredicate(final String queryFilter) {
		Predicate result = null;
		final String[] st = queryFilter.split(CONDITIONS_OR_DELIMITER_PATTERN);
		for (final String token : st) {
			final Predicate newPredicate = asAndPredicate(token);
			if (result == null) {
				result = newPredicate;
				continue;
			}
			result = result.or(newPredicate);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Predicate asAndPredicate(final String queryFilter) {
		Predicate result = null;
		final String[] st = queryFilter.split(CONDITIONS_AND_DELIMITER_PATTERN);
		for (final String token : st) {
			final Condition cond = nextCondition(token);
			final Predicate newPredicate = cond.asPredicate();
			if (result == null) {
				result = newPredicate;
				continue;
			}
			result = result.and(newPredicate);
		}
		return result;
	}

	private static Condition nextCondition(final String currentToken) {
		final String token = currentToken.replace(CONDITIONS_OR_DELIMITER_PATTERN, "").replace(CONDITIONS_AND_DELIMITER_PATTERN, "").trim();
		// Look for operation
		int opIndex = -1;
		ConditionOp conditionOp = null;
		for (ConditionOp op : ConditionOp.values()) {
			opIndex = token.indexOf(op.getOp());
			conditionOp = op;
			if (opIndex != -1) {
				// check if operation is not inside a protected string
				final int strIndex = token.indexOf("'");
				final int strDoubleIndex = token.indexOf("\"");
				if (strIndex != -1 && strIndex < opIndex) {
					continue;
				}
				if (strDoubleIndex != -1 && strDoubleIndex < opIndex) {
					continue;
				}
				// found op.
				break;
			}
		}
		final String operation = conditionOp.getOp();
		String fieldName = token.substring(0, opIndex).trim();
		// remove first "("
		if (fieldName.startsWith("(")) {
			fieldName = fieldName.substring(1);
		}
		String value = token.substring(opIndex + operation.length(), token.length()).trim().replace("\"", "");
		// remove last ")" if present
		if (value.endsWith(")")) {
			value = value.substring(0, value.length() - 1);
		}
		return new Condition(fieldName, conditionOp, value);
	}
}
