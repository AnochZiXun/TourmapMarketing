package net.tourmap.marketing.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 共用服務層
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
public class Services {

	@Autowired
	javax.servlet.ServletContext context;

	/**
	 * @return 此 context 的實體路徑
	 */
	public String getContextRealPath() {
		return context.getRealPath(context.getContextPath());
	}

	public String getWhoever(Integer me, String role, HttpSession session) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/" + role + "/" + me.toString() + ".json");
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = closeableHttpResponse.getEntity();
				if (httpEntity != null) {
					InputStream inputStream = httpEntity.getContent();
					try {
						JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
						if (jsonObject.has("result") && !jsonObject.isNull("result")) {
							return jsonObject.getJSONObject("result").getString("name");
						}
					} finally {
						IOUtils.closeQuietly(inputStream);
					}
				}
			}
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + ":\n\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}
		return "(無法取得名稱)";
	}

	public boolean hasVendorBusinessIdentificationNumber(String experiment) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/vendor/");
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject vendor = jsonArray.getJSONObject(i);
						String businessIdentificationNumber = vendor.isNull("businessIdentificationNumber") ? null : vendor.getString("businessIdentificationNumber");
						if (businessIdentificationNumber != null && experiment.equalsIgnoreCase(businessIdentificationNumber)) {
							return true;
						}
					}
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + ":\n\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}
		return false;
	}

	public boolean hasAdvertiserBusinessIdentificationNumber(String experiment) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://restful.tour-map.net/advertiser/");
		CloseableHttpResponse closeableHttpResponse = null;
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject advertiser = jsonArray.getJSONObject(i);
						String businessIdentificationNumber = advertiser.isNull("businessIdentificationNumber") ? null : advertiser.getString("businessIdentificationNumber");
						if (businessIdentificationNumber != null && experiment.equalsIgnoreCase(businessIdentificationNumber)) {
							return true;
						}
					}
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + ":\n\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}
		return false;
	}

	public boolean hasLogin(String experiment) {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = null;
		CloseableHttpResponse closeableHttpResponse = null;

		try {
			httpGet = new HttpGet("http://restful.tour-map.net/vendor/");
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject vendorObject = jsonArray.getJSONObject(i);
						String login = null, email = null;
						if (vendorObject.has("login") && !vendorObject.isNull("login")) {
							login = vendorObject.getString("login");
							if (login != null && experiment.equalsIgnoreCase(login)) {
								return true;
							}
						}
						if (vendorObject.has("email") && !vendorObject.isNull("email")) {
							email = vendorObject.getString("email");
							if (email != null && experiment.equalsIgnoreCase(email)) {
								return true;
							}
						}
					}
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + ":\n\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		try {
			httpGet = new HttpGet("http://restful.tour-map.net/agent/");
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject agentObject = jsonArray.getJSONObject(i);
						String login = null;
						if (agentObject.has("login") && !agentObject.isNull("login")) {
							login = agentObject.getString("login");
							if (login != null && experiment.equalsIgnoreCase(login)) {
								return true;
							}
						}
					}
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + ":\n\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		try {
			httpGet = new HttpGet("http://restful.tour-map.net/advertiser/");
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				try {
					JSONObject jsonObject = new JSONObject(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
					JSONArray jsonArray = jsonObject.getJSONArray("result");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject agentObject = jsonArray.getJSONObject(i);
						String login = null;
						if (agentObject.has("login") && !agentObject.isNull("login")) {
							login = agentObject.getString("login");
							if (login != null && experiment.equalsIgnoreCase(login)) {
								return true;
							}
						}
					}
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (IOException ioException) {
			System.err.println(getClass().getCanonicalName() + ":\n\t" + ioException.getLocalizedMessage());
		} finally {
			IOUtils.closeQuietly(closeableHttpResponse);
		}

		IOUtils.closeQuietly(closeableHttpClient);
		return false;
	}
}
