package net.tourmap.marketing.service;

import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Youtube;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.*;
import org.w3c.dom.Element;

/**
 * 影片
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class YoutubeService {

	@org.springframework.beans.factory.annotation.Autowired
	net.tourmap.marketing.repository.YoutubeRepository youtubeRepository;

	/**
	 * 載入
	 *
	 * @param entity 實體
	 * @param parentNode 父元素
	 */
	public void load(Youtube entity, Element parentNode) {
		String title = entity.getTitle();//標題
		Utils.createElementWithTextContent("title", parentNode, title == null ? "" : title);

		String url = entity.getUrl();//完整網址
		Utils.createElementWithTextContent("url", parentNode, url == null ? "" : url);
	}

	/**
	 * 儲存
	 *
	 * @param entity 實體
	 * @param title 標題
	 * @param url 完整網址
	 * @param parentNode 父元素
	 * @return 錯誤訊息
	 */
	@SuppressWarnings("null")
	public String save(Youtube entity, String title, String url, Element parentNode) {
		String errorMessage = null;
		Short id = entity.getId();

		/*
		 標題
		 */
		try {
			title = title.trim();
			if (title.isEmpty()) {
				title = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「標題」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「標題」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("title", parentNode, title == null ? "" : title);

		/*
		 完整網址
		 */
		try {
			url = url.trim();
			if (url.isEmpty()) {
				url = null;
				throw new NullPointerException();
			}
			if (id == null) {
				if (youtubeRepository.countByUrl(url) > 0) {
					errorMessage = "重複的「完整網址」！";
				}
			} else if (youtubeRepository.countByUrlAndIdNot(url, id) > 0) {
				errorMessage = "重複的「完整網址」！";
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「完整網址」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「完整網址」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("url", parentNode, url == null ? "" : url);

		/*
		 排序
		 */
		Short ordinal = entity.getOrdinal();
		if (id == null) {
			ordinal = Short.valueOf(Integer.toString(youtubeRepository.findAll().size() + 1));
		}

		if (errorMessage == null) {
			entity.setTitle(title);
			entity.setUrl(url);
			entity.setOrdinal(ordinal);
			youtubeRepository.saveAndFlush(entity);

			sort();

			return null;
		}
		return errorMessage;
	}

	/**
	 * 排序
	 */
	public void sort() {
		short ordinal = 1;
		for (Youtube entity : youtubeRepository.findAll(new Sort(Sort.Direction.ASC, "ordinal"))) {
			if (ordinal != entity.getOrdinal()) {
				entity.setOrdinal(ordinal);
				youtubeRepository.saveAndFlush(entity);
			}
			ordinal++;
		}
	}
}
