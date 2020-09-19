package site.moku.printassistant.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.moku.printassistant.print.utils.NoStorageException;

@Controller
@RequestMapping("/good")
public class GoodController {

    private final Logger logger = LoggerFactory.getLogger(GoodController.class);

    @Autowired
    private GoodService goodService;

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
}
