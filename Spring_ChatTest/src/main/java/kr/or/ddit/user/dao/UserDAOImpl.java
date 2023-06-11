package kr.or.ddit.user.dao;

import java.util.Map;

import javax.inject.Inject;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import kr.or.ddit.vo.UserVO;

@Repository
public class UserDAOImpl implements IUserDAO {
	
	@Inject
	private SqlSessionTemplate sqlSession;
	
	@Override
	public UserVO login(Map<String, Object> map) {
		return sqlSession.selectOne("User.login", map);
	}

}
