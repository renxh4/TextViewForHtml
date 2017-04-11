package com.example.jh.textviewforhtml;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jh on 2017/2/8.
 */
public class HtmlThreeImage {

    private static Handler mHandler;

    public static void inithtml2(final String htmlforImage, final TextView mTextView) {
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        Html.ImageGetter imageGetter = new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                Log.d("mmmmdrable", "getDrawable: "+source);
                // TODO Auto-generated method stub
                URL url;
                Drawable drawable = null;

                try {
                    url = new URL(source);
                    drawable = getThread(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return drawable;
            }
        };

        mTextView.setText(Html.fromHtml(htmlforImage, imageGetter, null));
    }

    @NonNull
    public static Drawable getThread(final URL url) {
        // 因为从网上下载图片是耗时操作 所以要开启新线程
        final Drawable[] mDrawable = new Drawable[1];
        Thread t =  new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    mDrawable[0] = Drawable.createFromStream(
                            url.openStream(), null);
                    mDrawable[0].setBounds(0, 0,
                            mDrawable[0].getIntrinsicWidth(),
                            mDrawable[0].getIntrinsicHeight());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        return mDrawable[0];
    }
}
