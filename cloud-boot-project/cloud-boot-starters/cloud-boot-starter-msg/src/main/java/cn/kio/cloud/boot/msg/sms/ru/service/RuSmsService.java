package cn.kio.cloud.boot.msg.sms.ru.service;

import cn.kio.cloud.boot.exception.ServiceRuntimeException;
import cn.kio.cloud.boot.msg.sms.ru.autoconfigure.RuSmsProperties;
import cn.kio.cloud.boot.msg.sms.ru.http.client.RuSendSmsClient;
import cn.kio.cloud.boot.msg.sms.ru.http.model.form.RuSendSmsForm;
import cn.kio.cloud.boot.msg.sms.ru.http.model.vo.RuSmsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kio
 */
@Slf4j
@RequiredArgsConstructor
public class RuSmsService {

    private final RuSmsProperties ruSmsProperties;

    private final RuSendSmsClient ruSendSmsClient;

    /**
     * 发送短信
     *
     * @param telephone 手机号码
     * @param message   短信内容
     * @return
     */
    public boolean sendCaptcha(String telephone, String message) {
        try {
            RuSendSmsForm form = new RuSendSmsForm();
            form.setLogin(ruSmsProperties.getUsername());
            form.setPsw(ruSmsProperties.getPassword());
            form.setCharset(ruSmsProperties.getCharset());
            form.setPhones(telephone);
            form.setMes(message);

            RuSmsVO ruSmsVO = ruSendSmsClient.sendSms(form);
            log.info("telephone:{} ruSmsVO: {}", telephone, ruSmsVO);
            if (null == ruSmsVO) {
                log.error("未收到服务器响应");
                return false;
            }

            if (null == ruSmsVO.getId() || ruSmsVO.getId() == 0 || ruSmsVO.getCnt() == null || ruSmsVO.getCnt() <= 0) {
                log.error("ru {} sms", telephone);
                return false;
            }
            return true;

        } catch (Exception e) {
            log.error("ru  send telephone captcha error,error message: {}", e.getMessage(), e);
            throw new ServiceRuntimeException("ru send telephone captcha error");
        }
    }
}
