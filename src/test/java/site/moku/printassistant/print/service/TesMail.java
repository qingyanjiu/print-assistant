package site.moku.printassistant.print.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import site.moku.printassistant.mail.CustomMailService;

@SpringBootTest
@ActiveProfiles("mail")
public class TesMail {

    @Autowired
    private CustomMailService customMailService;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testSendMail() {
        customMailService.sendMail();
    }

}
