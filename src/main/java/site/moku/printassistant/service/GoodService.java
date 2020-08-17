package site.moku.printassistant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.moku.printassistant.dao.GoodDao;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoodService {

    private final Logger logger = LoggerFactory.getLogger(GoodService.class);

    @Autowired
    private GoodDao goodDao;

    public void sell(int id) {
        int res = goodDao.sell(id);
        if (res == 0) {
            logger.info("already empty, can't sell more");
            return;
        } else {
            logger.info("create order");
        }
    }

    public void sellWithVersion(int id) {
        Map params = new HashMap();
        Map res = goodDao.qryVersion(id);
        params.put("version", res.get("version"));
        params.put("id", id);
        //获取剩余库存和版本号
        int count = (int) res.get("count");
        //如果还有库存，乐观锁去抢
        if(count > 0) {
            //没抢到，version被别人改了,update影响的行数为0
            if (goodDao.sellWithVersion(params) == 0) {
                logger.info("version updated by others, sell failed");
            } else {
                //抢到了
                logger.info("sell success");
            }
            //没库存了，直接退出
        } else {
            logger.info("empty... exit");
        }
    }
}
