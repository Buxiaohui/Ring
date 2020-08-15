/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

import java.util.Objects;

public class IndexDesc {
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
