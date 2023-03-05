package net.tourmap.marketing.repository;

import java.util.Collection;
import net.tourmap.marketing.entity.Youtube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * 影片
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface YoutubeRepository extends JpaRepository<Youtube, Short> {

	public long countByUrl(@Param("url") String url);

	public long countByUrlAndIdNot(@Param("url") String url, @Param("id") Short id);

	public Youtube findOndByOrdinal(@Param("ordinal") short ordinal);

	public Collection<Youtube> findTop5ByOrderByOrdinal();
}
