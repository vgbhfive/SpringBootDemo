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
 * ����/����/��Ӧ�ͻ���websocket����ĺ���ҵ����������
 * @author Vgbh
 *
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {
	
	private WebSocketServerHandshaker handshaker;
	private static final String WEB_SOCKET_URL = "ws://localhost:8888/websocket";
	
	//�ͻ��������˴������ӵ�ʱ�����
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyConfig.group.add(ctx.channel());
		System.out.println("Server starting... Waiting for Client connection.");
	}

	//�ͻ��������˶Ͽ����ӵ�ʱ�����
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyConfig.group.remove(ctx.channel());
		System.out.println("Client out of connection.");
	}

	//����˽��տͻ��˵��������֮�����
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	//���̳����쳣��ʱ�����
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	//����˴���ͻ���WebSocket����ĺ��Ĵ�����
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			//����ͻ��������˷���Http���������ҵ��
			handHttpRequest(ctx, (FullHttpRequest)msg);
		} else if (msg instanceof WebSocketFrame) {
			//����WebSocket����ҵ��
			handWebSocketFrame(ctx, (WebSocketFrame)msg);
		}
	}
	
	/**
	 * ����ͻ��������˷���Http�����ҵ��
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
		//�������ͻ��˷�������
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	/**
	 * ����ͻ���������֮���websocketҵ��
	 * @param ctx
	 * @param frame
	 */
	private void handWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof CloseWebSocketFrame) {
			//ȷ���Ƿ��ǹر�WebSocket��ָ��
			handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame.retain());
		}
		if (frame instanceof PingWebSocketFrame) {
			//ȷ���Ƿ���ping��Ϣ
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if ( !(frame instanceof TextWebSocketFrame)) {
			//�ж��Ƿ�Ϊ��������Ϣ������Ƕ�������Ϣ�����׳��쳣
			System.out.println("����Ŀǰ��֧�ֶ�������Ϣ��");
			throw new RuntimeException("{" + this.getClass().getName() + "} ��֧����Ϣ");
		}
		//����Ӧ����Ϣ
		//��ȡ�ͻ��������˷��͵���Ϣ
		String request = ((TextWebSocketFrame)frame).text();
		System.out.println("Server receive message from Client:" + request);
		TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + " ----> " + request);
		//Ⱥ������������������ӵĿͻ��˷�����Ϣ
		NettyConfig.group.write(tws);
	}
	
}






