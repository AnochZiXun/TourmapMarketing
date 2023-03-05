/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	$('A.frequentlyAskedQuestion').click(function (e) {
		e.preventDefault();
		$(this).siblings('P').toggle();
		return false;
	});
});