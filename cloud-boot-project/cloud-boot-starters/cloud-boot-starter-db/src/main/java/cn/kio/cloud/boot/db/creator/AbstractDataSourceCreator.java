package cn.kio.cloud.boot.db.creator;

import cn.kio.cloud.boot.db.tool.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * 抽象连接池创建器
 *
 * @author kio
 */
@Slf4j
public abstract class AbstractDataSourceCreator implements DataSourceCreator {

    /**
     * 子类去实际创建连接池
     *
     * @param dataSourceProperty 数据源信息
     * @return 实际连接池
     */
    public abstract DataSource doCreateDataSource(DataSourceProperty dataSourceProperty);

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        return doCreateDataSource(dataSourceProperty);
    }
}
