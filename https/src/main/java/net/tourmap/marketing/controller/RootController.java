package net.tourmap.marketing.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.dom.DOMSource;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Youtube;
import net.tourmap.marketing.service.AdministratorService;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Controller
@RequestMapping("/")
public class RootController {

	@Autowired
	net.tourmap.marketing.repository.AdministratorRepository administratorRepository;

	@Autowired
	AdministratorService administratorService;

	/**
	 * 首頁
	 *
	 * @return 網頁
	 */
	@RequestMapping(value = "/", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	protected ModelAndView index() throws Exception {
		Document document = Utils.newDocument();
		Utils.createElementWithAttribute("document", document, "title", "首頁");

		ModelAndView modelAndView = new ModelAndView("default");
		modelAndView.getModelMap().addAttribute(new DOMSource(document));
		return modelAndView;
	}

	@RequestMapping(value = "/default.asp", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	protected String jsonTest(HttpServletRequest request, HttpSession session) throws IOException {
		JSONObject jsonObject = null;
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://restful.tour-map.tld/vendor/248.json");
		CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
		try {
			HttpEntity entity = closeableHttpResponse.getEntity();
			if (entity != null) {
				InputStream inputStream = entity.getContent();
				try {
					jsonObject = new JSONObject(IOUtils.toString(inputStream));
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} finally {
			closeableHttpResponse.close();
		}

		StringBuilder stringBuilder = new StringBuilder();
		Iterator iterator = jsonObject.keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			stringBuilder.append(key).append(":\t").append(jsonObject.get(key)).append("\n");
		}

//		return request.getRemoteUser();
		return stringBuilder.toString();
	}
}
