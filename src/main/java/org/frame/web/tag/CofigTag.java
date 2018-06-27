package org.frame.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.frame.common.constant.ISymbolConstant;
import org.frame.web.profile.Config;

public class CofigTag extends TagSupport {

	private static final long serialVersionUID = -5391260909046142699L;

	protected Config config = new Config(this.getSource());
	
	private String source;
	
	private String info;
	
	private String key;
	
	private String script;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		request.setAttribute(info, config.get(key));
		
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException {
		if (ISymbolConstant.FLAG_TRUE.equals(this.getScript())) {
			StringBuffer sbufBody = new StringBuffer();
			sbufBody.append("<script type=\"text/javascript\">\n");
			sbufBody.append("var ").append(info).append(" = '").append(config.get(key)).append("';\n");
			sbufBody.append("</script>\n");

			try {
				pageContext.getOut().print(sbufBody.toString());
			} catch (IOException e) {
				throw new JspTagException("ConfigTag: " + e.getMessage());
			}
		}

		return EVAL_PAGE;
	}

}
