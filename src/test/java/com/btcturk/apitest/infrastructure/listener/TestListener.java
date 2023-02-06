package com.btcturk.apitest.infrastructure.listener;

import com.btcturk.apitest.infrastructure.user.UserPool;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestSuccess(ITestResult result) {
        UserPool.getInstance().receder().recede();
    }

    @Override
    public void onTestFailure(ITestResult result) {
// TODO Auto-generated method stub
        UserPool.getInstance().receder().recede();
    }
}
