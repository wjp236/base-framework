package com.platform.base.framework.trunk.core.context;

import com.platform.base.framework.trunk.core.exception.utils.ErrorBuilderTool;
import com.platform.base.frramework.trunk.util.constants.TrunkConstans;
import com.platform.base.frramework.trunk.util.file.FileAccessor;
import com.platform.base.frramework.trunk.util.file.FileAccessorListener;
import com.platform.base.frramework.trunk.util.xml.XmlUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

/**
 * Created by wangjp on 4/22/16.
 */
public class ScriptManager implements FileAccessorListener, ApplicationListener<ContextRefreshedEvent> {

    protected static Logger logger = LoggerFactory.getLogger(ScriptManager.class);

    /**
     * script instance
     */
    private static ScriptManager instance = new ScriptManager();

    public static ScriptManager getScriptManager() {
        if (instance == null) {
            throw new RuntimeException(ErrorBuilderTool.buildError("000002"));
        }
        return instance;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        instance = this;

        if (applicationContext == null) {
            applicationContext = contextRefreshedEvent.getApplicationContext();
        }

        try {
            //非校验请求
            loadRight(TrunkConstans.UNCHECKRIGHT_NAME);
            // 初始化错误码
            loadErrorCode(TrunkConstans.ERROR_FILE_NAME);
            // 初始化通用配置
            loadGeneralConfig(TrunkConstans.GENERAL_FILE_NAME);
        } catch (Exception e) {
            logger.error("setApplicationContext: ", e);
            throw new RuntimeException(ErrorBuilderTool.buildError("000002"));
        }

    }

    /**
     * error code mapping
     */
    private Map<String, String> codeCache = new HashMap<>();

    /**
     * 获取codeCash
     * @return
     */
    public Map<String, String> getCodeCache() {
        return codeCache;
    }

    /**
     * general config mapping
     */
    private Map<String, String> configCache = new HashMap<>();

    public Map<String, String> getConfigCache() {
        return configCache;
    }

    public void setConfigCache(Map<String, String> configCache) {
        this.configCache = configCache;
    }

    /**
     * base path mapping
     */
    private Map<String, String> baseCache = new HashMap<>();

    /**
     * lastModifiedCache mapping
     */
    private HashMap<String, Long> lastModifiedCache = new HashMap<>();

    /**
     * module instance mapping
     */
    private Map<String, List<Config>> moduleCache = new HashMap<>();

    /**
     * not check  state request
     */
    private ArrayList<String> NOT_CHECK_URL_LIST = new ArrayList<>();

    /**
     *
     */
    private Map<String, Object> appInfoCache = new HashMap<>();
    /**
     * inject property
     */
    private String scriptDirPath;

    /**
     * inject property
     */
    private String localDirPath;

    /**
     * spring application context
     */
    private ApplicationContext applicationContext;

    /**
     * @return the localDirPath
     */
    public String getLocalDirPath() {
        return localDirPath;
    }

    /**
     * @param localDirPath
     *            the localDirPath to set
     */
    public void setLocalDirPath(String localDirPath) {
        this.localDirPath = localDirPath;
    }

    /**
     * @return the scriptDirPath
     */
    public String getScriptDirPath() {
        return scriptDirPath;
    }

    /**
     * 通过错误码获取错误值
     *
     * @param code
     *            已定义的错误码
     * @return
     */
    public static String getErrorDesc(final String code) {
        return instance.codeCache.get(code);
    }

    /**
     * load local config
     *
     * @param key
     * @return
     */
    public String getLocalConfig(final String key) {
        return configCache.get(key);
    }

    /**
     * @param scriptDirPath
     *            the scriptDirPath to set
     */
    public void setScriptDirPath(String scriptDirPath) {
        this.scriptDirPath = scriptDirPath;
    }

    /**
     * 获取文件路径
     * @param dirName
     * @return
     * @throws IOException
     */
    public String getAbsolutePath(String dirName) throws IOException {
        return this.applicationContext.getResource(dirName).getFile().getAbsolutePath();
    }

    /**
     * load local script files
     *
     * @param dirName
     * @throws IOException
     */
    private void loadScriptFile(String dirName) throws IOException {
        FileAccessor.getListFiles(this, getAbsolutePath(dirName), "xml", false);
    }

    /**
     * load local script files
     *
     * @param dirName
     * @throws IOException
     */
    private void loadLocalFile(String dirName) throws IOException {
        FileAccessor.getListFiles(this, getAbsolutePath(dirName), "xml", false);
    }

    /**
     * @return the String
     */
    public String getBaseCache(String key) {
        return baseCache.get(key);
    }


    private String filterInnerErrorCode(String code, String text) {
        int codes = (code == null || code.length() == 0 ? 0 : Integer.parseInt(code));
        if (codes >= 900000 && codes <= 999999) {
            return "内部错误";
        }
        return text;
    }

    /**
     * @param appPath
     * @throws IOException
     */
    public void loadErrorCode(String appPath) throws IOException {
        codeCache.clear();

        InputStream stream = getClass().getClassLoader().getResourceAsStream(appPath);
        File targetFile = new File("ErrorCode.xml");
        FileUtils.copyInputStreamToFile(stream, targetFile);
        XmlUtil xmlUtil = new XmlUtil(new FileInputStream(targetFile));
        List<Element> elements = xmlUtil.getChildElementList();
        for (Element sub : elements) {
            String attr = sub.attribute("code").getText();
            String text = filterInnerErrorCode(attr, sub.getTextTrim());
            logger.info("attr: " + attr + ", text: " + text);
            codeCache.put(attr, text);
        }
    }

