/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	/**
	 * 我同意
	 */
	$('INPUT#agreeToStep2').click(function () {
		if (this.checked) {
			$('A#goToStep2').show();
			$(this.parentNode).hide();
		}
	});

	/**
	 * 跳至第二步驟
	 */
	$('A#goToStep2').click(function () {
		$('DIV#step1').hide(0, function () {
			$('DIV#step2').fadeToggle();
		});
	});

	/**
	 * 當您按下「確定送出」時即表示您已同意「線上合約法律條款說明」。
	 */
	$('A#clickToAgree').click(function (e) {
		e.preventDefault();
		$("#jDialog").dialog({
			draggable: false,
			maxHeight: document.documentElement.clientHeight / 3 * 2,
			modal: true,
			resizable: false,
			width: document.documentElement.clientWidth / 2
		});
		return false;
	});

	/**
	 * 第二次我同意
	 */
	$('INPUT#agreeAndSubmit').click(function () {
		if (this.checked) {
			$('INPUT#submit').show();
			$(this.parentNode).hide();
		}
	});
});