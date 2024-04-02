package cn.skyln.component.factories.strategy.impl;

import cn.skyln.component.factories.strategy.SendCodeStrategy;
import cn.skyln.enums.BizCodeEnum;
import cn.skyln.enums.MailEnum;
import cn.skyln.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author lamella
 * @since 2022/11/27/19:51
 */
@Service
@Slf4j
public class MailComponent implements SendCodeStrategy {
    public static final String DEFAULT_SUBJECT = "注册验证码";
    public static final String DEFAULT_CONTENT = "欢迎注册，您的验证码是\"%s\"，有效期10分钟，请勿向他人透漏验证码。";
    /**
     * Spring Boot 提供了一个发送邮件的简单抽象，直接注入即可使用
     */
    @Autowired
    private JavaMailSender mailSender;
    /**
     * 配置文件中的发送邮箱
     */
    @Value("${spring.mail.from}")
    private String from;

    /**
     * 发送一封邮件
     *
     * @param to       收件人
     * @param enumName 邮件类型枚举
     * @param code     验证码
     */
    @Override
    public void sendCode(String enumName, String to, String code) {
        MailEnum mailEnum = MailEnum.getMailEnumByName(enumName);
        if (Objects.isNull(mailEnum)) {
            throw new BizException(BizCodeEnum.CODE_TYPE_ERROR);
        }
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(mailEnum.getTemplateId());
        //邮件内容
        message.setText(mailEnum.getParam(code));
        //发送邮件
        mailSender.send(message);
        log.info("邮件发成功:{}", message);
    }
}
