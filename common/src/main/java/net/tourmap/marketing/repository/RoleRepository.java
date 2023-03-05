package net.tourmap.marketing.repository;

import net.tourmap.marketing.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 身份
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface RoleRepository extends JpaRepository<Role, Short> {
}
