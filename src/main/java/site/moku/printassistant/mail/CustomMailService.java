package site.moku.printassistant.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("mail")
public class CustomMailService extends AbstractMailService {

    private final Logger logger = LoggerFactory.getLogger(CustomMailService.class);

    @Autowired
    private MailProperties mailProperties;

    @Override
    protected String obtainTemplateName() {
        return "test.ftl";
    }

    @Override
    protected Map getData() {
        Map map = new HashMap();
        map.put("name", "hello");
        map.put("text", "Test Content");
        return map;
    }

    @Override
    protected void setupMimeMessageHelper(MimeMessageHelper helper) {
        try {
            helper.setFrom(mailProperties.getFrom());
            helper.setTo(InternetAddress.parse("101147784@qq.com"));//发送给谁
            helper.setSubject("Testttttt");//邮件标题
        } catch (MessagingException e) {
            logger.error("set up  Mime message helper error:"+e.getMessage());
        }
    }
}
