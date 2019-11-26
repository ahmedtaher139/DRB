package wakeb.tech.drb.imagepiker.model;

import java.util.ArrayList;
import java.util.List;

import wakeb.tech.drb.imagepiker.helper.ImagePickerUtils;
import wakeb.tech.drb.imagepiker.model.Image;

public class ImageFactory {

    public static List<wakeb.tech.drb.imagepiker.model.Image> singleListFromPath(String path) {
        List<wakeb.tech.drb.imagepiker.model.Image> images = new ArrayList<>();
        images.add(new Image(0, ImagePickerUtils.getNameFromFilePath(path), path));
        return images;
    }
}
