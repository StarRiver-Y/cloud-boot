package cn.kio.cloud.boot.common.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额保留两位小数
 *
 * @author kio
 */
public class MoneySerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
        if (value != null) {
            value = value.setScale(2, RoundingMode.HALF_UP);
            gen.writeString(value.toString());
        }
    }
}
