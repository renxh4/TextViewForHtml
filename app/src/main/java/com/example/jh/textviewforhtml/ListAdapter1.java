package com.example.jh.textviewforhtml;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by jh on 2017/1/17.
 */
public class ListAdapter1 extends BaseAdapter {

    private final Context mContext;
    private final String image;
    public int i=1;

    public ListAdapter1(Context context, String htmlforImage2) {
        this.mContext = context;
        this.image = htmlforImage2;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = View.inflate(mContext, R.layout.item, null);
        final TextView textView = (TextView) inflate.findViewById(R.id.item_textview);
        WebView webView = (WebView) inflate.findViewById(R.id.item_webview);

            i++;
            TextToHtmlUtils.getBitmap(new TextToHtmlUtils.ToHtml() {
                @Override
                public void toFile(CharSequence bitmap) {
                    textView.setText(bitmap);
                }
            }, image, mContext);


        return inflate;
    }
}
