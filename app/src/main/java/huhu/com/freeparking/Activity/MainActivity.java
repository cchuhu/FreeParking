package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import huhu.com.freeparking.Network.GetInfo;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Config;
import huhu.com.freeparking.Util.Constants;
import huhu.com.freeparking.Util.NetworkState;
import huhu.com.freeparking.Util.ToastBuilder;

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
        initViews();
        Log.e("执行onCreate", "生命周期");
    }


    @Override
    protected void onResume() {
        super.onResume();
        //获取停车数量
    }


    /**
     * 初始化界面布局
     */
    private void initViews() {
        btn_personal = (Button) findViewById(R.id.btn_personal);
        btn_qrscan = (Button) findViewById(R.id.btn_qrscan);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_number = (TextView) findViewById(R.id.tv_number);
        setListener();
        //设置首页数据
        tv_count.setText(Constants.CAR_COUNT);
        tv_number.setText(Constants.CAR_COUNT);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        //为个人中心界面设置监听器
        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkState.isOnline(MainActivity.this)) {
                    new GetInfo(Config.URL_GETINFO, Constants.Manager_Account, new GetInfo.getInfoSuccess() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject jo = new JSONObject(result);
                                Constants.Manager_Name = jo.get("manager_name").toString();
                                Intent i = new Intent(MainActivity.this, PersonInfoActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new GetInfo.getInfoFailed() {
                        @Override
                        public void onFailed() {

                        }
                    });
                } else {
                    ToastBuilder.Build("无法获取个人信息，请连网", MainActivity.this);
                }


            }
        });
        //为二维码扫描界面设置监听器
        btn_qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
