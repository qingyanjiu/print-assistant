package site.moku.printassistant.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private CustomMailService mailService;

    @RequestMapping("/send")
    @ResponseBody
    public ResponseEntity send() {
        mailService.sendMail();
        return ResponseEntity.ok("mail sent");
    }
}
