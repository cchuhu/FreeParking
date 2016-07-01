package huhu.com.freeparking.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Huhu on 5/6/16.
 * 创建toast的工具类
 */
public class ToastBuilder {
    public static void Build(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
