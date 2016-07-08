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
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import huhu.com.freeparking.Network.GetToken;
import huhu.com.freeparking.Network.Register;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Config;
import huhu.com.freeparking.Util.CutImageUtil;
import huhu.com.freeparking.Util.NetworkState;
import huhu.com.freeparking.Util.ToastBuilder;
import huhu.com.freeparking.Widget.BitmapBean;

/**
 * 注册界面
 */
public class RegisterActivity extends Activity {
    private Button btn_back, btn_regist;
    private EditText edt_register_acccount, edt_register_name, edt_register_pass, edt_register_conpass;
    private ImageView iv_register_icon;
    //token值
    private String str_token;
    //判断网络状况的标志位
    private Boolean isOnline = false;
    //上传资料的实例
    private String str_account, str_name, str_pwd, str_conpwd, str_urlicon;
    //上传图片的base url
    private String str_Baseurl = "http://7xvu2c.com1.z0.glb.clouddn.com/";
    //图片资源的url
    private String str_url = "";
    //图片资源的文件名
    private String filename = "";
    //打开本地相册的类型
    private final String IMAGE_TYPE = "image/*";
    private int IMAGE_CODE = 1;
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
        setContentView(R.layout.activity_register);
        initViews();

    }

    /**
     * 初始化视图资源
     */
    private void initViews() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_regist = (Button) findViewById(R.id.btn_regist);
        edt_register_acccount = (EditText) findViewById(R.id.edt_register_account);
        edt_register_conpass = (EditText) findViewById(R.id.edt_register_conpass);
        edt_register_pass = (EditText) findViewById(R.id.edt_register_pass);
        edt_register_name = (EditText) findViewById(R.id.edt_register_name);
        iv_register_icon = (ImageView) findViewById(R.id.img_register_icon);
        setListener();
    }

    /**
     * 设置监听器的方法
     */
    private void setListener() {
        //为注册界面设置监听
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //首先判断网络是否可用
                isOnline = NetworkState.isOnline(RegisterActivity.this);
                if (isOnline) {
                    //上传资料注册
                    uploadInit();
                }
                //如果网络不可用，弹出提示框
                else {
                    Toast.makeText(RegisterActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                }

            }
        });
        iv_register_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                RegisterActivity.this.finish();
            }
        });
    }

    /**
     * 上传全部信息，注册用户
     */
    private void uploadInfo() {
        new Register(Config.URL_REGISTER, str_url, str_account, str_name, str_pwd, new Register.registerSuccess() {
            @Override
            public void onSuccess(String result) {
                ToastBuilder.Build("注册成功！", RegisterActivity.this);

            }
        }, new Register.registerFailed() {
            @Override
            public void onFailed() {
                ToastBuilder.Build("注册失败！", RegisterActivity.this);
            }
        }) {

        };

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
    private Boolean uploadPic(String token) {
        //获取图片唯一地址
        str_url = getIMEI() + getTime();
        flag = str_url;
        str_url = str_url.replace(" ", "%20");
        str_url = str_url.replace(":", "%3A");
        str_url = str_Baseurl + str_url;
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(filename, flag, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);

            }
        }, null);


        return false;
    }


    /**
     * 检查所需资料是否为空
     *
     * @return
     */
    private Boolean isNotNull() {
        str_account = edt_register_acccount.getText().toString();
        str_pwd = edt_register_pass.getText().toString();
        str_conpwd = edt_register_conpass.getText().toString();
        str_name = edt_register_name.getText().toString();
        //先检查文字资料是否齐全
        if (str_account.equals("") || str_name.equals("") || str_pwd.equals("") || str_conpwd.equals("") || filename.equals("")) {
            ToastBuilder.Build("请完善资料", RegisterActivity.this);
            return false;
        } else {
            //如果密码与确认密码不匹配，提示
            if (!str_pwd.equals(str_conpwd)) {
                ToastBuilder.Build("请正确输入密码", RegisterActivity.this);
                return false;
            } else {
                if (str_account.length() >= 15 || str_pwd.length() >= 15) {
                    ToastBuilder.Build("账号密码的长度不能大于15位", RegisterActivity.this);
                    return false;
                }
                return true;
            }
        }

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
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                str_token = "error";
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
                    iv_register_icon.setImageBitmap(bean.getBitmap());


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {

                    return;
                }
                break;
            case RESULT_CANCELED: // 用户取消照相
                return;
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
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        RegisterActivity.this.finish();
    }
}
