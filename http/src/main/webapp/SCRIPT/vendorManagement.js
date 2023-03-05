/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	/**
	 * 一次性網頁製作費
	 */
	$('FORM#oneTimeCharge>INPUT[type="button"]').click(function () {
		var f = this.parentNode;
		$.post($(f).attr('id') + '.json', $(f).serialize(), function (d) {
			if (d.reason) {
				alert(d.reason);
			}
			if (d.response) {
				var r = d.result;
				$('INPUT#oneTimeChargeMerchantTradeNo').val(r.merchantTradeNo);
				$('INPUT#oneTimeChargeMerchantTradeDate').val(r.merchantTradeDate);
				$('INPUT#oneTimeChargeCheckMacValue').val(r.checkMacValue);
				$(f).submit();
			}
		}, 'json');
	});

	/**
	 * 每月上架費
	 */
	$('FORM#monthlyCharge>INPUT[type="button"]').click(function () {
		var f = this.parentNode;
		$.post($(f).attr('id') + '.json', $(f).serialize(), function (d) {
			if (d.reason) {
				alert(d.reason);
			}
			if (d.response) {
				var r = d.result;
				$('INPUT#monthlyChargeMerchantTradeNo').val(r.merchantTradeNo);
				$('INPUT#monthlyChargeMerchantTradeDate').val(r.merchantTradeDate);
				$('INPUT#monthlyChargeCheckMacValue').val(r.checkMacValue);
				$(f).submit();
			}
		}, 'json');
	});
});