package com.example.sreal.picandaudio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sreal on 7/10/2016.
 */
public class ChatUtil {

    public static String addImageTag(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte [] data = byteArrayOutputStream.toByteArray();
        //Base64编码方式对于android 和iphone 都可用。
        String body = Base64.encodeToString(data,Base64.DEFAULT);
        return body;
    }

    public static Bitmap getBitmap(String body){
        byte [] data = Base64.decode(body,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        return bitmap;
    }
}
