package com.imooc.netty;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户ID 与 Channel 的关联关系
 */
public class UserChannelRel {

    private static Map<String, Channel> manager = new HashMap<>();

    public static void put(String senderId,Channel channel){
        manager.put(senderId,channel);
    }

    public static Channel get(String senderId){
        return manager.get(senderId);
    }

    public static void print(){
        for (HashMap.Entry<String,Channel> entry :manager.entrySet()){
            System.out.println(entry.getKey() + "------" + entry.getValue().id().asLongText());
        }
    }
}
