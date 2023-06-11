package kr.or.ddit.vo;

import lombok.Data;

@Data
public class StudyGroupVO {
	private int stNo;
	private String stTitle;
	private String userId;
	private String stDate;
	private String userName;
	
	//DB에는 없음 스터디장설정 위한 변수
	private String authRole;
	private int msgCount;
}
