package com.example.jh.textviewforhtml;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private Drawable drawable;
    private Handler handler;
    private RecyclerView mRecyclerView;
    private ListView mListView;
    private TextView mTiem;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String Html5="Once I was part of many posts, of which my popularity shone. \\n\\nMany a thread friend I had, alas they were gone. \\n\\nAlone I felt, not realizing the error of my ways. \\n\\nFor I was on the staging server, and all the posts were a forum away.<br /><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/lnv/attachments/lnv/ll07_en/1/1/Family_guy_bird_is_the_word_by_tehinvisible-d48dzo9.jpg\" /></span>";
        String fiveimage= "<P><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 200px;\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/t5/image/serverpage/image-id/315iC02ECA6021B034F8/image-size/small?v=1.0&amp;px=200\" alt=\"Desert.jpg\" title=\"Desert.jpg\" /></span></P><P>small<span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 999px;\"><img src=\"http://lnv.stage.lithium.com/t5/image/serverpage/image-id/317iEEB8C0BECA3BCDAE/image-size/large?v=1.0&amp;px=999\" alt=\"Desert.jpg\" title=\"Desert.jpg\" /></span></P><P>Large</P>";
        String fourimage=  "<P>3. \"Options \" menu in a message.</P>\n<P>&nbsp;</P>\n<UL>\n<LI><STRONG>6'' screen&nbsp;</STRONG></LI>\n</UL>\n<P><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 480px;\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/t5/image/serverpage/image-id/131i182F86B2A3C6CA64/image-size/large?v=1.0&amp;px=999\" width=\"480\" height=\"853\" alt=\"6'' screen - Options.jpeg\" title=\"6'' screen - Options.jpeg\" /></span></P>\n<P>&nbsp;</P>\n<UL>\n<LI><STRONG>5'' screen </STRONG></LI>\n</UL>\n<P>&nbsp;<span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 480px;\"><img src=\"http://lnv.stage.lithium.com/t5/image/serverpage/image-id/134iA6F4851BFA8CDDFF/image-size/large?v=1.0&amp;px=999\" width=\"480\" height=\"853\" alt=\"5'' screen - Options.png\" title=\"5'' screen - Options.png\" /></span></P>\n<UL>\n<LI>4'' screen </LI>\n</UL>\n<P><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 480px;\"><img src=\"http://lnv.stage.lithium.com/t5/image/serverpage/image-id/135iA2517238585DFFD4/image-size/large?v=1.0&amp;px=999\" alt=\"4'' screen - Options.jpg\" title=\"4'' screen - Options.jpg\" /></span></P>";
        String threeimage="<P>Insert photos from Website.</P><P><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 200px;\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/t5/image/serverpage/image-id/318i4859A1D8FD2731B1/image-size/small?v=1.0&amp;px=200\" alt=\"Koala.jpg\" title=\"Koala.jpg\" /></span></P><P><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 400px;\"><img src=\"http://lnv.stage.lithium.com/t5/image/serverpage/image-id/319i462C12347AD55BB0/image-size/medium?v=1.0&amp;px=400\" alt=\"Koala.jpg\" title=\"Koala.jpg\" /></span></P><P><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width: 999px;\"><img src=\"http://lnv.stage.lithium.com/t5/image/serverpage/image-id/320i95E93C28CE624A5A/image-size/large?v=1.0&amp;px=999\" alt=\"Koala.jpg\" title=\"Koala.jpg\" /></span></P>";
        String bigImageHtml="<span class=\"lia-inline-image-display-wrapper lia-image-align-inline\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/lnv/attachments/lnv/ThinkTeam/61/1/overear-color-mixr-green-RGB-thrqrtright.jpg\" /></span>";
        String HtmlforImage2="<P>kfjaj</P><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width:400px\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/lnv/attachments/lnv/YogaTeam/326/1/guidelines_bg.png\" /></span>";
        String HtmlforImage = "hznz<span class=\"lia-inline-image-display-wrapper lia-image-align-inline\" style=\"width:100px\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/lnv/attachments/lnv/YogaTeam/325/1/5841e2669a1aa37af9791756.jpg\" /></span>";
        String HtmlforUrl = "<P><STRONG><SPAN>Ashley Maria had it made. The recent film school grad was ready to take Hollywood by storm. But something strange happened along the way. She ran into wall after brick wall—obstacles that seemed to exist only for women in her industry. Now she’s exacting her revenge the only way she knows how: by making a movie about it.<A href=\"http://www.ashley-maria.com/\" target=\"_blank\" rel=\"nofollow noopener noreferrer\">Ashley</A> spoke to us recently about her life in LA and some dazzling new frontiers in filmmaking.</SPAN></STRONG></P>";
        mTextView = (TextView) findViewById(R.id.textforhtml);
        mTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("mmmwaibian","jdkaf");
            }
        });
        mTiem = (TextView) findViewById(R.id.text_time);
        mEditText = (EditText) findViewById(R.id.edittext);
        initHtml(Html5);
        HtmlThreeImage.inithtml2(threeimage,mTextView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter mainAdapter = new MainAdapter(this,bigImageHtml);
        mRecyclerView.setAdapter(mainAdapter);
        mListView = (ListView) findViewById(R.id.listview);
        //mListView.setAdapter(new ListAdapter1(this,HtmlforImage));
        HTMLView text1 = (HTMLView) findViewById(R.id.Html_textview);
        text1.setText(HtmlforImage2);
        ImageView scroImageView = (ImageView) findViewById(R.id.scroimage);
        scroImageView.setOnClickListener(this);
    }



    private void initHtml(final String htmlforImage) {
        mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        TextToHtmlUtils2.getBitmap(new TextToHtmlUtils2.ToHtml() {
            @Override
            public void toFile(CharSequence imageGetter) {
                //mTextView.setText(imageGetter);
                mEditText.setText(imageGetter);
            }
        },htmlforImage,MainActivity.this,mTextView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scroimage:
                /*Intent intent = new Intent(MainActivity.this, ScroImageActivity.class);
                intent.putExtra("scro",true);
                startActivity(intent);*/
                long l = System.currentTimeMillis();
                String s = String.valueOf(l);
                String s1 = mEditText.getText().toString();
                mTiem.setText(s1);
                Log.d("mmmmedittext",s1);
                startActivity(new Intent(this,ImageActivity.class));
                break;
        }
    }
}
