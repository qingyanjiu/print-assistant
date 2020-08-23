package site.moku.printassistant.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class KafkaContoller {

    @Autowired
    private ProducerService producerService;

    @RequestMapping("produceSimpleMsg")
    @ResponseBody
    public ResponseEntity produceSimpleMsg(String msg) {
        producerService.produceSimpleMessage(msg);
        return ResponseEntity.ok("success");
    }
}
