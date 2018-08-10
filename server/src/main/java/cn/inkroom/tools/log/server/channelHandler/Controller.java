package cn.inkroom.tools.log.server.channelHandler;

import cn.inkroom.tools.log.server.Message;
import cn.inkroom.tools.log.server.OutTimer;
import cn.inkroom.tools.log.server.Queue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 单例
 *
 * @author 墨盒
 */
public class Controller extends ChannelHandlerAdapter {


    private static Controller instance;


    private long delay;
    private Queue queue;
    private OutTimer outTimer;
    private Pattern pattern = Pattern.compile("([0-9]+)-(.*)");
    private Logger logger = Logger.getLogger(getClass());

    private Controller(long delay) {
        queue = new Queue();
        this.delay = delay;
        outTimer = new OutTimer(queue, delay);
        outTimer.start();

    }

    private Controller() {
        queue = new Queue();
        this.delay = 10;
        outTimer = new OutTimer(queue, delay);
        outTimer.start();
    }


    public static Controller getInstance(long delay) {
        if (instance == null) {
            instance = new Controller(delay);
        }
        return instance;
    }

    static Controller getInstance() {
        if (instance == null) {
            throw new NullPointerException("controller未实例化");

        }
        return instance;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //do something msg
        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "utf-8");
        logger.info(request);
        //写给客户端
//        String response = "我是反馈的信息";
//        ctx.writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
        //.addListener(ChannelFutureListener.CLOSE);
        Matcher matcher = pattern.matcher(request);
        if (matcher.find()) {
            Message m = new Message(Long.parseLong(matcher.group(1)), matcher.group(2));
            queue.add(m);
            if (outTimer == null) {
                outTimer = new OutTimer(queue, delay);
                outTimer.start();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
