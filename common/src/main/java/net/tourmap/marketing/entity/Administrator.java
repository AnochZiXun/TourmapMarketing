package net.tourmap.marketing.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * userTable
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"Administrator\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"login\""})
})
public class Administrator implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false)
	private Short id;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"alias\"", length = 2147483647)
	private String alias;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(nullable = false, name = "\"login\"", length = 2147483647)
	private String login;

	@Size(max = 2147483647)
	@Column(name = "\"shadow\"", length = 2147483647)
	private String shadow;

	/**
	 * 建構子
	 */
	public Administrator() {
	}

	/**
	 * @param id 主鍵
	 */
	protected Administrator(Short id) {
		this.id = id;
	}

	/**
	 * @param login userNameCol
	 */
	public Administrator(String login) {
		this.login = login;
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
		if (!(object instanceof Administrator)) {
			return false;
		}
		Administrator other = (Administrator) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.Administrator[ id=" + id + " ]";
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
	 * @return 別名
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias 別名
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return userNameCol
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login userNameCol
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return userCredCol
	 */
	public String getShadow() {
		return shadow;
	}

	/**
	 * @param shadow userCredCol
	 */
	public void setShadow(String shadow) {
		this.shadow = shadow;
	}
}
