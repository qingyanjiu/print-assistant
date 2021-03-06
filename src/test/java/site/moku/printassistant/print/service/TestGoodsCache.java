package site.moku.printassistant.print.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import site.moku.printassistant.redis.GoodService;

import java.util.Set;

@SpringBootTest
public class TestGoodsCache {

  @Autowired
  private GoodService goodsService;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @BeforeEach
  public void setUp() throws Exception {
  }

  @AfterEach
  public void tearDown() throws Exception {
  }

  @Test
  public void testQryGoods() {
    System.out.println(goodsService.qryGoodsInfo("1"));
  }

  @Test
  public void testSet() throws JsonProcessingException {

    SetOperations<String, String> setOptions = redisTemplate.opsForSet();

    String[] set1 = new String[]{"1","2","3"};
    String[] set2 = new String[]{"4","2","3"};
    setOptions.add("set1",set1);
    setOptions.add("set2",set2);

    Set<String> res = redisTemplate.opsForSet().intersect("set1","set2");
    System.out.println(res);
  }


}
