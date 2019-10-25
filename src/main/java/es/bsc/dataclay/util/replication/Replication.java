
package es.bsc.dataclay.util.replication;

public abstract class Replication {
	public @interface InMaster {
	}

	public @interface BeforeUpdate {
		String clazz();

		String method();
	}

	public @interface AfterUpdate {
		String clazz();

		String method();
	}

	public @interface NoReplica {
	}

	public static final String beforeUpdateAnnotationDescr = getDescr(Replication.BeforeUpdate.class);
	public static final String afterUpdateAnnotationDescr = getDescr(Replication.AfterUpdate.class);

	private static String getDescr(final Class<?> clazz) {
		return "L" + clazz.getName().replace('.', '/') + ";";
	}
}
