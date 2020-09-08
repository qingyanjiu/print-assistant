package site.moku.printassistant.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import site.moku.printassistant.kafka.MyKafkaConsumerMulti;
import site.moku.printassistant.kafka.ProducerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestKafkaTransaction {

    @Autowired
    private ProducerService producerService;

    @MockBean
    private MyKafkaConsumerMulti myKafkaConsumerMulti;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTransaction() {
        producerService.transactionProduce();
    }


}
