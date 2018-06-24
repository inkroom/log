package cn.inkroom.software.log.client;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author 墨盒
 */
public class InkSocketAppender extends AppenderSkeleton {
    private Layout layout;
    private String host;
    private OutputStreamWriter out;
    private int port = 4560;
    private String ip;
    private boolean syn = false;//同步或异步
    private boolean requireIp = true;//是否需要输出ip

    public InkSocketAppender() {
    }

    public void setSyn(boolean syn) {
        this.syn = syn;
    }

    public void setRequireIp(boolean requireIp) {
        this.requireIp = requireIp;
    }

    private void getIp() {
        if (requireIp) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress() + "-";
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            ip = "";
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private void connect() {
        try {
            if (syn) {
                new Thread(() -> {
                    try {
                        createOut();
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogLog.warn("can't connect the " + this.host);
                    }
                }).start();
            } else {
                createOut();
            }
        } catch (Exception e) {
            LogLog.error("can't connect the " + this.host);
        }

    }

    private void createOut() throws IOException {
        out = new OutputStreamWriter(new Socket(this.host, this.port).getOutputStream());
    }

    @Override
    public void activateOptions() {
        connect();
        getIp();
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (this.out != null && this.layout != null) {
            String message = this.layout.format(loggingEvent);
            try {
                if (syn && out != null) {//完成连接前的日志都将丢失
                    new Thread(() -> {
                        try {
                            out.write(System.currentTimeMillis() + "-" + ip + message);
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    this.out.write(System.currentTimeMillis() + "-" + ip + message);
                    this.out.flush();
                }

            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                this.out = null;
                LogLog.error("send socket message fail");
            }
        }
    }

    @Override
    public void close() {
        if (this.out != null) {
            try {
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}
