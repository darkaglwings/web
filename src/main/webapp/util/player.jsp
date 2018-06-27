<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script language="JavaScript" src="<path:path type='context'/>/js/util/player.js"></script>

<section>
	<h3 class="opendiv-tit">正在播放</h3>
	<a href="javascript: over();" class="divclosed" title="关闭"></a>
	<div align="center">
		<object id="mediaplayer" classid="clsid:22d6f312-b0f6-11d0-94ab-0080c74c7e95" codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#version=5,1,52,701" standby="loading microsoft windows media player components..." type="application/x-oleobject" width="320" height="310">
			<param name="filename" value="">
			<param name="animationatstart" value="true">
			<param name="transparentatstart" value="true">
			<param name="autostart" value="true">
			<param name="showcontrols" value="true">
			<param name="ShowStatusBar" value="true">
			<param name="windowlessvideo" value="true">
			<embed autostart="false" showcontrols="true" showstatusbar="1" bgcolor="white" width="320" height="310">
		</object>
	</div>
</section>