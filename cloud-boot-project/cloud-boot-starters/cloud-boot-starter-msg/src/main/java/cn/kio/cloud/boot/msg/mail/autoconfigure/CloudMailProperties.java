package cn.kio.cloud.boot.msg.mail.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kio
 */
@Getter
@Setter
@ConfigurationProperties(prefix = CloudMailProperties.PREFIX)
public class CloudMailProperties {

    public static final String PREFIX = "cloud.boot.mail";

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * SMTP server host. For instance, 'smtp.example.com'.
     */
    private String host;

    /**
     * SMTP server port.
     */
    private Integer port;

    /**
     * Login user of the SMTP server.
     */
    private String username;

    /**
     * Login password of the SMTP server.
     */
    private String password;

    /**
     * Protocol used by the SMTP server.
     */
    private String protocol = "smtp";

    /**
     * Default MimeMessage encoding.
     */
    private Charset defaultEncoding = DEFAULT_CHARSET;

    /**
     * Additional JavaMail Session properties.
     */
    private Map<String, String> properties = new HashMap<>();

    /**
     * Session JNDI name. When set, takes precedence over other Session settings.
     */
    private String jndiName;
}

