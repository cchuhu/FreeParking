package huhu.com.freeparking.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import huhu.com.freeparking.R;


/**
 * Created by Huhu on 5/5/16.
 * 展示签到人员信息的弹出菜单
 */
public class CheckWindow extends PopupWindow {
    //弹窗视图
    private View view;
    //姓名 电话 工作职位 字符串
    private String name, tel, job;
    //上下文对象
    private Context context;
    //View对象
    private TextView tv_name, tv_tel, tv_job;

    /**
     * 构造函数
     *
     * @param name 姓名
     * @param tel  电话
     * @param job  工作职位
     */
    public CheckWindow(String name, String tel, String job, Context context, int width, int height) {
        this.name = name;
        this.tel = tel;
        this.job = job;
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popwindow, null);

        tv_name.setText(name);
        tv_job.setText(job);
        tv_tel.setText(tel);
        setHeight(height);
        setWidth(width);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CheckWindow.this.isShowing()) {
                    dismiss();
                }
            }
        });
    }


}
