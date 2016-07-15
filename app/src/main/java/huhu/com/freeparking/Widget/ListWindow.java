package huhu.com.freeparking.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import huhu.com.freeparking.R;

/**
 * Created by Huhu on 7/13/16.
 * 显示商家与停车券对应关系的窗口
 */
public class ListWindow extends PopupWindow {
    //弹窗视图
    private View view;
    //数据信息
    private ArrayList<TicketBean> arrayList;
    //列表视图
    private ListView listView;


    /**
     * 构造函数
     */
    public ListWindow(final Context context, final ArrayList<TicketBean> arrayList, int width, int height) {
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listwindow, null);
        this.arrayList = arrayList;
        //初始化视图
        listView = (ListView) view.findViewById(R.id.lv_detail);
        listView.setAdapter(new LotlistAdapter(context, arrayList));
        setFocusable(true);
        setOutsideTouchable(true);
        setHeight(height);
        setWidth(width);
        setContentView(view);

    }


    /**
     * 适配器类
     */
    class LotlistAdapter extends BaseAdapter {
        private ArrayList<TicketBean> arrayList;
        private LayoutInflater inflater;

        public LotlistAdapter(Context context, ArrayList<TicketBean> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.item, null);
            }
            TextView sname = (TextView) view.findViewById(R.id.tv_sname);
            TextView snum = (TextView) view.findViewById(R.id.tv_snum);
            sname.setText(arrayList.get(i).getSname());
            snum.setText(arrayList.get(i).getSnum());
            return view;
        }
    }

}
