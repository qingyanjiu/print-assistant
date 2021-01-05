package site.moku.printassistant.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.moku.printassistant.dao.KafkaSaveDataDao;

import java.sql.SQLException;
import java.util.Map;

//@Controller
@RequestMapping("/")
public class KafkaContoller {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private KafkaSaveDataDao dao;

    @RequestMapping("produceSimpleMsg")
    @ResponseBody
    public ResponseEntity produceSimpleMsg(String msg) {
        producerService.produceSimpleMessage(msg);
        return ResponseEntity.ok("success");
    }

    @RequestMapping("produceBatchMessage")
    @ResponseBody
    public ResponseEntity produceBatchMessage(String msg) {
        producerService.produceBatchMessage(msg);
        return ResponseEntity.ok("success");
    }

    @RequestMapping("kafkaSaveData")
    @ResponseBody
    public ResponseEntity kafkaSaveData(Map params) {
        try {
            dao.insert(params);
            return ResponseEntity.ok("success");
        } catch (SQLException e) {
            return ResponseEntity.ok("failed");
        }
    }
}
