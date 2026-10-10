package cn.kio.cloud.boot.common.domain;


import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * gsp 时间
 *
 * @author kio
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GpsTime implements Serializable {

    /**
     * 周数
     */
    @JsonProperty("Week")
    private Integer week;

    /**
     * 整秒数,之所以要定义为 double,是因为当 ds  为负秒的时候可以进行相加计算
     */
    @JsonProperty("Second")
    private Double second;

    /**
     * 转换时间戳
     */
    private Long timestamp;

    /**
     * 获取 utc 时间戳
     *
     * @return
     */
    @JsonIgnore
    public Long getTimestamp() {
        return 315964800 + Convert.toLong(this.week * 604800 + this.second);
    }

    /**
     * 获取时间
     *
     * @return
     */
    @JsonIgnore
    public Date getDate() {
        return new Date(getTimestamp() * 1000);
    }
}
