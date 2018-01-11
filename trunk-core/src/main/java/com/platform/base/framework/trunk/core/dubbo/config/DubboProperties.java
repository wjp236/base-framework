package com.platform.base.framework.trunk.core.dubbo.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "trunk.dubbo")
public class DubboProperties {

    private String appName;

    private int port;

    private String resAddress;

    private String resUsername;

    private String resPassowrd;

    private String protocol;

    private String protocolThreadpool;

    private int protocolThreads;

    private String file;

    private int accepts;

    private int connections;

    private String filters;

    private int timeout;

    private String version;

    private int retries;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getResAddress() {
        return resAddress;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getResUsername() {
        return resUsername;
    }

    public void setResUsername(String resUsername) {
        this.resUsername = resUsername;
    }

    public String getResPassowrd() {
        return resPassowrd;
    }

    public void setResPassowrd(String resPassowrd) {
        this.resPassowrd = resPassowrd;
    }

    public int getAccepts() {
        return accepts;
    }

    public void setAccepts(int accepts) {
        this.accepts = accepts;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getProtocolThreadpool() {
        return protocolThreadpool;
    }

    public void setProtocolThreadpool(String protocolThreadpool) {
        this.protocolThreadpool = protocolThreadpool;
    }

    public int getProtocolThreads() {
        return protocolThreads;
    }

    public void setProtocolThreads(int protocolThreads) {
        this.protocolThreads = protocolThreads;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}

