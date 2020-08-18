package site.moku.printassistant.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisTools {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript luaMsScript;

    @PostConstruct
    private void init() {
        this.luaMsScript = new DefaultRedisScript();
        luaMsScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("/script/ms.lua")));
    }
}
