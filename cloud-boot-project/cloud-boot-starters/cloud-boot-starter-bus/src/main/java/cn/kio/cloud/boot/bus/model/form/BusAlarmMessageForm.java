package cn.kio.cloud.boot.bus.model.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 监控告警 form 表单
 *
 * @author kio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusAlarmMessageForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 告警方式
     */
    private String type;

    /**
     * 企业微信机器人告警机器人 key
     */
    private String wechatRobotKey;

    /**
     * 告警级别  1  2  3
     */
    private Integer level;

    /**
     * 告警详细信息
     */
    private String msg;
}
