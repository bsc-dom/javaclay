
package es.bsc.dataclay.communication.grpc.services.dataservice;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.ServerChannel;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.epoll.EpollServerSocketChannel;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.dataservice.DataService;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ThreadFactoryWithNamePrefix;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;

/**
 * A sample gRPC server that serve the DataServiceServer service.
 */
public final class DataServiceServer {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("communication.DataService.server");

	/** GRPC server. */
	private final Server server;

	/**
	 * Create a DataServiceServer server listening on {@code port}.
	 * 
	 * @param port
	 *            Port
	 * @param ds
	 *            DS implementation
	 */
	public DataServiceServer(final int port, final DataService ds) {
		final int maxMessageSize = Configuration.Flags.MAX_MESSAGE_SIZE.getIntValue();
		final ThreadFactoryWithNamePrefix factory = new ThreadFactoryWithNamePrefix(ds.dsName);
		final NettyServerBuilder serverBuilder = NettyServerBuilder.forPort(port);

		// serverBuilder.maxMessageSize(maxMessageSize);
		serverBuilder.maxInboundMessageSize(maxMessageSize);
		serverBuilder.maxInboundMetadataSize(maxMessageSize);
		// serverBuilder.maxConcurrentCallsPerConnection(Integer.MAX_VALUE);
		// serverBuilder.keepAliveTimeout(Integer.MAX_VALUE, TimeUnit.SECONDS);
		// serverBuilder.flowControlWindow(Integer.MAX_VALUE);
		// serverBuilder.permitKeepAliveWithoutCalls(true);
		if (Configuration.Flags.GRPC_USE_FORK_JOIN_POOL.getBooleanValue()) {
			serverBuilder.executor(ForkJoinPool.commonPool());
		} else {
			serverBuilder.executor(Executors.newCachedThreadPool(factory));
		}
		int numBossThreads = Configuration.Flags.GRPC_BOSS_NUM_THREADS.getIntValue();
		int numWorkerThreads = Configuration.Flags.GRPC_WORKER_NUM_THREADS.getIntValue();
		EventLoopGroup eventLoopGroupBoss = null;
		EventLoopGroup eventLoopGroupWorker = null;
		Class<? extends ServerChannel> channelType = null;
		if (Configuration.Flags.GRPC_USER_EPOLL_THREAD_POOL.getBooleanValue()) {
			eventLoopGroupBoss = new EpollEventLoopGroup();
			eventLoopGroupWorker = new EpollEventLoopGroup();
			channelType = EpollServerSocketChannel.class;
		} else {
			eventLoopGroupBoss = new NioEventLoopGroup();
			eventLoopGroupWorker = new NioEventLoopGroup();
			channelType = NioServerSocketChannel.class;
		}

		if (numBossThreads != -1) {
			if (Configuration.Flags.GRPC_USER_EPOLL_THREAD_POOL.getBooleanValue()) {
				eventLoopGroupBoss = new EpollEventLoopGroup(numBossThreads, factory);
			} else {
				eventLoopGroupBoss = new NioEventLoopGroup(numBossThreads, factory);
			}
		}
		if (numWorkerThreads != -1) {
			if (Configuration.Flags.GRPC_USER_EPOLL_THREAD_POOL.getBooleanValue()) {
				eventLoopGroupWorker = new EpollEventLoopGroup(numWorkerThreads, factory);
			} else {
				eventLoopGroupWorker = new NioEventLoopGroup(numWorkerThreads, factory);
			}

		}
		serverBuilder.bossEventLoopGroup(eventLoopGroupBoss);
		serverBuilder.workerEventLoopGroup(eventLoopGroupWorker);
		serverBuilder.channelType(channelType);


		final DataServiceService dss = new DataServiceService(ds);
		serverBuilder.addService(dss);
		server = serverBuilder.build();
	}

	/**
	 * Start serving requests.
	 * 
	 * @throws IOException
	 *             IO exception.
	 */
	public void start() throws IOException {
		server.start();
		/*
		 * Runtime.getRuntime().addShutdownHook(new Thread() {
		 * 
		 * @Override public void run() { // Use stderr here since the logger may has
		 * been reset by its JVM shutdown hook.
		 * System.err.println("*** shutting down gRPC server since JVM is shutting down"
		 * ); DataServiceServer.this.stop(); System.err.println("*** server shut down");
		 * } });
		 */
	}

	/** Stop serving requests and shutdown resources. */
	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 * 
	 * @throws InterruptedException
	 *             Interrupted Exception.
	 */
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

}
