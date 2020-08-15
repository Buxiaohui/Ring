/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

public class Transfer {
    private ExecutorService mExecutorService;
    private CountDownLatch mCountDownLatch;

    private LinkedBlockingQueue<Goods> mGoodsList;
    private int mWorkerCount;

    private Transfer() {

    }

    public Transfer(LinkedBlockingQueue<Goods> goodsList, int workerCount) {
        this.mGoodsList = goodsList;
        this.mWorkerCount = workerCount;
        init();
    }

    private void init() {
        mExecutorService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
    }

    public void transfer(boolean countBalance) {
        this.transfer(countBalance, false);
    }

    /**
     * @param countBalance 工足量是否是均衡的（每个工人搬运的总数量近似->则视为均衡）
     * @param timeBalance  工作频率是否是均衡的(工人交替搬运货物->则视为均衡) TODO 未实现
     */
    public void transfer(boolean countBalance, boolean timeBalance) {
        if (mGoodsList == null || mGoodsList.size() <= 0) {
            throw new IllegalArgumentException("please create goods list correctly");
        }
        if (mWorkerCount <= 0) {
            throw new IllegalArgumentException("please set regular number of worker");
        }
        if (mWorkerCount > mGoodsList.size()) {
            throw new IllegalArgumentException("please set Economic number of worker");
        }
        mCountDownLatch = new CountDownLatch(mWorkerCount);
        ArrayList<Worker> workerList = new ArrayList<>(mWorkerCount);
        int regularTransferSize = mGoodsList.size() / mWorkerCount;
        int otherCount = mGoodsList.size() % mWorkerCount;
        for (int i = 0; i < mWorkerCount; i++) {
            int maxTransferCount;
            if (otherCount != 0 && i < otherCount) {
                maxTransferCount = regularTransferSize + 1;
            } else {
                maxTransferCount = regularTransferSize;
            }

            Worker worker =
                    new Worker(i, mGoodsList, mCountDownLatch, countBalance ? maxTransferCount : Integer.MAX_VALUE);
            workerList.add(worker);
            mExecutorService.submit(worker);
        }
        mExecutorService.shutdown();
        try {
            mCountDownLatch.await();
            Log.i("transfer", "complete");
            for (Worker worker : workerList) {
                Log.i("transfer", "worker:" + worker.index + "-->" + getListStr(worker.getMyTransferList()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static LinkedBlockingQueue<Goods> createGoodsList(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("please consider to set a regular size");
        }
        LinkedBlockingQueue<Goods> list = new LinkedBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            list.add(new Goods(i));
        }
        return list;
    }

    public static <E extends Goods> String getListStr(List<E> list) {
        StringBuilder stringBuilder = new StringBuilder("--list:");
        if (list == null || list.isEmpty()) {
            stringBuilder.append("empty");
        } else {
            Iterator<E> iterator = list.iterator();
            while (iterator.hasNext()) {
                E e = iterator.next();
                if (e.index <= 9) {
                    stringBuilder.append("  ").append(e.index);
                } else if (e.index <= 99) {
                    stringBuilder.append(" ").append(e.index);
                } else {
                    stringBuilder.append(e.index);
                }
                stringBuilder.append("、");
            }
        }
        return stringBuilder.toString();
    }

}
