package huhu.com.freeparking.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import huhu.com.freeparking.Widget.BitmapBean;

/**
 * Created by Huhu on 8/12/15.
 * 将图片切割为圆角矩形的工具类
 */
public class CutImageUtil {

    public static String filename;

    public static BitmapBean bean = new BitmapBean();

    public static BitmapBean setImageRoundCorner(Bitmap bitmap) {
        new DateFormat();
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        FileOutputStream fout = null;
        final String SDpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(SDpath + "/sdcard/parklot/");
        file.mkdirs();
        filename = file.getPath() + name;
        try {
            fout = new FileOutputStream(filename);

            bitmap = toRoundCorner(bitmap, 10);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fout.flush();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bean.setFilename(filename);
        bean.setBitmap(bitmap);
        return bean;
    }

    /**
     * 将图片设置为圆角
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
