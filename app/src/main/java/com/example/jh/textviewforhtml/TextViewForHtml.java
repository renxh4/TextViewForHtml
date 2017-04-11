package com.example.jh.textviewforhtml;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.xml.sax.XMLReader;

/**
 * Created by jh on 2017/1/16.
 */
public class TextViewForHtml extends TextView {
    private CharSequence mText;
    private Context mContext;

    public TextViewForHtml(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (null == text) {
            this.mText = "";
        } else {
            this.mText = text;
        }
        this.mText = text;
        Spanned spanned = Html.fromHtml(this.mText.toString(), new MyImageGetter(), new MyTagHandler());

        super.setText(spanned, type);
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return LinkMovementMethod.getInstance();
    }

    class MyImageGetter implements Html.ImageGetter {

        /**
         * This method is called when the HTML parser encounters an
         * <img> tag.  The <code>source</code> argument is the
         * string from the "src" attribute; the return value should be
         * a Drawable representation of the image or <code>null</code>
         * for a generic replacement image.  Make sure you call
         * setBounds() on your Drawable if it doesn't already have
         * its bounds set.
         *
         * @param source
         */
        @Override
        public Drawable getDrawable(String source) {
           /*在这里根据source来加载图片，并返回*/
           /*简单测试，返回程序ic_lanucher*/
           /*网络图片需要异步加载，在此处发起异步加载线程，图片加载完成后再设置一次setText，
               当再次执行到此处时，将加载好的图片（应存放在缓存中）返回就行了*/
            Drawable drawable = null;
            if (Build.VERSION.SDK_INT < 21) {
                drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
            } else {
                drawable = getResources().getDrawable(R.mipmap.ic_launcher, null);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());
            }

            return drawable;
        }
    }

    /**
     * 用来通知当解析器遇到无法识别的标签时该作出何种处理
     */
    class MyTagHandler implements Html.TagHandler {


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
                output.setSpan(new ImageClickSpan(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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

    class ImageClickSpan extends ClickableSpan {

        private Context context;
        private String url;


        public ImageClickSpan(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            Log.e("TAG-->", "ImageClickSpan");
            //showPicDialog(url);
        }
    }
}
