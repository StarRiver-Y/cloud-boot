package cn.kio.cloud.boot.db.tool;


import cn.kio.cloud.boot.db.datasource.AbstractRoutingDataSource;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kio
 */
public class ConnectionFactory {

    private static final ThreadLocal<Map<String, AbstractRoutingDataSource.ConnectionProxy>> CONNECTION_HOLDER =
            ThreadLocal.withInitial(() -> new ConcurrentHashMap<>(8));

    public static void putConnection(String ds, AbstractRoutingDataSource.ConnectionProxy connection) {
        Map<String, AbstractRoutingDataSource.ConnectionProxy> concurrentHashMap = CONNECTION_HOLDER.get();
        if (!concurrentHashMap.containsKey(ds)) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            concurrentHashMap.put(ds, connection);
        }
    }

    public static AbstractRoutingDataSource.ConnectionProxy getConnection(String ds) {
        return CONNECTION_HOLDER.get().get(ds);
    }

    public static void notify(Boolean state) throws Exception {
        Exception exception = null;
        try {
            Map<String, AbstractRoutingDataSource.ConnectionProxy> concurrentHashMap = CONNECTION_HOLDER.get();
            for (AbstractRoutingDataSource.ConnectionProxy connectionProxy : concurrentHashMap.values()) {
                try {
                    connectionProxy.notify(state);
                } catch (SQLException e) {
                    exception = e;
                }
            }
        } finally {
            CONNECTION_HOLDER.remove();
            if (exception != null) {
                throw exception;
            }
        }
    }

}
