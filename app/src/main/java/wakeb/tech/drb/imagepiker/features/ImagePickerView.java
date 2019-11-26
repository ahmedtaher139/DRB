package wakeb.tech.drb.imagepiker.features;

import java.util.List;

import wakeb.tech.drb.imagepiker.features.common.MvpView;
import wakeb.tech.drb.imagepiker.model.Folder;
import wakeb.tech.drb.imagepiker.model.Image;

public interface ImagePickerView extends MvpView {
    void showLoading(boolean isLoading);
    void showFetchCompleted(List<Image> images, List<Folder> folders);
    void showError(Throwable throwable);
    void showEmpty();
    void showCapturedImage();
    void finishPickImages(List<Image> images);
}
