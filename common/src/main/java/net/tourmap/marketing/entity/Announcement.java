package net.tourmap.marketing.entity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 活動消息
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Announcement.findAll", query = "SELECT a FROM Announcement a")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"Announcement\"")
public class Announcement implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"subject\"", length = 2147483647)
	private String subject;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"hyperTextMarkupLanguage\"", length = 2147483647)
	private String hyperTextMarkupLanguage;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false, name = "\"when\"")
	@Temporal(TemporalType.DATE)
	private Date when;

	/**
	 * 建構子
	 */
	public Announcement() {
	}

	/**
	 * @param id 主鍵
	 */
	protected Announcement(Short id) {
		this.id = id;
	}

	/**
	 * @param subject 主旨
	 * @param hyperTextMarkupLanguage HTML內容
	 * @param when 發佈日期
	 */
	public Announcement(String subject, String hyperTextMarkupLanguage, Date when) {
		this.subject = subject;
		this.hyperTextMarkupLanguage = hyperTextMarkupLanguage;
		this.when = when;
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
		if (!(object instanceof Announcement)) {
			return false;
		}
		Announcement other = (Announcement) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.Announcement[ id=" + id + " ]";
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
	 * @return 主旨
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject 主旨
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return HTML內容
	 */
	public String getHyperTextMarkupLanguage() {
		return hyperTextMarkupLanguage;
	}

	/**
	 * @param hyperTextMarkupLanguage HTML內容
	 */
	public void setHyperTextMarkupLanguage(String hyperTextMarkupLanguage) {
		this.hyperTextMarkupLanguage = hyperTextMarkupLanguage;
	}

	/**
	 * @return 發佈日期
	 */
	public Date getWhen() {
		return when;
	}

	/**
	 * @param when 發佈日期
	 */
	public void setWhen(Date when) {
		this.when = when;
	}
}
