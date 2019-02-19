package com.imooc.netty;

import com.imooc.SpringUtil;
import com.imooc.enums.MsgActionEnum;
import com.imooc.service.UserService;
import com.imooc.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用于记录和管理所有客户端的channel
    private static ChannelGroup users =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg)
            throws Exception {
        // 1. 获取客户端发来的消息
        String content = msg.text();
        DataContent dataContent = JsonUtils.jsonToPojo(content,DataContent.class);
        Integer action = dataContent.getAction();
        Channel currentChannel = ctx.channel();
        ChatMsg chatMsg = dataContent.getChatMsg();
        // 2. 判断消息的类型
        if(action == MsgActionEnum.CONNECT.getType()){
            // 2.1 WebSocket第一次建立连接，需要将用户的channel对象与userId进行关联
            UserChannelRel.put(chatMsg.getSenderId(),currentChannel);
            users.forEach(channel -> {
                System.out.println(channel.id());
            });
            UserChannelRel.print();
        }else if(action == MsgActionEnum.CHAT.getType()){
            // 2.2 发送聊天消息，存储聊天消息（加密），同时标记消息状态为未签收
            // 保存消息到数据库
            UserService userService = SpringUtil.getBean(UserService.class);
            String msgId = userService.saveMsg(new com.imooc.pojo.ChatMsg(chatMsg));
            chatMsg.setMsgId(msgId);
            DataContent dataContentMsg = new DataContent();
            dataContentMsg.setAction(MsgActionEnum.CHAT.getType());
            dataContentMsg.setChatMsg(chatMsg);
            // 发送消息
            Channel receiverChannel = UserChannelRel.get(chatMsg.getReceiverId());
            if(receiverChannel == null){
                // 用户离线 TODO
            }else{
                Channel realChannel = users.find(receiverChannel.id());
                if(realChannel==null){
                   // 用户离线 TODO
                }else{
                    realChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContentMsg)));
                }
            }
        }else if(action == MsgActionEnum.SIGNED.getType()){
             // 2.3 签收聊天消息，标记消息为已签收
            UserService userService = SpringUtil.getBean(UserService.class);
            String [] msgIdArray = chatMsg.getMsgId().split(",");
            List<String> msgIds = Arrays.asList(msgIdArray).stream().filter(id-> StringUtils.isNotBlank(id)).collect(Collectors.toList());
            if (msgIds != null && !msgIds.isEmpty() && msgIds.size()>0){
                userService.updateChatMsgSign(msgIds);
            }
        }else if(action == MsgActionEnum.KEEPALIVE.getType()){
            // 2.4 心跳检测

        }



    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放到ChannelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
		users.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id为："
                + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id为："
                + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //发生异常后关闭连接并从channelGroup中移除
        cause.printStackTrace();
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
