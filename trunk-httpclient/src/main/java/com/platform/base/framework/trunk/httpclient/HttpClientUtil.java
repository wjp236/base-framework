package com.platform.base.framework.trunk.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient工具类,提供3种工具类
 * 1:普通的HTTPS链接
 * 2:需要使用SSL证书认证的HTTPS链接
 * 3:需要使用SSL链接,但是绕过SSL认证的HTTPS链接
 * @author zhangding<zhangding@enn.com>
 */
public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/** 普通链接管理器,不需要考虑SSL认证  */
	private PoolingHttpClientConnectionManager commonConnectionManager;
	/** 普通链接管理器,链接池默认最大链接总数 */
	private int defaultCommonCMMaxTotalConn = 700;
	/** 普通链接管理器,每个Host默认最大连接数 */
	private int defaultCommonCMMaxConnPerRoute = 60;
	/** 闲置链接超时时间,默认为60秒钟 */
	private int defaultCommonCMIdleConnTimeout = 60000;
	
	/** 绕过SSL证书认证的链接管理器 */
	private PoolingHttpClientConnectionManager ignoreSSLConnectionManager;
	/** 绕过SSL证书认证的链接管理器,链接池默认最大链接总数 */
	private int defaultIgnoreSSLCMMaxTotalConn = 300;
	/** 绕过SSL证书认证的链接管理器,每个Host默认最大连接数  */
	private int defaultIgnoreSSLCMMaxConnPerRoute = 30;
	/** 闲置链接超时时间,默认为60秒钟 */
	private int defaultIgnoreSSLCMIdleConnTimeout = 60000;
	
	/** 默认链接超时时间,默认为10秒钟 */
	private int defaultConnectionTimeout = 10000;
	/** 默认读取超时时间,默认为10秒钟 */
	private int defaultReadTimeout = 10000;
	/** 默认等待链接管理器HttpConnectionManager返回链接超时时间(只有在达到最大链接数时起作用),单位毫秒 */
	private int defaultConnectionManagerTimeout = 1000;
	
	/** 默认编码方式 */
	private static String DEFAULT_ENCODING = "UTF-8";
	
	/** 单例  */
	private HttpClientUtil() {
		initCommonConnectionManager();
		initIgnoreSSLConnectionManager();
	}
	private static HttpClientUtil instance = new HttpClientUtil();
	public static HttpClientUtil getInstance() {
		return instance;
	}
	
	/**
	 * 发送请求
	 * （作者：zhangding<zhangding@enn.com>）
	 * @param httpClientParam
	 * @return
	 * @throws Exception
	 */
	public String send(HttpClientParam httpClientParam) throws Exception{
		
		logger.info("---HttpClientParam---{}",httpClientParam);
		
		if(HttpMethodType.POST.equals(httpClientParam.getHttpMethodType())) {
			// 1.POST请求
			if(SSLType.NOSSL.equals(httpClientParam.getSSLType())) {
				return doPost(httpClientParam);
			}else if(SSLType.IGNORESSL.equals(httpClientParam.getSSLType())) {
				return doIgnoreSSLPost(httpClientParam);
			}else if(SSLType.VERIFYSSL.equals(httpClientParam.getSSLType())) {
				return doSSLPost(httpClientParam);
			}else {
				logger.error("------SSLType ERROR---不支持的HTTPS请求方式---");
				return null;
			}
			
		}else {
			logger.error("------HttpMethodType ERROR---不支持的请求方式---");
			return null;
		}
		
	} 
	
	/**
	 * 发送POST请求
	 * （作者：zhangding<zhangding@enn.com>）
	 * @param httpClientParam
	 * @return
	 * @throws Exception 
	 */
	private String doPost(HttpClientParam httpClientParam) throws Exception {
		
		// 1.请求设置
		RequestConfig requestConfig = createRequestConfig(httpClientParam);
		// 2.获取连接
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(commonConnectionManager)
				.setDefaultRequestConfig(requestConfig).build();
		// 3.设置编码格式
		String encoding = httpClientParam.getEncoding();
		if(encoding == null || encoding == "" || encoding.length() == 0) {
			encoding = DEFAULT_ENCODING;
		}
		
		String result = null;
		CloseableHttpResponse response = null;
        InputStream inputStream = null;
        
		String mimeType = httpClientParam.getMimeType() == null?"text/html":httpClientParam.getMimeType();
        
        HttpPost httpPost = new HttpPost(httpClientParam.getUrl());
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", mimeType+";"+"charset="+encoding);
		httpPost.setHeader("Accept",mimeType);
		
		StringEntity stringEntity = new StringEntity(httpClientParam.getRequestParam(),encoding);
		stringEntity.setContentEncoding(encoding);
        stringEntity.setContentType(mimeType);
        httpPost.setEntity(stringEntity);
        
        try {
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("---HttpClient Response Code Error --->{}",statusCode);
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				inputStream = entity.getContent();
                result = IOUtils.toString(inputStream, encoding);
                logger.info("---HttpClient Response HttpEntity Content--->{}",result);
			}
			
		} catch (Exception e) {
			logger.error("---HttpClient Execute Error --->{}",e);
		} finally {
			if(inputStream != null) {
				// 关闭流
				inputStream.close();
            }
            if(response != null) {
                // 将链接归还给链接池
            	response.close();
            }
//            if(httpClient != null) {
//            	// 关闭连接池
//                httpClient.close();
//            }
		}
		return result;
	}
	
	/**
	 * 发送POST请求,绕过SSL证书认证
	 * （作者：zhangding<zhangding@enn.com>）
	 * @param httpClientParam
	 * @return
	 * @throws Exception 
	 */
	private String doIgnoreSSLPost(HttpClientParam httpClientParam) throws Exception {
		
		// 1.请求设置
		RequestConfig requestConfig = createRequestConfig(httpClientParam);
		// 2.获取连接
		CloseableHttpClient httpClient = HttpClients.custom()
				.setConnectionManager(ignoreSSLConnectionManager)
				.setDefaultRequestConfig(requestConfig).build();
		// 3.设置编码格式
		String encoding = httpClientParam.getEncoding();
		if(encoding == null || encoding == "" || encoding.length() == 0) {
			encoding = DEFAULT_ENCODING;
		}
		
		String result = null;
		CloseableHttpResponse response = null;
        InputStream inputStream = null;
		
		String mimeType = httpClientParam.getMimeType() == null?"text/html":httpClientParam.getMimeType();
        
        HttpPost httpPost = new HttpPost(httpClientParam.getUrl());
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", mimeType+";"+"charset="+encoding);
		
		StringEntity stringEntity = new StringEntity(httpClientParam.getRequestParam(),encoding);
		stringEntity.setContentEncoding(encoding);
        stringEntity.setContentType(mimeType);
        httpPost.setEntity(stringEntity);
        
        try {
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("---HttpClient Response Code Error --->{}",statusCode);
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				inputStream = entity.getContent();
                result = IOUtils.toString(inputStream, encoding);
                logger.info("---HttpClient Response HttpEntity Content--->{}",result);
			}
			
		} catch (Exception e) {
			logger.error("---HttpClient Execute Error --->{}",e);
		} finally {
			if(inputStream != null) {
				// 关闭流
				inputStream.close();
            }
            if(response != null) {
                // 将链接归还给链接池
            	response.close();
            }
//            if(httpClient != null) {
//            	// 关闭连接池
//                httpClient.close();
//            }
		}
		return result;
	}
	
	/**
	 * 发送POST请求,需要SSL证书认证
	 * （作者：zhangding<zhangding@enn.com>）
	 * @param httpClientParam
	 * @return
	 * @throws Exception 
	 */
	private String doSSLPost(HttpClientParam httpClientParam) throws Exception {
		
		// 1.请求设置
		RequestConfig requestConfig = createRequestConfig(httpClientParam);
		// 2.获取连接
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(createSSLCSF(httpClientParam))
				.setDefaultRequestConfig(requestConfig).build();
		
		// 3.设置编码格式
		String encoding = httpClientParam.getEncoding();
		if(encoding == null || encoding == "" || encoding.length() == 0) {
			encoding = DEFAULT_ENCODING;
		}
		
		String result = null;
		CloseableHttpResponse response = null;
        InputStream inputStream = null;
		
        String mimeType = httpClientParam.getMimeType() == null?"text/html":httpClientParam.getMimeType();
        
        HttpPost httpPost = new HttpPost(httpClientParam.getUrl());
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", httpClientParam.getMimeType()+";"+"charset="+encoding);
		
		StringEntity stringEntity = new StringEntity(mimeType);
		stringEntity.setContentEncoding(encoding);
        stringEntity.setContentType(mimeType);
        httpPost.setEntity(stringEntity);
        
        try {
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				logger.error("---HttpClient Response Code Error --->{}",statusCode);
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				inputStream = entity.getContent();
                result = IOUtils.toString(inputStream, encoding);
                logger.info("---HttpClient Response HttpEntity Content--->{}",result);
			}
			
		} catch (Exception e) {
			logger.error("---HttpClient Execute Error --->{}",e);
		} finally {
			if (inputStream != null) {
				// 关闭流
				inputStream.close();
			}
			if (response != null) {
				// 将链接归还给链接池
				response.close();
			}
			if (httpClient != null) {
				// 关闭连接池
				httpClient.close();
			}
		}
		return result;
	}
	
	/**
	 * 设置请求参数
	 * （作者：zhangding<zhangding@enn.com>）
	 * @return
	 */
	private RequestConfig createRequestConfig(HttpClientParam httpClientParam) {
		
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 1.设置连接超时时间
		int connectionTimeOut = defaultConnectionTimeout;
		if(httpClientParam.getConnectionTimeOut() > 0) {
			connectionTimeOut = httpClientParam.getConnectionTimeOut();
		}
		configBuilder.setConnectTimeout(connectionTimeOut);
		// 2.设置读取时间
		int readTimeOut = defaultReadTimeout;
		if(httpClientParam.getReadTimeOut() > 0) {
			readTimeOut = httpClientParam.getReadTimeOut();
		}
        configBuilder.setSocketTimeout(readTimeOut);
        // 3.设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(defaultConnectionManagerTimeout);
        // 4.在提交请求之前测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        RequestConfig requestConfig = configBuilder.build();
        return requestConfig;
	}
	
	
	
	/**
	 * 初始化普通链接管理器
	 * （作者：zhangding<zhangding@enn.com>）
	 */
	private void initCommonConnectionManager() {
		commonConnectionManager = new PoolingHttpClientConnectionManager();
		commonConnectionManager.setMaxTotal(defaultCommonCMMaxTotalConn);
		commonConnectionManager.setDefaultMaxPerRoute(defaultCommonCMMaxConnPerRoute);
		commonConnectionManager.setValidateAfterInactivity(defaultCommonCMIdleConnTimeout);
	}
	
	/**
	 * 初始化绕过SSL证书认证的链接管理器
	 * （作者：zhangding<zhangding@enn.com>）
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	private void initIgnoreSSLConnectionManager() {
		
		try {
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
					createIgnoreVerifySSL(), new TrustAnyHostnameVerifier());
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
					.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", sslConnectionSocketFactory)
					.build();
			ignoreSSLConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			ignoreSSLConnectionManager.setMaxTotal(defaultIgnoreSSLCMMaxTotalConn);
			ignoreSSLConnectionManager.setDefaultMaxPerRoute(defaultIgnoreSSLCMMaxConnPerRoute);
			ignoreSSLConnectionManager.setValidateAfterInactivity(defaultIgnoreSSLCMIdleConnTimeout);
			
		} catch (Exception e) {
			logger.error("---绕过SSL证书认证的链接管理器初始化失败--->{}",e);
		} 
	}
	
	/**
	 * 绕过SSL证书验证
	 * （作者：zhangding<zhangding@enn.com>）
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
	    
		SSLContext sc = SSLContext.getInstance("SSLv3");  
	  
	    // 1.实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
	    X509TrustManager trustManager = new X509TrustManager() {  
	        @Override  
	        public void checkClientTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override  
	        public void checkServerTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override  
	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	            return null;  
	        }  
	    };  
	    sc.init(null, new TrustManager[] { trustManager }, null);  
	    return sc;  
	} 
	
	/**
	 *  During handshaking, if the URL's hostname and the server's identification hostname mismatch, 
	 *  the verification mechanism can call back to implementers of this interface to determine if this connection should be allowed.
	 *  The policies can be certificate-based or may depend on other authentication schemes.
	 *  These callbacks are used when the default rules for URL hostname verification fail.
	 */
	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	/**
	 * 创建SSL链接Socket工厂
	 * （作者：zhangding<zhangding@enn.com>）
	 * @param httpClientParam
	 * @return
	 * @throws Exception
	 */
	private SSLConnectionSocketFactory createSSLCSF(HttpClientParam httpClientParam) throws Exception {
		
		SSLContext sc = createSSLContext(httpClientParam.getKeyUrl(), httpClientParam.getKeyPassword());
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sc);
		return sslConnectionSocketFactory;
	}
	
	/**
	 * 创建SSLContext
	 * （作者：zhangding<zhangding@enn.com>）
	 * @param keyUrl
	 * @param keyPasswrd
	 * @return
	 * @throws Exception
	 */
	private SSLContext createSSLContext(String keyUrl, String keyPasswrd) throws Exception{
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream fileInputStream = new FileInputStream(new File(keyUrl));
		try {
			keyStore.load(fileInputStream, keyPasswrd.toCharArray());
		} catch (Exception e) {
			logger.error("---加载SSL证书异常--->{}",e);
		} finally {
			if(fileInputStream != null) {
				fileInputStream.close();
			}
		}
		SSLContext sslcontext = SSLContexts.custom()
				.loadKeyMaterial(keyStore, keyPasswrd.toCharArray())
				.build();
		return sslcontext;
	}
	/**
	 * 
	 * @Title: doPostMapParams 
	 * @Description: 发送 POST 请求（HTTP），K=V&K=V形式 
	 * @author chenming<chenmingf@ennew.cn>
	 * @param httpClientParam
	 * @throws IOException
	 * @return String    返回类型
	 */
    public String doPostMapParams(HttpClientParam httpClientParam) throws IOException {  
    	//请求设置
    	RequestConfig requestConfig = createRequestConfig(httpClientParam);
    	//获取连接
    	CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(commonConnectionManager).setDefaultRequestConfig(requestConfig).build();
        String httpStr = null;  
        HttpPost httpPost = new HttpPost(httpClientParam.getUrl());  
        CloseableHttpResponse response = null;  
        Map<String, String> params = httpClientParam.getRequestMapParam();
        try {
            List<NameValuePair> pairList = new ArrayList<>(params.size());  
            for (Map.Entry<String, String> entry : params.entrySet()) {  
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());  
                pairList.add(pair);  
            }  
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));  
            response = httpClient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
            httpStr = EntityUtils.toString(entity, "UTF-8");  
            logger.info("---HttpClientUtil返回httpStr:{}"+httpStr);
        } catch (IOException e) {  
            logger.error("---HttpClientUtil异常："+e);
            throw e;
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                	logger.error("---HttpClientUtil异常："+e);
                    throw e;
                }  
            }  
        }  
        return httpStr;  
    }  
	
}
