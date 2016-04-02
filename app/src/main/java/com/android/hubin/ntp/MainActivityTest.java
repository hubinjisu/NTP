package com.android.hubin.ntp;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by hubin on 2016/4/1.
 */
@RunWith (RobolectricGradleTestRunner.class)
@Config (constants = BuildConfig.class)
public class MainActivityTest
{
    private MainActivity mActivity;

    // 引用待测Activity中的TextView和Button
    private TextView textView;
    private Button button;

    @org.junit.Before
    public void setUp() throws Exception
    {
        mActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testStartButton() throws Exception
    {
        junit.framework.Assert.assertEquals(true, mActivity.findViewById(R.id.start_ntp).isClickable());
    }

    @org.junit.After
    public void tearDown() throws Exception
    {

    }
}