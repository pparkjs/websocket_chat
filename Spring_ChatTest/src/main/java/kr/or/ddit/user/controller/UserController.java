package kr.or.ddit.user.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.user.service.IUserService;
import kr.or.ddit.vo.UserVO;

@Controller
public class UserController {
	
	@Inject
	private IUserService userService;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(@RequestParam Map<String, Object> map, HttpSession session) {
		String goPage = "";
		UserVO user = userService.login(map);
		if(user != null) {
			session.setAttribute("user", user);
			goPage = "redirect:/studyhome";
		}else {
			goPage = "login";
		}
		return goPage;
	}
}
