package wakeb.tech.drb.imagepiker.listeners;

import java.util.List;

import wakeb.tech.drb.imagepiker.model.Image;

public interface OnImageSelectedListener {
    void onSelectionUpdate(List<Image> selectedImage);
}
