package net.tourmap.marketing.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.FrequentlyAskedQuestion;
import net.tourmap.marketing.repository.FrequentlyAskedQuestionRepository;
import net.tourmap.marketing.service.FrequentlyAskedQuestionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * 問題Q&A
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Controller
@RequestMapping("/frequentlyAskedQuestion")
public class FrequentlyAskedQuestionController {

	@Autowired
	FrequentlyAskedQuestionRepository frequentlyAskedQuestionRepository;

	@Autowired
	FrequentlyAskedQuestionService frequentlyAskedQuestionService;

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
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "問題Q&A");

		/*
		 分頁
		 */
		Element paginationElement = Utils.createElement("pagination", documentElement);
		Pageable pageRequest = new org.springframework.data.domain.PageRequest(number, size, Sort.Direction.ASC, "ordinal");
		Page<FrequentlyAskedQuestion> pageOfEntities = frequentlyAskedQuestionRepository.findAll(pageRequest);
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
		for (FrequentlyAskedQuestion entity : pageOfEntities.getContent()) {
			String question = entity.getQuestion();
			String answer = entity.getAnswer();
			Short ordinal = entity.getOrdinal();
			Boolean consumer = entity.isConsumer();

			Element rowElement = Utils.createElementWithAttribute("row", listElement, "id", entity.getId().toString());//列(帶主鍵)
			Utils.createElementWithTextContent("question", rowElement, question == null ? "" : question);//問題
			Utils.createElementWithTextContent("answer", rowElement, answer == null ? "" : answer);//回答
			Utils.createElementWithTextContent("ordinal", rowElement, ordinal.toString());//排序
			Utils.createElementWithTextContent("consumer", rowElement, consumer.toString());//企業|消費者
		}

		ModelAndView modelAndView = new ModelAndView("frequentlyAskedQuestion/list");
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
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增問題Q&A");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增問題Q&A");
		frequentlyAskedQuestionService.load(new FrequentlyAskedQuestion(), formElement);

		ModelAndView modelAndView = new ModelAndView("frequentlyAskedQuestion/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增
	 *
	 * @param question 問題
	 * @param answer 回答
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/add.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView create(@RequestParam String question, @RequestParam String answer, HttpServletRequest request) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增問題Q&A");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增問題Q&A");
		String errorMessage = frequentlyAskedQuestionService.save(new FrequentlyAskedQuestion(), question, answer, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("frequentlyAskedQuestion/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/frequentlyAskedQuestion/");
	}

	/**
	 * 企業|消費者
	 *
	 * @param id 主鍵
	 * @return JSON
	 */
	@RequestMapping(value = "/{id:[\\d]+}/businessOrConsumer.json", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	protected String businessOrConsumer(@PathVariable Short id) {
		JSONObject jsonObject = new JSONObject();
		FrequentlyAskedQuestion entity = frequentlyAskedQuestionRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		entity.setConsumer(!entity.isConsumer());
		frequentlyAskedQuestionRepository.saveAndFlush(entity);

		return jsonObject.put("response", true).toString();
	}

	/**
	 * 開啟
	 *
	 * @param id 主鍵
	 * @return 網頁
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView read(@PathVariable Short id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		FrequentlyAskedQuestion entity = frequentlyAskedQuestionRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改問題Q&A");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改問題Q&A");
		frequentlyAskedQuestionService.load(entity, formElement);

		ModelAndView modelAndView = new ModelAndView("frequentlyAskedQuestion/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 修改
	 *
	 * @param id 主鍵
	 * @param question 問題
	 * @param answer 回答
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/{id:[\\d]+}", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView update(@PathVariable Short id, @RequestParam String question, @RequestParam String answer, HttpServletRequest request, HttpServletResponse response) throws Exception {
		FrequentlyAskedQuestion entity = frequentlyAskedQuestionRepository.findOne(id);
		if (entity == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//Not Found
			return null;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "修改問題Q&A");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "修改問題Q&A");
		String errorMessage = frequentlyAskedQuestionService.save(entity, question, answer, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("frequentlyAskedQuestion/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/frequentlyAskedQuestion/");
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
		FrequentlyAskedQuestion entity = frequentlyAskedQuestionRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		frequentlyAskedQuestionRepository.delete(entity);
		frequentlyAskedQuestionRepository.flush();
		frequentlyAskedQuestionService.sort();

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
		FrequentlyAskedQuestion entity = frequentlyAskedQuestionRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		short ordinal = entity.getOrdinal();
		FrequentlyAskedQuestion nextEntity = frequentlyAskedQuestionRepository.findOndByOrdinal((short) (ordinal + 1));
		if (nextEntity == null) {
			return jsonObject.put("reason", "已達底端！").toString();
		}
		nextEntity.setOrdinal((short) -1);
		frequentlyAskedQuestionRepository.saveAndFlush(nextEntity);

		entity.setOrdinal((short) (ordinal + 1));
		frequentlyAskedQuestionRepository.saveAndFlush(entity);

		nextEntity.setOrdinal(ordinal);
		frequentlyAskedQuestionRepository.saveAndFlush(nextEntity);

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
		FrequentlyAskedQuestion entity = frequentlyAskedQuestionRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		short ordinal = entity.getOrdinal();
		FrequentlyAskedQuestion previousEntity = frequentlyAskedQuestionRepository.findOndByOrdinal((short) (ordinal - 1));
		if (previousEntity == null) {
			return jsonObject.put("reason", "已達頂端！").toString();
		}
		previousEntity.setOrdinal((short) -1);
		frequentlyAskedQuestionRepository.saveAndFlush(previousEntity);

		entity.setOrdinal((short) (ordinal - 1));
		frequentlyAskedQuestionRepository.saveAndFlush(entity);

		previousEntity.setOrdinal(ordinal);
		frequentlyAskedQuestionRepository.saveAndFlush(previousEntity);

		return jsonObject.put("response", true).toString();
	}
}
