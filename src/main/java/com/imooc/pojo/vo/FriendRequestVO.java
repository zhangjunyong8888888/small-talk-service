package com.imooc.pojo.vo;

/**
 * @Description: 好友请求发送方的信息
 */
public class FriendRequestVO {
	
    private String sendUserId;
    private String sendUsername;
    private String sendFaceImage;
    private String sendNickname;

	@Override
	public int hashCode() {
		String s = sendUserId + sendUsername;
		return s.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		String s = sendUserId + sendUsername;
		FriendRequestVO friendRequestVO = (FriendRequestVO) obj;
		String s1 = friendRequestVO.getSendUserId() + friendRequestVO.getSendUsername();
		return s.equals(s1);
	}

	public String getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	public String getSendUsername() {
		return sendUsername;
	}
	public void setSendUsername(String sendUsername) {
		this.sendUsername = sendUsername;
	}
	public String getSendFaceImage() {
		return sendFaceImage;
	}
	public void setSendFaceImage(String sendFaceImage) {
		this.sendFaceImage = sendFaceImage;
	}
	public String getSendNickname() {
		return sendNickname;
	}
	public void setSendNickname(String sendNickname) {
		this.sendNickname = sendNickname;
	}
}