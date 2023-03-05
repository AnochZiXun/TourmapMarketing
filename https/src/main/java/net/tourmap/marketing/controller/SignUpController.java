package net.tourmap.marketing.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.CKEditor;
import net.tourmap.marketing.repository.CKEditorRepository;
import net.tourmap.marketing.service.SignUpService;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 編輯器
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Controller
@RequestMapping("/signUp")
public class SignUpController {

	@Autowired
	private CKEditorRepository ckeditorRepository;

	@Autowired
	private javax.servlet.ServletContext context;

	@Autowired
	private SignUpService signUpService;

	/**
	 * 微網店家步驟一
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/vendor/step1.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readVendorStep1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 1);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改微網店家步驟一");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改微網店家步驟一");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor1st.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 微網店家步驟一
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/vendor/step1.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateVendorStep1(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 1);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改微網店家步驟一");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改微網店家步驟一");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor1st.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/vendor/step1.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/vendor/step1.php");
	}

	/**
	 * 微網店家步驟二
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/vendor/step2.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readVendorStep2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 2);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改微網店家步驟二");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改微網店家步驟二");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor2nd.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 微網店家步驟二
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/vendor/step2.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateVendorStep2(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 2);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改微網店家步驟二");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改微網店家步驟二");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor2nd.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/vendor/step2.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/vendor/step2.php");
	}

	/**
	 * 微網店家步驟三
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/vendor/step3.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readVendorStep3(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 3);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改微網店家步驟三");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改微網店家步驟三");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor3rd.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 微網店家步驟三
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/vendor/step3.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateVendorStep3(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 3);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改微網店家步驟三");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改微網店家步驟三");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "vendor3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "vendor3rd.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/vendor/step3.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/vendor/step3.php");
	}

	/**
	 * 業務團隊步驟一
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/agent/step1.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readAgentStep1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 4);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改業務團隊步驟一");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改業務團隊步驟一");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent1st.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 業務團隊步驟一
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/agent/step1.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateAgentStep1(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 4);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改業務團隊步驟一");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改業務團隊步驟一");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent1st.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/agent/step1.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/agent/step1.php");
	}

	/**
	 * 業務團隊步驟二
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/agent/step2.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readAgentStep2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 5);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改業務團隊步驟二");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改業務團隊步驟二");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent2nd.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 業務團隊步驟二
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/agent/step2.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateAgentStep2(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 5);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改業務團隊步驟二");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改業務團隊步驟二");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent2nd.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/agent/step2.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/agent/step2.php");
	}

	/**
	 * 業務團隊步驟三
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/agent/step3.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readAgentStep3(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 6);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改業務團隊步驟三");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改業務團隊步驟三");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent3rd.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 業務團隊步驟三
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/agent/step3.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateAgentStep3(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 6);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改業務團隊步驟三");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改業務團隊步驟三");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "agent3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "agent3rd.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/agent/step3.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/agent/step3.php");
	}

	/**
	 * 店家廣告步驟一
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/advertiser/step1.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readAdvertiserStep1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 7);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改店家廣告步驟一");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改店家廣告步驟一");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser1st.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 店家廣告步驟一
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/advertiser/step1.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateAdvertiserStep1(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 7);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改店家廣告步驟一");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改店家廣告步驟一");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser1st.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser1st.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/advertiser/step1.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/advertiser/step1.php");
	}

	/**
	 * 店家廣告步驟二
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/advertiser/step2.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readAdvertiserStep2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 8);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改店家廣告步驟二");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改店家廣告步驟二");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser2nd.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 店家廣告步驟二
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/advertiser/step2.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateAdvertiserStep2(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 8);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改店家廣告步驟二");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改店家廣告步驟二");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser2nd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser2nd.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/advertiser/step2.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/advertiser/step2.php");
	}

	/**
	 * 店家廣告步驟三
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/advertiser/step3.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView readAdvertiserStep3(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 9);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改店家廣告步驟三");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改店家廣告步驟三");
		signUpService.load(entity, formElement);
//		Path path = SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser3rd.htm");
//		StringBuilder stringBuilder = new StringBuilder();
//		for (String line : Files.readAllLines(path, Charset.forName("UTF-8"))) {
//			stringBuilder.append(line);
//		}
//		Utils.createElementWithTextContent("markup", formElement, stringBuilder.toString());

		ModelAndView modelAndView = new ModelAndView("signUpSteps");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 店家廣告步驟三
	 *
	 * @param markup HTML內容
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/advertiser/step3.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView updateAdvertiserStep3(@RequestParam String markup, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CKEditor entity = ckeditorRepository.findOne((short) 9);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改店家廣告步驟三");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改店家廣告步驟三");

		String errorMessage = signUpService.save(entity, markup, formElement);
//		String errorMessage = null;
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(SystemUtils.IS_OS_LINUX ? Paths.get("/var", "lib", "tomcat7", "webapps", "ROOT", "advertiser3rd.htm") : Paths.get(context.getRealPath(context.getContextPath()), "WEB-INF", "htm", "advertiser3rd.htm"), Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
//			bufferedWriter.write(markup, 0, markup.length());
//			return new ModelAndView("redirect:/signUp/advertiser/step3.php");
//		} catch (IOException ioException) {
//			errorMessage = ioException.getLocalizedMessage();
//		}

		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("signUpSteps");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/signUp/advertiser/step3.php");
	}
}
