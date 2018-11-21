package com.BFMe.BFMBuyer.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Description：图片处理工具
 * Created by : 奚兆进
 * Company    ：白富美（北京）网络科技有限公司
 * Date       ：2017/4/11 10:09
 */

public class ImageUtils {
    /**
     * 将bitmap转化成图片
     */
    public static String getImage(Bitmap b, String name) throws Exception {

        String path = Environment.getExternalStorageDirectory() + "/bfmeIMG";
        File mediaFile = new File(path + File.separator + name + ".jpg");
        if (mediaFile.exists()) {
            mediaFile.delete();
        }
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(mediaFile);
        b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        fos.flush();
        fos.close();
        b.recycle();
        System.gc();
        return mediaFile.getPath();
    }

    /**
     * 将相册获取的图片url转成String
     *
     * @param uri
     * @return
     */
    public static String getPath(Activity context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
