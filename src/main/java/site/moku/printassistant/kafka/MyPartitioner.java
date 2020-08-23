package site.moku.printassistant.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
//        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
//        int numPartitions = partitions.size();
//        if (key == null) {
//            Random rand = new Random();
//            return rand.nextInt(numPartitions);
//        }
//        int floorMod = Math.floorMod(key.hashCode(), numPartitions);
//        return floorMod;
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}