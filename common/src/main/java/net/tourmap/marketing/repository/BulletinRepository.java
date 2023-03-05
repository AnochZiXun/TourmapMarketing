package net.tourmap.marketing.repository;

import net.tourmap.marketing.entity.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * 公佈欄
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Short> {

	public long countByFilename(@Param("filename") String filename);

	public long countByFilenameAndIdNot(@Param("filename") String filename, @Param("id") Short id);

	public Bulletin findOndByOrdinal(@Param("ordinal") short ordinal);
}
