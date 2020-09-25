package site.moku.printassistant.print.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import site.moku.printassistant.print.AbstractControllerTest;
import site.moku.printassistant.print.service.IPrintInfoService;

@SpringBootTest
public class TestIndexController extends AbstractControllerTest {

    private MockHttpServletRequest request;

//    @MockBean
//    private IPrintInfoService printInfoService;

    @SpyBean
    private IPrintInfoService printInfoService;

    @BeforeEach
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//        Mockito.when(printInfoService.getFromRedis("test")).thenReturn("test-value");
        Mockito.doReturn("test-value").when(printInfoService).getFromRedis("test");
    }

    @Test
    public void testAny() throws Exception {
        String res = printInfoService.getFromRedis("test1");
        Assertions.assertEquals("test-value", res);
    }
}
