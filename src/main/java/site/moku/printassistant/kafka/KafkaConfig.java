package site.moku.printassistant.kafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.transaction.ChainedKafkaTransactionManager;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.enable.auto.commit}")
    private Boolean autoCommit;

    @Value("${spring.kafka.consumer.auto.commit.interval.ms}")
    private Integer autoCommitInterval;

    @Value("${spring.kafka.consumer.session.timeout.ms}")
    private Integer sessionTimeout;

    @Value("${spring.kafka.consumer.request.timeout.ms}")
    private Integer requestTimeout;

    @Value("${spring.kafka.consumer.group.id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.max.poll.records}")
    private String maxPollRecords;

    @Value("${spring.kafka.consumer.max.poll.interval}")
    private String maxPollInterval;

    @Value("${spring.kafka.consumer.properties.isolation.level}")
    private String isolationLevel;

    /**
     * 消息发送失败重试次数
     */
    @Value("${spring.kafka.producer.retries}")
    private int retries;
    /**
     * 消息批量发送容量
     */
    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;
    /**
     * 缓存容量
     */
    @Value("${spring.kafka.producer.buffer.memory}")
    private int bufferMemory;

    /**
     * 生产者相关配置
     *
     * @return
     */
    private Map<String, Object> producerConfigs() {
        logger.info("Kafka生产者配置");
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class);
        return props;
    }

    private Map<String, Object> producerWithTransactionConfigs() {
        logger.info("支持事务的Kafka生产者配置");
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    private ProducerFactory<String, String> producerFactory() {
        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs());
        return producerFactory;
    }

    @Bean
    public ProducerFactory<String, String> producerFactoryWithTransaction() {
        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory<>(producerWithTransactionConfigs());
        producerFactory.transactionCapable();
        producerFactory.setTransactionIdPrefix("tx-");
        return producerFactory;
    }

    @Bean
    public KafkaTransactionManager transactionManager() {
        KafkaTransactionManager manager = new KafkaTransactionManager(producerFactoryWithTransaction());
        return manager;
    }

    /**
     * kafkaTemplate 覆盖默认配置类中的kafkaTemplate
     */
    @Bean
    @Primary
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

    /**
     * kafkaTemplate 覆盖默认配置类中的kafkaTemplate
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplateWithTransaction() {
        return new KafkaTemplate<String, String>(producerFactoryWithTransaction());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        // 此处并发度设置的都是Consumer个数，可以设置1到partition总数,
        // 但是，所有机器实例上总的并发度之和必须小于等于partition总数
        // 如果，总的并发度小于partition总数，有一个Consumer实例会消费超过一个以上partition
        factory.setConcurrency(2);
        //消费者设置手动ack的时候，不能设置自动提交ack
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory(consumerConfigs());
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        propsMap.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_uncommitted");
        return propsMap;
    }
}
