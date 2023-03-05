package net.tourmap.marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 公佈欄
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Bulletin.findAll", query = "SELECT b FROM Bulletin b")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"Bulletin\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"filename\""}),
	@UniqueConstraint(columnNames = {"\"ordinal\""})
})
public class Bulletin implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(unique = true, nullable = false, name = "\"filename\"", length = 2147483647)
	private String filename;

	@Basic(optional = false)
	@NotNull
	@Column(unique = true, nullable = false, name = "\"ordinal\"")
	private short ordinal;

	/**
	 * 建構子
	 */
	public Bulletin() {
	}

	/**
	 * @param id 主鍵
	 */
	public Bulletin(Short id) {
		this.id = id;
	}

	/**
	 * @param filename 檔案名稱
	 * @param ordinal 排序
	 */
	public Bulletin(String filename, short ordinal) {
		this.filename = filename;
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
		if (!(object instanceof Bulletin)) {
			return false;
		}
		Bulletin other = (Bulletin) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.Bulletin[ id=" + id + " ]";
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
	 * @return 檔案名稱
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename 檔案名稱
	 */
	public void setFilename(String filename) {
		this.filename = filename;
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
