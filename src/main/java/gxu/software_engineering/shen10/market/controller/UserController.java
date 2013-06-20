package gxu.software_engineering.shen10.market.controller;

import java.util.List;

import gxu.software_engineering.shen10.market.entity.User;
import gxu.software_engineering.shen10.market.service.UserService;
import static gxu.software_engineering.shen10.market.util.Consts.*;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 卖家操作控制器。
 * 
 * @author longkai
 * @email  im.longkai@gmail.com
 * @since  2013-6-20
 */
@Controller
@Scope("session")
public class UserController {
	
	private static final Logger L = LoggerFactory.getLogger(UserController.class);
	
	/** 最新信息 */
	private static final int	LATEST		= 1;

	/** 加载更多 */
	private static final int	LATEST_MORE	= 2;

	/** 刷新 */
	private static final int	REFRESH		= 3;
	
	@Inject
	private UserService userService;
	
	@RequestMapping(value = "/register", method = POST)
	public String register(Model model, User user, @RequestParam("pwd") String pwd) {
		L.info("卖家注册：{}", user);
		userService.register(user, pwd);
		model.addAttribute(STATUS, STATUS_OK);
		return BAD_REQUEST;
	}
	
	@RequestMapping(value = "/login", method = GET)
	public String login(
			Model model,
			@RequestParam("account") String account,
			@RequestParam("password") String password) {
		L.info("卖家登陆：account: {}", account);
		User user = userService.login(account, password);
		model.addAttribute(STATUS, STATUS_OK);
		model.addAttribute(USER, user);
		return BAD_REQUEST;
	}
	
	@RequestMapping(value = "/users/{uid}", method = GET)
	public String profile(Model model, @PathVariable("uid") long uid) {
		User user = userService.profile(uid);
		model.addAttribute(STATUS, STATUS_OK);
		model.addAttribute(USER, user);
		return BAD_REQUEST;
	}
	
	@RequestMapping(value = "/users/{uid}/modify", method = PUT)
	public String modify(
			Model model, HttpRequest request,
			@PathVariable("uid") long uid, User user) {
		userService.modify(user);
		model.addAttribute(STATUS, STATUS_OK);
		return BAD_REQUEST;
	}
	
	@RequestMapping(value = "/users", method = GET)
	public String list(
			Model model,
			@RequestParam("type") int type,
			@RequestParam("count") int count,
			@RequestParam(value = "last_id", defaultValue = "0") long lastId) {
		List<User> users = null;
		switch (type) {
		case LATEST:
			users = userService.latest(count);
			break;
		case LATEST_MORE:
			users = userService.list(lastId, count);
			break;
		case REFRESH:
			users = userService.list(lastId, count);
			break;
		default:
			throw new IllegalArgumentException("地不起，没有这个选项！");
		}
		model.addAttribute(USERS, users);
		return BAD_REQUEST;
	}
	
}