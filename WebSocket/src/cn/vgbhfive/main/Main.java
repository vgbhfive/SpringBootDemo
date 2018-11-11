package cn.vgbhfive.main;

import cn.vgbhfive.handler.MyWebSocketChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 启动这个应用
 * @author Vgbh
 *
 */
public class Main {

	public static void main(String[] args) {
		EventLoopGroup boos = new NioEventLoopGroup();
		EventLoopGroup work = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boos, work);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new MyWebSocketChannelHandler());
			System.out.println("Server starting...Waiting for connection.");
			Channel ch = b.bind(8888).sync().channel();
			ch.close().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			boos.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	
}
