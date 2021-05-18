
package es.bsc.dataclay.logic.classmgr;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Annotation;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantClassInfo;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantOperationInfo;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantPropertyInfo;
import es.bsc.dataclay.util.management.classmgr.LanguageDependantTypeInfo;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.classmgr.features.ArchitectureFeature;
import es.bsc.dataclay.util.management.classmgr.features.CPUFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature;
import es.bsc.dataclay.util.management.classmgr.features.LanguageFeature;
import es.bsc.dataclay.util.management.classmgr.features.MemoryFeature;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;
import es.bsc.dataclay.util.management.classmgr.java.JavaClassInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaImplementation;
import es.bsc.dataclay.util.management.classmgr.java.JavaOperationInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaPropertyInfo;
import es.bsc.dataclay.util.management.classmgr.java.JavaTypeInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonClassInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonImplementation;
import es.bsc.dataclay.util.management.classmgr.python.PythonOperationInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonPropertyInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonTypeInfo;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Class manager data base.
 */
public final class ClassManagerDB {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("managers.ClassManager.DB");

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** DataSource. */
	private final SQLiteDataSource dataSource;

	/**
	 * ClassManagerDB constructor.
	 * 
	 * @param dataSource
	 *            Name of the LM service managing.
	 */
	public ClassManagerDB(final SQLiteDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Create tables.
	 */
	public void createTables() {
		synchronized (dataSource) {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				for (final ClassManagerSQLStatements.SqlStatements stmt : ClassManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("CREATE_TYPE")) {
						try {
							final PreparedStatement createStmt = conn.prepareStatement(stmt.getSqlStatement());
							if (DEBUG_ENABLED) {
								logger.debug("[==DB==] Executing " + createStmt);
							}
							createStmt.execute();
							createStmt.close();
						} catch (final SQLException e) {
							// ignore if already exists
						}
					}
				}
			} catch (final SQLException e1) {
				logger.debug("SQL Exception in createTables (first step)", e1);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			conn = null;
			try {
				conn = dataSource.getConnection();
				for (final ClassManagerSQLStatements.SqlStatements stmt : ClassManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("CREATE_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						if (DEBUG_ENABLED) {
							logger.debug("[==DB==] Executing " + updateStatement);
						}
						updateStatement.execute();
						updateStatement.close();
					}
				}
			} catch (final SQLException e) {
				logger.debug("SQL Exception in createTables (second step)", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Delete the tables of MDS. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {
		synchronized (dataSource) {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				for (final ClassManagerSQLStatements.SqlStatements stmt : ClassManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("DROP_TYPE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						if (DEBUG_ENABLED) {
							logger.debug("[==DB==] Executing " + updateStatement);
						}
						updateStatement.execute();
						updateStatement.close();
					}
				}
			} catch (final SQLException e) {
				logger.debug("SQL Exception in dropTables (first step)", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			conn = null;
			try {
				conn = dataSource.getConnection();
				for (final ClassManagerSQLStatements.SqlStatements stmt : ClassManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("DROP_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						if (DEBUG_ENABLED) {
							logger.debug("[==DB==] Executing " + updateStatement);
						}
						updateStatement.execute();
						updateStatement.close();
					}
				}
			} catch (final SQLException e) {
				logger.debug("SQL Exception in dropTables (second step)", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Store accessedImplementation into database
	 * @param accessedImplementation
	 *            accessedImplementation
	 * @return UUID of stored object
	 */
	public UUID storeAccessedImplementation(final AccessedImplementation accessedImplementation) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_ACCESSED_IMPL.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, accessedImplementation.getNamespace());
				insertStatement.setString(3, accessedImplementation.getClassName());
				insertStatement.setString(4, accessedImplementation.getOpSignature());
				insertStatement.setInt(5, accessedImplementation.getImplPosition());
				insertStatement.setObject(6, accessedImplementation.getImplementationID().getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeAccessedImplementation", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store accessedProperty into database
	 * @param accessedProperty
	 *            accessedProperty
	 * @return UUID of stored object
	 */
	public UUID storeAccessedProperty(final AccessedProperty accessedProperty) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_ACCESSED_PROP.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, accessedProperty.getNamespace());
				insertStatement.setString(3, accessedProperty.getClassName());
				insertStatement.setString(4, accessedProperty.getName());
				insertStatement.setObject(5, accessedProperty.getPropertyID().getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeAccessedProperty", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}

	}

	/**
	 * Store type into database
	 * @param type
	 *            type
	 * @return UUID of stored object
	 */
	public UUID storeType(final Type type) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				UUID[] includesArray = null;
				if (type.getIncludes() != null) {
					includesArray = new UUID[type.getIncludes().size()];
					int j = 0;
					for (final Type include : type.getIncludes()) {
						includesArray[j] = storeType(include);
						j++;
					}
				}

				String[] extensionsTypeArray = null;
				UUID[] extensionsArray = null;
				if (type.getLanguageDepInfos() != null) {

					extensionsTypeArray = new String[type.getLanguageDepInfos().size()];
					extensionsArray = new UUID[type.getLanguageDepInfos().size()];
					int i = 0;
					for (final Entry<Langs, LanguageDependantTypeInfo> entry : type.getLanguageDepInfos().entrySet()) {
						extensionsTypeArray[i] = entry.getKey().name();
						if (entry.getKey().equals(Langs.LANG_JAVA)) {
							extensionsArray[i] = storeJavaTypeInfo((JavaTypeInfo) entry.getValue());
						} else if (entry.getKey().equals(Langs.LANG_PYTHON)) {
							extensionsArray[i] = storePythonTypeInfo((PythonTypeInfo) entry.getValue());
						}
						i++;
					}

				}

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_TYPE.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, type.getDescriptor());
				insertStatement.setString(3, type.getSignature());
				insertStatement.setString(4, type.getTypeName());

				if (includesArray != null) {
					final Array tArray = insertStatement.getConnection().createArrayOf("uuid", includesArray);
					insertStatement.setArray(5, tArray);
				} else {
					insertStatement.setArray(5, null);
				}

				if (type instanceof UserType) {
					final UserType utype = (UserType) type;
					insertStatement.setString(6, utype.getNamespace());
					insertStatement.setObject(7, utype.getClassID().getId());
				} else {
					insertStatement.setString(6, null);
					insertStatement.setObject(7, null);
				}

				if (extensionsTypeArray != null) {
					final Array sqlArray = insertStatement.getConnection().createArrayOf("varchar", extensionsTypeArray);
					insertStatement.setArray(8, sqlArray);
				} else {
					insertStatement.setArray(8, null);
				}

				if (extensionsArray != null) {
					final Array sqlArray = insertStatement.getConnection().createArrayOf("uuid", extensionsArray);
					insertStatement.setArray(9, sqlArray);
				} else {
					insertStatement.setArray(9, null);
				}

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();

			} catch (final Exception e) {
				logger.debug("SQL Exception in storeType", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}

	}

	/**
	 * Store JavaTypeInfo into database
	 * @param type
	 *            JavaTypeInfo
	 * @return UUID of stored object
	 */
	public UUID storeJavaTypeInfo(final JavaTypeInfo type) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_JAVA_TYPE.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeJavaTypeInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store PythonTypeInfo into database
	 * @param type
	 *            PythonTypeInfo
	 * @return UUID of stored object
	 */
	public UUID storePythonTypeInfo(final PythonTypeInfo type) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_PYTHON_TYPE.getSqlStatement());
				insertStatement.setObject(1, uuid);
				insertStatement.setString(2, type.getSignature());
				// CHECKSTYLE:OFF
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storePythonTypeInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store MemoryFeature into database
	 * @param memFeature
	 *            MemoryFeature
	 * @return UUID of stored object
	 */
	public UUID storeMemoryFeature(final MemoryFeature memFeature) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_MEMORY_FEATURE.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setInt(2, memFeature.getCapacityInMB());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeMemoryFeature", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}

	}

	/**
	 * Store CPUFeature into database
	 * @param feature
	 *            CPUFeature
	 * @return UUID of stored object
	 */
	public UUID storeCPUFeature(final CPUFeature feature) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_CPU_FEATURE.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setInt(2, feature.getAmount());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeCPUFeature", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}

	}

