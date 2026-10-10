package cn.kio.cloud.boot.db.provider;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 多数据源加载
 *
 * @author kio
 */
public interface DynamicDataSourceProvider {

    /**
     * 加载所有数据源
     *
     * @return 所有数据源，key为数据源名称
     */
    Map<String, DataSource> loadDataSources();
}
