package wakeb.tech.drb.imagepiker.features.camera;

import java.util.List;

import wakeb.tech.drb.imagepiker.model.Image;

public interface OnImageReadyListener {
    void onImageReady(List<Image> image);
}
