package cn.vgbhfive.vmail;

import cn.vgbhfive.vmail.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

/**
 * @time:
 * @author: Vgbh
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSimpleMail() {
        mailService.sendSimpleMail("3408835536@qq.com", "简单测试邮件", "这是第一份简单邮件！");
    }

    @Test
    public void testHtmlMail() throws MessagingException {
        String context = "<Html> <body> " +
                "<h2> 这是一封Html测试邮件，Hello World！</h2>" +
                " </body> </Html>";

        mailService.sendHtmlMail("3408835536@qq.com", "Html测试邮件", context);
    }

    @Test
    public void testHtmlMail2() throws MessagingException {
        Context context = new Context();//邮件模板正文
        context.setVariable("id", "111");//设置模板需要替换的参数
        String emailContext = templateEngine.process("mailTemplate", context);//使用templateEngine替换动态参数，产生的最后的Html内容。

        mailService.sendHtmlMail("3408835536@qq.com", "Html测试邮件2,模板邮件：", emailContext);
    }

    @Test
    public void testAttachmentsMail() throws MessagingException {
        String filePath = "f:\\aaa.txt";
        mailService.sendAttachmentsMail("3408835536@qq.com", "附件测试邮件", "这是第一份附件测试邮件！", filePath);
    }

    @Test
    public void testInLineResourceMail() throws MessagingException {
        String rscId = "1111";
        String context = "<Html> <body> " +
                "这是一个包含静态资源的邮件：<img src=\'cid:" + rscId + "\'>" +
                " </body> </Html>";
        String rscPath = "f:\\image.jpg";

        mailService.sendInlineResourceMail("3408835536@qq.com", "静态资源测试邮件", context, rscPath, rscId);
    }


}
