package cn.kio.cloud.boot.common.base;

import lombok.*;

import java.io.Serializable;

/**
 * 前端，默认下拉框封装
 *
 * @author kio
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseTree implements Serializable {

    /**
     * 显示值
     */
    private String title;

    /**
     * 选择值
     */
    private String value;

    /**
     * 主键
     */
    private String key;

    /**
     * 是否禁用
     */
    private Boolean disabled;

    /**
     * 下级菜单
     */
    private BaseTree children;
}
