package net.tourmap.marketing.repository;

import net.tourmap.marketing.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * userTable
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Short> {

	public long countByLogin(@Param("login") String login);

	public long countByLoginAndIdNot(@Param("login") String login, @Param("id") Short id);
}
