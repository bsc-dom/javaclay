package es.bsc.dataclay.logic;

import es.bsc.dataclay.dbhandler.DBHandlerFactory.DBHandlerType;
import es.bsc.dataclay.util.Configuration;

/**
 * Factory for the LogicModule instance.
 * 
 * <p>When instantiating the LogicModule, the specific DBHandler is also instantiated
 * (following the type in the configuration).</p>
 */
public class LogicModuleFactory {
	/**
	 * Return the new LogicModule instance.
	 * 
	 * @param name Name for the LogicModule
	 * @param hostName Hostname for the LogicModule
	 * @param port Port the LogicModule will be listening to
	 * @param inMemory inMemory flag (?)
	 * @param theexposedIPForClient IPs to be send to clients when information of a registered EE/SL is required
	 * @return The new LogicModule instance
	 * @throws InterruptedException When the actual LogicModule subtype instantiation fails
	 */
	public static LogicModule<?> initLogicModule(final String name, final String hostName, 
			final int port, final boolean inMemory, 
			final String theexposedIPForClient) throws InterruptedException {
		final DBHandlerType type = (DBHandlerType)Configuration.Flags.DB_HANDLER_TYPE_FOR_LOGICMODULE.getValue();
		switch(type) {
		case SQLITE:
			return new SQLiteLogicModule(name, hostName, port, true, theexposedIPForClient);
		default:
			throw new IllegalArgumentException("LogicModule type " + type + " not supported");
		}
	}
}