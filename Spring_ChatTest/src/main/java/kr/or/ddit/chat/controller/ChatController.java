package kr.or.ddit.chat.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.chat.service.IChatService;
import kr.or.ddit.study.service.IStudyGroupService;
import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;
import kr.or.ddit.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

@Controller
public class ChatController {
	
	@Inject
	private IChatService chatService;	
	
	// 채팅방 폼으로 이동
	@RequestMapping(value="chathome", method = RequestMethod.GET)
	public String chatForm() {
		return "chat";
	}
	
	// 채팅방 리스트 가져오기 ajax
	@ResponseBody
	@RequestMapping(value="/chatList.do", method = RequestMethod.POST)
	public ResponseEntity<List<StudyGroupVO>> getChatList(HttpSession session) {
		UserVO user = (UserVO)session.getAttribute("user");
		String userId = user.getUserId();
		
		// 해당 유저가 속해있는 채팅방 가져오기
		List<StudyGroupVO> list = chatService.getStudyGroupListById(userId);
		return new ResponseEntity<List<StudyGroupVO>>(list, HttpStatus.OK);
	}
	
	@ResponseBody //객체를 응답 데이터로 보내기위한 어노테이션 (default 데이터 형식 json)
	@RequestMapping(value="{stNo}.do")
	public ResponseEntity<List<ChatMessageVO>> messageList(@PathVariable int stNo, String userId) {
		// 해당 방의 매시지 리스트 가져오기
		List<ChatMessageVO> list = chatService.messageList(stNo); 
		// userId 는 안읽은 메시지 처리 위해서 받아온건데 1:1이 아니라 멀티 채팅방일때 생각중
		return new ResponseEntity<List<ChatMessageVO>>(list, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/studymember.do")
	public ResponseEntity<List<StudyMemberVO>> getStudyMember(int stNo) {
		// 해당 방의 멤버 리스트 가져오기
		List<StudyMemberVO> list = chatService.getStudyMember(stNo); 
		return new ResponseEntity<List<StudyMemberVO>>(list, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value="/getChatCnt.do")
	public ResponseEntity<Integer> getChatCnt(String userId) {
		// 해당 유저의 안읽은 메시지 개수 가져오기
		int chatCnt = chatService.getChatCnt(userId); 
		return new ResponseEntity<Integer>(chatCnt, HttpStatus.OK);
	}
	
	
	
}
