package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import huhu.com.freeparking.Network.GetIcon;
import huhu.com.freeparking.Network.GetIconBitmap;
import huhu.com.freeparking.Network.Login;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Config;
import huhu.com.freeparking.Util.Constants;
import huhu.com.freeparking.Util.NetworkState;
import huhu.com.freeparking.Util.ToastBuilder;

/**
 * 登陆界面
 */
public class LoginActivity extends Activity {
    //头像图片
    private ImageView iv_icon;
    //登录框账号和密码
    private EditText edt_account;
    private EditText edt_pass;
    //登陆按钮
    private Button btn_login;
    //注册按钮
    private Button btn_register;
    //获取到的账号和密码
    private String account, pwd;
    //头像图片的路径
    private String manager_img;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(msg.obj.toString());
                        manager_img = jo.get("manager_img").toString();
                        new GetIconBitmap(iv_icon).execute(manager_img.replace(" ", "%20"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

    }

    /**
     * 初始化视图
     */
    private void initViews() {
        iv_icon = (ImageView) findViewById(R.id.img_login_icon);
        edt_account = (EditText) findViewById(R.id.edt_login_account);
        edt_pass = (EditText) findViewById(R.id.edt_login_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_toregister);
        setListener();
    }

    /**
     * 设置监听器的方法
     */
    private void setListener() {
        //为注册界面设置监听
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkState.isOnline(LoginActivity.this)) {
                    account = edt_account.getText().toString();
                    pwd = edt_pass.getText().toString();
                    if (account.equals("") || pwd.equals("")) {
                        ToastBuilder.Build("请正确输入信息", LoginActivity.this);
                    } else {
                        new Login(Config.URL_LOGIN, account, pwd, new Login.loginSuccess() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    //1是登陆失败，成功之后会返回一个当天停车场的停车数量
                                    if (result.equals("1")) {
                                        ToastBuilder.Build("登陆失败", LoginActivity.this);
                                    } else {
                                        //先保存关键信息(账号、头像)
                                        Constants.Manager_Account = account;
                                        Constants.Manager_Icon = manager_img;
                                        //获取当天停车场车辆数量并跳转到主界面
                                        JSONObject jo = new JSONObject(result);
                                        Constants.CAR_COUNT = jo.get("car_count").toString();
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        LoginActivity.this.finish();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Login.loginFailed() {
                            @Override
                            public void onFailed() {

                            }
                        });
                    }

                } else {
                    ToastBuilder.Build("请连接网络", LoginActivity.this);
                }
            }

        });
        //设置实时显示头像的监听
        edt_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                account = edt_account.getText().toString();
                if (!account.equals("")) {
                    new GetIcon(Config.URL_GETICON, account, new GetIcon.getIconSuccess() {
                        @Override
                        public void onSuccess(String result) {
                            //获取头像成功，去下载图片
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    }, new GetIcon.getIconFailed() {
                        @Override
                        public void onFailed() {

                        }
                    });
                }
            }

        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                LoginActivity.this.finish();

            }
        });

    }

}
