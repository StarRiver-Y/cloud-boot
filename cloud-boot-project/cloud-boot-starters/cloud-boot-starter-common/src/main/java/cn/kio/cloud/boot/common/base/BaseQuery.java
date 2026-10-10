package cn.kio.cloud.boot.common.base;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 基础封装 Query
 *
 * @author kio
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseQuery implements Serializable {

    /**
     * 页数
     */
    @Min(value = 1, message = "获取的当前页数最低为 1")
    private Integer page;

    /**
     * 每页行数
     */

    @Range(min = 1, max = 100, message = "一次最大可查询 100 条")
    private Integer size;

    /**
     * 另一种分页方式
     */
    private Integer offset;

    /**
     * 另一种分页方式
     */
    @Range(max = 100, message = "一次最大可查询 100 条")
    private Integer limit;

    /**
     * 排序字段 sort=-id,createTime` 代表`id`倒序，`createTime`正序
     */
    private String sort;

    /**
     * 通用查询参数,可以作为多个字段的模糊搜索
     */
    private String params;
}
