package com.android.hubin.ntp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.hubin.ntp.manager.NtpManager;

public class MainActivity extends AppCompatActivity
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                    int ntpTime = intent.getIntExtra(NtpManager.DATA_INTENT, 0);
                    Log.i(TAG, "ntpTime: " + ntpTime);
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(NtpManager.ACTION_NTP_NOTIFY));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
