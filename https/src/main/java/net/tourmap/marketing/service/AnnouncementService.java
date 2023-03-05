package net.tourmap.marketing.service;

import java.util.Date;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Announcement;
import org.springframework.transaction.annotation.*;
import org.w3c.dom.Element;

/**
 * 活動消息
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class AnnouncementService {

	@org.springframework.beans.factory.annotation.Autowired
	net.tourmap.marketing.repository.AnnouncementRepository announcementRepository;

	/**
	 * 載入
	 *
	 * @param entity 實體
	 * @param parentNode 父元素
	 */
	public void load(Announcement entity, Element parentNode) {
		String subject = entity.getSubject();//主旨
		Utils.createElementWithTextContent("subject", parentNode, subject == null ? "" : subject);

		String hyperTextMarkupLanguage = entity.getHyperTextMarkupLanguage();//HTML內容
		Utils.createElementWithTextContent("hyperTextMarkupLanguage", parentNode, hyperTextMarkupLanguage == null ? "" : hyperTextMarkupLanguage);

		Date when = entity.getWhen();//發佈日期
		Utils.createElementWithTextContent("when", parentNode, when == null ? "" : Utils.formatDate(when));
	}

	/**
	 * 儲存
	 *
	 * @param entity 實體
	 * @param subject 主旨
	 * @param hyperTextMarkupLanguage HTML內容
	 * @param when 發佈日期
	 * @param parentNode 父元素
	 * @return 錯誤訊息
	 */
	public String save(Announcement entity, String subject, String hyperTextMarkupLanguage, Date when, Element parentNode) {
		String errorMessage = null;
		Short id = entity.getId();

		/*
		 主旨
		 */
		try {
			subject = subject.trim();
			if (subject.isEmpty()) {
				subject = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「主旨」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「主旨」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("subject", parentNode, subject == null ? "" : subject);

		/*
		 HTML內容
		 */
		try {
			hyperTextMarkupLanguage = hyperTextMarkupLanguage.trim();
			if (hyperTextMarkupLanguage.isEmpty()) {
				hyperTextMarkupLanguage = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「HTML內容」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「HTML內容」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("hyperTextMarkupLanguage", parentNode, hyperTextMarkupLanguage == null ? "" : hyperTextMarkupLanguage);

		if (when == null) {
			errorMessage = "「發佈日期」為必選！";
		}
		Utils.createElementWithTextContent("when", parentNode, when == null ? "" : Utils.formatDate(when));

		if (errorMessage == null) {
			entity.setSubject(subject);
			entity.setHyperTextMarkupLanguage(hyperTextMarkupLanguage);
			entity.setWhen(when);
			announcementRepository.saveAndFlush(entity);

			return null;
		}
		return errorMessage;
	}
}
