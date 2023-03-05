package net.tourmap.marketing.service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import net.tourmap.marketing.Utils;
import net.tourmap.marketing.entity.Administrator;
import org.springframework.transaction.annotation.*;
import org.w3c.dom.Element;

/**
 * 管理者
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class AdministratorService {

	@org.springframework.beans.factory.annotation.Autowired
	net.tourmap.marketing.repository.AdministratorRepository administratorRepository;

	/**
	 * 載入
	 *
	 * @param entity 實體
	 * @param parentNode 父元素
	 */
	public void load(Administrator entity, Element parentNode) {
		String alias = entity.getAlias();//別名
		Utils.createElementWithTextContent("alias", parentNode, alias == null ? "" : alias);

		String login = entity.getLogin();//帳號(電子郵件)
		Utils.createElementWithTextContent("login", parentNode, login == null ? "" : login);
	}

	/**
	 * 儲存
	 *
	 * @param entity 實體
	 * @param alias 標題
	 * @param login 帳號(電子郵件)
	 * @param parentNode 父元素
	 * @return 錯誤訊息
	 */
	@SuppressWarnings("null")
	public String save(Administrator entity, String alias, String login, Element parentNode) {
		String errorMessage = null;
		Short id = entity.getId();

		/*
		 別名
		 */
		try {
			alias = alias.trim();
			if (alias.isEmpty()) {
				alias = null;
				throw new NullPointerException();
			}
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「別名」為必填！";
		} catch (Exception ignore) {
			errorMessage = "輸入的「別名」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("title", parentNode, alias == null ? "" : alias);

		/*
		 帳號(電子郵件)
		 */
		try {
			login = login.trim();
			if (login.isEmpty()) {
				login = null;
				throw new NullPointerException();
			}
			if (id == null) {
				if (administratorRepository.countByLogin(login) > 0) {
					errorMessage = "重複的「帳號」！";
				}
			} else if (administratorRepository.countByLoginAndIdNot(login, id) > 0) {
				errorMessage = "重複的「帳號」！";
			}
			login = login.toLowerCase();
			new InternetAddress(login).validate();
		} catch (NullPointerException nullPointerException) {
			errorMessage = "「帳號」為必填！";
		} catch (AddressException ignore) {
			errorMessage = "輸入的「帳號」出現不明的錯誤！";
		}
		Utils.createElementWithTextContent("login", parentNode, login == null ? "" : login);

		if (errorMessage == null) {
			entity.setAlias(alias);
			entity.setLogin(login);
			administratorRepository.saveAndFlush(entity);

			return null;
		}
		return errorMessage;
	}
}
