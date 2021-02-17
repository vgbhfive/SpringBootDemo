package cn.vgbhfive.vmail.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * @time:
 * @author: Vgbh
 */
@Service
public interface MailService {

    //简单邮件
    void sendSimpleMail(String to, String subject, String context);

    //Html邮件
    void sendHtmlMail(String to, String subject, String context) throws MessagingException;

    //附件邮件
    void sendAttachmentsMail(String to, String subject, String context, String filePath) throws MessagingException;

    //静态资源邮件
    void sendInlineResourceMail(String to, String subject, String context, String rscPath, String rscId) throws MessagingException;

}
