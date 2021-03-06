package com.dizsun.timechain.service;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NTPClient {
    private NTPUDPClient client;
    private InetAddress inetAddress;

    public NTPClient() throws UnknownHostException {
        this(10_000, "120.25.108.11");
    }

    public NTPClient(int timeout, String server_name) throws UnknownHostException {
        this.client = new NTPUDPClient();
        client.setDefaultTimeout(timeout);
        this.inetAddress = InetAddress.getByName(server_name);
    }

    public long getCurrentTime() {
        TimeStamp systemNtpTime = TimeStamp.getCurrentTime();
        System.out.println("System time:\t" + "\t" + systemNtpTime.toDateString());

        return systemNtpTime.getTime();
    }

    public String getNTPTime() {
        String res = "获取同步时间失败！";
        try {
            TimeInfo timeInfo = this.client.getTime(this.inetAddress);
            timeInfo.computeDetails();

            if (timeInfo.getOffset() != null) {
                long offset = timeInfo.getOffset();

                long currentTime = System.currentTimeMillis();
                TimeStamp atomicNtpTime = TimeStamp.getNtpTime(currentTime + offset);

                System.out.println("Atomic time:\t" + "\t" + atomicNtpTime.toDateString());
//            return atomicNtpTime.getTime();
                res = atomicNtpTime.toDateString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
