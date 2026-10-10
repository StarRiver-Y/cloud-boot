package cn.kio.cloud.boot.security.auth.model.form;

import lombok.Data;

import java.util.Map;

/**
 * 请求信息
 * @author kio
 */
@Data
public class RequestInfoForm {

    /**
     * 用户id
     */
    private String userId;

    /**
     * app key
     */
    private String appKey;

    /**
     * 权限标识符
     */
    private String permissions;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求参数
     */
    private Map<String, String[]> queryParams;

    /**
     * 请求体
     */
    private String body;
}
