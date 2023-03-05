package net.tourmap.marketing.controller;

import javax.servlet.http.*;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Youtube;
import net.tourmap.marketing.service.YoutubeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;

/**
 * 影片
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/youtube")
public class YoutubeController {

	@Autowired
	net.tourmap.marketing.repository.YoutubeRepository youtubeRepository;

	@Autowired
	YoutubeService youtubeService;

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
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "影片");

		/*
		 分頁
		 */
		Element paginationElement = Utils.createElement("pagination", documentElement);
		Pageable pageRequest = new org.springframework.data.domain.PageRequest(number, size, Sort.Direction.ASC, "ordinal");
		Page<Youtube> pageOfEntities = youtubeRepository.findAll(pageRequest);
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
		for (Youtube entity : pageOfEntities.getContent()) {
			String title = entity.getTitle();
			String url = entity.getUrl();
			Short ordinal = entity.getOrdinal();

			Element rowElement = Utils.createElementWithAttribute("row", listElement, "id", entity.getId().toString());//列(帶主鍵)
			Utils.createElementWithTextContent("title", rowElement, title == null ? "" : title);//標題
			Utils.createElementWithTextContent("url", rowElement, url == null ? "" : url);//完整網址
			Utils.createElementWithTextContent("ordinal", rowElement, ordinal.toString());//排序
		}

		ModelAndView modelAndView = new ModelAndView("youtube/list");
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
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增影片");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增影片");
		youtubeService.load(new Youtube(), formElement);

		ModelAndView modelAndView = new ModelAndView("youtube/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增
	 *
	 * @param title 標題
	 * @param url 完整網址
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/add.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView create(@RequestParam String title, @RequestParam String url, HttpServletRequest request) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增影片");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增影片");
		String errorMessage = youtubeService.save(new Youtube(), title, url, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("youtube/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/youtube/");
	}

	/**
	 * 開啟
	 *
	 * @param id 主鍵
	 * @return 網頁
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView read(@PathVariable Short id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Youtube entity = youtubeRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改影片");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改影片");
		youtubeService.load(entity, formElement);

		ModelAndView modelAndView = new ModelAndView("youtube/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 修改
	 *
	 * @param id 主鍵
	 * @param title 標題
	 * @param url 完整網址
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView update(@PathVariable Short id, @RequestParam String title, @RequestParam String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Youtube entity = youtubeRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改影片");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改影片");
		String errorMessage = youtubeService.save(entity, title, url, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("youtube/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/youtube/");
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
		Youtube entity = youtubeRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		youtubeRepository.delete(entity);
		youtubeRepository.flush();
		youtubeService.sort();

		return jsonObject.put("response", true).toString();
	}

	/**
	 * 下移
	 *
	 * @param id 主鍵
	 * @return JSON
	 */
	@RequestMapping(value = "/{id:[\\d]+}/down.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String down(@PathVariable Short id) {
		JSONObject jsonObject = new JSONObject();
		Youtube entity = youtubeRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		short ordinal = entity.getOrdinal();
		Youtube nextEntity = youtubeRepository.findOndByOrdinal((short) (ordinal + 1));
		if (nextEntity == null) {
			return jsonObject.put("reason", "已達底端！").toString();
		}
		nextEntity.setOrdinal((short) -1);
		youtubeRepository.saveAndFlush(nextEntity);

		entity.setOrdinal((short) (ordinal + 1));
		youtubeRepository.saveAndFlush(entity);

		nextEntity.setOrdinal(ordinal);
		youtubeRepository.saveAndFlush(nextEntity);

		return jsonObject.put("response", true).toString();
	}

	/**
	 * 上移
	 *
	 * @param id 主鍵
	 * @return JSON
	 */
	@RequestMapping(value = "/{id:[\\d]+}/up.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String up(@PathVariable Short id) {
		JSONObject jsonObject = new JSONObject();
		Youtube entity = youtubeRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		short ordinal = entity.getOrdinal();
		Youtube previousEntity = youtubeRepository.findOndByOrdinal((short) (ordinal - 1));
		if (previousEntity == null) {
			return jsonObject.put("reason", "已達頂端！").toString();
		}
		previousEntity.setOrdinal((short) -1);
		youtubeRepository.saveAndFlush(previousEntity);

		entity.setOrdinal((short) (ordinal - 1));
		youtubeRepository.saveAndFlush(entity);

		previousEntity.setOrdinal(ordinal);
		youtubeRepository.saveAndFlush(previousEntity);

		return jsonObject.put("response", true).toString();
	}
}
