package net.tourmap.marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 影片
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Youtube.findAll", query = "SELECT y FROM Youtube y")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"Youtube\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"url\""}),
	@UniqueConstraint(columnNames = {"\"ordinal\""})
})
public class Youtube implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"title\"", length = 2147483647)
	private String title;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(unique = true, nullable = false, name = "\"url\"", length = 2147483647)
	private String url;

	@Basic(optional = false)
	@NotNull
	@Column(unique = true, nullable = false, name = "\"ordinal\"")
	private short ordinal;

	/**
	 * 建構子
	 */
	public Youtube() {
	}

	/**
	 * @param id 主鍵
	 */
	protected Youtube(Short id) {
		this.id = id;
	}

	/**
	 * @param title 標題
	 * @param url 完整網址
	 * @param ordinal 排序
	 */
	public Youtube(String title, String url, short ordinal) {
		this.title = title;
		this.url = url;
		this.ordinal = ordinal;
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
		if (!(object instanceof Youtube)) {
			return false;
		}
		Youtube other = (Youtube) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.Youtube[ id=" + id + " ]";
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
	 * @return 標題
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title 標題
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return 完整網址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url 完整網址
	 */
	public void setUrl(String url) {
		this.url = url;
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
}
