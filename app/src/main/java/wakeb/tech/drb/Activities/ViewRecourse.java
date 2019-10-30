package wakeb.tech.drb.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import butterknife.BindView;
import butterknife.OnClick;
import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.R;

public class ViewRecourse extends BaseActivity {

    TextView address_tv, desc_tv;
    ImageView image;
    MxVideoPlayerWidget videoView;

    String type , address , url , desc;

    @BindView(R.id.desc_layout)
    LinearLayout desc_layout;

    @OnClick(R.id.view_recourse_image)
    void view_recourse_image()
    {
        Intent intent =  new Intent(this , ShowImage.class);
        intent.putExtra("URL" , url);
        startActivity(intent);
    }

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recourse);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable;
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                drawable = getResources().getDrawable(R.drawable.background_svg, getTheme());
            } else {
                drawable = VectorDrawableCompat.create(getResources(), R.drawable.background_svg, getTheme());
            }
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(drawable);
        }
        else
        {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                type = null;
                address = null;
                url = null;
                desc = null;

            } else {
                type = extras.getString("TYPE");
                address = extras.getString("ADDRESS");
                url = extras.getString("URL");
                desc = extras.getString("DESC");

                Log.i("extras.getString", type +address + url +desc);

            }
        } else {
            type = (String) savedInstanceState.getSerializable("TYPE");
            address = (String) savedInstanceState.getSerializable("ADDRESS");
            url = (String) savedInstanceState.getSerializable("URL");
            desc = (String) savedInstanceState.getSerializable("DESC");

        }

        address_tv = (TextView) findViewById(R.id.view_recourse_address);
        desc_tv = (TextView) findViewById(R.id.view_recourse_desc);
        image = (ImageView) findViewById(R.id.view_recourse_image);
        videoView = (MxVideoPlayerWidget) findViewById(R.id.view_recourse_video);

        address_tv.setText(address);
        desc_tv.setText(desc);

        if (TextUtils.isEmpty(desc)) {
            desc_layout.setVisibility(View.GONE);
        }

        if (type.equals("image"))
        {
            Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
            image.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(getApplicationContext())
                    .load(url)
                    .apply(new RequestOptions()
                            .placeholder(image.getDrawable())
                    )
                    .into(image);

        }
        else
        {
            Toast.makeText(this, type, Toast.LENGTH_SHORT).show();

            image.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.startPlay(url, MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "");



        }
    }

    @Override
    protected void setViewListeners() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean isValidData() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MxVideoPlayer.releaseAllVideos();
    }
}



