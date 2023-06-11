package kr.or.ddit.vo;

import lombok.Data;

@Data
public class StudyMemberVO {
	private int stNo;
	private String userId;
	private String userName;
	private String authRole;
	private int msgCount;
}
