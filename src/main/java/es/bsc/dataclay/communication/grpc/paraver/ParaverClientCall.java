
package es.bsc.dataclay.communication.grpc.paraver;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.paraver.Paraver;
import es.bsc.dataclay.paraver.TraceType;
import es.bsc.dataclay.util.Configuration;
import io.grpc.ClientCall;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.Metadata;

/**
 * Class intended to intercept GRPC client calls for Paraver traces.
 * 
 * @param <ReqT>
 *            Request type.
 * @param <RespT>
 *            Response type.
 */
public final class ParaverClientCall<ReqT, RespT> extends SimpleForwardingClientCall<ReqT, RespT> {

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("Paraver");

	// CHECKSTYLE:OFF
	/** Message ID header key. */
	public static Metadata.Key<String> MSGID_KEY = Metadata.Key.of("MessageID", Metadata.ASCII_STRING_MARSHALLER);

	/** Client port header key. */
	public static Metadata.Key<String> CLIENTPORT_KEY = Metadata.Key.of("ClientPort", Metadata.ASCII_STRING_MARSHALLER);
	// CHECKSTYLE:ON

	/**
	 * Current message ID. We inject this information to 'map' communications since
	 * we do not have ClientIP.
	 */
	private static AtomicInteger currentMessageID = new AtomicInteger(0);

	/** Local host name. */
	private final String localHostName;

	/** Remote Host name. */
	private final String remoteHostAddr;

	/** Remote port. */
	private final int remotePort;

	/** Current request ID. */
	private int curRequestID;

	/** Response listener. */
	private ParaverClientCallListener<RespT> paraverResponseListener;

	/**
	 * Constructor.
	 * 
	 * @param thelocalHostName
	 *            local Host name.
	 * @param theremoteHostAddr
	 *            Remote Host address.
	 * @param theremotePort
	 *            Remote Host port.
	 * @param nextCall
	 *            Next delegate call.
	 */
	public ParaverClientCall(final String thelocalHostName, final String theremoteHostAddr, final int theremotePort,
			final ClientCall<ReqT, RespT> nextCall) {
		super(nextCall);
		this.localHostName = thelocalHostName;
		this.remoteHostAddr = theremoteHostAddr;
		this.remotePort = theremotePort;
	}

	@Override
	public void start(final Listener<RespT> responseListener, final Metadata headers) {
		curRequestID = currentMessageID.incrementAndGet();
		headers.put(MSGID_KEY, String.valueOf(curRequestID));
		paraverResponseListener = new ParaverClientCallListener<>(this.localHostName, this.remoteHostAddr,
				this.remotePort, curRequestID, responseListener);

		if (DEBUG_ENABLED) {
			logger.debug("[" + localHostName + " -> " + remoteHostAddr + ":" + remotePort + "] Request " + curRequestID
					+ ": start client call in thread " + Thread.currentThread().getId());
		}
		delegate().start(paraverResponseListener, headers);
	}

	@Override
	public void sendMessage(final ReqT message) {
		Paraver.traceSendCommunicationGrpc(TraceType.SEND_REQUEST, localHostName, 0, curRequestID, remoteHostAddr,
				remotePort, 0);
		if (DEBUG_ENABLED) {
			logger.debug("[" + localHostName + " -> " + remoteHostAddr + ":" + remotePort + "] Request " + curRequestID
					+ ": sending message in thread " + Thread.currentThread().getId());
		}
		delegate().sendMessage(message);
	}
}
