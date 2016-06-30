package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import huhu.com.freeparking.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
    }

    /**
     * 初始化视图资源
     */
    private void initViews() {
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_logoff = (Button) findViewById(R.id.btn_logoff);
        iv_person_icon = (ImageView) findViewById(R.id.img_person_icon);
        tv_account = (TextView) findViewById(R.id.tv_personAccount);
        edt_name = (EditText) findViewById(R.id.edt_name);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_changePass = (Button) findViewById(R.id.btn_changePass);

    }
}
