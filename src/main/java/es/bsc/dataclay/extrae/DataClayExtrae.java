
package es.bsc.dataclay.extrae;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.util.Configuration;

/**
 * This utility class contains functions for Extrae tracing.
 */
public final class DataClayExtrae {

	public static final boolean COMPILE_EXTRAE = true;

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("Paraver");

	/** Indicates that Extrae tracing is activated. */
	public static boolean extraeTracing = false;

	/** Per each intervened method. It's value. */
	private static final Map<String, Long> ALL_METHOD_VALUES = new HashMap<>();
	
	/** Traced methods. */
	private static Map<String, Long> tracedMethods = new HashMap<>();

	/** Type of dataClay events. */
	private static final int EVENT_TYPE = 8000010;
	
	/** Extrae task ID. */
	private static int taskID = 0;
	
	/** First available task ID. */
	private static int currentAvailableTaskID = 0;

	/** Indicates traces were generated. */
	private static boolean generatedTraces = false;
	
	static { 
		if (COMPILE_EXTRAE) {
			// read from paraver_values
			final ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.paraver_values");
			for (final String key : props.keySet()) {
				ALL_METHOD_VALUES.put(key, Long.valueOf(props.getString(key)));
			}
		}
	}
	
	
	/**
	 * Prepare Extrae tracing
	 * 
	 */
	public static synchronized void initializeExtrae(
			final boolean initializeWrapper) {
		if (COMPILE_EXTRAE) {
			try {

				if (initializeWrapper) {

					taskID = currentAvailableTaskID;
					currentAvailableTaskID++;
					es.bsc.cepbatools.extrae.Wrapper.SetTaskID(taskID);
					es.bsc.cepbatools.extrae.Wrapper.SetNumTasks(taskID + 1);
					logger.debug("** Calling Extrae Wrapper.Init()");
					es.bsc.cepbatools.extrae.Wrapper.Init();
				} else {
					// if we have starting task ID = 2, then 1 master and 1 worker are set
					// so we will have:
					// 1 ds java x

					//Wrapper.SetNumTasks(currentAvailableTaskID * 2 + 1); //COMPSs only
					logger.debug("** WARNING: NOT calling Extrae Wrapper.Init()");
					//Wrapper.Init();
					//	Wrapper.SetNumTasks(taskID + 1);
				}

				//Paraver.enablePThreads();
				extraeTracing = true;
				logger.debug("** INITIALIZED Extrae TRACING FOR task ID " + taskID + ". Extrae has "
						+ es.bsc.cepbatools.extrae.Wrapper.GetNumTasks() + " tasks "
						+ ". \n WARNING: Application will NOT be traced if no " +
						" initialization was done (COMPSs initializes it) or Paraver aspects injection was not applied");

			} catch (final Exception e) {
				logger.debug("** Exception while initializing Extrae", e);
			} catch (final Error e) {
				logger.debug("** Error while initializing Extrae", e);
			}
		}
	}

	/**
	 * Enable Extrae tracing
	 */
	public static synchronized void enableExtraeTracing() {
		if (COMPILE_EXTRAE) {
			extraeTracing = true;
			taskID = currentAvailableTaskID;
			es.bsc.cepbatools.extrae.Wrapper.SetNumTasks(taskID + 1);
			logger.debug("** ENABLED Extrae TRACING FOR task ID " + taskID + ". Extrae has "
					+ es.bsc.cepbatools.extrae.Wrapper.GetNumTasks() + " tasks "
					+ ". \n WARNING: Trace with Extrae if enabled. Application will NOT be traced if no " +
					" initialization was done (COMPSs initializes it) or Paraver aspects injection was not applied");
		}
		
	}
	
	/**
	 * Disable Extrae tracing
	 */
	public static synchronized void disableExtraeTracing() {
		if (COMPILE_EXTRAE) {
			if (extraeTracing) {
				defineEventTypes();
				extraeTracing = false;
				generatedTraces = true;
				logger.debug("** FINISHED Extrae TRACING FOR " + taskID + " with task ID " + es.bsc.cepbatools.extrae.Wrapper.GetTaskID());

			}
			logger.debug("** DISABLED Extrae TRACING FOR task ID " + taskID + ". Extrae has "
					+ es.bsc.cepbatools.extrae.Wrapper.GetNumTasks() + " tasks "
					+ ". \n WARNING: Trace with Extrae if enabled. Application will NOT be traced if no " +
					" initialization was done (COMPSs initializes it) or Paraver aspects injection was not applied");
		}
		
	}

	/**
	 * Finish tracing
	 */
	public static synchronized void finishTracing(final boolean finalizeWrapper) {
		if (COMPILE_EXTRAE) {
			if (extraeTracing) {
				defineEventTypes();
				//Wrapper.SetOptions(Wrapper.EXTRAE_ENABLE_ALL_OPTIONS & ~Wrapper.EXTRAE_PTHREAD_OPTION);
				if (finalizeWrapper) {
					logger.debug("** Calling Extrae Wrapper.Fini()");
					es.bsc.cepbatools.extrae.Wrapper.Fini();
				}
			/* else {
				logger.debug("** WARNING: NOT calling Extrae Wrapper.Fini()");
				try {
					DataClayExtraeWrapper.Flush();
				} catch (Error err) { 
					err.printStackTrace();
				} catch (Exception e) { 
					e.printStackTrace();
				}
			} */
				//Wrapper.SetOptions(Wrapper.EXTRAE_DISABLE_ALL_OPTIONS);
				extraeTracing = false;
				generatedTraces = true;
				logger.debug("** FINISHED Extrae TRACING FOR " + taskID + " with task ID " + es.bsc.cepbatools.extrae.Wrapper.GetTaskID());



			}
		}
	}
	
