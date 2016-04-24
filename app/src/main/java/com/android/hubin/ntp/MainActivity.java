package com.android.hubin.ntp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.hubin.ntp.manager.NtpManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    private EditText ntpServerET;
    private TextView ntpTimeTV;
    private Button startBtn;
    private BroadcastReceiver receiver;
    private NtpManager ntpManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ntpServerET = (EditText) findViewById(R.id.ntp_server);
        ntpTimeTV = (TextView) findViewById(R.id.result);
        startBtn = (Button) findViewById(R.id.start_ntp);
        ntpManager = NtpManager.getInstance(this);
        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(ntpServerET.getText()))
                {
                    ntpManager.startNtpService(ntpServerET.getText().toString(), 123, 123, 27, 30);
                }
            }
        });

        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String intentAction = intent.getAction();
                Log.i(TAG, "intentAction: " + intentAction);
                if (intentAction.equals(NtpManager.ACTION_NTP_NOTIFY))
                {
                    long ntpTime = intent.getLongExtra(NtpManager.DATA_INTENT, 0);
                    ntpTimeTV.setText(formatTime(ntpTime));
                    Log.i(TAG, "ntpTime: " + ntpTime);
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(NtpManager.ACTION_NTP_NOTIFY));
    }

    private String formatTime(long time)
    {
        if (time == 0)
        {
            return "0";
        }
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String dateFormat = format.format(date);
        return dateFormat;
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
