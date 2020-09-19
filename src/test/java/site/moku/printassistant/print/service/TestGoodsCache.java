package site.moku.printassistant.print.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import site.moku.printassistant.kafka.MyKafkaConsumerMulti;
import site.moku.printassistant.redis.GoodService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestGoodsCache {

  @Autowired
  private GoodService goodsService;

  @MockBean
  private MyKafkaConsumerMulti myKafkaConsumerMulti;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Before
  public void setUp() throws Exception {
  }

  @After
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
