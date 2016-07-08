package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import huhu.com.freeparking.Network.ChangeInfo;
import huhu.com.freeparking.Network.GetIconBitmap;
import huhu.com.freeparking.Network.GetToken;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Config;
import huhu.com.freeparking.Util.Constants;
import huhu.com.freeparking.Util.CutImageUtil;
import huhu.com.freeparking.Util.NetworkState;
import huhu.com.freeparking.Util.ToastBuilder;
import huhu.com.freeparking.Widget.BitmapBean;

/**
 * 个人中心页面
 */
public class PersonInfoActivity extends Activity {
    //标题栏的按钮
    private Button btn_back;
    private Button btn_logoff;
    //头像图片
    private ImageView iv_person_icon;
    //账号信息
    private TextView tv_account;
    //姓名信息（可修改）
    private EditText edt_name;
    //保存按钮
    private Button btn_save;
    //修改密码按钮
    private Button btn_changePass;
    //图片资源的文件名
    private String filename = "";
    //token值
    private String str_token;
    //打开本地相册的类型
    private final String IMAGE_TYPE = "image/*";
    private int IMAGE_CODE = 1;
    //上传图片的base url
    private String str_Baseurl = "http://7xvu2c.com1.z0.glb.clouddn.com/";
    //图片资源的url
    private String str_url = "";
    private String manager_name;
    //标识符
    private String flag;
    /**
     * 0:上传图片
     * 1：图片上传成功，上传资料
     */

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    uploadPic(str_token);
                    break;
                case 1:
                    uploadInfo();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initViews();
    }

    /**
     * 初始化视图资源
     */
    private void initViews() {
        btn_back = (Button) findViewById(R.id.btn_personback);
        btn_logoff = (Button) findViewById(R.id.btn_logoff);
        iv_person_icon = (ImageView) findViewById(R.id.img_person_icon);
        tv_account = (TextView) findViewById(R.id.tv_personAccount);
        edt_name = (EditText) findViewById(R.id.edt_name);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_changePass = (Button) findViewById(R.id.btn_changePass);
        initSource();
        setListener();
    }

    /**
     * 初始化数据资源
     */
    private void initSource() {
        new GetIconBitmap(iv_person_icon).execute(Constants.Manager_Icon.replace(" ", "%20"));
        tv_account.setText(Constants.Manager_Account);
        edt_name.setText(Constants.Manager_Name);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PersonInfoActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                PersonInfoActivity.this.finish();
            }
        });
        iv_person_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkState.isOnline(PersonInfoActivity.this)) {
                    uploadInit();
                } else {
                    ToastBuilder.Build("请连接网络", PersonInfoActivity.this);
                }

            }
        });
        btn_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PersonInfoActivity.this, ChangePassActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                PersonInfoActivity.this.finish();
            }
        });
    }

    /**
     * 上传全部信息，注册用户
     */
    private void uploadInfo() {
        new ChangeInfo(Config.URL_CHANGE, Constants.Manager_Account, Constants.Manager_Icon, manager_name, new ChangeInfo.changeSuccess() {
            @Override
            public void onSuccess(String result) {

                if (result.equals("0")) {
                    Constants.Manager_Name = manager_name;
                    ToastBuilder.Build("修改成功", PersonInfoActivity.this);
                } else {
                    ToastBuilder.Build("修改失败", PersonInfoActivity.this);
                }
            }
        }, new ChangeInfo.changeFailed() {
            @Override
            public void onFailed() {

            }
        });

    }

    /**
     * 获取相册照片的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK:
                try {
                    Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    ContentResolver cr = this.getContentResolver();
                    Bitmap album_bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    if (album_bitmap == null) {
                    }
                    album_bitmap = ThumbnailUtils.extractThumbnail(album_bitmap, 180, 180);
                    BitmapBean bean = CutImageUtil.setImageRoundCorner(album_bitmap);
                    filename = bean.getFilename();
                    iv_person_icon.setImageBitmap(bean.getBitmap());
                    //获取图片唯一地址
                    str_url = getIMEI() + getTime();
                    flag = str_url;
                    str_url = str_url.replace(" ", "%20");
                    str_url = str_url.replace(":", "%3A");
                    str_url = str_Baseurl + str_url;
                    Constants.Manager_Icon = str_url;

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {

                    return;
                }
                break;
            case RESULT_CANCELED: // 用户取消
                return;
        }
    }

    /**
     * 上传注册资料，先获取token上传头像，再上传资料
     */
    private void uploadInit() {
        if (isNotNull()) {
            //获取上传图片的token,上传图片到七牛
            getToken();
        } else {
            Log.e("资料不完善", "不能上传");
        }
    }

    /**
     * @param token 上传凭证
     * @return 返回是否上传成功的布尔值
     */
    private void uploadPic(String token) {


        UploadManager uploadManager = new UploadManager();
        uploadManager.put(filename, flag, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);

            }
        }, null);


    }

    /**
     * 获取上传头像图片的token
     *
     * @return
     */
    private void getToken() {
        //获取token
        new GetToken(Config.URL_GETOKEN, new GetToken.onTokenSuccess() {
            @Override
            public void onSuccess(String result) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(result);
                    str_token = jo.getString("uptoken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);

            }
        }, new GetToken.onTokenFailed() {
            @Override
            public void onFailed() {
                Toast.makeText(PersonInfoActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                str_token = "error";
            }
        });

    }

    /**
     * 检查所需资料是否为空
     *
     * @return
     */
    private Boolean isNotNull() {
        manager_name = edt_name.getText().toString();
        //先检查文字资料是否齐全
        if (manager_name.equals("")) {
            ToastBuilder.Build("请完善资料！", PersonInfoActivity.this);
            return false;
        } else {
            return true;
        }

    }

    /**
     * 获取头像照片上传时间
     *
     * @return
     */
    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        return time;
    }

    /**
     * 获取手机标识符
     *
     * @return
     */
    public String getIMEI() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        return IMEI;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PersonInfoActivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        PersonInfoActivity.this.finish();

    }
}
