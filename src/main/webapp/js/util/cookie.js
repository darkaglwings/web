/**
 * get content from cookie
 *      
 * @param name content name
 * 
 * @returns content value
 */
function getCookie(name) {
	var result = "";
	
	var start = -1, end = -1;
	if (document.cookie.length > 0) {
		start = document.cookie.indexOf(name + "=");
		if (start != -1) {
			start = start + name.length + 1;
			end = document.cookie.indexOf(";", start);
			if (end == -1) {
				end = document.cookie.length;
			}
			
			result = unescape(document.cookie.substring(start, end));
		} 
	}
	
	return result;
}

/**
 * set content to cookie
 *      
 * @param name content name
 * @param value content value
 * @param days cookie expires days(count by day)
 * @param path cookie path(default is "/")
 * 
 * @returns void
 */
function setCookie(name, value, days, path) {
	var expiresDays = "";
	if (days) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + days);
		expiresDays = ";expires="+exdate.toGMTString();
	}
	
	if (path) {
		path = ";path=" + path;
	} else {
		path = ";path=/";
	}
	
	document.cookie = name+ "=" + escape(value) + expiresDays + path;
}