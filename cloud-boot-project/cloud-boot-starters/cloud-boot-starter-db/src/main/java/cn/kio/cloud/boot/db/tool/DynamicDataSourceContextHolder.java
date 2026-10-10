package cn.kio.cloud.boot.db.tool;

import org.apache.commons.lang3.StringUtils;

/**
 * 基于 ThreadLocal 的切换数据源工具类
 *
 * @author kio
 */
public final class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> LOOKUP_KEY_HOLDER = new ThreadLocal<>();

    private DynamicDataSourceContextHolder() {
    }

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String get() {
        return LOOKUP_KEY_HOLDER.get();
    }

    /**
     * 设置当前线程数据源
     *
     * @param ds 数据源名称
     */
    public static String set(String ds) {
        String dataSourceStr = StringUtils.isBlank(ds) ? "" : ds;
        LOOKUP_KEY_HOLDER.set(dataSourceStr);
        return dataSourceStr;
    }

    /**
     * 清空当前线程数据源
     */
    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }
}
