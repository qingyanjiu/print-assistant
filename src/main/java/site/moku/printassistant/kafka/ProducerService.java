package site.moku.printassistant.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class ProducerService {

    private final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

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

}
