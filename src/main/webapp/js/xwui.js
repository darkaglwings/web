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
function iCard(id,action){
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
			tags[i].onmouseover = function active(currentId){
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

/*====================================================================================
input初始状态文字，用于输入提示
====================================================================================*/
function iVal(id,text,className){
try{
	var a = iVal.arguments.length;
	if( a>2 ){addClass($id(id),className);}
	$id(id).value=text;
	$id(id).onfocus=function(){
		if($id(id).value==text){
			$id(id).value="";
			if( a>2 ){removeClass($id(id),className);}
		}
	}
	$id(id).onblur=function(){
		if($id(id).value==""){
			$id(id).value=text;
			if( a>2 ){addClass($id(id),className);}
		}
	}
}catch(e){}
}


/*====================================================================================
input初始状态class，用于密码输入提示
====================================================================================*/
function iClass(id,classname){
try{	
	addClass($id(id),classname);
	$id(id).onfocus=function(){
		removeClass($id(id),classname);
	}
	$id(id).onblur=function(){
		removeClass($id(id),classname);
		if($id(id).value==""){
			addClass($id(id),classname);
		}
	}
}catch(e){}
}


/*====================================================================================
DOM的innerHTML
====================================================================================*/
function iHtml(id,html){
try{
	$id(id).innerHTML=html;
	$id(id).onfocus=function(){
		if(this.innerHTML==html){
			this.innerHTML="";
		}
	}
	$id(id).onblur=function(){
		if(this.innerHTML==""){
			this.innerHTML=html;
		}
	}
}catch(e){}
}


function iCloseDiv(id){
try{
	document.getElementById(id).style.display = "none";
	document.getElementsByTagName('html')[0].style.overflow = '';
}catch(e){}
}


/*====================================================================================
固定在某个位置的浮动层，可随屏幕滚动
====================================================================================*/
function iSmallAD(id,float,marginside,marginvalign,htmlcode){
try{
	this.id = id;
	this.float = "center"; //left/right/center
	this.valign = "middle"; //top/bottom/middle
	this.marginside = 0;
	this.marginvalign = 0;
	this.htmlcode = '';
	this.creat = function(){
		var marginbottom = this.marginbottom;
		var marginside = this.marginside;
		var marginvalign = this.marginvalign;
		var htmlcode = this.htmlcode;
		
		if( !hasId(this.id) ){
			var mydiv = document.createElement("div");
			document.body.appendChild(mydiv);
			mydiv.id = this.id;
			addClass($id(this.id),'icreatSmallAD');
		}
		var obj = $id(this.id);
		var clientWidth = document.body.clientWidth; //可见区域宽度
		var clientHeight = document.documentElement.clientHeight; //可见区域高度
		var scrollTop = document.documentElement.scrollTop+document.body.scrollTop; //上部被卷去的高
		var scrollHeight = document.body.scrollHeight; //正文高度
		
		//初始化部分属性
		obj.style.position = "absolute";
		obj.style.zIndex = "999";
		var v_popwin_height = obj.offsetHeight;
		var v_popwin_width = obj.offsetWidth;
		var defaultTop = 0;
		if( htmlcode.length>0 || htmlcode!='' ){
			obj.innerHTML = htmlcode;
		}
		
		//水平位置
		if(this.float=="left"){obj.style.left = marginside + "px";}
		if(this.float=="right"){obj.style.right = marginside + "px";}
		if(this.float=="center"){
			obj.style.left = Math.floor((clientWidth - v_popwin_width)/2) + "px";	
		}
		//垂直位置
		if(this.valign=="top"){
			defaultTop = marginvalign;
			obj.style.top = defaultTop + "px";
		}
		if(this.valign=="bottom"){
			defaultTop = clientHeight - v_popwin_height - marginvalign + scrollTop;
			obj.style.top = defaultTop + "px";
		}
		if(this.valign=="middle"){
			defaultTop = Math.floor((clientHeight-v_popwin_height)/2);
			obj.style.top = defaultTop + "px";			
		}
		
		vvstep = 10;
		function move(){
			currentTop = parseInt(obj.style.top.replace('/px/i',''));
			scrollTop = document.documentElement.scrollTop+document.body.scrollTop; //上部被卷去的高，Chrome某些情况会出错，必须这样才可以兼容所有浏览器
			targetTop = defaultTop+scrollTop;
			distance = Math.abs(currentTop-targetTop);
			var totalHeight = Math.max(clientHeight,scrollHeight);
			
			if( targetTop < currentTop ){ step = -vvstep }else{ step=vvstep } 
			if( distance >= vvstep  ){
				obj.style.top = currentTop + step + 'px';
			}else if( distance != 0 ){
				if( targetTop < currentTop ){
					obj.style.top = currentTop - 1 + 'px';
				}
				if( targetTop > currentTop ){
					obj.style.top = currentTop + 1 + 'px';
				}
			}
		}
		var outcall = setInterval(function(){move()},10);
	}
	this.hidden = function(){$id(this.id).style.display = "none"};
	this.remove = function(){var node = $id(this.id);if (node) {node.parentNode.removeChild(node);}};
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
			slidint = setTimeout(playnext,timeDelay*1000);
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
展开闭合
====================================================================================*/
function oprdiv(){
	
	if (document.getElementById("mybtn").className == "myloved")
	{
		document.getElementById("lovebox").style.display="none";
		document.getElementById("mybtn").className="myloved2"
	}
	else
		{
		document.getElementById("lovebox").style.display="block";
		document.getElementById("mybtn").className="myloved"
	}
}






















