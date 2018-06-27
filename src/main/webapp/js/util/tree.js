var queryNodes;
var setting;
var zNodes;

/**
 * expand parent node
 * @param node
 * @param isAll true expand this node and all children
 *              false only expand this node
 * 
 * @returns void
 */
function expandParent(node, isAll) {
	if (node && node.getParentNode()) {
		var zTree = $.fn.zTree.getZTreeObj("tree");
		if (isAll == true) {
			zTree.expandNode(node.getParentNode(), true, false, false);
			expandParent(node.getParentNode(), true);
		} else {
			zTree.expandNode(node.getParentNode(), true, false, false);
		}
	}
}

function getFontCss(treeId, treeNode) {
	return (treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
}

/**
 * set selected nodes highlight
 * 
 * @returns void
 */
function highlight() {
  var parameter = document.getElementById("parameter").value;
  var zTree = $.fn.zTree.getZTreeObj("tree");
  queryNodes = zTree.getNodesByParamFuzzy("name", parameter);
  
  for (var i= 0; i < queryNodes.length; i++) {
	queryNodes[i].highlight = true;
	zTree.updateNode(queryNodes[i]);
	expandParent(queryNodes[i], true);
  }
}

/**
 * close tree
 *        
 * @param isWindow true open tree with modelDialog window
 * 
 * @returns void
 */
function iClose(isWindow) {
	if (isWindow) 
		window.close();
	else 
		iCloseDiv();
}

/**
 * initiate nodes' check state
 *
 * @param info checked nodes' ids(separated by ",")
 * 
 * @returns void
 */
function initNode(info) {
	var checked = info.split(",");
	var zTree = $.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.transformToArray(zTree.getNodes());
	for (var i = 0; i < checked.length; i++) {
		for (var j = 0; j < nodes.length; j++) {
			if (nodes[j].id == checked[i]) {
				zTree.checkNode(nodes[j], true, false, false);
			}
		}
	}
}

/**
 * initiate a tree
 * @param url url to get date
 * @param content id of tree div
 * @param multi true only check one
 *              false could check more 
 * @param checked checked nodes' ids(separated by ",")
 * @param mode check pattern
 * 
 * @returns void
 */
function initTree(url, content, multi, checked, mode) {
	if (multi == true) {
		setting = {check: {enable: true},data : {simpleData : {enable : true}},view: {fontCss: getFontCss}};
	} else {
		setting = {check: {enable: true,chkStyle: "radio",radioType: "all"},data : {simpleData : {enable : true}},view: {fontCss: getFontCss}};
	}
	
	if (mode) {
		setting.check.chkboxType = mode;
	}
	
	if (document.getElementById("parameter")) {
		document.getElementById("parameter").value = "";
	}
	
	ajax("POST", url, "", true, 'zNodes = JSON.parse(msg);$.fn.zTree.init($("#tree"), setting, zNodes);if ("' + checked + '") initNode("' + checked + '");');
	
	iPopup(content);
}

/**
 * open or close query zone
 *        
 * @param e switch bar element
 * 
 * @returns void
 */
function iQuery(e) {
	var height = $("#wrap").css("height");
	height = height.replace(/px/g, "");
	if (e.className == 'current') {
		height = parseInt(height) - 30;
		$("#wrap").css("height", height + "px");
		e.className = ''; 
	} else if (e.className == '') {
		height = parseInt(height) + 30;
		$("#wrap").css("height", height + "px");
		e.className = 'current';
	}
	
	on_off('query');
}

/**
 * set null operation method
 * 
 * @returns void
 */
function iNull() {
	resultHandle();
	iClose();
}

/**
 * save operation method
 * 
 * @returns void
 */
function iSave() {
	var result = new Array();
	var zTree = $.fn.zTree.getZTreeObj("tree");
	var nodes = zTree.transformToArray(zTree.getNodes());
	for (var i = 0; i < nodes.length; i++) {
		if (nodes[i].checked == true) {
			result.push(nodes[i]);
		}
	}
	
	resultHandle(result);
	iClose();
}

/**
 * set check mode
 * 
 * @param pattern check pattern
 * 
 * @returns void
 */
function setCheckMode(pattern) {
	if (pattern) {
		var zTree = $.fn.zTree.getZTreeObj("tree");
		zTree.setting.check.chkboxType = pattern;
	}
}

/**
 * make all nodes unhighlight
 * 
 * @returns void
 */
function unhighlight() {
	  document.getElementById("parameter").value = "";
	  var zTree = $.fn.zTree.getZTreeObj("tree");
	  for (var i= 0; i < queryNodes.length; i++) {
		queryNodes[i].highlight = false;
		zTree.updateNode(queryNodes[i]);
	  }
	}