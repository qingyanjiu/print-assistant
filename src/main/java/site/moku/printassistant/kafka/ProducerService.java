package site.moku.printassistant.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;
import site.moku.printassistant.dao.KafkaSaveDataDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//@Service
public class ProducerService {

    private final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    @Qualifier("kafkaTemplateWithTransaction")
    private KafkaTemplate kafkaTemplateWithTransaction;

    @Autowired
    private KafkaSaveDataDao dao;

    @Autowired
    private ObjectMapper objectMapper;

    public void produceSimpleMessage(String msg) {
        kafkaTemplate.send("test", msg).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                logger.info("send message to kafka success, partition {}, topic {}", stringObjectSendResult.getRecordMetadata().partition(), stringObjectSendResult.getRecordMetadata().topic());
            }

            @Override
            public void onFailure(Throwable throwable) {
                logger.error("send message to kafka failed, result:{}", throwable.getCause());
            }
        });
    }

    public void produceBatchMessage(String msg) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < 1000; i++) {
                int _i = i;
                es.execute(() -> {
//                    kafkaTemplate.send("test", msg + _i).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//                        @Override
//                        public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
//                            logger.info("send message to kafka success, partition {}, topic {}", stringObjectSendResult.getRecordMetadata().partition(), stringObjectSendResult.getRecordMetadata().topic());
//                        }
//
//                        @Override
//                        public void onFailure(Throwable throwable) {
//                            logger.error("send message to kafka failed, result:{}", throwable.getCause());
//                        }
//                    });
                    kafkaTemplate.send("test", msg + _i);
                });
            }
        } finally {
            es.shutdown();
            try {
                if (!es.awaitTermination(20, TimeUnit.SECONDS)) {
                    logger.warn("can't shutdown thread pool... force shut down");
                    es.shutdownNow();
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }

    }

    public void produceSaveData() {
        Map data = new HashMap();
        List<Map> list = dao.qry();
        list.forEach(item -> {
            try {
                kafkaTemplate.send("test", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional
    public void transactionProduce() {
        try {
            kafkaTemplateWithTransaction.send("test", "transaction!");
            Thread.sleep(5000);
            kafkaTemplateWithTransaction.send("test", "transaction111!");
            Thread.sleep(5000);
            kafkaTemplateWithTransaction.send("test", "transaction333!");
//        System.out.println(1/0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
