package wakeb.tech.drb.imagepiker.features;

import android.content.Intent;

import java.util.List;

import wakeb.tech.drb.imagepiker.model.Image;

public interface ImagePickerInteractionListener {
    void setTitle(String title);
    void cancel();
    // Get this callback by calling an ImagePickerFragment's finishPickImages() method. It
    // removes Images whose files no longer exist.
    void finishPickImages(Intent result);

    /**
     * Called when the user selects or deselects sn image. Also called in onCreateView.
     * May include Images whose files no longer exist.
     */
    void selectionChanged(List<Image> imageList);
}
