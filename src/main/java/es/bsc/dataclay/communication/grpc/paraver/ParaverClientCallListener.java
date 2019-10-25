
package es.bsc.dataclay.communication.grpc.paraver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.paraver.Paraver;
import es.bsc.dataclay.util.Configuration;
import io.grpc.ClientCall.Listener;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata;

/**
 * Class intended to intercept GRPC client call responses for Paraver traces.
 * 
 * @param <RespT>
 *            Response type.
 */
public final class ParaverClientCallListener<RespT> extends SimpleForwardingClientCallListener<RespT> {

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("Paraver");

	/** Local host name. */
	private final String localHostName;

	/** Local port. To be updated considering request channel. */
	private int localPort = 0;

	/** Remote Host name. */
	private final String remoteHostAddr;

	/** Remote port. */
	private final int remotePort;

	/** Current request ID. */
	private final int curRequestID;

	/**
	 * Constructor.
	 * 
	 * @param thelocalHostName
	 *            local Host name.
	 * @param theremoteHostAddr
	 *            Remote Host address.
	 * @param theremotePort
	 *            Remote Host port.
	 * @param thereqID
	 *            Request ID.
	 * @param listener
	 *            Next delegate call.
	 */
	public ParaverClientCallListener(final String thelocalHostName, final String theremoteHostAddr,
			final int theremotePort, final int thereqID, final Listener<RespT> listener) {
		super(listener);
		this.localHostName = thelocalHostName;
		this.remoteHostAddr = theremoteHostAddr;
		this.remotePort = theremotePort;
		this.curRequestID = thereqID;
	}

	@Override
	public void onHeaders(final Metadata headers) {
		// Received headers of a response
		if (Paraver.traceIsActiveInterceptor()) {
			final String strLocalPort = headers.get(ParaverClientCall.CLIENTPORT_KEY);
			if (strLocalPort != null) {
				this.localPort = Integer.parseInt(strLocalPort);
			}
		}
		if (DEBUG_ENABLED) {
			logger.debug("[" + localHostName + ":" + localPort + " <- " + remoteHostAddr + ":" + remotePort
					+ "] Request " + curRequestID + ": response headers in thread " + Thread.currentThread().getId());
		}
		delegate().onHeaders(headers);
	}

	@Override
	public void onMessage(final RespT message) {
		// Received a response
		if (Paraver.traceIsActiveInterceptor()) {
			Paraver.traceReceiveCommunicationGrpc(this.localHostName, this.localPort, this.remoteHostAddr,
					this.remotePort, this.curRequestID, 0);
		}
		if (DEBUG_ENABLED) {
			logger.debug("[" + localHostName + " <- " + remoteHostAddr + ":" + remotePort + "] Request " + curRequestID
					+ ": response in thread " + Thread.currentThread().getId());
		}
		delegate().onMessage(message);
	}
}
