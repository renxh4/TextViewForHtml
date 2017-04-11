package com.example.jh.textviewforhtml;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jh on 2017/1/18.
 */
public class BitmapUtils {
    public static String SD_CARD_DIR = Environment.getExternalStorageDirectory() + "/";
    public static String FILE_DIR = SD_CARD_DIR + "lenovo/community";
    public static final String IMAGE = FILE_DIR + "/image/";

    //读取SD卡下的图片
    public static Bitmap getBitmapInputStreamFromSDCard(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String SDCarePath = Environment.getExternalStorageDirectory().toString();
            String filePath = SDCarePath + File.separator + fileName;
            File file = new File(filePath);
            Log.d("mmmmsize", file.length() + "/"+"已经缩小");
            try {
               /* FileInputStream fileInputStream = new FileInputStream(file);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(fileInputStream, null, opts);
                opts.inSampleSize = 2;
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
                opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
                fileInputStream.reset();
                Bitmap bitmap1 = BitmapFactory.decodeStream(fileInputStream,new Rect(),opts);*/
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath,opts);
                opts.inSampleSize = 4;
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
                opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
                Bitmap bitmap1 = BitmapFactory.decodeFile(filePath,opts);
                return bitmap1;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    //压缩且保存图片到SDCard
    public static void compressAndSaveBitmapToSDCard(Bitmap rawBitmap, String fileName, int quality) {
        File dir = new File(IMAGE);
        if (!dir.exists()) {
            dir.mkdirs();
            Log.d("haha", "create file success");
        }
        String saveFilePaht = getSDCardPath() + File.separator + fileName;
        Log.d("mmmmhaha", "wojinlaile" + saveFilePaht);
        Log.d("mmmmhaha", "path" + IMAGE);
        File saveFile = new File(saveFilePaht);
        if (!saveFile.exists()) {
            Log.d("mmmmhaha", "不存在");
            try {
                saveFile.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
                if (fileOutputStream != null) {
                    //imageBitmap.compress(format, quality, stream);
                    //把位图的压缩信息写入到一个指定的输出流中
                    //第一个参数format为压缩的格式
                    //第二个参数quality为图像压缩比的值,0-100.0 意味着小尺寸压缩,100意味着高质量压缩
                    //第三个参数stream为输出流
                    rawBitmap.compress(Bitmap.CompressFormat.PNG, quality, fileOutputStream);
                    Log.d("mmmmhaha", "储存中");
                }
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("mmmmhaha", "失败了");
            }
        }
    }

    //获取SDCard的目录路径功能
    public static String getSDCardPath() {
        String SDCardPath = null;
        // 判断SDCard是否存在
        boolean IsSDcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (IsSDcardExist) {
            SDCardPath = Environment.getExternalStorageDirectory().toString();
        }
        return SDCardPath;
    }

    public static Drawable bitmaptodrawable(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        BitmapDrawable bit = (BitmapDrawable) drawable;
        Bitmap bitmap = bit.getBitmap();
        return bitmap;
    }

    public static void saveFile(Bitmap bitmap, String name) throws IOException {
        File dir = new File(IMAGE);
        if (!dir.exists()) {
            dir.mkdirs();
            Log.d("haha", "create file success");
        }
        File file = new File(IMAGE + name);
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
        fos.flush();
        fos.close();
    }
}
