package wakeb.tech.drb.imagepiker.features.camera;

import android.content.Context;
import android.content.Intent;

import wakeb.tech.drb.imagepiker.features.common.BaseConfig;

public interface CameraModule {
    Intent getCameraIntent(Context context, BaseConfig config);
    void getImage(Context context, Intent intent, OnImageReadyListener imageReadyListener);
    void removeImage();
}
