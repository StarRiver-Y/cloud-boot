package cn.kio.cloud.boot.msg.mail.sevice;

import cn.kio.cloud.boot.msg.mail.autoconfigure.CloudMailProperties;
import cn.kio.cloud.boot.msg.mail.domain.CloudMailForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

/**
 * 短信发送  service
 *
 * @author kio
 */
@Slf4j
@RequiredArgsConstructor
public class CloudMailService {

    private final CloudMailProperties cloudMailProperties;

    private final JavaMailSender javaMailSender;

    /**
     * 发送邮件
     *
     * @param form
     * @return
     */
    public boolean sendMail(CloudMailForm form) {
        checkMail(form);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(StringUtils.isBlank(form.getPersonal()) ? cloudMailProperties.getUsername() : form.getPersonal() + " <" + cloudMailProperties.getUsername() + ">");
        message.setTo(form.getSendTos());
        message.setSubject(form.getSubject());
        message.setText(form.getText());
        message.setSentDate(new Date());
        javaMailSender.send(message);
        log.info("发送邮件成功: {} -> {}", cloudMailProperties.getUsername(), form.getSendTos());
        return true;
    }

    /**
     * 发送 HTML 邮件
     *
     * @param form
     * @return
     */
    public boolean sendHtmlMail(CloudMailForm form) {
        checkMail(form);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(new InternetAddress(cloudMailProperties.getUsername(), form.getPersonal()));

            message.setFrom(StringUtils.isBlank(form.getPersonal()) ? cloudMailProperties.getUsername() : form.getPersonal() + " <" + cloudMailProperties.getUsername() + ">");
            messageHelper.setTo(form.getSendTos());
            messageHelper.setSubject(form.getSubject());
            messageHelper.setText(form.getText(), true);
            File attachment = form.getAttachment();
            if (attachment != null && attachment.exists()) {
                messageHelper.addAttachment(attachment.getName(), attachment);
            }
            javaMailSender.send(message);
            log.info("发送邮件成功: {} -> {}", cloudMailProperties.getUsername(), form.getSendTos());
            return true;
        } catch (Exception e) {
            log.error("send email html error", e);
        }

        return false;

    }

    private void checkMail(CloudMailForm form) {
        Assert.notNull(form, "邮件请求不能为空");
        Assert.notNull(form.getSendTos(), "邮件收件人不能为空");
        Assert.notNull(form.getSubject(), "邮件主题不能为空");
        Assert.notNull(form.getText(), "邮件收件人不能为空");
    }
}

