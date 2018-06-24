package cn.inkroom.software.log.server;

/**
 * @author 墨盒
 */
public class Entry {
    public static void main(String[] args) {


        Controller controller = new Controller(10);

        LogServer logServer = new LogServer(8379, controller);
        logServer.start();
    }
}
