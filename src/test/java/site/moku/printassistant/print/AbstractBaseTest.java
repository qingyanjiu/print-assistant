package site.moku.printassistant.print;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class AbstractBaseTest {

  @BeforeEach
  public void setUp(TestInfo testInfo) {
    System.out.println(
        "----------  Test "
            + testInfo.getDisplayName()
            + " Begin "
            + LocalDateTime.now()
            + "  ----------");
  }

  @AfterEach
  public void tearDown(TestInfo testInfo) {
    System.out.println(
        "----------   Test "
            + testInfo.getDisplayName()
            + " End  "
            + LocalDateTime.now()
            + "  ----------");
  }
}

