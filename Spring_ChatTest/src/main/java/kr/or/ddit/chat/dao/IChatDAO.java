package kr.or.ddit.chat.dao;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;

public interface IChatDAO {
	public List<ChatMessageVO> messageList(int roomId);
	public int insertMessage(ChatMessageVO chatMessage);
	public List<StudyGroupVO> getStudyGroupListById(String userId);
	public List<StudyMemberVO> getStudyMember(int stNo);
	public void updateMessageCountExceptMe(ChatMessageVO chatMessage);
	public void readChatMessage(ChatMessageVO chatMessage);
	public int getChatCnt(String userId);
	public int getMemberCount(int stNo);
	public void insertUnreadMember(Map<String, Object> map);
	public List<Integer> getUnreadCntByUser(ChatMessageVO chatMessage);
	public void readMessageInRoom(ChatMessageVO chatMessage);
	public void deleteUnreadMsg(ChatMessageVO chatMessage);
}
