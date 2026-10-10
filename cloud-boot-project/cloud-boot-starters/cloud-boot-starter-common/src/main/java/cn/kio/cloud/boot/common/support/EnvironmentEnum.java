package cn.kio.cloud.boot.common.support;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 环境枚举
 *
 * @author kio
 */

public enum EnvironmentEnum {

    /**
     * 本地环境
     */
    LOCAL,
    /**
     * 开发环境
     */
    DEV,
    /**
     * 测试环境
     */
    TEST,
    /**
     * 预发环境
     */
    PRE,
    /**
     * 生产环境
     */
    PROD;

    public static EnvironmentEnum getEnvironmentEnumByActive(String active) {
        if (StringUtils.isBlank(active)) {
            return LOCAL;
        }

        List<EnvironmentEnum> environmentEnums = EnumUtils.getEnumList(EnvironmentEnum.class);
        for (EnvironmentEnum environmentEnum : environmentEnums) {
            if (active.toUpperCase().startsWith(environmentEnum.name())) {
                return environmentEnum;
            }
        }

        return null;
    }

}
