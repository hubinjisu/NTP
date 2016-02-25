package com.android.hubin.ntp.manager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NtpManager
{
    public static String ACTION_NTP_NOTIFY = "ACTION_NTP_NOTIFY";
    public static String DATA_INTENT = "DATA_INTENT";
    private static String TAG = "NtpManager";
    private static NtpManager instance;

    private String serverAddress = "";
    private int serverPort;
    private int localPort;
    private int zoneSize;
    private int zone;
    private int interval = 30;

    private List<Integer> zoneMap = new ArrayList<Integer>();

    private ScheduledExecutorService pool = null;

    private Context context;

    private NtpManager(Context context)
    {
        this.context = context;
        zoneMap.add(-12 * 60);
        zoneMap.add(-11 * 60);
        zoneMap.add(-10 * 60);
        zoneMap.add(-9 * 60);
        zoneMap.add(-8 * 60);
        zoneMap.add(-7 * 60);
        zoneMap.add(-6 * 60);
        zoneMap.add(-5 * 60);
        zoneMap.add((int) (-(4 + 30 / 60.0) * 60));
        zoneMap.add(-4 * 60);
        zoneMap.add((int) (-(3 + 30 / 60.0) * 60));
        zoneMap.add(-3 * 60);
        zoneMap.add(-2 * 60);
        zoneMap.add(-1 * 60);
        zoneMap.add(0 * 60);
        zoneMap.add(1 * 60);
        zoneMap.add(2 * 60);
        zoneMap.add(3 * 60);
        zoneMap.add((int) (3 + 30 / 60.0) * 60);
        zoneMap.add(4 * 60);
        zoneMap.add((int) ((4 + 30 / 60.0) * 60));
        zoneMap.add(5 * 60);
        zoneMap.add((int) ((5 + 30 / 60.0) * 60));
        zoneMap.add((int) ((5 + 45 / 60.0) * 60));
        zoneMap.add(6 * 60);
        zoneMap.add((int) ((6 + 30 / 60.0) * 60));
        zoneMap.add(7 * 60);
        zoneMap.add(8 * 60);
        zoneMap.add(9 * 60);
        zoneMap.add((int) ((9 + 30 / 60.0) * 60));
        zoneMap.add(10 * 60);
        zoneMap.add(11 * 60);
        zoneMap.add(12 * 60);
        zoneMap.add(13 * 60);
    }

    public static NtpManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new NtpManager(context);
        }
        return instance;
    }

    public void startNtpService(String serverAddress, int serverPort, int localPort, int zoneSize, int interval)
    {
        Log.i(TAG, "startNtpService::serverAddress:" + serverAddress + " serverPort:" + serverPort + " zoneSize:" + zoneSize + " interval:" + interval);
        this.zoneSize = zoneSize;
        zone = zoneMap.get(zoneSize);
        this.interval = interval;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.interval = interval;
        this.localPort = localPort;

        if (pool != null)
        {
            pool.shutdownNow();
        }

        pool = new ScheduledThreadPoolExecutor(1);
        pool.scheduleWithFixedDelay(new Runnable()
        {
            @Override
            public void run()
            {
                updateTime();
            }
        }, 0, interval, TimeUnit.MINUTES);
    }

    private void updateTime()
    {
        NtpMessage msg = getNtpMessage();
        if (msg == null)
        {
            return;
        }
        try
        {
            double destinationTimestamp = (System.currentTimeMillis() / 1000.0) + 2208988800.0;
            destinationTimestamp = destinationTimestamp + getOffsetOfTimezone() * 60 * 60 - zone * 60;
            double localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2;
            long updateTime = System.currentTimeMillis() + (long) (localClockOffset * 1000l);
            Intent data = new Intent(ACTION_NTP_NOTIFY);
            data.putExtra(DATA_INTENT, updateTime);
            context.sendBroadcast(data);
//            SystemClock.setCurrentTimeMillis(updateTime);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }
    }

    private NtpMessage getNtpMessage()
    {
        int port = 123;
        int timeout = 3000;

        // get the address and NTP address request
        InetAddress ipv4Addr = null;
        DatagramSocket socket = null;
        long responseTime = -1;
        NtpMessage msg = null;
        try
        {
            ipv4Addr = InetAddress.getByName(serverAddress);// ���NTPʱ��������ο���ע

            socket = new DatagramSocket();
            socket.setSoTimeout(timeout); // will force the
            // InterruptedIOException

            // Send NTP request
            byte[] data = new NtpMessage().toByteArray();
            DatagramPacket outgoing = new DatagramPacket(data, data.length, ipv4Addr, serverPort);
            long sentTime = System.currentTimeMillis();
            socket.send(outgoing);

            // Get NTP Response
            // byte[] buffer = new byte[512];
            DatagramPacket incoming = new DatagramPacket(data, data.length);
            socket.receive(incoming);
            responseTime = System.currentTimeMillis() - sentTime;

            // Validate NTP Response
            // IOException thrown if packet does not decode as expected.
            msg = new NtpMessage(incoming.getData());

            // Store response time if available
            Log.i(TAG, "responsetime==" + responseTime + "ms");
        }
        catch (IOException e)
        {
            Log.e(TAG, e.toString());
        }
        finally
        {
            if (socket != null)
            {
                socket.close();
            }
        }

        return msg;
    }

    public static int getOffsetOfTimezone()
    {
        return TimeZone.getDefault().getRawOffset() / 3600 / 1000;
    }

}
