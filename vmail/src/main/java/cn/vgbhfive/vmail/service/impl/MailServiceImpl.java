package cn.vgbhfive.vmail.service.impl;

import cn.vgbhfive.vmail.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @time:
 * @author: Vgbh
 */
@Component
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleMail(String to, String subject, String context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(context);

        try {
            javaMailSender.send(message);
            logger.info("\n\n\n\n------简单邮件发送成功!-------\n\n\n\n");
        } catch (Exception e) {
            logger.info("\n\n\n\n------出现异常，简单邮件发送失败！-------\n\n\n\n" + e);
            throw e;
        }
    }

    @Override
    public void sendHtmlMail(String to, String subject, String context)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context, true);

            javaMailSender.send(message);
            logger.info("\n\n\n\n------HTML邮件发送成功！-------\n\n\n\n");
        } catch (Exception e) {
            logger.info("\n\n\n\n------出现异常，Html邮件发送失败！-------\n\n\n\n" + e);
            throw e;
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String context, String filePath)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));//从源文件地址获取文件
            String fileName = file.getFilename();//获取文件的名称
            helper.addAttachment(fileName, file);

            javaMailSender.send(message);
            logger.info("\n\n\n\n------附件资源邮件发送成功！-------\n\n\n\n");
        } catch (Exception e) {
            logger.info("\n\n\n\n------出现异常，附件资源邮件发送失败！-------\n\n\n\n" + e);
            throw e;
        }
    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String context, String rscPath, String rscId)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(context, true);

            FileSystemResource resource = new FileSystemResource(new File(rscPath));//从源文件地址获取文件
            helper.addInline(rscId, resource);

            javaMailSender.send(message);
            logger.info("\n\n\n\n------嵌入静态资源邮件发送成功！-------\n\n\n\n");
        } catch (Exception e) {
            logger.info("\n\n\n\n------出现异常，嵌入静态资源邮件发送失败！-------\n\n\n\n" + e);
            throw e;
        }
    }


}
