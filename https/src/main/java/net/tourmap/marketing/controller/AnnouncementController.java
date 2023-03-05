package net.tourmap.marketing.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Announcement;
import net.tourmap.marketing.repository.AnnouncementRepository;
import net.tourmap.marketing.service.AnnouncementService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 活動消息
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Controller
@RequestMapping("/announcement")
public class AnnouncementController {

	@Autowired
	AnnouncementRepository announcementRepository;

	@Autowired
	AnnouncementService announcementService;

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
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "活動消息");

		/*
		 分頁
		 */
		Element paginationElement = Utils.createElement("pagination", documentElement);
		Pageable pageRequest = new org.springframework.data.domain.PageRequest(number, size, Sort.Direction.DESC, "when");
		Page<Announcement> pageOfEntities = announcementRepository.findAll(pageRequest);
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
		for (Announcement entity : pageOfEntities.getContent()) {
			String subject = entity.getSubject();
			Date when = entity.getWhen();

			Element rowElement = Utils.createElementWithAttribute("row", listElement, "id", entity.getId().toString());//列(帶主鍵)
			Utils.createElementWithTextContent("subject", rowElement, subject == null ? "" : subject);//主旨
			Utils.createElementWithTextContent("when", rowElement, when == null ? "" : Utils.formatDate(when));//發佈日期
		}

		ModelAndView modelAndView = new ModelAndView("announcement/list");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/add.php", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView add(HttpServletRequest request) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增活動消息");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增活動消息");
		announcementService.load(new Announcement(), formElement);

		ModelAndView modelAndView = new ModelAndView("announcement/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增
	 *
	 * @param subject 主旨
	 * @param hyperTextMarkupLanguage HTML內容
	 * @param when 發佈日期
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/add.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView create(@RequestParam String subject, @RequestParam String hyperTextMarkupLanguage, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date when, HttpServletRequest request) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增活動消息");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增活動消息");
		String errorMessage = announcementService.save(new Announcement(), subject, hyperTextMarkupLanguage, when, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("announcement/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/announcement/");
	}

	/**
	 * 開啟
	 *
	 * @param id 主鍵
	 * @return 網頁
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView read(@PathVariable Short id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Announcement entity = announcementRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改活動消息");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改活動消息");
		announcementService.load(entity, formElement);

		ModelAndView modelAndView = new ModelAndView("announcement/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 修改
	 *
	 * @param id 主鍵
	 * @param subject 主旨
	 * @param hyperTextMarkupLanguage HTML內容
	 * @param when 發佈日期
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView update(@PathVariable Short id, @RequestParam String subject, @RequestParam String hyperTextMarkupLanguage, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date when, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Announcement entity = announcementRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改活動消息");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改活動消息");
		String errorMessage = announcementService.save(entity, subject, hyperTextMarkupLanguage, when, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("announcement/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/announcement/");
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
		Announcement entity = announcementRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		announcementRepository.delete(entity);
		announcementRepository.flush();

		return jsonObject.put("response", true).toString();
	}
}
