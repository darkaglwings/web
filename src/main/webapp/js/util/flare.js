$(document).ready(function (){
    var src = 'http://flarevideo.com/flarevideo/examples/volcano.mp4';
    var type = 'video/mp4';
    setup(src, type);
});

/**
 * initialize flare video player
 * @param url media url
 * @param autoplay (boolean) play or not play after player initiation
 * 
 * @returns void
 */
function setup(src, type) {
	$(document).ready(function (){
	    jQuery(function($){
	      fv = $("#video").flareVideo();
	      fv.load([
	        {
	          src:  src,
	          type: type
	        }
	      ]);
	    });
	});
}

