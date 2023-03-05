package net.tourmap.marketing.repository;

import net.tourmap.marketing.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 收費項目
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface AccountRepository extends JpaRepository<Account, Short> {
}
