package es.bsc.dataclay.util.structs;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import es.bsc.dataclay.dbhandler.sql.DataServiceDBSQLStatements;
import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.classmgr.ClassManagerSQLStatements;
import es.bsc.dataclay.logic.contractmgr.ContractManagerSQLStatements;
import es.bsc.dataclay.logic.datacontractmgr.DataContractManagerSQLStatements;
import es.bsc.dataclay.logic.datasetmgr.DataSetManagerSQLStatements;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManagerSQLStatements;
import es.bsc.dataclay.logic.logicmetadata.LogicMetadataSQLStatements;
import es.bsc.dataclay.logic.namespacemgr.NamespaceManagerSQLStatements;
import es.bsc.dataclay.logic.notificationmgr.NotificationMgrSQLStatements;
import es.bsc.dataclay.logic.sessionmgr.SessionManagerSQLStatements;
import es.bsc.dataclay.metadataservice.MetaDataServiceSQLStatements;
import es.bsc.dataclay.util.Configuration;

public class MemoryCache<A, B> {

    /** Cache threads. */
    private static final ScheduledExecutorService execService = Executors.newSingleThreadScheduledExecutor();
    /** List of all caches. */
    private static final List<MemoryCache> ALL_CACHES = new ArrayList<>();

    static {
        final Runnable task = () -> {
            for (MemoryCache cache : ALL_CACHES) {
                cache.cleanUp();
            }
            //DataServiceDBSQLStatements.SqlStatements.unloadStatements();
            AccountMgrSQLStatements.SqlStatements.unloadStatements();
            ClassManagerSQLStatements.SqlStatements.unloadStatements();
            ContractManagerSQLStatements.SqlStatements.unloadStatements();
            DataContractManagerSQLStatements.SqlStatements.unloadStatements();
            DataSetManagerSQLStatements.SqlStatements.unloadStatements();
            InterfaceManagerSQLStatements.SqlStatements.unloadStatements();
            LogicMetadataSQLStatements.SqlStatements.unloadStatements();
            NamespaceManagerSQLStatements.SqlStatements.unloadStatements();
            NotificationMgrSQLStatements.SqlStatements.unloadStatements();
            SessionManagerSQLStatements.SqlStatements.unloadStatements();
            //MetaDataServiceSQLStatements.SqlStatements.unloadStatements();
            System.gc();
        };
        //FIXME: race condition while reading enum + cleaning
        execService.scheduleAtFixedRate(task, Configuration.Flags.METADATA_CLEAN_PERIOD_MILLIS.getIntValue(),
                Configuration.Flags.METADATA_CLEAN_PERIOD_MILLIS.getIntValue(), TimeUnit.SECONDS);
    }

    private Cache<A, B>  cache = CacheBuilder.newBuilder().weakKeys()
                .expireAfterWrite(Configuration.Flags.METADATA_CACHE_ENTRY_EXPIRATION_MILLIS.getIntValue(), TimeUnit.SECONDS).build();

    public MemoryCache() {
        ALL_CACHES.add(this);
    }


    public B get(A key) {
        return cache.getIfPresent(key);
    }

    public void put(A key, B obj) {
        cache.put(key, obj);
    }

    public void cleanUp() {
        cache.cleanUp();
    }

    public void clear() {
        cache.asMap().clear();
    }

    public B remove(final A key) {
        return cache.asMap().remove(key);
    }

    public boolean containsKey(final A key) {
        return cache.asMap().containsKey(key);
    }

    public boolean containsValue(final B value) {
        return cache.asMap().containsValue(value);
    }

    public Set<Map.Entry<A, B>> entrySet() {
        return cache.asMap().entrySet();
    }

    public boolean isEmpty() {
        return cache.asMap().isEmpty();
    }

    public void putAll(final Map<? extends A, ? extends B> m) {
        cache.asMap().putAll(m);
    }
}
