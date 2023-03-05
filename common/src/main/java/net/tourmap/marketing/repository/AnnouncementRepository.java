package net.tourmap.marketing.repository;

import java.util.Collection;
import net.tourmap.marketing.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 活動消息
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Short> {

	public Collection<Announcement> findTop5ByOrderByWhenDesc();
}
