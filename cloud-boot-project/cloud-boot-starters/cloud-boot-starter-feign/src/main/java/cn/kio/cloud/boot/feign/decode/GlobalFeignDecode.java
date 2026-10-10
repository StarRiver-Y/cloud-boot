package cn.kio.cloud.boot.feign.decode;

import cn.kio.cloud.boot.common.domain.CloudResponse;
import cn.kio.cloud.boot.common.util.JsonUtils;
import cn.kio.cloud.boot.exception.FeignDecodeException;
import cn.kio.cloud.boot.exception.ServiceRuntimeException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;

import static java.lang.String.format;

/**
 * 全局 feign 组件间调用响应解码
 *
 * @author kio
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalFeignDecode implements Decoder {

    @Override
    public Object decode(Response response, Type type) throws FeignException, IOException {
        Response.Body body = response.body();
        if (body == null) {
            return null;
        }
        if (String.class.equals(type)) {
            return Util.toString(body.asReader(Util.UTF_8));
        }

        String result = Util.toString(response.body().asReader(Util.UTF_8));
        if (type.getTypeName().startsWith(CloudResponse.class.getTypeName())) {
            return JsonUtils.toBean(result, type);
        }

        CloudResponse res;
        try {
            res = JsonUtils.toBean(result, CloudResponse.class);
            if (null == res || res.getCode() == null) {
                return JsonUtils.toBean(result, type);
            }

            if (res.isSucceed()) {
                return JsonUtils.toBean(JsonUtils.toJson(res.getData()), type);
            }
            throw new FeignDecodeException(res);
        } catch (Exception e) {
            if (e instanceof FeignDecodeException || e instanceof ServiceRuntimeException) {
                throw e;
            }
            throw new DecodeException(response.status(), format("%s is not a type supported by this decoder.", type), response.request());
        }

    }
}
