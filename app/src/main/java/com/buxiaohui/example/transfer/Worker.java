/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

final class Worker extends IndexDesc implements Runnable {
    private static final String TAG = "Worker";
    private ArrayList<Goods> mMyTransferList;
    private LinkedBlockingQueue<Goods> mTransferList;
    private CountDownLatch mCountDownLatch;
    private int mMaxTransferCount;

    public final ArrayList<Goods> getMyTransferList() {
        return mMyTransferList;
    }

    public Worker(int index, LinkedBlockingQueue<Goods> transferList, CountDownLatch countDownLatch,
                  int maxTransferCount) {
        super(index);
        this.mTransferList = transferList;
        this.mMyTransferList = new ArrayList<>();
        this.mCountDownLatch = countDownLatch;
        this.mMaxTransferCount = maxTransferCount;
    }

    @Override
    public void run() {
        try {
            while (mMyTransferList.size() < mMaxTransferCount) {
                Goods goods = mTransferList.take();
                Log.i(TAG, "index:" + index + "->" + goods);
                mMyTransferList.add(goods);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mCountDownLatch.countDown();
        }
    }

    @Override
    public String toString() {
        return "Worker{" +
                ", index=" + index +
                "\n" +
                ", ->" + Transfer.getListStr(mMyTransferList) +
                '}';
    }

}
