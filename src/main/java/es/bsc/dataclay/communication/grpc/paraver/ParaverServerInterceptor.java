
package es.bsc.dataclay.communication.grpc.paraver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.util.Configuration;
import io.grpc.BindableService;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.ServerServiceDefinition;

/**
 * Paraver server call interceptor for GRPC.
 */
public final class ParaverServerInterceptor implements ServerInterceptor {

	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();
	private static final Logger logger = LogManager.getLogger("Paraver");

	/** Service name. */
	protected final String serviceName;
	/** service port. */
	protected final int servicePort;

	/**
	 * Constructor
	 * 
	 * @param theserviceName
	 *            service name.
	 * @param theservicePort
	 *            service port
	 */
	public ParaverServerInterceptor(final String theserviceName, final int theservicePort) {
		this.serviceName = theserviceName;
		this.servicePort = theservicePort;
	}

	/**
	 * Add tracing to all requests made to this service.
	 * 
	 * @param bindableService
	 *            to intercept
	 * @return the serviceDef with a tracing interceptor
	 */
	public ServerServiceDefinition intercept(final BindableService bindableService) {
		return ServerInterceptors.intercept(bindableService, this);
	}

	@Override
	public <ReqT, RespT> Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> methodDesc, final Metadata headers,
			final ServerCallHandler<ReqT, RespT> next) {

		String reqIDstr = headers.get(ParaverClientCall.MSGID_KEY);
		int reqID = 0;
		try {
			reqID = Integer.parseInt(reqIDstr);
		} catch (final Exception e) {
			if (DEBUG_ENABLED) {
				logger.debug("Intercepted call for non-valid requestID " + reqIDstr + ". Exception: " + e.getMessage());
			}
		}

		final String remoteSocketAddr = methodDesc.getAttributes().get(io.grpc.Grpc.TRANSPORT_ATTR_REMOTE_ADDR)
				.toString();
		final String remoteHostAddr = remoteSocketAddr.substring(1, remoteSocketAddr.indexOf(':'));
		final int remoteHostPort = Integer
				.valueOf(remoteSocketAddr.substring(remoteSocketAddr.indexOf(':') + 1, remoteSocketAddr.length()));

		final ParaverServerCall<ReqT, RespT> paraverCall = new ParaverServerCall<>(this, remoteHostAddr, remoteHostPort,
				reqID, methodDesc);
		return new ParaverServerCallListener<>(this, remoteHostAddr, remoteHostPort, reqID,
				next.startCall(paraverCall, headers));
	}
}
