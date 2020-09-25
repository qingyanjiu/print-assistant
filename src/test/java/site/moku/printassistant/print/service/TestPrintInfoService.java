package site.moku.printassistant.print.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TestPrintInfoService {

//    @MockBean
//    private IPrintInfoService printInfoService;

    @Autowired
    private IPrintInfoService printInfoService;


    @BeforeEach
    public void setUp() throws Exception {
//        when(printInfoService.getHistoryFromRedis()).thenReturn(null);
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testSaveToRedis() {
        printInfoService.saveToRedis("gogo","testgogo");
    }

    @Test
    public void testGetHistoryFromRedis() {
        Assertions.assertNull(printInfoService.getHistoryFromRedis());
        verify(printInfoService,times(1)).getHistoryFromRedis();
    }

    @Test
    public void testTestCache() {
        System.out.println(printInfoService.testCache());
    }
}
