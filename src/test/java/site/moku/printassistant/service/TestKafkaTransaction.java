package site.moku.printassistant.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestKafkaTransaction {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Transactional
    public void testTransaction() {
        kafkaTemplate.send("test","transaction!");
        kafkaTemplate.send("test","transaction!transaction!");
//        throw new UnsupportedOperationException();
    }


}
