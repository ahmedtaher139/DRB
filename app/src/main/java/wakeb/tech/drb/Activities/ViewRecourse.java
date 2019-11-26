package wakeb.tech.drb.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devbrackets.android.exomedia.ui.widget.VideoView;


import butterknife.BindView;
import butterknife.OnClick;

import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.R;

public class ViewRecourse extends BaseActivity {

    TextView address_tv, desc_tv;
    ImageView image;
    VideoView videoView;

    String type , address , url , desc;

    @BindView(R.id.desc_layout)
    LinearLayout desc_layout;

    MediaController mediaController;

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
        mediaController =  new MediaController(this);
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

            type = getIntent().getStringExtra("TYPE");
            address =getIntent().getStringExtra("ADDRESS");
            url = getIntent().getStringExtra("URL");
            desc = getIntent().getStringExtra("DESC");

        address_tv = (TextView) findViewById(R.id.view_recourse_address);
        desc_tv = (TextView) findViewById(R.id.view_recourse_desc);
        image = (ImageView) findViewById(R.id.view_recourse_image);
        videoView = (VideoView) findViewById(R.id.view_recourse_video);

        address_tv.setText(address);
        desc_tv.setText(desc);

        if (TextUtils.isEmpty(desc)) {
            desc_layout.setVisibility(View.GONE);
        }

        if (type.equals("image"))
        {
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

            image.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(url));

        }
    }



    @Override
    protected void init() {

    }




}



