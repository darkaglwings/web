/**
 * initialize player
 * @param url media url
 * @param autoplay (boolean) play or not play after player initiation
 * 
 * @returns void
 */
function init(url, autoplay) {
	mediaplayer.filename = url;
	if (autoplay == true) {
		start();
	}
}

/**
 * player start
 *
 * @returns void
 */
function start() {
	mediaplayer.play();
}

/**
 * player stop
 *
 * @returns void
 */
function stop() {
	mediaplayer.stop();
}