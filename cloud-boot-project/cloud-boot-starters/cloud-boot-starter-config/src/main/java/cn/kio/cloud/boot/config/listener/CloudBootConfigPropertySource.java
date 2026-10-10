package cn.kio.cloud.boot.config.listener;

import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * 自定义 MapPropertySource
 *
 * @author kio
 */
public class CloudBootConfigPropertySource extends MapPropertySource {
    public CloudBootConfigPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
}
