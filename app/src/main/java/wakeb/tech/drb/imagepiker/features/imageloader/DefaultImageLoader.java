package wakeb.tech.drb.imagepiker.features.imageloader;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import wakeb.tech.drb.R;
import wakeb.tech.drb.imagepiker.features.imageloader.ImageType;

public class DefaultImageLoader implements ImageLoader {

    @Override
    public void loadImage(String path, ImageView imageView, wakeb.tech.drb.imagepiker.features.imageloader.ImageType imageType) {
        Glide.with(imageView.getContext())
                .load(path)
                .apply(RequestOptions
                        .placeholderOf(imageType == wakeb.tech.drb.imagepiker.features.imageloader.ImageType.FOLDER
                                ? R.drawable.ef_folder_placeholder
                                : R.drawable.ef_image_placeholder)
                        .error(imageType == ImageType.FOLDER
                                ? R.drawable.ef_folder_placeholder
                                : R.drawable.ef_image_placeholder)
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}
