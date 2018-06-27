package org.frame.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frame.common.util.Properties;
import org.frame.repository.sql.model.Page;
import org.frame.web.constant.IWebConstant;
import org.springframework.web.context.WebApplicationContext;


public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet{

	private static final long serialVersionUID = -7351087566526858231L;

	public DispatcherServlet() {
		super();
	}

	public DispatcherServlet(WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
	}

	@Override
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Page page = (Page) request.getSession().getAttribute(IWebConstant.PAGE_CODE);
		
		String target = request.getParameter(IWebConstant.PAGE_TARGET);
		String size = request.getParameter(IWebConstant.PAGE_SIZE);

		if (page != null && target != null && !"".equals(target) && !"null".equals(target)) {
			int targetPage, pageSize;
			
			try {
				targetPage = Integer.parseInt(target);
			} catch (NumberFormatException e) {
				targetPage = 1;
				throw new Exception("target page can not convet to number.\n target page is: " + target);
			}
			
			try {
				pageSize = Integer.parseInt(size);
			} catch (NumberFormatException e) {
				pageSize = Integer.parseInt(new Properties(IWebConstant.DEFAULT_CONFIG_PROPERTIES).read(IWebConstant.PAGE_SIZE));
				throw new Exception("target page can not convet to number.\n target page is: " + target);
			}
			
			page.setTargetPage(targetPage);
			page.setPageSize(pageSize);
			request.setAttribute(IWebConstant.PAGE_CODE, page);
			request.getSession().removeAttribute(IWebConstant.PAGE_CODE);
		}

		super.doDispatch(request, response);
	}

}
