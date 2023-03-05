package net.tourmap.marketing.controller;

import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.repository.*;
import net.tourmap.marketing.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;

/**
 * 旅圖廣告
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/advertiser")
public class AdvertiserController {

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
		Integer me = (Integer) session.getAttribute("me");
		String role = (String) session.getAttribute("role");
		if (me == null) {
			ModelAndView modelAndView = new ModelAndView("redirect:/logIn.asp");
			return modelAndView;
		}

		Document document = Utils.newDocument();
		Element documentElement = Utils.createElementWithAttribute("document", document, "me", me.toString());

		Utils.createElementWithTextContent("name", documentElement, services.getWhoever(me, role, session));

		//TODO:	是否已繳費
		ResourceBundle resourceBundle = ResourceBundle.getBundle("allPay");
		Utils.createElementWithTextContentAndAttribute("oneTimeCharge", documentElement, resourceBundle.getString("MerchantID"), "URL", resourceBundle.getString("URL"));

		ModelAndView modelAndView = new ModelAndView("advertiser/default");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}
}
