package huhu.com.freeparking.Widget;

import android.graphics.Bitmap;

/**
 * Created by Huhu on 7/6/16.
 * 返回包装好的图片的路径和Bitmap
 */
public class BitmapBean {
    private String filename;
    private Bitmap bitmap;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
