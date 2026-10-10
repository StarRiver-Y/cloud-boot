package cn.kio.cloud.boot.aspect;

import cn.kio.cloud.boot.common.annotation.CloudEventLog;
import cn.kio.cloud.boot.expression.ExpressionEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

/**
 * @author kio
 */
@Component
@Aspect
@Slf4j
public class CloudEventLogAdvice {

    private static final ExpressionEvaluator<String> EVALUATOR = new ExpressionEvaluator<>();

    private static final String CLOUD_COMMON_EVENT_LOG_HEADER = "Cloud-Event-Log";

    private static final String EVENT_DELIMITER = "\u0001";

    @Value("${cloud.brand:}")
    private String brand;

    @Around("@annotation(cloudEventLog)")
    public Object eventLogAspect(ProceedingJoinPoint point, CloudEventLog cloudEventLog) throws Throwable {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            HttpServletResponse response;
            if (requestAttributes != null) {
                response = ((ServletRequestAttributes) requestAttributes).getResponse();
                if (response != null) {
                    StringBuilder eventInfo = new StringBuilder();
                    addEventInfo(eventInfo, this.evalEventLogParam(point, cloudEventLog.appKey()));
                    addEventInfo(eventInfo, this.evalEventLogParam(point, cloudEventLog.cid()));
                    addEventInfo(eventInfo, this.evalEventLogParam(point, cloudEventLog.type()));
                    addEventInfo(eventInfo, brand);
                    addEventInfo(eventInfo, this.evalEventLogParam(point, cloudEventLog.userLocation()));
                    String event = Base64.getEncoder().encodeToString(eventInfo.toString().getBytes());
                    response.setHeader(CLOUD_COMMON_EVENT_LOG_HEADER, event);
                }
            }
        } catch (Exception e) {
            log.error("eventLogAspect error", e);
        }
        return point.proceed();
    }

    /**
     * 拼接数据
     */
    private void addEventInfo(StringBuilder eventInfo, String param) {
        if (StringUtils.isNotBlank(param)) {
            eventInfo.append(param);
        }
        eventInfo.append(EVENT_DELIMITER);
    }

    /**
     * 解析EL表达式
     *
     * @param point 切入点
     * @param param 需要解析的EL表达式
     * @return 解析出的值
     */
    private String evalEventLogParam(ProceedingJoinPoint point, String param) {
        try {
            if (StringUtils.isBlank(param)) {
                return "";
            }
            MethodSignature ms = (MethodSignature) point.getSignature();
            Method method = ms.getMethod();
            Object[] args = point.getArgs();
            Object target = point.getTarget();
            Class<?> targetClass = target.getClass();
            EvaluationContext context = EVALUATOR.createEvaluationContext(target, target.getClass(), method, args);
            AnnotatedElementKey elementKey = new AnnotatedElementKey(method, targetClass);
            return EVALUATOR.condition(param, elementKey, context, String.class);
        } catch (Exception e) {
            log.warn("evalEventLogParam error param{}", param);
        }
        return param;
    }

}
