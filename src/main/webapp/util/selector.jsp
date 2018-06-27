<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<h3 id="tit_selector" class="opendiv-tit"></h3>
<span href="#" class="divclosed" style="cursor: pointer" onclick="iCloseDiv()"></span>
<div class="tab-area3">
	<table cellspacing="0" cellpadding="0" class="tab-style2">
		<tr>
			<td valign="top">
				<fieldset class="sys-set2">
					<legend>所 有</legend>
					<p><select name="left" id="left" multiple="multiple" style="height: 100%; width: 100%" /></p>
				</fieldset>
			</td>
			<td width="20%">
				<p class="center"><input type="button" class="cbtn1" onclick="javascript: move_all('left', 'right')" value="&gt;&gt;" /></p>
				</br>
				<p class="center"><input type="button" class="cbtn1" onclick="javascript: move('left', 'right')" value="&gt;" /></p>
				</br>
				<p class="center"><input type="button" class="cbtn1" onclick="javascript: move('right', 'left')" value="&lt;" /></p>
				</br>
				<p class="center"><input type="button" class="cbtn1" onclick="javascript: move_all('right', 'left')" value="&lt;&lt;" /></p>
			</td>
			<td valign="top">
				<fieldset class="sys-set2">
					<legend>已 选</legend>
					<p><select name="right" id="right" multiple="multiple" style="height: 100%; width: 100%" /></p>
				</fieldset>
			</td>
		</tr>
	</table>
	<p class="center margin10">
		<input type="button" class="savebtn" value="确定" onclick="javascript: iSaveDiv();" />
		<input type="button" class="canbtn" value="取消" onClick="javascript: iCloseDiv();" />
	</p>
</div>
