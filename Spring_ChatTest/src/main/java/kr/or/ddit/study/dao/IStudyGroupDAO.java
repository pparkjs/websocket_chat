package kr.or.ddit.study.dao;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.AlarmVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;
import kr.or.ddit.vo.UserVO;

public interface IStudyGroupDAO {

	public List<StudyGroupVO> getStudyGroupList();
	public void insertAlarm(Map<String, Object> map);
	public int insertStudy(StudyGroupVO study);
	public List<AlarmVO> getAlarmInfo(String userId);
	public List<UserVO> getUserList(String userId);
	public void addStudyMember(StudyGroupVO study);
	public void deleteAlarm(int alarmNo);
	public int joinStudyGroup(StudyMemberVO member);

}
