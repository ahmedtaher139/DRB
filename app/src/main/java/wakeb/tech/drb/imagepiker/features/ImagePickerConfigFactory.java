package wakeb.tech.drb.imagepiker.features;

import android.content.Context;

import java.util.ArrayList;

import wakeb.tech.drb.imagepiker.features.ImagePickerConfig;
import wakeb.tech.drb.imagepiker.features.ImagePickerSavePath;
import wakeb.tech.drb.imagepiker.features.IpCons;
import wakeb.tech.drb.imagepiker.features.ReturnMode;
import wakeb.tech.drb.imagepiker.features.cameraonly.CameraOnlyConfig;

public class ImagePickerConfigFactory {

    public static CameraOnlyConfig createCameraDefault() {
        CameraOnlyConfig config = new CameraOnlyConfig();
        config.setSavePath(wakeb.tech.drb.imagepiker.features.ImagePickerSavePath.DEFAULT);
        config.setReturnMode(wakeb.tech.drb.imagepiker.features.ReturnMode.ALL);
        return config;
    }

    public static wakeb.tech.drb.imagepiker.features.ImagePickerConfig createDefault(Context context) {
        wakeb.tech.drb.imagepiker.features.ImagePickerConfig config = new ImagePickerConfig();
        config.setMode(wakeb.tech.drb.imagepiker.features.IpCons.MODE_MULTIPLE);
        config.setLimit(IpCons.MAX_LIMIT);
        config.setShowCamera(true);
        config.setFolderMode(false);
        config.setSelectedImages(new ArrayList<>());
        config.setSavePath(ImagePickerSavePath.DEFAULT);
        config.setReturnMode(ReturnMode.NONE);
        return config;
    }
}
