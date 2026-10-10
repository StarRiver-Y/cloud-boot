package cn.kio.cloud.boot.db.creator;

import cn.kio.cloud.boot.db.tool.DataSourceProperty;

import javax.sql.DataSource;

/**
 * 连接池创建器
 *
 * @author kio
 */
public interface DataSourceCreator {

    /**
     * 通过属性创建数据源
     *
     * @param dataSourceProperty 数据源属性
     * @return 被创建的数据源
     */
    DataSource createDataSource(DataSourceProperty dataSourceProperty);


    /**
     * 当前创建器是否支持根据此属性创建
     *
     * @param dataSourceProperty 数据源属性
     * @return 是否支持
     */
    boolean support(DataSourceProperty dataSourceProperty);
}
