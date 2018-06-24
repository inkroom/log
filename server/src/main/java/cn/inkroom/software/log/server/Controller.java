package cn.inkroom.software.log.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 墨盒
 */
public class Controller extends ChannelHandlerAdapter {

    private long interval;
    private Queue queue;
    private OutTimer outTimer;
    private Pattern pattern = Pattern.compile("([0-9]+)-(.*)");
    private Logger logger = Logger.getLogger(getClass());

    public Controller(long interval) {
        queue = new Queue();
        this.interval = interval;
        outTimer = new OutTimer(queue, interval);
        outTimer.start();

    }

    public Controller() {
        queue = new Queue();
        this.interval = 10;
        outTimer = new OutTimer(queue, interval);
        outTimer.start();
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
                outTimer = new OutTimer(queue, interval);
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
