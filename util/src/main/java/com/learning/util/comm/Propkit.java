package com.learning.util.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 配置文件处理
 *
 * @author wangzhenhua
 */
public class Propkit {
    private static final Logger logger = LoggerFactory.getLogger("PropKit");

    private Properties properties;

    /**
     * 加载配置文件。</br>
     * 0. 加载当前目录
     * 1、从WEB-INF目录下加载。WEB-INF目录加载不到继续（3）</br>
     * 2、从WEB-INF/classes目录下加载。加载不到抛出异常，加载失败
     *
     * @param file 资源文件名
     * @return
     */
    public Properties loadPropertyFile(String file) {
        if (StrKit.isBlank(file))
            throw new IllegalArgumentException("Parameter of file can not be blank");
        if (file.contains(".."))
            throw new IllegalArgumentException("Parameter of file can not contains \"..\"");


        String fullFile = file;
        properties = loadPropFile(fullFile);

        if (properties == null) {
            if (file.startsWith(File.separator)) {
                fullFile = getWebRootPath() + File.separator + "WEB-INF" + file;
            } else {
                fullFile = getWebRootPath() + File.separator + "WEB-INF" + File.separator + file;
            }
            properties = loadPropFile(fullFile);
        }

        if (properties == null) {
            if (file.startsWith(File.separator)) {
                fullFile = getWebRootPath() + File.separator + "WEB-INF/classes" + file;
            } else {
                fullFile = getWebRootPath() + File.separator + "WEB-INF/classes" + File.separator + file;
            }
            properties = loadPropFile(fullFile);
        }

        if (properties == null)
            throw new RuntimeException("Properties file loading failed: " + file + " from directory WEB-INF or WEB-INF/classes.");
        return properties;
    }


    private Properties loadPropFile(String fullFile) {
        InputStream inputStream = null;
        Properties p = null;
        try {
            logger.info("load properties file from:" + fullFile);
            inputStream = new FileInputStream(new File(fullFile));
            p = new Properties();
            p.load(inputStream);
        } catch (FileNotFoundException e) {
            logger.warn("Properties file not found: " + fullFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return p;
    }

    public String getProperty(String key) {
        checkPropertyLoading();
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        checkPropertyLoading();
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 读取Integer值
     *
     * @param key
     * @return
     */
    public Integer getPropertyToInt(String key) {
        checkPropertyLoading();
        Integer resultInt = null;
        String resultStr = properties.getProperty(key);
        if (resultStr != null)
            resultInt = Integer.parseInt(resultStr);
        return resultInt;
    }

    /**
     * 读取Integer值
     *
     * @param key
     * @param defaultValue 默认值
     * @return
     */
    public Integer getPropertyToInt(String key, Integer defaultValue) {
        Integer result = getPropertyToInt(key);
        return result != null ? result : defaultValue;
    }

    public Boolean getPropertyToBoolean(String key) {
        checkPropertyLoading();
        String resultStr = properties.getProperty(key);
        Boolean resultBool = null;
        if (resultStr != null) {
            if (resultStr.trim().equalsIgnoreCase("true"))
                resultBool = true;
            else if (resultStr.trim().equalsIgnoreCase("false"))
                resultBool = false;
        }
        return resultBool;
    }

    public Boolean getPropertyToBoolean(String key, boolean defaultValue) {
        Boolean result = getPropertyToBoolean(key);
        return result != null ? result : defaultValue;
    }

    private void checkPropertyLoading() {
        if (properties == null)
            throw new RuntimeException("You must first load properties file by loadPropertyFile(String) method.");
    }

    private String getWebRootPath() {
        try {
            String path = Propkit.class.getResource("/").toURI().getPath();
            return new File(path).getParentFile().getParentFile().getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
