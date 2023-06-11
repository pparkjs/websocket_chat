package kr.or.ddit.user.service;

import java.util.Map;

import kr.or.ddit.vo.UserVO;

public interface IUserService {

	public UserVO login(Map<String, Object> map);

}
