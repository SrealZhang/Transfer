package com.example.sreal.picandaudio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

    public static File getAudioFile(){
        File fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!fileDirectory.exists()){
            fileDirectory.mkdirs();
        }
        return new File(fileDirectory,"1.mp3");
    }

    public static String addAudioTag(){
        String filePath = getAudioFile().getAbsolutePath();
        byte [] data = readFileFromSdcard(filePath);
        String body = Base64.encodeToString(data,Base64.DEFAULT);
        return body;
    }

    private static byte[] readFileFromSdcard(String filePath) {
        FileInputStream fis = null;
        byte [] data = null;
        try {
            fis = new FileInputStream(filePath);
            int size  = fis.available();
            data = new byte[size];
            fis.read(data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
