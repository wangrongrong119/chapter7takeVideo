package com.bytedance.camera.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bytedance.camera.demo.utils.Utils;

import java.io.File;

import static com.bytedance.camera.demo.utils.Utils.rotateImage;

public class TakePictureActivity extends AppCompatActivity {

//    private static final Activity ActivityCompat = ;
    private ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private File imgFile;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        imageView = findViewById(R.id.img);

        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(TakePictureActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(TakePictureActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //todo 在这里申请相机、存储的权限
                Log.d("nonono","读写联系人权限未被授予，需要申请！");
                ActivityCompat.requestPermissions((Activity)this,new String[]{ Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE);

            } else {
                Log.d("okokok","权限已经被授予，显示细节页面！");

                takePicture();
            }
        });

    }

    private void takePicture() {
        //todo 打开相机

        Intent takePictureIntent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        //因为需要存储并显示

        imgFile = Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE);
        if(imgFile!=null){
            Uri fileuri=FileProvider.getUriForFile(this,"com.bytedance.camera.demo",imgFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileuri);
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            setPic();
        }
    }

    private void setPic() {




        //todo 根据imageView裁剪
        int targetW=imageView.getWidth();
        int targetH=imageView.getHeight();
        //todo 根据缩放比例读取文件，生成Bitmap
        BitmapFactory.Options  bmOptions =new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);

        int photoW=bmOptions.outWidth;
        int photoH=bmOptions.outHeight;

        int scaleFactor=Math.min(photoW/targetW,photoH/targetH);
        bmOptions.inJustDecodeBounds=false;
        bmOptions.inSampleSize=scaleFactor;
        bmOptions.inPurgeable=true;

        Bitmap bmp=BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
        imageView.setImageBitmap(bmp);







        //todo 如果存在预览方向改变，进行图片旋转
//        rotateImage(Bitmap bitmap, String path);



        //todo 如果存在预览方向改变，进行图片旋转
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                //todo 判断权限是否已经授予
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    takePicture();


                }
                break;
            }
        }
    }
}
