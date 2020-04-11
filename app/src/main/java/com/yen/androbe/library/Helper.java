package com.yen.androbe.library;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;

public class Helper {

    int start = 4;
    int end = 4;

    public String encrypt(String key){

        // json_encode
        key = "\"" + key + "\"";

        // base 64 編碼
        Base64.Encoder b64_encoder = Base64.getEncoder();
        byte[] textByte = key.getBytes();
        String encodedText = b64_encoder.encodeToString(textByte);

        // 字串倒序
        encodedText = reverseString(encodedText);

        // 加入亂數字串
        encodedText = insertText(encodedText);

        return encodedText;
    }

    public String decrypt(String key) throws UnsupportedEncodingException {

        // 刪除亂數字串
        String decodeText = deleteText(key);

        // 字串反序
        decodeText = reverseString(decodeText);

        // base 64 反解
        Base64.Decoder b64_decoder = Base64.getDecoder();
        decodeText = new String(b64_decoder.decode(decodeText), "UTF-8");

        return decodeText;
    }

    // 字串倒序
    public static String reverseString (String str){
        StringBuffer stringBuffer = new StringBuffer(str);
        return stringBuffer.reverse().toString();
    }

    // 加入亂數字串
    public static String insertText(String text)  {
        text = randomGen(4) + text + randomGen(4);

        return text;
    }

    // 刪除亂數字串
    public static String deleteText(String text)  {
        text = text.substring(4, text.length() - 4);

        return text;
    }

    // 產生隨機碼
    public static String randomGen(int len) {
        Random r = new Random() ;
        r.nextInt(62);
        String s="" ;
        char c = 'x' ;
        for(int i=0;i<len;i++) {
            int no = r.nextInt(62);
            if (no < 26) {
                c = (char) ('a' + no);
            } else if (no < 52) {
                c = (char) ('A' + no - 26);
            } else {
                c = (char) ('0' + no - 52);
            }
            s= s+c;
        }
        return s ;
    }
}
