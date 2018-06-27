var popped = new Array();
var scrollBack = false;

$(document).ready(function(e) {
	//scrollTop();
});

/**
 * enable context menu
 * 
 * @param id DomElement id
 * @param menu context menu
 * 
 * @returns void 
 */
function contextMenu(id, menu) {
	$.contextMenu({
        selector: "#" + id, 
        callback: function(key, options) {
            eval(eval("options.items."+key+".onclick"));
        },
        items: menu
    });
}

/**
 * make an element on or off
 *        
 * @param id id of target element
 * 
 * @returns void
 */
function on_off(id) {
	var ele = document.getElementById(id);
	if (ele.style.display == "none") {
		ele.style.display = "";
	} else {
		ele.style.display = "none";
	}
}

/**
 * pop a central div and make background disabled
 * 
 * @param id id of element that be popped
 * 
 * @returns void
 */
function iPopup(id) {
	if (id) {
		var popup_height = $("#" + id).css("height");
		var popup_width = $("#" + id).css("width");
		
		popped.push($.layer({
			    type : 1,
			    title : ['',false],
			    closeBtn : ['',false],
			    fix : false,
			    offset:['50%' , '50%'],
			    area : [popup_width, popup_height],
			    page : {dom : '#' + id}
			})
		);
	} else {
		mask();
	}
	
	document.onkeyup = keyUp;
	document.body.onunload = iCloseDiv;
	
	if (parent.header && parent.header.mask) parent.header.mask();
	if (parent.left && parent.left.mask) parent.left.mask();
	if (parent.bottom && parent.bottom.mask) parent.bottom.mask();
}

/**
 * close popped div and enable background
 * 
 * @returns void
 */
function iCloseDiv() {
	while (popped.length > 0) {
		layer.close(popped[0]);
		popped.pop();
	}
	
	if (parent.header && parent.header.mask) parent.header.unmask();
	if (parent.left && parent.left.mask) parent.left.unmask();
	if (parent.bottom && parent.bottom.mask) parent.bottom.unmask();
}

/**
 * to press ESC to close popped div
 *       
 * @param e keyup event(just for ie)
 * 
 * @returns void
 */
function keyUp(e) {
	var currKey=0;
	e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	switch (currKey){
		case 27: iCloseDiv(); break;
	}
}

/**
 * mask background
 * 
 * @returns void
 */
function mask() {
	popped.push($.layer({ type : 1 }));
}

/**
 * roll back to top
 */
function scrollTop() {
	jQuery(function($) {
		$.scrolltotop({
			className : '',
			controlHTML : '<a href="javascript:;">回到顶部↑</a>',
			//此处可以换成图片如 '' ,
			//controlHTML : '<img style="width: 24px; height: 24px;" src="http://www.rainleaves.com/go-top.png"/>', //此处可以换成下面的图片格式
			offsety : 0
		});
	});
}

/**
 * unmask background
 * 
 * @returns void
 */
function unmask(i) {
	while (popped.length > 0) {
		layer.close(popped[0]);
		popped.pop();
	}
}