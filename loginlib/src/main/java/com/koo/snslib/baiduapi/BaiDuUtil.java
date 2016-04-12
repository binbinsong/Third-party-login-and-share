/**
 * Copyright (c) 2011 Baidu.com, Inc. All Rights Reserved
 */
package com.koo.snslib.baiduapi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
  *
 * @author chenhetong(chenhetong@baidu.com)
 * 
 */
public class BaiDuUtil {

	private static class DefaultTrustManager implements X509TrustManager {

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] cert, String oauthType)
				throws java.security.cert.CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] cert, String oauthType)
				throws java.security.cert.CertificateException {
		}
	}

	public static boolean ENABLE_LOG = true;


	public static boolean isEmpty(String query) {
		boolean ret = false;
		if (query == null || query.trim().equals("")) {
			ret = true;
		}
		return ret;
	}


	public static String encodeUrl(Bundle params) {
		if (params == null || params.isEmpty()) {
			return null;
		}
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (String key : params.keySet()) {
			String paramValue = params.getString(key);
			if (paramValue == null) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(URLEncoder.encode(key)).append("=")
					.append(URLEncoder.encode(paramValue));
		}
		return sb.toString();
	}


	public static Bundle decodeUrl(String query) {
		Bundle ret = new Bundle();
		if (query != null) {
			String[] pairs = query.split("&");
			for (String pair : pairs) {
				String[] keyAndValues = pair.split("=");
				if (keyAndValues != null && keyAndValues.length == 2) {
					String key = keyAndValues[0];
					String value = keyAndValues[1];
					if (!isEmpty(key) && !isEmpty(value)) {
						ret.putString(URLDecoder.decode(key),
								URLDecoder.decode(value));
					}
				}
			}
		}
		return ret;
	}


	public static Bundle parseUrl(String url) {
		Bundle ret;
		url = url.replace("bdconnect", "http");
		try {
			URL urlParam = new URL(url);
			ret = decodeUrl(urlParam.getQuery());
			ret.putAll(decodeUrl(urlParam.getRef()));
			return ret;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}


	public static String openUrl(String url, String method, Bundle params)
			throws IOException {
		HttpURLConnection conn;
		String response;
		String charset = "UTF-8";
		InputStream is = null;
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		try {
			if ("GET".equals(method)) {
				url = url + "?" + encodeUrl(params);
				conn = getURLConnection(url, method, ctype);
			} else {
				conn = getURLConnection(url, method, ctype);
				String query = encodeUrl(params);
				byte[] content = query.getBytes(charset);
				conn.getOutputStream().write(content);
			}
			int respCode = conn.getResponseCode();
			if (200 == respCode) {
				is = conn.getInputStream();
			} else {
				is = conn.getErrorStream();
			}
			response = read(is);
			return response;
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
		}
	}


	public static String uploadFile(String url, Bundle parameters)
			throws IOException {
		String charset = "UTF-8";
		String boundary = System.currentTimeMillis() + "";
		String ctype = "multipart/form-data;charset=" + charset + ";boundary="
				+ boundary;
		String method = "POST";
		String response;
		OutputStream out = null;
		InputStream is = null;
		HttpURLConnection conn;
		try {
			conn = getURLConnection(url, method, ctype);
			out = conn.getOutputStream();
			byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n")
					.getBytes(charset);
 			if (parameters != null) {
				for (String key : parameters.keySet()) {
					Object paramValue = parameters.get(key);
					if ((paramValue instanceof byte[])) {
 						byte[] fileParameters = getFileParameters(key,
								"content/unknown", charset);
						out.write(entryBoundaryBytes);
						out.write(fileParameters);
						out.write((byte[]) paramValue);
						continue;
					}
					String value = (String) paramValue;
					byte[] textParameters = getTextParameters(key, value,
							charset);
					out.write(entryBoundaryBytes);
					out.write(textParameters);
				}
			}

 			byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n")
					.getBytes(charset);
			out.write(endBoundaryBytes);
			out.flush();
			int respCode = conn.getResponseCode();
			if (respCode == 200) {
				is = conn.getInputStream();
			} else {
				is = conn.getErrorStream();
			}
			response = read(is);
			return response;
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
			if (out != null) {
				out.close();
				out = null;
			}
		}
	}


	private static byte[] getTextParameters(String fieldName,
			String fieldValue, String charset)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("Content-Disposition:form-data;name=\"");
		sb.append(fieldName);
		sb.append("\"Content-Type:text/plain\r\n\r\n");
		sb.append(fieldValue);
		return sb.toString().getBytes(charset);
	}


	private static byte[] getFileParameters(String fileName,
			String contentType, String charset)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append("Content-Disposition:form-data;name=\"");
		sb.append("upload");
		sb.append("\";filename=\"");
		sb.append(fileName);
		sb.append("\"\r\nContent-Type:");
		sb.append(contentType);
		sb.append("\r\n\r\n");
		return sb.toString().getBytes(charset);
	}


	private static String read(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader bf = new BufferedReader(new InputStreamReader(is), 1000);
		String str = null;
		while ((str = bf.readLine()) != null) {
			sb.append(str);
		}
		bf.close();
		bf = null;
		return sb.toString();
	}


	private static HttpURLConnection getURLConnection(String url,
			String method, String ctype) throws MalformedURLException,
			IOException {
		HttpURLConnection conn;
		URL urlObj = new URL(url);
		if ("https".equalsIgnoreCase(urlObj.getProtocol())) {
			SSLContext ctx;
			try {
				ctx = SSLContext.getInstance("TLS");
				ctx.init(new KeyManager[0],
						new TrustManager[] { new DefaultTrustManager() },
						new SecureRandom());
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
			HttpsURLConnection connHttps = (HttpsURLConnection) urlObj
					.openConnection();
			connHttps.setSSLSocketFactory(ctx.getSocketFactory());
			connHttps.setHostnameVerifier(new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			conn = connHttps;
		} else {
			conn = (HttpURLConnection) urlObj.openConnection();
		}
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("User-Agent", System.getProperties()
				.getProperty("http.agent")
				+ " BaiduOpenApiAndroidSDK "
				+ " os: "
				+ android.os.Build.VERSION.SDK
				+ ","
				+ android.os.Build.VERSION.RELEASE);
		conn.setRequestProperty("Content-Type", ctype);
		conn.setRequestProperty("Connection", "Keep-Alive");
		return conn;
	}

	public static void clearCookies(Context context) {
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncManager = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	public static void checkResponse(String response) throws BaiduException {
		if (isEmpty(response)) {
			return;
		}
		JSONObject json;
		try {
			json = new JSONObject(response);
 			if (json.has("error") && json.has("error_description")) {
				String errorCode = json.getString("errorCode");
				String errorDesp = json.getString("error_description");
				throw new BaiduException(errorCode, errorDesp);
			}
 			if (json.has("error_code") && json.has("error_msg")) {
				String errorCode = json.getString("erro_code");
				String errorMsg = json.getString("error_msg");
				throw new BaiduException(errorCode, errorMsg);
			}
		} catch (JSONException e) {
			BaiDuUtil.logd("Baidu Parse Json Exception ", "" + e);
		}

	}

	public static void showAlert(Context context, String title, String text) {
		AlertDialog alertDialog = new Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(text);
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
	}


	public static void logd(String tag, String msg) {
		if (ENABLE_LOG) {
			Log.d(tag, msg);
		}
	}

}
