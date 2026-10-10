package cn.kio.cloud.boot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 分页切面属性
 * @author kio
 */
@Getter
@Setter
@ConfigurationProperties(CloudPageAdviceProperties.PREFIX)
public class CloudPageAdviceProperties {

    public static final String PREFIX = "cloud.boot.page";

    /**
     * 默认排序字段
     */
    private String defaultSort = "created_date";
}
