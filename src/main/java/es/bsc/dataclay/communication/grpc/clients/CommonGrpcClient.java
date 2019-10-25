
package es.bsc.dataclay.communication.grpc.clients;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.clients.dataservice.DataServiceGrpcClient;
import es.bsc.dataclay.communication.grpc.clients.logicmodule.LogicModuleGrpcClient;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.util.Configuration;
import io.grpc.Metadata;

/**
 * Class containing common methods for GRPC clients.
 */
public final class CommonGrpcClient {
	/** Logger. */
	private final Logger logger;

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Origin host name. */
	private final String originHostName;

	/** Logic Module clients. */
	private final Map<String, LogicModuleGrpcClient> allLMclients = new HashMap<>();

	/** DataService clients. */
	private final Map<String, DataServiceGrpcClient> allDSclients = new HashMap<>();

	/** Custom header key. */
	public static final Metadata.Key<String> SERVICE_ALIAS_HEADER_KEY =
		      Metadata.Key.of("service-alias", Metadata.ASCII_STRING_MARSHALLER);
	
	/**
	 * Constructor.
	 * @param hostName
	 *            Name of the origin host
	 * @throws InterruptedException
	 *             If connection was interrupted
	 */
	public CommonGrpcClient(final String hostName) throws InterruptedException {
		originHostName = hostName;
		logger = LogManager.getLogger("grpc.client.common." + originHostName);
	}

	/**
	 * Get a Netty Logic Module API
	 * @param targetIP
	 *            IP of the server
	 * @param targetPort
	 *            Port of the server
	 * @return A new TCPLogicModuleAPI
	 * @throws InterruptedException
	 *             If connection was interrupted
	 */
	public LogicModuleAPI getLogicModuleAPI(final String targetIP,
			final int targetPort)
			throws InterruptedException {
		final String completeAddr = targetIP + targetPort;
		LogicModuleGrpcClient client = allLMclients.get(completeAddr);
		if (client == null) {
			client = new LogicModuleGrpcClient(targetIP, targetPort);
			allLMclients.put(completeAddr, client);
			try {
				client.checkAlive();
			} catch (final Exception ex) {
				throw new InterruptedException("Unable to connect to LogicModule");
			}
		}
		return client;
	}

	/**
	 * Get a Netty DataService API
	 * @param targetIP
	 *            IP of the server
	 * @param targetPort
	 *            Port of the server
	 * @return A new DataServiceAPI
	 * @throws InterruptedException
	 *             If connection was interrupted
	 */
	public DataServiceAPI getDataServiceAPI(final String targetIP,
			final int targetPort)
			throws InterruptedException {
		final String completeAddr = targetIP + targetPort;
		DataServiceGrpcClient client = allDSclients.get(completeAddr);
		if (client == null) {
			client = new DataServiceGrpcClient(this.originHostName, targetIP, targetPort);
			allDSclients.put(completeAddr, client);
		}
		return client;
	}

	/**
	 * Finish client connections.
	 */
	public void finishClientConnections() {
		try {
			for (final LogicModuleGrpcClient client : allLMclients.values()) {
				client.shutdown();
			}
			for (final DataServiceGrpcClient client : allDSclients.values()) {
				client.shutdown();
			}
		} catch (final Exception ex) {
			logger.debug("finishClientconnections error", ex);
		}
	}

	/**
	 * Wait to process all async requests.
	 */
	public void waitAndProcessAllAsyncRequests() {
		try {
			for (final LogicModuleGrpcClient client : allLMclients.values()) {
				client.waitAndProcessAllAsyncRequests();
			}
			for (final DataServiceGrpcClient client : allDSclients.values()) {
				client.waitAndProcessAllAsyncRequests();
			}
		} catch (final Exception ex) {
			logger.debug("waitAndProcessAllAsyncRequests error", ex);
		}
	}

	/**
	 * Wait to process all pending objects.
	 */
	public void registerPendingObjects() {
		try {
			for (final DataServiceGrpcClient client : allDSclients.values()) {
				client.registerPendingObjects();
			}
		} catch (final Exception ex) {
			logger.debug("registerPendingObjects error", ex);
		}
	}

}
