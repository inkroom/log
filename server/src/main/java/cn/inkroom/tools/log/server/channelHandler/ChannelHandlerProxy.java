package cn.inkroom.tools.log.server.channelHandler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * netty要求对于每一个新的连接，channelHandler都必须是新的对象
 * <p>用此类绕过这个限制</p>
 *
 * @author 墨盒
 * @Deate 18-8-9
 */
public class ChannelHandlerProxy extends ChannelHandlerAdapter {




    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Controller.getInstance().channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Controller.getInstance().exceptionCaught(ctx, cause);
    }
}
