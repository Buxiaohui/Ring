/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;

public class Transfer {
    private LinkedBlockingDeque<Goods> mGoodsList;
    private int mWorkerCount;

    public Transfer(LinkedBlockingDeque<Goods> goodsList, int workerCount) {
        this.mGoodsList = goodsList;
        this.mWorkerCount = workerCount;
        init();
    }

    public static LinkedBlockingDeque<Goods> createGoodsList(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("please consider to set a regular size");
        }
        LinkedBlockingDeque<Goods> list = new LinkedBlockingDeque<>(size);
        for (int i = 0; i < size; i++) {
            list.add(new Goods(i));
        }
        return list;
    }

    private ExecutorService mExecutorService;

    private Transfer() {

    }

    private CountDownLatch mCountDownLatch;

    private void init() {
        mExecutorService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
    }

    public void transfer() {
        if (mGoodsList == null || mGoodsList.size() <= 0) {
            throw new IllegalArgumentException("please create goods list correctly");
        }
        if (mWorkerCount <= 0) {
            throw new IllegalArgumentException("please set regular number of worker");
        }
        mCountDownLatch = new CountDownLatch(mWorkerCount);
        ArrayList<Worker> workerList = new ArrayList<>(mWorkerCount);

        for (int i = 0; i < mWorkerCount; i++) {
            Worker worker = new Worker(i, mGoodsList, mCountDownLatch);
            workerList.add(worker);
            mExecutorService.submit(worker);
        }
        mExecutorService.shutdown();
        try {
            mCountDownLatch.await();
            Log.i("transfer", "complete");
            for (Worker worker : workerList) {
                Log.i("transfer", "worker:" + worker.index + "-->" + getListStr(worker.mMyTransferList));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class Log {
        private static boolean ENABLE = true;

        public static final void i(String tag, String content) {
            if (ENABLE) {
                System.out.println(tag + "," + content);
            }
        }
    }

    private static final class Worker extends IndexDesc implements Runnable {
        private static final String TAG = "Worker";
        private ArrayList<Goods> mMyTransferList;
        private LinkedBlockingDeque<Goods> mTransferList;
        private CountDownLatch mCountDownLatch;

        public final ArrayList<Goods> getMyTransferList() {
            return mMyTransferList;
        }

        public Worker(int index, LinkedBlockingDeque<Goods> transferList, CountDownLatch countDownLatch) {
            super(index);
            this.mTransferList = transferList;
            this.mMyTransferList = new ArrayList<>();
            this.mCountDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                while (mTransferList.size() != 0) {
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
                    ", ->" + getListStr(mMyTransferList) +
                    '}';
        }

    }

    public static <E extends Goods> String getListStr(List<E> list) {
        StringBuilder stringBuilder = new StringBuilder("--list:");
        if (list == null || list.isEmpty()) {
            stringBuilder.append("empty");
        } else {
            Iterator<E> iterator = list.iterator();
            while (iterator.hasNext()) {
                E e = iterator.next();
                String s;
                if (e.index <= 9) {
                    s = "  " + e.index;
                } else if (e.index <= 99) {
                    s = " " + e.index;
                } else {
                    s = "" + e.index;
                }
                stringBuilder.append(s);
                stringBuilder.append("、");
            }
        }
        return stringBuilder.toString();
    }

    private static final class Goods extends IndexDesc {
        public Goods() {
        }

        public Goods(int index) {
            super(index);
        }

        @Override
        public String toString() {
            return "Goods{" +
                    "index=" + index +
                    '}';
        }
    }

    public static class IndexDesc {
        protected int index;

        public IndexDesc() {
        }

        public IndexDesc(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            IndexDesc indexDesc = (IndexDesc) o;
            return index == indexDesc.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }

        @Override
        public String toString() {
            return "IndexDesc{" +
                    "index=" + index +
                    '}';
        }
    }
}
