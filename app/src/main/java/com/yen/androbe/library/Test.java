package com.yen.androbe.library;

import android.util.Base64;


public class Test {

    String text;

    public Test() {

        String strBase64 = Base64.encodeToString("whoislcj".getBytes(), Base64.DEFAULT);

        String str2 = new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));

    }
}
