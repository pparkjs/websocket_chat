package kr.or.ddit.chat.service;

import java.util.List;

import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;

public interface IChatService {

	public List<ChatMessageVO> messageList(int roomId);

	public int insertMessage(ChatMessageVO chatMessage);

	public List<StudyGroupVO> getStudyGroupListById(String userId);

	public List<StudyMemberVO> getStudyMember(int stNo);

	public void readChatMessage(ChatMessageVO chatMessage);

	public int getChatCnt(String userId);

	public int getMemberCount(int stNo);

	public List<Integer> getUnreadCntByUser(ChatMessageVO chatMessage);

	public void readMessageInRoom(ChatMessageVO chatMessage);
	
}
