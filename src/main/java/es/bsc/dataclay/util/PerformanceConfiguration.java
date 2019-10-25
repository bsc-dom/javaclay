
package es.bsc.dataclay.util;


public final class PerformanceConfiguration {

	/**
	 * ConfigurationFlags cannot be instantiated
	 */
	private PerformanceConfiguration() {

	}

	/** Whether to perform performance analysis USING JUNIT. */
	public static final String PERF_ANALYSIS_TEST_PROP = "PerfAnalysisTest";

	// CHECKSTYLE:OFF
	/** Default value for whether to perform performance analysis USING JUNIT. */
	public static boolean PERF_ANALYSIS_TEST = false;
	// CHECKSTYLE:ON

}
