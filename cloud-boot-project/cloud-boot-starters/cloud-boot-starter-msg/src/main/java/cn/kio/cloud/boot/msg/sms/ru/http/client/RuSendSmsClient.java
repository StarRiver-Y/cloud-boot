package cn.kio.cloud.boot.msg.sms.ru.http.client;

import cn.kio.cloud.boot.msg.sms.ru.http.model.form.RuSendSmsForm;
import cn.kio.cloud.boot.msg.sms.ru.http.model.vo.RuSmsVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 短信相关 client
 *
 * @author kio
 */
@FeignClient(value = "ru-send-sms-client", url = "${cloud.boot.sms.ru-url:https://smsc.ru}")
public interface RuSendSmsClient {

    /**
     * get 方式发送短信
     *
     * @param form ak
     */
    @GetMapping("/sys/send.php")
    RuSmsVO sendSms(@SpringQueryMap RuSendSmsForm form);
}
