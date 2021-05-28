
package es.bsc.dataclay.communication.grpc.services.logicmodule;

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
	//	serverBuilder.maxMessageSize(Integer.MAX_VALUE);
		serverBuilder.maxInboundMessageSize(maxMessageSize);
		serverBuilder.maxInboundMetadataSize(maxMessageSize);
	//	serverBuilder.maxHeaderListSize(Integer.MAX_VALUE);
		serverBuilder.maxConcurrentCallsPerConnection(Integer.MAX_VALUE);
	//	serverBuilder.keepAliveTime(10, TimeUnit.SECONDS);
	//	serverBuilder.keepAliveTimeout(10, TimeUnit.SECONDS);
		if (Configuration.Flags.GRPC_USE_FORK_JOIN_POOL.getBooleanValue()) {
			int numThreads = Configuration.Flags.GRPC_THREADPOOL_PARALLELISM.getIntValue();
			if (numThreads == -1) {
				//serverBuilder.executor(ForkJoinPool.commonPool());
				serverBuilder.executor(new ForkJoinPool());
			} else {
				serverBuilder.executor(new ForkJoinPool(numThreads));
			}
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
