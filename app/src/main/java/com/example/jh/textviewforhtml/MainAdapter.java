package com.example.jh.textviewforhtml;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by jh on 2017/1/16.
 */
public class MainAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private final String image;

    public MainAdapter(Context context, String htmlforImage) {
        this.mContext = context;
        this.image=htmlforImage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = View.inflate(mContext, R.layout.item, null);
        MyViewHolder myViewHolder = new MyViewHolder(inflate);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        TextToHtmlUtils.getBitmap(new TextToHtmlUtils.ToHtml() {
            @Override
            public void toFile(CharSequence bitmap) {
                myViewHolder.mItemText.setText(bitmap);
            }
        },image,mContext);
        //myViewHolder.mWebView.loadDataWithBaseURL(null, image, "text/html", "utf-8", null);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mItemText;
        private final WebView mWebView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.item_textview);
            mWebView = (WebView) itemView.findViewById(R.id.item_webview);
            mItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("mmm","点击了");
                }
            });
            mItemText.setClickable(true);
            mItemText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
