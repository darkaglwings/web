package org.frame.web.controller.authority;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.frame.common.constant.ISymbolConstant;
import org.frame.web.controller.BaseController;
import org.frame.web.model.authority.Authorization;
import org.frame.web.service.authority.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AuthorizationController extends BaseController {
	
	@Resource
	@Autowired
	private IAuthorizationService authorizationService;
	
	@RequestMapping(value = "/system/authorization/list.jspx")
	public String list(HttpServletRequest request, Model model) {
		try {
			model.addAttribute("authorization", authorizationService.query());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/system/authority/authorize.jsp";
	}
	
	@RequestMapping(value = "/system/authorization/modify.jspx")
	public String modify(HttpServletRequest request, Model model) {
		try {
			Authorization authorization = (Authorization) super.request2bean(request, Authorization.class);
			model.addAttribute(ISymbolConstant.INFO_CODE, authorizationService.modify(authorization));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/system/authorization/list.jspx";
	}
	
	@RequestMapping(value = "/system/authorization/remove.jspx")
	public String remove(HttpServletRequest request, Model model) {
		try {
			model.addAttribute(ISymbolConstant.INFO_CODE, authorizationService.remove());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "/system/authorization/list.jspx";
	}
	
}
