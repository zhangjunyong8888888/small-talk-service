package com.imooc.service;

import com.imooc.pojo.ChatMsg;
import com.imooc.pojo.Users;
import com.imooc.pojo.vo.FriendRequestVO;
import com.imooc.pojo.vo.MyFriendsVO;

import java.util.List;

public interface UserService {

    /**
     * @Description: 判断用户名是否存在
     */
     boolean queryUsernameIsExist(String username);

    /**
     * @Description: 查询用户是否存在
     */
     Users queryUserForLogin(String username, String pwd);

    /**
     * @Description: 用户注册
     */
     Users saveUser(Users user);


     /**
      * @Description: 修改用户记录
      */
     Users updateUserInfo(Users user);


     Integer preconditionSearchFriends(String myUserId, String friendUsername);

     Users queryUserInfoByUsername(String friendUsername);

     void sendFriendRequest(String myUserId, String friendUsername);

    List<FriendRequestVO> queryFriendRequestList(String userId);

    /**
     * @Description: 删除好友请求记录
     */
     void deleteFriendRequest(String sendUserId, String acceptUserId);

    /**
     * @Description: 通过好友请求
     * 				1. 保存好友
     * 				2. 逆向保存好友
     * 				3. 删除好友请求记录
     */
     void passFriendRequest(String sendUserId, String acceptUserId);

    /**
     * @Description: 查询好友列表
     */
     List<MyFriendsVO> queryMyFriends(String userId);

     String  saveMsg(ChatMsg chatMsg);

    /**
     * 批量签收消息
      * @param msgIds
     * @return
     */
    int updateChatMsgSign(List<String> msgIds);

    /**
     * @Description: 获取未签收消息列表
     */
    List<ChatMsg> getUnReadMsgList(String acceptUserId);
}
