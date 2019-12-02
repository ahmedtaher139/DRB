package wakeb.tech.drb.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import wakeb.tech.drb.Activities.ShowImage;
import wakeb.tech.drb.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
  ArrayList<String> urls;

    LayoutInflater mLayoutInflater;

    public  SliderAdapter(Context context , ArrayList<String> urls){
        this.context=context;
        this.urls=urls;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.image_slider_item, container, false);

        PhotoView imageView = (PhotoView) itemView.findViewById(R.id.myZoomageView);
        ProgressBar zoomProgress = (ProgressBar) itemView.findViewById(R.id.zoomProgress);

        Glide.with(context)
                .load(urls.get(position))
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(context ,  R.string.connection_error, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        zoomProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .placeholder(imageView.getDrawable())
                )
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}