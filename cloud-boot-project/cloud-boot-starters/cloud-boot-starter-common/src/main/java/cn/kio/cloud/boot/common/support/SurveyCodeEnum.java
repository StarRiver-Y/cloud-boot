package cn.kio.cloud.boot.common.support;

/**
 * 云服务全局异常码
 *
 * @author kio
 */
@Deprecated
public enum SurveyCodeEnum {

    /**
     * 成功
     */
    SUCCESS(1, "响应成功"),

    /**
     * 参数校验异常
     */
    ERROR(0, "参数校验异常"),

    /**
     * 未注册
     */
    UN_REG_CODE(2, "未注册"),

    /**
     * 未绑定手机号异常
     */
    UN_BIND_PHONE(3, "未绑定手机号异常"),

    /**
     * 绑定异常
     */
    BIND_CODE(4, "绑定异常"),

    /**
     * 未登陆
     */
    UN_LOGIN(5, "未登陆");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误描述信息
     */
    private final String desc;

    SurveyCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
