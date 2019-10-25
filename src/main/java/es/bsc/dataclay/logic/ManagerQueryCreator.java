
package es.bsc.dataclay.logic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.persistence.Id;

import es.bsc.dataclay.util.ids.ID;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;

/**
 * @pierlauro this class is temporary and it's the base for the future automatic managers db/query generator, please don't
 *            delete it
 */

public class ManagerQueryCreator {
	final static private List<Class<?>> classes = // new ArrayList<>(
			Arrays.asList(new Class[] {
					Account.class,
					DataSet.class,
					PasswordCredential.class
			});

	final static private Map<Class<?>, String> types = new HashMap<>();

	static {
		types.put(String.class, "varchar");
		types.put(String[].class, "varchar[]");
		types.put(int.class, "integer");
		types.put(Integer.class, "integer");
		types.put(long.class, "integer");
		types.put(Long.class, "integer");
		types.put(boolean.class, "bool");
		types.put(Boolean.class, "bool");
		types.put(UUID.class, "uuid"); // Maybe better 2 integers?
		types.put(ID.class, "uuid");
		types.put(UUID[].class, "uuid[]");
	}

	public static void generateManagerTables(final Class<?> c) {
		final StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE IF NOT EXISTS " + getClassName(c) + "(\n");
		for (final Field f : getAllFields(c)) {
			final Class<?> fieldType = f.getType();
			final String name = f.getName();
			final String type;// = types.containsKey(f.getType()) ? types.get(f.getType()): f.getType().getName();

			if (types.containsKey(fieldType)) {
				type = types.get(fieldType);
				sb.append('\t' + name + " " + type);
				if (isPrimaryKey(f)) {
					sb.append(" PRIMARY KEY");
				}
				if (notNull(f)) {
					sb.append(" NOT NULL");
				}
				sb.append(",\n");
				continue;
			}

			if (fieldType.isEnum()) {
				final StringBuffer constants = new StringBuffer("(");
				for (final Object o : fieldType.getEnumConstants()) {
					constants.append("'" + o.toString() + "', ");
				}
				constants.replace(constants.lastIndexOf(","), constants.lastIndexOf(",") + 2, ")");

				sb.append('\t' + name + " varchar NOT NULL CHECK (" + name + " IN " + constants + "),\n");
				continue;
			}

			if (classes.contains(fieldType)) {
				generateManagerTables(fieldType);
			}

			for (final Field field : getAllFields(fieldType)) {
				if (isPrimaryKey(field)) {
					final String fieldName = f.getName() + "_fk";
					sb.append('\t' + fieldName + " " + types.get(field.getType()));
					sb.append(" REFERENCES " + getClassName(fieldType) + "(" + field.getName() + ")" + ",\n");
					break;
				}
			}
		}

		sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, " \n);");
		classes.remove(c);
	}

	protected static String getClassName(final Class<?> c) {
		return c.getName().replace('.', '_');
	}

	protected static List<Field> getAllFields(Class<?> type) {
		final ArrayList<Field> fields = new ArrayList<>();

		while (type != null) {
			fields.addAll(0, Arrays.asList(type.getDeclaredFields()));
			type = type.getSuperclass();
		}

		return fields;
	}

	protected static boolean isPrimaryKey(final Field field) {
		return field.getAnnotationsByType(Id.class).length > 0;
	}

	protected static boolean notNull(final Field field) {
		return field.getAnnotationsByType(Nonnull.class).length > 0;
	}

	public static void main(final String[] args) {
		while (classes.size() > 0) {
			generateManagerTables(classes.get(0));
		}
	}

}
