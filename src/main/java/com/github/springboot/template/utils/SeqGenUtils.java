package com.github.springboot.template.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 流水号生成器
 */
public class SeqGenUtils implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(SeqGenUtils.class);

    private static final long serialVersionUID = 7327224582088064882L;


    //数据中心
    private long dataCenterId;

    //机器标识
    private long machineId;

    //序列号 禁止指令重排
    private volatile long sequence = 1L;

    //全局序列号
    private long glbSeq = 1L;

    //上次日期
    private String lastDate;

    private SeqGenUtils() {
        try {
            String localIp = IpUtil.getLocalIp();

            if (ObjectUtils.isNotEmpty(localIp) && localIp.contains(".")) {
                String[] localIpArr = localIp.split("[.]");
                long total = 0;
                for (int i = 0; i < localIpArr.length; i++) {
                    total += Long.parseLong(localIpArr[i]);
                    if (i == 2) {
                        dataCenterId = total % 31;
                    } else if (i == 3) {
                        machineId = total % 255;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("获取dataCenterId和machineId失败");
        }
    }



    private static class Instance {
        private static final SeqGenUtils INSTANCE = new SeqGenUtils();
    }

    /**
     * <p>
     * 防止序列化反射 破坏单例模式
     * </p>
     */
    private Object readResolve() {
        return SeqGenUtils.getInstance();
    }


    public static SeqGenUtils getInstance() {
        return Instance.INSTANCE;
    }

    public synchronized String nextId() {
        String curDate = DateUtils.getLocalDateStr();
        if (!curDate.equals(lastDate) || sequence >= 999) {
            sequence = 1;
            lastDate = curDate;
        }
        return String.format("%03d%s%03d", this.machineId, DateUtils.dateToStr("HHmmss"), sequence++);
    }

    /**
     * 生成全局序列号
     */
    public synchronized String nextGlobalId() {
        String curDate = DateUtils.getLocalDateStr();
        if (!curDate.equals(lastDate) || glbSeq >= 99) {
            glbSeq = 1;
            lastDate = curDate;
        }
        return String.format("G000000SEAL%s%03d%s%02d", curDate, this.machineId, DateUtils.dateToStr("HHmmss"), glbSeq++);
    }


    /**
     * ipUtils
     */
    public static class IpUtil{

        private static String ip;

        public static String getLocalIpAddr(String interfaceName) {
            if (ObjectUtils.isNotEmpty(ip)) {
                return ip;
            }

            String localIpAddr = null;

            try {
                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
                NetworkInterface ni = null;
                Enumeration ipAddrEnum = null;
                InetAddress addr = null;
                while (interfaces.hasMoreElements()) {
                    ni = (NetworkInterface) interfaces.nextElement();
                    ipAddrEnum = ni.getInetAddresses();
                    while (ipAddrEnum.hasMoreElements()) {
                        addr = (InetAddress) ipAddrEnum.nextElement();
                        if (addr.isLoopbackAddress() == true) {
                            continue;
                        }

                        if (addr instanceof Inet6Address) {
                            continue;
                        }

                        localIpAddr = addr.getHostAddress();

                        if (ni.getName().equals(interfaceName)) {
                            return localIpAddr;
                        }

                    }
                }

            } catch (Exception e) {
                log.warn("Failed to get local ip list. error message:{}", e.getMessage(), e);
                throw new RuntimeException("Failed to get local ip list");
            }
            return localIpAddr;
        }

        public static String getLocalIp() {
            if (ObjectUtils.isNotEmpty(ip)) {
                return ip;
            }

            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
                log.info("服务器当前 ip={}", ip);

            } catch (Exception e) {
                log.warn(e.getLocalizedMessage());
                ip = getLocalIpAddr("");
            }

            return ip;
        }
    }

}
