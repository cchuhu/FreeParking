package huhu.com.freeparking.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import huhu.com.freeparking.R;
import huhu.com.freeparking.Util.Constants;

/**
 * Created by Huhu on 7/13/16.
 * 显示停车场列表的菜单
 */
public class ParkListWindow extends PopupWindow {
    //弹窗视图
    private View view;
    //数据信息
    private ArrayList<ParkLotBean> arrayList;
    //列表视图
    private ListView listView;
    //

    /**
     * 构造函数
     */
    public ParkListWindow(final Context context, final ArrayList<ParkLotBean> arrayList, int width, int height, final TextView tv_choosePark) {
        view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.parklistwindow, null);
        this.arrayList = arrayList;
        //初始化视图
        listView = (ListView) view.findViewById(R.id.lv_park);
        listView.setAdapter(new LotlistAdapter(context, arrayList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constants.Lot_id = arrayList.get(i).getLot_id();
                Constants.Lot_address = arrayList.get(i).getLot_address();
                tv_choosePark.setText(Constants.Lot_address);
                dismiss();
            }
        });
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
        private ArrayList<ParkLotBean> arrayList;
        private LayoutInflater inflater;

        public LotlistAdapter(Context context, ArrayList<ParkLotBean> arrayList) {
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
                view = inflater.inflate(R.layout.parkiten, null);
            }
            TextView id = (TextView) view.findViewById(R.id.lot_id);
            TextView address = (TextView) view.findViewById(R.id.lot_address);
            id.setText(arrayList.get(i).getLot_id());
            address.setText(arrayList.get(i).getLot_address());
            return view;
        }
    }

}
