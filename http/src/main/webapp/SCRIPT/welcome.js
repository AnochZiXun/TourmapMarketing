/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	$('A.frequentlyAskedQuestion').click(function (e) {
		e.preventDefault();
		$(this).siblings('P').toggle();
		return false;
	});
	$('DIV.rightContentVideo LI>A').click(function (e) {
		e.preventDefault();
		$('IFRAME#youtube').attr({'src': $(this).attr('href')});
		return false;
	});
	$('FORM.backForm').submit(function (e) {
		e.preventDefault();
		$.post($(this).attr('action'), $(this).serialize(), function (j) {
			if (j.reason) {
				alert(j.reason);
			}
			if (j.response) {
				alert('感謝您的意見回饋！');
			}
		}, 'json');
		return false;
	});
});