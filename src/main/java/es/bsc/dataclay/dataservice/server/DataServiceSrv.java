
/**
 * @file DataServiceSrv.java
 * @date Oct 8, 2012
 */
package es.bsc.dataclay.dataservice.server;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.clients.CommonGrpcClient;
import es.bsc.dataclay.communication.grpc.services.dataservice.DataServiceServer;
import es.bsc.dataclay.dataservice.DataService;
import es.bsc.dataclay.dbhandler.DBHandlerConf;
import es.bsc.dataclay.exceptions.metadataservice.ExecutionEnvironmentAlreadyExistsException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.configs.CfgDataService;
import es.bsc.dataclay.util.configs.CfgDataServiceEnvLoader;
import es.bsc.dataclay.util.configs.CfgLogic;
import es.bsc.dataclay.util.configs.CfgLogicEnvLoader;

/**
 * This class implements the service part of the Data Service.
 * 
 */
public final class DataServiceSrv {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("DataService.srv");

	/** Server name. */
	private final String srvName;

	/** Grpc server. */
	private final DataServiceServer grpcServer;

	/** Grpc client. */
	private CommonGrpcClient grpcClient;

	// CHECKSTYLE:OFF
	/** Indicates if server is running or not. */
	public boolean runningServer;
	// CHECKSTYLE:ON

	/** Indicates if server has opened client connections with LM or not. */
	private boolean dSconnectedToLM;

	/** DataServices reference. */
	private final DataService dataService;

	/** DS configuration. */
	private final CfgDataService cfgDS;

	/** Logic Module configuration. */
	private final CfgLogic cfgLM;

	/**
	 * Start server
	 * @param thecfgDS
	 *            DS configuration
	 * @param thecfgLM
	 *            LM configuration
	 * @throws Exception
	 *             if some exception occurs
	 */
	public DataServiceSrv(final CfgDataService thecfgDS, final CfgLogic thecfgLM) throws Exception {
		// Handler info
		cfgDS = thecfgDS;
		cfgLM = thecfgLM;
		final DBHandlerConf dbHandlerConf = cfgDS.getDbHandlerConf();
		// Create DataService
		dataService = new DataService(cfgDS.getName(), cfgDS.getHostname(),
				cfgDS.getTcpPort(), dbHandlerConf, this);
		grpcServer = new DataServiceServer(cfgDS.getTcpPort(), dataService);
		srvName = cfgDS.getName();
	}

	/**
	 * Start service.
	 * @throws Exception
	 *             if some exception occurs
	 */
	public void start() throws Exception {

		grpcServer.start();

		// Initialize client
		// Connect to the LogicModule and register the DataService
		CfgLogicEnvLoader.parseConfiguration();

		//
		boolean registered = false;
		short retry = 0;
		final short maxRetries = Configuration.Flags.MAX_RETRY_AUTOREGISTER.getShortValue();
		while (!registered) {
			try {
				dataService.initLocalWithAutoregistration(cfgLM.getLogicModuleHostName(),
						cfgLM.getLogicModuleTCPPort(),
						cfgDS.getName());
				registered = true;

			} catch (final ExecutionEnvironmentAlreadyExistsException es) {
				registered = true;
				// already registered
			} catch (final Exception e) {
				retry++;
				if (retry > maxRetries) {
					throw e;
				}
				logger.info("[{}] LM not started yet. Retrying...", srvName);
				logger.debug("Catched Exception in iteration", e);
				Thread.sleep(Configuration.Flags.RETRY_AUTOREGISTER_TIME.getLongValue());
			}
		}

		grpcClient = dataService.runtime.getCommonGrpcClient();
		final String serviceName = cfgDS.getName();
		final Thread shutdownHook = new Thread() {
			@Override
			public void run() {
				try {
					if (runningServer) {
						stopService();
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
				System.err.println("DATASERVICE GRACEFULLY STOPPED :)");
			}
		};
		shutdownHook.setName(srvName + "-ShutdownHook");
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		runningServer = true;
		dSconnectedToLM = true;
		final String content = "READY";
		Files.write(Paths.get(Configuration.Flags.STATE_FILE_PATH.getStringValue()), content.getBytes());
		grpcServer.blockUntilShutdown();
	}

	/**
	 * Unbinds the service
	 */
	public void stopService() {
		if (dataService != null) {

			// ==== Ask and Notify LM === //
			runningServer = false;

			// ==== Store metaData cache === //
			logger.debug("[{}] Updating all objects in memory...", srvName);
			dataService.shutdownUpdate();
			logger.debug("[{}] Persisting EE static information...", srvName);
			dataService.persistEEInfo();
			logger.debug("[{}] Finishing cached threads...", srvName);
			dataService.finishCacheThreads();

			disconnectFromOthers();
			logger.debug("[{}] Finishing server connections...", srvName);
			grpcClient.finishClientConnections();
			grpcServer.stop();

			logger.info("[{}] Stop service procedure done!", srvName);
		}
	}

	/**
	 * Disconnect DS from others
	 */
	public void disconnectFromOthers() {
		// This is done so all client connections should be closed before closing server connections.
		if (dSconnectedToLM) {
			dataService.finishClientConnections();
			dSconnectedToLM = false;
		}
	}

	/**
	 * 
	 * @param args
	 *            args[0] == the manager configuration file path with the configuration for backends and so on
	 * @throws Exception
	 *             if some exception was thrown
	 */
	public static void main(final String[] args) throws Exception {
		final CfgDataService cfgDS = CfgDataServiceEnvLoader.parseConfiguration();
		final CfgLogic cfgLM = CfgLogicEnvLoader.parseConfiguration();
		new DataServiceSrv(cfgDS, cfgLM).start();
	}

	/**
	 * 
	 * @param args
	 *            args[0] == the manager configuration file path with the configuration for backends and so on
	 * @throws Exception
	 *             if some exception was thrown
	 * @return DSSrv for test.
	 */
	public static DataServiceSrv getForTest(final String[] args) throws Exception {
		final CfgDataService cfgDS = CfgDataServiceEnvLoader.parseConfiguration();
		final CfgLogic cfgLM = CfgLogicEnvLoader.parseConfiguration();
		return new DataServiceSrv(cfgDS, cfgLM);
	}

	/**
	 * Used in mock testing
	 * @return DataSercice
	 */
	public DataService getDataService() {
		return dataService;
	}
}
