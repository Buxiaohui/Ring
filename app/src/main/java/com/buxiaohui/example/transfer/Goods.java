/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

final class Goods extends IndexDesc {
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
