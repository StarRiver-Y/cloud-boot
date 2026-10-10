package cn.kio.cloud.boot.msg.mail.domain;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 邮件发送表单
 *
 * @author kio
 */
public class CloudMailForm implements Serializable {

    /**
     * 发送邮件来源
     */
    private String personal;

    /**
     * 接收人
     */
    private String[] sendTos;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String text;

    /**
     * 附件
     */
    private File attachment;

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String[] getSendTos() {
        return sendTos;
    }

    public void setSendTos(String[] sendTos) {
        this.sendTos = sendTos;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"sendTos\":").append(Arrays.toString(sendTos));
        sb.append(",\"subject\":").append(subject == null ? "" : "\"").append(subject).append(subject == null ? "" : "\"");
        sb.append(",\"text\":").append(text == null ? "" : "\"").append(text).append(text == null ? "" : "\"");
        sb.append('}');
        return sb.toString();
    }
}
