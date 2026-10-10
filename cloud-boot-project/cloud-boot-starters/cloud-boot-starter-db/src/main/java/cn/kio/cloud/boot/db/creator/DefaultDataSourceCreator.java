package cn.kio.cloud.boot.db.creator;

import cn.kio.cloud.boot.db.tool.DataSourceProperty;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源创建器
 *
 * @author kio
 */
@Slf4j
@Setter
public class DefaultDataSourceCreator {

    private List<DataSourceCreator> creators;

    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {

        DataSourceCreator dataSourceCreator = null;
        for (DataSourceCreator creator : this.creators) {
            if (creator.support(dataSourceProperty)) {
                dataSourceCreator = creator;
                break;
            }
        }
        if (dataSourceCreator == null) {
            throw new IllegalStateException("creator must not be null,please check the DataSourceCreator");
        }
        return dataSourceCreator.createDataSource(dataSourceProperty);
    }

}
