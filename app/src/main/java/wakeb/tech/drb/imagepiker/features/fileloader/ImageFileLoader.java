package wakeb.tech.drb.imagepiker.features.fileloader;

import java.io.File;
import java.util.ArrayList;

import wakeb.tech.drb.imagepiker.features.common.ImageLoaderListener;

public interface ImageFileLoader {

    void loadDeviceImages(
            final boolean isFolderMode,
            final boolean onlyVideo,
            final boolean includeVideo,
            final boolean includeAnimation,
            final ArrayList<File> excludedImages,
            final ImageLoaderListener listener
    );

    void abortLoadImages();
}
