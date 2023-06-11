package kr.or.ddit.study.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.AlarmVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;

public interface IStudyGroupService {

	public List<StudyGroupVO> getStudyGroupList();

	public int insertStudy(StudyGroupVO study);

	public List<AlarmVO> getAlarmInfo(String userId);

	public void deleteAlarm(int alarmNo);

	public int joinStudyGroup(StudyMemberVO member);

}
