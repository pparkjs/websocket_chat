package kr.or.ddit.vo;

import lombok.Data;

@Data
public class AlarmVO {
	private int alarmNo;
	private String userId;
	private String alarmType;
	private String alarmCdate;
	private String alarmPrefix;
	private int stNo;
}
