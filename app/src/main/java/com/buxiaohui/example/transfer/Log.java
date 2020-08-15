/*
 * Copyright (C) 2020 Baidu, Inc. All Rights Reserved.
 */
package com.buxiaohui.example.transfer;

class Log {
    private static boolean ENABLE = true;

    public static final void i(String tag, String content) {
        if (ENABLE) {
            System.out.println(tag + "," + content);
        }
    }
}
