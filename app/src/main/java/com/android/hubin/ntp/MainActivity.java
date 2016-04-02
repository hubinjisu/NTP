package com.android.hubin.ntp;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.hubin.ntp.manager.NtpManager;

public class MainActivity extends Activity
{
    private EditText ntpServerET;
    private TextView ntpTimeTV;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ntpServerET = (EditText) findViewById(R.id.ntp_server);
        ntpTimeTV = (TextView) findViewById(R.id.result);
        startBtn = (Button) findViewById(R.id.start_ntp);
        startBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!TextUtils.isEmpty(ntpServerET.getText()))
                {
                    NtpManager.getInstance().startNtpService(ntpServerET.getText().toString(), 123, 123,27, 30);
                }
            }
        });
    }

}
