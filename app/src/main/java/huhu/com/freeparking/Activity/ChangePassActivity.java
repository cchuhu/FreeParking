package huhu.com.freeparking.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import huhu.com.freeparking.R;

/**
 * 修改密码页面
 */
public class ChangePassActivity extends AppCompatActivity {
    private Button btn_back, btn_save;
    private EditText edt_prepass, edt_newpass, edt_conpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
    }

    /**
     * 初始化视图资源
     */
    private void initViews() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_save = (Button) findViewById(R.id.btn_save);
        edt_conpass = (EditText) findViewById(R.id.edt_confirm_pass);
        edt_prepass = (EditText) findViewById(R.id.edt_previous_pass);
        edt_newpass = (EditText) findViewById(R.id.edt_new_pass);

    }
}
