<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<section>
	<h3 class="opendiv-tit">请选择</h3>
	<span href="#" class="divclosed" style="cursor: pointer" onclick="iCloseDiv()"></span>
	<div id="wrap" class="tree_wrap">
		<div><ul id="tree" class="ztree"></ul></div>
	</div>
	<div class="bar-horizontal">
		<span class="current" onclick="javascript: iQuery(this);"></span>
	</div>
	<div id="query" class="query-area" style="display: none">
		<span>查询内容：<input type="text" id="parameter" /></span>
		<span><input type="button" value="查询" onClick="javascript: highlight();" /></span>
		<span><input type="button" value="重置" onClick="javascript: unhighlight();" /></span>
	</div>
	<div align="center">
		<input type="button" class="savebtn" value="确定" onclick="javascript: iSave();" />
		<input type="button" class="savebtn" value="置空" onClick="javascript: iNull();" />
		<input type="button" class="canbtn" value="取消" onClick="javascript: iClose();" />
	</div>
</section>