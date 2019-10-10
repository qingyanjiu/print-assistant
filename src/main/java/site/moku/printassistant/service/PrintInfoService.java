/**
 * @author louisliu
 */

package site.moku.printassistant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PrintInfoService implements IPrintInfoService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveToRedis(String printKey, String content) {
        stringRedisTemplate.opsForValue().set(printKey, content);
    }

    @Override
    public String getFromRedis(String printKey) {
        return stringRedisTemplate.opsForValue().get(printKey);
    }

    @Override
    public Set<String> getHistoryFromRedis() {
        return stringRedisTemplate.keys("*");
    }
}
