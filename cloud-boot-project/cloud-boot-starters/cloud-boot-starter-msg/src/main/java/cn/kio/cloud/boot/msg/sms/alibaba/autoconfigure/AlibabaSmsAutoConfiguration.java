package cn.kio.cloud.boot.msg.sms.alibaba.autoconfigure;

import cn.kio.cloud.boot.msg.sms.alibaba.service.AlibabaSmsService;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * alibaba 短信
 *
 * @author kio
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({AlibabaSmsProperties.class})
@ConditionalOnProperty(prefix = AlibabaSmsProperties.PREFIX, name = "enabled", havingValue = "true")
public class AlibabaSmsAutoConfiguration {

    @Bean
    public AsyncClient asyncClient(AlibabaSmsProperties properties) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder().accessKeyId(properties.getAccessKey()).accessKeySecret(properties.getAccessSecret()).build());
        return AsyncClient.builder().region(properties.getRegionId()).credentialsProvider(provider).overrideConfiguration(ClientOverrideConfiguration.create().setEndpointOverride(properties.getEndpoint())).build();
    }

    @Bean
    public AlibabaSmsService alibabaSmsService(AsyncClient client, AlibabaSmsProperties properties) {
        return new AlibabaSmsService(client, properties);
    }
}
