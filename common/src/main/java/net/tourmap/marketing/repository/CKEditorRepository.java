package net.tourmap.marketing.repository;

import net.tourmap.marketing.entity.CKEditor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 編輯器
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface CKEditorRepository extends JpaRepository<CKEditor, Short> {
}
