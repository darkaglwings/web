$(document).ready(function(e) {
	$(".multiSelect").multiselect({
		header: false,
		noneSelectedText: "--请选择--",
        checkAllText: "全选",
        uncheckAllText: '全不选',
		selectedList: 3
	});
});

/**
 * move one option form left to right
 *      
 * @param leftId id of left select
 * @param rightId id of right select
 * 
 * @returns void
 */
function move(leftId, rightId) {
	var left = document.getElementById(leftId);
	var right = document.getElementById(rightId);
	
	if (left && right) {
		var index = left.selectedIndex;
		if (index != -1) {
			var option = left.options[index];
			right.options.add(option);
			
			var rightlength = right.options.length;
			for (var i = 0; i < rightlength; i++) {
				if (right.options[0].value > option.value) {
					right.options.add(right.options[0]);
				}
			}
		}
	}
}

/**
 * move all options form left to right
 *      
 * @param leftId id of left select
 * @param rightId id of right select
 * 
 * @returns void
 */
function move_all(leftId, rightId) {
	var left = document.getElementById(leftId);
	var right = document.getElementById(rightId);
	
	var leftlength = left.options.length;
	for (var i = 0; i < leftlength; i++) {
		right.options.add(left.options[0]);
	}
	
	var rightlength = right.options.length;
	for (var i = 0; i < rightlength; i++) {
		if (right.options[0].value > right.options[right.options.length - 1].value) {
			right.options.add(right.options[0]);
		}
	}
}

/**
 * remove all options form a select
 *      
 * @param id id of select
 * 
 * @returns void
 */
function removeOptions(id) {
	document.getElementById(id).options.length = 0;
}

/**
 * make a select readOnly(needs addEvent)
 *      
 * @param args object displayed
 * 
 * @returns void
 */
function reset(args) {
	var val = this.val;
	var sel = this.sel;

	var option = sel.options;
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == val) {
			option[i].selected = true;
		} else {
			option[i].selected = false;
		}
	}
}

/*function reset(val, ele) {
	var option = ele.options;
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == val) {
			option[i].selected = true;
		} else {
			option[i].selected = false;
		}
	}
}*/

/**
 * to get selected options information in one select, id and name separated by "||"
 * 
 * @param selectid id of select
 * 
 * @returns void
 */
function selected(selectid, container_id, container_name) {
	var data, idcontainer, namecontainer;
	
	if (selectid) {
		data = document.getElementById(selectid);
	} else {
		data = undefined;
	}
	
	if (container_id) {
		idcontainer = document.getElementById(container_id);
	} else {
		idcontainer = undefined;
	}
	
	if (container_name) {
		namecontainer = document.getElementById(container_name);
	} else {
		namecontainer = undefined;
	}
	
	var id = undefined, name = undefined;
	if (data) {
		for (var i = 0; i < data.options.length; i++) {
			if (data.options[i].selected == true) {
				if (id) {
					id += "," + data.options[i].value;
				} else {
					id = data.options[i].value;
				}
				
				if (name) {
					name += "," + data.options[i].innerText;
				} else {
					name = data.options[i].innerText;
				}
			}
		}
	}
	
	if (id == undefined) id = "";
	if (name == undefined) name = "";
	
	if (idcontainer) {
		idcontainer.value = id;
	}
	
	if (namecontainer) {
		namecontainer.value = name;
	}
	
	return id + "||" + name;
}

/**
 * connection ids of options(separated by ","), texts of options(separated by ",") and put them in two elements
 * 
 * @param selectid id of select
 * @param container_id id of element that put ids in
 * @param container_name id of element that put texts in
 * 
 * @returns void
 */
function selectHandler(selectid, container_id, container_name) {
	var data, idcontainer, namecontainer;
	
	if (selectid) {
		data = document.getElementById(selectid);
	} else {
		data = undefined;
	}
	
	if (container_id) {
		idcontainer = document.getElementById(container_id);
	} else {
		idcontainer = undefined;
	}
	
	if (container_name) {
		namecontainer = document.getElementById(container_name);
	} else {
		namecontainer = undefined;
	}
	
	var id = "", name = "";
	if (data) {
		for (var i = 0; i < data.options.length; i++) {
			if (i == data.options.length - 1) {
				id += data.options[i].value;
				name += data.options[i].innerText;
			} else {
				id += data.options[i].value + ",";
				name += data.options[i].innerText + ",";
			}

		}
	}
	
	if (idcontainer) {
		idcontainer.value = id;
	}
	
	if (namecontainer) {
		namecontainer.value = name;
	}
	
	return id + "||" + name;
}

/**
 * to get data according to another select
 * 
 * @param url ajax url to get data
 * @param that object of select to trigger this event
 * @param callback callback function to handle data
 * 
 * @returns void
 */
function subset(url, that, parameter, callback) {
	var sort = that.value;
	if (sort != -1) {
		eval(ajax("POST", url, parameter + "=" + sort, true, callback));
	}
}