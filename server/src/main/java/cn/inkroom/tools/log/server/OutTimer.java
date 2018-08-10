package cn.inkroom.tools.log.server;

/**
 * @author 墨盒
 */
public class OutTimer extends Thread {

    private Queue queue;

    private long interval;

    public OutTimer(Queue queue, long interval) {
        this.queue = queue;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            queue.println();
        }

    }
}
