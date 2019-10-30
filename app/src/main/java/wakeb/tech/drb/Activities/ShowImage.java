package wakeb.tech.drb.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;

import butterknife.BindView;
import butterknife.OnClick;
import wakeb.tech.drb.Base.BaseActivity;
import wakeb.tech.drb.R;

public class ShowImage extends BaseActivity {

    @BindView(R.id.myZoomageView)
    ZoomageView myZoomageView;

    @BindView(R.id.zoomProgress)
    ProgressBar zoomProgress;

    @OnClick(R.id.back_button)
    void back_button() {
        finish();
    }

    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                URL = null;
            } else {
                URL = extras.getString("URL");
            }
        } else {
            URL = (String) savedInstanceState.getSerializable("URL");
        }

        Glide.with(this)
                .load(URL)
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(ShowImage.this, R.string.connection_error, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        zoomProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .placeholder(myZoomageView.getDrawable())
                )
                .into(myZoomageView);


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
}
