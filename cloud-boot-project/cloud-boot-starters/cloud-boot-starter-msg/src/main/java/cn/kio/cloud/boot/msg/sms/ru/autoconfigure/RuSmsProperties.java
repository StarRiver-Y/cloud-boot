package cn.kio.cloud.boot.msg.sms.ru.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: RuSmscProperties
 * @Description:
 * @author kio
 * @date: 2023/11/13 11:09
 */
@Getter
@Setter
@ConfigurationProperties(prefix = RuSmsProperties.PREFIX)
public class RuSmsProperties {

    public static final String PREFIX = "cloud.boot.sms.ru";

    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 消息编码：koi8-r、windows-1251 或 utf-8（默认）
     */
    private String charset = StandardCharsets.UTF_8.name().toLowerCase();

    /**
     * debug 标志
     */
    private Boolean debug = false;
}
