import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author 墨盒
 */
public class LogTest {

    @Test
    public void test(){

        Logger logger = Logger.getLogger(String.class);
        logger.info("测试消息");
        logger.info("测试消息2");
        logger.debug("测试消息3");

        logger = Logger.getLogger(Integer.class);
        logger.info("测试消息");
        logger.info("测试消息2");
        logger.debug("测试消息3");
    }

}
