package kr.or.ddit.study.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.or.ddit.study.dao.IStudyGroupDAO;
import kr.or.ddit.vo.AlarmVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;
import kr.or.ddit.vo.UserVO;

@Service
public class StudyGroupServiceImpl implements IStudyGroupService {
	
	@Inject
	private IStudyGroupDAO studyDao;
	
	@Override
	public List<StudyGroupVO> getStudyGroupList() {
		return studyDao.getStudyGroupList();
	}

	@Override
	public int insertStudy(StudyGroupVO study) {
		// 스터디그룹 생성
		int result = studyDao.insertStudy(study);
		
		study.setAuthRole("스터디장");
		// 스터디장 스터디 멤버에 추가
		studyDao.addStudyMember(study);
		
		String userId = study.getUserId();
		// 자신을 뺀 모든 유저의 List를 가져옴
		List<UserVO> userList = studyDao.getUserList(userId);
		Map<String , Object> map = new HashMap<String, Object>();
		for (UserVO user : userList) {
			map.put("senderName", study.getUserName());
			map.put("userId", user.getUserId());
			map.put("stNo", study.getStNo());
			
			// alarm테이블에 자신을 뺀 모든 유저에 대한 알림 추가 
			studyDao.insertAlarm(map);
		}
		return result;
	}

	@Override
	public List<AlarmVO> getAlarmInfo(String userId) {
		return studyDao.getAlarmInfo(userId);
	}

	@Override
	public void deleteAlarm(int alarmNo) {
		studyDao.deleteAlarm(alarmNo);
	}

	@Override
	public int joinStudyGroup(StudyMemberVO member) {
		return studyDao.joinStudyGroup(member);
	}

}
