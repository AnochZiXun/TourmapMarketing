package net.tourmap.marketing.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Bulletin;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Element;

/**
 * 公佈欄
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class BulletinService {

	@org.springframework.beans.factory.annotation.Autowired
	net.tourmap.marketing.repository.BulletinRepository bulletinRepository;

	@Autowired
	javax.servlet.ServletContext context;

	/**
	 * 載入
	 *
	 * @param entity 實體
	 * @param parentNode 父元素
	 */
	public void load(Bulletin entity, Element parentNode) {
		String filename = entity.getFilename();//檔案名稱
		Utils.createElementWithTextContent("filename", parentNode, filename == null ? "" : filename);
	}

	/**
	 * 儲存
	 *
	 * @param entity 實體
	 * @param multipartFile 檔案
	 * @param parentNode 父元素
	 * @return 錯誤訊息
	 */
	@SuppressWarnings("null")
	public String save(Bulletin entity, MultipartFile multipartFile, Element parentNode) throws Exception {
		String errorMessage = null;
		Short id = entity.getId();

		/*
		 檔案
		 */
		byte[] content = null;
		try {
			content = multipartFile.getBytes();
			if (content.length == 0) {
				content = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			if (id == null) {
				errorMessage = "新增時「檔案」為必選！";
			}
		} catch (Exception ignore) {
			errorMessage = "選擇的「檔案」出現不明的錯誤！";
		}

		/*
		 檔案名稱
		 */
		String filename = null;
		if (content != null) {
			filename = multipartFile.getOriginalFilename();
			if (id == null) {
				if (bulletinRepository.countByFilename(filename) > 0) {
					errorMessage = "已存在相同的檔案名稱！";
				}
			}
		} else if (id != null) {
			filename = entity.getFilename();
			if (bulletinRepository.countByFilenameAndIdNot(filename, id) > 0) {
				errorMessage = "已存在相同的檔案名稱！";
			}
		}
		Utils.createElementWithTextContent("filename", parentNode, filename);

		/*
		 排序
		 */
		Short ordinal = entity.getOrdinal();
		if (id == null) {
			ordinal = Short.valueOf(Integer.toString(bulletinRepository.findAll().size() + 1));
		}

		if (errorMessage == null) {
			if (content != null) {
				InputStream inputStream = new ByteArrayInputStream(content);

				//Path path = Paths.get(context.getRealPath(context.getContextPath()), "bulletin", filename);
				Path path;
				if (System.getProperty("os.name").equalsIgnoreCase("Windows")) {
					path = Paths.get(context.getRealPath(context.getContextPath()), "bulletin", filename);
				} else {
					path = Paths.get("/var/lib/tomcat7/webapps/http/ROOT", "bulletin", filename);
				}
				File file = path.toFile();
				file.createNewFile();

				FileOutputStream fileOutputStream = new FileOutputStream(file);
				int i;
				while ((i = inputStream.read()) != -1) {
					fileOutputStream.write(i);
				}
				IOUtils.closeQuietly(fileOutputStream);

				IOUtils.closeQuietly(inputStream);
			}
			entity.setFilename(filename);
			entity.setOrdinal(ordinal);
			bulletinRepository.saveAndFlush(entity);

			sort();

			return null;
		}
		return errorMessage;
	}

	/**
	 * 排序
	 */
	public void sort() {
		short ordinal = 1;
		for (Bulletin entity : bulletinRepository.findAll(new Sort(Sort.Direction.ASC, "ordinal"))) {
			if (ordinal != entity.getOrdinal()) {
				entity.setOrdinal(ordinal);
				bulletinRepository.saveAndFlush(entity);
			}
			ordinal++;
		}
	}
}
