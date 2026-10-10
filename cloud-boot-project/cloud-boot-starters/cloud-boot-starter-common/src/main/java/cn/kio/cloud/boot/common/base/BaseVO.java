package cn.kio.cloud.boot.common.base;

import cn.kio.cloud.boot.common.annotation.CreatedDate;
import cn.kio.cloud.boot.common.annotation.UpdatedDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础封装 VO
 *
 * @author kio
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseVO<ID> implements Serializable {

    /**
     * 主键 id
     */
    private ID id;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建人名称
     */
    private String createdByName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdDate;

    /**
     * 创建时间 时间戳
     */
    @CreatedDate
    private Long createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 修改人名称
     */
    private String updatedByName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedDate;

    /**
     * 更新时间 时间戳
     */
    @UpdatedDate
    private Long updatedTime;

    /**
     * 启用禁用 0：启用  1禁用
     */
    private Integer enabled;

    /**
     * 是否删除 0：否  1是
     */
    private Integer deleted;

    /**
     * 是否启用 0-禁用 1-启用
     */
    private Integer enabledFlag;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer deletedFlag;

    public boolean isDeleted() {
        return deleted == 1;
    }

    public boolean isEnabled() {
        return enabled == 0;
    }

    /**
     * 是否启用
     *
     * @return true 启用 false 禁用
     */
    public boolean isDeletedFlag() {
        return deletedFlag != null && deletedFlag == 1;
    }

    /**
     * 是否启用
     *
     * @return true 启用 false 禁用
     */
    public boolean isEnabledFlag() {
        return enabledFlag != null && enabledFlag == 1;
    }
}
