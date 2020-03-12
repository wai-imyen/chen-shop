package com.yen.androbe.library;

import java.util.List;

public class DataBeanTest {
    public Data data;
    public String code;
    public String message;

    public static class Data{
        public String product_id;
        public String price;
        public String name;
        public String thumb;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
