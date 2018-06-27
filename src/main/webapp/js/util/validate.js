/**
 * attach mode for number, cellphone, telephone
 */
$(document).ready(function() {
	$(".number").keyup(function(){
		$(this).val($(this).val().replace(/\D|^0/g,''));  
	}).bind("paste",function(){
		$(this).val($(this).val().replace(/\D|^0/g,''));  
	}).css("ime-mode", "disabled");
	
	$(".cellphone").keyup(function(){
		$(this).val($(this).val().replace(/\D|^0/g,''));  
	}).bind("paste",function(){
		$(this).val($(this).val().replace(/\D|^0/g,''));  
	}).css("ime-mode", "disabled");
	
	$(".telephone").keyup(function(){
		$(this).val($(this).val().replace(/[^0-9-]|^0/g,''));  
	}).bind("paste",function(){
		$(this).val($(this).val().replace(/[^0-9-]|^0/g,''));  
	}).css("ime-mode", "disabled");
});

/**
 * check a value is a cellphone number or not
 * @param value value to be checked
 * @returns true value is a cellphone number
 *          false value is not a cellphone number
 */
function cellphone(value) {
	if (value != "") {
		var reg = new RegExp(/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/);
		if(!reg.test(value)) return false;
	}

	return true;
}

/**
 * check a value is a email address or not
 * @param value value to be checked
 * @returns true value is a email address
 *          false value is not a email address
 */
function email(value) {
	if (value != "") {
		var reg = new RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/);
		if(!reg.test(value)) return false;
	}

	return true;
}

/**
 * check a value is a number or not
 * @param value value to be checked
 * @returns true value is number
 *          false value is not number
 */
function number(value) {
	if (value != "") {
		var reg = new RegExp(/^[0-9]*$/);
		if(!reg.test(value)) return false;
	}
	
	return true;
}

/**
 * check a value is a telephone number or not
 * @param value value to be checked
 * @returns true value is a telephone number
 *          false value is not a telephone number
 */
function telephone(value) {
	if (value != "") {
		var reg = new RegExp(/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/);
		if(!reg.test(value)) return false;
	}

	return true;
}
