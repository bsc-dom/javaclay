
package es.bsc.dataclay.communication.grpc.paraver;

import es.bsc.dataclay.paraver.Paraver;
import io.grpc.ForwardingServerCallListener;

/**
 * Class intended to intercept GRPC requests to server for Paraver traces.
 * 
 * @param <ReqT>
 *            Request type.
 */
public final class ParaverServerCallListener<ReqT> extends ForwardingServerCallListener<ReqT> {

	/** Interceptor of this listener. */
	private final ParaverServerInterceptor callInterceptor;

	/** Remote Host name. */
	private final String remoteHostAddr;

	/** Remote port. */
	private final int remotePort;

	/** Current request ID. */
	private final int curRequestID;

	/** Next listener. */
	private final io.grpc.ServerCall.Listener<ReqT> next;

	/**
	 * Constructor.
	 * 
	 * @param thecallInterceptor
	 *            Call interceptor creating this.
	 * @param theremoteHostAddr
	 *            Remote Host address.
	 * @param theremotePort
	 *            Remote Host port.
	 * @param thereqID
	 *            Request ID.
	 * @param listener
	 *            Next delegate call.
	 */
	public ParaverServerCallListener(final ParaverServerInterceptor thecallInterceptor, final String theremoteHostAddr,
			final int theremotePort, final int thereqID, final io.grpc.ServerCall.Listener<ReqT> listener) {
		next = listener;
		this.callInterceptor = thecallInterceptor;
		this.remoteHostAddr = theremoteHostAddr;
		this.remotePort = theremotePort;
		this.curRequestID = thereqID;
	}

	@Override
	public void onMessage(final ReqT message) {
		// Received a request
		if (Paraver.traceIsActiveInterceptor()) {
			Paraver.traceReceiveCommunicationGrpc(callInterceptor.serviceName, callInterceptor.servicePort,
					this.remoteHostAddr, this.remotePort, this.curRequestID, 0);
		}
		delegate().onMessage(message);
	}

	@Override
	protected io.grpc.ServerCall.Listener<ReqT> delegate() {
		return next;
	}

}
