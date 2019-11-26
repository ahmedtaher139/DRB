package wakeb.tech.drb.imagepiker.helper;

import androidx.core.content.FileProvider;

import wakeb.tech.drb.imagepiker.features.ImagePickerComponentHolder;

public class ImagePickerFileProvider extends FileProvider {
    @Override
    public boolean onCreate() {
        ImagePickerComponentHolder.getInstance().init(getContext());
        return super.onCreate();
    }
}
