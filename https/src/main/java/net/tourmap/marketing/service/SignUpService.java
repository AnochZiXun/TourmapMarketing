package net.tourmap.marketing.service;

import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.CKEditor;
import org.springframework.transaction.annotation.*;
import org.w3c.dom.Element;

/**
 * 編輯器
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class SignUpService {

	@org.springframework.beans.factory.annotation.Autowired
	net.tourmap.marketing.repository.CKEditorRepository ckeditorRepository;

	/**
	 * 載入
	 *
	 * @param entity 實體
	 * @param parentNode 父元素
	 */
	public void load(CKEditor entity, Element parentNode) {
		String markup = entity.getMarkup();//HTML內容
		Utils.createElementWithTextContent("markup", parentNode, markup == null ? "" : markup);
	}

	/**
	 * 儲存
	 *
	 * @param entity 實體
	 * @param markup HTML內容
	 * @param parentNode 父元素
	 * @return 錯誤訊息
	 */
	public String save(CKEditor entity, String markup, Element parentNode) {
		String errorMessage = null;
		Short id = entity.getId();

		/*
		 HTML內容
		 */
		try {
			markup = markup.trim();
			if (markup.isEmpty()) {
				markup = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「HTML內容」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「HTML內容」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("markup", parentNode, markup == null ? "" : markup);

		if (errorMessage == null) {
			entity.setMarkup(markup);
			ckeditorRepository.saveAndFlush(entity);

			return null;
		}
		return errorMessage;
	}
}
