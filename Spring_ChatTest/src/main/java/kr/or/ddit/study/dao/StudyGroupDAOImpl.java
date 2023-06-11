package kr.or.ddit.study.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.or.ddit.vo.AlarmVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;
import kr.or.ddit.vo.UserVO;

@Repository
public class StudyGroupDAOImpl implements IStudyGroupDAO {

	@Inject
	private SqlSessionTemplate sqlSession;
	
	@Override
	public List<StudyGroupVO> getStudyGroupList() {
		return sqlSession.selectList("StudyGroup.getStudyGroupList");
	}

	@Override
	public void insertAlarm(Map<String, Object> map) {
		sqlSession.insert("StudyGroup.insertAlarm", map);
	}

	@Override
	public int insertStudy(StudyGroupVO study) {
		return sqlSession.insert("StudyGroup.insertStudy", study);
	}

	@Override
	public List<AlarmVO> getAlarmInfo(String userId) {
		return sqlSession.selectList("StudyGroup.getAlarmInfo", userId);
	}

	@Override
	public List<UserVO> getUserList(String userId) {
		return sqlSession.selectList("StudyGroup.getUserList", userId);
	}

	@Override
	public void addStudyMember(StudyGroupVO study) {
		sqlSession.insert("StudyGroup.addStudyMember", study);
	}

	@Override
	public void deleteAlarm(int alarmNo) {
		sqlSession.delete("StudyGroup.deleteAlarm", alarmNo);
	}

	@Override
	public int joinStudyGroup(StudyMemberVO member) {
		return sqlSession.insert("StudyGroup.joinStudyGroup", member);
	}

}
