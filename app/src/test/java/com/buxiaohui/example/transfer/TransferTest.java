/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransferTest {
    @Test
    public void transferGoods(){
        transfer(1000,10);
        transfer(997,9);
    }

    private void transfer(int goodsCount,int workerCount) {
        Transfer transfer1 = new Transfer(Transfer.createGoodsList(goodsCount),workerCount);
        transfer1.transfer(true);
    }

}