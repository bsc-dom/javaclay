
/**
 * @file LogicModule.java
 * 
 * @date Oct 25, 2012
 */
package es.bsc.dataclay.logic.server;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.services.logicmodule.LogicModuleServer;
import es.bsc.dataclay.logic.LogicModule;
import es.bsc.dataclay.logic.LogicModuleFactory;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.configs.CfgLogic;
import es.bsc.dataclay.util.configs.CfgLogicEnvLoader;

/**
 * This class implements the service part of the Logic Module.
 * 
 */
public final class LogicModuleSrv {
	private static final Logger logger = LogManager.getLogger("LogicModule.srv");

	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();
	/** Server name. */
	private final String srvName;

	/** Logic module TCP port. */
	private final int tcpPort;

	/** In memory */
	private final boolean inMemory;

	/** Host name. */
	private final String hostname;

	/** Logic module instance. */
	private LogicModule<?> logicModule;

	/** Grpc server. */
	private LogicModuleServer grpcServer;
	// CHECKSTYLE:OFF
	/** Indicates if server is running. */
	public boolean running;
	/** Logic Module configuration. */
	private final CfgLogic cfgLM;
	// CHECKSTYLE:ON

	/**
	 * LogicModuleSrv constructor
	 * 
	 * @param thecfgLM
	 *            LM configuration
	 * @throws InterruptedException
	 *             if GRPC cannot be started
	 */
	public LogicModuleSrv(final CfgLogic thecfgLM) throws InterruptedException {
		// Initialize client
		cfgLM = thecfgLM;
		tcpPort = cfgLM.getLogicModuleTCPPort();
		hostname = cfgLM.getLogicModuleHostName();
		srvName = cfgLM.getLogicModuleName();
		inMemory = cfgLM.isInMemory();

		// Create the LM
		logicModule = LogicModuleFactory.initLogicModule(srvName, 
				hostname, tcpPort, inMemory, cfgLM.getExposedIPForClient());

		// Bind the service
		grpcServer = new LogicModuleServer(srvName, tcpPort, logicModule);
	}

	/**
	 * @param args
	 *            Containing the config file
	 * @throws Exception
	 *             if an exception occurs
	 */
	public static void main(final String[] args) throws Exception {
		// Logic module basic config
		final CfgLogic thecfgLogic = CfgLogicEnvLoader.parseConfiguration();
		new LogicModuleSrv(thecfgLogic).start();
	}

	/**
	 * Start service.
	 * 
	 * @throws Exception
	 *             if some exception occurs
	 */
	public void start() throws Exception {

		final Thread shutdownHook = new Thread() {
			@Override
			public void run() {
				try {
					if (running) {
						stopService();
						logger.info("LOGICMODULE STOPPED BY SHUTDOWNHOOK.");
					}
				} catch (final Exception e) {
					logger.error("Exception while stopping LogicModuleSrv service", e);
				}
			}
		};
		shutdownHook.setName(srvName + "-ShutdownHook");
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		grpcServer.start();
		running = true;
		final String content = "READY";
		Files.write(Paths.get(Configuration.Flags.STATE_FILE.getStringValue()), content.getBytes());
		grpcServer.blockUntilShutdown();

	}

	/**
	 * Unbinds the service
	 * 
	 * @throws Exception
	 *             if an exception occurs
	 */
	public void stopService() throws Exception {
		logicModule.setShuttingDown(true);
		running = false;

		// CHECKSTYLE:ON
		logicModule.finishCacheThreads();

		logger.debug("Closing managers...");
		logicModule.closeManagerDb();

		logger.debug("Closing logic DB...");
		logicModule.closeDb();

		logicModule = null;
		logger.debug("Finishing server connections...");
		grpcServer.stop();
		grpcServer = null;

		logger.info("stopService done!");
	}

	/**
	 * Clean MD caches
	 * 
	 * @throws Exception
	 *             if an exception occurs
	 */
	public void cleanMDCaches() throws Exception {
		logicModule.cleanMetaDataCaches();
	}

	/**
	 * @return the reference to LM
	 */
	public LogicModule<?> getLogicModule() {
		return logicModule;
	}

	/**
	 * Wait for all asynchronous request to finish.
	 */
	public void waitAndProcessAllAsyncRequests() {
		logicModule.waitAndProcessAllAsyncRequests();
	}

	/**
	 * Return the TCP Port for the LogicModule.
	 * 
	 * @return The TCP port.
	 */
	public int getTcpPort() {
		return tcpPort;
	}

	/**
	 * Return the hostname of the LogicModule.
	 * 
	 * @return The hostname string.
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * 
	 * @param args
	 *            args[0] == the manager configuration file path with the
	 *            configuration for backends and so on
	 * @throws Exception
	 *             if some exception was thrown
	 * @return LogicModuleSrv for test.
	 */
	public static LogicModuleSrv getForTest(final String[] args) throws Exception {
		// Logic module basic config
		final CfgLogic thecfgLogic = CfgLogicEnvLoader.parseConfiguration();
		return new LogicModuleSrv(thecfgLogic);
	}

}
