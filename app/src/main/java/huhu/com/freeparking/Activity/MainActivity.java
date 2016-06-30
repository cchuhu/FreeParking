package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import huhu.com.freeparking.R;

/**
 * 登陆后的主界面，显示通车数量
 */
public class MainActivity extends Activity {
    //个人中心按钮
    private Button btn_personal;
    //二维码扫描按钮
    private Button btn_qrscan;
    //显示通车数量
    private TextView tv_count;
    private TextView tv_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 初始化界面布局
     */
    private void initViews() {
        btn_personal = (Button) findViewById(R.id.btn_personal);
        btn_qrscan = (Button) findViewById(R.id.btn_qrscan);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_number = (TextView) findViewById(R.id.tv_number);
    }
}
