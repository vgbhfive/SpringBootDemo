package cn.vgbhfive.handler;

import java.util.Date;

import cn.vgbhfive.config.NettyConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

/**
 * 接收/处理/响应客户端websocket请求的核心业务请求处理类
 * @author Vgbh
 *
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {
	
	private WebSocketServerHandshaker handshaker;
	private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";
	
	//客户端与服务端创建连接的时候调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyConfig.group.add(ctx.channel());
		System.out.println("Server starting... Waiting for Client connection.");
	}

	//客户端与服务端断开连接的时候调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyConfig.group.remove(ctx.channel());
		System.out.println("Client out of connection.");
	}

	//服务端接收客户端的数据完成之后调用
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	//工程出现异常的时候调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	//服务端处理客户端WebSocket请求的核心处理方法
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			//处理客户端向服务端发起Http握手请求的业务
			handHttpRequest(ctx, (FullHttpRequest)msg);
		} else if (msg instanceof WebSocketFrame) {
			//处理WebSocket连接业务
			handWebSocketFrame(ctx, (WebSocketFrame)msg);
		}
	}
	
	/**
	 * 处理客户端向服务端发起Http请求的业务
	 * @param ctx
	 * @param req
	 */
	private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		if (!req.getDecoderResult().isSuccess()	|| !("websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}
	
	private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		//服务端向客户端发送数据
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	/**
	 * 处理客户端与服务端之间的websocket业务
	 * @param ctx
	 * @param frame
	 */
	private void handWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof CloseWebSocketFrame) {
			//确定是否是关闭WebSocket的指令
			handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
		}
		if (frame instanceof PingWebSocketFrame) {
			//确保是否是ping消息
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if ( !(frame instanceof TextWebSocketFrame)) {
			//判断是否为二进制消息，如果是二进制消息，则抛出异常
			System.out.println("我们目前不支持二进制消息！");
			throw new RuntimeException("{" + this.getClass().getName() + "} 不支持消息");
		}
		//返回应答消息
		//获取客户端向服务端发送的消息
		String request = ((TextWebSocketFrame)frame).text();
		System.out.println("Server receive message from Client:" + request);
		TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + " ----> " + request);
		//群发，服务端向所有连接的客户端发送消息
		NettyConfig.group.write(tws);
	}
	
}






