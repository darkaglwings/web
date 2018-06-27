/**
 * method for ajax invoke
 *      
 * @param type http request type
 * @param url http request url
 * @param data http request parameters
 * @param async  true sync
 *              false async
 * @param successCallback successCallback will be invoked when remote servlet respond
 * @param failureCallback failureCallback will be invoked when errors occurred in remote servlet
 * 
 * @returns void
 */
function ajax(type, url, data, async, successCallback, failureCallback) {
	$.ajax({
		type    : type,
		url     : url,
		data    : data,
		async   : async,
		success : function(msg) {
			if (successCallback) eval(successCallback);
		},
		error   : function(e){
			if (failureCallback) {
				eval(failureCallback);
			} else {
				alert("获取信息出错\nerror: "+e.status+" "+e.statusText);
			}
		}
	});
}

/**
 * abandon operation method
 * 
 * @param id of form that be submitted
 * @param url http request url
 * @param flag abandon 0 available 1 inavailable
 * 
 * @returns void
 */
function abandon(form, url, flag) {
	var id = id_picker("id", false);

	if (id) {
		document.getElementById("ids").value = id;
		document.getElementById("flag").value = flag;
		save(form, false, url);
	}
}

/**
 * back operation method
 *      
 * @param url http request url
 * 
 * @returns void
 */
function back(url) {
	if (url)
		window.location.href = url;
	else
		window.back();
}

/**
 * cancel operation method
 * 
 * @param form id of form that be canceled
 * 
 * @returns void
 */
function cancel(form) {
	var target_form;
	if (form) {
		target_form = document.getElementById(form);
	} else {
		target_form = document.forms[0];
	}
	
	if (target_form) {
		var reset_button = document.createElement("input");
		reset_button.type = "reset";
		target_form.appendChild(reset_button);
		reset_button.click();
		target_form.removeChild(reset_button);
	}
}

/**
 * create operation method
 *        
 * @param form id of form that be submitted
 * @param url http request url
 * 
 * @returns void
 */
function create(form, url) {
	document.getElementById("ids").value = "";
	save(form, false, url);
}

/**
 * edit operation method
 *        
 * @param form id of form that be submitted
 * @param url http request url
 * 
 * @returns void
 */
function edit(form, url) {
	var id = id_picker("id", true);

	if (id) {
		document.getElementById("ids").value = id;
		save(form, false, url);
	}
}

/**
 * forward operation method
 *
 * @param url http request url
 * 
 * @returns void
 */
function forward(url) {
	if (url)
		window.location.href = url;
	else
		window.forward();
}

/**
 * go operation method
 *
 * @param url http request url
 * 
 * @returns void
 */
function go(url){
	window.location.href = url;
}

/**
 * export operation method
 *           
 * @param form id of form that be submitted
 * @param url http request url
 * 
 * @returns void
 */
function im_export(form, url) {
	save(form, false, url);
}

/**
 * move operation method
 *      
 * @param form id of form that be submitted
 * @param verify (boolean) form needs verify or not
 * @param url http request url
 * 
 * @returns void
 */
function move(form, verify, url) {
	save(form, verify, url);
}

/**
 * query operation method
 *       
 * @param form id of form that be submitted
 * @param url http request url
 * 
 * @returns void
 */
function query(form, url) {
	save(form, false, url);
}

/**
 * refresh operation method
 *       
 * @param form id of form that be submitted
 * @param url http request url
 * 
 * @returns void
 */
function refresh(form, url) {
	save(form, false, url);
}


/**
 * save operation method
 *       
 * @param form id of form that be submitted
 * @param verify form needs verify or not(boolean)
 * @param url http request url
 * 
 * @returns void
 */
function save(form, verify, url) {
	var target_form;
	if (form) {
		target_form = document.getElementById(form);
	} else {
		target_form = document.forms[0];
	}
	
	if (verify){
		if (validate) {
			if (!validate()) {
				return false;
			}
		}
	}

	if (url) target_form.action = url;
	
	if (target_form) {
		var submit_button = document.createElement("input");
		submit_button.type = "submit";
		target_form.appendChild(submit_button);
		submit_button.click();
		target_form.removeChild(submit_button);
	}
	
	//document.forms[0].submit();
}

function synchronization(form, url) {
	var callback = "if (msg == 'true') alert('数据同步已提交，稍后请重新刷新页面'); else alert('数据同步失败，原数据可能已丢失，请联系管理员从数据库手动同步数据');";
	ajax("POST", url, "", false, callback);
}

/**
 * remove operation method
 *       
 * @param form id of form that be submitted
 * @param url http request url
 * 
 * @returns void
 */
function trash(form, url) {
	var id = id_picker("id", false);

	if (id) {
		document.getElementById("ids").value = id;
		if (confirm("该操作将删除所选数据，是否继续？")) {
			save(form, false, url);
		}
	}
}

/**
 * verify operation method
 * 
 * @param id of form that be submitted
 * @param url http request url
 * @param flag verify 0 pass 1 reject
 * 
 * @returns void
 */
function verify(form, url, flag) {
	var id = id_picker("id", false);

	if (id) {
		document.getElementById("ids").value = id;
		document.getElementById("flag").value = flag;
		save(form, false, url);
	}
}