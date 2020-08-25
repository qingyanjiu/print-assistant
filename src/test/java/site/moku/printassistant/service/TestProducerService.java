package site.moku.printassistant.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.moku.printassistant.kafka.ProducerService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestProducerService {

//    @MockBean
//    private IPrintInfoService printInfoService;

    @Autowired
    private ProducerService service;


    @Before
    public void setUp() throws Exception {
//        when(printInfoService.getHistoryFromRedis()).thenReturn(null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProduceSaveData() {
        service.produceSaveData();
    }

}
