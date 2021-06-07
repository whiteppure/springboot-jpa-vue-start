package com.github.springboot.template.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

/**
 * ip Utils
 */
public class IPUtils {

    private static final Logger log = LoggerFactory.getLogger(IPUtils.class);


    private IPUtils() {
    }

    public static String getIpAddress() {
        final String defaultIp= "127.0.0.1";
        String ipAddress = null;
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();

                if (!netInterface.isLoopback() && !netInterface.isVirtual() && netInterface.isUp()) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            ipAddress = ip.getHostAddress();
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("IP地址获取失败，{}", e.getMessage());
            ipAddress = defaultIp;
        }
        return ipAddress;
    }

    /**
     * 获取本地ip
     * @return ip
     */
    public static String getSimpleIpAddress(){
        String ipAddress ;
        final String defaultIp= "127.0.0.1";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取ip失败：{}",e.getMessage());
            ipAddress = defaultIp;
        }
        return ipAddress ;
    }

}
