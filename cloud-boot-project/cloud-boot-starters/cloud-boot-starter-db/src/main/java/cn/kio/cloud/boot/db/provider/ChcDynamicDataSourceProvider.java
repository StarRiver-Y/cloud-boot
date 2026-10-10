package cn.kio.cloud.boot.db.provider;

import cn.kio.cloud.boot.db.creator.DefaultDataSourceCreator;
import cn.kio.cloud.boot.db.tool.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源提供者
 *
 * @author kio
 * @since 1.0.0
 */
@Slf4j
public class ChcDynamicDataSourceProvider implements DynamicDataSourceProvider {

    /**
     * 所有数据源
     */
    private final Map<String, DataSourceProperty> dataSourcePropertiesMap;

    @Autowired
    private DefaultDataSourceCreator defaultDataSourceCreator;

    public ChcDynamicDataSourceProvider(Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        this.dataSourcePropertiesMap = dataSourcePropertiesMap;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        return createDataSourceMap(dataSourcePropertiesMap);
    }

    private Map<String, DataSource> createDataSourceMap(
            Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size());
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            String dsName = item.getKey();
            DataSourceProperty dataSourceProperty = item.getValue();
            String poolName = dataSourceProperty.getPoolName();
            if (poolName == null || "".equals(poolName)) {
                poolName = dsName;
            }
            dataSourceProperty.setPoolName(poolName);
            dataSourceMap.put(dsName, defaultDataSourceCreator.createDataSource(dataSourceProperty));
        }
        return dataSourceMap;
    }
}
