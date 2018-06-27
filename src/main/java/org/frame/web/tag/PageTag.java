package org.frame.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.frame.common.constant.ISymbolConstant;
import org.frame.common.util.Properties;
import org.frame.repository.sql.model.Page;
import org.frame.web.constant.IWebConstant;


public class PageTag extends TagSupport {

	private static final long serialVersionUID = 7598074948551623670L;

	private int dispread = 5;

	private String dispreadLevel;
	
	private String pageSize;

	private String formName;

	private String totalCountRequired;

	private String pageInfoRequired;

	private String pageChooseRequired;

	private String dispersed;
	
	private String emptyInfo;

	private String pageStyle;

	private String totalCountStyle;

	private String pageInfoStyle;

	private String linkStyle;

	private String chooseStyle;

	private String currPageStyle;
	
	private String nonCurrPageStyle;

	/*public int getDispread() {
		return dispread;
	}

	public void setDispread(int dispread) {
		this.dispread = dispread;
	}*/

	public String getDispreadLevel() {
		return dispreadLevel;
	}

	public void setDispreadLevel(String dispreadLevel) {
		int nDispread = 5;
		String defDispread = new Properties(IWebConstant.DEFAULT_CONFIG_PROPERTIES).read(IWebConstant.PAGE_DISPREAD).toLowerCase();
		if (defDispread != null && !"".equals(defDispread)) {
			try {
				nDispread = Integer.parseInt(defDispread);
			} catch (NumberFormatException e) {
				nDispread = 5;
				e.printStackTrace();
			} catch (Exception e) {
				nDispread = 5;
				e.printStackTrace();
			}
		}
		
		int settedDispread = -1;
		if (dispreadLevel != null && !"".equals(dispreadLevel)) {
			try {
				settedDispread = Integer.parseInt(dispreadLevel);
			} catch (NumberFormatException e) {
				settedDispread = -1;
				e.printStackTrace();
			} catch (Exception e) {
				settedDispread = -1;
				e.printStackTrace();
			}
		}
		
		if (settedDispread == -1) {
			this.dispread = nDispread;
		} else {
			this.dispread = settedDispread;
		}
		
		this.dispreadLevel = dispreadLevel;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getTotalCountRequired() {
		return totalCountRequired;
	}

	public void setTotalCountRequired(String totalCountRequired) {
		this.totalCountRequired = totalCountRequired;
	}

	public String getPageInfoRequired() {
		return pageInfoRequired;
	}

	public void setPageInfoRequired(String pageInfoRequired) {
		this.pageInfoRequired = pageInfoRequired;
	}

	public String getPageChooseRequired() {
		return pageChooseRequired;
	}

	public void setPageChooseRequired(String pageChooseRequired) {
		this.pageChooseRequired = pageChooseRequired;
	}

	public String getDispersed() {
		return dispersed;
	}

	public void setDispersed(String dispersed) {
		this.dispersed = dispersed;
	}

	public String getEmptyInfo() {
		return emptyInfo;
	}

	public void setEmptyInfo(String emptyInfo) {
		this.emptyInfo = emptyInfo;
	}

	public String getPageStyle() {
		if (pageStyle == null || "null".equals(pageStyle)) 
			pageStyle = "";
		return pageStyle;
	}

	public void setPageStyle(String pageStyle) {
		this.pageStyle = pageStyle;
	}

	public String getTotalCountStyle() {
		if (totalCountStyle == null || "null".equals(totalCountStyle)) 
			totalCountStyle = "";
		return totalCountStyle;
	}

	public void setTotalCountStyle(String totalCountStyle) {
		this.totalCountStyle = totalCountStyle;
	}

	public String getPageInfoStyle() {
		if (pageInfoStyle == null || "null".equals(pageInfoStyle)) 
			pageInfoStyle = "";
		return pageInfoStyle;
	}

	public void setPageInfoStyle(String pageInfoStyle) {
		this.pageInfoStyle = pageInfoStyle;
	}

	public String getLinkStyle() {
		if (linkStyle == null || "null".equals(linkStyle)) 
			linkStyle = "";
		return linkStyle;
	}

	public void setLinkStyle(String linkStyle) {
		this.linkStyle = linkStyle;
	}

	public String getChooseStyle() {
		if (chooseStyle == null || "null".equals(chooseStyle)) 
			chooseStyle = "";
		return chooseStyle;
	}

	public void setChooseStyle(String chooseStyle) {
		this.chooseStyle = chooseStyle;
	}

	public String getCurrPageStyle() {
		if (currPageStyle == null || "null".equals(currPageStyle)) 
			currPageStyle = "";
		return currPageStyle;
	}

	public void setCurrPageStyle(String currPageStyle) {
		this.currPageStyle = currPageStyle;
	}
	
	public String getNonCurrPageStyle() {
		if (nonCurrPageStyle == null || "null".equals(nonCurrPageStyle)) 
			nonCurrPageStyle = "";
		return nonCurrPageStyle;
	}

	public void setNonCurrPageStyle(String nonCurrPageStyle) {
		this.nonCurrPageStyle = nonCurrPageStyle;
	}

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Page page = (Page) request.getAttribute(IWebConstant.PAGE_CODE);
		request.getSession().setAttribute(IWebConstant.PAGE_CODE, page);

		StringBuffer sbufPage = new StringBuffer("");
		
		if (page != null) {
			sbufPage.append("<div class='").append(this.getPageStyle()).append("'>");
			
			if (page.hasData()) {
				if (!ISymbolConstant.FLAG_FALSE.equals(this.getTotalCountRequired())) {
					sbufPage.append("<span");
					if (!"".equals(this.getTotalCountStyle())) {
						sbufPage.append(" class='").append(this.getTotalCountStyle()).append("'");
					}
					sbufPage.append(">");
					sbufPage.append("总记录数：");
					sbufPage.append("<em>").append(page.getTotalCount()).append("</em>");
					sbufPage.append("条");
					sbufPage.append("</span>");
					
					sbufPage.append("&nbsp;&nbsp;");
				}

				if (!ISymbolConstant.FLAG_FALSE.equals(this.getPageInfoRequired())) {
					sbufPage.append("<span");
					if (!"".equals(this.getPageInfoStyle())) {
						sbufPage.append(" class='").append(this.getPageInfoStyle()).append("'");
					}
					sbufPage.append(">");
					sbufPage.append("当前第");
					sbufPage.append("<em>").append(page.getCurrPage()).append("</em>");
					sbufPage.append("页");

					sbufPage.append("/");

					sbufPage.append("共");
					sbufPage.append("<em>").append(page.getTotalPage()).append("</em>");
					sbufPage.append("页");
					sbufPage.append("</span>");
					
					sbufPage.append("&nbsp;&nbsp;");
				}

				sbufPage.append("<span class='").append(this.getLinkStyle()).append("'>");

				if (ISymbolConstant.FLAG_TRUE.equals(this.getDispersed())) {
					int[] pageInfo = this.analysePage(page);
					String css = "";

					sbufPage.append("<a href='javascript:pagination(").append(page.getCurrPage() - 1 < 1 ? 1 : page.getCurrPage() - 1).append(")'>");
					sbufPage.append("&laquo; 上一页");
					sbufPage.append("</a>");

					sbufPage.append("&nbsp;");

					for (int i = 0; i < pageInfo.length; i++) {
						if (pageInfo[i] == page.getCurrPage()) {
							css = this.getCurrPageStyle();
						} else {
							css = this.getNonCurrPageStyle();
						}

						sbufPage.append("<a href='javascript:pagination(").append(pageInfo[i]).append(")' class='").append(css).append("'>");
						sbufPage.append(pageInfo[i]);
						sbufPage.append("</a>");
					}

					sbufPage.append("&nbsp;");

					sbufPage.append("<a href='javascript:pagination(").append((page.getCurrPage() + 1 > page.getTotalPage() ? page.getTotalPage() : page.getCurrPage() + 1)).append(")'>");
					sbufPage.append("下一页 &raquo;");
					sbufPage.append("</a>");
				} else {
					if (page.hasPreviousPage()) {
						sbufPage.append("<a href='javascript:pagination(1)'>");
						sbufPage.append("最前页");
						sbufPage.append("</a>");

						sbufPage.append("&nbsp;&nbsp;");

						sbufPage.append("<a href='javascript:pagination(").append(page.getCurrPage() - 1 < 1 ? 1 : page.getCurrPage() - 1).append(")'>");
						sbufPage.append("上一页");
						sbufPage.append("</a>");
					}
					if (page.hasNextPage()) {
						sbufPage.append("&nbsp;&nbsp;");

						sbufPage.append("<a href='javascript:pagination(").append((page.getCurrPage() + 1 > page.getTotalPage() ? page.getTotalPage() : page.getCurrPage() + 1)).append(")'>");
						sbufPage.append("下一页");
						sbufPage.append("</a>");

						sbufPage.append("&nbsp;&nbsp;");

						sbufPage.append("<a href='javascript:pagination(").append(page.getTotalPage()).append(")'>");
						sbufPage.append("最后页");
						sbufPage.append("</a>");
					}
				}

				sbufPage.append("</span>");

				if (!ISymbolConstant.FLAG_FALSE.equals(this.getPageChooseRequired())) {
					sbufPage.append("&nbsp;&nbsp;");

					sbufPage.append("<select name='choosePage' onChange='javascript:pagination(this.value)' class='").append(this.getChooseStyle()).append("'>\n");

					String selected = "";
					for (int i = 1; i <= page.getTotalPage(); i++) {
						if (i == page.getCurrPage())
							selected = "selected";
						else
							selected = "";

						sbufPage.append("<option value='").append(i).append("'").append(selected).append(">").append(i).append("/").append(page.getTotalPage()).append("</option>");
					}
					if (page.getCurrPage() > page.getTotalPage()) {
						sbufPage.append("<option value='").append(page.getCurrPage()).append("' selected>").append(page.getCurrPage()).append("</option>");
					}
					sbufPage.append("</select>");
				}
			} else {
				if ("true".equals(this.getEmptyInfo())) sbufPage.append("暂未查询到相关数据...");
			}

			sbufPage.append("<input type='hidden' id='targetPage' name='targetPage' value=''/>\n");
			
			sbufPage.append("</div>");
			
			try {
				pageContext.getOut().print(sbufPage.toString());
			} catch (IOException e) {
				throw new JspTagException("Tag: " + e.getMessage());
			}
		} else {
			System.err.println("page not found.");
		}

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		StringBuffer sbufBody = new StringBuffer();
		sbufBody.append("<script type=\"text/javascript\">\n");
		sbufBody.append("function pagination(target) {\n");
		sbufBody.append("document.getElementById('").append(IWebConstant.PAGE_TARGET).append("').value=target; \n");
		if(getFormName() != null && !"".equals(getFormName())) {
			sbufBody.append("document.").append(getFormName()).append(".submit();\n");
		} else {
			sbufBody.append("document.forms[0].submit();\n");
		}
		sbufBody.append("}\n");
		sbufBody.append("</script>\n");

		try {
			pageContext.getOut().print(sbufBody.toString());
		} catch (IOException e) {
			throw new JspTagException("Tag: " + e.getMessage());
		}

		return EVAL_PAGE;
	}

	private int[] analysePage(Page page) {
		int[] result = new int[dispread];
		int currPage = page.getCurrPage();
		int totalPage = page.getTotalPage();

		if (totalPage > dispread) {
			if (currPage > dispread) {
				int middle = dispread / 2;

				if (currPage + dispread - middle -1 > totalPage) {
					for (int i = dispread; i > 0; i--) {
						result[i - 1] = totalPage - dispread + i;
					}
				} else {
					for (int i = dispread - 1; i >= 0; i--) {
						result[i] = currPage - middle + i;
					}
				}
			} else {
				for (int i = 0; i < dispread; i++) {
					result[i] = i + 1;
				}
			}
		} else {
			result = new int[totalPage];
			for (int i = 0; i < totalPage; i++) {
				result[i] = i + 1;
			}
		}

		return result;
	}

	/*public static void main(String[] args) {
		PageTag pageTag = new PageTag();
		Page page = new Page();
		page.setCurrPage(1);
		page.setTotalPage(2);
		int[] result = pageTag.analysePage(page);
		for(int i = 0; i < result.length; i++) {
			System.out.println(result[i]);
		}
	}*/

}