package huhu.com.freeparking.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import huhu.com.freeparking.R;


/**
 * Created by Huhu on 5/5/16.
 * 展示签到人员信息的弹出菜单
 */
public class CheckWindow extends PopupWindow {
    //弹窗视图
    private View view;


    /**
     * 构造函数
     */
    public CheckWindow(Context context, int width, int height) {
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popwindow, null);
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
