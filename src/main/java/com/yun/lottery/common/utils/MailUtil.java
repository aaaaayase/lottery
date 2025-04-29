package com.yun.lottery.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author yun
 * @date 2025/4/28 23:24
 * @desciption:
 */
@Component
@Slf4j

public class MailUtil {

    @Value(value = "${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发邮件
     *
     * @param to:      目标邮箱地址
     * @param subject： 标题
     * @param context： 正文
     * @return
     */
    public Boolean sendSampleMail(String to, String subject, String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(context);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("向{}发送邮件失败！", to, e);
            return false;
        }
        return true;
    }
}