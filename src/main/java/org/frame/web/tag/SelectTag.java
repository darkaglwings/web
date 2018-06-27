package org.frame.web.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class SelectTag extends TagSupport {

	private static final long serialVersionUID = 7463329682298134113L;
	
	private String clazz;
	
	private String code;
	
	private String data;
	
	private String id;
	
	private String name;
	
	private String onChange;
	
	private String readOnly;
	
	private String required;
	
	private String selected;
	
	private String style;
	
	private String top;
	
	private String value;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public String getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		List<Object> lstData = (List<Object>) request.getAttribute(getData());
		
		StringBuffer sbufSelect = new StringBuffer("<select");
		
		if (getId() != null && !"".equals(getId()) && !"null".equals(getId())) {
			sbufSelect.append(" id='").append(getId()).append("'");
		}
		
		if (getName() != null && !"".equals(getName()) && !"null".equals(getName())) {
			sbufSelect.append(" name='").append(getName()).append("'");
		}
		
		if (getClazz() != null && !"".equals(getClazz()) && !"null".equals(getClazz())) {
			sbufSelect.append(" class='").append(getClazz()).append("'");
		}
		
		if (getStyle() != null && !"".equals(getStyle()) && !"null".equals(getStyle())) {
			sbufSelect.append(" style=\"").append(getStyle()).append("\"");
		}
		
		if (getRequired() != null && !"true".equals(getRequired())) {
			sbufSelect.append(" required");
		}
		
		if (getReadOnly() != null && "true".equals(getReadOnly()) && getSelected() != null && !"".equals(getSelected())) {
			sbufSelect.append(" onchange=\"reset(").append(getSelected()).append(", this);");
			
			if (getOnChange() != null && !"".equals(getOnChange())) {
				sbufSelect.append(getOnChange());
			}
			
			sbufSelect.append(";\"");
		} else {
			if (getOnChange() != null && !"".equals(getOnChange())) {
				sbufSelect.append(" onchange=\"").append(getOnChange()).append(";\"");
			}
		}
		
		sbufSelect.append(">");
		
		if (getTop() != null && !"".equals(getTop()) && !"null".equals(getTop())) {
			sbufSelect.append("<option value='-1'>").append(getTop()).append("</option>");
		}
		
		String strCode;
		String strIsSelected = "";
		
		if (lstData != null && getCode() != null && !"".equals(getCode()) && getValue() != null && !"".equals(getValue())) {
			for (Object object : lstData) {
				strCode = this.invoke(object, getCode());
				if (getSelected() != null && !"".equals(getSelected())) {
					if (getSelected().equals(strCode)) {
						strIsSelected = "selected='true'";
					} else {
						strIsSelected = "";
					}
				}
				
				sbufSelect.append("<option value='").append(strCode).append("' ").append(strIsSelected).append(">");
				sbufSelect.append(this.invoke(object, getValue()));
				sbufSelect.append("</option>");
			}
		}
		
		sbufSelect.append("</select>");
		
		try {
			pageContext.getOut().print(sbufSelect.toString());
		} catch (IOException e) {
			throw new JspTagException("Tag: " + e.getMessage());
		}
		
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException {
		if ("true".equals(readOnly)) {
			StringBuffer sbufBody = new StringBuffer();
			sbufBody.append("<script type=\"text/javascript\">\n");
			sbufBody.append("function reset(val, ele) {\n");
			sbufBody.append("\t").append("var option = ele.options;\n");
			sbufBody.append("\t").append("for (var i = 0; i < option.length; i++) {\n");
			sbufBody.append("\t").append("\t").append("if (option[i].value == val) {\n");
			sbufBody.append("\t").append("\t").append("\t").append("option[i].selected = true;\n");
			sbufBody.append("\t").append("\t").append("} else {\n");
			sbufBody.append("\t").append("\t").append("\t").append("option[i].selected = false;\n");
			sbufBody.append("\t").append("\t").append("}\n");
			sbufBody.append("\t").append("}\n");
			sbufBody.append("}\n");
			sbufBody.append("</script>\n");

			try {
				pageContext.getOut().print(sbufBody.toString());
			} catch (IOException e) {
				throw new JspTagException("Tag: " + e.getMessage());
			}
		}

		return EVAL_PAGE;
	}

	private String invoke(Object object, String methodName) {
		String method = "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1, methodName.length());

		try {
			return String.valueOf(object.getClass().getMethod(method).invoke(object));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err.println("no such method: " + method);
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
