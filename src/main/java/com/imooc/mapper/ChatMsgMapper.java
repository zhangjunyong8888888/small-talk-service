package com.imooc.mapper;

import com.imooc.pojo.ChatMsg;
import com.imooc.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChatMsgMapper extends MyMapper<ChatMsg> {

    int updateChatMsgSign(@Param("msgIds")List msgIds,@Param("status") Integer status);
}