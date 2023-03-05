package net.tourmap.marketing.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.servlet.http.HttpSession;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.AllPayHistory;
import net.tourmap.marketing.entity.Availability;
import net.tourmap.marketing.repository.*;
import net.tourmap.marketing.service.Services;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;

/**
 * 業務團隊
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/agent")
public class AgentController {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AllPayHistoryRepository allPayHistoryRepository;

	@Autowired
	private AvailabilityRepository availabilityRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private Services services;

	/**
	 * 首頁
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView index(HttpSession session) throws Exception {
//		Document document = Utils.newDocument();
//		Element documentElement = Utils.createElement("document", document);
//
//		ModelAndView modelAndView = new ModelAndView("redirect:/");
//		modelAndView.getModelMap().addAttribute(new DOMSource(document));
//		return modelAndView;
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me == null) {
			return new ModelAndView("redirect:/logIn.asp");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "me", me.toString());

		Utils.createElementWithTextContent("name", documentElement, services.getWhoever(me, role, session));

		Availability availability = availabilityRepository.findOneByRoleAndPrimaryKey(
			roleRepository.findOne((short) 2),
			me
		);
		if (availability == null) {
			availability = new Availability(roleRepository.findOne((short) 2), me, false);
			availabilityRepository.saveAndFlush(availability);
		}
		Date monthly = availability.getMonthly();

		ResourceBundle resourceBundle = ResourceBundle.getBundle("allPay");
		String merchantID = resourceBundle.getString("MerchantID"),
			returnURL = resourceBundle.getString("ReturnURL"),
			url = resourceBundle.getString("URL");

		/*
		 月費
		 */
		Element monthlyChargeElement = Utils.createElementWithTextContentAndAttribute("monthlyCharge", documentElement, url, "merchantID", merchantID);
		monthlyChargeElement.setAttribute("paid", Boolean.toString(monthly != null && !new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN).getTime().after(monthly)));
		if (monthly != null) {
			monthlyChargeElement.setAttribute("until", Utils.formatDate(monthly));
		}
		boolean isSupervisor = false;
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/agent/" + me + ".json");
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					if (jsonObject.has("result")) {
						JSONObject result = jsonObject.getJSONObject("result");
						if (result.has("status")) {
							isSupervisor = result.getInt("status") == 1;
						}
					}
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + ":\n\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}
		monthlyChargeElement.setAttribute("amount", isSupervisor ? "10000" : "5000");
		monthlyChargeElement.setAttribute("returnUrl", returnURL);

		/*
		 Google 費用
		 */
		Element oneTimeChargeElement = Utils.createElementWithTextContentAndAttribute("oneTimeCharge", documentElement, url, "merchantID", merchantID);
		oneTimeChargeElement.setAttribute("paid", Boolean.toString(availability.getOneTime()));
		oneTimeChargeElement.setAttribute("amount", "1500");
		oneTimeChargeElement.setAttribute("returnUrl", returnURL);

		ModelAndView modelAndView = new ModelAndView("agent/default");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 月費
	 *
	 * @param merchantID 廠商編號
	 * @param paymentType 交易類型
	 * @param totalAmount 交易金額
	 * @param tradeDesc 交易描述
	 * @param itemName 商品名稱
	 * @param returnURL 付款完成通知回傳網址
	 * @param choosePayment 付款方式
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/monthlyCharge.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String monthlyCharge(@RequestParam(name = "MerchantID") String merchantID, @RequestParam(name = "PaymentType") String paymentType, @RequestParam(name = "TotalAmount") Integer totalAmount, @RequestParam(name = "TradeDesc") String tradeDesc, @RequestParam(name = "ItemName") String itemName, @RequestParam(name = "ReturnURL") String returnURL, @RequestParam(name = "ChoosePayment") String choosePayment, HttpSession session) {
		JSONObject jsonObject = new JSONObject();

		Integer me = (Integer) session.getAttribute("me");
		if (me == null) {
			return jsonObject.put("reason", "尚未登入！").toString();
		}

		Map<String, String> map = new HashMap<>();
		try {
			ResourceBundle resourceBundle = ResourceBundle.getBundle("allPay");
			GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);

			/*
			 廠商交易編號
			 */
			String merchantTradeNo = Long.toString(gregorianCalendar.getTimeInMillis());
			Integer suffix = 0;
			while (allPayHistoryRepository.findOneByMerchantTradeNo(merchantTradeNo) != null) {
				suffix++;
			}
			merchantTradeNo += suffix.toString();
			map.put("merchantTradeNo", merchantTradeNo);

			/*
			 廠商交易時間
			 */
			String merchantTradeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(gregorianCalendar.getTime());
			map.put("merchantTradeDate", merchantTradeDate);

			/*
			 產生檢查碼並組合 JSON 結果物件
			 */
			Map<String, String> parameterMap = new TreeMap<>();
			parameterMap.put("MerchantID", merchantID);
			parameterMap.put("MerchantTradeNo", merchantTradeNo);
			parameterMap.put("MerchantTradeDate", merchantTradeDate);
			parameterMap.put("PaymentType", paymentType);
			parameterMap.put("TotalAmount", totalAmount.toString());
			parameterMap.put("TradeDesc", tradeDesc);
			parameterMap.put("ItemName", itemName);
			parameterMap.put("ReturnURL", returnURL);
			parameterMap.put("ChoosePayment", choosePayment);
			StringBuilder stringBuilder = new StringBuilder("HashKey=" + resourceBundle.getString("HashKey"));
			for (Map.Entry<String, String> entrySet : parameterMap.entrySet()) {
				stringBuilder.append("&").append(entrySet.getKey()).append("=").append(entrySet.getValue());
			}
			stringBuilder.append("&HashIV=").append(resourceBundle.getString("HashIV"));
			String checkMacValue = Utils.md5(URLEncoder.encode(stringBuilder.toString(), "UTF-8").toLowerCase()).toUpperCase();
			map.put("checkMacValue", checkMacValue);

			/*
			 將付款資訊保存以備歐付寶收到付款後關連
			 */
			allPayHistoryRepository.saveAndFlush(
				new AllPayHistory(
					availabilityRepository.findOneByRoleAndPrimaryKey(roleRepository.findOne((short) 2), me),
					merchantTradeNo,
					itemName,
					checkMacValue,
					accountRepository.findOne((short) 3)
				)
			);
		} catch (Exception exception) {
			return jsonObject.put("reason", exception.getLocalizedMessage()).toString();
		}
		return jsonObject.put("response", true).put("result", map).toString();
	}

	/**
	 * Google 費用
	 *
	 * @param merchantID 廠商編號
	 * @param paymentType 交易類型
	 * @param totalAmount 交易金額
	 * @param tradeDesc 交易描述
	 * @param itemName 商品名稱
	 * @param returnURL 付款完成通知回傳網址
	 * @param choosePayment 付款方式
	 * @return JSONObject
	 */
	@RequestMapping(value = "/oneTimeCharge.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String oneTimeCharge(@RequestParam(name = "MerchantID") String merchantID, @RequestParam(name = "PaymentType") String paymentType, @RequestParam(name = "TotalAmount") Integer totalAmount, @RequestParam(name = "TradeDesc") String tradeDesc, @RequestParam(name = "ItemName") String itemName, @RequestParam(name = "ReturnURL") String returnURL, @RequestParam(name = "ChoosePayment") String choosePayment, HttpSession session) {
		JSONObject jsonObject = new JSONObject();

		Integer me = (Integer) session.getAttribute("me");
		if (me == null) {
			return jsonObject.put("reason", "尚未登入！").toString();
		}

		Map<String, String> map = new HashMap<>();
		try {
			ResourceBundle resourceBundle = ResourceBundle.getBundle("allPay");
			GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN);

			/*
			 廠商交易編號
			 */
			String merchantTradeNo = Long.toString(gregorianCalendar.getTimeInMillis());
			Integer suffix = 0;
			while (allPayHistoryRepository.findOneByMerchantTradeNo(merchantTradeNo) != null) {
				suffix++;
			}
			merchantTradeNo += suffix.toString();
			map.put("merchantTradeNo", merchantTradeNo);

			/*
			 廠商交易時間
			 */
			String merchantTradeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(gregorianCalendar.getTime());
			map.put("merchantTradeDate", merchantTradeDate);

			/*
			 產生檢查碼並組合 JSON 結果物件
			 */
			Map<String, String> parameterMap = new TreeMap<>();
			parameterMap.put("MerchantID", merchantID);
			parameterMap.put("MerchantTradeNo", merchantTradeNo);
			parameterMap.put("MerchantTradeDate", merchantTradeDate);
			parameterMap.put("PaymentType", paymentType);
			parameterMap.put("TotalAmount", totalAmount.toString());
			parameterMap.put("TradeDesc", tradeDesc);
			parameterMap.put("ItemName", itemName);
			parameterMap.put("ReturnURL", returnURL);
			parameterMap.put("ChoosePayment", choosePayment);
			StringBuilder stringBuilder = new StringBuilder("HashKey=" + resourceBundle.getString("HashKey"));
			for (Map.Entry<String, String> entrySet : parameterMap.entrySet()) {
				stringBuilder.append("&").append(entrySet.getKey()).append("=").append(entrySet.getValue());
			}
			stringBuilder.append("&HashIV=").append(resourceBundle.getString("HashIV"));
			String checkMacValue = Utils.md5(URLEncoder.encode(stringBuilder.toString(), "UTF-8").toLowerCase()).toUpperCase();
			map.put("checkMacValue", checkMacValue);

			/*
			 將付款資訊保存以備歐付寶收到付款後關連
			 */
			allPayHistoryRepository.saveAndFlush(
				new AllPayHistory(
					availabilityRepository.findOneByRoleAndPrimaryKey(roleRepository.findOne((short) 2), me),
					merchantTradeNo,
					itemName,
					checkMacValue,
					accountRepository.findOne((short) 4)
				)
			);
		} catch (Exception exception) {
			return jsonObject.put("reason", exception.getLocalizedMessage()).toString();
		}
		return jsonObject.put("response", true).put("result", map).toString();
	}

	/**
	 * 有效期限
	 *
	 * @param id 業務團隊對應至旅圖觀止資料表的主鍵
	 * @param session 階段
	 * @return JSONObject
	 */
	@RequestMapping(value = "/{id:[\\d]+}/availability.json", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	protected String availability(@PathVariable Integer id, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Availability availability = availabilityRepository.findOneByRoleAndPrimaryKey(roleRepository.findOne((short) 2), id);
		if (availability == null) {
			return jsonObject.put("reason", "找不到此業務團隊！").put("response", false).toString();
		}
		Date monthly = availability.getMonthly();

		JSONObject resultJSONObject = new JSONObject();
		resultJSONObject.put("oneTimeCharge", availability.getOneTime());
		resultJSONObject.put("monthlyCharge", monthly != null && !new GregorianCalendar(TimeZone.getTimeZone("Asia/Taipei"), Locale.TAIWAN).getTime().after(monthly));
		return jsonObject.put("response", true).put("result", resultJSONObject).toString();
	}
}
