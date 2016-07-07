package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import huhu.com.freeparking.Network.GetIconBitmap;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Constants;

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
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                PersonInfoActivity.this.finish();
            }
        });
    }
}
