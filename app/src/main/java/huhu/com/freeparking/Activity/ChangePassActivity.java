package huhu.com.freeparking.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import huhu.com.freeparking.Network.ChangePwd;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Config;
import huhu.com.freeparking.Util.NetworkState;
import huhu.com.freeparking.Util.ToastBuilder;

/**
 * 修改密码页面
 */
public class ChangePassActivity extends AppCompatActivity {
    private Button btn_back, btn_save;
    private EditText edt_prepass, edt_newpass, edt_conpass;
    private String str_previous_pass, str_new_pass, str_conpass;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initViews();
    }

    /**
     * 初始化视图资源
     */
    private void initViews() {
        btn_back = (Button) findViewById(R.id.btn_personback);
        btn_save = (Button) findViewById(R.id.btn_save);
        edt_conpass = (EditText) findViewById(R.id.edt_confirm_pass);
        edt_prepass = (EditText) findViewById(R.id.edt_previous_pass);
        edt_newpass = (EditText) findViewById(R.id.edt_new_pass);
        setListener();

    }

    /**
     * 设置监听器
     */
    private void setListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChangePassActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                ChangePassActivity.this.finish();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkState.isOnline(ChangePassActivity.this)) {
                    if (isNotNull()) {
                        new ChangePwd(Config.URL_CHANGEPWD, str_previous_pass, str_new_pass, new ChangePwd.changeSuccess() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.equals("0")) {
                                    ToastBuilder.Build("修改成功,请重新登录", ChangePassActivity.this);
                                    //删除已保存的密码
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    //跳转到主界面
                                    Intent i = new Intent(ChangePassActivity.this, LoginActivity.class);
                                    startActivity(i);
                                    ChangePassActivity.this.finish();
                                } else {
                                    ToastBuilder.Build("修改失败，请重试", ChangePassActivity.this);
                                }
                            }
                        }, new ChangePwd.changeFailed() {
                            @Override
                            public void onFailed() {

                            }
                        });

                    }
                } else {
                    ToastBuilder.Build("请连接网络", ChangePassActivity.this);
                }


            }
        });

    }

    /**
     * 检测输入是否为空
     */
    private Boolean isNotNull() {
        str_previous_pass = edt_prepass.getText().toString();
        str_new_pass = edt_newpass.getText().toString();
        str_conpass = edt_conpass.getText().toString();

        //先检查文字资料是否齐全
        if (str_previous_pass.equals("") || str_new_pass.equals("") || str_conpass.equals("")) {
            ToastBuilder.Build("输入不能为空", ChangePassActivity.this);
            return false;
        } else {
            //如果密码与确认密码不匹配，提示
            if (!str_new_pass.equals(str_conpass)) {
                ToastBuilder.Build("请正确输入密码", ChangePassActivity.this);
                return false;
            } else {
                if (str_new_pass.length() >= 15) {
                    ToastBuilder.Build("密码的长度不能大于15位", ChangePassActivity.this);
                    return false;
                }
                return true;
            }
        }

    }

}
