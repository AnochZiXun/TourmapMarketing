package net.tourmap.marketing.repository;

import java.util.Collection;
import net.tourmap.marketing.entity.FrequentlyAskedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * 問題Q&A
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface FrequentlyAskedQuestionRepository extends JpaRepository<FrequentlyAskedQuestion, Short> {

	public FrequentlyAskedQuestion findOndByOrdinal(@Param("ordinal") short ordinal);

	public Collection<FrequentlyAskedQuestion> findTop5ByOrderByOrdinal();
}
