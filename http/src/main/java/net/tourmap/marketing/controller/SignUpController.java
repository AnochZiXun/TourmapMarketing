package net.tourmap.marketing.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.repository.CKEditorRepository;
import net.tourmap.marketing.service.Services;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author	P-C Lin (a.k.a ???????????????)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/signUp")
public class SignUpController {

	@Autowired
	private CKEditorRepository ckeditorRepository;

	@Autowired
	private Services services;

	private final String EMAIL_ADDRESS = "service@tour-map.net";

	//private final int MAX_IMAGE_UPLOAD_SIZE = 5242880;
	private final int MAX_IMAGE_UPLOAD_SIZE = 524288;

	/**
	 * ??????????????????
	 *
	 * @param session
	 * @return ??????
	 */
	@RequestMapping(value = "/vendor.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView vendor(HttpSession session) throws IOException, ParserConfigurationException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me != null) {
			if (role != null && "vendor".equals(role)) {
				return new ModelAndView("redirect:/vendor/");
			}
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Element formElement = Utils.createElement("form", documentElement);
		String errorMessage = null;

		Utils.createElementWithTextContent("step1Html", documentElement, ckeditorRepository.findOne((short) 1).getMarkup());
		//Path path1st = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor1st.htm");
		//StringBuilder stringBuilder1st = new StringBuilder();
		//for (String line : Files.readAllLines(path1st, Charset.forName("UTF-8"))) {
		//	stringBuilder1st.append(line);
		//}
		//Utils.createElementWithTextContent("step1Html", documentElement, stringBuilder1st.toString());

		Utils.createElementWithTextContent("step2Html", documentElement, ckeditorRepository.findOne((short) 2).getMarkup());
		//Path path2nd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor2nd.htm");
		//StringBuilder stringBuilder2nd = new StringBuilder();
		//for (String line : Files.readAllLines(path2nd, Charset.forName("UTF-8"))) {
		//	stringBuilder2nd.append(line);
		//}
		//Utils.createElementWithTextContent("step2Html", documentElement, stringBuilder2nd.toString());

		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/category/");
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					Element categoryElement = Utils.createElement("category", formElement);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject category = jsonArray.getJSONObject(i);
						Utils.createElementWithTextContentAndAttribute("option", categoryElement, category.getString("name"), "value", Integer.toString(category.getInt("id")));
					}
				} catch (JSONException jsonException) {
					errorMessage = jsonException.getLocalizedMessage();
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			errorMessage = ioException.getLocalizedMessage();
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
		}

		ModelAndView modelAndView = new ModelAndView("signUp/vendorFirst");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * ??????????????????||????????????????????????
	 *
	 * @param name ????????????
	 * @param businessIdentificationNumber ??????
	 * @param phone ??????
	 * @param fax ??????
	 * @param address ????????????
	 * @param contact ?????????
	 * @param email ????????????
	 * @param categoryId ??????????????????
	 * @param shadow1 ????????????
	 * @param shadow2 ????????????
	 * @param multipartFile1 ?????????????????????
	 * @param multipartFile2 ??????????????????????????????
	 * @param multipartFile3 ??????????????????????????????
	 * @param session
	 * @return ??????
	 */
	@RequestMapping(value = "/vendor.old.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView vendor0(@RequestParam String name, @RequestParam String businessIdentificationNumber, @RequestParam String phone, @RequestParam(defaultValue = "") String fax, @RequestParam(defaultValue = "") String address, @RequestParam String contact, @RequestParam String email, @RequestParam(name = "category") Integer categoryId, @RequestParam String shadow1, @RequestParam String shadow2, @RequestParam(name = "certificateOfProfitSeekingEnterprise", required = false) MultipartFile multipartFile1, @RequestParam(name = "personInCharge", required = false) MultipartFile multipartFile2, @RequestParam(name = "personInCharge_", required = false) MultipartFile multipartFile3, HttpSession session) throws EmailException, IOException, ParserConfigurationException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me != null) {
			if (role != null && "vendor".equals(role)) {
				return new ModelAndView("redirect:/vendor/");
			}
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Element formElement = Utils.createElement("form", documentElement);
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = null;
		String errorMessage = null;

		Utils.createElementWithTextContent("step1Html", documentElement, ckeditorRepository.findOne((short) 1).getMarkup());
		//Path path1st = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor1st.htm");
		//StringBuilder stringBuilder1st = new StringBuilder();
		//for (String line : Files.readAllLines(path1st, Charset.forName("UTF-8"))) {
		//	stringBuilder1st.append(line);
		//}
		//Utils.createElementWithTextContent("step1Html", documentElement, stringBuilder1st.toString());

		Utils.createElementWithTextContent("step2Html", documentElement, ckeditorRepository.findOne((short) 2).getMarkup());
		//Path path2nd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor2nd.htm");
		//StringBuilder stringBuilder2nd = new StringBuilder();
		//for (String line : Files.readAllLines(path2nd, Charset.forName("UTF-8"))) {
		//	stringBuilder2nd.append(line);
		//}
		//Utils.createElementWithTextContent("step2Html", documentElement, stringBuilder2nd.toString());

		/*
		 ????????????
		 */
		name = name.trim();
		if (name.isEmpty()) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("name", formElement, name);

		/*
		 ??????
		 */
		businessIdentificationNumber = businessIdentificationNumber.trim();
		if (businessIdentificationNumber.isEmpty()) {
			errorMessage = "?????????????????????";
		} else if (!businessIdentificationNumber.matches("^(\\d{8})$")) {
			errorMessage = "??????????????????";
		} else if (services.hasVendorBusinessIdentificationNumber(businessIdentificationNumber)) {
			errorMessage = "?????????????????????";
		}
		Utils.createElementWithTextContent("businessIdentificationNumber", formElement, businessIdentificationNumber);

		/*
		 ??????
		 */
		phone = phone.trim();
		if (phone.isEmpty()) {
			errorMessage = "?????????????????????";
		}
		Utils.createElementWithTextContent("phone", formElement, phone);

		/*
		 ??????
		 */
		if (fax.isEmpty()) {
			fax = null;
		} else {
			Utils.createElementWithTextContent("fax", formElement, fax);
		}

		/*
		 ??????
		 */
		if (address.isEmpty()) {
			address = null;
		} else {
			Utils.createElementWithTextContent("address", formElement, address);
		}

		/*
		 ?????????
		 */
		contact = contact.trim();
		if (contact.isEmpty()) {
			errorMessage = "????????????????????????";
		}
		Utils.createElementWithTextContent("contact", formElement, contact);

		/*
		 ????????????
		 */
		email = email.trim().toLowerCase();
		if (email.isEmpty()) {
			errorMessage = "???????????????????????????";
		} else if (services.hasLogin(email)) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("email", formElement, email);

		/*
		 ??????????????????
		 */
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/category/");
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					Element categoryElement = Utils.createElement("category", formElement);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject category = jsonArray.getJSONObject(i);
						Integer id = category.getInt("id");
						Element optionElement = Utils.createElementWithTextContentAndAttribute("option", categoryElement, category.getString("name"), "value", id.toString());
						if (Objects.equals(categoryId, id)) {
							optionElement.setAttribute("selected", null);
						}
					}
				} catch (JSONException jsonException) {
					errorMessage = jsonException.getLocalizedMessage();
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			errorMessage = ioException.getLocalizedMessage();
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		/*
		 ????????????
		 */
		shadow1 = shadow1.trim();
		if (shadow1.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		/*
		 ????????????
		 */
		shadow2 = shadow2.trim();
		if (shadow2.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		if (!shadow1.equals(shadow2)) {
			errorMessage = "????????????????????????????????????";
		}

		/*
		 ?????????????????????
		 */
		byte[] certificateOfProfitSeekingEnterprise = null;
		int certificateOfProfitSeekingEnterpriseLength = 0;
		if (multipartFile1 != null) {
			try {
				certificateOfProfitSeekingEnterprise = multipartFile1.getBytes();
				certificateOfProfitSeekingEnterpriseLength = certificateOfProfitSeekingEnterprise.length;
				if (certificateOfProfitSeekingEnterpriseLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge = null;
		int personInChargeLength = 0;
		if (multipartFile2 != null) {
			try {
				personInCharge = multipartFile2.getBytes();
				personInChargeLength = personInCharge.length;
				if (personInChargeLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge_ = null;
		int personInChargeLength_ = 0;
		if (multipartFile3 != null) {
			try {
				personInCharge_ = multipartFile3.getBytes();
				personInChargeLength_ = personInCharge_.length;
				if (personInChargeLength_ > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
			Utils.createElement("hideStep1", documentElement);

			ModelAndView modelAndView = new ModelAndView("signUp/vendorFirst");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("businessIdentificationNumber", businessIdentificationNumber);
		jsonObject.put("phone", phone);
		jsonObject.put("fax", fax);
		jsonObject.put("address", address);
		jsonObject.put("contact", contact);
		jsonObject.put("email", email);
		jsonObject.put("category", categoryId);
		jsonObject.put("shadow", shadow1);

		HttpPost httpPost = new HttpPost("http://restful.tour-map.net/vendor/add.json");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("json", jsonObject.toString()));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
			closeableHttpResponse = closeableHttpClient.execute(httpPost);
//			HttpEntity httpEntity = closeableHttpResponse.getEntity();
//			if (httpEntity != null) {
//				InputStream inputStream = null;
//				try {
//					inputStream = httpEntity.getContent();
//					Utils.createElementWithTextContent("step3Html", documentElement, IOUtils.toString(inputStream, Charset.forName("UTF-8")));
//				} finally {
//					IOUtils.closeQuietly(inputStream);
//				}
//			}
			if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
				Utils.createElementWithTextContent("errorMessage", documentElement, "???????????????????????????????????????");
				Utils.createElement("hideStep1", documentElement);

				ModelAndView modelAndView = new ModelAndView("signUp/vendorFirst");
				modelAndView.getModelMap().addAttribute(new DOMSource(document));
				return modelAndView;
			}
			Utils.createElementWithTextContent("step3Html", documentElement, ckeditorRepository.findOne((short) 3).getMarkup());
			//Path path3rd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor3rd.htm");
			//StringBuilder stringBuilder3rd = new StringBuilder();
			//for (String line : Files.readAllLines(path3rd, Charset.forName("UTF-8"))) {
			//	stringBuilder3rd.append(line);
			//}
			//Utils.createElementWithTextContent("step3Html", documentElement, stringBuilder3rd.toString());
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + "\n:\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		try {
			closeableHttpClient.close();
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + "\n:\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpClient);
		}

		MultiPartEmail multiPartEmail = new HtmlEmail();
		multiPartEmail.setHostName("smtp.googlemail.com");
		multiPartEmail.setSmtpPort(465);
		multiPartEmail.setAuthentication("tourmap.net@gmail.com", "RU; 2k6xu4");
		multiPartEmail.setSSLOnConnect(true);
		multiPartEmail.setCharset("UTF-8");
		multiPartEmail.setFrom(EMAIL_ADDRESS, "?????????", "UTF-8");
		multiPartEmail.addTo("vendor_oc@tour-map.net");//2016-03-30
		multiPartEmail.addCc(new String[]{"vendor_cc@tour-map.net", email});//2016-04-07
		multiPartEmail.addBcc("vendor_bcc@tour-map.net");//2016-03-30
		multiPartEmail.setSubject("???????????????????????????????????????");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<TABLE style='border-collapse:separate' border='1' cellspacing='6' cellpadding='6'>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(name).append("</TD>");
		stringBuilder.append("<TH>??????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(businessIdentificationNumber).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>??????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(phone).append("</TD>");
		stringBuilder.append("<TH>??????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(fax == null ? "" : fax).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(address).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>?????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(contact).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(email).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("</TABLE>");
		multiPartEmail.setMsg(stringBuilder.toString());

		if (certificateOfProfitSeekingEnterprise != null && certificateOfProfitSeekingEnterpriseLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(certificateOfProfitSeekingEnterprise);

			Path path = Paths.get(services.getContextRealPath(), multipartFile1.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		if (personInCharge != null && personInChargeLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge);

			Path path = Paths.get(services.getContextRealPath(), multipartFile2.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		if (personInCharge_ != null && personInChargeLength_ > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge_);

			Path path = Paths.get(services.getContextRealPath(), multipartFile2.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		multiPartEmail.send();

		ModelAndView modelAndView = new ModelAndView("signUp/vendorLast");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * ??????????????????||????????????????????????
	 *
	 * @param name ????????????
	 * @param businessIdentificationNumber ??????
	 * @param phone ??????
	 * @param fax ??????
	 * @param address ????????????
	 * @param contact ?????????
	 * @param email ????????????
	 * @param categoryId ??????????????????
	 * @param shadow1 ????????????
	 * @param shadow2 ????????????
	 * @param multipartFile1 ?????????????????????
	 * @param multipartFile2 ??????????????????????????????
	 * @param multipartFile3 ??????????????????????????????
	 * @param multipartFile4 ??????????????????????????????
	 * @param session
	 * @return ??????
	 * @since 2016-04-06
	 */
	@RequestMapping(value = "/vendor.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@SuppressWarnings("ConvertToTryWithResources")
	protected ModelAndView vendor(@RequestParam String name, @RequestParam String businessIdentificationNumber, @RequestParam String phone, @RequestParam(defaultValue = "") String fax, @RequestParam(defaultValue = "") String address, @RequestParam String contact, @RequestParam String email, @RequestParam(name = "category") Integer categoryId, @RequestParam String shadow1, @RequestParam String shadow2, @RequestParam(name = "certificateOfProfitSeekingEnterprise", required = false) MultipartFile multipartFile1, @RequestParam(name = "personInCharge", required = false) MultipartFile multipartFile2, @RequestParam(name = "personInCharge_", required = false) MultipartFile multipartFile3, @RequestParam(name = "statementCover", required = false) MultipartFile multipartFile4, HttpSession session) throws EmailException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me != null) {
			if (role != null && "vendor".equals(role)) {
				return new ModelAndView("redirect:/vendor/");
			}
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Element formElement = Utils.createElement("form", documentElement);
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = null;
		String errorMessage = null;

		Utils.createElementWithTextContent("step1Html", documentElement, ckeditorRepository.findOne((short) 1).getMarkup());

		Utils.createElementWithTextContent("step2Html", documentElement, ckeditorRepository.findOne((short) 2).getMarkup());

		/*
		 ????????????
		 */
		name = name.trim();
		if (name.isEmpty()) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("name", formElement, name);

		/*
		 ??????
		 */
		businessIdentificationNumber = businessIdentificationNumber.trim();
		if (businessIdentificationNumber.isEmpty()) {
			errorMessage = "?????????????????????";
		} else if (!businessIdentificationNumber.matches("^(\\d{8})$")) {
			errorMessage = "??????????????????";
		} else if (services.hasVendorBusinessIdentificationNumber(businessIdentificationNumber)) {
			errorMessage = "?????????????????????";
		}
		Utils.createElementWithTextContent("businessIdentificationNumber", formElement, businessIdentificationNumber);

		/*
		 ??????
		 */
		phone = phone.trim();
		if (phone.isEmpty()) {
			errorMessage = "?????????????????????";
		}
		Utils.createElementWithTextContent("phone", formElement, phone);

		/*
		 ??????
		 */
		if (fax.isEmpty()) {
			fax = null;
		} else {
			Utils.createElementWithTextContent("fax", formElement, fax);
		}

		/*
		 ??????
		 */
		if (address.isEmpty()) {
			address = null;
		} else {
			Utils.createElementWithTextContent("address", formElement, address);
		}

		/*
		 ?????????
		 */
		contact = contact.trim();
		if (contact.isEmpty()) {
			errorMessage = "????????????????????????";
		}
		Utils.createElementWithTextContent("contact", formElement, contact);

		/*
		 ????????????
		 */
		email = email.trim().toLowerCase();
		if (email.isEmpty()) {
			errorMessage = "???????????????????????????";
		} else if (services.hasLogin(email)) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("email", formElement, email);

		/*
		 ??????????????????
		 */
		String categoryString = null;
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/category/");
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					Element categoryElement = Utils.createElement("category", formElement);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject category = jsonArray.getJSONObject(i);
						Integer id = category.getInt("id");
						Element optionElement = Utils.createElementWithTextContentAndAttribute("option", categoryElement, category.getString("name"), "value", id.toString());
						if (Objects.equals(categoryId, id)) {
							optionElement.setAttribute("selected", null);
							categoryString = category.getString("name");
						}
					}
				} catch (JSONException jsonException) {
					errorMessage = jsonException.getLocalizedMessage();
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			errorMessage = ioException.getLocalizedMessage();
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		/*
		 ????????????
		 */
		shadow1 = shadow1.trim();
		if (shadow1.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		/*
		 ????????????
		 */
		shadow2 = shadow2.trim();
		if (shadow2.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		if (!shadow1.equals(shadow2)) {
			errorMessage = "????????????????????????????????????";
		}

		/*
		 ?????????????????????
		 */
		byte[] certificateOfProfitSeekingEnterprise = null;
		int certificateOfProfitSeekingEnterpriseLength = 0;
		if (multipartFile1 != null) {
			try {
				certificateOfProfitSeekingEnterprise = multipartFile1.getBytes();
				certificateOfProfitSeekingEnterpriseLength = certificateOfProfitSeekingEnterprise.length;
				if (certificateOfProfitSeekingEnterpriseLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge = null;
		int personInChargeLength = 0;
		if (multipartFile2 != null) {
			try {
				personInCharge = multipartFile2.getBytes();
				personInChargeLength = personInCharge.length;
				if (personInChargeLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge_ = null;
		int personInChargeLength_ = 0;
		if (multipartFile3 != null) {
			try {
				personInCharge_ = multipartFile3.getBytes();
				personInChargeLength_ = personInCharge_.length;
				if (personInChargeLength_ > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] statementCover = null;
		int statementCoverLength = 0;
		if (multipartFile4 != null) {
			try {
				statementCover = multipartFile4.getBytes();
				statementCoverLength = statementCover.length;
				if (statementCoverLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
			Utils.createElement("hideStep1", documentElement);

			ModelAndView modelAndView = new ModelAndView("signUp/vendorFirst");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		MultiPartEmail multiPartEmail = new HtmlEmail();
		multiPartEmail.setHostName("smtp.googlemail.com");
		multiPartEmail.setSmtpPort(465);
		multiPartEmail.setAuthentication("tourmap.net@gmail.com", "RU; 2k6xu4");
		multiPartEmail.setSSLOnConnect(true);
		multiPartEmail.setCharset("UTF-8");
		multiPartEmail.setFrom(EMAIL_ADDRESS, "?????????", "UTF-8");
		multiPartEmail.addTo("vendor_oc@tour-map.net");//2016-03-30
		multiPartEmail.addCc(new String[]{"vendor_cc@tour-map.net", email});//2016-04-07
		multiPartEmail.addBcc("vendor_bcc@tour-map.net");//2016-03-30
		multiPartEmail.setSubject("???????????????????????????????????????");

		/*
		 ????????????????????????????????????
		 */
		if (certificateOfProfitSeekingEnterprise != null && certificateOfProfitSeekingEnterpriseLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(certificateOfProfitSeekingEnterprise);

			Path path = Paths.get(services.getContextRealPath(), multipartFile1.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		/*
		 ?????????????????????????????????????????????
		 */
		if (personInCharge != null && personInChargeLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge);

			Path path = Paths.get(services.getContextRealPath(), multipartFile2.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		/*
		 ?????????????????????????????????????????????
		 */
		if (personInCharge_ != null && statementCoverLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge_);

			Path path = Paths.get(services.getContextRealPath(), multipartFile3.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		/*
		 ?????????????????????????????????????????????
		 */
		if (statementCover != null && statementCoverLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(statementCover);

			Path path = Paths.get(services.getContextRealPath(), multipartFile4.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		/*
		 2016-05-04	?????????????????????????????????????????????

		 StringBuilder stringBuilder = new StringBuilder();
		 stringBuilder.append("<TABLE style='border-collapse:separate' border='1' cellspacing='6' cellpadding='6'>");
		 stringBuilder.append("<TR>");
		 stringBuilder.append("<TH>????????????</TH>");
		 stringBuilder.append("<TD style='color:#EF7E00'>").append(name).append("</TD>");
		 stringBuilder.append("<TH>??????</TH>");
		 stringBuilder.append("<TD style='color:#EF7E00'>").append(businessIdentificationNumber).append("</TD>");
		 stringBuilder.append("</TR>");
		 stringBuilder.append("<TR>");
		 stringBuilder.append("<TH>??????</TH>");
		 stringBuilder.append("<TD style='color:#EF7E00'>").append(phone).append("</TD>");
		 stringBuilder.append("<TH>??????</TH>");
		 stringBuilder.append("<TD style='color:#EF7E00'>").append(fax == null ? "" : fax).append("</TD>");
		 stringBuilder.append("</TR>");
		 stringBuilder.append("<TR>");
		 stringBuilder.append("<TH>????????????</TH>");
		 stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(address).append("</TD>");
		 stringBuilder.append("</TR>");
		 stringBuilder.append("<TR>");
		 stringBuilder.append("<TH>?????????</TH>");
		 stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(contact).append("</TD>");
		 stringBuilder.append("</TR>");
		 stringBuilder.append("<TR>");
		 stringBuilder.append("<TH>????????????</TH>");
		 stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(email).append("</TD>");
		 stringBuilder.append("</TR>");
		 stringBuilder.append("</TABLE>");
		 multiPartEmail.setMsg(stringBuilder.toString()).send();
		 */

		/*
		 2016-05-04	????????????XSL(T)??????????????????
		 */
		Document documentEmail = Utils.newDocument();
		Element elementMessageBody = Utils.createElement("messageBody", documentEmail);
		Utils.createElementWithTextContent("name", elementMessageBody, name);
		Utils.createElementWithTextContent("address", elementMessageBody, address);
		Utils.createElementWithTextContent("businessIdentificationNumber", elementMessageBody, businessIdentificationNumber);
		Utils.createElementWithTextContent("contact", elementMessageBody, contact);
		Utils.createElementWithTextContent("phone", elementMessageBody, phone);
		Utils.createElementWithTextContent("fax", elementMessageBody, fax);
		Utils.createElementWithTextContent("email", elementMessageBody, email);
		Utils.createElementWithTextContent("shadow", elementMessageBody, shadow1);
		Utils.createElementWithTextContent("category", elementMessageBody, categoryString);
		StringWriter stringWriter = new StringWriter();
		TransformerFactory.newInstance().newTransformer(new StreamSource(getClass().getResourceAsStream("/welcomeVendor.xsl"))).transform(new DOMSource(documentEmail), new StreamResult(stringWriter));
		stringWriter.flush();
		stringWriter.close();
		multiPartEmail.setMsg(stringWriter.toString()).send();

		Utils.createElementWithTextContent("step3Html", documentElement, ckeditorRepository.findOne((short) 3).getMarkup());

		ModelAndView modelAndView = new ModelAndView("signUp/vendorLast");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * ??????????????????
	 *
	 * @param session
	 * @return ??????
	 */
	@RequestMapping(value = "/agent.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView agent(HttpSession session) throws IOException, ParserConfigurationException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me != null) {
			if (role != null && "agent".equals(role)) {
				return new ModelAndView("redirect:/agent/");
			}
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Element formElement = Utils.createElement("form", documentElement);
		String errorMessage = null;

		Utils.createElementWithTextContent("step1Html", documentElement, ckeditorRepository.findOne((short) 4).getMarkup());
		//Path path1st = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent1st.htm");
		//StringBuilder stringBuilder1st = new StringBuilder();
		//for (String line : Files.readAllLines(path1st, Charset.forName("UTF-8"))) {
		//	stringBuilder1st.append(line);
		//}
		//Utils.createElementWithTextContent("step1Html", documentElement, stringBuilder1st.toString());

		Utils.createElementWithTextContent("supervisor", formElement, "true");

		Utils.createElementWithTextContent("step2Html", documentElement, ckeditorRepository.findOne((short) 5).getMarkup());
		//Path path2nd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent2nd.htm");
		//StringBuilder stringBuilder2nd = new StringBuilder();
		//for (String line : Files.readAllLines(path2nd, Charset.forName("UTF-8"))) {
		//	stringBuilder2nd.append(line);
		//}
		//Utils.createElementWithTextContent("step2Html", documentElement, stringBuilder2nd.toString());

		Utils.createElementWithTextContent("individual", formElement, "true");

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
		}

		ModelAndView modelAndView = new ModelAndView("signUp/agentFirst");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * ??????????????????||????????????????????????
	 *
	 * @param individual (??????|??????)??????
	 * @param name ??????????????????
	 * @param cellular ????????????
	 * @param address ??????
	 * @param email ????????????
	 * @param industry ???????????????
	 * @param shadow1 ????????????
	 * @param shadow2 ????????????
	 * @param multipartFile1 ?????????????????????
	 * @param multipartFile2 ??????????????????????????????
	 * @param multipartFile3 ??????????????????????????????
	 * @param supervisorString ????????????
	 * @param session
	 * @return ??????
	 */
	@RequestMapping(value = "/agent.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView agent(@RequestParam Boolean individual, @RequestParam String name, @RequestParam String cellular, @RequestParam(required = false) String address, @RequestParam String email, @RequestParam(required = false) String industry, @RequestParam String shadow1, @RequestParam String shadow2, @RequestParam(name = "certificateOfProfitSeekingEnterprise", required = false) MultipartFile multipartFile1, @RequestParam(name = "personInCharge", required = false) MultipartFile multipartFile2, @RequestParam(name = "personInCharge_", required = false) MultipartFile multipartFile3, @RequestParam(name = "supervisor", required = false) String supervisorString, HttpSession session) throws EmailException, IOException, ParserConfigurationException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me != null) {
			if (role != null && "agent".equals(role)) {
				return new ModelAndView("redirect:/agent/");
			}
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Element formElement = Utils.createElement("form", documentElement);
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = null;
		String errorMessage = null;

		Utils.createElementWithTextContent("step1Html", documentElement, ckeditorRepository.findOne((short) 4).getMarkup());
		//Path path1st = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent1st.htm");
		//StringBuilder stringBuilder1st = new StringBuilder();
		//for (String line : Files.readAllLines(path1st, Charset.forName("UTF-8"))) {
		//	stringBuilder1st.append(line);
		//}
		//Utils.createElementWithTextContent("step1Html", documentElement, stringBuilder1st.toString());

		Utils.createElementWithTextContent("step2Html", documentElement, ckeditorRepository.findOne((short) 5).getMarkup());
		//Path path2nd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent2nd.htm");
		//StringBuilder stringBuilder2nd = new StringBuilder();
		//for (String line : Files.readAllLines(path2nd, Charset.forName("UTF-8"))) {
		//	stringBuilder2nd.append(line);
		//}
		//Utils.createElementWithTextContent("step2Html", documentElement, stringBuilder2nd.toString());

		/*
		 (??????|??????)??????
		 */
		Utils.createElementWithTextContent("individual", formElement, individual.toString());

		/*
		 ??????????????????
		 */
		name = name.trim();
		if (name.isEmpty()) {
			errorMessage = "?????????????????????????????????";
		}
		Utils.createElementWithTextContent("name", formElement, name);

		/*
		 ????????????
		 */
		cellular = cellular.trim();
		if (cellular.isEmpty()) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("cellular", formElement, cellular);

		/*
		 ??????
		 */
		if (address != null) {
			Utils.createElementWithTextContent("address", formElement, address);
		}

		/*
		 ????????????
		 */
		email = email.trim().toLowerCase();
		if (email.isEmpty()) {
			errorMessage = "???????????????????????????";
		} else if (services.hasLogin(email)) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("email", formElement, email);

		/*
		 ???????????????
		 */
		if (industry.isEmpty()) {
			industry = null;
		} else {
			Utils.createElementWithTextContent("industry", formElement, industry);
		}

		/*
		 ????????????
		 */
		shadow1 = shadow1.trim();
		if (shadow1.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		/*
		 ????????????
		 */
		shadow2 = shadow2.trim();
		if (shadow2.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		if (!shadow1.equals(shadow2)) {
			errorMessage = "????????????????????????????????????";
		}

		/*
		 ?????????????????????
		 */
		byte[] certificateOfProfitSeekingEnterprise = null;
		int certificateOfProfitSeekingEnterpriseLength = 0;
		if (multipartFile1 != null) {
			try {
				certificateOfProfitSeekingEnterprise = multipartFile1.getBytes();
				certificateOfProfitSeekingEnterpriseLength = certificateOfProfitSeekingEnterprise.length;
				if (certificateOfProfitSeekingEnterpriseLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge = null;
		int personInChargeLength = 0;
		if (multipartFile2 != null) {
			try {
				personInCharge = multipartFile2.getBytes();
				personInChargeLength = personInCharge.length;
				if (personInChargeLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge_ = null;
		int personInChargeLength_ = 0;
		if (multipartFile3 != null) {
			try {
				personInCharge_ = multipartFile3.getBytes();
				personInChargeLength_ = personInCharge_.length;
				if (personInChargeLength_ > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ????????????
		 */
		Boolean supervisor = null;
		if (supervisorString == null) {
			errorMessage = "????????????????????????";
		} else {
			supervisor = Boolean.parseBoolean(supervisorString);
			Utils.createElementWithTextContent("supervisor", formElement, supervisor.toString());
		}

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
			Utils.createElement("hideStep1", documentElement);

			ModelAndView modelAndView = new ModelAndView("signUp/agentFirst");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
			Utils.createElement("hideStep1", documentElement);

			ModelAndView modelAndView = new ModelAndView("signUp/agentFirst");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("cellular", cellular);
		jsonObject.put("address", address);
		jsonObject.put("email", email);
		jsonObject.put("industry", industry);
		jsonObject.put("shadow", shadow1);
		//jsonObject.put("supervisor", supervisor);

		HttpPost httpPost = new HttpPost("http://restful.tour-map.net/agent/add.json");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("json", jsonObject.toString()));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
			closeableHttpResponse = closeableHttpClient.execute(httpPost);
			if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
				StatusLine statusLine = closeableHttpResponse.getStatusLine();
				System.err.println(getClass().getCanonicalName() + ":\t" + statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase());//2016-03-10
				Utils.createElementWithTextContent("errorMessage", documentElement, "???????????????????????????????????????");
				Utils.createElement("hideStep1", documentElement);

				ModelAndView modelAndView = new ModelAndView("signUp/agentFirst");
				modelAndView.getModelMap().addAttribute(new DOMSource(document));
				return modelAndView;
			}
			Utils.createElementWithTextContent("step3Html", documentElement, ckeditorRepository.findOne((short) 6).getMarkup());
			//Path path3rd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent3rd.htm");
			//StringBuilder stringBuilder3rd = new StringBuilder();
			//for (String line : Files.readAllLines(path3rd, Charset.forName("UTF-8"))) {
			//	stringBuilder3rd.append(line);
			//}
			//Utils.createElementWithTextContent("step3Html", documentElement, stringBuilder3rd.toString());
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + "\n:\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		try {
			closeableHttpClient.close();
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + "\n:\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpClient);
		}

		MultiPartEmail multiPartEmail = new HtmlEmail();
		multiPartEmail.setHostName("smtp.googlemail.com");
		multiPartEmail.setSmtpPort(465);
		multiPartEmail.setAuthentication("tourmap.net@gmail.com", "RU; 2k6xu4");
		multiPartEmail.setSSLOnConnect(true);
		multiPartEmail.setCharset("UTF-8");
		multiPartEmail.setFrom(EMAIL_ADDRESS, "?????????", "UTF-8");
		multiPartEmail.addTo("agent_oc@tour-map.net");//2016-03-30
		multiPartEmail.addCc(new String[]{"agent_cc@tour-map.net", email});//2016-04-07
		multiPartEmail.addBcc("agent_bcc@tour-map.net");//2016-03-30
		multiPartEmail.setSubject("???????????????????????????????????????");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<TABLE style='border-collapse:separate' border='1' cellspacing='6' cellpadding='6'>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>??????????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(name).append("</TD>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(cellular).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>??????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(address == null ? "" : address).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(email).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>???????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(industry == null ? "" : industry).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(supervisor ? "A(????????????)" : "B(????????????)").append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("</TABLE>");
		multiPartEmail.setMsg(stringBuilder.toString());

		if (certificateOfProfitSeekingEnterprise != null && certificateOfProfitSeekingEnterpriseLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(certificateOfProfitSeekingEnterprise);

			Path path = Paths.get(services.getContextRealPath(), multipartFile1.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		if (personInCharge != null && personInChargeLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge);

			Path path = Paths.get(services.getContextRealPath(), multipartFile2.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		if (personInCharge_ != null && personInChargeLength_ > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge_);

			Path path = Paths.get(services.getContextRealPath(), multipartFile3.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		multiPartEmail.send();

		ModelAndView modelAndView = new ModelAndView("signUp/agentLast");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * ??????????????????
	 *
	 * @param session
	 * @return ??????
	 */
	@RequestMapping(value = "/advertiser.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView advertiser(HttpSession session) throws IOException, ParserConfigurationException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me != null) {
			if (role != null && "advertiser".equals(role)) {
				return new ModelAndView("redirect:/advertiser/");
			}
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Element formElement = Utils.createElement("form", documentElement);
		String errorMessage = null;

		Utils.createElementWithTextContent("step1Html", documentElement, ckeditorRepository.findOne((short) 7).getMarkup());
		//Path path1st = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser1st.htm");
		//StringBuilder stringBuilder1st = new StringBuilder();
		//for (String line : Files.readAllLines(path1st, Charset.forName("UTF-8"))) {
		//	stringBuilder1st.append(line);
		//}
		//Utils.createElementWithTextContent("step1Html", documentElement, stringBuilder1st.toString());

		Utils.createElementWithTextContent("step2Html", documentElement, ckeditorRepository.findOne((short) 8).getMarkup());
		//Path path2nd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser2nd.htm");
		//StringBuilder stringBuilder2nd = new StringBuilder();
		//for (String line : Files.readAllLines(path2nd, Charset.forName("UTF-8"))) {
		//	stringBuilder2nd.append(line);
		//}
		//Utils.createElementWithTextContent("step2Html", documentElement, stringBuilder2nd.toString());

		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/category/");
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					Element categoryElement = Utils.createElement("category", formElement);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject category = jsonArray.getJSONObject(i);
						Utils.createElementWithTextContentAndAttribute("option", categoryElement, category.getString("name"), "value", Integer.toString(category.getInt("id")));
					}
				} catch (JSONException jsonException) {
					errorMessage = jsonException.getLocalizedMessage();
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			errorMessage = ioException.getLocalizedMessage();
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
		}

		ModelAndView modelAndView = new ModelAndView("signUp/advertiserFirst");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * ??????????????????||????????????????????????
	 *
	 * @param name ????????????
	 * @param businessIdentificationNumber ??????
	 * @param phone ??????
	 * @param fax ??????
	 * @param address ????????????
	 * @param contact ?????????
	 * @param email ????????????
	 * @param categoryId ??????????????????
	 * @param shadow1 ????????????
	 * @param shadow2 ????????????
	 * @param multipartFile1 ?????????????????????
	 * @param multipartFile2 ??????????????????????????????
	 * @param multipartFile3 ??????????????????????????????
	 * @param session
	 * @return ??????
	 */
	@RequestMapping(value = "/advertiser.asp", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView advertiser(@RequestParam String name, @RequestParam String businessIdentificationNumber, @RequestParam String phone, @RequestParam(defaultValue = "") String fax, @RequestParam(defaultValue = "") String address, @RequestParam String contact, @RequestParam String email, @RequestParam(name = "category") Integer categoryId, @RequestParam String shadow1, @RequestParam String shadow2, @RequestParam(name = "certificateOfProfitSeekingEnterprise", required = false) MultipartFile multipartFile1, @RequestParam(name = "personInCharge", required = false) MultipartFile multipartFile2, @RequestParam(name = "personInCharge_", required = false) MultipartFile multipartFile3, HttpSession session) throws EmailException, IOException, ParserConfigurationException {
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me != null) {
			if (role != null && "advertiser".equals(role)) {
				return new ModelAndView("redirect:/advertiser/");
			}
			return new ModelAndView("redirect:/");
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElement("document", document);
		Element formElement = Utils.createElement("form", documentElement);
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = null;
		String errorMessage = null;

		Utils.createElementWithTextContent("step1Html", documentElement, ckeditorRepository.findOne((short) 7).getMarkup());
		//Path path1st = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser1st.htm");
		//StringBuilder stringBuilder1st = new StringBuilder();
		//for (String line : Files.readAllLines(path1st, Charset.forName("UTF-8"))) {
		//	stringBuilder1st.append(line);
		//}
		//Utils.createElementWithTextContent("step1Html", documentElement, stringBuilder1st.toString());

		Utils.createElementWithTextContent("step2Html", documentElement, ckeditorRepository.findOne((short) 8).getMarkup());
		//Path path2nd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser2nd.htm");
		//StringBuilder stringBuilder2nd = new StringBuilder();
		//for (String line : Files.readAllLines(path2nd, Charset.forName("UTF-8"))) {
		//	stringBuilder2nd.append(line);
		//}
		//Utils.createElementWithTextContent("step2Html", documentElement, stringBuilder2nd.toString());


		/*
		 ????????????
		 */
		name = name.trim();
		if (name.isEmpty()) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("name", formElement, name);

		/*
		 ??????
		 */
		businessIdentificationNumber = businessIdentificationNumber.trim();
		if (businessIdentificationNumber.isEmpty()) {
			errorMessage = "?????????????????????";
		} else if (!businessIdentificationNumber.matches("^(\\d{8})$")) {
			errorMessage = "??????????????????";
		} else if (services.hasAdvertiserBusinessIdentificationNumber(businessIdentificationNumber)) {
			errorMessage = "?????????????????????";
		}
		Utils.createElementWithTextContent("businessIdentificationNumber", formElement, businessIdentificationNumber);

		/*
		 ??????
		 */
		phone = phone.trim();
		if (phone.isEmpty()) {
			errorMessage = "?????????????????????";
		}
		Utils.createElementWithTextContent("phone", formElement, phone);

		/*
		 ??????
		 */
		if (fax.isEmpty()) {
			fax = null;
		} else {
			Utils.createElementWithTextContent("fax", formElement, fax);
		}

		/*
		 ??????
		 */
		if (address.isEmpty()) {
			address = null;
		} else {
			Utils.createElementWithTextContent("address", formElement, address);
		}

		/*
		 ?????????
		 */
		contact = contact.trim();
		if (contact.isEmpty()) {
			errorMessage = "????????????????????????";
		}
		Utils.createElementWithTextContent("contact", formElement, contact);

		/*
		 ????????????
		 */
		email = email.trim().toLowerCase();
		if (email.isEmpty()) {
			errorMessage = "???????????????????????????";
		} else if (services.hasLogin(email)) {
			errorMessage = "???????????????????????????";
		}
		Utils.createElementWithTextContent("email", formElement, email);

		/*
		 ??????????????????
		 */
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/category/");
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					Element categoryElement = Utils.createElement("category", formElement);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject category = jsonArray.getJSONObject(i);
						Integer id = category.getInt("id");
						Element optionElement = Utils.createElementWithTextContentAndAttribute("option", categoryElement, category.getString("name"), "value", id.toString());
						if (Objects.equals(categoryId, id)) {
							optionElement.setAttribute("selected", null);
						}
					}
				} catch (JSONException jsonException) {
					errorMessage = jsonException.getLocalizedMessage();
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			errorMessage = ioException.getLocalizedMessage();
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		/*
		 ????????????
		 */
		shadow1 = shadow1.trim();
		if (shadow1.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		/*
		 ????????????
		 */
		shadow2 = shadow2.trim();
		if (shadow2.isEmpty()) {
			errorMessage = "???????????????????????????";
		}

		if (!shadow1.equals(shadow2)) {
			errorMessage = "????????????????????????????????????";
		}

		/*
		 ?????????????????????
		 */
		byte[] certificateOfProfitSeekingEnterprise = null;
		int certificateOfProfitSeekingEnterpriseLength = 0;
		if (multipartFile1 != null) {
			try {
				certificateOfProfitSeekingEnterprise = multipartFile1.getBytes();
				certificateOfProfitSeekingEnterpriseLength = certificateOfProfitSeekingEnterprise.length;
				if (certificateOfProfitSeekingEnterpriseLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge = null;
		int personInChargeLength = 0;
		if (multipartFile2 != null) {
			try {
				personInCharge = multipartFile2.getBytes();
				personInChargeLength = personInCharge.length;
				if (personInChargeLength > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		/*
		 ??????????????????????????????
		 */
		byte[] personInCharge_ = null;
		int personInChargeLength_ = 0;
		if (multipartFile3 != null) {
			try {
				personInCharge_ = multipartFile3.getBytes();
				personInChargeLength_ = personInCharge_.length;
				if (personInChargeLength_ > MAX_IMAGE_UPLOAD_SIZE) {
					errorMessage = "?????????????????????????????????????????? 512Kb???";
				}
			} catch (IOException ioException) {
				errorMessage = ioException.getLocalizedMessage();
			}
		}

		if (errorMessage != null) {
			Utils.createElementWithTextContent("errorMessage", documentElement, errorMessage);
			Utils.createElement("hideStep1", documentElement);

			ModelAndView modelAndView = new ModelAndView("signUp/advertiserFirst");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", name);
		jsonObject.put("businessIdentificationNumber", businessIdentificationNumber);
		jsonObject.put("phone", phone);
		jsonObject.put("fax", fax);
		jsonObject.put("address", address);
		jsonObject.put("contact", contact);
		jsonObject.put("email", email);
		jsonObject.put("category", categoryId);
		jsonObject.put("shadow", shadow1);

		HttpPost httpPost = new HttpPost("http://restful.tour-map.net/advertiser/add.json");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		List<NameValuePair> nameValuePairs = new ArrayList<>();
		nameValuePairs.add(new BasicNameValuePair("json", jsonObject.toString()));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
			closeableHttpResponse = closeableHttpClient.execute(httpPost);
			if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
				System.err.println(closeableHttpResponse.getStatusLine().getStatusCode());
				Utils.createElementWithTextContent("errorMessage", documentElement, "???????????????????????????????????????");
				Utils.createElement("hideStep1", documentElement);

				ModelAndView modelAndView = new ModelAndView("signUp/advertiserFirst");
				modelAndView.getModelMap().addAttribute(new DOMSource(document));
				return modelAndView;
			}
			Utils.createElementWithTextContent("step3Html", documentElement, ckeditorRepository.findOne((short) 9).getMarkup());
			//Path path3rd = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser3rd.htm");
			//StringBuilder stringBuilder3rd = new StringBuilder();
			//for (String line : Files.readAllLines(path3rd, Charset.forName("UTF-8"))) {
			//	stringBuilder3rd.append(line);
			//}
			//Utils.createElementWithTextContent("step3Html", documentElement, stringBuilder3rd.toString());
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + "\n:\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		try {
			closeableHttpClient.close();
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + "\n:\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpClient);
		}

		MultiPartEmail multiPartEmail = new HtmlEmail();
		multiPartEmail.setHostName("smtp.googlemail.com");
		multiPartEmail.setSmtpPort(465);
		multiPartEmail.setAuthentication("tourmap.net@gmail.com", "RU; 2k6xu4");
		multiPartEmail.setSSLOnConnect(true);
		multiPartEmail.setCharset("UTF-8");
		multiPartEmail.setFrom(EMAIL_ADDRESS, "?????????", "UTF-8");
		multiPartEmail.addTo("advertiser_oc@tour-map.net");//2016-03-30
		multiPartEmail.addCc(new String[]{"advertiser_cc@tour-map.net", email});//2016-04-07
		multiPartEmail.addBcc("advertiser_bcc@tour-map.net");//2016-03-30
		multiPartEmail.setSubject("???????????????????????????????????????");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<TABLE style='border-collapse:separate' border='1' cellspacing='6' cellpadding='6'>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(name).append("</TD>");
		stringBuilder.append("<TH>??????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(businessIdentificationNumber).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>??????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(phone).append("</TD>");
		stringBuilder.append("<TH>??????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00'>").append(fax == null ? "" : fax).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(address == null ? "" : address).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>?????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(contact).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("<TR>");
		stringBuilder.append("<TH>????????????</TH>");
		stringBuilder.append("<TD style='color:#EF7E00' colspan='3'>").append(email).append("</TD>");
		stringBuilder.append("</TR>");
		stringBuilder.append("</TABLE>");
		multiPartEmail.setMsg(stringBuilder.toString());

		if (certificateOfProfitSeekingEnterprise != null && certificateOfProfitSeekingEnterpriseLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(certificateOfProfitSeekingEnterprise);

			Path path = Paths.get(services.getContextRealPath(), multipartFile1.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		if (personInCharge != null && personInChargeLength > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge);

			Path path = Paths.get(services.getContextRealPath(), multipartFile2.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		if (personInCharge_ != null && personInChargeLength_ > 0) {
			InputStream inputStream = new ByteArrayInputStream(personInCharge_);

			Path path = Paths.get(services.getContextRealPath(), multipartFile3.getOriginalFilename());
			File file = path.toFile();
			file.createNewFile();

			FileOutputStream fileOutputStream = new FileOutputStream(file);
			int i;
			while ((i = inputStream.read()) != -1) {
				fileOutputStream.write(i);
			}
			fileOutputStream.close();
			IOUtils.closeQuietly(fileOutputStream);

			IOUtils.closeQuietly(inputStream);

			EmailAttachment emailAttachment = new EmailAttachment();
			emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
			emailAttachment.setPath(file.getAbsolutePath());

			multiPartEmail.attach(emailAttachment);
		}

		multiPartEmail.send();

		ModelAndView modelAndView = new ModelAndView("signUp/advertiserLast");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
}
