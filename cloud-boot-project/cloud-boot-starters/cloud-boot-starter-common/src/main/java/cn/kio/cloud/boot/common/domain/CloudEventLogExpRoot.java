package cn.kio.cloud.boot.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudEventLogExpRoot implements Serializable {

    private Object object;

    private Object[] args;
}
