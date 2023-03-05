/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	/**
	 * Google 自訂搜尋
	 */
	var cx = '004921207483117845430:txnxnmixoyq';
	var gcse = document.createElement('script');
	gcse.type = 'text/javascript';
	gcse.async = true;
	gcse.src = (document.location.protocol === 'https:' ? 'https:' : 'http:') + '//cse.google.com/cse.js?cx=' + cx;
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(gcse, s);

	/**
	 * 橫幅
	 */
	$('.headerBanner').unslider({
		speed: 400, //The speed to animate each slide (in milliseconds)
		delay: 3000, //The delay between slide animations (in milliseconds)
		complete: function () {
		}, //A function that gets called after every slide animation
		keys: true, //Enable keyboard (left, right) arrow shortcuts
		dots: false, //Display dot navigation
		fluid: false//Support responsive design. May break non-responsive designs
	});
});