package net.tourmap.marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 身份
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"Role\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"name\""})
})
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 8)
	@Column(nullable = false, name = "\"name\"", length = 8)
	private String name;

	/**
	 * 建構子
	 */
	public Role() {
	}

	/**
	 * @param id 主鍵
	 */
	protected Role(Short id) {
		this.id = id;
	}

	/**
	 * @param name 身份名稱
	 */
	public Role(String name) {
		this.name = name;
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
		if (!(object instanceof Role)) {
			return false;
		}
		Role other = (Role) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.Role[ id=" + id + " ]";
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
	 * @return 身份名稱
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 身份名稱
	 */
	public void setName(String name) {
		this.name = name;
	}
}
