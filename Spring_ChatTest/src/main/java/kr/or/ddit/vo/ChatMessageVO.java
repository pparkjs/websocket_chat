package kr.or.ddit.vo;

import lombok.Data;

@Data
public class ChatMessageVO {
	private int messageId;
	private int stNo;
	private String messageContent;
	private String messageRegdate;
	private String userId;
	private String userName;
	private int unreadCount;
	
	// DB에 없는 필요한 변수
	private String type;
}
