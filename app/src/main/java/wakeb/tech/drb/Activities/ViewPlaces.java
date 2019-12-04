package wakeb.tech.drb.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.DefaultExceptionHandler;

public class ViewPlaces extends BaseActivity {

    private static Context c;
    public Dialog d;

    TextView address_tv, desc_tv;
    ImageView image;


    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    String type, url, desc;

    LatLng address;

    @BindView(R.id.viewPlacesProgress)
    ProgressBar viewPlacesProgress;

    @BindView(R.id.desc_layout)
    LinearLayout desc_layout;

    @OnClick(R.id.view_recourse_image)
    void view_recourse_image() {
        Intent intent = new Intent(this, ShowImage.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

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
        } else {
            Window window = getWindow();
            window.setBackgroundDrawableResource(R.drawable.background_png);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_places);


        type = getIntent().getStringExtra("TYPE");
        address = getIntent().getExtras().getParcelable("ADDRESS");
        url = getIntent().getStringExtra("URL");
        desc = getIntent().getStringExtra("DESC");


        address_tv = (TextView) findViewById(R.id.view_recourse_address);
        desc_tv = (TextView) findViewById(R.id.view_recourse_desc);
        image = (ImageView) findViewById(R.id.view_recourse_image);


        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(address.latitude, address.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() != 0) {
            address_tv.setText(addresses.get(0).getAddressLine(0));
        }
        desc_tv.setText(desc);
        if (TextUtils.isEmpty(desc)) {
            desc_layout.setVisibility(View.GONE);
        }

        Glide.with(getApplicationContext())
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(image.getDrawable())
                )
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewPlacesProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image);


    }



    @Override
    protected void init() {

    }



}