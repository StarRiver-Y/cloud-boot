package cn.kio.cloud.boot.db.creator;

import cn.kio.cloud.boot.db.tool.DataSourceProperty;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;


/**
 * Hikari数据源创建器
 *
 * @author kio
 */
public class HikariDataSourceCreator extends AbstractDataSourceCreator implements DataSourceCreator {

    @Override
    public DataSource doCreateDataSource(DataSourceProperty dataSourceProperty) {
        HikariConfig config = dataSourceProperty.getHikari();
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        config.setPoolName(dataSourceProperty.getPoolName());
        String driverClassName = dataSourceProperty.getDriverClassName();
        if (StringUtils.isNotBlank(driverClassName)) {
            config.setDriverClassName(driverClassName);
        }
        return new HikariDataSource(config);
    }

    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        Class<? extends DataSource> type = dataSourceProperty.getType();
        return type == null || "com.zaxxer.hikari.HikariDataSource".equals(type.getName());
    }
}
