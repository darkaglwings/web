Array.prototype.unique = function() { 
    var data = this || []; 
    var a = {}; //声明一个对象，javascript的对象可以当哈希表用 
    for (var i = 0; i < data.length; i++) { 
        a[data] = true; //设置标记，把数组的值当下标，这样就可以去掉重复的值 
    } 
    data.length = 0;  
      
    for (var i in a) { //遍历对象，把已标记的还原成数组 
        this[data.length] = i;  
    }  
    return data; 
};

String.prototype.initialsToUpper = function() { 
    return this.substring(0,1).toUpperCase().concat(this.substring(1)); 
}; 

String.prototype.contains = function(str) { 
    return this.indexOf(str) > -1 ? true : false; 
}; 

String.prototype.trim = function() { 
    return this.replace(/(^\s*)|(\s*$ )/g, ""); 
}; 
    
String.prototype.lTrim = function() { 
    return this.replace(/(^\s*)/g, ""); 
}; 
    
String.prototype.rTrim = function() {  
    return this.replace(/(\s*$ )/g, ""); 
};

/**
 * make all checks named name is checked or not checked
 * 
 * @param isChecked checked or not
 * @param name name of checks
 * 
 * @returns void
 */
function all_checked(isChecked, name) {
	var checks = document.getElementsByName(name);
	for (var i = 0; i < checks.length; i++) {
		checks[i].checked = isChecked;
	}
}

/**
 * attach a function to an event of a element
 *          
 * @param target element will be attached
 * @param eventName event name
 * @param handler function name
 * @param argsObject function args
 * 
 * @returns void
 */
function addEvent(target, eventName, handler, argsObject) {
	var eventHandler = handler;
	if(argsObject) {
		eventHandler = function(e) {handler.call(argsObject, e);};
	}

	if(window.attachEvent)
		target.attachEvent("on" + eventName, eventHandler);
	else
		target.addEventListener(eventName, eventHandler, false);
}

/**
 * to get checks' id
 * 
 * @param identify checks' name
 * @param onlyOne true only check one
 *                flase could check more
 * 
 * @returns ids(separated by ",")
 */
function id_picker(identify, onlyOne) {
	var id = "";
	var checked = false;
	var check = document.getElementsByName(identify);
	for (var i = 0; i < check.length; i++) {
		if (check[i].checked == true) {
			if (onlyOne) {
				if (checked) {
					alert("只能选择一条记录进行编辑");
					return undefined;
				}else{
					checked = true;
				}
			}

			if (id == "") {
				id += check[i].id;
			}else{
				id += "," + check[i].id;
			}
		}
	}

	if (id == "") {
		alert("请选择一条记录进行操作");
		return undefined;
	}

	return id;
}

/**
 * run an exe form local machine
 *     
 * @param path exe path
 * 
 * @returns void
 */
function run(path) {
	var result = false;
	try {
		var shell = new ActiveXObject("wscript.shell");
		shell.Run(path);
		shell = null;
		
		result = true;
	} catch(e) {
		alert('找不到文件"' + path + '"(或它的组件之一)。请确定路径和文件名是否正确.');
	}
	
	return result;
}

/**
 * get navigator information
 *
 * @returns if navigator is ie then returns version of ie else returns undefined
 */
