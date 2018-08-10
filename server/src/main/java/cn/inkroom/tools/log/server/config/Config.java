package cn.inkroom.tools.log.server.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author 墨盒
 * @Deate 18-8-9
 */
public class Config {

    private long delay = 10;
    private int port = 8999;
    private String path = "/home/inkbox/";
    private String level = "info";


    public boolean loadConfig(String path) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(path));


            this.delay = Long.parseLong(properties.getProperty(delay, String.valueOf(delay)));

            this.port = Integer.parseInt(properties.getProperty("port", String.valueOf(this.port)));

            this.path = properties.getProperty("file_path");

            this.level = properties.getProperty("level", this.level);


            if (delay <= 0) {
                return false;
            }

            if (port < 0 || port > 65535) {
                return false;
            }

            if (this.path == null || this.path.isEmpty()) {
                return false;
            }

            if (this.level == null || this.level.isEmpty()) {
                return false;
            }

            this.level = this.level.toLowerCase();


            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public long getDelay() {
        return delay;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getLevel() {
        return level;
    }
}
