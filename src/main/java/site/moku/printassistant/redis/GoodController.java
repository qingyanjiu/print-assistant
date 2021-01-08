package site.moku.printassistant.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import site.moku.printassistant.print.utils.NoStorageException;

import java.time.Duration;

@Controller
@RequestMapping("/good")
public class GoodController {

    private final Logger logger = LoggerFactory.getLogger(GoodController.class);

    @Autowired
    private GoodService goodService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping("sell")
    @ResponseBody
    public ResponseEntity sell(int id) {
        ResponseEntity<String> responseEntity = null;
        goodService.sellWithVersion(id);
        responseEntity = ResponseEntity.ok("success");
//        logger.info("sell success, good id: {}", id);
        return responseEntity;
    }

    @RequestMapping("decreaseStorage")
    @ResponseBody
    public ResponseEntity decreaseStorage(String goodName) {
        ResponseEntity<String> responseEntity = null;
        try {
            goodService.decreaseStorageWithRedis(goodName);
            responseEntity = ResponseEntity.ok("success");
        } catch (NoStorageException e) {
            responseEntity = ResponseEntity.ok("failed");
        }
        return responseEntity;
    }

    @RequestMapping(path = "pull", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux pullMessage() {
        Flux res = Flux.interval(Duration.ofSeconds(1)).map(f -> {
            String r = stringRedisTemplate.opsForList().leftPop("topic1");
            if (r != null) {
                return r;
            } else {
                return "";
            }
        });
        return res;
    }

    @RequestMapping("push")
    @ResponseBody
    public ResponseEntity pushMessage(String text) {
        stringRedisTemplate.opsForList().rightPush("topic1", text);
        return ResponseEntity.noContent().build();
    }
}
