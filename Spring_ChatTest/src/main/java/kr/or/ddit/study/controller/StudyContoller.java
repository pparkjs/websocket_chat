package kr.or.ddit.study.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.study.service.IStudyGroupService;
import kr.or.ddit.vo.AlarmVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;
import kr.or.ddit.vo.UserVO;

@Controller
public class StudyContoller {
	
	@Inject
	private IStudyGroupService studyService;
	
	@RequestMapping(value="studyhome", method = RequestMethod.GET)
	public String studyForm(Model model) {
		List<StudyGroupVO> list = studyService.getStudyGroupList();
		model.addAttribute("studyList", list);
		return "study";
	}
	
	// 스터디그룹 생성(채팅방)
	@RequestMapping(value="studyhome", method = RequestMethod.POST)
	public String insertStudy(StudyGroupVO study) {
		String goPage = "";
		int result = studyService.insertStudy(study);
		if(result > 0) {
			goPage = "redirect:/studyhome";
		}else {
			goPage = "study";
		}
		return goPage;
	}
	
	// 해당 유저의 안읽은 알람 정보 가져오기
	@ResponseBody
	@PostMapping("/studyHome/getAlarmInfo")
	public List<AlarmVO> getAlarmInfo(HttpSession session) {
		UserVO userVO = (UserVO)session.getAttribute("user");
		
		List<AlarmVO> data = studyService.getAlarmInfo(userVO.getUserId());
		
		return data;
	}
	
	// 읽은 알람 삭제하기
	@ResponseBody
	@PostMapping("/studyHome/deleteAlarm")
	public ResponseEntity<String> deleteAlarm(int alarmNo) {
		studyService.deleteAlarm(alarmNo);
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}
	
	// 스터디 가입하기 
	@ResponseBody
	@PostMapping("/joinStudy")
	public ResponseEntity<String> joinStudyGroup(@RequestBody StudyMemberVO member) {
		int result = studyService.joinStudyGroup(member);
		
		if(result > 0) {
			return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("FAILED", HttpStatus.BAD_REQUEST);
		}
	}
	
}
