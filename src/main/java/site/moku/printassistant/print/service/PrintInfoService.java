/**
 * @author louisliu
 */

package site.moku.printassistant.print.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class PrintInfoService implements IPrintInfoService {

    private static final String PREFIX = "print::";

    @Value("${print.history.expire.days}")
    private long expireDays;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveToRedis(String printKey, String content) {
        stringRedisTemplate.opsForValue().set(PREFIX.concat(printKey), content, expireDays, TimeUnit.DAYS);
    }

    @Override
    public String getFromRedis(String printKey) {
        return stringRedisTemplate.opsForValue().get(printKey);
    }

    @Override
    public Set<String> getHistoryFromRedis() {
        return stringRedisTemplate.keys(PREFIX.concat("*"));
    }

    @Cacheable(value = "test",key = "'testcache'")
    public int testCache() {
        return ThreadLocalRandom.current().nextInt(10000);
    }
}