function testIE() {
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	var s;
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
	(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
	(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
	(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	return Sys.ie;
}

/**
 * make a string of today's date
 *
 * @param pattern yyyy-MM-dd HH:mm:ss or yyyy年MM月dd日 HH时mm分ss秒 or sub
 * 
 * @returns today's date(default is yyyy-MM-dd HH:mm:ss)
 */
function today_picker(pattern) {
	var year, month, day;
	var intYear, intMonth, intDay;
	var hours, minutes, seconds;
	var intHours, intMinutes, intSeconds;
	var today;

	today = new Date();

	intYear = today.getFullYear();
	intMonth = today.getMonth() + 1;
	intDay = today.getDate();
	intHours = today.getHours();
	intMinutes = today.getMinutes();
	intSeconds = today.getSeconds();

	year = intYear + "";

	if (intMonth < 10) {
		month = "0" + intMonth;
	}else{
		month = intMonth + "";  
	}

	if (intDay < 10) {
		day = "0" + intDay;
	}else{
		day = intDay + "";
	}

	if (intHours == 0) {
		hours = "00";
	} else if (intHours < 10) { 
		hours = "0" + intHours;
	} else {
		hours = intHours + "";
	}

	if (intMinutes < 10) {
		minutes = "0" + intMinutes;
	} else {
		minutes = intMinutes + "";
	}

	if (intSeconds < 10) {
		seconds = "0" + intSeconds;
	} else {
		seconds = intSeconds + "";
	} 

	if (pattern) {
		if (pattern == "yy") {
			timeString = year.substring(2, 2);
		} else if (pattern == "yy年") {
			timeString = year.substring(2, 2) + "年";
		} else if (pattern == "yyyy") {
			timeString = year;
		} else if (pattern == "yyyy年") {
			timeString = year + "年";
		} else if (pattern == "MM") {
			timeString = month;
		} else if (pattern == "MM月") {
			timeString = month + "月";
		} else if (pattern == "yy-MM") {
			timeString = year.substring(2, 2) + "-" + month;
		} else if (pattern == "yy年MM月") {
			timeString = year.substring(2, 2) + "年" + month + "月";
		} else if (pattern == "yyyy-MM") {
			timeString = year + "-" + month;
		} else if (pattern == "yyyy年MM月") {
			timeString = year + "年" + month + "月";
		} else if (pattern == "dd") {
			timeString = day;
		} else if (pattern == "dd日") {
			timeString = day + "日";
		} else if (pattern == "MM-dd") {
			timeString = month + "-" + day;
		} else if (pattern == "MM月dd日") {
			timeString = month + "月" + day + "日";
		} else if (pattern == "yy-MM-dd") {
			timeString = year.substring(2, 2) + "-" + month + "-" + day;
		} else if (pattern == "yy年MM月dd日") {
			timeString = year.substring(2, 2) + "年" + month + "月" + day + "日";
		} else if (pattern == "yyyy-MM-dd") {
			timeString = year + "-" + month + "-" + day;
		} else if (pattern == "yyyy年MM月dd日") {
			timeString = year + "年" + month + "月" + day + "日";
		} else if (pattern == "HH") {
			timeString = hours;
		} else if (pattern == "HH时") {
			timeString = hours + "时";
		} else if (pattern == "dd HH") {
			timeString = day + " " + hours;
		} else if (pattern == "dd日 HH时") {
			timeString = day + "日 " + hours + "时";
		} else if (pattern == "MM-dd HH") {
			timeString = month + "-" + day + " " + hours;
		} else if (pattern == "MM月dd日 HH时") {
			timeString = month + "月" + day + "日 " + hours + "时";
		} else if (pattern == "yy-MM-dd HH") {
			timeString = year.substring(2, 2) + "-" + month + "-" + day + " " + hours;
		} else if (pattern == "yy年MM月dd日 HH时") {
			timeString = year.substring(2, 2) + "年" + month + "月" + day + "日 " + hours + "时";
		} else if (pattern == "yyyy-MM-dd HH") {
			timeString = year + "-" + month + "-" + day + " " + hours;
		} else if (pattern == "yyyy年MM月dd日 HH时") {
			timeString = year + "年" + month + "月" + day + "日 " + hours + "时";
		} else if (pattern == "mm") {
			timeString = minutes;
		} else if (pattern == "mm分") {
			timeString = minutes + "分";
		} else if (pattern == "HH:mm") {
			timeString = hours + ":" + minutes;
		} else if (pattern == "HH时mm分") {
			timeString = hours + "时" + minutes + "分";
		} else if (pattern == "dd HH:mm") {
			timeString = day + " " + hours + ":" + minutes;
		} else if (pattern == "dd日 HH时mm分") {
			timeString = day + "日 " + hours + "时" + minutes + "分";
		} else if (pattern == "MM-dd HH:mm") {
			timeString = month + "-" + day + " " + hours + ":" + minutes;
		} else if (pattern == "MM月dd日 HH时mm分") {
			timeString = month + "月" + day + "日 " + hours + "时" + minutes + "分";
		} else if (pattern == "yy-MM-dd HH:mm") {
			timeString = year.substring(2, 2) + "-" + month + "-" + day + " " + hours + ":" + minutes;
		} else if (pattern == "yy年MM月dd日 HH时mm分") {
			timeString = year.substring(2, 2) + "年" + month + "月" + day + "日 " + hours + "时" + minutes + "分";
		} else if (pattern == "yyyy-MM-dd HH:mm") {
			timeString = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
		} else if (pattern == "yyyy年MM月dd日 HH时mm分") {
			timeString = year + "年" + month + "月" + day + "日 " + hours + "时" + minutes + "分";
		} else if (pattern == "ss") {
			timeString = seconds;
		} else if (pattern == "ss秒") {
			timeString = seconds + "秒";
		} else if (pattern == "mm:ss") {
			timeString = minutes + ":" + seconds;
		} else if (pattern == "mm分ss秒") {
			timeString = minutes + "分" + seconds + "秒";
		} else if (pattern == "HH:mm:ss") {
			timeString = hours + ":" + minutes + ":" + seconds;
		} else if (pattern == "HH:mm分ss秒") {
			timeString = hours + "时" + minutes + "分" + seconds + "秒";
		} else if (pattern == "dd HH:mm:ss") {
			timeString = day + " " + hours + ":" + minutes + ":" + seconds;
		} else if (pattern == "dd日 HH:mm分ss秒") {
			timeString = day + "日 " + hours + "时" + minutes + "分" + seconds + "秒";
		} else if (pattern == "MM-dd HH:mm:ss") {
			timeString = month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
		} else if (pattern == "MM月dd日 HH:mm分ss秒") {
			timeString = month + "月" + day + "日 " + hours + "时" + minutes + "分" + seconds + "秒";
		} else if (pattern == "yy-MM-dd HH:mm:ss") {
			timeString = year.substring(2, 2) + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
		} else if (pattern == "yy年MM月dd日 HH:mm分ss秒") {
			timeString = year.substring(2, 2) + "年" + month + "月" + day + "日 " + hours + "时" + minutes + "分" + seconds + "秒";
		} else if (pattern == "yyyy-MM-dd HH:mm:ss") {
			timeString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
		} else if (pattern == "yyyy年MM月dd日 HH:mm分ss秒") {
			timeString = year + "年" + month + "月" + day + "日 " + hours + "时" + minutes + "分" + seconds + "秒";
		} 
	} else {
		timeString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
	}
	
	return timeString;
}