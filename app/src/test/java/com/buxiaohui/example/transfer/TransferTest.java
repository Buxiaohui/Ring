/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransferTest {
    @Test
    public void transferGoods(){
        Transfer transfer = new Transfer(Transfer.createGoodsList(1000),10);
        transfer.transfer();
    }

}