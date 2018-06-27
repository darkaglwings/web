var sys = {};
var ua = navigator.userAgent.toLowerCase();
if (window.ActiveXObject)
	sys.ie = ua.match(/msie ([\d.]+)/)[1];
  else if (document.getBoxObjectFor)
	  sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];
  else if (window.MessageEvent && !document.getBoxObjectFor)
	  sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];
  else if (window.opera)
	  sys.opera = ua.match(/opera.([\d.]+)/)[1];
  else if (window.openDatabase)
	  sys.safari = ua.match(/version\/([\d.]+)/)[1];
  else
	  sys.unknown = "unknown";
    	