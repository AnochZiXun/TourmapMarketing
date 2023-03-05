package net.tourmap.marketing.service;

import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.FrequentlyAskedQuestion;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.*;
import org.w3c.dom.Element;

/**
 * 問題Q&A
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class FrequentlyAskedQuestionService {

	@org.springframework.beans.factory.annotation.Autowired
	net.tourmap.marketing.repository.FrequentlyAskedQuestionRepository frequentlyAskedQuestionRepository;

	/**
	 * 載入
	 *
	 * @param entity 實體
	 * @param parentNode 父元素
	 */
	public void load(FrequentlyAskedQuestion entity, Element parentNode) {
		String question = entity.getQuestion();//問題
		Utils.createElementWithTextContent("question", parentNode, question == null ? "" : question);

		String answer = entity.getAnswer();//回答
		Utils.createElementWithTextContent("answer", parentNode, answer == null ? "" : answer);
	}

	/**
	 * 儲存
	 *
	 * @param entity 實體
	 * @param question 問題
	 * @param answer 回答
	 * @param parentNode 父元素
	 * @return 錯誤訊息
	 */
	public String save(FrequentlyAskedQuestion entity, String question, String answer, Element parentNode) {
		String errorMessage = null;
		Short id = entity.getId();

		/*
		 問題
		 */
		try {
			question = question.trim();
			if (question.isEmpty()) {
				question = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「問題」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「問題」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("question", parentNode, question == null ? "" : question);

		/*
		 回答
		 */
		try {
			answer = answer.trim();
			if (answer.isEmpty()) {
				answer = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「回答」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「回答」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("answer", parentNode, answer == null ? "" : answer);

		/*
		 排序
		 */
		Short ordinal = entity.getOrdinal();
		if (id == null) {
			ordinal = Short.valueOf(Integer.toString(frequentlyAskedQuestionRepository.findAll().size() + 1));
		}

		if (errorMessage == null) {
			entity.setQuestion(question);
			entity.setAnswer(answer);
			entity.setOrdinal(ordinal);
			frequentlyAskedQuestionRepository.saveAndFlush(entity);

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
		for (FrequentlyAskedQuestion entity : frequentlyAskedQuestionRepository.findAll(new Sort(Sort.Direction.ASC, "ordinal"))) {
			if (ordinal != entity.getOrdinal()) {
				entity.setOrdinal(ordinal);
				frequentlyAskedQuestionRepository.saveAndFlush(entity);
			}
			ordinal++;
		}
	}
}
