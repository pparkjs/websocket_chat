package kr.or.ddit.user.service;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.or.ddit.user.dao.IUserDAO;
import kr.or.ddit.vo.UserVO;

@Service
public class UserServiceImpl implements IUserService {
	
	@Inject
	private IUserDAO userDao;
	
	@Override
	public UserVO login(Map<String, Object> map) {
		return userDao.login(map);
	}

}
