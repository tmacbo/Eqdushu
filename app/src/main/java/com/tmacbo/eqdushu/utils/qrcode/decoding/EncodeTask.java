package com.tmacbo.eqdushu.utils.qrcode.decoding;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by zhangyuanlong on 16/6/6.
 * 二维码生成工具，使用详见AddFriendActivity
 */
public class EncodeTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView mQRCodeImage;
    private Encoder mEncoder;

    public EncodeTask(ImageView qrCodeImage, int imgWidth, int imgHeight) {
        this.mQRCodeImage = qrCodeImage;
        this.mEncoder = new Encoder.Builder()
                .setBackgroundColor(0xFFFFFF)
                .setCodeColor(0xFF000000)
                .setOutputBitmapPadding(0)
                .setOutputBitmapWidth(imgWidth)
                .setOutputBitmapHeight(imgHeight)
                .build();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return mEncoder.encode(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mQRCodeImage.setImageBitmap(bitmap);
//        mEncoder = null;
//        bitmap.recycle();
//        bitmap = null;
    }
}
