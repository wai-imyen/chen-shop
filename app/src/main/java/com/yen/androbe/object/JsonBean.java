package com.yen.androbe.object;

public class JsonBean<T>{

    public T data;
    public String code;
    public String message;

    public T getData() {
        return (T) data;
    }

    public void setData(T data) {
        this.data = data;
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
