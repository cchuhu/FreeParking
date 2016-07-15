package huhu.com.freeparking.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import huhu.com.freeparking.Network.GetCarCount;
import huhu.com.freeparking.Network.GetDetail;
import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Config;
import huhu.com.freeparking.Util.Constants;
import huhu.com.freeparking.Util.ToastBuilder;
import huhu.com.freeparking.Widget.ListWindow;
import huhu.com.freeparking.Widget.TicketBean;

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
    //设置 弹出窗口
    private ListWindow listWindow;
    //列表
    private ArrayList<TicketBean> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //获取停车数量
        new GetCarCount(Config.URL_GETCOUNT, Constants.Manager_Account, new GetCarCount.getSuccess() {
            @Override
            public void onSuccess(String result) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(result);
                    Constants.CAR_COUNT = jo.get("car_count").toString();
                    //设置首页数据
                    tv_count.setText(Constants.CAR_COUNT);
                    tv_number.setText(Constants.CAR_COUNT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new GetCarCount.getFailed() {
            @Override
            public void onFailed() {

            }
        });
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

                Intent i = new Intent(MainActivity.this, PersonInfoActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                MainActivity.this.finish();
            }
        });
        //为二维码扫描界面设置监听器
        btn_qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CaptureActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                MainActivity.this.finish();


            }
        });
        //弹出商家与停车券的对应关系
        tv_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetDetail(Config.URL_GETSUM, Constants.Manager_Account, new GetDetail.getSuccess() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("1")) {
                            ToastBuilder.Build("获取信息失败", MainActivity.this);
                        } else {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(result);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //解析消费数据
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    TicketBean ticketBean = new TicketBean();
                                    ticketBean.setSname(jsonObject.get("seller_name").toString());
                                    ticketBean.setSnum(jsonObject.get("ticket_num").toString());
                                    arrayList.add(ticketBean);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        showDetail(tv_count, arrayList);
                    }
                }, new GetDetail.getFailed() {
                    @Override
                    public void onFailed() {

                    }
                });

            }
        });


    }

    /**
     * 显示商家与停车券对应关系的窗口
     */
    private void showDetail(View view, final ArrayList<TicketBean> arrayList) {
        //设置弹出窗口
        WindowManager wm = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels - 100;
        int height = outMetrics.heightPixels / 2;
        listWindow = new ListWindow(MainActivity.this, arrayList, width, height);
        listWindow.setFocusable(true);
        backgroundAlpha(0.5f);
        listWindow.setOutsideTouchable(true);
        listWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                arrayList.clear();
            }
        });
        listWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