	/**
	 * Add an event
	 * 
	 * @param eventType
	 *            Event type (enter, exit, exception)
	 * @param methodSignature
	 *            Method signature
	 */
	public static synchronized void emitEvent(final boolean enter, final String methodSignature) {
		if (COMPILE_EXTRAE) {
			if (!extraeTracing) {
				return;
			}
			final Long methodID = ALL_METHOD_VALUES.get(methodSignature);
			if (methodID != null) {
				tracedMethods.put(methodSignature, methodID);
				if (enter) {
					es.bsc.cepbatools.extrae.Wrapper.Event(EVENT_TYPE, methodID);
				} else {
					es.bsc.cepbatools.extrae.Wrapper.Event(EVENT_TYPE, 0);
				}
				//logger.debug("Traced event (enter =" + enter + ") : " + methodSignature + " with value : " + methodID);
			}
		}
	}
	
	/**
     * When using extrae's tracing, this call enables the instrumentation of ALL created threads
     * from here onwards. To deactivate it use disablePThreads().
     */
    public static void enablePThreads() {
		if (COMPILE_EXTRAE) {
			synchronized (DataClayExtrae.class) {
				es.bsc.cepbatools.extrae.Wrapper.SetOptions(es.bsc.cepbatools.extrae.Wrapper.EXTRAE_ENABLE_ALL_OPTIONS);
			}
		}
    }

    /**
     * When using extrae's tracing, this call disables the instrumentation of any created threads
     * from here onwards. To reactivate it use enablePThreads()
     */
    public static void disablePThreads() {
		if (COMPILE_EXTRAE) {
			synchronized (DataClayExtrae.class) {
				es.bsc.cepbatools.extrae.Wrapper.SetOptions(es.bsc.cepbatools.extrae.Wrapper.EXTRAE_ENABLE_ALL_OPTIONS & ~es.bsc.cepbatools.extrae.Wrapper.EXTRAE_PTHREAD_OPTION);
			}
		}
    }
	
	/**
	 * Method to define event types for Extrae/Paraver based on current known called
	 * methods.
	 */
	public static synchronized void defineEventTypes() {
		if (COMPILE_EXTRAE) {
			if (!extraeTracing) {
				return;
			}
			logger.debug("** DEFINING EVENT TYPES AND VALUES");

			final long[] methodValues = new long[tracedMethods.size() + 1];
			final String[] descriptors = new String[tracedMethods.size() + 1];
			int i = 1;
			methodValues[0] = 0;
			descriptors[0] = "End";
			for (final Entry<String, Long> currentTracedMethod : tracedMethods.entrySet()) {
				final String methodDesc = currentTracedMethod.getKey();
				final Long methodValue = currentTracedMethod.getValue();
				descriptors[i] = methodDesc;
				methodValues[i] = methodValue;
				logger.debug("-- EVENT " + methodDesc);
				i++;
			}

			es.bsc.cepbatools.extrae.Wrapper.defineEventType(EVENT_TYPE, "dataClay", methodValues, descriptors);
		}
	}

	/**
	 * Indicates if Extrae tracing is enabled
	 * 
	 * @return True if it is active. 
	 */
	public static synchronized boolean extraeTracingIsEnabled() {
		return extraeTracing;
	}

	/**
	 * Get Extrae task ID
	 * @return task ID
	 */
	public static int getTaskID() {
		return taskID;
	}
	
	/**
	 * Get WRAPPER Extrae task ID
	 * @return task ID
	 */
	public static int getWrapperTaskID() {
		if (COMPILE_EXTRAE) {
			return es.bsc.cepbatools.extrae.Wrapper.GetTaskID();
		}
		return 0;
	}


	/**
	 * Get current available task ID
	 * @return Current available task ID
	 */
	public static int getCurrentAvailableTaskID() {
		return currentAvailableTaskID;
	}
	
	/**
	 * Get current available task ID and increment it by one.
	 * @return Current available task ID
	 */
	public static int getAndIncrementCurrentAvailableTaskID() {
		final int nextTaskID = currentAvailableTaskID;
		currentAvailableTaskID++;
		return nextTaskID;
	}

	/**
	 * Set current available task ID
	 * @param thecurrentAvailableTaskID Current available task ID
	 */
	public static void setCurrentAvailableTaskID(final int thecurrentAvailableTaskID) {
		DataClayExtrae.currentAvailableTaskID = thecurrentAvailableTaskID;
	}
	
	/**
	 * Get Extrae traces in workspace directory
	 * @return
	 */
	public static Map<String, byte[]> getTraces() { 
		final Map<String, byte[]> traces = new HashMap<>();
		if (generatedTraces) {
	
			final java.io.BufferedReader b = null;
			// Read TRACE.mpits
			try {
				final String folderPath = System.getProperty("user.dir") + File.separator + "set-0";
	
				final File folder = new File(folderPath); //TODO: only set-0?
				logger.debug(" READING FROM FOLDER " + folderPath);
				
				 // retrieve file listing
			    final File[] fileList = folder.listFiles();
			    if (fileList != null) {
				    for (final File file : fileList ) {
						final byte[] bArray = Files.readAllBytes(file.toPath());
						traces.put(file.getName(), bArray);
					}
			    }
				logger.debug("Sending files: " + traces.keySet());
				
				
			} catch (final IOException e) {
				logger.debug("Exception while getting traces", e);
				
			} finally { 
				if (b != null) { 
					try {
						b.close();
					} catch (final IOException e) {
						logger.debug("Exception while getting traces", e);
						
					}
				}
			}
			generatedTraces = false;
		}
		return traces;

	}

	/**
	 * Indicates there are generated traces ready. 
	 * @return TRUE if there are generated traces ready. 
	 */
	public static boolean isGeneratedTraces() {
		return generatedTraces;
	}

}
