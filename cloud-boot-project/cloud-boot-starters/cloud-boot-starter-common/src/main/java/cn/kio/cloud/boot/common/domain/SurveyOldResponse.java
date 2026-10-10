package cn.kio.cloud.boot.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 老的云服务全局响应 已经过期了,不建议使用,仅用做兼容
 *
 * @author kio
 */
@Data
@Slf4j
@Builder
@Deprecated
public class SurveyOldResponse<T> implements Serializable {

    @JsonProperty("Cmd")
    private String Cmd;

    @JsonProperty("Code")
    private String Code;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Result")
    private String Result;

    @JsonProperty("Content")
    private Byte[] Content;

    /**
     * ID
     */
    @JsonProperty("ID")
    private String ID;

    /**
     * name
     */
    @JsonProperty("Name")
    private String Name;

    /**
     * 文件名（含后缀）
     */
    @JsonProperty("FileName")
    private String FileName;

    /**
     * Type
     */
    @JsonProperty("Type")
    private String Type;

    /**
     * content size in byte
     */
    @JsonProperty("Size")
    private Long Size;

    @JsonProperty("Data")
    public T Data;

    @JsonProperty("User")
    private Object User;

    @JsonProperty("LoginDeviceID")
    private String LoginDeviceID;

    /**
     * 是否存在
     */
    @JsonProperty("Exist")
    private Boolean Exist = false;

    /**
     * 工程所属用户ID
     */
    @JsonProperty("ProjectID")
    private String ProjectID;

    /**
     * 工程所属用户ID
     */
    @JsonProperty("UserID")
    private String UserID;

    /**
     * 工程个数（某名称工程存在的个数）
     */
    @JsonProperty("Count")
    private Integer Count;

    /**
     * 创建时间
     */
    @JsonProperty("CreateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CreateTime;

    /**
     * 备注
     */
    @JsonProperty("Remarks")
    private String Remarks;

    /**
     * 分类
     */
    @JsonProperty("Category")
    private String Category;
}
