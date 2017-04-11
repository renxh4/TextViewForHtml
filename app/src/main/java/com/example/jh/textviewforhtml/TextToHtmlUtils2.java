package com.example.jh.textviewforhtml;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jh on 2016/10/10.
 */
public class TextToHtmlUtils2 {

    private static Dialog mDialog;
    private static Context montext;

    public static void getBitmap(final ToHtml compressBitmap, final String url, final Context context, final TextView textView) {
        TextToHtmlUtils2.montext = context;
        Observable.create(new Observable.OnSubscribe<CharSequence>() {
            @Override
            public void call(Subscriber<? super CharSequence> subscriber) {
                //这里去加载图片
                CharSequence getbitmap = getbitmap(url);
                Log.d("mmmmagain", "完了");
                subscriber.onNext(getbitmap);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onNext(CharSequence bitmap) {
                        compressBitmap.toFile(bitmap);
                        //textView.setText(bitmap);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private static CharSequence getbitmap(String url) {
        Log.d("mmmdrablehaha", "I AM COMING");
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public boolean istrue = true;

            @Override
            public Drawable getDrawable(String source) {
                // TODO Auto-generated method stub
                Log.d("mmmdrablehaha", "getDrawable: " + source);
                Drawable drawable = null;
                try {
                    String[] split = source.split("/");
                    String s = split[split.length - 1];
                    String saveFilePaht = BitmapUtils.getSDCardPath() + File.separator + s;
                    File file = new File(saveFilePaht);
                    OkHttpManager instance = OkHttpManager.getInstance(montext);
                    Response response = instance._getAsyn1(source);
                    byte[] bytes = response.body().bytes();
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    //BitmapFactory.decodeStream(inputStream, null, opts);
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
                    int i = computeInitialSampleSize(opts, -1, 1000 * 1000);
                    opts.inSampleSize = i;
                    opts.inJustDecodeBounds = false;
                    opts.inInputShareable = true;
                    opts.inPurgeable = true;
                    opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
                    BitmapUtils.compressAndSaveBitmapToSDCard(bitmap1, s, 80);
                    Log.d("mmmmurl", source + "/" + s);
                    drawable = BitmapUtils.bitmaptodrawable(bitmap1);
                    Log.d("mmmmimage", "不存在" + i);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    Log.d("mmmmimage", "这里会bug吗" );
                } catch (Exception e) {
                    Log.d("mmmmagain", "bug");
                    e.printStackTrace();
                }
                return drawable;
            }
        };

        CharSequence test = Html.fromHtml(url, imageGetter, new MyTagHandler());
        return test;
    }

    public static abstract class ToHtml {
        public abstract void toFile(CharSequence bitmap);
    }

    /**
     * 用来通知当解析器遇到无法识别的标签时该作出何种处理
     */
    static class MyTagHandler implements Html.TagHandler {


        /**
         * 参数：
         * opening：为true时表示某个标签开始解析,为false时表示该标签解析完
         * tag:当前解析的标签
         * output:文本中的内容
         * xmlReader:xml解析器
         */
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            Log.e("TAG-->", tag);
            Log.e("output-->", output.toString());
            if (tag.toLowerCase().equals("img")) {//解析<img/>标签（注意标签格式不是<img></img>）
                Log.e("opening-->", opening + "");
                int len = output.length();
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                Log.e("images-->", images.length + "");
                String imgURL = images[0].getSource();
                Log.e("imgURL-->", imgURL + "");
                //添加点击事件
                output.setSpan(new ImageClickSpan(montext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else if (tag.equalsIgnoreCase("strike")) {//自定义解析<strike></strike>标签
                int len = output.length();
                Log.e("opening-->", opening + "");
                if (opening) {//开始解析该标签，打一个标记
                    output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
                } else {//解析结束，读出所有标记，取最后一个标记为当前解析的标签的标记（因为解析方式是便读便解析）
                    StrikethroughSpan[] spans = output.getSpans(0, len, StrikethroughSpan.class);
                    if (spans.length > 0) {
                        for (int i = spans.length - 1; i >= 0; i--) {
                            if (output.getSpanFlags(spans[i]) == Spannable.SPAN_MARK_MARK) {
                                int start = output.getSpanStart(spans[i]);
                                output.removeSpan(spans[i]);
                                if (start != len) {
                                    output.setSpan(new StrikethroughSpan(), start, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                break;
                            }
                        }
                    }
                }
            } else {//其他标签不再处理
                Log.e("TAG-->", tag + "--不做处理");
            }
        }
    }

    static class ImageClickSpan extends ClickableSpan {

        private Context context;
        private String url;


        public ImageClickSpan(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            Log.e("TAG-->", "ImageClickSpan");
            showPicDialog(url);
        }


    }

    private static void showPicDialog(String url) {
        // Context contetx = BaseApplcation.getContetx();
        String[] split = url.split("/");
        String s = split[split.length - 1];
        String saveFilePaht = BitmapUtils.getSDCardPath() + File.separator + s;
        Intent intent = new Intent(montext, ImageActivity.class);
        intent.putExtra("path", saveFilePaht);
        montext.startActivity(intent);
    }

    //定义一个根据图片url获取InputStream的方法
    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 用数据装
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
        is.close();
        // 关闭流一定要记得。
        return outstream.toByteArray();
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
