package site.moku.printassistant.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.moku.printassistant.print.utils.NoStorageException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

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

    @RequestMapping("pull")
    public void pullMessage(HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()){
            String res = "";
            while(true) {
                res = stringRedisTemplate.opsForList().leftPop("topic1");
                os.write(res.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("push")
    public ResponseEntity pushMessage(String text) {
        stringRedisTemplate.opsForList().rightPush("topic1", text);
        return ResponseEntity.accepted().build();
    }
}
