package net.tourmap.marketing.entity;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 歐付寶付款歷程
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "AllPayHistory.findAll", query = "SELECT a FROM AllPayHistory a")
})
@Table(catalog = "\"tourmap\"", schema = "\"public\"", name = "\"AllPayHistory\"", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"merchantTradeNo"})
})
public class AllPayHistory implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(nullable = false, name = "\"id\"")
	private Long id;

	@JoinColumn(name = "\"availability\"", referencedColumnName = "\"id\"", nullable = false)
	@ManyToOne(optional = false)
	private Availability availability;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32767)
	@Column(nullable = false, name = "\"merchantTradeNo\"", length = 32767)
	private String merchantTradeNo;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32767)
	@Column(nullable = false, name = "\"itemName\"", length = 32767)
	private String itemName;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 32767)
	@Column(nullable = false, name = "\"checkMacValue\"", length = 32767)
	private String checkMacValue;

	@Column(name = "\"rtnCode\"")
	private Short rtnCode;

	@Size(max = 32767)
	@Column(name = "\"rtnMsg\"", length = 32767)
	private String rtnMsg;

	@Size(max = 32767)
	@Column(name = "\"tradeNo\"", length = 32767)
	private String tradeNo;

	@Column(name = "\"tradeAmt\"")
	private Integer tradeAmt;

	@Column(name = "\"paymentDate\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date paymentDate;

	@Size(max = 32767)
	@Column(name = "\"paymentType\"", length = 32767)
	private String paymentType;

	@Column(name = "\"paymentTypeChargeFee\"")
	private Integer paymentTypeChargeFee;

	@Column(name = "\"tradeDate\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradeDate;

	@Column(name = "\"simulatePaid\"")
	private Short simulatePaid;

	@Size(max = 32767)
	@Column(name = "\"returnCheckMacValue\"", length = 32767)
	private String returnCheckMacValue;

	@JoinColumn(name = "\"account\"", referencedColumnName = "\"id\"", nullable = false)
	@ManyToOne(optional = false)
	private Account account;

	/**
	 * 建構子
	 */
	public AllPayHistory() {
	}

	/**
	 * @param id 主鍵
	 */
	protected AllPayHistory(Long id) {
		this.id = id;
	}

	/**
	 * @param availability 有效日期
	 * @param merchantTradeNo 交易編號
	 * @param itemName 商品名稱
	 * @param checkMacValue (請求的)檢查碼
	 * @param account 收費項目
	 */
	public AllPayHistory(Availability availability, String merchantTradeNo, String itemName, String checkMacValue, Account account) {
		this.availability = availability;
		this.merchantTradeNo = merchantTradeNo;
		this.itemName = itemName;
		this.checkMacValue = checkMacValue;
		this.account = account;
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
		if (!(object instanceof AllPayHistory)) {
			return false;
		}
		AllPayHistory other = (AllPayHistory) object;
		return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
	}

	@Override
	public String toString() {
		return "net.tourmap.marketing.entity.AllPayHistory[ id=" + id + " ]";
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
	 * @return 有效日期
	 */
	public Availability getAvailability() {
		return availability;
	}

	/**
	 * @param availability 有效日期
	 */
	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	/**
	 * @return 交易編號
	 */
	public String getMerchantTradeNo() {
		return merchantTradeNo;
	}

	/**
	 * @param merchantTradeNo 交易編號
	 */
	public void setMerchantTradeNo(String merchantTradeNo) {
		this.merchantTradeNo = merchantTradeNo;
	}

	/**
	 * @return 商品名稱
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName 商品名稱
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return (請求的)檢查碼
	 */
	public String getCheckMacValue() {
		return checkMacValue;
	}

	/**
	 * @param checkMacValue (請求的)檢查碼
	 */
	public void setCheckMacValue(String checkMacValue) {
		this.checkMacValue = checkMacValue;
	}

	/**
	 * @return 交易狀態
	 */
	public Short getRtnCode() {
		return rtnCode;
	}

	/**
	 * @param rtnCode 交易狀態
	 */
	public void setRtnCode(Short rtnCode) {
		this.rtnCode = rtnCode;
	}

	/**
	 * @return 交易訊息
	 */
	public String getRtnMsg() {
		return rtnMsg;
	}

	/**
	 * @param rtnMsg 交易訊息
	 */
	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

	/**
	 * @return 交易編號
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * @param tradeNo 交易編號
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * @return 交易金額
	 */
	public Integer getTradeAmt() {
		return tradeAmt;
	}

	/**
	 * @param tradeAmt 交易金額
	 */
	public void setTradeAmt(Integer tradeAmt) {
		this.tradeAmt = tradeAmt;
	}

	/**
	 * @return 付款時間
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate 付款時間
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return 會員選擇的付款方式
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType 會員選擇的付款方式
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return 通路費
	 */
	public Integer getPaymentTypeChargeFee() {
		return paymentTypeChargeFee;
	}

	/**
	 * @param paymentTypeChargeFee 通路費
	 */
	public void setPaymentTypeChargeFee(Integer paymentTypeChargeFee) {
		this.paymentTypeChargeFee = paymentTypeChargeFee;
	}

	/**
	 * @return 訂單成立時間
	 */
	public Date getTradeDate() {
		return tradeDate;
	}

	/**
	 * @param tradeDate 訂單成立時間
	 */
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	/**
	 * @return 是否為模擬付款
	 */
	public Short getSimulatePaid() {
		return simulatePaid;
	}

	/**
	 * @param simulatePaid 是否為模擬付款
	 */
	public void setSimulatePaid(Short simulatePaid) {
		this.simulatePaid = simulatePaid;
	}

	/**
	 * @return 回覆的檢查碼
	 */
	public String getReturnCheckMacValue() {
		return returnCheckMacValue;
	}

	/**
	 * @param returnCheckMacValue 回覆的檢查碼
	 */
	public void setReturnCheckMacValue(String returnCheckMacValue) {
		this.returnCheckMacValue = returnCheckMacValue;
	}

	/**
	 * @return 收費項目
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account 收費項目
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
}
