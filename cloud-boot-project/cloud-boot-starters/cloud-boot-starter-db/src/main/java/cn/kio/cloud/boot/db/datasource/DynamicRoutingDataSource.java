package cn.kio.cloud.boot.db.datasource;

import cn.kio.cloud.boot.db.exception.DataSourceNotFoundException;
import cn.kio.cloud.boot.db.provider.DynamicDataSourceProvider;
import cn.kio.cloud.boot.db.tool.DynamicDataSourceContextHolder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 核心动态数据源组件
 *
 * @author kio
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {

    /**
     * 所有数据库
     */
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    @Autowired
    private List<DynamicDataSourceProvider> providers;

    @Setter
    private String primary = "master";

    @Override
    protected String getPrimary() {
        return primary;
    }

    @Override
    public DataSource determineDataSource() {
        String dsKey = DynamicDataSourceContextHolder.get();
        return getDataSource(dsKey);
    }


    private DataSource determinePrimaryDataSource() {
        log.debug("datasource switch to the primary datasource");
        DataSource dataSource = dataSourceMap.get(primary);
        if (dataSource != null) {
            return dataSource;
        }
        throw new DataSourceNotFoundException("datasource can not find primary datasource");
    }

    /**
     * 获取所有的数据源
     *
     * @return 当前所有数据源
     */
    public Map<String, DataSource> getDataSources() {
        return dataSourceMap;
    }

    /**
     * 获取数据源
     *
     * @param ds 数据源名称
     * @return 数据源
     */
    public DataSource getDataSource(String ds) {
        if (StringUtils.isBlank(ds)) {
            return determinePrimaryDataSource();
        } else if (dataSourceMap.containsKey(ds)) {
            log.debug("datasource switch to the datasource named [{}]", ds);
            return dataSourceMap.get(ds);
        }
        return determinePrimaryDataSource();
    }

    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        DataSource oldDataSource = dataSourceMap.put(ds, dataSource);
        // 关闭老的数据源
        if (oldDataSource != null) {
            closeDataSource(ds, oldDataSource);
        }
        log.info("datasource - add a datasource named [{}] success", ds);
    }

    /**
     * 删除数据源
     *
     * @param ds 数据源名称
     */
    public synchronized void removeDataSource(String ds) {
        if (StringUtils.isBlank(ds)) {
            throw new RuntimeException("remove parameter could not be empty");
        }
        if (primary.equals(ds)) {
            throw new RuntimeException("could not remove primary datasource");
        }
        if (dataSourceMap.containsKey(ds)) {
            DataSource dataSource = dataSourceMap.remove(ds);
            closeDataSource(ds, dataSource);
            log.info("datasource - remove the database named [{}] success", ds);
        } else {
            log.warn("datasource - could not find a database named [{}]", ds);
        }
    }

    @Override
    public void destroy() throws Exception {
        log.info("datasource start closing ....");
        for (Map.Entry<String, DataSource> item : dataSourceMap.entrySet()) {
            closeDataSource(item.getKey(), item.getValue());
        }
        log.info("datasource all closed success,bye");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加并分组数据源
        Map<String, DataSource> dataSources = new HashMap<>(16);
        for (DynamicDataSourceProvider provider : providers) {
            dataSources.putAll(provider.loadDataSources());
        }
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
        // 检测默认数据源是否设置
        if (dataSourceMap.containsKey(primary)) {
            log.info("datasource initial loaded [{}] datasource,primary datasource named [{}]", dataSources.size(), primary);
        } else {
            log.warn("datasource initial loaded [{}] datasource,Please add your primary datasource or check your configuration", dataSources.size());
        }
    }

    /**
     * close db
     *
     * @param ds         dsName
     * @param dataSource db
     */
    private void closeDataSource(String ds, DataSource dataSource) {
        try {
            assert dataSource != null;
            Method closeMethod = ReflectionUtils.findMethod(dataSource.getClass(), "close");
            if (closeMethod != null) {
                closeMethod.invoke(dataSource);
            }
        } catch (Exception e) {
            log.warn("datasource closed datasource named [{}] failed", ds, e);
        }
    }

}
