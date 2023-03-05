package net.tourmap.marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 問題Q&A
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "FrequentlyAskedQuestion.findAll", query = "SELECT f FROM FrequentlyAskedQuestion f")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"FrequentlyAskedQuestion\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"ordinal\""})
})
public class FrequentlyAskedQuestion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"question\"", length = 2147483647)
	private String question;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"answer\"", length = 2147483647)
	private String answer;

	@Basic(optional = false)
	@NotNull
	@Column(unique = true, nullable = false, name = "\"ordinal\"")
	private short ordinal;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false, name = "\"consumer\"")
	private boolean consumer;

	/**
	 * 建構子
	 */
	public FrequentlyAskedQuestion() {
	}

	/**
	 * @param id 主鍵
	 */
	protected FrequentlyAskedQuestion(Short id) {
		this.id = id;
		this.consumer = false;
	}

	/**
	 * @param question 問題
	 * @param answer 回答
	 * @param ordinal 排序
	 */
	public FrequentlyAskedQuestion(String question, String answer, short ordinal) {
		this.question = question;
		this.answer = answer;
		this.ordinal = ordinal;
		this.consumer = false;
	}

	/**
	 * @param question 問題
	 * @param answer 回答
	 * @param ordinal 排序
	 * @param isConsumer 企業|消費者
	 */
	public FrequentlyAskedQuestion(String question, String answer, short ordinal, boolean isConsumer) {
		this.question = question;
		this.answer = answer;
		this.ordinal = ordinal;
		this.consumer = isConsumer;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof FrequentlyAskedQuestion)) {
			return false;
		}
		FrequentlyAskedQuestion other = (FrequentlyAskedQuestion) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.FrequentlyAskedQuestion[ id=" + id + " ]";
	}

	/**
	 * @return 主鍵
	 */
	public Short getId() {
		return id;
	}

	/**
	 * @param id 主鍵
	 */
	public void setId(Short id) {
		this.id = id;
	}

	/**
	 * @return 問題
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question 問題
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return 回答
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer 回答
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return 排序
	 */
	public short getOrdinal() {
		return ordinal;
	}

	/**
	 * @param ordinal 排序
	 */
	public void setOrdinal(short ordinal) {
		this.ordinal = ordinal;
	}

	/**
	 * @return 企業|消費者
	 */
	public boolean isConsumer() {
		return consumer;
	}

	/**
	 * @param consumer 企業|消費者
	 */
	public void setConsumer(boolean consumer) {
		this.consumer = consumer;
	}
}
