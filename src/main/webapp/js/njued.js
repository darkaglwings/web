/*====================================================================================
定义一些基本的方法，可略过
====================================================================================*/
$id = function(p){return document.getElementById(p);}

$idTag = function(p,tag){return document.getElementById(p).getElementsByTagName(tag);}

String.prototype.trim = function(){ return this.replace(/(^\s*)|(\s*$)/g, "");}

hasId = function(id){var s=document.getElementById(id);if(s){return true}else{return false}}

addClass = function(element, className){if (!element) return;var elementClassName = element.className.trim();if (elementClassName.length == 0) {element.className = className;return;}if (elementClassName == className || elementClassName.match(new RegExp("(^|\\s)" + className + "(\\s|$)"))) {return;}element.className = elementClassName + " " + className;}

removeClass = function(element, className) {if (!element) return;var elementClassName = element.className.trim();if (elementClassName.length == 0) return;if(elementClassName == className){element.className = "";return;}if (elementClassName.match(new RegExp("(^|\\s)" + className + "(\\s|$)")))element.className = elementClassName.replace((new RegExp("(^|\\s)" + className + "(\\s|$)"))," ");}

getElementsByClassName = function(id,className){var obj = $id(id);var ele=[];var tags = obj.getElementsByTagName('*');for(var i=0;i<tags.length;i++){var elementClassName = tags[i].className.trim();if( elementClassName.match(new RegExp("(^|\\s)" + className + "(\\s|$)")) ){ele[ele.length] = tags[i];}}return ele;}

addDOMLoadEvent=(function(){var load_events=[],load_timer,script,done,exec,old_onload,init=function(){done=true;clearInterval(load_timer);while(exec=load_events.shift())exec();if(script)script.onreadystatechange=''};return function(func){if(done)return func();if(!load_events[0]){if(document.addEventListener)document.addEventListener("DOMContentLoaded",init,false);/*@cc_on @*/ /*@if(@_win32)document.write("<script id=__ie_onload defer src=//0><\/script>");script=document.getElementById("__ie_onload");script.onreadystatechange=function(){if(this.readyState=="complete")init()};/*@end @*/if(/WebKit/i.test(navigator.userAgent)){load_timer=setInterval(function(){if(/loaded|complete/.test(document.readyState))init()},10)}old_onload=window.onload;window.onload=function(){init();if(old_onload)old_onload()}}load_events.push(func)}})();

/*====================================================================================
选项卡切换显示
action : ['click','over']
====================================================================================*/
function iTab(id,action){
try{
	var obj = $id(id);
	var tags = getElementsByClassName(id,'iTab_tag');
	var iTab_child_dom = getElementsByClassName(id,'iTab_child_dom');
	for( i=0;i<iTab_child_dom.length;i++){
		iTab_child_dom[i].id = id+'_iTab_child_dom_'+i;	
	}
	for( i=0;i<tags.length;i++){
		tags[i].id = id+"_iTab_tag_"+i;
		if( action == 'click' ){
			tags[i].onclick = function active(currentId){
				//for( i=0;i<tags.length;i++){
					//removeClass($id(id+"_iTab_tag_"+i),'current');
					//$id(id+'_iTab_child_dom_'+i).style.display = "none";
				//}
				currentId = this.id;
				//addClass($id(currentId),'current');
				currentIdNum  = currentId.match(/[^\d]*(\d+)$/)[1];
				currentId = id+'_iTab_child_dom_' + currentIdNum;			
				
				if($id(currentId).style.display == ""){
					removeClass($id(this.id),'current');
					$id(currentId).style.display = "none";
				}else{
					$id(currentId).style.display = "";
					addClass($id(this.id),'current');
				}
			}
		}
		if( action == 'over' ){
			tags[i].onmouseover = function(){
				for( i=0;i<tags.length;i++){
					removeClass($id(id+"_iTab_tag_"+i),'current');
					$id(id+'_iTab_child_dom_'+i).style.display = "none";
				}
				currentId = this.id;
				addClass($id(currentId),'current');
				currentIdNum  = currentId.match(/[^\d]*(\d+)$/)[1];
				currentId = id+'_iTab_child_dom_' + currentIdNum;
				$id(currentId).style.display = "";
			}
		}
	}
}catch(e){}
}

