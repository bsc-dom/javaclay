package es.bsc.dataclay.api;

/**
 * Handler to manage listeners for asynchronous calls.
 */
public abstract class CallbackHandler {

	/**
	 * Method that will process the callback event.
	 * 
	 * @param e
	 *            event received that must be processed.
	 */
	public abstract void eventListener(final CallbackEvent e);
}
