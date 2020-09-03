package site.moku.printassistant.kafka;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import site.moku.printassistant.dao.KafkaSaveDataDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Component
@ConditionalOnProperty(prefix = "consumer-mode", value = "enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConsumerDataToDB {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerDataToDB.class);

    @Autowired
    private KafkaSaveDataDao dao;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(groupId = "save-to-db", topicPartitions = {@TopicPartition(topic = "test", partitions = {"0"})})
    public void consumeTest(ConsumerRecord consumerRecord) {
        logger.info("*************** consumed *************get from topic {}, partition {}, value {}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.value());
        try {
            Map params = objectMapper.readValue((String) consumerRecord.value(), Map.class);
            dao.insert(params);
        } catch (SQLException e) {
            logger.error("save failed {}");
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
