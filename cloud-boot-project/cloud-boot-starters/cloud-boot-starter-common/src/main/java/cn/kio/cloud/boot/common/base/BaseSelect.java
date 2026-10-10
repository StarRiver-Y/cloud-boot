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
public class BaseSelect<T> implements Serializable {

    /**
     * 数据主键 id
     */
    private T id;

    /**
     * 显示值
     */
    private String label;

    /**
     * 选择值
     */
    private String value;

    /**
     * 是否禁用
     */
    private Boolean disabled;
}
