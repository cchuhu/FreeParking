package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import huhu.com.freeparking.R;

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
    }

}
