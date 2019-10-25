package es.bsc.dataclay.util.prefetchingspec;

public class PrefetchingClassesNotGeneratedException extends RuntimeException {
	
	public PrefetchingClassesNotGeneratedException(Exception e) {
		super();
		this.setStackTrace(e.getStackTrace());
	}
}
