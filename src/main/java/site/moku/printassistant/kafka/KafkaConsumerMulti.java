package site.moku.printassistant.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ConsumerStoppedEvent;
import org.springframework.kafka.support.Acknowledgment;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.*;

public abstract class KafkaConsumerMulti implements ApplicationListener<ApplicationEvent> {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerMulti.class);

    private final int THREAD_POOL_SHUTDOWN_AWAIT_SECONDS = 120;
    private final TimeUnit THREAD_POOL_SHUTDOWN_TIME_UNIT = TimeUnit.SECONDS;

    private int coreThreadNum = 1;

    private int maxThreadNum = 5;

    private volatile boolean threadPoolIsClosing  = false;

    private ExecutorService es = new ThreadPoolExecutor(
            coreThreadNum,
            maxThreadNum,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue(500),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @KafkaListener(groupId = "multi-thread", topics = {"test"}, containerFactory = "kafkaListenerContainerFactory")
    public void consumeTest(List<ConsumerRecord> consumerRecords, Acknowledgment ack) {

        int size = consumerRecords.size();

        logger.info("consumed batch msg: size -- " + consumerRecords.size());

        CountDownLatch cdl = new CountDownLatch(size);
        consumerRecords.forEach(record -> {
            consume(cdl, record);
        });
        try {
            cdl.await();
            ack.acknowledge();
            logger.info("finish commit offset");
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private void consume(CountDownLatch cdl, ConsumerRecord record) {
        try {
            es.execute(() -> {
                doConsume(record);
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            cdl.countDown();
        }
    }

    //自己实现如何处理每条消息
    protected abstract void doConsume(ConsumerRecord record);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ConsumerStoppedEvent) {
            threadPoolIsClosing = true;
            logger.warn("consumer is stopped, try to close threadPool ...");
            closeThreadPool();
        }
    }

    @PreDestroy
    public void closeBeforeDestroy() {
        if(!threadPoolIsClosing)
            closeThreadPool();
    }

    private void closeThreadPool() {
        try {
            this.es.shutdown();
            if (!es.awaitTermination(THREAD_POOL_SHUTDOWN_AWAIT_SECONDS, THREAD_POOL_SHUTDOWN_TIME_UNIT)) {
                logger.warn("consumer is stopped, force close");
                es.shutdownNow();
                if (!es.awaitTermination(THREAD_POOL_SHUTDOWN_AWAIT_SECONDS, THREAD_POOL_SHUTDOWN_TIME_UNIT)) {
                    logger.warn("{} didn't terminate!", es);
                }
            }
        } catch (InterruptedException e) {
            logger.error("error occurred when closing thread pool...  force close" + e.getMessage());
            es.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
