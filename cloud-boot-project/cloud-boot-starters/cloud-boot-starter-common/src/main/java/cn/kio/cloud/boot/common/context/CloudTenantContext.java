package cn.kio.cloud.boot.common.context;

/**
 * @author kio
 */
public class CloudTenantContext {

    private static final ThreadLocal<String> TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return TENANT.get();
    }

    public static void clear() {
        TENANT.remove();
    }
}
