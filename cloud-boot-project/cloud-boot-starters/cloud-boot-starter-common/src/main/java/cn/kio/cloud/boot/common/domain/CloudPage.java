package cn.kio.cloud.boot.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页
 *
 * @author kio
 */
@Data
public class CloudPage<E>  implements Serializable {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer page;

    /**
     * 每页的数量
     */
    private Integer size;

    /**
     * 结果集
     */
    private List<E> records;
}
