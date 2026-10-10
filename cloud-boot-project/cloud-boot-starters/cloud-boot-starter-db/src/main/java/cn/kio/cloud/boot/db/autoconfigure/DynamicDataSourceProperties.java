package cn.kio.cloud.boot.db.autoconfigure;

import cn.kio.cloud.boot.db.tool.DataSourceProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kio
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = DynamicDataSourceProperties.PREFIX)
public class DynamicDataSourceProperties {

    public static final String PREFIX = "cloud.db";

    private String primary = "master";

    /**
     * 每一个数据源
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();
}
