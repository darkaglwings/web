package org.frame.web.creator.jsp.impl;

import java.util.List;
import java.util.Map;

import org.frame.common.io.File;
import org.frame.common.lang.StringHelper;
import org.frame.repository.constant.IRepositoryConstant;
import org.frame.repository.database.IDatabase;
import org.frame.repository.sql.jdbc.IJDBC;
import org.frame.web.creator.jsp.IJsp;

public class Jsp implements IJsp {
	
	private IJDBC dao;
	
	private String output, schema;
	
	private boolean removePrefix;
	
	private List<Map<String, String>> data;
	
	public Jsp(IJDBC dao, String pack, String output, boolean removePrefix) {
		this.removePrefix = removePrefix;
		
		if (StringHelper.isNull(output)) output = "d:/";
		if (StringHelper.isNull(pack)) pack = "default";
		
		pack = pack.replace(".", "/");
		
		if (!output.endsWith("/")) output += "/";
		
		this.dao = dao;
		this.output = output;
		//this.pack = pack;
	}
	
	@SuppressWarnings("unchecked")
	public void create(String schema, List<String> required) {
		this.schema = schema;
		
		String sql = this.dao.getDatabase().fields();
		
		for (String table : required) {
			data = (List<Map<String, String>>) this.dao.select(sql, new Object[]{ this.schema, table });
			
			this.jsp(table);
			this.js(table);
		}
	}
	
	private void detail(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufResult = new StringBuffer();
		StringBuffer sbufHidden = new StringBuffer();
		StringBuffer sbufData = new StringBuffer();
		
		Map<String, String> map;
		String column, comment;
		String[] field;
		for (int i = 0; i < data.size(); i++) {
			map = (Map<String, String>) data.get(i);
			
			comment = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_COMMENT));
			if (StringHelper.isNull(comment)) {
				comment = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			}
			
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			field = this.adjust(column, false);
			
