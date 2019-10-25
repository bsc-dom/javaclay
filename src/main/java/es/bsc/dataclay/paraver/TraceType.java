
package es.bsc.dataclay.paraver;

/** Indicates the type of the trace (first column). */
public enum TraceType {
	/** Indicates the trace is an event. */
	EVENT,
	/** Indicates the trace is a send request. */
	SEND_REQUEST,
	/** Indicates the trace is the reception of some request or response. */
	RECEIVE_COMMUNICATION,
	/** Indicates the trace is a send of a response. */
	SEND_RESPONSE,
}
