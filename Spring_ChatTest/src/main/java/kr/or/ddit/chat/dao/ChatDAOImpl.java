package kr.or.ddit.chat.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.StudyGroupVO;
import kr.or.ddit.vo.StudyMemberVO;

@Repository
public class ChatDAOImpl implements IChatDAO {
	
	@Inject
	private SqlSessionTemplate sqlSession;
	
	@Override
	public List<ChatMessageVO> messageList(int stNo) {
		return sqlSession.selectList("Chat.messageList", stNo);
	}

	@Override
	public int insertMessage(ChatMessageVO chatMessage) {
		return sqlSession.insert("Chat.insertMessage", chatMessage);
	}

	@Override
	public List<StudyGroupVO> getStudyGroupListById(String userId) {
		return sqlSession.selectList("Chat.getStudyGroupListById", userId);
	}

	@Override
	public List<StudyMemberVO> getStudyMember(int stNo) {
		return sqlSession.selectList("Chat.getStudyMember", stNo);
	}

	@Override
	public void updateMessageCountExceptMe(ChatMessageVO chatMessage) {
		sqlSession.update("Chat.updateMessageCountExceptMe", chatMessage);
	}

	@Override
	public void readChatMessage(ChatMessageVO chatMessage) {
		sqlSession.update("Chat.readChatMessage", chatMessage);
	}

	@Override
	public int getChatCnt(String userId) {
		return sqlSession.selectOne("Chat.getChatCnt", userId);
	}

	@Override
	public int getMemberCount(int stNo) {
		return sqlSession.selectOne("Chat.getMemberCount", stNo);
	}

	@Override
	public void insertUnreadMember(Map<String, Object> map) {
		sqlSession.insert("Chat.insertUnreadMember", map);
	}

	@Override
	public List<Integer> getUnreadCntByUser(ChatMessageVO chatMessage) {
		return sqlSession.selectList("Chat.getUnreadCntByUser", chatMessage);
	}

	@Override
	public void readMessageInRoom(ChatMessageVO chatMessage) {
		sqlSession.update("Chat.readMessageInRoom", chatMessage);
	}

	@Override
	public void deleteUnreadMsg(ChatMessageVO chatMessage) {
		sqlSession.update("Chat.deleteUnreadMsg", chatMessage);
	}

}
