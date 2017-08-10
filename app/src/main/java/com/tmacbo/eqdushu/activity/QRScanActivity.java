//package com.tmacbo.eqdushu.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.AssetFileDescriptor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Vibrator;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//import android.view.View;
//
//import com.cloudfin.common.bean.req.BaseReq;
//import com.cloudfin.common.bean.resp.BaseResp;
//import com.cloudfin.common.server.ProcessManager;
//import com.cloudfin.common.utils.CommonConstants;
//import com.cloudfin.common.utils.Utils;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.ChecksumException;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.FormatException;
//import com.google.zxing.NotFoundException;
//import com.google.zxing.Result;
//import com.google.zxing.common.HybridBinarizer;
//import com.google.zxing.qrcode.QRCodeReader;
//import com.ieyecloud.doctor.R;
//import com.ieyecloud.doctor.common.service.Service;
//import com.ieyecloud.doctor.common.utils.ToastUtils;
//import com.ieyecloud.doctor.common.view.ActionView;
//import com.ieyecloud.doctor.common.view.BaseActivity;
//import com.ieyecloud.doctor.contact.AddPatientReqData;
//import com.ieyecloud.doctor.contact.activity.UserProfileActivity;
//import com.ieyecloud.doctor.newcontacts.AddContactResp;
//import com.ieyecloud.doctor.newcontacts.UserProfileNewActivity;
//import com.ieyecloud.doctor.picker.CommonImagePickerDetailActivity;
//import com.ieyecloud.doctor.picker.CommonImagePickerListActivity;
//import com.ieyecloud.doctor.picker.util.CommonUtils;
//import com.ieyecloud.doctor.qrcode.camera.CameraManager;
//import com.ieyecloud.doctor.qrcode.decoding.CaptureActivityHandler;
//import com.ieyecloud.doctor.qrcode.decoding.InactivityTimer;
//import com.ieyecloud.doctor.qrcode.decoding.RGBLuminanceSource;
//import com.ieyecloud.doctor.qrcode.view.ViewfinderView;
//import com.lidroid.xutils.view.annotation.ContentView;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.netease.nim.uikit.common.util.string.StringUtil;
//
//import java.io.IOException;
//import java.util.Hashtable;
//import java.util.Vector;
//
//
///**
// * @Author zhangyuanlong
// * @Since on 16/3/30  15:18
// * @Description 二维码扫描页面
// */
//@ContentView(value = R.layout.activity_qr)
//public class QRScanActivity extends AppCompatActivity implements Callback {
//
//    public static final int IMAGE_PICKER_REQUEST_CODE = 100;
//    private static final int REQ_FOR_FIND_PATIENT = CommonConstants.FIRST_VAL++;
//
//
//    private CaptureActivityHandler handler;
//    private ViewfinderView viewfinderView;
//    private boolean hasSurface;
//    private Vector<BarcodeFormat> decodeFormats;
//    private String characterSet;
//    private InactivityTimer inactivityTimer;
//    private MediaPlayer mediaPlayer;
//    private boolean playBeep;
//    private static final float BEEP_VOLUME = 0.10f;
//    private boolean vibrate;
//    SurfaceView surfaceView;
//
//    @ViewInject(R.id.viewAction)
//    private ActionView viewAction;
//
//    private Context mContext;
//
//    String imagePath;
//
//    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////		setContentView(R.activity_answer_my_template.activity_qr);
//        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
//        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        mContext = this;
//
//        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//
//        hasSurface = false;
//        inactivityTimer = new InactivityTimer(this);
////        initHeadBlue();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        CameraManager.init(this);
//        surfaceView.setVisibility(View.VISIBLE);
//        surfaceView.setOnClickListener(this);
//
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(this);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//        decodeFormats = null;
//        characterSet = null;
//
//        playBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
//        initBeepSound();
//        vibrate = true;
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (handler != null) {
//            handler.quitSynchronously();
//        }
//        handler = null;
//        surfaceView.setVisibility(View.INVISIBLE);
//        CameraManager.get().closeDriver();
//    }
//
//    @Override
//    public void setViewSize() {
//        initHeadColor(R.color.b4);
//        setTitleColorAndSize(R.color.w4, R.dimen.z3);
//        viewAction.setVisibility(View.VISIBLE);
//        viewAction.updateView(2, null, getResources().getDrawable(R.drawable.btn_bg_scan_picture_selecor));
////
//    }
//
//
//    @Override
//    public void setTitle(CharSequence title) {
//        super.setTitle(getResources().getString(R.string.home_scan_str));
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        if (v == surfaceView) {
//            if(null == handler)
//                handler = new CaptureActivityHandler(this, decodeFormats,
//                    characterSet);
//            handler.requestAutoFocus();
//        } else if (v == viewAction) {
//            startActivityForResult(new Intent(mContext, CommonImagePickerListActivity.class), IMAGE_PICKER_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//
//    /**
//     * 根据手机号搜索患者accid
//     */
//    public void addPatientReq(String mobile) {
//        showProgressDialog(false, 0);
//        AddPatientReqData data = new AddPatientReqData();
//        data.setPhone(mobile);
//        data.setUserType("m");
//        BaseReq req = new BaseReq(Service.Key_patient_find, data);
//        ProcessManager.getInstance().addProcess(this, req, this);
//    }
//
//    @Override
//    public void call(int id, Object... args) {
//        if (id == REQ_FOR_FIND_PATIENT) {
//            AddContactResp resp = (AddContactResp) args[0];
//            if (null != resp && null != resp.getData()) {
//                if (resp.getData().isIfFriend())
//                    UserProfileNewActivity.start(QRScanActivity.this, resp.getData().getUserId() + "", false, null);
//                else
//                    UserProfileNewActivity.start(QRScanActivity.this, resp.getData().getUserId() + "", true, "isNotFriend");
//            }
////            NimUserInfoCache.getInstance().getUserInfoFromRemote(resp.getData().getAccid(),null);
////            query(resp.getData().getAccid());
//        }
//    }
//
//
//    @Override
//    protected boolean callBack(BaseResp responseBean) {
//        if (responseBean.getKey().equals(Service.Key_patient_find)) {
//            cancelLoadingDialog();
//            if (responseBean.isOk()) {
//                runCallFunctionInHandler(REQ_FOR_FIND_PATIENT, responseBean);
//            } else if (!Utils.isEmpty(responseBean.getRspInf())) {
//                showToastText(responseBean.getRspInf());
//            } else {
//                showToastText(responseBean.getRspInf());
//            }
//        }
//        return false;
//    }
//
//
//    private void query(final String account) {
////        final String account = mEtSearch.getText().toString().toLowerCase();
////        cancelLoadingDialog();
//        if (!StringUtil.isEmpty(account)) {
//            UserProfileActivity.start(QRScanActivity.this, account);
//        } else {
//            ToastUtils.askToastSingle(QRScanActivity.this, getString(R.string.user_not_exsit), getString(R.string.user_tips), null);
//        }
//    }
//
//
//    /**
//     * 处理扫描结果
//     *
//     * @param result
//     * @param barcode
//     */
//    public void handleDecode(Result result, Bitmap barcode) {
//        inactivityTimer.onActivity();
//        playBeepSoundAndVibrate();
//        if (result == null || result.getText().equals("")) {
//            showToastText(getResources().getString(R.string.tips_decode_null));
//        } else {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("result", result.getText());
//            if (Utils.isEmpty(result.getText())) {
//                return;
//            }
////            showToastText("扫描结果：" + result.getText());
//            String phone = result.getText();
//            if (phone.contains("?")) {
//                phone = phone.split("\\?")[1];
//                //可以兼容兼容老版本
//                if (phone.contains("&")) {
//                    String[] phones = phone.split("&");
//                    System.out.println("phones =" + phones.length);
//                    for (String phoneItem : phones) {
//                        if (phoneItem.contains("mulin_")) {
//                            String[] phones2 = phoneItem.split("_");
//                            if (phones2.length >= 2) {
//                                phone = phones2[phones2.length - 1];
//                            } else
//                                phone = "";
//                            break;
//                        }
//                    }
//                }
//            }
//            addPatientReq(phone);
////            showToastText("扫描结果："+result.getText());
////			bundle.putParcelable("bitmap", barcode);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
//        }
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        try {
//            CameraManager.get().openDriver(surfaceHolder);
//        } catch (IOException ioe) {
//            return;
//        } catch (RuntimeException e) {
//            return;
//        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(this, decodeFormats,
//                    characterSet);
//            if (!CommonUtils.isEmpty(imagePath)) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Result result = scanningImage(imagePath);
//                        Message m = handler.obtainMessage();
//                        m.what = R.id.decode_succeeded;
//                        m.obj = result;
//                        handler.sendMessage(m);
//                        imagePath = null;
//                    }
//                }).start();
//            }
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//
//    }
//
//    public ViewfinderView getViewfinderView() {
//        return viewfinderView;
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void drawViewfinder() {
//        viewfinderView.drawViewfinder();
//
//    }
//
//    private void initBeepSound() {
//        if (playBeep && mediaPlayer == null) {
//            // The volume on STREAM_SYSTEM is not adjustable, and users found it
//            // too loud,
//            // so we now play on the music stream.
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(beepListener);
//
//            AssetFileDescriptor file = getResources().openRawResourceFd(
//                    R.raw.beep);
//            try {
//                mediaPlayer.setDataSource(file.getFileDescriptor(),
//                        file.getStartOffset(), file.getLength());
//                file.close();
//                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null;
//            }
//        }
//    }
//
//    private static final long VIBRATE_DURATION = 200L;
//
//    private void playBeepSoundAndVibrate() {
//        if (playBeep && mediaPlayer != null) {
//            mediaPlayer.start();
//        }
//        if (vibrate) {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(VIBRATE_DURATION);
//        }
//    }
//
//    /**
//     * When the beep has finished playing, rewind to queue up another one.
//     */
//    private final OnCompletionListener beepListener = new OnCompletionListener() {
//        public void onCompletion(MediaPlayer mediaPlayer) {
//            mediaPlayer.seekTo(0);
//        }
//    };
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//
//        if (requestCode == IMAGE_PICKER_REQUEST_CODE) {
//            imagePath = data.getStringExtra(CommonImagePickerDetailActivity
//                    .KEY_BUNDLE_RESULT_IMAGE_PATH);
//
//
//        }
//    }
//
//    /**
//     * 扫描二维码图片的方法
//     *
//     * @param path
//     * @return
//     */
//    public Result scanningImage(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return null;
//        }
//        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
//        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true; // 先获取原大小
//        Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
//        options.inJustDecodeBounds = false; // 获取新的大小
//        int sampleSize = (int) (options.outHeight / (float) 200);
//        if (sampleSize <= 0)
//            sampleSize = 1;
//        options.inSampleSize = sampleSize;
//        scanBitmap = BitmapFactory.decodeFile(path, options);
//        System.out.println("path = " + path + ";scanBitmap = " + scanBitmap);
//        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
//        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
//        QRCodeReader reader = new QRCodeReader();
//        try {
//            return reader.decode(bitmap1, hints);
//
//        } catch (NotFoundException e) {
//            e.printStackTrace();
//        } catch (ChecksumException e) {
//            e.printStackTrace();
//        } catch (FormatException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}