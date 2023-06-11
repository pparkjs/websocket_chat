package kr.or.ddit.chat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.or.ddit.chat.dao.IChatDAO;
import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;

@Service
public class ChatServiceImpl implements IChatService {
	
	@Inject
	private IChatDAO chatDao;
	
	@Override
	public List<ChatMessageVO> messageList(int stNo) {
		return chatDao.messageList(stNo);
	}

	@Override
	public int insertMessage(ChatMessageVO chatMessage) {
		// 메시지 삽입
		int result = chatDao.insertMessage(chatMessage);
		
		// 나를 제외한 해당 방 사람들에게 안읽은 메시지 카운트
		chatDao.updateMessageCountExceptMe(chatMessage);
		
		int stNo = chatMessage.getStNo();
		List<StudyMemberVO> memberList = chatDao.getStudyMember(stNo);
		
		Map<String, Object> map = new HashMap<String, Object>();
		for (StudyMemberVO member : memberList) {
			map.put("userId", member.getUserId());
			map.put("messageId", chatMessage.getMessageId());
			map.put("stNo", chatMessage.getStNo());
			// 메시지 등록 시마다 해당 방에 속한 유저들 대상으로 안읽은 유저 테이블에 추가
			chatDao.insertUnreadMember(map);
		}
		return result;
	}

	@Override
	public List<StudyGroupVO> getStudyGroupListById(String userId) {
		return chatDao.getStudyGroupListById(userId);
	}

	@Override
	public List<StudyMemberVO> getStudyMember(int stNo) {
		return chatDao.getStudyMember(stNo);
	}

	@Override
	public void readChatMessage(ChatMessageVO chatMessage) {
		chatDao.readChatMessage(chatMessage);
	}

	@Override
	public int getChatCnt(String userId) {
		return chatDao.getChatCnt(userId);
	}

	@Override
	public int getMemberCount(int stNo) {
		return chatDao.getMemberCount(stNo);
	}

	@Override
	public List<Integer> getUnreadCntByUser(ChatMessageVO chatMessage) {
		return chatDao.getUnreadCntByUser(chatMessage);
	}

	@Override
	public void readMessageInRoom(ChatMessageVO chatMessage) {
		// 안읽은 멤버 테이블에 해당 방에 대한 유저에 관한 행 삭제
		chatDao.deleteUnreadMsg(chatMessage);
		chatDao.readMessageInRoom(chatMessage);
	}

}
