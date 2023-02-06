package com.btcturk.apitest.infrastructure.listener;

import com.btcturk.apitest.infrastructure.user.UserPool;
import org.testng.ITestContext;
import org.testng.ITestListener;

public class TestListener implements ITestListener {

    @Override
    public void onFinish(ITestContext context) {
        UserPool.getInstance().receder().recede();
    }
}
