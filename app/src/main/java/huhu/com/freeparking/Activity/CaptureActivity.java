package huhu.com.freeparking.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;

import huhu.com.freeparking.Network.CheckTicket;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Config;
import huhu.com.freeparking.Util.NetworkState;
import huhu.com.freeparking.Util.ToastBuilder;
import huhu.com.freeparking.Widget.CheckWindow;
import huhu.com.freeparking.zxing.AmbientLightManager;
import huhu.com.freeparking.zxing.BeepManager;
import huhu.com.freeparking.zxing.CameraManager;
import huhu.com.freeparking.zxing.CaptureActivityHandler;
import huhu.com.freeparking.zxing.FinishListener;
import huhu.com.freeparking.zxing.InactivityTimer;
import huhu.com.freeparking.zxing.ViewfinderView;

/**
 * 二维码扫描的界面
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    //相机管理器
    private CameraManager cameraManager;
    //显示人数的textView
    private TextView tv_num;
    //展示信息的popupwindow
    private CheckWindow checkWindow;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            checkWindow.dismiss();

        }
    };
    //返回按钮
    private ImageButton btn_back;
    //摄像头标志
    private int REQUEST_CAMERA = 0;

    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;

    //----------------------------------
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.capture);
        initViews();
    }

    /**
     * 初始化资源
     */
    private void initViews() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CaptureActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                CaptureActivity.this.finish();
            }
        });


    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        handler = null;
        resetStatusView();

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            getAppVersionAndTakePermission(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);

        inactivityTimer.onResume();

        decodeFormats = null;
        characterSet = null;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        viewfinderView.recycleLineDrawable();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAMERA:// ���������
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            getAppVersionAndTakePermission(holder);
            // initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 处理识别结果
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) throws UnsupportedEncodingException {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        String msg = rawResult.getText();
        if (msg == null || "".equals(msg)) {
            msg = "�޷�ʶ��";
        }
        if (msg.equals("")) {
            Toast.makeText(CaptureActivity.this, "无法识别", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("msg", msg);
            /**
             * 在这里处理结果
             */
            String ticket_id = URLDecoder.decode(msg, "utf-8");
            if (NetworkState.isOnline(CaptureActivity.this)) {
                checkTicket(ticket_id);
            } else {
                ToastBuilder.Build("请连接网络", CaptureActivity.this);
            }
        }
        restartPreviewAfterDelay(1000);

    }

    /**
     * 验证停车券是否合法
     *
     * @param id
     */
    private void checkTicket(String id) {
        new CheckTicket(Config.URL_CHECK, id, new CheckTicket.checkSuccess() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("0")) {
                    showDetail(viewfinderView);
                }
                if (result.equals("1")) {
                    ToastBuilder.Build("该停车券未绑定用户", CaptureActivity.this);
                }
                if (result.equals("2")) {
                    ToastBuilder.Build("该停车券已使用", CaptureActivity.this);
                }
                if (result.equals("3")) {
                    ToastBuilder.Build("该停车券已过期", CaptureActivity.this);
                } else {
                    ToastBuilder.Build("检测失败", CaptureActivity.this);
                }

            }
        }, new CheckTicket.checkFailed() {
            @Override
            public void onFailed() {

            }
        });

    }


    /**
     * 显示弹出窗口
     */
    private void showDetail(View view) {
        //设置弹出窗口
        WindowManager wm = (WindowManager) CaptureActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels - 100;
        int height = outMetrics.heightPixels / 2;
        checkWindow = new CheckWindow(CaptureActivity.this, width, height);
        checkWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        checkWindow.setOutsideTouchable(true);
        //设置窗口2秒后自动消失
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            return;
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("����");
        builder.setMessage("��Ǹ������������⣬��������Ҫ�����豸");
        builder.setPositiveButton("ȷ��", new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CaptureActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        CaptureActivity.this.finish();
    }

    //判断系统是否为6.0且获取权限(拍照)
    public void getAppVersionAndTakePermission(SurfaceHolder holder) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        //若是大于6.0，则需代码获取权限
        if (currentapiVersion >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            } else {
                initCamera(holder);
            }
        } else {//版本低于6.0
            initCamera(holder);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //做什么？
            } else {//拒绝了
                Toast.makeText(CaptureActivity.this, "摄像头权限拒绝", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(CaptureActivity.this, MainActivity.class);
                startActivity(i);
                CaptureActivity.this.finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
