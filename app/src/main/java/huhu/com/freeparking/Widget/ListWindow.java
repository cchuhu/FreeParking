package huhu.com.freeparking.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import huhu.com.freeparking.R;

/**
 * Created by Huhu on 7/13/16.
 * 显示商家与停车券对应关系的窗口
 */
public class ListWindow extends PopupWindow{
    //弹窗视图
    private View view;


    /**
     * 构造函数
     */
    public ListWindow(Context context, int width, int height) {
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popwindow, null);
        setHeight(height);
        setWidth(width);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ListWindow.this.isShowing()) {
                    dismiss();
                }
            }
        });
    }
}