	/**
	 * Store LanguageFeature into database
	 * @param feature
	 *            LanguageFeature
	 * @return UUID of stored object
	 */
	public UUID storeLanguageFeature(final LanguageFeature feature) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_LANGUAGE_FEATURE.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, feature.getLanguageName());
				insertStatement.setString(3, feature.getVersion());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeLanguageFeature", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store ArchitectureFeature into database
	 * @param feature
	 *            ArchitectureFeature
	 * @return UUID of stored object
	 */
	public UUID storeArchitectureFeature(final ArchitectureFeature feature) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_ARCH_FEATURE.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, feature.getArchitectureName());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeArchitectureFeature", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}

	}

	/**
	 * Store prefetchingInfo into database
	 * @param prefetchingInfo
	 *            prefetchingInfo
	 * @return UUID of stored object
	 */
	public UUID storePrefecthingInfo(final PrefetchingInformation prefetchingInfo) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_PREFETCHING_INFO.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setBoolean(2, prefetchingInfo.getDisableDynamicPrefetching());
				insertStatement.setBoolean(3, prefetchingInfo.getInjectPrefetchingCall());
				insertStatement.setString(4, prefetchingInfo.getPrefetchingNameSpace());
				insertStatement.setString(5, prefetchingInfo.getPrefetchingClassName());
				insertStatement.setString(6, prefetchingInfo.getPrefetchingMethodSignature());
				if (prefetchingInfo.getPrefetchingImplementationID() != null) {
					insertStatement.setObject(7, prefetchingInfo.getPrefetchingImplementationID().getId());
				} else {
					insertStatement.setObject(7, null);
				}
				if (prefetchingInfo.getPrefetchingClassID() != null) {
					insertStatement.setObject(8, prefetchingInfo.getPrefetchingClassID().getId());
				} else {
					insertStatement.setObject(8, null);
				}
				if (prefetchingInfo.getPropertiesToPrefetch() != null) {
					final UUID[][] accProps = new UUID[prefetchingInfo.getPropertiesToPrefetch().size()][];
					int i = 0;
					for (final List<Property> accPropsList : prefetchingInfo.getPropertiesToPrefetch()) {
						int j = 0;
						accProps[i] = new UUID[accPropsList.size()];
						for (final Property prop : accPropsList) {
							accProps[i][j] = prop.getDataClayID().getId();
							j++;
						}
						i++;
					}

					if (accProps.length > 0) {
						final Array sqlArray = insertStatement.getConnection().createArrayOf("uuid", accProps);
						insertStatement.setArray(9, sqlArray);
					} else {
						insertStatement.setArray(9, null);
					}
				} else {
					insertStatement.setArray(9, null);
				}

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storePrefetchingInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store JavaImplementation into database
	 * @param implementation
	 *            implementation
	 * @return UUID of stored object
	 */
	public UUID storeJavaImplementation(final JavaImplementation implementation) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = implementation.getDataClayID().getId();
			try {

				final UUID[] includes = new UUID[implementation.getIncludes().size()];
				int i = 0;
				for (final Type include : implementation.getIncludes()) {
					includes[i] = this.storeType(include);
					i++;
				}

				final UUID[] accessedProps = new UUID[implementation.getAccessedProperties().size()];
				i = 0;
				for (final AccessedProperty accProp : implementation.getAccessedProperties()) {
					accessedProps[i] = this.storeAccessedProperty(accProp);
					i++;
				}

				final UUID[] accessedImpls = new UUID[implementation.getAccessedImplementations().size()];
				i = 0;
				for (final AccessedImplementation accImpl : implementation.getAccessedImplementations()) {
					accessedImpls[i] = this.storeAccessedImplementation(accImpl);
					i++;
				}

				final String[] featureTypes = new String[implementation.getRequiredQualitativeFeatures().size()
						+ implementation.getRequiredQuantitativeFeatures().size()];
				final UUID[] features = new UUID[featureTypes.length];
				i = 0;
				for (final Entry<FeatureType, QualitativeFeature> entry : implementation.getRequiredQualitativeFeatures().entrySet()) {
					featureTypes[i] = entry.getKey().name();
					switch (entry.getKey()) {
						case ARCHITECTURE:
							features[i] = this.storeArchitectureFeature((ArchitectureFeature) entry.getValue());
							break;
						case LANGUAGE:
							features[i] = this.storeLanguageFeature((LanguageFeature) entry.getValue());
							break;
						default:
							break;

					}
					i++;
				}
				for (final Entry<FeatureType, QuantitativeFeature> entry : implementation.getRequiredQuantitativeFeatures().entrySet()) {
					featureTypes[i] = entry.getKey().name();
					switch (entry.getKey()) {
						case CPU:
							features[i] = this.storeCPUFeature((CPUFeature) entry.getValue());
							break;
						case MEMORY:
							features[i] = this.storeMemoryFeature((MemoryFeature) entry.getValue());
							break;
						default:
							break;

					}
					i++;
				}

				UUID prefetchingInfoUUID = null;
				if (implementation.getPrefetchingInfo() != null) {
					prefetchingInfoUUID = this.storePrefecthingInfo(implementation.getPrefetchingInfo());
				}

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_JAVA_IMPLEMENTATION.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, implementation.getResponsibleAccountName());
				insertStatement.setString(3, implementation.getNamespace());
				insertStatement.setString(4, implementation.getClassName());
				insertStatement.setString(5, implementation.getOpNameAndDescriptor());
				insertStatement.setInt(6, implementation.getPosition());

				Array sqlArray = insertStatement.getConnection().createArrayOf("uuid", includes);
				insertStatement.setArray(7, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("uuid", accessedProps);
				insertStatement.setArray(8, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("uuid", accessedImpls);
				insertStatement.setArray(9, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("varchar", featureTypes);
				insertStatement.setArray(10, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("uuid", features);
				insertStatement.setArray(11, sqlArray);

				insertStatement.setObject(12, implementation.getOperationID().getId());
				insertStatement.setObject(13, implementation.getMetaClassID().getId());
				insertStatement.setObject(14, implementation.getResponsibleAccountID().getId());
				insertStatement.setObject(15, implementation.getNamespaceID().getId());
				insertStatement.setObject(16, prefetchingInfoUUID);

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeJavaImplementation", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store PythonImplementation into database
	 * @param implementation
	 *            implementation
	 * @return UUID of stored object
	 */
	public UUID storePythonImplementation(final PythonImplementation implementation) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = implementation.getDataClayID().getId();
			try {

				final UUID[] includes = new UUID[implementation.getIncludes().size()];
				int i = 0;
				for (final Type include : implementation.getIncludes()) {
					includes[i] = this.storeType(include);
					i++;
				}

				final UUID[] accessedProps = new UUID[implementation.getAccessedProperties().size()];
				i = 0;
				for (final AccessedProperty accProp : implementation.getAccessedProperties()) {
					accessedProps[i] = this.storeAccessedProperty(accProp);
					i++;
				}

				final UUID[] accessedImpls = new UUID[implementation.getAccessedImplementations().size()];
				i = 0;
				for (final AccessedImplementation accImpl : implementation.getAccessedImplementations()) {
					accessedImpls[i] = this.storeAccessedImplementation(accImpl);
					i++;
				}

				final String[] featureTypes = new String[implementation.getRequiredQualitativeFeatures().size()
						+ implementation.getRequiredQuantitativeFeatures().size()];
				final UUID[] features = new UUID[featureTypes.length];
				i = 0;
				for (final Entry<FeatureType, QualitativeFeature> entry : implementation.getRequiredQualitativeFeatures().entrySet()) {
					featureTypes[i] = entry.getKey().name();
					switch (entry.getKey()) {
						case ARCHITECTURE:
							features[i] = this.storeArchitectureFeature((ArchitectureFeature) entry.getValue());
							break;
						case LANGUAGE:
							features[i] = this.storeLanguageFeature((LanguageFeature) entry.getValue());
							break;
						default:
							break;

					}
					i++;
				}
				for (final Entry<FeatureType, QuantitativeFeature> entry : implementation.getRequiredQuantitativeFeatures().entrySet()) {
					featureTypes[i] = entry.getKey().name();
					switch (entry.getKey()) {
						case CPU:
							features[i] = this.storeCPUFeature((CPUFeature) entry.getValue());
							break;
						case MEMORY:
							features[i] = this.storeMemoryFeature((MemoryFeature) entry.getValue());
							break;
						default:
							break;

					}
					i++;
				}

				UUID prefetchingInfoUUID = null;
				if (implementation.getPrefetchingInfo() != null) {
					prefetchingInfoUUID = this.storePrefecthingInfo(implementation.getPrefetchingInfo());
				}

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(
						ClassManagerSQLStatements.SqlStatements.INSERT_PYTHON_IMPLEMENTATION.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, implementation.getResponsibleAccountName());
				insertStatement.setString(3, implementation.getNamespace());
				insertStatement.setString(4, implementation.getClassName());
				insertStatement.setString(5, implementation.getOpNameAndDescriptor());
				insertStatement.setInt(6, implementation.getPosition());

				Array sqlArray = insertStatement.getConnection().createArrayOf("uuid", includes);
				insertStatement.setArray(7, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("uuid", accessedProps);
				insertStatement.setArray(8, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("uuid", accessedImpls);
				insertStatement.setArray(9, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("varchar", featureTypes);
				insertStatement.setArray(10, sqlArray);
				sqlArray = insertStatement.getConnection().createArrayOf("uuid", features);
				insertStatement.setArray(11, sqlArray);

				insertStatement.setObject(12, implementation.getOperationID().getId());
				insertStatement.setObject(13, implementation.getMetaClassID().getId());
				insertStatement.setObject(14, implementation.getResponsibleAccountID().getId());
				insertStatement.setObject(15, implementation.getNamespaceID().getId());
				insertStatement.setObject(16, prefetchingInfoUUID);
				insertStatement.setString(17, implementation.getCode());

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storePythonImplementation", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return uuid;
		}
	}

	/**
	 * Store Property into database
	 * @param property
	 *            property
	 * @return UUID of stored object
	 */
	public UUID storeProperty(final Property property) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = property.getDataClayID().getId();
			try {

				final UUID typeID = this.storeType(property.getType());

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_PROPERTY.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, property.getNamespace());
				insertStatement.setString(3, property.getClassName());
				insertStatement.setString(4, property.getName());
				insertStatement.setInt(5, property.getPosition());

				insertStatement.setObject(6, typeID);
				if (property.getGetterOperationID() != null) {
					insertStatement.setObject(7, property.getGetterOperationID().getId());
				} else {
					insertStatement.setObject(7, null);
				}
				if (property.getGetterImplementationID() != null) {
					insertStatement.setObject(8, property.getGetterImplementationID().getId());
				} else {
					insertStatement.setObject(8, null);
				}
				if (property.getSetterOperationID() != null) {
					insertStatement.setObject(9, property.getSetterOperationID().getId());
				} else {
					insertStatement.setObject(9, null);
				}
				if (property.getSetterImplementationID() != null) {
					insertStatement.setObject(10, property.getSetterImplementationID().getId());
				} else {
					insertStatement.setObject(10, null);
				}

				insertStatement.setObject(11, property.getNamespaceID().getId());
				insertStatement.setObject(12, property.getMetaClassID().getId());

				if (property.getLanguageDepInfos() != null) {
					final String[] extensionsTypeArray = new String[property.getLanguageDepInfos().size()];
					final UUID[] extensionsArray = new UUID[property.getLanguageDepInfos().size()];
					int i = 0;
					for (final Entry<Langs, LanguageDependantPropertyInfo> entry : property.getLanguageDepInfos().entrySet()) {
						extensionsTypeArray[i] = entry.getKey().name();
						if (entry.getKey().equals(Langs.LANG_JAVA)) {
							extensionsArray[i] = storeJavaPropertyInfo((JavaPropertyInfo) entry.getValue());
						} else if (entry.getKey().equals(Langs.LANG_PYTHON)) {
							extensionsArray[i] = storePythonPropertyInfo((PythonPropertyInfo) entry.getValue());
						}
						i++;
					}

					Array sqlArray = insertStatement.getConnection().createArrayOf("varchar", extensionsTypeArray);
					insertStatement.setArray(13, sqlArray);
					sqlArray = insertStatement.getConnection().createArrayOf("uuid", extensionsArray);
					insertStatement.setArray(14, sqlArray);
				} else {
					insertStatement.setArray(13, null);
					insertStatement.setArray(14, null);
				}

				final List<Annotation> annotations = property.getAnnotations();
				if (annotations != null && annotations.size() > 0) {
					final UUID[] annotationsUUIDArray = new UUID[annotations.size()];
					for (int i = 0; i < annotations.size(); i++) {
						annotationsUUIDArray[i] = storeAnnotation(annotations.get(i));
					}
					final Array sqlArray = insertStatement.getConnection().createArrayOf("uuid", annotationsUUIDArray);
					insertStatement.setArray(15, sqlArray);
				} else {
					insertStatement.setArray(15, null);
				}

				if (property.getUpdateImplementationID() != null) {
					insertStatement.setObject(16, property.getUpdateImplementationID().getId());
				} else {
					insertStatement.setObject(16, null);
				}
				if (property.getUpdateOperationID() != null) {
					insertStatement.setObject(17, property.getUpdateOperationID().getId());
				} else {
					insertStatement.setObject(17, null);
				}

				insertStatement.setString(18, property.getBeforeUpdate());
				insertStatement.setString(19, property.getAfterUpdate());
				insertStatement.setBoolean(20, property.getInMaster());

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeProperty", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store JavaPropertyInfo into database
	 * @param type
	 *            JavaPropertyInfo
	 * @return UUID of stored object
	 */
	public UUID storeJavaPropertyInfo(final JavaPropertyInfo type) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_JAVA_PROPERTY.getSqlStatement());
				insertStatement.setObject(1, uuid);
				insertStatement.setInt(2, type.getModifier());
				// CHECKSTYLE:OFF
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeJavaPropertyInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store Annotation into database
	 * @param annotation
	 *            Annotation
	 * @return UUID of stored object
	 */
	public UUID storeAnnotation(final Annotation annotation) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_ANNOTATION.getSqlStatement());
				insertStatement.setObject(1, uuid);
				insertStatement.setString(2, annotation.getDescr());

				Array sqlKeys = null, sqlValues = null;
				final Map<String, String> parameters = annotation.getParameters();
				if (parameters != null && parameters.size() > 0) {
					final String[] keys = new String[parameters.size()];
					final String[] values = new String[parameters.size()];
					int i = 0;
					final Iterator<String> iterator = annotation.getParameters().keySet().iterator();
					while (iterator.hasNext()) {
						keys[i] = iterator.next();
						values[i] = parameters.get(keys[i]);
						i++;
					}
					sqlKeys = insertStatement.getConnection().createArrayOf("varchar", keys);
					sqlValues = insertStatement.getConnection().createArrayOf("varchar", values);
				}
				insertStatement.setArray(3, sqlKeys);
				insertStatement.setArray(4, sqlValues);

				// CHECKSTYLE:OFF
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeAnnotation", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store PythonPropertyInfo into database
	 * @param type
	 *            PythonPropertyInfo
	 * @return UUID of stored object
	 */
	public UUID storePythonPropertyInfo(final PythonPropertyInfo type) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_PYTHON_PROPERTY.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storePythonPropertyInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store Operation into database
	 * @param operation
	 *            operation
	 * @return UUID of stored object
	 */
	public UUID storeOperation(final Operation operation) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = operation.getDataClayID().getId();
			try {
				// Params
				final UUID[] paramsTypes = new UUID[operation.getParams().size()];
				int i = 0;
				for (final Type paramType : operation.getParams().values()) {
					paramsTypes[i] = this.storeType(paramType);
					i++;
				}
				// Return
				final UUID returnTypeID = this.storeType(operation.getReturnType());

				// Implementations
				final UUID[] implementationsIDs = new UUID[operation.getImplementations().size()];
				i = 0;
				for (final Implementation impl : operation.getImplementations()) {
					if (impl instanceof JavaImplementation) {
						implementationsIDs[i] = this.storeJavaImplementation((JavaImplementation) impl);
					} else if (impl instanceof PythonImplementation) {
						implementationsIDs[i] = this.storePythonImplementation((PythonImplementation) impl);
					}
					i++;
				}

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_OPERATION.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, operation.getNamespace());
				insertStatement.setString(3, operation.getClassName());
				insertStatement.setString(4, operation.getDescriptor());
				insertStatement.setString(5, operation.getSignature());
				insertStatement.setString(6, operation.getName());
				insertStatement.setString(7, operation.getNameAndDescriptor());

				final String[] paramsNames = operation.getParams().keySet().toArray(new String[]{});
				Array sqlArray = insertStatement.getConnection().createArrayOf("varchar", paramsNames);
				insertStatement.setArray(8, sqlArray);

				sqlArray = insertStatement.getConnection().createArrayOf("uuid", paramsTypes);
				insertStatement.setArray(9, sqlArray);

				final String[] paramsOrder = operation.getParamsOrder().toArray(new String[]{});
				sqlArray = insertStatement.getConnection().createArrayOf("varchar", paramsOrder);
				insertStatement.setArray(10, sqlArray);

				insertStatement.setObject(11, returnTypeID);

				sqlArray = insertStatement.getConnection().createArrayOf("uuid", implementationsIDs);
				insertStatement.setArray(12, sqlArray);

				insertStatement.setBoolean(13, operation.getIsAbstract());
				insertStatement.setBoolean(14, operation.getIsStaticConstructor());

				insertStatement.setObject(15, operation.getMetaClassID().getId());
				insertStatement.setObject(16, operation.getNamespaceID().getId());

				if (operation.getLanguageDepInfos() != null) {
					final String[] extensionsTypeArray = new String[operation.getLanguageDepInfos().size()];
					final UUID[] extensionsArray = new UUID[operation.getLanguageDepInfos().size()];
					i = 0;
					for (final Entry<Langs, LanguageDependantOperationInfo> entry : operation.getLanguageDepInfos().entrySet()) {
						extensionsTypeArray[i] = entry.getKey().name();
						if (entry.getKey().equals(Langs.LANG_JAVA)) {
							extensionsArray[i] = storeJavaOperationInfo((JavaOperationInfo) entry.getValue());
						} else if (entry.getKey().equals(Langs.LANG_PYTHON)) {
							extensionsArray[i] = storePythonOperationInfo((PythonOperationInfo) entry.getValue());
						}
						i++;
					}

					sqlArray = insertStatement.getConnection().createArrayOf("varchar", extensionsTypeArray);
					insertStatement.setArray(17, sqlArray);

					sqlArray = insertStatement.getConnection().createArrayOf("uuid", extensionsArray);
					insertStatement.setArray(18, sqlArray);
				} else {
					insertStatement.setArray(17, null);
					insertStatement.setArray(18, null);
				}

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement + "(" + uuid + ")");
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeOperation", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}

	}

	/**
	 * Store JavaOperationInfo into database
	 * @param type
	 *            JavaOperationInfo
	 * @return UUID of stored object
	 */
	public UUID storeJavaOperationInfo(final JavaOperationInfo type) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_JAVA_OPERATION.getSqlStatement());
				insertStatement.setObject(1, uuid);
				insertStatement.setInt(2, type.getModifier());
				// CHECKSTYLE:OFF
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeJavaOperationInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store PythonOperationInfo into database
	 * @param type
	 *            PythonOperationInfo
	 * @return UUID of stored object
	 */
	public UUID storePythonOperationInfo(final PythonOperationInfo type) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_PYTHON_OPERATION.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storePythonOperationInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}

	}

	/**
	 * Store MetaClass into database
	 * @param metaclass
	 *            metaclass
	 * @return UUID of stored object
	 */
	public UUID storeMetaClass(final MetaClass metaclass) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = metaclass.getDataClayID().getId();
			try {

				final UUID[] properties = new UUID[metaclass.getProperties().size()];
				int i = 0;
				for (final Property prop : metaclass.getProperties()) {
					properties[i] = this.storeProperty(prop);
					i++;
				}

				final UUID[] operations = new UUID[metaclass.getOperations().size()];
				i = 0;
				for (final Operation op : metaclass.getOperations()) {
					operations[i] = this.storeOperation(op);
					i++;
				}

				UUID parentTypeID = null;
				if (metaclass.getParentType() != null) {
					parentTypeID = this.storeType(metaclass.getParentType());
				}
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_METACLASS.getSqlStatement());
				insertStatement.setObject(1, uuid);
				// CHECKSTYLE:OFF
				insertStatement.setString(2, metaclass.getNamespace());
				insertStatement.setString(3, metaclass.getName());
				insertStatement.setObject(4, parentTypeID);

				Array sqlArray = insertStatement.getConnection().createArrayOf("uuid", properties);
				insertStatement.setArray(5, sqlArray);

				sqlArray = insertStatement.getConnection().createArrayOf("uuid", operations);
				insertStatement.setArray(6, sqlArray);

				insertStatement.setBoolean(7, metaclass.getIsAbstract());
				insertStatement.setObject(8, metaclass.getNamespaceID().getId());

				final String[] extensionsTypeArray = new String[metaclass.getLanguageDepInfos().size()];
				final UUID[] extensionsArray = new UUID[metaclass.getLanguageDepInfos().size()];
				i = 0;
				for (final Entry<Langs, LanguageDependantClassInfo> entry : metaclass.getLanguageDepInfos().entrySet()) {
					extensionsTypeArray[i] = entry.getKey().name();
					if (entry.getKey().equals(Langs.LANG_JAVA)) {
						extensionsArray[i] = storeJavaClassInfo((JavaClassInfo) entry.getValue());
					} else if (entry.getKey().equals(Langs.LANG_PYTHON)) {
						extensionsArray[i] = storePythonClassInfo((PythonClassInfo) entry.getValue());
					}
					i++;
				}

				sqlArray = insertStatement.getConnection().createArrayOf("varchar", extensionsTypeArray);
				insertStatement.setArray(9, sqlArray);

				sqlArray = insertStatement.getConnection().createArrayOf("uuid", extensionsArray);
				insertStatement.setArray(10, sqlArray);

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeMetaClass", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store JavaClassInfo into database
	 * @param info
	 *            JavaClassInfo
	 * @return UUID of stored object
	 */
	public UUID storeJavaClassInfo(final JavaClassInfo info) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_JAVA_METACLASS.getSqlStatement());
				// CHECKSTYLE:OFF

				insertStatement.setObject(1, uuid);
				insertStatement.setString(2, info.getSignature());
				if (info.getJavaParentInterfaces() != null) {
					final Array sqlArray = insertStatement.getConnection().createArrayOf("varchar", info.getJavaParentInterfaces());
					insertStatement.setArray(3, sqlArray);
				} else {
					insertStatement.setArray(3, null);
				}

				insertStatement.setBytes(4, info.getClassByteCode());
				insertStatement.setInt(5, info.getModifier());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storeJavaClassInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Store PythonClassInfo into database
	 * @param info
	 *            PythonClassInfo
	 * @return UUID of stored object
	 */
	public UUID storePythonClassInfo(final PythonClassInfo info) {
		synchronized (dataSource) {
			Connection conn = null;
			final UUID uuid = UUID.randomUUID();
			try {

				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.INSERT_PYTHON_METACLASS.getSqlStatement());
				insertStatement.setObject(1, uuid);

				final String[] importx = new String[info.getImports().size()];

				int i = 0;
				for (final String entry : info.getImports()) {
					importx[i] = entry;
					i++;
				}

				final Array sqlArray = insertStatement.getConnection().createArrayOf("varchar", importx);
				insertStatement.setArray(2, sqlArray);
				// CHECKSTYLE:OFF

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				logger.debug("SQL Exception in storePythonClassInfo", e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
			return uuid;
		}
	}

	/**
	 * Deserialize type
	 * @param rs
	 *            Result set
	 * @return Type
	 */
	private Type deserializeType(final ResultSet rs) {
		synchronized (dataSource) {
			Type type = null;
			try {
				boolean isUserType = false;

				final UUID id = (UUID) rs.getObject("id");

				final String namespace = rs.getString("namespace");
				MetaClassID classID = null;
				if (namespace != null) {
					// it's user type
					isUserType = true;
					classID = new MetaClassID((UUID) rs.getObject("classID"));
				}
				final String descriptor = rs.getString("descriptor");
				final String signature = rs.getString("signature");
				final String typeName = rs.getString("typeName");
				Array sqlArray = rs.getArray("includes");
				final List<Type> includes = new ArrayList<>();
				if (sqlArray != null) {
					final UUID[] uuidArray = (UUID[]) sqlArray.getArray();
					for (final UUID includedUUID : uuidArray) {
						final Type include = getTypeByID(includedUUID);
						includes.add(include);
					}
				}

				final Map<Langs, LanguageDependantTypeInfo> extensions = new HashMap<>();
				sqlArray = rs.getArray("extendedtypes");
				if (sqlArray != null) {
					final Array extensionsArray = rs.getArray("extensions");
					final String[] typesNames = (String[]) sqlArray.getArray();
					final UUID[] uuidArray = (UUID[]) extensionsArray.getArray();
					for (int i = 0; i < typesNames.length; ++i) {
						if (typesNames[i].equals(Langs.LANG_JAVA.name())) {
							final JavaTypeInfo info = this.getJavaTypeByID(uuidArray[i]);
							extensions.put(Langs.LANG_JAVA, info);
						} else if (typesNames[i].equals(Langs.LANG_PYTHON.name())) {
							final PythonTypeInfo info = this.getPythonTypeByID(uuidArray[i]);
							extensions.put(Langs.LANG_PYTHON, info);
						}
					}
				}

				if (isUserType) {
					final UserType utype = new UserType(namespace,
							typeName, descriptor, signature, includes);
					utype.setClassID(classID);
					type = utype;
				} else {
					type = new Type(descriptor, signature, typeName, includes);
				}

				type.setId(id);
				type.setLanguageDepInfos(extensions);

			} catch (final SQLException e) {
				logger.debug("SQL Exception in deserializeType", e);
			}
			return type;
		}
	}

	/**
	 * Deserialize JavaTypeInfo
	 * @param rs
	 *            Result set
	 * @return JavaTypeInfo
	 */
	private JavaTypeInfo deserializeJavaType(final ResultSet rs) {
		final JavaTypeInfo type = new JavaTypeInfo(); // no fields
		try {
			final UUID id = (UUID)rs.getObject("id");
			type.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeJavaType", e);
		}
		return type;
	}

	/**
	 * Deserialize PythonTypeInfo
	 * @param rs
	 *            Result set
	 * @return PythonTypeInfo
	 */
	private PythonTypeInfo deserializePythonType(final ResultSet rs) {
		PythonTypeInfo type = null;
		try {
			final String signature = rs.getString("signature");
			type = new PythonTypeInfo(signature);
			final UUID id = (UUID)rs.getObject("id");
			type.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializePythonType", e);
		}
		return type;
	}

	/**
	 * Deserialize property
	 * @param rs
	 *            Result set
	 * @return Property
	 */
	private Property deserializeProperty(final ResultSet rs) {
		Property property = null;
		try {

			final PropertyID id = new PropertyID((UUID)rs.getObject("id"));
			final String namespace = rs.getString("namespace");
			final String className = rs.getString("className");
			final String name = rs.getString("name");
			final int position = rs.getInt("position");
			final Type propType = getTypeByID((UUID)rs.getObject("type"));

			OperationID getterOperationID = null;
			final UUID getterUUID = (UUID)rs.getObject("getterOperationID");
			if (getterUUID != null) {
				getterOperationID = new OperationID(getterUUID);
			}

			ImplementationID getterImplementationID = null;
			final UUID getterImplementationUUID = (UUID)rs.getObject("getterImplementationID");
			if (getterImplementationUUID != null) {
				getterImplementationID = new ImplementationID(getterImplementationUUID);
			}

			OperationID setterOperationID = null;
			final UUID setterUUID = (UUID)rs.getObject("setterOperationID");
			if (setterUUID != null) {
				setterOperationID = new OperationID(setterUUID);
			}

			ImplementationID setterImplementationID = null;
			final UUID setterImplementationUUID = (UUID)rs.getObject("setterImplementationID");
			if (setterImplementationUUID != null) {
				setterImplementationID = new ImplementationID(setterImplementationUUID);
			}

			ImplementationID updateImplementationID = null;
			if (rs.getObject("updateImplementationID") != null) {
				updateImplementationID = new ImplementationID((UUID)rs.getObject("updateImplementationID"));
			}

			OperationID updateOperationID = null;
			if (rs.getObject("updateOperationID") != null) {
				updateOperationID = new OperationID((UUID)rs.getObject("updateOperationID"));
			}

			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("namespaceID"));
			final MetaClassID metaClassID = new MetaClassID((UUID)rs.getObject("metaClassID"));

			final Map<Langs, LanguageDependantPropertyInfo> extensions = new HashMap<>();
			final Array sqlArray = rs.getArray("extendedtypes");
			if (sqlArray != null) {
				final Array extensionsArray = rs.getArray("extensions");
				final String[] typesNames = (String[]) sqlArray.getArray();
				final UUID[] uuidArray = (UUID[]) extensionsArray.getArray();
				for (int i = 0; i < typesNames.length; ++i) {
					if (typesNames[i].equals(Langs.LANG_JAVA.name())) {
						final JavaPropertyInfo info = this.getJavaPropertyByID(uuidArray[i]);
						extensions.put(Langs.LANG_JAVA, info);
					} else if (typesNames[i].equals(Langs.LANG_PYTHON.name())) {
						final PythonPropertyInfo info = this.getPythonPropertyByID(uuidArray[i]);
						extensions.put(Langs.LANG_PYTHON, info);
					}
				}
			}

			property = new Property(position, name, propType, namespace, className);
			property.setDataClayID(id);
			property.setGetterImplementationID(getterImplementationID);
			property.setGetterOperationID(getterOperationID);
			property.setMetaClassID(metaClassID);
			property.setNamespaceID(namespaceID);
			property.setSetterImplementationID(setterImplementationID);
			property.setSetterOperationID(setterOperationID);
			property.setUpdateImplementationID(updateImplementationID);
			property.setUpdateOperationID(updateOperationID);
			property.setType(propType);
			property.setLanguageDepInfos(extensions);
			property.setBeforeUpdate(rs.getString("beforeUpdate"));
			property.setAfterUpdate(rs.getString("afterUpdate"));
			property.setInMaster(rs.getBoolean("inMaster"));

			final Array annotationsUUIDArray = rs.getArray("annotations");
			if (annotationsUUIDArray != null) {
				final UUID[] uuidArray = (UUID[]) annotationsUUIDArray.getArray();
				for (int i = 0; i < uuidArray.length; i++) {
					property.addAnnotation(this.getAnnotationByID(uuidArray[i]));
				}
			}
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeProperty", e);
		}
		return property;
	}

	/**
	 * Deserialize Annotation
	 * @param rs
	 *            Result set
	 * @return Annotation
	 */
	private Annotation deserializeAnnotation(final ResultSet rs) {
		Annotation annotation = null;
		try {
			annotation = new Annotation(rs.getString("descr"));
			annotation.setId((UUID)rs.getObject("id"));

			final Array keysArray = rs.getArray(3);
			final Array valuesArray = rs.getArray(4);
			if(keysArray != null && valuesArray != null) {
				final String[] keys = (String[]) keysArray.getArray(), values = (String[]) valuesArray.getArray();
				for (int i = 0; i < keys.length; i++) {
					annotation.addParameter(keys[i], values[i]);
				}
			}
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeAnnotation", e);
		}
		return annotation;
	}

	/**
	 * Deserialize JavaPropertyInfo
	 * @param rs
	 *            Result set
	 * @return JavaPropertyInfo
	 */
	private JavaPropertyInfo deserializeJavaProperty(final ResultSet rs) {
		JavaPropertyInfo info = null;
		try {
			final int modifier = rs.getInt("modifier");
			info = new JavaPropertyInfo(modifier);
			final UUID id = (UUID)rs.getObject("id");
			info.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeJavaProperty", e);
		}
		return info;
	}

	/**
	 * Deserialize PythonPropertyInfo
	 * @param rs
	 *            Result set
	 * @return PythonPropertyInfo
	 */
	private PythonPropertyInfo deserializePythonProperty(final ResultSet rs) {
		final PythonPropertyInfo info = new PythonPropertyInfo(); // no fields
		try {
			final UUID id = (UUID)rs.getObject("id");
			info.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializePythonProperty", e);
		}
		return info;
	}

	/**
	 * Deserialize AccessedImplementation
	 * @param rs
	 *            Result set
	 * @return AccessedImplementation
	 */
	private AccessedImplementation deserializeAccessedImplementation(final ResultSet rs) {
		AccessedImplementation accImpl = null;
		try {

			final String namespace = rs.getString("namespace");
			final String className = rs.getString("className");
			final String opSignature = rs.getString("opSignature");
			final int position = rs.getInt("position");
			final ImplementationID implID = new ImplementationID((UUID)rs.getObject("implid"));
			final UUID id = (UUID)rs.getObject("id");
			accImpl = new AccessedImplementation(namespace, className, opSignature, position);
			accImpl.setImplementationID(implID);
			accImpl.setId(id);

		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeAccessedImplementation", e);
		}
		return accImpl;
	}

	/**
	 * Deserialize AccessedProperty
	 * @param rs
	 *            Result set
	 * @return AccessedProperty
	 */
	private AccessedProperty deserializeAccessedProperty(final ResultSet rs) {
		AccessedProperty accProp = null;
		try {

			final String namespace = rs.getString("namespace");
			final String className = rs.getString("className");
			final String name = rs.getString("name");
			final PropertyID propID = new PropertyID((UUID)rs.getObject("propid"));
			final UUID id = (UUID)rs.getObject("id");

			accProp = new AccessedProperty(namespace, className, name);
			accProp.setPropertyID(propID);
			accProp.setId(id);

		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeAccessedProperty", e);
		}
		return accProp;
	}

	/**
	 * Deserialize MemoryFeature
	 * @param rs
	 *            Result set
	 * @return MemoryFeature
	 */
	private MemoryFeature deserializeMemoryFeature(final ResultSet rs) {
		MemoryFeature feature = null;
		try {

			final int capacity = rs.getInt("capacityInMB");
			feature = new MemoryFeature(capacity);
			final UUID id = (UUID)rs.getObject("id");
			feature.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeMemoryFeature", e);
		}
		return feature;
	}

	/**
	 * Deserialize CPUFeature
	 * @param rs
	 *            Result set
	 * @return CPUFeature
	 */
	private CPUFeature deserializeCPUFeature(final ResultSet rs) {
		CPUFeature feature = null;
		try {

			final int amount = rs.getInt("amount");
			feature = new CPUFeature(amount);
			final UUID id = (UUID)rs.getObject("id");
			feature.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeCPUFeature", e);
		}
		return feature;
	}

	/**
	 * Deserialize LanguageFeature
	 * @param rs
	 *            Result set
	 * @return LanguageFeature
	 */
	private LanguageFeature deserializeLanguageFeature(final ResultSet rs) {
		LanguageFeature feature = null;
		try {

			final String languageName = rs.getString("languageName");
			final String version = rs.getString("version");
			feature = new LanguageFeature(languageName, version);
			final UUID id = (UUID)rs.getObject("id");
			feature.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeLanguageFeature", e);
		}
		return feature;
	}

	/**
	 * Deserialize ArchitectureFeature
	 * @param rs
	 *            Result set
	 * @return ArchitectureFeature
	 */
	private ArchitectureFeature deserializeArchitectureFeature(final ResultSet rs) {
		ArchitectureFeature feature = null;
		try {

			final String architectureName = rs.getString("architectureName");
			feature = new ArchitectureFeature(architectureName);
			final UUID id = (UUID)rs.getObject("id");
			feature.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeArchitectureFeature", e);
		}
		return feature;
	}

	/**
	 * Deserialize PrefetchingInfo
	 * @param rs
	 *            Result set
	 * @return PrefetchingInfo
	 */
	private PrefetchingInformation deserializePrefetchingInfo(final ResultSet rs) {
		PrefetchingInformation prefetchingInfo = null;
		try {

			final boolean disableDynamicPrefetching = rs.getBoolean("disableDynamicPrefetching");
			final boolean injectPrefetchingCall = rs.getBoolean("injectPrefetchingCall");
			final String prefetchingNamespace = rs.getString("prefetchingNamespace");
			final String prefetchingClassName = rs.getString("prefetchingClassName");
			final String prefetchingMethodSignature = rs.getString("prefetchingMethodSignature");

			final UUID uuidImplID = (UUID)rs.getObject("implID");
			ImplementationID implId = null;
			if (uuidImplID != null) {
				implId = new ImplementationID((UUID)rs.getObject("implID"));
			}

			final UUID uuidPFID = (UUID)rs.getObject("classID");
			MetaClassID classId = null;
			if (uuidPFID != null) {
				classId = new MetaClassID((UUID)rs.getObject("classID"));
			}

			final Array sqlArray = rs.getArray("propertiesToPrefetch");
			final List<List<Property>> properties = new ArrayList<>();
			try {
				if (sqlArray != null) {
					final UUID[][] uuidArray = (UUID[][]) sqlArray.getArray();
					for (final UUID[] subArray : uuidArray) {
						final List<Property> subList = new ArrayList<>();
						for (final UUID propUUID : subArray) {
							final Property prop = getPropertyByID(propUUID);
							subList.add(prop);
						}
						properties.add(subList);
					}
				}
			} catch (final Exception e) {
				// Class cast exception if array is empty
			}
			prefetchingInfo = new PrefetchingInformation();
			prefetchingInfo.setDisableDynamicPrefetching(disableDynamicPrefetching);
			prefetchingInfo.setInjectPrefetchingCall(injectPrefetchingCall);
			prefetchingInfo.setPrefetchingNameSpace(prefetchingNamespace);
			prefetchingInfo.setPrefetchingClassName(prefetchingClassName);
			prefetchingInfo.setPrefetchingMethodSignature(prefetchingMethodSignature);
			prefetchingInfo.setPrefetchingImplementationID(implId);
			prefetchingInfo.setPrefetchingClassID(classId);
			prefetchingInfo.setPropertiesToPrefetch(properties);
			final UUID id = (UUID)rs.getObject("id");
			prefetchingInfo.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializePrefetchingInfo", e);
		}
		return prefetchingInfo;
	}

	/**
	 * Deserialize Implementation
	 * @param rs
	 *            Result set
	 * @return Implementation
	 */
	private Implementation deserializeImplementation(final String tableName, final ResultSet rs) {
		Implementation impl = null;
		try {
			final ImplementationID id = new ImplementationID((UUID)rs.getObject("id"));
			final String responsibleaccountname = rs.getString("responsibleaccountname");

			final String namespace = rs.getString("namespace");
			final String className = rs.getString("className");
			final String opNameAndDescriptor = rs.getString("opNameAndDescriptor");
			final int position = rs.getInt("position");

			Array sqlArray = rs.getArray("includes");
			final List<Type> includes = new ArrayList<>();
			if (sqlArray != null) {
				final UUID[] uuidArray = (UUID[]) sqlArray.getArray();
				for (final UUID includedUUID : uuidArray) {
					final Type include = getTypeByID(includedUUID);
					includes.add(include);
				}
			}

			sqlArray = rs.getArray("accessedProperties");
			final List<AccessedProperty> accessedProperties = new ArrayList<>();
			if (sqlArray != null) {
				final UUID[] uuidArray = (UUID[]) sqlArray.getArray();
				for (final UUID includedUUID : uuidArray) {
					final AccessedProperty accProp = getAccessedPropertyByID(includedUUID);
					accessedProperties.add(accProp);
				}
			}

			sqlArray = rs.getArray("accessedImplementations");
			final List<AccessedImplementation> accessedImplementations = new ArrayList<>();
			if (sqlArray != null) {
				final UUID[] uuidArray = (UUID[]) sqlArray.getArray();
				for (final UUID includedUUID : uuidArray) {
					final AccessedImplementation accImpl = getAccessedImplementationByID(includedUUID);
					accessedImplementations.add(accImpl);
				}
			}

			sqlArray = rs.getArray("featurestypes");
			final Array reqfeaturesArray = rs.getArray("reqfeatures");
			final Map<FeatureType, QuantitativeFeature> requiredQuantitativeFeatures = new HashMap<>();
			final Map<FeatureType, QualitativeFeature> requiredQualitativeFeatures = new HashMap<>();
			if (sqlArray != null) {
				final String[] strArray = (String[]) sqlArray.getArray();
				final UUID[] uuidArray = (UUID[]) reqfeaturesArray.getArray();
				for (int i = 0; i < strArray.length; ++i) {
					final FeatureType fType = FeatureType.valueOf(strArray[i]);

					Feature feature = null;
					switch (fType) {
					case ARCHITECTURE:
						feature = getArchitectureFeatureByID(uuidArray[i]);

						break;
					case CPU:
						feature = getCPUFeatureByID(uuidArray[i]);
						break;
					case LANGUAGE:
						feature = getLanguageFeatureByID(uuidArray[i]);
						break;
					case MEMORY:
						feature = getMemoryFeatureByID(uuidArray[i]);
						break;
					default:
						logger.error("wrong deserialization of feature type");
						break;
					}

					switch (feature.getType().getParent()) {
					case QUALITATIVE:
						requiredQualitativeFeatures.put(fType, (QualitativeFeature) feature);
						break;
					case QUANTITATIVE:
						requiredQuantitativeFeatures.put(fType, (QuantitativeFeature) feature);
						break;
					default:
						logger.error("wrong deserialization of feature type");
						break;

					}
				}
			}

			final OperationID operationID = new OperationID((UUID)rs.getObject("operationID"));
			final MetaClassID metaClassID = new MetaClassID((UUID)rs.getObject("metaClassID"));
			final AccountID responsibleAccountID = new AccountID((UUID)rs.getObject("responsibleAccountID"));
			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("namespaceID"));
			final UUID prefetchingInfoUUID = (UUID)rs.getObject("prefetchingInfo");
			PrefetchingInformation prefetchingInfo = null;
			if (prefetchingInfoUUID != null) {
				prefetchingInfo = getPrefetchingInfoByID(prefetchingInfoUUID);
			}

			if (tableName.equals("java_implementation")) {

				impl = new JavaImplementation(position, accessedProperties,
						accessedImplementations, includes,
						prefetchingInfo, requiredQuantitativeFeatures,
						requiredQualitativeFeatures,
						namespace, className, opNameAndDescriptor);
				impl.setDataClayID(id);
				impl.setResponsibleAccountName(responsibleaccountname);
				impl.setResponsibleAccountID(responsibleAccountID);
				impl.setOperationID(operationID);
				impl.setMetaClassID(metaClassID);
				impl.setNamespaceID(namespaceID);

			} else if (tableName.equals("python_implementation")) {
				impl = new PythonImplementation(position, accessedProperties,
						accessedImplementations, includes,
						prefetchingInfo, requiredQuantitativeFeatures,
						requiredQualitativeFeatures,
						namespace, className, opNameAndDescriptor,
						getPythonCodeByImplementationID(id.getId()));
				impl.setDataClayID(id);
				impl.setResponsibleAccountName(responsibleaccountname);
				impl.setResponsibleAccountID(responsibleAccountID);
				impl.setOperationID(operationID);
				impl.setMetaClassID(metaClassID);
				impl.setNamespaceID(namespaceID);

			} else {
				throw new RuntimeException("Implementation of language not supported");
			}

		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeImplementation", e);
		}
		return impl;
	}

	/**
	 * Deserialize operation
	 * @param rs
	 *            Result set
	 * @return operation
	 */
	private Operation deserializeOperation(final ResultSet rs) {
		Operation operation = null;
		try {

			final OperationID id = new OperationID((UUID)rs.getObject("id"));
			final String namespace = rs.getString("namespace");
			final String className = rs.getString("className");
			final String descriptor = rs.getString("descriptor");
			final String signature = rs.getString("signature");
			final String name = rs.getString("name");
			final String nameAndDescriptor = rs.getString("nameAndDescriptor");

			final Array namesArray = rs.getArray("paramsNames");
			final Array typesArray = rs.getArray("paramsTypes");
			final Map<String, Type> params = new HashMap<>();
			if (namesArray != null) {
				final String[] paramsNames = (String[]) namesArray.getArray();
				final UUID[] paramsTypesUUIDs = (UUID[]) typesArray.getArray();
				for (int i = 0; i < paramsNames.length; ++i) {
					final Type paramType = this.getTypeByID(paramsTypesUUIDs[i]);
					params.put(paramsNames[i], paramType);
				}
			}

			final Array namesOrderArray = rs.getArray("paramOrder");
			final List<String> paramsOrder = new ArrayList<>();
			if (namesOrderArray != null) {
				final String[] namesOrder = (String[]) namesOrderArray.getArray();
				for (final String paramName : namesOrder) {
					paramsOrder.add(paramName);
				}

			}

			final UUID returnTypeUUID = (UUID)rs.getObject("returnType");
			final Type returnType = getTypeByID(returnTypeUUID);

			final Array implsArray = rs.getArray("implementations");
			final SortedSet<Implementation> implementations = new TreeSet<>();
			if (implsArray != null) {
				final UUID[] implsArrayUUIDs = (UUID[]) implsArray.getArray();
				for (final UUID implUUID : implsArrayUUIDs) {
					implementations.add(this.getImplementationByID(implUUID));
				}

			}

			final boolean isAbstract = rs.getBoolean("isAbstract");
			final boolean isStaticConstructor = rs.getBoolean("isStaticConstructor");

			final MetaClassID metaClassID = new MetaClassID((UUID)rs.getObject("metaClassID"));
			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("namespaceID"));

			final Array sqlArray = rs.getArray("extendedtypes");

			final String[] typesNames;
			if (sqlArray == null) {
				typesNames = new String[0];
			} else {
				typesNames = (String[]) sqlArray.getArray();
			}

			final Array extensionsArray = rs.getArray("extensions");
			final UUID[] uuidArray;
			if (extensionsArray == null) {
				uuidArray = new UUID[0];
			} else {
				uuidArray = (UUID[]) extensionsArray.getArray();
			}

			final Map<Langs, LanguageDependantOperationInfo> extensions = new HashMap<>();
			for (int i = 0; i < typesNames.length; ++i) {
				if (typesNames[i].equals(Langs.LANG_JAVA.name())) {
					final JavaOperationInfo info = this.getJavaOperationByID(uuidArray[i]);
					extensions.put(Langs.LANG_JAVA, info);
				} else if (typesNames[i].equals(Langs.LANG_PYTHON.name())) {
					final PythonOperationInfo info = this.getPythonOperationByID(uuidArray[i]);
					extensions.put(Langs.LANG_PYTHON, info);
				}
			}

			operation = new Operation(name, descriptor, signature,
					nameAndDescriptor,
					namespace, className,
					isAbstract);
			operation.setDataClayID(id);
			operation.setImplementations(implementations);
			operation.setLanguageDepInfos(extensions);
			operation.setMetaClassID(metaClassID);
			operation.setNamespaceID(namespaceID);
			operation.setParamsOrder(paramsOrder);
			operation.setParams(params);
			operation.setReturnType(returnType);
			operation.setIsStaticConstructor(isStaticConstructor);

		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeOperation", e);
		}
		return operation;
	}

	/**
	 * Deserialize JavaOperationInfo
	 * @param rs
	 *            Result set
	 * @return JavaOperationInfo
	 */
	private JavaOperationInfo deserializeJavaOperation(final ResultSet rs) {
		JavaOperationInfo info = null;
		try {
			final int modifier = rs.getInt("modifier");
			info = new JavaOperationInfo(modifier);
			final UUID id = (UUID)rs.getObject("id");
			info.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeJavaOperation", e);
		}
		return info;
	}

	/**
	 * Deserialize PythonOperationInfo
	 * @param rs
	 *            Result set
	 * @return PythonOperationInfo
	 */
	private PythonOperationInfo deserializePythonOperation(final ResultSet rs) {
		final PythonOperationInfo info = new PythonOperationInfo(); // no fields
		try {
			final UUID id = (UUID)rs.getObject("id");
			info.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializePythonOperation", e);
		}
		return info;
	}

	/**
	 * Deserialize metaclass
	 * @param rs
	 *            Result set
	 * @return MetaClass
	 */
	private MetaClass deserializeMetaClass(
			final ResultSet rs) {
		MetaClass metaClass = null;
		try {
			// CHECKSTYLE:OFF
			final MetaClassID classID = new MetaClassID((UUID)rs.getObject("id"));
			final String namespace = rs.getString("namespace");
			final String name = rs.getString("name");
			final UUID parentTypeID = (UUID)rs.getObject("parentType");
			UserType parentType = null;
			if (parentTypeID != null) {
				parentType = (UserType) getTypeByID(parentTypeID);
			}

			Array sqlArray = rs.getArray("properties");
			final SortedSet<Property> properties = new TreeSet<>();
			if (sqlArray != null) {
				final UUID[] uuidArray = (UUID[]) sqlArray.getArray();
				for (final UUID propUUID : uuidArray) {
					final Property prop = getPropertyByID(propUUID);
					properties.add(prop);
				}
			}

			sqlArray = rs.getArray("operations");
			final Set<Operation> operations = new HashSet<>();
			if (sqlArray != null) {
				final UUID[] uuidArray = (UUID[]) sqlArray.getArray();
				for (final UUID propUUID : uuidArray) {
					final Operation op = getOperationByID(propUUID);
					operations.add(op);
				}
			}

			final boolean isAbstract = rs.getBoolean("isAbstract");
			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("namespaceID"));

			sqlArray = rs.getArray("extendedtypes");
			final Array extensionsArray = rs.getArray("extensions");
			final String[] typesNames = (String[]) sqlArray.getArray();
			final UUID[] uuidArray = (UUID[]) extensionsArray.getArray();
			final Map<Langs, LanguageDependantClassInfo> extensions = new HashMap<>();
			for (int i = 0; i < typesNames.length; ++i) {
				if (typesNames[i].equals(Langs.LANG_JAVA.name())) {
					final JavaClassInfo info = this.getJavaClassByID(uuidArray[i]);
					extensions.put(Langs.LANG_JAVA, info);
				} else if (typesNames[i].equals(Langs.LANG_PYTHON.name())) {
					final PythonClassInfo info = this.getPythonClassByID(uuidArray[i]);
					extensions.put(Langs.LANG_PYTHON, info);
				}
			}

			metaClass = new MetaClass(namespace, name,
					parentType, isAbstract);

			metaClass.setDataClayID(classID);
			metaClass.setLanguageDepInfos(extensions);
			metaClass.setNamespaceID(namespaceID);
			metaClass.setOperations(operations);
			metaClass.setProperties(properties);

		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializeMetaClass", e);
		}
		return metaClass;
	}

	/**
	 * Deserialize JavaClassInfo
	 * @param rs
	 *            Result set
	 * @return JavaClassInfo
	 */
	private JavaClassInfo deserializeJavaClass(final ResultSet rs) {
		JavaClassInfo info = null;
		try {
			final String signature = rs.getString("signature");
			final Array sqlArray = rs.getArray("javaParentInterfaces");
			String[] javaParentInterfaces = null;
			if (sqlArray != null) {
				javaParentInterfaces = (String[]) sqlArray.getArray();
			}
			final byte[] bytecode = rs.getBytes("classByteCode");

			info = new JavaClassInfo(signature, bytecode);
			info.setJavaParentInterfaces(javaParentInterfaces);
			final UUID id = (UUID)rs.getObject("id");
			int modifier = rs.getInt("modifier");
			info.setId(id);
			info.setModifier(modifier);
		} catch (final SQLException e) {

			logger.debug("SQL Exception in deserializeJavaClass", e);
		}
		return info;
	}

	/**
	 * Deserialize PythonClassInfo
	 * @param rs
	 *            Result set
	 * @return PythonClassInfo
	 */
	private PythonClassInfo deserializePythonClass(final ResultSet rs) {
		PythonClassInfo info = null;
		try {
			final Array sqlArray = rs.getArray("importx");
			final List<String> importx = new ArrayList<>();
			if (sqlArray != null) {
				for (final String str : (String[]) sqlArray.getArray()) {
					importx.add(str);
				}
			}

			info = new PythonClassInfo(importx);
			final UUID id = (UUID)rs.getObject("id");
			info.setId(id);
		} catch (final SQLException e) {
			logger.debug("SQL Exception in deserializePythonClass", e);
		}
		return info;
	}

	/**
	 * Get Type by ID
	 * @param typeID
	 *            ID of the type
	 * @return The type
	 * @throws SQLException
	 *             if type not found
	 */
	public Type getTypeByID(final UUID typeID) throws SQLException {
		synchronized (dataSource) {
			ResultSet rs = null;
			Type type = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_TYPE_BY_ID.getSqlStatement());

				selectStatement.setObject(1, typeID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement + "(" + typeID + ")");
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					type = deserializeType(rs);
				}
				selectStatement.close();
			} catch (final SQLException e) {
				throw e;
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					logger.debug("SQL Exception in getTypeByID", e);
				}
			}

			return type;
		}
	}

	/**
	 * Get JavaTypeInfo by ID
	 * @param uuid
	 *            ID
	 * @return The JavaTypeInfo
	 * @throws SQLException
	 *             if type not found
	 */
	private JavaTypeInfo getJavaTypeByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		JavaTypeInfo type = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_JAVA_TYPE_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				type = deserializeJavaType(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return type;
	}

	/**
	 * Get PythonTypeInfo by ID
	 * @param uuid
	 *            ID
	 * @return The PythonTypeInfo
	 * @throws SQLException
	 *             if type not found
	 */
	private PythonTypeInfo getPythonTypeByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		PythonTypeInfo type = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PYTHON_TYPE_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				type = deserializePythonType(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return type;
	}

	/**
	 * Get Property by ID
	 * @param propertyID
	 *            ID of the Property
	 * @return The Property
	 * @throws SQLException
	 *             if type not found
	 */
	private Property getPropertyByID(final UUID propertyID) throws SQLException {
		ResultSet rs = null;
		Property property = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PROPERTY_BY_ID.getSqlStatement());

			selectStatement.setObject(1, propertyID);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				property = deserializeProperty(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
		return property;
	}

	/**
	 * Get Annotation by ID
	 * @param annotationID
	 *            ID of the Property
	 * @return The annotation
	 * @throws SQLException
	 *             if annotation not found
	 */
	private Annotation getAnnotationByID(final UUID annotationID) throws SQLException {
		ResultSet rs = null;
		Annotation annotation = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_ANNOTATION_BY_ID.getSqlStatement());

			selectStatement.setObject(1, annotationID);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				annotation = deserializeAnnotation(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			logger.debug("SQL Exception in getAnnotationByID", e);
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return annotation;
	}

	/**
	 * Get JavaPropertyInfo by ID
	 * @param uuid
	 *            ID of the JavaPropertyInfo
	 * @return The JavaPropertyInfo
	 * @throws SQLException
	 *             if JavaPropertyInfo not found
	 */
	private JavaPropertyInfo getJavaPropertyByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		JavaPropertyInfo property = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_JAVA_PROPERTY_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				property = deserializeJavaProperty(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return property;
	}

	/**
	 * Get PythonPropertyInfo by ID
	 * @param uuid
	 *            ID of the PythonPropertyInfo
	 * @return The PythonPropertyInfo
	 * @throws SQLException
	 *             if PythonPropertyInfo not found
	 */
	private PythonPropertyInfo getPythonPropertyByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		PythonPropertyInfo property = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PYTHON_PROPERTY_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				property = deserializePythonProperty(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return property;
	}

	/**
	 * Get AccessedImplementation by ID
	 * @param uuid
	 *            ID of the AccessedImplementation
	 * @return The AccessedImplementation
	 * @throws SQLException
	 *             if type not found
	 */
	private AccessedImplementation getAccessedImplementationByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		AccessedImplementation result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_ACCESSED_IMPL_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeAccessedImplementation(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return result;
	}

	/**
	 * Get AccessedProperty by ID
	 * @param uuid
	 *            ID of the AccessedProperty
	 * @return The AccessedProperty
	 * @throws SQLException
	 *             if type not found
	 */
	private AccessedProperty getAccessedPropertyByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		AccessedProperty result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_ACCESSED_PROP_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeAccessedProperty(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return result;
	}

	/**
	 * Get MemoryFeature by ID
	 * @param uuid
	 *            ID of the MemoryFeature
	 * @return The MemoryFeature
	 * @throws SQLException
	 *             if type not found
	 */
	private MemoryFeature getMemoryFeatureByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		MemoryFeature result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_MEMORY_FEATURE_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeMemoryFeature(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return result;
	}

	/**
	 * Get CPUFeature by ID
	 * @param uuid
	 *            ID of the CPUFeature
	 * @return The CPUFeature
	 * @throws SQLException
	 *             if type not found
	 */
	private CPUFeature getCPUFeatureByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		CPUFeature result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_CPU_FEATURE_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeCPUFeature(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return result;
	}

	/**
	 * Get LanguageFeature by ID
	 * @param uuid
	 *            ID of the LanguageFeature
	 * @return The LanguageFeature
	 * @throws SQLException
	 *             if type not found
	 */
	private LanguageFeature getLanguageFeatureByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		LanguageFeature result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_LANGUAGE_FEATURE_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeLanguageFeature(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return result;
	}

	/**
	 * Get ArchitectureFeature by ID
	 * @param uuid
	 *            ID of the ArchitectureFeature
	 * @return The ArchitectureFeature
	 * @throws SQLException
	 *             if type not found
	 */
	private ArchitectureFeature getArchitectureFeatureByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		ArchitectureFeature result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_ARCH_FEATURE_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeArchitectureFeature(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return result;
	}

	/**
	 * Get PrefetchingInformation by ID
	 * @param uuid
	 *            ID of the PrefetchingInformation
	 * @return The PrefetchingInformation
	 * @throws SQLException
	 *             if type not found
	 */
	private PrefetchingInformation getPrefetchingInfoByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		PrefetchingInformation result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PREFETCHING_INFO_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializePrefetchingInfo(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return result;
	}

	/**
	 * Given a PythonImplementationID, return specifically its code
	 * 
	 * @param uuid
	 *            ID of the PythonImplementation
	 * @return
	 * @throws SQLException
	 */
	private String getPythonCodeByImplementationID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		String res = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PYTHON_CODE_BY_IMPLEMENTATION_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				res = rs.getString(1);
			}
			selectStatement.close();

		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
		return res;
	}

	/**
	 * Get Implementation by ID
	 * @param uuid
	 *            ID of the Implementation
	 * @return The Implementation
	 * @throws SQLException
	 *             if Operation not found
	 */
	private Implementation getImplementationByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		Implementation info = null;
		Connection conn = null;
		// TODO fix this: first tries java and then python
		try {
			conn = dataSource.getConnection();
			PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_JAVA_IMPLEMENTATION_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeImplementation("java_implementation", rs);
			}else {
				conn = dataSource.getConnection();
				PreparedStatement selectStatement2 = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PYTHON_IMPLEMENTATION_BY_ID.getSqlStatement());

				selectStatement2.setObject(1, uuid);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement2);
				}
				rs = selectStatement2.executeQuery();
				if (rs.next()) {
					info = deserializeImplementation("python_implementation", rs);
				}
				selectStatement2.close();

			}
			selectStatement.close();

		} catch (final SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return info;
	}

	/**
	 * Get Operation by ID
	 * @param uuid
	 *            ID of the Operation
	 * @return The Operation
	 * @throws SQLException
	 *             if Operation not found
	 */
	private Operation getOperationByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		Operation info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_OPERATION_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement + "(" + uuid + ")");
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeOperation(rs);
			}
			selectStatement.close();

		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return info;
	}

	/**
	 * Get JavaOperationInfo by ID
	 * @param uuid
	 *            ID of the JavaOperationInfo
	 * @return The JavaOperationInfo
	 * @throws SQLException
	 *             if JavaOperationInfo not found
	 */
	private JavaOperationInfo getJavaOperationByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		JavaOperationInfo info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_JAVA_OPERATION_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeJavaOperation(rs);
			}
			selectStatement.close();

		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return info;
	}

	/**
	 * Get PythonOperationInfo by ID
	 * @param uuid
	 *            ID of the PythonOperationInfo
	 * @return The PythonOperationInfo
	 * @throws SQLException
	 *             if PythonOperationInfo not found
	 */
	private PythonOperationInfo getPythonOperationByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		PythonOperationInfo info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PYTHON_OPERATION_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializePythonOperation(rs);
			}
			selectStatement.close();

		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return info;
	}

	/**
	 * Get MetaClass by ID
	 * @param metaClassID
	 *            ID of the object
	 * @return The MetaClass
	 * @throws SQLException
	 *             if not found
	 */
	private MetaClass getMetaClassByID(final UUID metaClassID) throws SQLException {
		ResultSet rs = null;
		MetaClass mClass = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_METACLASS_BY_ID.getSqlStatement());

			selectStatement.setObject(1, metaClassID);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				mClass = deserializeMetaClass(rs);
			}
			selectStatement.close();

		} catch (final SQLException e) {
			logger.debug("SQL Exception in getMetaClassByID", e);
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return mClass;
	}

	/**
	 * Get JavaClassInfo by ID
	 * @param uuid
	 *            ID of the JavaClassInfo
	 * @return The JavaClassInfo
	 * @throws SQLException
	 *             if JavaClassInfo not found
	 */
	private JavaClassInfo getJavaClassByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		JavaClassInfo info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_JAVA_METACLASS_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeJavaClass(rs);
			}
			selectStatement.close();

		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return info;
	}

	/**
	 * Get PythonClassInfo by ID
	 * @param uuid
	 *            ID of the PythonClassInfo
	 * @return The PythonClassInfo
	 * @throws SQLException
	 *             if PythonClassInfo not found
	 */
	private PythonClassInfo getPythonClassByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		PythonClassInfo info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PYTHON_METACLASS_BY_ID.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializePythonClass(rs);
			}
			selectStatement.close();

		} catch (final SQLException e) {
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

		return info;
	}

	/**
	 * Get MetaClass by ID
	 * @param metaClassID
	 *            ID of the object
	 * @return The MetaClass
	 */
	public MetaClass getMetaClassByID(final MetaClassID metaClassID) {
		synchronized (dataSource) {
			try {
				return getMetaClassByID(metaClassID.getId());
			} catch (final SQLException e) {
				throw new DbObjectNotExistException(metaClassID);
			}
		}
	}

	/**
	 * Get Operation by ID
	 * @param id
	 *            ID of the object
	 * @return The Operation
	 */
	public Operation getOperationByID(final OperationID id) {
		synchronized (dataSource) {
			try {
				return getOperationByID(id.getId());
			} catch (final SQLException e) {
				throw new DbObjectNotExistException(id);
			}
		}
	}

	/**
	 * Get Property by ID
	 * @param id
	 *            ID of the object
	 * @return The Property
	 */
	public Property getPropertyByID(final PropertyID id) {
		synchronized (dataSource) {
			try {
				return getPropertyByID(id.getId());
			} catch (final SQLException e) {
				throw new DbObjectNotExistException(id);
			}
		}
	}

	/**
	 * Get Implementation by ID
	 * @param id
	 *            ID of the object
	 * @return The Property
	 */
	public Implementation getImplementationByID(final ImplementationID id) {
		synchronized (dataSource) {
			try {
				return getImplementationByID(id.getId());
			} catch (final SQLException e) {
				throw new DbObjectNotExistException(id);
			}
		}
	}

	/**
	 * Delete accessedImplementation
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteAccessedImplementation(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_ACCESSED_IMPL_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteAccessedImplementation", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteAccessedProperty(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_ACCESSED_PROP_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();
		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteAccessedProperty", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteJavaTypeInfo(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_JAVA_TYPE_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteJavaTypeInfo", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deletePythonTypeInfo(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_PYTHON_TYPE_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deletePythonTypeInfo", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteType(final UUID uuid) {

		// Get type
		try {
			final Type typeToDelete = getTypeByID(uuid);
			final Map<Langs, LanguageDependantTypeInfo> extensions = typeToDelete.getLanguageDepInfos();
			for (final Entry<Langs, LanguageDependantTypeInfo> curEntry : extensions.entrySet()) {
				final Langs lang = curEntry.getKey();
				switch (lang) {
				case LANG_JAVA:
					final JavaTypeInfo javaInfo = (JavaTypeInfo) curEntry.getValue();
					this.deleteJavaTypeInfo(javaInfo.getId());
					break;
				case LANG_NONE:
					break;
				case LANG_PYTHON:
					final PythonTypeInfo info = (PythonTypeInfo) curEntry.getValue();
					this.deletePythonTypeInfo(info.getId());
					break;
				default:
					break;
				}
			}

			// Delete includes
			for (final Type included : typeToDelete.getIncludes()) {
				this.deleteType(included.getId());
			}

		} catch (final SQLException e1) {
			logger.debug("SQL Exception in deleteType", e1);
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_TYPE_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt + "(" + uuid + ")");
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteType", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteMemoryFeature(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_MEMORY_FEATURE_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteMemoryFeature", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteCPUFeature(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_CPU_FEATURE_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteCPUFeature", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteLanguageFeature(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_LANGUAGE_FEATURE_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteLanguageFeature", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteArchitectureFeature(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_ARCH_FEATURE_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteArchitectureFeature", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deletePrefetchingInfo(final UUID uuid) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_PREFETCHING_INFO_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deletePrefetchingInfo", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteJavaImplementation(final UUID uuid) {

		try {
			final JavaImplementation implementation = (JavaImplementation) this.getImplementationByID(uuid);

			// Includes
			for (final Type included : implementation.getIncludes()) {
				this.deleteType(included.getId());
			}

			// Acc. props
			for (final AccessedProperty accProp : implementation.getAccessedProperties()) {
				this.deleteAccessedProperty(accProp.getId());
			}

			// Acc. impls
			for (final AccessedImplementation accImpl : implementation.getAccessedImplementations()) {
				this.deleteAccessedImplementation(accImpl.getId());
			}

			// Features
			for (final Entry<FeatureType, QuantitativeFeature> curEntry : implementation.getRequiredQuantitativeFeatures().entrySet()) {
				final QuantitativeFeature qfeature = curEntry.getValue();
				switch (curEntry.getKey()) {
				case CPU:
					final CPUFeature cpuFeature = (CPUFeature) qfeature;
					this.deleteCPUFeature(cpuFeature.getId());
					break;
				case MEMORY:
					final MemoryFeature memFeature = (MemoryFeature) qfeature;
					this.deleteMemoryFeature(memFeature.getId());
					break;
				default:
					break;

				}
			}

			for (final Entry<FeatureType, QualitativeFeature> curEntry : implementation.getRequiredQualitativeFeatures().entrySet()) {
				final QualitativeFeature qfeature = curEntry.getValue();
				switch (curEntry.getKey()) {
				case ARCHITECTURE:
					final ArchitectureFeature archFeature = (ArchitectureFeature) qfeature;
					this.deleteArchitectureFeature(archFeature.getId());
					break;
				case LANGUAGE:
					final LanguageFeature langFeature = (LanguageFeature) qfeature;
					this.deleteLanguageFeature(langFeature.getId());
					break;
				default:
					break;

				}
			}

			// Prefetching info
			if (implementation.getPrefetchingInfo() != null) {
				this.deletePrefetchingInfo(implementation.getPrefetchingInfo().getId());
			}

		} catch (final SQLException e1) {
			logger.debug("SQL Exception in deleteJavaImplementation (first step)", e1);
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_JAVA_IMPLEMENTATION_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteJavaImplementation (second step)", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deletePythonImplementation(final UUID uuid) {

		try {
			final PythonImplementation implementation = (PythonImplementation) this.getImplementationByID(uuid);

			// Includes
			for (final Type included : implementation.getIncludes()) {
				this.deleteType(included.getId());
			}

			// Acc. props
			for (final AccessedProperty accProp : implementation.getAccessedProperties()) {
				this.deleteAccessedProperty(accProp.getId());
			}

			// Acc. impls
			for (final AccessedImplementation accImpl : implementation.getAccessedImplementations()) {
				this.deleteAccessedImplementation(accImpl.getId());
			}

			// Features
			for (final Entry<FeatureType, QuantitativeFeature> curEntry : implementation.getRequiredQuantitativeFeatures().entrySet()) {
				final QuantitativeFeature qfeature = curEntry.getValue();
				switch (curEntry.getKey()) {
				case CPU:
					final CPUFeature cpuFeature = (CPUFeature) qfeature;
					this.deleteCPUFeature(cpuFeature.getId());
					break;
				case MEMORY:
					final MemoryFeature memFeature = (MemoryFeature) qfeature;
					this.deleteMemoryFeature(memFeature.getId());
					break;
				default:
					break;

				}
			}

			for (final Entry<FeatureType, QualitativeFeature> curEntry : implementation.getRequiredQualitativeFeatures().entrySet()) {
				final QualitativeFeature qfeature = curEntry.getValue();
				switch (curEntry.getKey()) {
				case ARCHITECTURE:
					final ArchitectureFeature archFeature = (ArchitectureFeature) qfeature;
					this.deleteArchitectureFeature(archFeature.getId());
					break;
				case LANGUAGE:
					final LanguageFeature langFeature = (LanguageFeature) qfeature;
					this.deleteLanguageFeature(langFeature.getId());
					break;
				default:
					break;

				}
			}

			// Prefetching info
			if (implementation.getPrefetchingInfo() != null) {
				this.deletePrefetchingInfo(implementation.getPrefetchingInfo().getId());
			}

		} catch (final SQLException e1) {
			logger.debug("SQL Exception in deletePythonImplementation (first step)", e1);
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_JAVA_IMPLEMENTATION_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deletePythonImplementation (second step)", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteJavaProperty(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_JAVA_PROPERTY_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteJavaProperty", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deletePythonProperty(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_JAVA_PROPERTY_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deletePythonProperty", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteProperty(final UUID uuid) {

		try {
			final Property prop = this.getPropertyByID(uuid);

			// Type
			this.deleteType(prop.getType().getId());

			// Extensions
			final Map<Langs, LanguageDependantPropertyInfo> extensions = prop.getLanguageDepInfos();
			for (final Entry<Langs, LanguageDependantPropertyInfo> curEntry : extensions.entrySet()) {
				final Langs lang = curEntry.getKey();
				switch (lang) {
				case LANG_JAVA:
					final JavaPropertyInfo javaInfo = (JavaPropertyInfo) curEntry.getValue();
					this.deleteJavaProperty(javaInfo.getId());
					break;
				case LANG_NONE:
					break;
				case LANG_PYTHON:
					final PythonPropertyInfo info = (PythonPropertyInfo) curEntry.getValue();
					this.deletePythonProperty(info.getId());
					break;
				default:
					break;
				}
			}

			// Annotations
			for (final Annotation annotation : prop.getAnnotations()) {
				this.deleteAnnotation(annotation.getId());
			}

		} catch (final SQLException e1) {
			logger.debug("SQL Exception in deleteProperty (first step)", e1);
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_PROPERTY_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			logger.debug("SQL Exception in deleteProperty (second step)", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of annotation to delete
	 */
	private void deleteAnnotation(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_ANNOTATION_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteJavaOperation(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_JAVA_OPERATION_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deletePythonOperation(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_PYTHON_OPERATION_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteOperation(final UUID uuid) {

		try {
			final Operation operation = this.getOperationByID(uuid);

			// Params
			for (final Type paramType : operation.getParams().values()) {
				this.deleteType(paramType.getId());
			}
			// Return
			this.deleteType(operation.getReturnType().getId());

			// Implementations
			for (final Implementation impl : operation.getImplementations()) {
				if (impl instanceof JavaImplementation) {
					this.deleteJavaImplementation(impl.getDataClayID().getId());
				} else if (impl instanceof PythonImplementation) {
					this.deletePythonImplementation(impl.getDataClayID().getId());
				}
			}

			// Extensions
			final Map<Langs, LanguageDependantOperationInfo> extensions = operation.getLanguageDepInfos();
			for (final Entry<Langs, LanguageDependantOperationInfo> curEntry : extensions.entrySet()) {
				final Langs lang = curEntry.getKey();
				switch (lang) {
				case LANG_JAVA:
					final JavaOperationInfo javaInfo = (JavaOperationInfo) curEntry.getValue();
					this.deleteJavaOperation(javaInfo.getId());
					break;
				case LANG_NONE:
					break;
				case LANG_PYTHON:
					final PythonOperationInfo info = (PythonOperationInfo) curEntry.getValue();
					this.deletePythonOperation(info.getId());
					break;
				default:
					break;
				}
			}

		} catch (final SQLException e1) {
			e1.printStackTrace();
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_OPERATION_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteJavaClass(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_JAVA_METACLASS_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deletePythonClass(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_PYTHON_METACLASS_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteMetaClass(final UUID uuid) {

		try {
			final MetaClass clazz = this.getMetaClassByID(uuid);

			// Parent type
			if (clazz.getParentType() != null) {
				this.deleteType(clazz.getParentType().getId());
			}

			// Properties
			for (final Property prop : clazz.getProperties()) {
				this.deleteProperty(prop.getDataClayID().getId());
			}

			// Operations
			for (final Operation op : clazz.getOperations()) {
				this.deleteOperation(op.getDataClayID().getId());
			}

			// Extensions
			final Map<Langs, LanguageDependantClassInfo> extensions = clazz.getLanguageDepInfos();
			for (final Entry<Langs, LanguageDependantClassInfo> curEntry : extensions.entrySet()) {
				final Langs lang = curEntry.getKey();
				switch (lang) {
				case LANG_JAVA:
					final JavaClassInfo javaInfo = (JavaClassInfo) curEntry.getValue();
					this.deleteJavaClass(javaInfo.getId());
					break;
				case LANG_NONE:
					break;
				case LANG_PYTHON:
					final PythonClassInfo info = (PythonClassInfo) curEntry.getValue();
					this.deletePythonClass(info.getId());
					break;
				default:
					break;
				}
			}

		} catch (final SQLException e1) {
			e1.printStackTrace();
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.DELETE_METACLASS_BY_ID.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();
			stmt.close();

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	/**
	 * Delete
	 * @param classID
	 *            ID of object to delete
	 */
	public void deleteClass(final MetaClassID classID) {
		synchronized (dataSource) {
			this.deleteMetaClass(classID.getId());
		}
	}

	/**
	 * Update java bytecode
	 * @param javaClassInfoUUID
	 *            UUID of JavaClassInfo
	 * @param newByteCode
	 *            New bytecode
	 */
	public void updateJavaClassByteCode(final UUID javaClassInfoUUID,
			final byte[] newByteCode) {
		synchronized (dataSource) {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.UPDATE_JAVA_CLASS_BYTECODE_BY_ID.getSqlStatement());
				stmt.setBytes(1, newByteCode);
				stmt.setObject(2, javaClassInfoUUID);
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}

	}

	/**
	 * Check if exists class in namespace
	 * @param namespaceID
	 *            ID of namespace
	 * @return TRUE if exists.
	 */
	public boolean existsClassInNamespace(final NamespaceID namespaceID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			boolean exists = false;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement existsStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.EXISTS_CLASS_IN_NAMESPACE.getSqlStatement());

				existsStatement.setObject(1, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + existsStatement);
				}
				rs = existsStatement.executeQuery();
				rs.next();
				exists = rs.getBoolean(1);
				existsStatement.close();
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return exists;
		}

	}

	/**
	 * Update class with new properties and operations
	 * @param classID
	 *            ID of class
	 * @param newProperty
	 *            New property
	 * @param newSetter
	 *            New setter operation
	 * @param newGetter
	 *            New getter operation
	 */
	public void updateClassPropertiesAndOperations(final MetaClassID classID,
			final Property newProperty, final Operation newSetter,
			final Operation newGetter, final Operation newUpdate) {
		synchronized (dataSource) {
			final UUID newPropertyUUID = this.storeProperty(newProperty);
			final UUID setterUUID = this.storeOperation(newSetter);
			final UUID getterUUID = this.storeOperation(newGetter);

			UUID updateUUID = null;
			if (newUpdate != null) {
				updateUUID = this.storeOperation(newUpdate);
			}
			Connection conn = null;
			try {
				// CHECKSTYLE:OFF
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.UPDATE_CLASS_ADD_PROPERTY_BY_ID.getSqlStatement());
				stmt.setObject(1, newPropertyUUID);
				stmt.setObject(2, setterUUID);
				stmt.setObject(3, getterUUID);
				if (updateUUID != null) {
					stmt.setObject(4, updateUUID);
				} else {
					stmt.setObject(4, null);
				}
				stmt.setObject(5, classID.getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}

	}

	/**
	 * Update class with new properties and operations
	 * @param classID
	 *            ID of class
	 * @param newProperty
	 *            New property
	 * @param newSetter
	 *            New setter operation
	 * @param newGetter
	 *            New getter operation
	 */
	public void updateClassPropertiesAndOperations(final MetaClassID classID,
			final Property newProperty, final OperationID newSetter,
			final OperationID newGetter) {
		synchronized (dataSource) {
			final UUID newPropertyUUID = this.storeProperty(newProperty);
			final UUID setterUUID = newSetter.getId();
			final UUID getterUUID = newGetter.getId();
			Connection conn = null;
			try {
				// CHECKSTYLE:OFF
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.UPDATE_CLASS_ADD_PROPERTY_BY_ID.getSqlStatement());
				stmt.setObject(1, newPropertyUUID);
				stmt.setObject(2, setterUUID);
				stmt.setObject(3, getterUUID);
				stmt.setObject(4, classID.getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Update class with new operation
	 * @param classID
	 *            ID of class
	 * @param newoperation
	 *            New operation
	 */
	public void updateClassAddOperation(final MetaClassID classID,
			final Operation newoperation) {
		synchronized (dataSource) {
			final UUID newOpUUID = this.storeOperation(newoperation);
			Connection conn = null;
			try {
				// CHECKSTYLE:OFF
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.UPDATE_CLASS_ADD_OPERATION_BY_ID.getSqlStatement());
				stmt.setObject(1, newOpUUID);
				stmt.setObject(2, classID.getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Update class removing operation
	 * @param classID
	 *            ID of class
	 * @param opID
	 *            ID of operation
	 */
	public void updateClassRemoveOperation(final MetaClassID classID,
			final OperationID opID) {
		synchronized (dataSource) {
			Connection conn = null;
			try {
				// CHECKSTYLE:OFF
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.UPDATE_CLASS_REMOVE_OPERATION_BY_ID.getSqlStatement());
				stmt.setObject(1, opID.getId());
				stmt.setObject(2, classID.getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Update operation removing implementation
	 * @param opID
	 *            ID of operation
	 * @param implID
	 *            ID of implementation
	 */
	public void updateOperationRemoveImplementation(final OperationID opID,
			final ImplementationID implID) {
		synchronized (dataSource) {
			Connection conn = null;
			try {
				// CHECKSTYLE:OFF
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.UPDATE_OPERATION_REMOVE_IMPLEMENTATION_BY_ID.getSqlStatement());
				stmt.setObject(1, implID.getId());
				stmt.setObject(2, opID.getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Update operation adding implementation
	 * @param opID
	 *            ID of operation
	 * @param impl
	 *            implementation
	 */
	public void updateOperationAddImplementation(final OperationID opID,
			final Implementation impl) {
		synchronized (dataSource) {
			UUID implUUID = null;
			if (impl instanceof JavaImplementation) {
				implUUID = this.storeJavaImplementation((JavaImplementation) impl);
			} else if (impl instanceof PythonImplementation) {
				implUUID = this.storePythonImplementation((PythonImplementation) impl);
			}

			Connection conn = null;
			try {
				// CHECKSTYLE:OFF
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.UPDATE_OPERATION_ADD_IMPLEMENTATION_BY_ID.getSqlStatement());
				stmt.setObject(1, implUUID);
				stmt.setObject(2, opID.getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}
		}
	}

	/**
	 * Delete operation recursively
	 * @param dataClayID
	 *            ID of operation
	 */
	public void deleteOperation(final OperationID dataClayID) {
		synchronized (dataSource) {
			this.deleteOperation(dataClayID.getId());
		}
	}

	/**
	 * Delete implementation recursively
	 * @param impl
	 *            implementation
	 */
	public void deleteImplementation(final Implementation impl) {
		synchronized (dataSource) {
			if (impl instanceof JavaImplementation) {
				deleteJavaImplementation(impl.getDataClayID().getId());
			} else if (impl instanceof PythonImplementation) {
				deletePythonImplementation(impl.getDataClayID().getId());
			}
		}
	}

	/**
	 * Check if exists class in some type
	 * @param classID
	 *            ID of class
	 * @return TRUE if exists.
	 */
	public boolean existsClassInSomeType(final MetaClassID classID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			boolean exists = false;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement existsStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.EXISTS_TYPE_CLASS_ID.getSqlStatement());

				existsStatement.setObject(1, classID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + existsStatement);
				}
				rs = existsStatement.executeQuery();
				rs.next();
				exists = rs.getBoolean(1);
				existsStatement.close();
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return exists;
		}
	}

	/**
	 * Check if exists accessed implementation
	 * @param implID
	 *            ID of implementation
	 * @return TRUE if exists.
	 */
	public boolean existsAccessedImplementationWithID(final ImplementationID implID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			boolean exists = false;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement existsStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.EXISTS_ACCESSED_IMPLEMENTATION_ID.getSqlStatement());

				existsStatement.setObject(1, implID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + existsStatement);
				}
				rs = existsStatement.executeQuery();
				rs.next();
				exists = rs.getBoolean(1);
				existsStatement.close();
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return exists;
		}
	}

	/**
	 * Get class by name and namespace id
	 * @param className
	 *            Name of class
	 * @param namespaceID
	 *            Namespace ID
	 * @return The class
	 */
	public MetaClass getClassByNameAndNamespaceID(final String className,
			final NamespaceID namespaceID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			MetaClass result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_CLASS_BY_NAME_AND_NAMESPACEID.getSqlStatement());

				selectStatement.setString(1, className);
				selectStatement.setObject(2, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeMetaClass(rs);
				}
				selectStatement.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return result;
		}
	}

	/**
	 * Get class by name and namespace id
	 * @param className
	 *            Name of class
	 * @param namespace
	 *            Namespace
	 * @return The class
	 */
	public MetaClass getClassByNameAndNamespace(final String className,
			final String namespace) {
		synchronized (dataSource) {
			ResultSet rs = null;
			MetaClass result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_CLASS_BY_NAME_AND_NAMESPACE.getSqlStatement());

				selectStatement.setString(1, className);
				selectStatement.setString(2, namespace);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeMetaClass(rs);
				}
				selectStatement.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return result;
		}
	}

	/**
	 * Get property by names
	 * @param propertyName
	 *            property name
	 * @param className
	 *            Name of class
	 * @param namespace
	 *            Namespace
	 * @return The property
	 */
	public Property getPropertyByNames(final String propertyName, final String className,
			final String namespace) {
		synchronized (dataSource) {
			ResultSet rs = null;
			Property result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PROPERTY_BY_NAMES.getSqlStatement());
				selectStatement.setString(1, propertyName);
				// CHECKSTYLE:OFF
				selectStatement.setString(2, className);
				selectStatement.setString(3, namespace);
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeProperty(rs);
				}
				selectStatement.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return result;
		}
	}

	/**
	 * Get operation by names
	 * @param operationSignature
	 *            signature
	 * @param className
	 *            Name of class
	 * @param namespace
	 *            Namespace
	 * @return The Operation
	 */
	public Operation getOperationByNames(final String operationSignature, final String className,
			final String namespace) {
		synchronized (dataSource) {
			ResultSet rs = null;
			Operation result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_OPERATION_BY_NAMES.getSqlStatement());
				selectStatement.setString(1, operationSignature);
				selectStatement.setString(2, className);
				// CHECKSTYLE:OFF
				selectStatement.setString(3, namespace);
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeOperation(rs);
				}
				selectStatement.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return result;
		}
	}

	/**
	 * Get properties by class id and namespace id
	 * @param classID
	 *            ID of class
	 * @param namespaceID
	 *            Namespace ID
	 * @return The properties
	 */
	public List<Property> getPropertiesByClassIDAndNamespaceID(final MetaClassID classID,
			final NamespaceID namespaceID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			final List<Property> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_PROPERTY_OF_ENRICHMENT.getSqlStatement());

				selectStatement.setObject(1, classID.getId());
				selectStatement.setObject(2, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeProperty(rs));
				}
				selectStatement.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return result;
		}
	}

	/**
	 * Get operations by class id and namespace id
	 * @param classID
	 *            ID of class
	 * @param namespaceID
	 *            Namespace ID
	 * @return The operations
	 */
	public List<Operation> getOperationsByClassIDAndNamespaceID(final MetaClassID classID,
			final NamespaceID namespaceID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			final List<Operation> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_OPERATION_OF_ENRICHMENT.getSqlStatement());

				selectStatement.setObject(1, classID.getId());
				selectStatement.setObject(2, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeOperation(rs));
				}
				selectStatement.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return result;
		}
	}

	/**
	 * Get classes in namespace
	 * @param namespaceID
	 *            Namespace ID
	 * @return The classes
	 */
	public List<MetaClass> getClassesInNamespace(
			final NamespaceID namespaceID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			final List<MetaClass> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_CLASSES_IN_NAMESPACE.getSqlStatement());

				selectStatement.setObject(1, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeMetaClass(rs));
				}
				selectStatement.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					logger.debug("SQL Exception while closing connection", ex1);
				}
			}

			return result;
		}
	}

	public MetaClassID getClassIDByNameAndNamespaceID(final String className, final NamespaceID nspaceID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			MetaClassID result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(ClassManagerSQLStatements.SqlStatements.SELECT_CLASSID_BY_NAME_AND_NAMESPACEID.getSqlStatement());

				selectStatement.setString(1, className);
				selectStatement.setObject(2, nspaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = new MetaClassID((UUID) rs.getObject("id"));
				}
				selectStatement.close();

			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}

			return result;
		}
	}
	
	/**
	 * Close DB.
	 */
	public void close() {
		try {
			dataSource.close();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
