package wakeb.tech.drb.imagepiker.features.common;

import java.util.List;

import wakeb.tech.drb.imagepiker.model.Folder;
import wakeb.tech.drb.imagepiker.model.Image;

public interface ImageLoaderListener {
    void onImageLoaded(List<Image> images, List<Folder> folders);
    void onFailed(Throwable throwable);
}
