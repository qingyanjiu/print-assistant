package site.moku.printassistant.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPrintInfoService {

    @MockBean
    private IPrintInfoService printInfoService;


    @Before
    public void setUp() throws Exception {
        when(printInfoService.getHistoryFromRedis()).thenReturn(null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetHistoryFromRedis() {
        Assert.assertNull(printInfoService.getHistoryFromRedis());
        verify(printInfoService,times(1)).getHistoryFromRedis();
    }
}
