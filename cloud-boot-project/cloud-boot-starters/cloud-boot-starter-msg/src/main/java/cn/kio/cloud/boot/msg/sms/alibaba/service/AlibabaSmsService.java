package cn.kio.cloud.boot.msg.sms.alibaba.service;

import cn.kio.cloud.boot.common.util.JsonUtils;
import cn.kio.cloud.boot.exception.ServiceRuntimeException;
import cn.kio.cloud.boot.msg.sms.alibaba.autoconfigure.AlibabaSmsProperties;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 短信发送  service
 *
 * @author kio
 */
@Slf4j
@RequiredArgsConstructor
public class AlibabaSmsService {

    private final AsyncClient client;

    private final AlibabaSmsProperties properties;

    /**
     * 发送短信
     *
     * @param telephone 手机号码
     * @param code      短信模板参数
     * @return
     */
    public boolean sendCaptcha(String telephone, String code) {
        SendSmsRequest smsRequest = SendSmsRequest.builder()
                //签名信息
                .signName(properties.getSignName())
                //电话
                .phoneNumbers(telephone)
                //验证码发模板
                .templateCode(properties.getCaptchaTemplate())
                //具体验证码
                .templateParam("{\"code\":\"" + code + "\"}").build();

        return send(smsRequest);
    }

    /**
     * 指定模板发送短信
     *
     * @param telephone       手机号码
     * @param captchaTemplate 短信模板
     * @param templateParam   短信模板内容
     * @return
     */
    public boolean sendCaptchaByTemplate(String telephone, String captchaTemplate, String templateParam) {
        SendSmsRequest smsRequest = SendSmsRequest.builder()
                //签名信息
                .signName(properties.getSignName())
                //电话
                .phoneNumbers(telephone)
                //验证码发模板
                .templateCode(captchaTemplate)
                //具体验证码
                .templateParam(templateParam).build();

        return send(smsRequest);
    }

    /**
     * 发送国际短信
     *
     * @param telephone 手机号码
     * @param code      短信模板参数
     * @return
     */
    public boolean sendInternationalCaptcha(String telephone, String code) {
        SendSmsRequest smsRequest = SendSmsRequest.builder()
                //签名信息
                .signName(properties.getSignNameInternational())
                //电话
                .phoneNumbers(telephone)
                //验证码发模板
                .templateCode(properties.getCaptchaTemplateInternational())
                //具体验证码
                .templateParam("{\"code\":\"" + code + "\"}").build();
        return send(smsRequest);
    }

    /**
     * 指定模板发送短信
     *
     * @param telephone       手机号码
     * @param captchaTemplate 短信模板
     * @param templateParam   短信模板内容
     * @return
     */
    public boolean sendInternationalCaptchaByTemplate(String telephone, String captchaTemplate, String templateParam) {
        SendSmsRequest smsRequest = SendSmsRequest.builder()
                //签名信息
                .signName(properties.getSignNameInternational())
                //电话
                .phoneNumbers(telephone)
                //验证码发模板
                .templateCode(captchaTemplate)
                //具体验证码
                .templateParam(templateParam).build();

        return sendInternationalCaptchaByTemplate(telephone, null, captchaTemplate, templateParam);
    }

    /**
     * 指定签名和模板发送短信
     *
     * @param telephone       手机号码
     * @param sign            签名
     * @param captchaTemplate 短信模板
     * @param templateParam   短信模板内容
     * @return
     */
    public boolean sendInternationalCaptchaByTemplate(String telephone, String sign, String captchaTemplate, String templateParam) {
        SendSmsRequest smsRequest = SendSmsRequest.builder()
                //签名信息
                .signName(null == sign ? properties.getSignNameInternational() : sign)
                //电话
                .phoneNumbers(telephone)
                //验证码发模板
                .templateCode(captchaTemplate)
                //具体验证码
                .templateParam(templateParam).build();

        return send(smsRequest);
    }

    /**
     * 发送白名单消息,暂时支持 us
     *
     * @param telephone
     * @param message
     * @return
     */
    public Boolean sendWhitelistMsg(String telephone, String message) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", properties.getAccessKey(), properties.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(properties.getEndpoint());
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendMessageToGlobe");
        request.putQueryParameter("To", telephone);
        request.putQueryParameter("Message", message);
        request.putQueryParameter("From", "18773124359");
        request.putQueryParameter("Type", "OTP");

        try {
            CommonResponse commonResponse = client.getCommonResponse(request);
            Map<String, Object> data = JsonUtils.toBean(commonResponse.getData(), Map.class);
            return "OK".equals(data.get("Code"));
        } catch (Exception e) {
            log.error("当前发送 us 短信发生错误", e);
            throw new ServiceRuntimeException("us send telephone captcha error");
        }
    }


    private boolean send(SendSmsRequest smsRequest) {
        CompletableFuture<SendSmsResponse> future = client.sendSms(smsRequest);

        try {
            SendSmsResponse response = future.get();

            if ("OK".equals(response.getBody().getCode())) {
                return true;
            }
            log.error("alibaba sms send error, error message: {}, error request id: {}", response.getBody().getMessage(), response.getBody().getRequestId());
            throw new ServiceRuntimeException("alibaba sms send error, error message: " + response.getBody().getMessage() + ", error request id: " + response.getBody().getRequestId());

        } catch (Exception e) {
            log.error("alibaba  send telephone captcha error,error message: {}", e.getMessage(), e);
            throw new ServiceRuntimeException("alibaba  send telephone captcha error");
        }
    }
}