//首页轮播
function indexScroll(id,idtag,id2,id2tag,timeDelay){
	var timeDelay = timeDelay; //定义图片延迟显示的秒数
	var currentId = 0; 
	var slidint;
	var imageNum;

	//控制当前焦点按钮样式
	function setFocus(n){
		var imgList = document.getElementById(id).getElementsByTagName(idtag);	
		imageNum = imgList.length;
		if ( imageNum > 1 ){
			for ( i=0;i<imgList.length;i++ ){
				if ( i == n ){
					document.getElementById(id2).getElementsByTagName(id2tag)[i].className = "focus";
					document.getElementById(id).getElementsByTagName(idtag)[i].style.display = "block";
				}
				else{
					document.getElementById(id2).getElementsByTagName(id2tag)[i].className = "";
					document.getElementById(id).getElementsByTagName(idtag)[i].style.display = "none";
				}
			}
			//imgAlpha();
		}
	}

	//播放下一张
	function playnext(){
		if(currentId == imageNum-1){
			currentId = 0;
		}
		else{
			currentId++;
		};
		setFocus(currentId);
		playit();
	}
	function playit(){
		if( timeDelay>0 ){
			slidint = setTimeout(playnext,timeDelay*10000);
		}
	}
	function stopit(){
		clearTimeout(slidint);
	}
	//鼠标滑过及离开图片的操作
	function checkMouseOver(){
		document.getElementById(id).onmouseover = function(){
			stopit();
		}
		document.getElementById(id).onmouseout = function(){
			playit();
		}
		document.getElementById(id2).onmouseover = function(){
			stopit();
		}
		document.getElementById(id2).onmouseout = function(){
			playit();
		}
	}
	//点击标签按钮后的操作
	function checkClick(){
		var btnList = document.getElementById(id2).getElementsByTagName(id2tag);
		for (i=0;i<btnList.length;i++){	
			btnList[i].id = "tempBtn"+i;
			btnList[i].onmouseover = function(){
				//currentId = this.id.slice(-1);
				currentId  = this.id.match(/[^\d]*(\d+)$/)[1];
				setFocus(currentId);
				stopit();
				playit();
			}
		}
	}
	//function smothSwitchAd(){
	try{
		document.getElementById(id2).getElementsByTagName(id2tag)[0].className = "focus";
		checkMouseOver();
		checkClick();
		playit();
	}catch(e){}
	//}
	//smothSwitchAd();
}

/*====================================================================================
常见的弹出居中浮动层
并将背景锁定、灰显
====================================================================================*/
function iPopupWin(id,htmlcode){
try{
	
	//获取需要的高度宽度数据，备用
	var clientWidth = document.body.clientWidth; //可见区域宽度
	var clientHeight = document.body.clientHeight; //可见区域高度
	var scrollTop = document.documentElement.scrollTop; //上部被卷去的高
	
	//查找遮罩及主体内容div，赋予相应的id
	var mask = getElementsByClassName(id,'iPopupWin_mask')[0];
	mask.id = id+'_mask';
	maskid = mask.id;
	var content = getElementsByClassName(id,'iPopupWin_content')[0];
	content.id = id+'_content';
	contentid = content.id;
	
	//设置遮罩层大小，显示全部浮出层
	document.getElementById(maskid).style.height = clientHeight +"px";
	document.getElementById(id).style.display = "block";
	//document.getElementById(maskid).focus();
	
	//控制主体窗口位置
	var v_popwin = $id(contentid);
	if( htmlcode!=null && htmlcode.length != 0 ){
		v_popwin.innerHTML = htmlcode;
	}
	var v_popwin_height = $id(contentid).offsetHeight;
	var v_popwin_width = $id(contentid).offsetWidth;
	v_popwin.style.position = "absolute";
	v_popwin.style.left = "50%";
	v_popwin.style.top = "50%";
	v_popwin.style.zIndex = "9999";
	v_popwin.style.marginLeft = -(v_popwin_width/2) + "px";
	v_popwin.style.marginTop = -(v_popwin_height/2) + "px";
	if(testIE()=="6.0"){
		v_popwin.style.marginTop = -(v_popwin_height/2) + scrollTop + "px";	
		//IE6 select fix
		var myFrame = document.createElement("iframe");
		document.getElementById(id).appendChild(myFrame);
		myFrame.width = clientWidth;
		myFrame.height = clientHeight;
		myFrame.style.position = "absolute";
		myFrame.style.left = 0 + "px";
		myFrame.style.top = 0 + "px";
		myFrame.style.zIndex = "0";
		myFrame.allowtransparency = 'true';
		myFrame.style.filter = 'Alpha(Opacity=0)';
	}
	
	document.getElementsByTagName('html')[0].style.overflow = 'hidden';
	function keyUp(e){
		var currKey=0,e=e||event;
		currKey=e.keyCode||e.which||e.charCode;
		switch (currKey){
			case 27: iCloseDiv(id); break;
		} 
	}
	document.onkeyup = keyUp;
}catch(e){}
}
function iCloseDiv(id){
try{
	document.getElementById(id).style.display = "none";
	document.getElementsByTagName('html')[0].style.overflow = '';
}catch(e){}
}



















