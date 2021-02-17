package cn.vgbhfive.vmail.controller;

import cn.vgbhfive.vmail.Utils.ReturnResult;
import cn.vgbhfive.vmail.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @time:
 * @author: Vgbh
 */
@RestController
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Autowired
    private MailService mailService;

    @RequestMapping(value = "/vmail/simple", method = RequestMethod.POST)
    public ReturnResult sendSimpleMail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("context") String context) {
        ReturnResult result = new ReturnResult();

        if (StringUtils.isEmpty(to) || !to.contains("@")) {
            result.setRspCode("400");
            result.setRspMsg("目标邮箱不正确！");
        }
        if (StringUtils.isEmpty(context)) {
            result.setRspCode("401");
            result.setRspMsg("邮件内容不能为空！");
        }
        try {
            mailService.sendSimpleMail(to, subject, context);
            logger.info("简单邮件发送成功！");
        } catch (Exception e) {
            result.setRspCode("402");
            result.setRspMsg("邮件发送出现异常！");
            logger.info("简单邮件发送出现异常！");
        }

        return result;
    }


}
