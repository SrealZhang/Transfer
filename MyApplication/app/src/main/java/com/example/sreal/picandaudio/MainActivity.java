package com.example.sreal.picandaudio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnChoosePic, btnRecordAudio, dialogButtonRecord, playRecord;
    public ImageView ivShowImage;
    public AlertDialog alertDialog;
    public TextView tvState;
    public MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListener();
    }

    private void setListener() {
        btnChoosePic.setOnClickListener(this);
        btnRecordAudio.setOnClickListener(this);
        playRecord.setOnClickListener(this);
    }

    private void init() {
        btnChoosePic = (Button) findViewById(R.id.btn_choosePic);
        btnRecordAudio = (Button) findViewById(R.id.btn_recordAudio);
        playRecord = (Button) findViewById(R.id.btn_playRecord);
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
                    Toast.makeText(MainActivity.this, body, Toast.LENGTH_SHORT).show();
                    Log.i("info", body);
                    //TODO 接下来调用发送消息的方法将图片发送出去。
                    Toast.makeText(MainActivity.this,"调用发送消息方法将图片发送出去",Toast.LENGTH_SHORT).show();
                    Bitmap bitmap1 = ChatUtil.getBitmap(body);
                    ivShowImage.setImageBitmap(bitmap1);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choosePic:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 200);
                break;
            case R.id.btn_recordAudio:
                showRecordDialog();
                break;

            case R.id.btn_playRecord:
                playRecordedAudio();

        }
    }

    private void playRecordedAudio() {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            String path = ChatUtil.getAudioFile().getAbsolutePath();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showRecordDialog() {
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        View view = View.inflate(MainActivity.this, R.layout.record, null);
        alertDialog.setView(view);
        alertDialog.show();
        dialogButtonRecord = (Button) view.findViewById(R.id.btn_recorder);
        tvState = (TextView) view.findViewById(R.id.tv_recorder_state);
        dialogButtonRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (mediaRecorder == null){
                            try {
                                mediaRecorder = new MediaRecorder();
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                                mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/1.mp3");
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mediaRecorder.stop();
                        mediaRecorder.release();
                        mediaRecorder = null;
                        alertDialog.cancel();

                        String body = ChatUtil.addAudioTag();
                        //TODO 调用发送消息方法将音频发送出去。
                        Toast.makeText(MainActivity.this,"调用发送消息方法将音频发送出去",Toast.LENGTH_SHORT).show();
                        break;

                }
                return false;
            }
        });

    }
}
