
package es.bsc.dataclay.communication.grpc.services.logicmodule;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.logic.LogicModule;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ThreadFactoryWithNamePrefix;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;

/**
 * A sample gRPC server that serve the LogicModule service.
 */
public final class LogicModuleServer {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("communication.LogicModule.server");

	/** Logic Module GRPC server. */
	private final Server server;

	/**
	 * Create a LogicModuleServer server listening on {@code port}.
	 * 
	 * @param srvName
	 *            Server name
	 * @param port
	 *            Port.
	 * @param lm
	 *            LM impl.
	 */
	public LogicModuleServer(final String srvName, final int port, final LogicModule lm) {
		final int maxMessageSize = Configuration.Flags.MAX_MESSAGE_SIZE.getIntValue();

		// Logger.getLogger("io.grpc").setLevel(Level.OFF);
		// final int maxMessageSize =
		// Configuration.Flags.MAX_MESSAGE_SIZE.getIntValue();
		final ThreadFactoryWithNamePrefix factory = new ThreadFactoryWithNamePrefix(srvName);

		final NettyServerBuilder serverBuilder = NettyServerBuilder.forPort(port);
		serverBuilder.maxMessageSize(Integer.MAX_VALUE);
		serverBuilder.maxInboundMessageSize(Integer.MAX_VALUE);
		serverBuilder.maxInboundMetadataSize(Integer.MAX_VALUE);
		// serverBuilder.maxConcurrentCallsPerConnection(Integer.MAX_VALUE);
		serverBuilder.maxHeaderListSize(Integer.MAX_VALUE);
		// serverBuilder.keepAliveTime(10, TimeUnit.SECONDS);
		// serverBuilder.keepAliveTimeout(10, TimeUnit.SECONDS);
		serverBuilder.executor(Executors.newCachedThreadPool(factory));

		final LogicModuleService lms = new LogicModuleService(lm);

		serverBuilder.addService(lms);
		

		server = serverBuilder.build();
	}

	/**
	 * Start serving requests.
	 * 
	 * @throws IOException
	 *             If some exception occurs.
	 */
	public void start() throws IOException {
		server.start();
	}

	/** Stop serving requests and shutdown resources. */
	public void stop() {
		if (server != null) {
			server.shutdownNow();
			try {
				blockUntilShutdown();
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 * 
	 * @throws InterruptedException
	 *             If some exception occurred.
	 */
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}
}
