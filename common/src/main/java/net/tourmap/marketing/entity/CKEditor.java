package net.tourmap.marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 編輯器
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "CKEditor.findAll", query = "SELECT c FROM CKEditor c")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"CKEditor\"")
public class CKEditor implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"markup\"", length = 2147483647)
	private String markup;

	/**
	 * 建構子
	 */
	public CKEditor() {
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
		if (!(object instanceof CKEditor)) {
			return false;
		}
		CKEditor other = (CKEditor) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.CKEditor[ id=" + id + " ]";
	}

	/**
	 * @param id 主鍵
	 */
	public CKEditor(Short id) {
		this.id = id;
	}

	/**
	 * @param markup 語法
	 */
	public CKEditor(String markup) {
		this.markup = markup;
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
	 * @return 語法
	 */
	public String getMarkup() {
		return markup;
	}

	/**
	 * @param markup 語法
	 */
	public void setMarkup(String markup) {
		this.markup = markup;
	}
}
