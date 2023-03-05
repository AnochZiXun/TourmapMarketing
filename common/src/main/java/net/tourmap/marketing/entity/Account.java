package net.tourmap.marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 收費項目
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"Account\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"name\""})
})
public class Account implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(nullable = false, name = "\"id\"")
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"name\"", length = 2147483647)
	private String name;

	/**
	 * 建構子
	 */
	public Account() {
	}

	/**
	 * @param id 主鍵
	 */
	protected Account(Short id) {
		this.id = id;
	}

	/**
	 * @param name 收費項目
	 */
	public Account(String name) {
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
		if (!(object instanceof Account)) {
			return false;
		}
		Account other = (Account) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.Account[ id=" + id + " ]";
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
	 * @return 收費項目
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 收費項目
	 */
	public void setName(String name) {
		this.name = name;
	}
}
