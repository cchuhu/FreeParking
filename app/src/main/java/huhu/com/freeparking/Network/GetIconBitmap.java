package huhu.com.freeparking.Network;

import android.os.AsyncTask;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * 下载图片并显示的工具类
 */

public class GetIconBitmap extends AsyncTask {
    public ImageView iv;

    public GetIconBitmap(ImageView iv) {
        this.iv = iv;

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        iv.setImageBitmap((Bitmap) o);
    }

    @Override
    protected Bitmap doInBackground(Object[] params) {
        String url = (String) params[0];
        return loadImageFromNetwork(url);
    }

    /**
     * 从网络获取图片的方法
     *
     * @param imageUrl
     * @return
     */
    private Bitmap loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        Bitmap bm = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bm = bd.getBitmap();
        } catch (IOException e) {

        }

        return bm;
    }

}