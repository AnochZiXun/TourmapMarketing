package net.tourmap.marketing.entity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 有效日期
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "Availability.findAll", query = "SELECT a FROM Availability a")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"Availability\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"\"role\"", "\"primaryKey\""})
})
public class Availability implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Long id;

	@JoinColumn(name = "\"role\"", referencedColumnName = "\"id\"", nullable = false)
	@ManyToOne(optional = false)
	private Role role;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false, name = "\"primaryKey\"")
	private int primaryKey;

	@Basic(optional = false)
	@NotNull
	@Column(nullable = false, name = "\"oneTime\"")
	private boolean oneTime;

	@Column(name = "\"monthly\"")
	@Temporal(TemporalType.DATE)
	private Date monthly;

	/**
	 * 建構子
	 */
	public Availability() {
	}

	/**
	 * @param id 主鍵
	 */
	protected Availability(Long id) {
		this.id = id;
	}

	/**
	 * @param role 身份
	 * @param primaryKey 對應至旅圖觀止資料表的主鍵
	 * @param oneTime 一次性網頁製作已繳
	 */
	public Availability(Role role, int primaryKey, boolean oneTime) {
		this.role = role;
		this.primaryKey = primaryKey;
		this.oneTime = oneTime;
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
		if (!(object instanceof Availability)) {
			return false;
		}
		Availability other = (Availability) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.Availability[ id=" + id + " ]";
	}

	/**
	 * @return 主鍵
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id 主鍵
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 身份
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role 身份
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return 對應至旅圖觀止資料表的主鍵
	 */
	public int getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey 對應至旅圖觀止資料表的主鍵
	 */
	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * @return 一次性網頁製作已繳
	 */
	public boolean getOneTime() {
		return oneTime;
	}

	/**
	 * @param oneTime 一次性網頁製作已繳
	 */
	public void setOneTime(boolean oneTime) {
		this.oneTime = oneTime;
	}

	/**
	 * @return 每月上架有效日期
	 */
	public Date getMonthly() {
		return monthly;
	}

	/**
	 * @param monthly 每月上架有效日期
	 */
	public void setMonthly(Date monthly) {
		this.monthly = monthly;
	}
}
