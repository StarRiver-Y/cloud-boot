package cn.kio.cloud.boot.common.base;

import cn.kio.cloud.boot.common.annotation.CreatedBy;
import cn.kio.cloud.boot.common.annotation.CreatedDate;
import cn.kio.cloud.boot.common.annotation.UpdatedBy;
import cn.kio.cloud.boot.common.annotation.UpdatedDate;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础封装 entity
 *
 * @author kio
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity<ID> implements Serializable {

    /**
     * 主键 id
     */
    private ID id;

    /**
     * 创建人
     */
    @CreatedBy
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createdDate;

    /**
     * 创建时间 时间戳
     */
    @CreatedDate
    private Long createdTime;

    /**
     * 更新人
     */
    @UpdatedBy
    private String updatedBy;

    /**
     * 更新时间
     */
    @UpdatedDate
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
}
