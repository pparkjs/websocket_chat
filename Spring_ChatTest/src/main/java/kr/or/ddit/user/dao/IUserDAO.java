package kr.or.ddit.user.dao;

import java.util.Map;

import kr.or.ddit.vo.UserVO;

public interface IUserDAO {

	public UserVO login(Map<String, Object> map);

}
