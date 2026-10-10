package cn.kio.cloud.boot.common.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * 基于 Jackson 实现的工具类  Json 序列化与反序列化严禁使用 FastJson
 *
 * @author kio
 */
public final class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper mapper = new ObjectMapper()
            //时间
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .setTimeZone(TimeZone.getTimeZone("GMT+8"))
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            //属性为NULL 不序列化
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS)
            // 对于空的对象转json的时候不抛出错误
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            // 禁用遇到未知属性抛出异常
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    /**
     * json 转对象
     *
     * @param jsonStr   json 串
     * @param classType 对象类型
     * @return 对象
     */
    public static <T> T toBean(String jsonStr, Class<T> classType) {
        if (StringUtils.isEmpty(jsonStr) || classType == null) {
            log.warn("Json string {} is empty!", classType);
            return null;
        }

        try {
            return mapper.readValue(jsonStr, classType);
        } catch (Exception e) {
            log.error("json to bean error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }

    /**
     * byte 转对象
     *
     * @param bytes     bytes 字节
     * @param classType 对象类型
     * @return 对象
     */
    public static <T> T toBean(byte[] bytes, Class<T> classType) {
        if (null == bytes || classType == null) {
            log.warn("byte {} is empty!", classType);
            return null;
        }

        try {
            return mapper.readValue(bytes, classType);
        } catch (Exception e) {
            log.error("json to bean error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }

    /**
     * json 转对象
     *
     * @param jsonStr json字符串
     * @param type    转化类型
     * @return 对象
     */
    public static <T> T toBean(String jsonStr, Type type) {
        if (StringUtils.isBlank(jsonStr) || type == null) {
            log.warn("Json string {} is empty!", type);
            return null;
        }

        try {
            return mapper.readValue(jsonStr, mapper.getTypeFactory().constructType(type));
        } catch (Exception e) {
            log.error("json to bean error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }

    /**
     * json 转化为带泛型的对象
     *
     * @param jsonStr       json字符串
     * @param typeReference 转化类型
     * @return 对象
     */
    public static <T> T toBean(String jsonStr, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(jsonStr) || typeReference == null) {
            log.warn("Json string {} is empty!", typeReference);
            return null;
        }
        try {
            return mapper.readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.error("json to bean error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }

    /**
     * 对象 转 byte[]
     *
     * @param obj 对象
     * @return 字节流
     */
    public static byte[] toByte(Object obj) {
        if (null == obj) {
            return null;
        }
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("Object to byte[] error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }

    /**
     * json 转化为 list 集合对象
     *
     * @param jsonStr   json字符串
     * @param classType 转化类型
     * @return 对象
     */
    public static <T> List<T> toList(String jsonStr, Class<T> classType) {

        if (StringUtils.isBlank(jsonStr) || classType == null) {
            log.warn("Json string {} is empty!", classType);
            return null;
        }
        try {
            return mapper.readValue(jsonStr, mapper.getTypeFactory().constructParametricType(List.class, classType));
        } catch (JsonProcessingException e) {
            log.error("json to bean error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }

    /**
     * 对象转 json (未格式化 json)
     *
     * @param obj 对象
     * @return json 串
     */
    public static String toJson(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("json to json error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }

    /**
     * 对象转 json (格式化 json)
     *
     * @param obj 对象
     * @return 格式化的 json 串
     */
    public static String toJsonFormat(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.error("json to json error, message: {}", ExceptionUtil.stacktraceToString(e), e);
        }
        return null;
    }
}
