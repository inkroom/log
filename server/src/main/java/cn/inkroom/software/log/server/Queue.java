package cn.inkroom.software.log.server;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author 墨盒
 */
public class Queue {

    private Logger logger = Logger.getLogger(getClass());

    private LinkedList<Message> list;

    public Queue() {
        list = new LinkedList<>();
    }

    public void add(Message m) {
        synchronized (Queue.class) {
            int size = list.size();
            if (size == 0) {
                list.add(m);
                return;
            }
            for (int i = size - 1; i >= 0; i--) {
                if (list.get(i).getTime() <= m.getTime()) {
                    if (i != size - 1) {
                        list.add(i + 1, m);

                    } else {
                        list.add(m);
                    }
                    break;
                }
            }
        }
    }


    public void println() {
        synchronized (Queue.class) {
            Iterator<Message> iterator = list.iterator();
            while (iterator.hasNext()) {
                Message m = iterator.next();

                String message = m.getMessage().toUpperCase();
                if (message.contains("INFO")) {
                    logger.info(m.getMessage());
                } else if (message.contains("DEBUG")) {
                    logger.debug(m.getMessage());
                } else if (message.contains("WARN")) {
                    logger.warn(m.getMessage());
                } else if (message.contains("ERROR")) {
                    logger.error(m.getMessage());
                } else if (message.contains("TRACE")) {
                    logger.trace(m.getMessage());
                } else {
                    logger.info(m.getMessage());
                }

                iterator.remove();
            }
        }
    }

}
