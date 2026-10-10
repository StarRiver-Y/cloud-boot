package cn.kio.cloud.boot.common.support;

/**
 * 全局异常码
 *
 * @author kio
 */

public enum GlobalErrorCodeEnum {

    /**
     * 成功
     */
    SUCCESS("SUCCESS", "响应成功"),

    /**
     * 参数校验异常
     */
    METHOD_ARG_NOT_VALID("C00000001", "参数校验异常"),

    /**
     * rpc 服务调用异常
     */
    RPC_CALL_NOT_FOUND_ERROR("C00000002", "内部服务未找到"),

    /**
     * rpc 服务调用异常
     */
    RPC_CALL_ERROR("C00000003", "内部服务调用异常"),

    /**
     * 资源无权限访问
     */
    SECURITY_FORBIDDEN("C00000004", "资源权限访问异常"),

    /**
     * 认证未通过
     */
    AUTH_FAIL("C00000005", "认证未通过"),

    /**
     * 数据不存在或者已被删除
     */
    DATA_NOT_FOUND("C00000006", "数据不存在或者已被删除"),

    /**
     * 未知异常
     */
    OTHER_ERROR("C00099999", "未知异常");

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误描述信息
     */
    private final String desc;

    GlobalErrorCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
