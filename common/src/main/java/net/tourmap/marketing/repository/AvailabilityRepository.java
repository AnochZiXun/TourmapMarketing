package net.tourmap.marketing.repository;

import net.tourmap.marketing.entity.Availability;
import net.tourmap.marketing.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * 有效日期
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

	public Availability findOneByRoleAndPrimaryKey(@Param("role") Role role, @Param("primaryKey") int primaryKey);
}
