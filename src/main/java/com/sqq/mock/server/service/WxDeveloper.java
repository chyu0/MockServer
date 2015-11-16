package com.sqq.mock.server.service;

import java.util.List;
import java.util.Map;

public class WxDeveloper {

    private String userName;

    private List<String> ips;

    private Map<String, String> userInfo;

    private Map<String, Map<String, String>> wxConfigMap;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public Map<String, Map<String, String>> getWxConfigMap() {
        return wxConfigMap;
    }

    public void setWxConfigMap(Map<String, Map<String, String>> wxConfigMap) {
        this.wxConfigMap = wxConfigMap;
    }

    public Map<String, String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
    }

}
