
package es.bsc.dataclay.communication.grpc.paraver;

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import es.bsc.dataclay.paraver.Paraver;
import es.bsc.dataclay.paraver.TraceType;
import io.grpc.Metadata;
import io.grpc.ServerCall;

/**
 * Class intended to intercept GRPC server calls for Paraver traces.
 * 
 * @param <ReqT>
 *            Request type.
 * @param <RespT>
 *            Response type.
 */
public final class ParaverServerCall<ReqT, RespT> extends SimpleForwardingServerCall<ReqT, RespT> {

	/** This call interceptor. */
	ParaverServerInterceptor callInterceptor;

	/** Remote Host name. */
	private final String remoteHostAddr;

	/** Remote port. */
	private final int remotePort;

	/** Current request ID. */
	private final int curRequestID;

	/**
	 * Constructor.
	 * 
	 * @param thecallInterceptor
	 *            Call interceptor
	 * @param theremoteHostAddr
	 *            Remote Host address.
	 * @param theremotePort
	 *            Remote Host port.
	 * @param thereqID
	 *            Request ID.
	 * @param nextCall
	 *            Next delegate call.
	 */
	public ParaverServerCall(final ParaverServerInterceptor thecallInterceptor, final String theremoteHostAddr,
			final int theremotePort, final int thereqID, final ServerCall<ReqT, RespT> nextCall) {
		super(nextCall);
		this.callInterceptor = thecallInterceptor;
		this.remoteHostAddr = theremoteHostAddr;
		this.remotePort = theremotePort;
		this.curRequestID = thereqID;
	}

	@Override
	public void sendHeaders(final Metadata headers) {
		// Add the remote port that originated the request in headers
		if (Paraver.traceIsActiveInterceptor()) {
			headers.put(ParaverClientCall.CLIENTPORT_KEY, "" + this.remotePort);
		}
		delegate().sendHeaders(headers);
	}

	@Override
	public void sendMessage(final RespT message) {
		// Send response message
		if (Paraver.traceIsActiveInterceptor()) {
			Paraver.traceSendCommunicationGrpc(TraceType.SEND_RESPONSE, callInterceptor.serviceName,
					callInterceptor.servicePort, Integer.valueOf(curRequestID), this.remoteHostAddr, this.remotePort,
					0);
		}
		delegate().sendMessage(message);
	}

}
