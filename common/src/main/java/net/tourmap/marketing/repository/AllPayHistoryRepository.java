package net.tourmap.marketing.repository;

import net.tourmap.marketing.entity.AllPayHistory;
import net.tourmap.marketing.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * 歐付寶付款歷程
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface AllPayHistoryRepository extends JpaRepository<AllPayHistory, Long> {

	public AllPayHistory findOneByAvailability(@Param("availability") Availability availability);

	public AllPayHistory findOneByMerchantTradeNo(@Param("merchantTradeNo") String merchantTradeNo);
}
