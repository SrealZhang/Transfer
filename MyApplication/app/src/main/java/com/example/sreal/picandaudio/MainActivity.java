package com.example.sreal.picandaudio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public Button btnChoosePic;
    public ImageView ivShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListener();
    }

    private void setListener() {
        btnChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 200);
            }
        });
    }

    private void init() {
        btnChoosePic = (Button) findViewById(R.id.btn_choosePic);
        ivShowImage = (ImageView) findViewById(R.id.iv_showImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == this.RESULT_OK) {
            try {
                //得到选中的图片
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                if (bitmap != null) {
                    String body = ChatUtil.addImageTag(bitmap);
                    Toast.makeText(MainActivity.this,body,Toast.LENGTH_SHORT).show();
                    Log.i("info",body);
                    //TODO 接下来调用发送消息的方法将图片发送出去。
                    Bitmap bitmap1 = ChatUtil.getBitmap(body);
                    ivShowImage.setImageBitmap(bitmap1);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
