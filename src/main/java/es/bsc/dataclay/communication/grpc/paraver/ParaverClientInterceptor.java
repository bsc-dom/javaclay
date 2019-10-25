
package es.bsc.dataclay.communication.grpc.paraver;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.MethodDescriptor;

/**
 * Paraver client call interceptor for GRPC.
 */
public final class ParaverClientInterceptor implements ClientInterceptor {

	/** local host name (user/service host acting as client) */
	private final String localHostName;

	/** Remote Host name. */
	private final String remoteHostAddr;

	/** Remote port. */
	private final int remotePort;

	/**
	 * Constructor
	 * 
	 * @param thelocalHostName
	 *            local Host name (user/service host acting as client)
	 * @param theremoteHostAddr
	 *            Remote Host address.
	 * @param theremotePort
	 *            Remote Host port.
	 */
	public ParaverClientInterceptor(final String thelocalHostName, final String theremoteHostAddr,
			final int theremotePort) {
		this.localHostName = thelocalHostName;
		this.remoteHostAddr = theremoteHostAddr;
		this.remotePort = theremotePort;
	}

	/**
	 * Use this intercepter to trace all requests made by this client channel.
	 * 
	 * @param channel
	 *            to be traced
	 * @return intercepted channel
	 */
	public Channel intercept(final Channel channel) {
		return ClientInterceptors.intercept(channel, this);
	}

	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(final MethodDescriptor<ReqT, RespT> methodDesc,
			final CallOptions callOptions, final Channel next) {

		final ClientCall<ReqT, RespT> clientCall = new ParaverClientCall<>(localHostName, remoteHostAddr, remotePort,
				next.newCall(methodDesc, callOptions));
		return clientCall;

	}
}