			if (IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_KEY).equals(map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_KEY)))) {
				sbufHidden.append("\t").append("\t").append("\t").append("\t").append("<input type=\"hidden\" id=\"").append(field[1]).append("\" name=\"").append(field[1]).append("\" value=\"<c:out value='${data.").append(field[1]).append("}'/>\"/>").append("\n");
			} else {
				if (i % 2 == 0) {
					sbufData.append("\t").append("\t").append("\t").append("\t").append("\t").append("<td class=\"item-name\">").append(comment).append(": </td>").append("\n");
					sbufData.append("\t").append("\t").append("\t").append("\t").append("\t").append("<td><input type=\"text\" id=\"").append(field[1]).append("\" name=\"").append(field[1]).append("\" value=\"<c:out value='${data.").append(field[1]).append("}'/>\" style=\"width:99%\" />").append("\n");
					sbufData.append("\t").append("\t").append("\t").append("\t").append("</tr>").append("\n");
				} else {
					sbufData.append("\t").append("\t").append("\t").append("\t").append("<tr>").append("\n");
					sbufData.append("\t").append("\t").append("\t").append("\t").append("\t").append("<td class=\"item-name\">").append(comment).append(": </td>").append("\n");
					sbufData.append("\t").append("\t").append("\t").append("\t").append("\t").append("<td><input type=\"text\" id=\"").append(field[1]).append("\" name=\"").append(field[1]).append("\" value=\"<c:out value='${data.").append(field[1]).append("}'/>\" style=\"width:99%\" />").append("\n");
				}
				
				if ((i == data.size() - 1) && sbufData.lastIndexOf("</tr>") != sbufData.length() - 6) {
					sbufData.append("\t").append("\t").append("\t").append("\t").append("</tr>").append("\n");
				}
			}
			
		}
		
		sbufResult.append("<%@ page language=\"java\" contentType=\"text/html; charset=utf-8\" pageEncoding=\"utf-8\"%>").append("\n");
		sbufResult.append("<!doctype html>").append("\n");
		sbufResult.append("<html>").append("\n");
		sbufResult.append("<head>").append("\n");
		sbufResult.append("<meta charset=\"utf-8\" />").append("\n");
		sbufResult.append("<title></title>").append("\n");
		sbufResult.append("</head>").append("\n");
		sbufResult.append("<body>").append("\n");
		sbufResult.append("<%@ include file=\"../frame/head.jsp\"%>").append("\n");
		sbufResult.append("<script type=\"text/javascript\" src=\"../js/").append(tableInfo[0].toLowerCase()).append("/detail.js\" ></script>").append("\n");
		sbufResult.append("<section>").append("\n");
		sbufResult.append("<div class=\"DivGlobal clearfix\">").append("\n");
		sbufResult.append("\t").append("<%@ include file=\"../frame/left.jsp\"%>").append("\n");
		sbufResult.append("\t").append("<div class=\"DivMainRight\">").append("\n");
		sbufResult.append("\t").append("<!--main area-->").append("\n");
		sbufResult.append("\t").append("<form id=\"detail\" action=\"modify.jspx\" method=\"post\">").append("\n");
		sbufResult.append("\t").append("<div class=\"Content nontit\">").append("\n");
		sbufResult.append("\t").append("\t").append("<div class=\"item-title\">").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span><a href=\"javascript:void(0);\" onclick=\"javascript: save('detail', true);\" class=\"itembtn10\">保存</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span><a href=\"javascript:void(0);\" onclick=\"javascript: cancel('detail');\" class=\"itembtn11\">重填</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span style=\"background:none;\"><a href=\"javascript:void(0);\" onclick=\"javascript: back('list.jspx');\" class=\"itembtn12\">返回</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("\t").append("<h3 class=\"mode-tit\">详情信息</h3>").append("\n");
		sbufResult.append("\t").append("\t").append("<div class=\"tab-area\">").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<div style=\"display:none\">").append("\n");
		sbufResult.append(sbufHidden);
		sbufResult.append("\t").append("\t").append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"tab-style2 tab-detail\">").append("\n");
		sbufResult.append(sbufData);
		sbufResult.append("\t").append("\t").append("\t").append("</table>").append("\n");
		sbufResult.append("\t").append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("</form>").append("\n");
		sbufResult.append("\t").append("<!--//main area-->").append("\n");
		sbufResult.append("\t").append("</div>").append("\n");
		sbufResult.append("</div>").append("\n");
		sbufResult.append("</section>").append("\n");
		sbufResult.append("<%@ include file=\"../frame/foot.jsp\"%>").append("\n");
		sbufResult.append("</body>").append("\n");
		sbufResult.append("</html>");
		
		new File(output + "jsp/" + tableInfo[0].toLowerCase() + "/detail.jsp").byte2file(sbufResult.toString().getBytes());
	}
	
	private void jsp(String table) {
		this.list(table);
		this.detail(table);
	}
	
	private void js(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufContent = new StringBuffer();
		sbufContent.append("function validate() {").append("\n");
		sbufContent.append("\t").append("\n");
		sbufContent.append("\t").append("return true;").append("\n");
		sbufContent.append("}").append("\n");
		
		new File(output + "js/" + tableInfo[0].toLowerCase() + "/detail.js").byte2file(sbufContent.toString().getBytes());
	}

	private void list(String table) {
		String[] tableInfo = this.adjust(table, this.removePrefix);
		
		StringBuffer sbufResult = new StringBuffer();
		StringBuffer sbufQuery = new StringBuffer();
		StringBuffer sbufTableHeader = new StringBuffer();
		StringBuffer sbufOdd = new StringBuffer();
		StringBuffer sbufEven = new StringBuffer();
		
		Map<String, String> map;
		String column, comment, parameter;
		String[] field;
		for (int i = 0; i < data.size(); i++) {
			map = (Map<String, String>) data.get(i);
			
			comment = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_COMMENT));
			if (StringHelper.isNull(comment)) {
				comment = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			}
			
			column = map.get(IDatabase.info.get(IRepositoryConstant.DATABASE_INFO_COLUMN_NAME));
			field = this.adjust(column, false);
			
			parameter = "query_" + field[1];
			
			sbufQuery.append("\t").append("\t").append("\t").append("<span>").append(comment).append(": ").append("<input type=\"text\" id=\"").append(parameter).append("\" name=\"").append(parameter).append("\" value=\"<c:out value='${").append(parameter).append("}'/>\"/></span>").append("\n");
			sbufTableHeader.append("\t").append("\t").append("\t").append("\t").append("\t").append("<th>").append(comment).append("</th>").append("\n");
			sbufEven.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<td><c:out value=\"${data.").append(field[1]).append("}\"/></td>").append("\n");
			sbufOdd.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<td class=\"tds01\"><c:out value=\"${data.").append(field[1]).append("}\"/></td>").append("\n");
		}
		
		sbufResult.append("<%@ page language=\"java\" contentType=\"text/html; charset=utf-8\" pageEncoding=\"utf-8\"%>").append("\n");
		sbufResult.append("<!doctype html>").append("\n");
		sbufResult.append("<html>").append("\n");
		sbufResult.append("<head>").append("\n");
		sbufResult.append("<meta charset=\"utf-8\" />").append("\n");
		sbufResult.append("<title></title>").append("\n");
		sbufResult.append("</head>").append("\n");
		sbufResult.append("<body>").append("\n");
		sbufResult.append("<%@ include file=\"../frame/head.jsp\"%>").append("\n");
		sbufResult.append("<section>").append("\n");
		sbufResult.append("<div class=\"DivGlobal clearfix\">").append("\n");
		sbufResult.append("\t").append("<%@ include file=\"../frame/left.jsp\"%>").append("\n");
		sbufResult.append("\t").append("<div class=\"DivMainRight\">").append("\n");
		sbufResult.append("\t").append("<!--main area-->").append("\n");
		sbufResult.append("\t").append("<form id=\"list\" action=\"list.jspx\" method=\"post\">").append("\n");
		sbufResult.append("\t").append("<input type=\"hidden\" id=\"ids\" name=\"ids\" value=\"\" />").append("\n");
		sbufResult.append("\t").append("<input type=\"hidden\" id=\"flag\" name=\"flag\" value=\"\" />").append("\n");
		sbufResult.append("\t").append("<div class=\"Content\">").append("\n");
		sbufResult.append("\t").append("\t").append("<div class=\"item-title\">").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span style=\"display:block\"><a href=\"javascript:void(0);\" onclick=\"javascript: create('list', 'detail.jspx');\" class=\"itembtn6\">新建</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span style=\"display:block\"><a href=\"javascript:void(0);\" onclick=\"javascript: edit('list', 'detail.jspx');\" class=\"itembtn4\">修改</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span style=\"display:block\"><a href=\"javascript:void(0);\" onclick=\"javascript: abandon('list', 'abandon.jspx', 0);\" class=\"itembtn3\">启用</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span style=\"display:block\"><a href=\"javascript:void(0);\" onclick=\"javascript: abandon('list', 'abandon.jspx', 1);\" class=\"itembtn29\">弃用</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span style=\"display:block\"><a href=\"javascript:void(0);\" onclick=\"javascript: trash('list', 'remove.jspx');\" class=\"itembtn7\">删除</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<span style=\"display:block\"><a href=\"javascript:void(0);\" onclick=\"javascript: refresh('list', 'list.jspx');\" class=\"itembtn8\">刷新</a></span>").append("\n");
		sbufResult.append("\t").append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("\t").append("<div class=\"item-title-sub\">").append("\n");
		sbufResult.append(sbufQuery);
		sbufResult.append("\t").append("\t").append("\t").append("<span><input type=\"button\" class=\"is-btn1\" value=\"检索\" onclick=\"javascript: query();\"/></span>").append("\n");
		sbufResult.append("\t").append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("\t").append("<h3 class=\"mode-tit\">列表信息</h3>").append("\n");
		sbufResult.append("\t").append("\t").append("<div class=\"tab-area\">").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"tab-style1\">").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("<tr>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("<th class=\"center\"><input type=\"checkbox\" onclick=\"javascript: all_checked(this.checked, 'id')\"/></th>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("<th>编号</th>").append("\n");
		sbufResult.append(sbufTableHeader.substring(0, sbufTableHeader.length() - 1));
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("</tr>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("<c:set var=\"i\" value=\"${0}\"/>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("<c:forEach items=\"${page.data}\" var=\"data\">").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("<c:choose>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("<c:when test=\"${i % 2 == 0}\">").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<tr>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<td class=\"center\"><input type=\"checkbox\" id=\"<c:out value='${data.id}'/>\" name=\"id\"/></td>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<td><c:out value=\"${i + 1}\"/></td>").append("\n");
		sbufResult.append(sbufEven);
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("</tr>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("</c:when>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("<c:otherwise>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<tr>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<td class=\"center tds01\"><input type=\"checkbox\" id=\"<c:out value='${data.id}'/>\" name=\"id\"/></td>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("<td class=\"tds01\"><c:out value=\"${i + 1}\"/></td>").append("\n");
		sbufResult.append(sbufOdd);
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("</tr>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("\t").append("</c:otherwise>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("</c:choose>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("<c:set var=\"i\" value=\"${i + 1}\"/>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("\t").append("</c:forEach>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("</table>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<!--page-->").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<page:pagination totalCountRequired=\"false\" pageInfoRequired=\"true\" dispersed=\"true\" dispreadLevel=\"5\" pageChooseRequired=\"false\" pageStyle=\"pagination\" currPageStyle=\"number  current\" nonCurrPageStyle=\"number\" emptyInfo=\"true\"/>").append("\n");
		sbufResult.append("\t").append("\t").append("\t").append("<!--//page-->").append("\n");
		sbufResult.append("\t").append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("</div>").append("\n");
		sbufResult.append("\t").append("</form>").append("\n");
		sbufResult.append("\t").append("<!--//main area-->").append("\n");
		sbufResult.append("\t").append("</div>").append("\n");
		sbufResult.append("</div>").append("\n");
		sbufResult.append("</section>").append("\n");
		sbufResult.append("<%@ include file=\"../frame/foot.jsp\"%>").append("\n");
		sbufResult.append("</body>").append("\n");
		sbufResult.append("</html>");
		
		new File(output + "jsp/" + tableInfo[0].toLowerCase() + "/list.jsp").byte2file(sbufResult.toString().getBytes());
	}
	
	private String[] adjust(String value, boolean removePrefix) {
		String[] result = new String[2];
		
		String word, temp = "";
		String[] words = value.split("_");
		for (int i = 0; i < words.length; i++) {
			if (removePrefix && i == 0) continue;
			word = words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length());
			temp += word;
		}
		
		result[0] = temp;
		
		temp = temp.substring(0, 1).toLowerCase() + temp.substring(1, temp.length());
		result[1] = temp;
		
		return result;
	}

}
