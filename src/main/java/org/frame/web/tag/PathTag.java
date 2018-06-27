package org.frame.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class PathTag extends TagSupport {

	private static final long serialVersionUID = 6210628690662417323L;

	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int doStartTag() throws JspException {
		String result;
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		if ("context".equals(this.getType())) {
			result = request.getContextPath();
		} else if ("url".equals(this.getType())) {
			result = request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		} else {
			result = "";
		}
		
		try {
			pageContext.getOut().print(result);
		} catch (IOException e) {
			throw new JspTagException("Tag: " + e.getMessage());
		}
		
		return SKIP_BODY;
	}
	
}
