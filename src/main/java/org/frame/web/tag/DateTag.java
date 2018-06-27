package org.frame.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.frame.common.util.Date;

public class DateTag extends TagSupport {

	private static final long serialVersionUID = 3759240066711440899L;

	private String date;
	
	private String local;
	
	private String time;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int doStartTag() throws JspException {
		String pattern, result;
		
		if ("true".equals(this.getLocal())) {
			if ("true".equals(this.getDate()) && "true".equals(this.getTime())) {
				pattern = "yyyy年MM月dd日 HH时mm分ss秒";
			} else if ("true".equals(this.getDate()) && !"true".equals(this.getTime())) {
				pattern = "yyyy年MM月dd日";
			} else if (!"true".equals(this.getDate()) && "true".equals(this.getTime())) {
				pattern = "HH时mm分ss秒";
			} else if (!"true".equals(this.getDate()) && !"true".equals(this.getTime())) {
				pattern = "";
			} else {
				pattern = "yyyy年MM月dd日 HH时mm分ss秒";
			}
		} else {
			if ("true".equals(this.getDate()) && "true".equals(this.getTime())) {
				pattern = "yyyy-MM-dd HH:mm:ss";
			} else if ("true".equals(this.getDate()) && !"true".equals(this.getTime())) {
				pattern = "yyyy-MM-dd";
			} else if (!"true".equals(this.getDate()) && "true".equals(this.getTime())) {
				pattern = "HH:mm:ss";
			} else if (!"true".equals(this.getDate()) && !"true".equals(this.getTime())) {
				pattern = "";
			} else {
				pattern = "yyyy-MM-dd HH:mm:ss";
			}
		}
		
		if ("".equals(pattern))
			result = "";
		else
			result = new Date().date2String(pattern);
		
		try {
			pageContext.getOut().print(result);
		} catch (IOException e) {
			throw new JspTagException("Tag: " + e.getMessage());
		}
		
		return SKIP_BODY;
	}
	
}
