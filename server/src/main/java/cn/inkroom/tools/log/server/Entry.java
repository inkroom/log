package cn.inkroom.tools.log.server;

import cn.inkroom.tools.log.server.channelHandler.Controller;
import cn.inkroom.tools.log.server.config.Config;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Properties;

/**
 * @author 墨盒
 */
public class Entry {
    public static void main(String[] args) {


        Config config = new Config();

        if (config.loadConfig("config.conf")) {

            try {
                Properties properties = new Properties();

                properties.load(Object.class.getResourceAsStream("log4j.properties"));

                properties.setProperty("log4j.appender.file.level", config.getLevel());

                properties.setProperty("log4j.appender.file.fileName", config.getPath());

                PropertyConfigurator.configure(properties);
            } catch (IOException e) {
                System.out.println("配置文件读取错误");
            }

            LogServer logServer = new LogServer(config.getPort(), Controller.getInstance(config.getDelay()));
            logServer.start();
        } else {
            System.out.println("配置文件错误");
        }


//        Controller controller = new Controller(10);


    }
}
