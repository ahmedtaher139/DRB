package wakeb.tech.drb.imagepiker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import wakeb.tech.drb.R;
import wakeb.tech.drb.imagepiker.features.imageloader.ImageLoader;
import wakeb.tech.drb.imagepiker.features.imageloader.ImageType;
import wakeb.tech.drb.imagepiker.helper.ImagePickerUtils;
import wakeb.tech.drb.imagepiker.listeners.OnImageClickListener;
import wakeb.tech.drb.imagepiker.listeners.OnImageSelectedListener;
import wakeb.tech.drb.imagepiker.model.Image;

public class ImagePickerAdapter extends BaseListAdapter<ImagePickerAdapter.ImageViewHolder> {

    private List<Image> images = new ArrayList<>();
    private List<Image> selectedImages = new ArrayList<>();

    private OnImageClickListener itemClickListener;
    private OnImageSelectedListener imageSelectedListener;

    public ImagePickerAdapter(Context context, ImageLoader imageLoader,
                              List<Image> selectedImages, OnImageClickListener itemClickListener) {
        super(context, imageLoader);
        this.itemClickListener = itemClickListener;

        if (selectedImages != null && !selectedImages.isEmpty()) {
            this.selectedImages.addAll(selectedImages);
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(
                getInflater().inflate(R.layout.ef_imagepicker_item_image, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {

        final Image image = images.get(position);
        final boolean isSelected = isSelected(image);

        getImageLoader().loadImage(
                image.getPath(),
                viewHolder.imageView,
                ImageType.GALLERY
        );

        boolean showFileTypeIndicator = false;
        String fileTypeLabel = "";
        if(ImagePickerUtils.isGifFormat(image)) {
            fileTypeLabel = getContext().getResources().getString(R.string.ef_gif);
            showFileTypeIndicator = true;
        }
        if(ImagePickerUtils.isVideoFormat(image)) {
            fileTypeLabel = getContext().getResources().getString(R.string.ef_video);
            showFileTypeIndicator = true;
        }
        viewHolder.fileTypeIndicator.setText(fileTypeLabel);
        viewHolder.fileTypeIndicator.setVisibility(showFileTypeIndicator
                ? View.VISIBLE
                : View.GONE);

        viewHolder.selected_layout.setBackground(isSelected
                ?  ContextCompat.getDrawable(getContext(), R.drawable.circle_green) : ContextCompat.getDrawable(getContext(), R.drawable.circle_grey));


        viewHolder.alphaView.setAlpha(isSelected
                ? 0.5f
                : 0f);

        viewHolder.selected_image_done.setVisibility(isSelected
                ? View.VISIBLE
                : View.GONE);

        viewHolder.itemView.setOnClickListener(v -> {
            boolean shouldSelect = itemClickListener.onImageClick(
                    isSelected
            );

            if (isSelected) {
                removeSelectedImage(image, position);
            } else if (shouldSelect) {
                addSelected(image, position);
            }
        });


    }

    private boolean isSelected(Image image) {
        for (Image selectedImage : selectedImages) {
            if (selectedImage.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public void setData(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    private void addSelected(final Image image, final int position) {
        mutateSelection(() -> {
            selectedImages.add(image);
            notifyItemChanged(position);
        });
    }

    private void removeSelectedImage(final Image image, final int position) {
        mutateSelection(() -> {
            selectedImages.remove(image);
            notifyItemChanged(position);
        });
    }

    public void removeAllSelectedSingleClick() {
        mutateSelection(() -> {
            selectedImages.clear();
            notifyDataSetChanged();
        });
    }

    private void mutateSelection(Runnable runnable) {
        runnable.run();
        if (imageSelectedListener != null) {
            imageSelectedListener.onSelectionUpdate(selectedImages);
        }
    }

    public void setImageSelectedListener(OnImageSelectedListener imageSelectedListener) {
        this.imageSelectedListener = imageSelectedListener;
    }

    public Image getItem(int position) {
        return images.get(position);
    }

    public List<Image> getSelectedImages() {
        return selectedImages;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView , selected_image_done;
        private View alphaView;
        private TextView fileTypeIndicator;
        private FrameLayout container;
        private RelativeLayout selected_layout;

        ImageViewHolder(View itemView) {
            super(itemView);

            container = (FrameLayout) itemView;
            imageView = itemView.findViewById(R.id.image_view);
            imageView = itemView.findViewById(R.id.image_view);
            alphaView = itemView.findViewById(R.id.view_alpha);
            fileTypeIndicator = itemView.findViewById(R.id.ef_item_file_type_indicator);
            selected_image_done = itemView.findViewById(R.id.selected_image_done);
            selected_layout = itemView.findViewById(R.id.selected_layout);
        }
    }

}
