package site.moku.printassistant.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(groupId = "consumer",topicPartitions = {@TopicPartition(topic = "test",partitions = {"0"})})
    public void consumeTest(ConsumerRecord consumerRecord) {
         logger.info("*************** consumed *************get from topic {}, partition {}, value {}",consumerRecord.topic(),consumerRecord.partition(),consumerRecord.value());
    }
}
