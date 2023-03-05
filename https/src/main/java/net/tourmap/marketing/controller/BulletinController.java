package net.tourmap.marketing.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Bulletin;
import net.tourmap.marketing.service.BulletinService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 公佈欄
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Controller
@RequestMapping("/bulletin")
public class BulletinController {

	@Autowired
	net.tourmap.marketing.repository.BulletinRepository bulletinRepository;

	@Autowired
	BulletinService bulletinService;

	@Autowired
	javax.servlet.ServletContext context;

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
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "公佈欄");

		/*
		 分頁
		 */
		Element paginationElement = Utils.createElement("pagination", documentElement);
		Pageable pageRequest = new org.springframework.data.domain.PageRequest(number, size, Sort.Direction.ASC, "ordinal");
		Page<Bulletin> pageOfEntities = bulletinRepository.findAll(pageRequest);
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
		for (Bulletin entity : pageOfEntities.getContent()) {
			String filename = entity.getFilename();
			Short ordinal = entity.getOrdinal();

			Element rowElement = Utils.createElementWithAttribute("row", listElement, "id", entity.getId().toString());//列(帶主鍵)
			Utils.createElementWithTextContent("filename", rowElement, filename == null ? "" : filename);//檔案名稱
			Utils.createElementWithTextContent("ordinal", rowElement, ordinal.toString());//排序
		}

		ModelAndView modelAndView = new ModelAndView("bulletin/list");
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
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增公佈欄");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增公佈欄");

		ModelAndView modelAndView = new ModelAndView("bulletin/form");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	/**
	 * 新增
	 *
	 * @param multipartFile 檔案
	 * @return 網頁或重導向
	 */
	@RequestMapping(value = "/add.php", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	protected ModelAndView create(@RequestParam(required = false, value = "multipartFile") MultipartFile multipartFile, HttpServletRequest request) throws Exception {
		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "title", "新增公佈欄");

		Element formElement = Utils.createElementWithAttribute("form", documentElement, "action", request.getRequestURI());
		formElement.setAttribute("legend", "新增公佈欄");
		String errorMessage = bulletinService.save(new Bulletin(), multipartFile, formElement);
		if (errorMessage != null) {
			formElement.setAttribute("errorMessage", errorMessage);
			ModelAndView modelAndView = new ModelAndView("bulletin/form");
			modelAndView.getModelMap().addAttribute(new DOMSource(document));
			return modelAndView;
		}
		return new ModelAndView("redirect:/bulletin/");
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
		Bulletin entity = bulletinRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		//Path path = Paths.get(context.getRealPath(context.getContextPath()), "bulletin", entity.getFilename());
		Path path;
		if (System.getProperty("os.name").equalsIgnoreCase("Windows")) {
			path = Paths.get(context.getRealPath(context.getContextPath()), "bulletin", entity.getFilename());
		} else {
			path = Paths.get("/var/lib/tomcat7/webapps/http/ROOT", "bulletin", entity.getFilename());
		}
		File file = path.toFile();
		if (file.delete()) {
			bulletinRepository.delete(entity);
			bulletinRepository.flush();
		}
		bulletinService.sort();

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
		Bulletin entity = bulletinRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		short ordinal = entity.getOrdinal();
		Bulletin nextEntity = bulletinRepository.findOndByOrdinal((short) (ordinal + 1));
		if (nextEntity == null) {
			return jsonObject.put("reason", "已達底端！").toString();
		}
		nextEntity.setOrdinal((short) -1);
		bulletinRepository.saveAndFlush(nextEntity);

		entity.setOrdinal((short) (ordinal + 1));
		bulletinRepository.saveAndFlush(entity);

		nextEntity.setOrdinal(ordinal);
		bulletinRepository.saveAndFlush(nextEntity);

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
		Bulletin entity = bulletinRepository.findOne(id);
		if (entity == null) {
			return jsonObject.put("reason", "找不到！").toString();
		}

		short ordinal = entity.getOrdinal();
		Bulletin previousEntity = bulletinRepository.findOndByOrdinal((short) (ordinal - 1));
		if (previousEntity == null) {
			return jsonObject.put("reason", "已達頂端！").toString();
		}
		previousEntity.setOrdinal((short) -1);
		bulletinRepository.saveAndFlush(previousEntity);

		entity.setOrdinal((short) (ordinal - 1));
		bulletinRepository.saveAndFlush(entity);

		previousEntity.setOrdinal(ordinal);
		bulletinRepository.saveAndFlush(previousEntity);

		return jsonObject.put("response", true).toString();
	}
}