    /**
     * @param appPath
     * @throws IOException
     */
    public void loadGeneralConfig(String appPath) throws IOException {
        configCache.clear();

        InputStream stream = getClass().getClassLoader().getResourceAsStream(appPath);
        File targetFile = new File("GeneralConfig.xml");
        FileUtils.copyInputStreamToFile(stream, targetFile);
        XmlUtil xmlUtil = new XmlUtil(new FileInputStream(targetFile));
        List<Element> elements = xmlUtil.getChildElementList();
        for (Element element : elements) {
            String name = element.getName();
            String text = element.getTextTrim();
            logger.info("name: " + name + ", text: " + text);
            if (!configCache.containsKey(name)) {
                configCache.put(name, text);
            }
        }
    }


    public boolean isCheckRight(String uri) {
        for (String element : NOT_CHECK_URL_LIST) {
            if (uri.indexOf(element) > -1) {
                return false;
            }
        }
        return true;
    }

    private void loadRight(String appPath) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(appPath);
        File targetFile = new File("UnCheckRight.xml");
        FileUtils.copyInputStreamToFile(stream, targetFile);
        XmlUtil xmlUtil = new XmlUtil(new FileInputStream(targetFile));
        List<Element> elements = xmlUtil.getChildElementList();
        for (Element element : elements) {
            String name = element.getName();
            String text = element.getTextTrim();
            logger.info("name: " + name + ", text: " + text);
            if ("right".equals(name)) {
                NOT_CHECK_URL_LIST.add(text);
            } else {
                logger.warn("found the undefined right.");
            }
        }

    }

    /**
     * 获取配置文件
     * @param elements
     * @return
     */
    private Config loadConfig(List<Element> elements) {
        Config c = new Config();
        for (Element e : elements) {
            String name = e.getName();
            String text = e.getTextTrim();
            logger.info("name: " + name + ", text: " + text);
            if ("scheme".equals(name)) {
                c.scheme = text;
            } else if ("host".equals(name)) {
                c.host = text;
            } else if ("port".equals(name)) {
                c.port = text;
            } else if ("conntimeout".equals(name)) {
                c.conntimeout = text;
            } else if ("sotimeout".equals(name)) {
                c.sotimeout = text;
            } else if ("action".equals(name)) {
                c.action = text;
            } else if ("serial".equals(name)) {
                c.serial = text;
            }
        }
        return c;
    }

    /**
     * @param fileName
     * @param filePath
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void loadConfigElement(final String fileName, final String filePath) throws IOException {
        XmlUtil xmlUtil = new XmlUtil(new FileInputStream(new File(filePath)));
        List<Element> elements = xmlUtil.getChildElementList();
        if (elements == null || elements.isEmpty()) {
            logger.error("config file incorrect [" + filePath + "], please check it.");
            throw new IOException("config file incorrect [" + filePath + "], please check it.");
        }

        List<Config> configList = new ArrayList<Config>();
        for (Element element : elements) {
            String name = element.getName();
            if ("server".equals(name)) {
                Config c = loadConfig(element.elements());
                configList.add(c);
            } else {
                logger.info("Not found the server tag: " + name);
            }
        }
        moduleCache.remove(fileName);
        moduleCache.put(fileName, configList);
    }


    @Override
    public void onFileAccessorNotify(String fileName, String filePath, long lastModified) throws Exception {
        logger.info(fileName + ":" + filePath + ":" + lastModified);
        baseCache.put(fileName, filePath);
        lastModifiedCache.put(fileName, lastModified);
        if (filePath.indexOf(TrunkConstans.SCRIPT_DIR_MATCH) > -1) {
            loadConfigElement(fileName, filePath);
        }
    }



    /**
     * script config key
     */
    public static final String URL = "config_url";
    public static final String IP = "config_ip";
    public static final String ADDR = "config_addr";
    public static final String SCHEME = "config_scheme";
    public static final String HOST = "config_host";
    public static final String PORT = "config_port";
    public static final String CONN_TIMEOUT = "config_conn_timeout";
    public static final String SO_TIMEOUT = "config_so_timeout";
    public static final String LOCAL_HOST_NAME = "localHost";
    public static final String UNCHECKRIGHT = "uncheckright_addr";
    public static final String UNCHECKFEE = "uncheckfee_addr";
    public static final String UNCHECKSTATE = "uncheckstate_addr";
    public static final String SERIAL = "config_serial";

    static class Server {
        String sn;
    }

    public static class Config extends Server {
        String scheme;
        String host;
        String port;
        String action = "";
        String conntimeout;
        String sotimeout;
        String serial = "";

        public Config() {
        }

        public String getConfig(String key) {
            if (URL.equals(key)) {
                return String.format("%s://%s:%s/%s", scheme, host, port, action);
            }

            if (ADDR.equals(key)) {
                return String.format("%s://%s:%s/", scheme, host, port);
            }

            if (IP.equals(key)) {
                return String.format("%s:%s", host, port);
            }

            if (SCHEME.equals(key)) {
                return scheme;
            }

            if (HOST.equals(key)) {
                return host;
            }

            if (PORT.equals(key)) {
                return port;
            }

            if (CONN_TIMEOUT.equals(key)) {
                return conntimeout;
            }

            if (SO_TIMEOUT.equals(key)) {
                return sotimeout;
            }

            if(SERIAL.equals(key)) {
                return serial;
            }

            return null;
        }
    }

}
