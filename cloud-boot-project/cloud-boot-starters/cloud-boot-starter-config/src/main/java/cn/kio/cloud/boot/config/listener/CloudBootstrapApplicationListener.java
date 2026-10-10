package cn.kio.cloud.boot.config.listener;

import cn.kio.cloud.boot.common.util.JsonUtils;
import cn.kio.cloud.boot.config.properties.CloudBootConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kio
 */
@Slf4j
public class CloudBootstrapApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>, Ordered {

    private static final String INIT_BOOT_CONFIG = "initConfig";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

        ConfigurableEnvironment env = event.getEnvironment();

        if (env.getPropertySources().contains(INIT_BOOT_CONFIG)) {
            return;
        }

        if (ClassUtils.isPresent("com.alibaba.cloud.nacos.NacosConfigBootstrapConfiguration", null)) {
            System.out.println("------ CLoud Boot Load Nacos Properties ------ ");
            env.getPropertySources().addFirst(new CloudBootConfigPropertySource(INIT_BOOT_CONFIG, buildNacosBootConfig()));
            return;
        }


        if (ClassUtils.isPresent("org.springframework.cloud.config.client.ConfigClientAutoConfiguration", null)) {
            System.out.println("------ CLoud Boot Load Git Properties ------ ");
            env.getPropertySources().addFirst(new CloudBootConfigPropertySource(INIT_BOOT_CONFIG, buildGitBootConfig()));
            return;
        }

        if (ClassUtils.isPresent("org.springframework.cloud.kubernetes.client.config.reload.KubernetesClientConfigReloadAutoConfiguration", null)) {
            System.out.println("------ CLoud Boot Load K8S ConfigMap Properties ------ ");
            env.getPropertySources().addFirst(new CloudBootConfigPropertySource(INIT_BOOT_CONFIG, buildK8sBootConfig()));
        }
    }

    @Override
    public int getOrder() {
        return org.springframework.cloud.bootstrap.BootstrapApplicationListener.DEFAULT_ORDER;
    }

    private Map<String, Object> buildNacosBootConfig() {
        Map<String, Object> source = new HashMap<>(16);
        source.put("spring.cloud.nacos.config.server-addr", "${" + CloudBootConfigProperties.PREFIX + ".url}");
        source.put("spring.cloud.nacos.config.username", "${" + CloudBootConfigProperties.PREFIX + ".username:}");
        source.put("spring.cloud.nacos.config.password", "${" + CloudBootConfigProperties.PREFIX + ".password:}");
        source.put("spring.cloud.nacos.config.namespace", "${" + CloudBootConfigProperties.PREFIX + ".group}-${spring.profiles.active}");
        source.put("spring.cloud.nacos.config.file-extension", "properties");
        source.put("spring.cloud.nacos.config.name", "${spring.application.name}");
        source.put("spring.cloud.nacos.config.group", "${" + CloudBootConfigProperties.PREFIX + ".group}");
        source.put("spring.cloud.nacos.config.shared-configs[0].data-id", "${" + CloudBootConfigProperties.PREFIX + ".group}-boot.properties");
        source.put("spring.cloud.nacos.config.shared-configs[0].group", "${" + CloudBootConfigProperties.PREFIX + ".group}");

        return source;

    }

    private Map<String, Object> buildGitBootConfig() {
        Map<String, Object> source = new HashMap<>(8);
        source.put("spring.cloud.config.name", "${" + CloudBootConfigProperties.PREFIX + ".group}-boot,${spring.application.name}");
        source.put("spring.cloud.config.uri", "${" + CloudBootConfigProperties.PREFIX + ".url}");
        source.put("spring.cloud.config.profile", "${spring.profiles.active}");
        source.put("spring.cloud.config.username", "${" + CloudBootConfigProperties.PREFIX + ".username:}");
        source.put("spring.cloud.config.password", "${" + CloudBootConfigProperties.PREFIX + ".password:}");

        return source;
    }

    private Map<String, Object> buildK8sBootConfig() {
        Map<String, Object> source = new HashMap<>(8);

        source.put("spring.cloud.kubernetes.reload.enabled", true);
        source.put("spring.cloud.kubernetes.reload.monitoring-config-maps", true);
        source.put("spring.cloud.kubernetes.reload.strategy", "refresh");
        source.put("spring.cloud.kubernetes.reload.mode", "polling");
        source.put("spring.cloud.kubernetes.reload.period", 500);
        source.put("spring.cloud.kubernetes.config.namespace", "${cloud.boot.config.group}-${spring.profiles.active}");
        source.put("spring.cloud.kubernetes.config.sources[0].name", "${" + CloudBootConfigProperties.PREFIX + ".group}-boot-cm");
        source.put("spring.cloud.kubernetes.config.sources[0].namespace", "${cloud.boot.config.group}-${spring.profiles.active}");
        source.put("spring.cloud.kubernetes.config.sources[1].name", "${spring.application.name}-cm");
        source.put("spring.cloud.kubernetes.config.sources[1].namespace", "${cloud.boot.config.group}-${spring.profiles.active}");

        return source;
    }
}
