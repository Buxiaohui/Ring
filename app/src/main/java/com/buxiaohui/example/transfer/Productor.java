/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

final class Productor extends IndexDesc implements Runnable {
    private static final String TAG = "Productor";
    private ArrayList<Goods> mMyTransferList;
    private LinkedBlockingQueue<Goods> mTransferList;
    private CountDownLatch mCountDownLatch;
    private int mMaxTransferCount;
    private int mGoodsStartIndex;

    public final ArrayList<Goods> getMyTransferList() {
        return mMyTransferList;
    }

    public Productor(int index,int goodsStartIndex,LinkedBlockingQueue<Goods> transferList, CountDownLatch countDownLatch,
                     int maxTransferCount) {
        this.index = index;
        this.mGoodsStartIndex = goodsStartIndex;
        this.mTransferList = transferList;
        this.mMyTransferList = new ArrayList<>();
        this.mCountDownLatch = countDownLatch;
        this.mMaxTransferCount = maxTransferCount;
    }

    @Override
    public void run() {
        try {
            while (mMyTransferList.size() < mMaxTransferCount) {
                Goods goods = new Goods(mGoodsStartIndex);
                mGoodsStartIndex+=1;
                mTransferList.put(goods);
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
                ", ->" + Transfer.getListStr(mMyTransferList) +
                '}';
    }

}
