//package site.moku.printassistant.print.service;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import site.moku.printassistant.kafka.MyKafkaConsumerMulti;
//import site.moku.printassistant.kafka.ProducerService;
//
//@SpringBootTest
//public class TestKafkaTransaction {
//
//    @Autowired
//    private ProducerService producerService;
//
//    @MockBean
//    private MyKafkaConsumerMulti myKafkaConsumerMulti;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//    }
//
//    @AfterEach
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void testTransaction() {
//        producerService.transactionProduce();
//    }
//
//
//}
