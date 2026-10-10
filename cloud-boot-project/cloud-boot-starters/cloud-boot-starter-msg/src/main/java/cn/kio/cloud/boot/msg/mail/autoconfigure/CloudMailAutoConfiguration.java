package cn.kio.cloud.boot.msg.mail.autoconfigure;

import cn.kio.cloud.boot.msg.mail.sevice.CloudMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

/**
 * alibaba 短信
 *
 * @author kio
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CloudMailProperties.class})
@ConditionalOnProperty(prefix = CloudMailProperties.PREFIX, name = "enabled", havingValue = "true")
public class CloudMailAutoConfiguration {

    @Bean
    public MailProperties mailProperties(CloudMailProperties properties) {
        MailProperties mailProperties = new MailProperties();
        BeanUtils.copyProperties(properties, mailProperties);
        mailProperties.getProperties().putAll(properties.getProperties());
        return mailProperties;
    }

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public JavaMailSenderImpl mailSender(MailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        applyProperties(properties, sender);
        return sender;
    }


    @Bean
    public CloudMailService cloudMailService(CloudMailProperties properties, JavaMailSender sender) {
        MailProperties mailProperties = new MailProperties();
        BeanUtils.copyProperties(properties, mailProperties);
        mailProperties.getProperties().putAll(properties.getProperties());
        return new CloudMailService(properties, sender);
    }

    private void applyProperties(MailProperties properties, JavaMailSenderImpl sender) {
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }
        if (!properties.getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(properties.getProperties()));
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

}
