package net.tourmap.marketing.controller;

import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Administrator;
import net.tourmap.marketing.service.AdministratorService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;

/**
 * userTable
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/administrator")
public class AdministratorController {

	@Autowired
	net.tourmap.marketing.repository.AdministratorRepository administratorRepository;

	@Autowired
	AdministratorService administratorService;

	/**
	 * 列表
	 *
	 * @param size 一頁幾筆
	 * @param number 第幾頁
	 * @return 網頁
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView read(@RequestParam(value = "s", defaultValue = "10") Integer size, @RequestParam(value = "p", defaultValue = "0") Integer number) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "管理員");

		/*
		 分頁
		 */
		Element paginationElement = Utils.createElement("pagination", documentElement);
		Pageable pageRequest = new org.springframework.data.domain.PageRequest(number, size);
		Page<Administrator> pageOfEntities = administratorRepository.findAll(pageRequest);
		number = pageOfEntities.getNumber();
		Integer totalPages = pageOfEntities.getTotalPages();
		Long totalElements = pageOfEntities.getTotalElements();
		if (pageOfEntities.hasPrevious()) {
			paginationElement.setAttribute("previous", Integer.toString(number - 1));
			if (!pageOfEntities.isFirst()) {
				paginationElement.setAttribute("first", "0");
			}
		}
		paginationElement.setAttribute("size", size.toString());
		paginationElement.setAttribute("number", number.toString());
		for (Integer i = 0; i < totalPages; i++) {
			Element optionElement = Utils.createElementWithTextContentAndAttribute("option", paginationElement, Integer.toString(i + 1), "value", i.toString());
			if (number.equals(i)) {
				optionElement.setAttribute("selected", null);
			}
		}
		paginationElement.setAttribute("totalPages", totalPages.toString());
		paginationElement.setAttribute("totalElements", totalElements.toString());
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
		for (Administrator entity : pageOfEntities.getContent()) {
			String alias = entity.getAlias();
			String login = entity.getLogin();
			String shadow = entity.getShadow();

			Element rowElement = Utils.createElementWithAttribute("row", listElement, "id", entity.getId().toString());//列(帶主鍵)
			Utils.createElementWithTextContent("alias", rowElement, alias == null ? "" : alias);//別名
			Utils.createElementWithTextContent("login", rowElement, login == null ? "" : login);//帳號(電子郵件)
			Utils.createElementWithTextContent("shadow", rowElement, shadow == null ? "" : shadow);//密碼
		}

		ModelAndView modelAndView = new ModelAndView("administrator/list");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/add.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView create(HttpServletRequest request) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增管理員");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增管理員");
		administratorService.load(new Administrator(), formElement);

		ModelAndView modelAndView = new ModelAndView("administrator/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增
	 *
	 * @param alias 別名
	 * @param login 帳號(電子郵件)
	 * @param shadow 密碼
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/add.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView create(@RequestParam String alias, @RequestParam String login, HttpServletRequest request) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增管理員");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增管理員");
		String errorMessage = administratorService.save(new Administrator(), alias, login, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("administrator/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/administrator/");
	}

	/**
	 * 開啟
	 *
	 * @param id 主鍵
	 * @return 網頁
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView read(@PathVariable Short id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Administrator entity = administratorRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改管理員");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改管理員");
		administratorService.load(entity, formElement);

		ModelAndView modelAndView = new ModelAndView("administrator/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 修改
	 *
	 * @param id 主鍵
	 * @param alias 別名
	 * @param login 帳號(電子郵件)
	 * @param shadow 密碼
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView update(@PathVariable Short id, @RequestParam String alias, @RequestParam String login, @RequestParam(defaultValue = "") String shadow, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Administrator entity = administratorRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改管理員");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改管理員");
		String errorMessage = administratorService.save(entity, alias, login, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("administrator/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/administrator/");
	}

	/**
	 * 刪除
	 *
	 * @param id 主鍵
	 * @return JSON
	 */
	@RequestMapping(value = "/{id:[\\d]+}/remove.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String delete(@PathVariable Short id) {
		JSONObject jsonObject = new JSONObject();
		Administrator entity = administratorRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		administratorRepository.delete(entity);
		administratorRepository.flush();

		return jsonObject.put("response", true).toString();
	}

	@RequestMapping(value = "/{id:[\\d]+}/shadow.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView shadow(@PathVariable Short id, HttpServletRequest request, HttpServletResponse response) throws ParserConfigurationException {
		Administrator entity = administratorRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改管理員密碼");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改管理員密碼");
		Utils.createElement("shadow", formElement);

		ModelAndView modelAndView = new ModelAndView("administrator/shadow");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	@RequestMapping(value = "/{id:[\\d]+}/shadow.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView shadow(@PathVariable Short id, @RequestParam(defaultValue = "") String shadow, HttpServletRequest request, HttpServletResponse response) throws ParserConfigurationException {
		Administrator entity = administratorRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改管理員密碼");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改管理員密碼");

		shadow = shadow.isEmpty() ? null : org.apache.catalina.realm.RealmBase.Digest(shadow, "MD5", "UTF-8");
		entity.setShadow(shadow);
		administratorRepository.saveAndFlush(entity);

		ModelAndView modelAndView = new ModelAndView("administrator/shadow");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 停用
	 *
	 * @param id 主鍵
	 * @return JSON
	 */
	@RequestMapping(value = "/{id:[\\d]+}/suspend.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String suspend(@PathVariable Short id) {
		JSONObject jsonObject = new JSONObject();
		Administrator entity = administratorRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		if (entity.getShadow() == null) {
			return jsonObject.put("reason", "已停用！").toString();
		}

		entity.setShadow(null);
		administratorRepository.saveAndFlush(entity);

		return jsonObject.put("response", true).toString();
	}
}
