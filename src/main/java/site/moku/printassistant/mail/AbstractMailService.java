package site.moku.printassistant.mail;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer configurer;

    protected abstract String obtainTemplateName();

    protected abstract Map getData();

    /**
     * @param helper
     * helper.setFrom(mailProperties.getFrom());
     * helper.setTo(InternetAddress.parse(""));//发送给谁
     * helper.setSubject("");//邮件标题
     */
    protected abstract void setupMimeMessageHelper(MimeMessageHelper helper);

    private String generateTemplate(Map templateModel) throws IOException, TemplateException {
        String templateName = this.obtainTemplateName();
        String text = "";
        if (templateName != null) {
            Template template = configurer.getConfiguration().getTemplate(templateName);
            //赋值后的模板邮件内容
            text = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
        } else {
            throw new UnsupportedOperationException("未设置freemarker模板");
        }
        return text;
    }

    public void sendMail() {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            this.setupMimeMessageHelper(helper);

            Map data = this.getData();

            String text = generateTemplate(data);
            helper.setText(text, true);//邮件内容
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
