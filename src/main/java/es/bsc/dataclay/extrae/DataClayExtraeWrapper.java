package es.bsc.dataclay.extrae;

import es.bsc.dataclay.util.Configuration;

public final class DataClayExtraeWrapper {

	static { 
		System.out.println("Using Extrae wrapper lib located at " + Configuration.Flags.JAVACLAY_EXTRAE_WRAPPER_LIB.getStringValue());
		System.load(Configuration.Flags.JAVACLAY_EXTRAE_WRAPPER_LIB.getStringValue());
	}
	
	public static native void Flush();

}

