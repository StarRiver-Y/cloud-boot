package cn.kio.cloud.boot.bus.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusMessageForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 发送事件品牌
     */
    private String brand;

    /**
     * 发送事件应用名
     */
    private String appName;

    /**
     * 事件类型
     */
    private String type;

    /**
     * 事件时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件具体信息
     */
    private Object msg;
}
