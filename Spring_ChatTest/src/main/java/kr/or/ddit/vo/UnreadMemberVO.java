package kr.or.ddit.vo;

import lombok.Data;

@Data
public class UnreadMemberVO {
	private int messageId;
	private String userId;
	private int readCheck;
	private int stNo;
}
