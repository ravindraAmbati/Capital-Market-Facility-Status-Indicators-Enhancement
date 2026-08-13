package com.sab.carm.fcm.mongo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * External MongoDB infrastructure configuration.
 */
@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {

    private String username;
    private String password;
    private String authenticationDatabase;
    private String database;
    private boolean sslEnabled;
    private boolean sslInvalidHostnameAllowed;
    private int connectTimeout = 10000;
    private int socketTimeout = 10000;
    private List<String> serverAddresses = new ArrayList<>();
    private Map<String, String> dbCollectionNames = new LinkedHashMap<>();

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAuthenticationDatabase() { return authenticationDatabase; }
    public void setAuthenticationDatabase(String authenticationDatabase) { this.authenticationDatabase = authenticationDatabase; }
    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }
    public boolean isSslEnabled() { return sslEnabled; }
    public void setSslEnabled(boolean sslEnabled) { this.sslEnabled = sslEnabled; }
    public boolean isSslInvalidHostnameAllowed() { return sslInvalidHostnameAllowed; }
    public void setSslInvalidHostnameAllowed(boolean sslInvalidHostnameAllowed) { this.sslInvalidHostnameAllowed = sslInvalidHostnameAllowed; }
    public int getConnectTimeout() { return connectTimeout; }
    public void setConnectTimeout(int connectTimeout) { this.connectTimeout = connectTimeout; }
    public int getSocketTimeout() { return socketTimeout; }
    public void setSocketTimeout(int socketTimeout) { this.socketTimeout = socketTimeout; }
    public List<String> getServerAddresses() { return serverAddresses; }
    public void setServerAddresses(List<String> serverAddresses) { this.serverAddresses = serverAddresses; }
    public Map<String, String> getDbCollectionNames() { return dbCollectionNames; }
    public void setDbCollectionNames(Map<String, String> dbCollectionNames) { this.dbCollectionNames = dbCollectionNames; }
}
