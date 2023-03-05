package net.tourmap.marketing.controller;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.*;
import net.tourmap.marketing.repository.*;
import net.tourmap.marketing.service.Services;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.*;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;

/**
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/")
public class RootController {

	@Autowired
	AllPayHistoryRepository allPayHistoryRepository;

	@Autowired
	AnnouncementRepository announcementRepository;

	@Autowired
	BulletinRepository bulletinRepository;

	@Autowired
	FrequentlyAskedQuestionRepository frequentlyAskedQuestionRepository;

	@Autowired
	YoutubeRepository youtubeRepository;

	@Autowired
	Services services;

	/**
	 * 首頁
	 *
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView index(HttpSession session) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);

		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			documentElement.setAttribute("me", me.toString());
		}

		boolean isFirst = true;
		Element youtubesElement = Utils.createElement("youtubes", documentElement);
		for (Youtube youtube : youtubeRepository.findTop5ByOrderByOrdinal()) {
			String url = youtube.getUrl();
			Utils.createElementWithTextContentAndAttribute("youtube", youtubesElement, youtube.getTitle(), "url", url);
			if (isFirst) {
				youtubesElement.setAttribute("first", url);
				isFirst = false;
			}
		}

		Element announcementsElement = Utils.createElement("announcements", documentElement);
		for (Announcement announcement : announcementRepository.findTop5ByOrderByWhenDesc()) {
			Utils.createElementWithTextContentAndAttribute("announcement", announcementsElement, announcement.getSubject(), "id", announcement.getId().toString());
		}

		Element frequentlyAskedQuestionsElement = Utils.createElement("frequentlyAskedQuestions", documentElement);
		for (FrequentlyAskedQuestion frequentlyAskedQuestion : frequentlyAskedQuestionRepository.findTop5ByOrderByOrdinal()) {
			Element frequentlyAskedQuestionElement = Utils.createElement("frequentlyAskedQuestion", frequentlyAskedQuestionsElement);
			Utils.createElementWithTextContent("question", frequentlyAskedQuestionElement, frequentlyAskedQuestion.getQuestion());
			Utils.createElementWithTextContent("answer", frequentlyAskedQuestionElement, frequentlyAskedQuestion.getAnswer());
		}

		ModelAndView modelAndView = new ModelAndView("default");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 登入
	 *
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/logIn.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView logIn(HttpSession session) throws Exception {
		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Utils.createElement("document", document);

		ModelAndView modelAndView = new ModelAndView("logIn");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 登入
	 *
	 * @param role 身份
	 * @param login 帳號
	 * @param shadow 密碼
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/logIn.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView logIn(@RequestParam String role, @RequestParam String login, @RequestParam String shadow, HttpSession session) throws Exception {
		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("login", login.toLowerCase());
		jsonObject.put("shadow", shadow);

		HttpPost httpPost = new HttpPost("http://restful.tour-map.net/" + role + "/logIn.json");
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("json", jsonObject.toString()));
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
		closeableHttpResponse = closeableHttpClient.execute(httpPost);
		if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
			Utils.createElementWithTextContent("errorMessage", documentElement, "遠端伺服器傳回錯誤的回應！");

			ModelAndView modelAndView = new ModelAndView("logIn");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		HttpEntity httpEntity = closeableHttpResponse.getEntity();
		if (httpEntity != null) {
			InputStream inputStream = null;
			inputStream = httpEntity.getContent();
			JSONObject jsonResponse = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
			if (jsonResponse.has("response") && !jsonResponse.isNull("response")) {
				if (jsonResponse.getBoolean("response") == false) {
					Utils.createElementWithTextContent("errorMessage", documentElement, "錯誤的帳號或密碼！");

					ModelAndView modelAndView = new ModelAndView("logIn");
					modelAndView.getModelMap().addAttribute(new DOMSource(document));
					return modelAndView;
				}
			}
			if (jsonResponse.has("result") && !jsonResponse.isNull("result")) {
				session.setAttribute("me", jsonResponse.getInt("result"));
				session.setAttribute("role", role);
			}
			IOUtils.closeQuietly(inputStream);
		}
		IOUtils.closeQuietly(closeableHttpResponse);
		IOUtils.closeQuietly(closeableHttpClient);

		/*
		 2016-03-30
		 */
		String requestURI = (String) session.getAttribute("referer");
		if (requestURI != null) {
			return new ModelAndView("redirect:" + requestURI);
		}

		return new ModelAndView("redirect:/" + role + "/");
	}

	/**
	 * 登出
	 *
	 * @param session
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/logOut.asp", method = RequestMethod.GET)
	protected ModelAndView logOut(HttpSession session) {
		session.invalidate();

		return new ModelAndView("redirect:/");
	}

	/**
	 * 活動消息(列表)
	 *
	 * @param number 第幾頁
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/announcement/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView announcement(@RequestParam(value = "p", defaultValue = "0") Integer number, HttpSession session) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			documentElement.setAttribute("me", me.toString());
		}

		/*
		 分頁
		 */
		final Integer size = 10;
		Element paginationElement = Utils.createElement("pagination", documentElement);
		Pageable pageRequest = new org.springframework.data.domain.PageRequest(number, size, Sort.Direction.DESC, "when");
		Page<Announcement> pageOfEntities = announcementRepository.findAll(pageRequest);
		number = pageOfEntities.getNumber();
		Integer totalPages = pageOfEntities.getTotalPages();
		if (pageOfEntities.hasPrevious()) {
			paginationElement.setAttribute("previous", Integer.toString(number - 1));
			if (!pageOfEntities.isFirst()) {
				paginationElement.setAttribute("first", "0");
			}
		}
		paginationElement.setAttribute("number", number.toString());
		for (Integer i = 0; i < totalPages; i++) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", paginationElement, Integer.toString(i + 1), "value", i.toString());
			if (number.equals(i)) {
				optionElement.setAttribute("selected", null);
			}
		}
		if (pageOfEntities.hasNext()) {
			paginationElement.setAttribute("next", Integer.toString(number + 1));
			if (!pageOfEntities.isLast()) {
				paginationElement.setAttribute("last", Integer.toString(totalPages - 1));
			}
		}

		/*
		 列表
		 */
		Element listElement = Utils.createElement("list", documentElement);
		for (Announcement entity : pageOfEntities.getContent()) {
			Date when = entity.getWhen();

			Element rowElement = Utils.createElementWithTextContentAndAttribute("row", listElement, entity.getSubject(), "id", entity.getId().toString());//列(主旨帶主鍵)
			rowElement.setAttribute("when", when == null ? "" : Utils.formatDate(when));//發佈日期
		}

		ModelAndView modelAndView = new ModelAndView("announcement/default");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 活動消息
	 *
	 * @param id 主鍵
	 * @param response 回應
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/announcement/{id:[\\d]+}.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView announcement(@PathVariable Short id, HttpServletResponse response, HttpSession session) throws Exception {
		Announcement entity = announcementRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			documentElement.setAttribute("me", me.toString());
		}

		Utils.createElementWithTextContent("subject", documentElement, entity.getSubject());
		Utils.createElementWithTextContent("hyperTextMarkupLanguage", documentElement, entity.getHyperTextMarkupLanguage());

		ModelAndView modelAndView = new ModelAndView("announcement/singleton");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 公佈欄
	 *
	 * @param request
	 * @param session
	 * @return 網頁
	 * @since 2016-03-30
	 */
	@RequestMapping(value = "/bulletin", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView bulletin2(HttpServletRequest request, HttpSession session) throws ParserConfigurationException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me == null) {
			session.setAttribute("referer", request.getRequestURI());
			return new ModelAndView("redirect:/logIn.asp");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "me", me.toString());
		documentElement.setAttribute("me", me.toString());

		Utils.createElementWithTextContent("name", documentElement, services.getWhoever(me, role, session));

		Element listElement = Utils.createElement("list", documentElement);
		for (Bulletin entity : bulletinRepository.findAll(new Sort(Sort.Direction.DESC, "ordinal"))) {
			Utils.createElementWithTextContentAndAttribute("row", listElement, entity.getFilename(), "id", entity.getId().toString());//列(含主鍵)帶檔案名稱
		}

		ModelAndView modelAndView = new ModelAndView("bulletin");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 問題Q&A
	 *
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/frequentlyAskedQuestion/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView frequentlyAskedQuestion(HttpSession session) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			documentElement.setAttribute("me", me.toString());
		}

		Element listElement = Utils.createElement("list", documentElement);
		for (FrequentlyAskedQuestion entity : frequentlyAskedQuestionRepository.findAll(new Sort("ordinal"))) {
			Element rowElement = Utils.createElementWithAttribute("row", listElement, "id", entity.getId().toString());//列(帶主鍵)
			Utils.createElementWithTextContent("question", rowElement, entity.getQuestion());//問題
			Utils.createElementWithTextContent("answer", rowElement, entity.getAnswer());//回答
			Utils.createElementWithTextContent("consumer", rowElement, Boolean.toString(entity.isConsumer()));//企業|消費者
		}

		ModelAndView modelAndView = new ModelAndView("frequentlyAskedQuestion/default");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 意見回饋
	 *
	 * @param fullname 姓名
	 * @param contactNumber 聯絡電話
	 * @param email 電子信箱
	 * @param textarea 意見回饋
	 * @return JSON 物件
	 */
	@RequestMapping(value = "/feedback.asp", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String feedback(@RequestParam(required = false) String fullname, @RequestParam(required = false) String contactNumber, @RequestParam(required = false) String email, @RequestParam(required = false) String textarea) {
		JSONObject jsonObject = new JSONObject();

		/*
		 姓名
		 */
		try {
			fullname = fullname.trim();
			if (fullname.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception exception) {
			return jsonObject.put("reason", "姓名不得為空！").toString();
		}

		/*
		 聯絡電話
		 */
		try {
			contactNumber = contactNumber.trim();
			if (contactNumber.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception ignore) {
			contactNumber = null;
		}

		/*
		 電子信箱
		 */
		try {
			email = email.trim();
			if (email.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception ignore) {
			email = null;
		}

		/*
		 BJ4
		 */
		if (contactNumber == null && email == null) {
			return jsonObject.put("reason", "聯絡電話或電子信箱須擇一填寫！").toString();
		}

		/*
		 意見回饋
		 */
		try {
			textarea = textarea.trim();
			if (textarea.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception exception) {
			return jsonObject.put("reason", "意見回饋不得為空！").toString();
		}

		StringBuilder stringBuilder = new StringBuilder("<TABLE border='1' cellspacing='3' cellpadding='3'>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>姓名</th>");
		stringBuilder.append("<TD>").append(fullname).append("</TD>");
		stringBuilder.append("</TR>");
		if (contactNumber != null) {
			stringBuilder.append("<TR>");
			stringBuilder.append("<TH>聯絡電話</th>");
			stringBuilder.append("<TD>").append(contactNumber).append("</TD>");
			stringBuilder.append("</TR>");
		}
		if (email != null) {
			stringBuilder.append("<TR>");
			stringBuilder.append("<TH>電子信箱</th>");
			stringBuilder.append("<TD>").append(email).append("</TD>");
			stringBuilder.append("</TR>");
		}
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>意見回饋</th>");
		stringBuilder.append("<TD><PRE>").append(textarea).append("</PRE></TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("</TABLE>");

		HtmlEmail htmlEmail = new HtmlEmail();
		htmlEmail.setHostName("smtp.googlemail.com");
		htmlEmail.setSmtpPort(465);
		htmlEmail.setAuthentication("tourmap.net@gmail.com", "RU; 2k6xu4");
		htmlEmail.setSSLOnConnect(true);
		htmlEmail.setCharset("UTF-8");
		try {
			htmlEmail.setFrom("service@tour-map.net", "「旅途國際」的「意見回饋」", "UTF-8");
			htmlEmail.addTo("backform_oc@tour-map.net");//2016-03-30
			htmlEmail.addCc(new String[]{"backform_cc@tour-map.net", email});//2016-04-07
			htmlEmail.addBcc("backform_bcc@tour-map.net");//2016-03-30
			htmlEmail.setSubject("「意見回饋」來自「旅途國際」");
			htmlEmail.setHtmlMsg(stringBuilder.toString());
			htmlEmail.send();
		} catch (EmailException emailException) {
			return jsonObject.put("reason", emailException.getLocalizedMessage()).toString();
		}

		return jsonObject.put("response", true).toString();
	}

	/**
	 * 連絡我們
	 *
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/contactUs.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView contactUs(HttpSession session) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			documentElement.setAttribute("me", me.toString());
		}

		ModelAndView modelAndView = new ModelAndView("contactUs");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 連絡我們
	 *
	 * @param fullname 姓名
	 * @param company 公司
	 * @param contactNumber 聯絡電話
	 * @param email 電子信箱
	 * @param content 意見回饋
	 * @param session
	 * @return 網頁
	 */
	@RequestMapping(value = "/contactUs.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView contactUs(@RequestParam(required = false) String fullname, @RequestParam(required = false) String company, @RequestParam(required = false) String contactNumber, @RequestParam(required = false) String email, @RequestParam(required = false) String content, HttpSession session) throws ParserConfigurationException {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Integer me = (Integer) session.getAttribute("me");
		if (me != null) {
			documentElement.setAttribute("me", me.toString());
		}

		String errorMessage = null;
		Element formElement = Utils.createElement("form", documentElement);

		/*
		 姓名
		 */
		try {
			fullname = fullname.trim();
			if (fullname.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception exception) {
			fullname = null;
		}
		Utils.createElementWithTextContent("fullname", formElement, fullname == null ? "" : fullname);

		/*
		 公司
		 */
		try {
			company = company.trim();
			if (company.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception ignore) {
			company = null;
		}
		Utils.createElementWithTextContent("company", formElement, company == null ? "" : company);

		/*
		 BJ4
		 */
		if (fullname == null && company == null) {
			errorMessage = "至少需填寫姓名或公司！";
		}

		/*
		 聯絡電話
		 */
		try {
			contactNumber = contactNumber.trim();
			if (contactNumber.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception ignore) {
			contactNumber = null;
		}
		Utils.createElementWithTextContent("phone", formElement, contactNumber == null ? "" : contactNumber);

		/*
		 電子信箱
		 */
		try {
			email = email.trim();
			if (email.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception ignore) {
			email = null;
		}
		Utils.createElementWithTextContent("email", formElement, email == null ? "" : email);

		/*
		 BJ4
		 */
		if (contactNumber == null && email == null) {
			errorMessage = "至少需填寫聯絡電話或電子郵件！";
		}

		/*
		 內容
		 */
		try {
			content = content.trim();
			if (content.isEmpty()) {
				throw new NullPointerException();
			}
		} catch (Exception exception) {
			errorMessage = "連絡內容不得為空！";
			content = null;
		}
		Utils.createElementWithTextContent("content", formElement, content == null ? "" : content);

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);

			ModelAndView modelAndView = new ModelAndView("contactUs");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		StringBuilder stringBuilder = new StringBuilder("<TABLE border='1' cellspacing='3' cellpadding='3'>");
		if (fullname != null) {
			stringBuilder.append("<TR>");
			stringBuilder.append("<TH>姓名</th>");
			stringBuilder.append("<TD>").append(fullname).append("</TD>");
			stringBuilder.append("</TR>");
		}
		if (company != null) {
			stringBuilder.append("<TR>");
			stringBuilder.append("<TH>公司</th>");
			stringBuilder.append("<TD>").append(company).append("</TD>");
			stringBuilder.append("</TR>");
		}
		if (contactNumber != null) {
			stringBuilder.append("<TR>");
			stringBuilder.append("<TH>聯絡電話</th>");
			stringBuilder.append("<TD>").append(contactNumber).append("</TD>");
			stringBuilder.append("</TR>");
		}
		if (email != null) {
			stringBuilder.append("<TR>");
			stringBuilder.append("<TH>電子郵件</th>");
			stringBuilder.append("<TD>").append(email).append("</TD>");
			stringBuilder.append("</TR>");
		}
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>連絡內容</th>");
		stringBuilder.append("<TD><PRE>").append(content).append("</PRE></TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("</TABLE>");

		HtmlEmail htmlEmail = new HtmlEmail();
		htmlEmail.setHostName("smtp.googlemail.com");
		htmlEmail.setSmtpPort(465);
		htmlEmail.setAuthentication("tourmap.net@gmail.com", "RU; 2k6xu4");
		htmlEmail.setSSLOnConnect(true);
		htmlEmail.setCharset("UTF-8");
		try {
			htmlEmail.setFrom("service@tour-map.net", "「旅途國際」的「連絡我們」", "UTF-8");
			htmlEmail.addTo("contactus_oc@tour-map.net");//2016-03-30
			htmlEmail.addCc(new String[]{"contactus_cc@tour-map.net", email});//2016-04-07
			htmlEmail.addBcc("contactus_bcc@tour-map.net");//2016-03-30
			htmlEmail.setSubject("「連絡我們」來自「旅途國際」");
			htmlEmail.setHtmlMsg(stringBuilder.toString());
			htmlEmail.send();
		} catch (EmailException emailException) {
			formElement.setAttribute("errorMessage", emailException.getLocalizedMessage());

			ModelAndView modelAndView = new ModelAndView("contactUs");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		return new ModelAndView("redirect:/");
	}

	/**
	 * 歐付寶：付款結果通知
	 *
	 * @param merchantId 特店(廠商)編號
	 * @param merchantTradeNo 交易編號
	 * @param rtnCode 交易狀態
	 * @param rtnMsg 交易訊息
	 * @param tradeNo 交易編號
	 * @param tradeAmt 交易金額
	 * @param paymentDate 付款時間
	 * @param paymentType 會員選擇的付款方式
	 * @param paymentTypeChargeFee 通路費
	 * @param tradeDate 訂單成立時間
	 * @param simulatePaid 是否為模擬付款
	 * @param checkMacValue 檢查碼
	 * @return 純文字
	 */
	@RequestMapping(value = "/receivable.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String receivable(@RequestParam(name = "MerchantID", defaultValue = "") String merchantId, @RequestParam(name = "MerchantTradeNo", defaultValue = "") String merchantTradeNo, @RequestParam(name = "RtnCode", required = false) Short rtnCode, @RequestParam(name = "RtnMsg", required = false) String rtnMsg, @RequestParam(name = "TradeNo", required = false) String tradeNo, @RequestParam(name = "TradeAmt", required = false) Integer tradeAmt, @RequestParam(name = "PaymentDate", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss") Date paymentDate, @RequestParam(name = "PaymentType", required = false) String paymentType, @RequestParam(name = "PaymentTypeChargeFee", required = false) Integer paymentTypeChargeFee, @RequestParam(name = "TradeDate", required = false) @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss") Date tradeDate, @RequestParam(name = "SimulatePaid", required = false) Short simulatePaid, @RequestParam(name = "CheckMacValue", defaultValue = "") String checkMacValue/*, HttpServletRequest request*/) throws UnsupportedEncodingException {
		//for (Map.Entry<String, String[]> entrySet : request.getParameterMap().entrySet()) {
		//	String key = entrySet.getKey();
		//	System.out.print(key + "=");
		//	boolean isFirst = true;
		//	for (String value : entrySet.getValue()) {
		//		System.out.print(value + (isFirst ? "" : ","));
		//		if (isFirst) {
		//			isFirst = false;
		//		}
		//	}
		//	System.out.println();
		//}

		ResourceBundle resourceBundle = ResourceBundle.getBundle("allPay");
		if (!merchantId.equals(resourceBundle.getString("MerchantID"))) {
			return "0|錯誤的特店(廠商)編號！";
		}

		AllPayHistory allPayHistory = allPayHistoryRepository.findOneByMerchantTradeNo(merchantTradeNo);
		if (allPayHistory == null) {
			return "0|找不到相對應的交易編號！";
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Map<String, String> parameterMap = new TreeMap<>();
		parameterMap.put("MerchantID", merchantId);
		parameterMap.put("MerchantTradeNo", merchantTradeNo);
		parameterMap.put("RtnCode", rtnCode.toString());
		parameterMap.put("RtnMsg", rtnMsg);
		parameterMap.put("TradeNo", tradeNo);
		parameterMap.put("TradeAmt", tradeAmt.toString());
		parameterMap.put("PaymentDate", simpleDateFormat.format(paymentDate));
		parameterMap.put("PaymentType", paymentType);
		parameterMap.put("PaymentTypeChargeFee", paymentTypeChargeFee.toString());
		parameterMap.put("TradeDate", simpleDateFormat.format(tradeDate));
		parameterMap.put("SimulatePaid", simulatePaid.toString());
		StringBuilder stringBuilder = new StringBuilder("HashKey=" + resourceBundle.getString("HashKey"));
		for (Map.Entry<String, String> entrySet : parameterMap.entrySet()) {
			stringBuilder.append("&").append(entrySet.getKey()).append("=").append(entrySet.getValue());
		}
		stringBuilder.append("&HashIV=").append(resourceBundle.getString("HashIV"));
		String returnCheckMacValue = Utils.md5(URLEncoder.encode(stringBuilder.toString(), "UTF-8").toLowerCase()).toUpperCase();
		if (!checkMacValue.equals(returnCheckMacValue)) {
			return "0|錯誤的檢查碼！";
		}

		allPayHistory.setRtnCode(rtnCode);
		allPayHistory.setRtnMsg(rtnMsg);
		allPayHistory.setTradeNo(tradeNo);
		allPayHistory.setTradeAmt(tradeAmt);
		allPayHistory.setPaymentDate(paymentDate);
		allPayHistory.setPaymentType(paymentType);
		allPayHistory.setPaymentTypeChargeFee(paymentTypeChargeFee);
		allPayHistory.setTradeDate(tradeDate);
		allPayHistory.setSimulatePaid(simulatePaid);
		allPayHistory.setReturnCheckMacValue(returnCheckMacValue);
		allPayHistoryRepository.saveAndFlush(allPayHistory);

		//TODO:	根據allPayHistory.getAccount(收費項目)調整已繳
		return "1|OK";
	}
}
