package cn.kio.cloud.boot.db.tool;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;

/**
 * @author kio
 */
@Slf4j
@Data
@Accessors(chain = true)
public class DataSourceProperty {

    /**
     * 连接池名称
     */
    private String poolName;

    /**
     * 连接池类型，如果不设置自动查找 HikariCp
     */
    private Class<? extends DataSource> type;

    /**
     * JDBC driver
     */
    private String driverClassName;

    /**
     * JDBC url 地址
     */
    private String url;

    /**
     * JDBC 用户名
     */
    private String username;

    /**
     * JDBC 密码
     */
    private String password;


    /**
     * HikariCp参数配置
     */
    @NestedConfigurationProperty
    private HikariConfig hikari = new HikariConfig();
}
