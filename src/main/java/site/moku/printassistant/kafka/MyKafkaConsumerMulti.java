package site.moku.printassistant.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaConsumerMulti extends KafkaConsumerMulti {

    private final Logger logger = LoggerFactory.getLogger(MyKafkaConsumerMulti.class);

    @Override
    protected void doConsume(ConsumerRecord record) {
        logger.info("[" + Thread.currentThread().getName() + "] consumed batch msg: -- " + record.value());
    }
}
