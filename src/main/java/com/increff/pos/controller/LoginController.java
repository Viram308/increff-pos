package com.increff.pos.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.dto.UserDto;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.SecurityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Controller
@RequestMapping(value = "/session")
public class LoginController {

	@Autowired
	private UserDto userDto;
	@Autowired
	private InfoData info;

	@ApiOperation(value = "Logs in a user")
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, UserForm userForm)
			throws ApiException, NoSuchAlgorithmException, UnsupportedEncodingException {

		UserPojo userPojo = userDto.getByEmail(userForm.getEmail());
		if (userPojo == null) {
			info.setMessage("Invalid username or password");
			return new ModelAndView("redirect:/site/login");
		}
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(userForm.getPassword().getBytes("UTF-8"));
		String password = String.format("%032x", new BigInteger(1, md.digest()));
		boolean authenticated = (userPojo != null && Objects.equals(userPojo.getPassword(), password));

		if (!authenticated) {
			info.setMessage("Invalid username or password");
			return new ModelAndView("redirect:/site/login");
		}

		// Create authentication object
		Authentication authentication = userDto.convertUserPojotoAuthentication(userPojo);
		// Create new session
		HttpSession session = req.getSession(true);
		// Attach Spring SecurityContext to this new session
		SecurityUtil.createContext(session);
		// Attach Authentication object to the Security Context
		SecurityUtil.setAuthentication(authentication);

		return new ModelAndView("redirect:/ui/home");

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/logout");
	}

}
