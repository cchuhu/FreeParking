package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import huhu.com.freeparking.R;

/**
 * 注册界面
 */
public class RegisterActivity extends Activity {
    private Button btn_back, btn_regist;
    private EditText edt_register_acccount, edt_register_name, edt_register_pass, edt_register_conpass;
    private ImageView iv_register_icon;

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
    }
}
